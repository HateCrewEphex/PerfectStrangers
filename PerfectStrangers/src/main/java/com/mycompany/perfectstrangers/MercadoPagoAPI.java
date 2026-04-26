package com.mycompany.perfectstrangers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class MercadoPagoAPI {

    // 1. TUS CREDENCIALES EXACTAS
    private static final String ACCESS_TOKEN = "TU_ACCESS_TOKEN_AQUI"; // Reemplaza con tu Access Token de Mercado Pago. Es indispensable para que funcione la comunicación con la terminal.
    private static final String DEVICE_ID = "TU_DEVICE_ID_AQUI"; // El ID que acabas de asociar a tu caja en el panel de Mercado Pago o por endpoint. Sin este ID correcto, no funcionará nada.
    private static final String API_BASE = "https://api.mercadopago.com";
    private static final int POLLING_INTERVAL_MS = 3000;
    private static final int POLLING_TIMEOUT_MS = 180000;
    private static volatile String intentIdEnCurso;
    private String ultimoErrorUsuario;

    public static class ConfiguracionTerminalResultado {
        public boolean ok;
        public String storeId;
        public String posId;
        public String mensaje;
    }

    private static class HttpResult {
        int statusCode;
        String body;
    }

    public String getUltimoErrorUsuario() {
        return ultimoErrorUsuario;
    }

    /**
     * Método principal que la interfaz debe llamar.
     * Enciende la terminal y espera hasta que el cliente pague o cancele.
     */
    public boolean procesarCobroEnTerminal(double totalCobro, int idOrden) {
        ultimoErrorUsuario = null;
        System.out.println("1. Enviando cobro a la terminal " + DEVICE_ID + "...");
        
        // Paso A: Crear la intención de pago y despertar la pantalla
        String intentId = despertarTerminal(totalCobro, idOrden);
        
        if (intentId == null) {
            System.out.println("Error: No se pudo encender la terminal.");
            if (ultimoErrorUsuario == null || ultimoErrorUsuario.isBlank()) {
                ultimoErrorUsuario = "No fue posible iniciar el cobro en la terminal.";
            }
            return false;
        }

        System.out.println("Terminal encendida. ID de Cobro: " + intentId);
        System.out.println("2. Esperando a que el cliente pase la tarjeta...");

        // Paso B: Entrar en el ciclo de Polling (preguntar cada 3 segundos)
        return hacerPollingDePago(intentId);
    }

    /**
     * Hace la petición POST para encender la Point Smart 2
     */
    private String despertarTerminal(double totalCobro, int idOrden) {
        try {
            int amountCentavos = (int) Math.round(totalCobro * 100.0);
            if (amountCentavos < 500) {
                System.out.println("Error de validación MercadoPago (orden " + idOrden + "): el monto mínimo para terminal es $5.00 MXN.");
                return null;
            }

            String endpoint = API_BASE + "/point/integration-api/devices/" + DEVICE_ID + "/payment-intents";
            URL url = new URL(endpoint);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // La API Point espera amount en centavos (int), no en pesos decimales.
            String jsonInput = "{\"amount\": " + amountCentavos + "}";

            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int codigoRespuesta = conexion.getResponseCode();
            if (codigoRespuesta == 201 || codigoRespuesta == 200) {
                String respuestaJson = leerRespuestaServidor(conexion);
                // Extraemos el ID generado usando nuestro método auxiliar
                String intentId = extraerValorJson(respuestaJson, "id");
                if (intentId != null && !intentId.isBlank()) {
                    intentIdEnCurso = intentId;
                }
                return intentId;
            } else if (codigoRespuesta == 409) {
                String respuestaError = leerRespuestaServidor(conexion);
                String mensaje = extraerValorJson(respuestaError, "message");
                System.out.println("Error de API MercadoPago (orden " + idOrden + "): " + respuestaError);

                if (mensaje != null && mensaje.toLowerCase().contains("queued intent") && intentIdEnCurso != null && !intentIdEnCurso.isBlank()) {
                    System.out.println("Ya existe una intención en cola. Reutilizando intento en curso: " + intentIdEnCurso);
                    return intentIdEnCurso;
                }

                ultimoErrorUsuario = "Ya existe un cobro pendiente en la terminal. Complétalo o cancélalo en el dispositivo e inténtalo de nuevo.";
                return null;
            } else {
                String respuestaError = leerRespuestaServidor(conexion);
                System.out.println("Error de API MercadoPago (orden " + idOrden + "): " + respuestaError);
                String mensaje = extraerValorJson(respuestaError, "message");
                if (mensaje != null && !mensaje.isBlank()) {
                    ultimoErrorUsuario = "Mercado Pago respondió: " + mensaje;
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ultimoErrorUsuario = "Error de conexión con la terminal de Mercado Pago.";
            return null;
        }
    }

    /**
     * Hace peticiones GET en bucle hasta que la terminal reporte un estado final
     */
    private boolean hacerPollingDePago(String intentId) {
        try {
            String endpoint = API_BASE + "/point/integration-api/payment-intents/" + intentId;
            URL url = new URL(endpoint);
            long inicio = System.currentTimeMillis();

            while (true) {
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");
                conexion.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);

                int codigo = conexion.getResponseCode();
                if (codigo == 200) {
                    String respuestaJson = leerRespuestaServidor(conexion);
                    String estadoPago = extraerValorJson(respuestaJson, "state").toUpperCase();

                    System.out.println("Revisando terminal... Estado actual: " + estadoPago);

                    // Evaluamos la respuesta de la terminal
                    if (estadoPago.equals("FINISHED") || estadoPago.equals("APPROVED") || estadoPago.equals("SUCCESS")) {
                        System.out.println("¡PAGO APROBADO EXITOSAMENTE!");
                        limpiarIntentEnCurso(intentId);
                        return true;
                    } else if (estadoPago.equals("CANCELED") || estadoPago.equals("CANCELLED") || estadoPago.equals("ERROR") || estadoPago.equals("FAILED") || estadoPago.equals("EXPIRED")) {
                        System.out.println("EL PAGO FUE CANCELADO O RECHAZADO.");
                        limpiarIntentEnCurso(intentId);
                        return false;
                    }
                    // Si el estado es "OPEN", significa que el cliente aún no pone el NIP. 
                    // El ciclo continuará.
                } else {
                    System.out.println("Error consultando estado en MercadoPago: " + leerRespuestaServidor(conexion));
                    return false;
                }

                long transcurrido = System.currentTimeMillis() - inicio;
                if (transcurrido >= POLLING_TIMEOUT_MS) {
                    System.out.println("Tiempo de espera agotado en terminal. No se confirmó el cobro en 180 segundos.");
                    ultimoErrorUsuario = "La terminal no confirmó el pago a tiempo. Si sigue abierta en la terminal, termina o cancela ahí y vuelve a intentar.";
                    return false;
                }

                // Pausamos el programa 3 segundos antes de volver a preguntar para no saturar el servidor
                Thread.sleep(POLLING_INTERVAL_MS); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Polling interrumpido.");
            ultimoErrorUsuario = "La espera de respuesta de la terminal fue interrumpida.";
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            ultimoErrorUsuario = "Error al consultar el estado del cobro en Mercado Pago.";
            return false;
        }
    }

    private void limpiarIntentEnCurso(String intentId) {
        if (intentId != null && intentId.equals(intentIdEnCurso)) {
            intentIdEnCurso = null;
        }
    }

    /**
     * Configuración inicial (una sola vez): crea sucursal y caja (POS).
     * Luego puedes asociar la terminal desde el panel de MP o por endpoint si tu cuenta lo habilita.
     */
    public ConfiguracionTerminalResultado configurarSucursalYCaja(
        String userId,
        String existingStoreId,
        String existingPosId,
        String nombreSucursal,
        String externalIdSucursal,
        String cityName,
        String stateName,
        String streetName,
        double latitud,
        double longitud,
        String nombreCaja,
        String externalIdCaja
    ) {
        ConfiguracionTerminalResultado resultado = new ConfiguracionTerminalResultado();
        try {
            if (userId == null || userId.isBlank()) {
                resultado.ok = false;
                resultado.mensaje = "user_id es obligatorio.";
                return resultado;
            }
            if (nombreSucursal == null || nombreSucursal.isBlank()) {
                resultado.ok = false;
                resultado.mensaje = "El nombre de la sucursal es obligatorio.";
                return resultado;
            }
            if (nombreCaja == null || nombreCaja.isBlank()) {
                resultado.ok = false;
                resultado.mensaje = "El nombre de la caja (POS) es obligatorio.";
                return resultado;
            }

            boolean usarSucursalExistente = existingStoreId != null && !existingStoreId.isBlank();
            boolean usarPosExistente = existingPosId != null && !existingPosId.isBlank();
            String externalSucursalFinal = normalizarExternalId(externalIdSucursal, "SUCPS");
            String externalPosFinal = normalizarExternalId(externalIdCaja, "POSPS");

            String storeId = existingStoreId;
            if (!usarSucursalExistente) {
                if (cityName == null || cityName.isBlank()) {
                    resultado.ok = false;
                    resultado.mensaje = "city_name es obligatorio para crear sucursal.";
                    return resultado;
                }
                if (stateName == null || stateName.isBlank()) {
                    resultado.ok = false;
                    resultado.mensaje = "state_name es obligatorio para crear sucursal.";
                    return resultado;
                }
                if (streetName == null || streetName.isBlank()) {
                    resultado.ok = false;
                    resultado.mensaje = "street_name es obligatorio para crear sucursal.";
                    return resultado;
                }

                String storePayload = "{"
                    + "\"name\":\"" + escaparJson(nombreSucursal) + "\"," 
                    + "\"external_id\":\"" + escaparJson(externalSucursalFinal) + "\"," 
                    + "\"location\":{"
                    + "\"latitude\":" + String.format(Locale.US, "%.8f", latitud) + ","
                    + "\"longitude\":" + String.format(Locale.US, "%.8f", longitud) + ","
                    + "\"city_name\":\"" + escaparJson(cityName) + "\"," 
                    + "\"state_name\":\"" + escaparJson(stateName) + "\"," 
                    + "\"street_name\":\"" + escaparJson(streetName) + "\""
                    + "}"
                    + "}";

                HttpResult storeResp = ejecutarJson("POST", API_BASE + "/users/" + userId + "/stores", storePayload);
                if (storeResp.statusCode != 201 && storeResp.statusCode != 200) {
                    resultado.ok = false;
                    if (storeResp.body != null && storeResp.body.contains("external id") && storeResp.body.contains("already assigned")) {
                        resultado.mensaje = "Ya existe una sucursal con ese external_id (" + externalSucursalFinal + "). Usa un external_id distinto o ejecuta el asistente en modo 'ya existe'.";
                        return resultado;
                    }
                    resultado.mensaje = "No se pudo crear sucursal: " + storeResp.body;
                    return resultado;
                }

                storeId = extraerValorJson(storeResp.body, "id");
                if (storeId == null || storeId.isBlank()) {
                    resultado.ok = false;
                    resultado.mensaje = "Sucursal creada pero no se pudo leer su id. Respuesta: " + storeResp.body;
                    return resultado;
                }
            }

            String posId = existingPosId;
            if (!usarPosExistente) {
                String posPayload = "{"
                    + "\"name\":\"" + escaparJson(nombreCaja) + "\"," 
                    + "\"fixed_amount\":false,"
                    + "\"store_id\":\"" + escaparJson(storeId) + "\"," 
                    + "\"external_id\":\"" + escaparJson(externalPosFinal) + "\""
                    + "}";

                HttpResult posResp = ejecutarJson("POST", API_BASE + "/pos", posPayload);
                if (posResp.statusCode != 201 && posResp.statusCode != 200) {
                    resultado.ok = false;
                    resultado.storeId = storeId;
                    if (posResp.body != null && posResp.body.contains("external id") && posResp.body.contains("already assigned")) {
                        resultado.mensaje = "Ya existe una caja/POS con ese external_id (" + externalPosFinal + "). Usa un external_id distinto o captura el POS existente.";
                        return resultado;
                    }
                    resultado.mensaje = "Sucursal creada, pero no se pudo crear POS: " + posResp.body;
                    return resultado;
                }

                posId = extraerValorJson(posResp.body, "id");
            }

            resultado.ok = true;
            resultado.storeId = storeId;
            resultado.posId = posId;
            resultado.mensaje = (usarSucursalExistente ? "Sucursal existente detectada. " : "Sucursal creada. ")
                + (usarPosExistente ? "POS existente detectado. " : "POS creado. ")
                + "Siguiente paso: asociar la terminal " + DEVICE_ID + " a la caja/POS y activar modo PDV (puedes usar activarModoPDV()).";
            return resultado;
        } catch (Exception e) {
            resultado.ok = false;
            resultado.mensaje = "Error al configurar sucursal/caja: " + e.getMessage();
            return resultado;
        }
    }

    /**
     * Devuelve el JSON de terminales del vendedor para validar device_id disponible.
     */
    public String listarTerminalesVendedor() {
        return listarTerminalesVendedor(null, null);
    }

    /**
     * Lista terminales Point activas con filtro opcional por sucursal/caja.
     */
    public String listarTerminalesVendedor(String storeId, String posId) {
        try {
            StringBuilder endpoint = new StringBuilder(API_BASE + "/terminals/v1/list?limit=50");
            if (storeId != null && !storeId.isBlank()) {
                endpoint.append("&store_id=")
                    .append(URLEncoder.encode(storeId, StandardCharsets.UTF_8));
            }
            if (posId != null && !posId.isBlank()) {
                endpoint.append("&pos_id=")
                    .append(URLEncoder.encode(posId, StandardCharsets.UTF_8));
            }

            HttpResult resp = ejecutarJson("GET", endpoint.toString(), null);
            if (resp.statusCode == 200) {
                return resp.body;
            }
            if (resp.statusCode == 404) {
                return "No se encontraron terminales para los filtros indicados. Verifica la asociación terminal/sucursal/caja en Mercado Pago.";
            }
            return "Error al listar terminales (" + resp.statusCode + "): " + resp.body;
        } catch (Exception e) {
            return "Error al listar terminales: " + e.getMessage();
        }
    }

    /**
     * Atajo para activar la terminal actual en modo PDV.
     */
    public String activarModoPDV() {
        return activarModoOperacionTerminal("PDV");
    }

    /**
     * Cambia modo de operación de la terminal (PDV o STANDALONE).
     */
    public String activarModoOperacionTerminal(String modoOperacion) {
        try {
            String modo = modoOperacion == null ? "" : modoOperacion.trim().toUpperCase(Locale.ROOT);
            if (!"PDV".equals(modo) && !"STANDALONE".equals(modo)) {
                return "Modo inválido. Usa PDV o STANDALONE.";
            }

            String endpoint = API_BASE + "/point/integration-api/devices/" + DEVICE_ID;
            String payload = "{\"operating_mode\":\"" + modo + "\"}";
            HttpResult resp = ejecutarJson("PATCH", endpoint, payload);

            if (resp.statusCode == 200) {
                return "Modo de operación actualizado correctamente: " + resp.body;
            }

            if (resp.statusCode == 403 && resp.body != null && resp.body.contains("store_pos_not_found")) {
                return "La terminal aún no tiene sucursal/caja asociada. Primero vincúlala en Mercado Pago y vuelve a intentar. Detalle: " + resp.body;
            }

            if (resp.statusCode == 412) {
                return "No se pudo activar PDV porque la caja ya tiene otra terminal en PDV/suspendida. Detalle: " + resp.body;
            }

            return "No se pudo actualizar el modo de operación (" + resp.statusCode + "): " + resp.body;
        } catch (Exception e) {
            return "Error al cambiar modo de operación: " + e.getMessage();
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES (HERRAMIENTAS)
    // ==========================================

    private String leerRespuestaServidor(HttpURLConnection conexion) throws Exception {
        BufferedReader in;
        if (conexion.getResponseCode() >= 200 && conexion.getResponseCode() <= 299) {
            in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
        }
        StringBuilder respuesta = new StringBuilder();
        String linea;
        while ((linea = in.readLine()) != null) {
            respuesta.append(linea);
        }
        in.close();
        return respuesta.toString();
    }

    private HttpResult ejecutarJson(String method, String endpoint, String jsonBody) throws Exception {
        if ("PATCH".equalsIgnoreCase(method)) {
            return ejecutarJsonConHttpClient(method, endpoint, jsonBody);
        }

        URL url = new URL(endpoint);
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod(method);
        conexion.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
        conexion.setRequestProperty("Content-Type", "application/json");

        if (jsonBody != null) {
            conexion.setDoOutput(true);
            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        HttpResult result = new HttpResult();
        result.statusCode = conexion.getResponseCode();
        result.body = leerRespuestaServidor(conexion);
        return result;
    }

    private HttpResult ejecutarJsonConHttpClient(String method, String endpoint, String jsonBody) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .header("Authorization", "Bearer " + ACCESS_TOKEN)
            .header("Content-Type", "application/json");

        if (jsonBody == null) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));
        }

        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResult result = new HttpResult();
        result.statusCode = response.statusCode();
        result.body = response.body() == null ? "" : response.body();
        return result;
    }

    private String escaparJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String valorPorDefecto(String valor, String fallback) {
        return (valor == null || valor.isBlank()) ? fallback : valor;
    }

    private String normalizarExternalId(String valor, String prefijo) {
        if (valor != null && !valor.isBlank()) {
            String limpio = valor.replaceAll("[^A-Za-z0-9]", "").trim();
            if (!limpio.isBlank()) {
                return limpio;
            }
        }
        return prefijo + System.currentTimeMillis();
    }

    /**
     * Truco de ingeniería para extraer datos de un JSON sin usar librerías extra.
     * Busca la clave solicitada y recorta el valor que está entre comillas.
     */
    private String extraerValorJson(String json, String clave) {
        try {
            String busqueda = "\"" + clave + "\":";
            int inicioClave = json.indexOf(busqueda);
            if (inicioClave == -1) return "";

            int cursor = inicioClave + busqueda.length();
            while (cursor < json.length() && Character.isWhitespace(json.charAt(cursor))) {
                cursor++;
            }
            if (cursor >= json.length()) return "";

            if (json.charAt(cursor) == '"') {
                int inicioValor = cursor + 1;
                int finValor = json.indexOf("\"", inicioValor);
                if (finValor == -1) return "";
                return json.substring(inicioValor, finValor);
            }

            int finValor = cursor;
            while (finValor < json.length()) {
                char c = json.charAt(finValor);
                if (c == ',' || c == '}' || Character.isWhitespace(c)) {
                    break;
                }
                finValor++;
            }

            return json.substring(cursor, finValor);
        } catch (Exception e) {
            return "";
        }
    }
}