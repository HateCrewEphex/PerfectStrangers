import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuscadorTerminal {

    // ¡Pon tu Token de prueba aquí!
    private static final String ACCESS_TOKEN = "APP_USR-271925585344741-042602-4f0610d0a17bac3b7e7f6f6d238038b9-3360258074";

    public static void main(String[] args) {
        try {
            // La ruta de Mercado Pago para listar dispositivos
            URL url = new URL("https://api.mercadopago.com/point/integration-api/devices");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            
            // Configuramos la petición GET
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);

            int respuesta = conexion.getResponseCode();
            
            if (respuesta == 200) {
                // Leemos lo que nos responde la nube
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea;
                StringBuilder respuestaJson = new StringBuilder();
                
                while ((linea = in.readLine()) != null) {
                    respuestaJson.append(linea);
                }
                in.close();
                
                // Imprimimos la respuesta en la consola de NetBeans
                System.out.println("====== TERMINALES ENCONTRADAS ======");
                System.out.println(respuestaJson.toString());
                System.out.println("====================================");
                System.out.println("Busca la parte que dice \"id\": \"EL_ID_ESTA_AQUI\"");
                
            } else {
                System.out.println("Error de conexión. Código: " + respuesta);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}