package com.mycompany.perfectstrangers;

import java.util.Scanner;

public class MPPointSetupTerminal {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            MercadoPagoAPI api = new MercadoPagoAPI();

            System.out.println("=== CONFIGURACION INICIAL MP POINT ===");
            System.out.println("Este proceso se ejecuta una sola vez por sucursal/caja.");

            System.out.print("¿Ya tienes sucursal y POS creados? (si/no): ");
            String usarExistentes = scanner.nextLine().trim().toLowerCase();
            boolean modoExistente = usarExistentes.startsWith("s");

            System.out.print("user_id de Mercado Pago: ");
            String userId = scanner.nextLine().trim();

            String existingStoreId = "";
            String existingPosId = "";
            String cityName = "";
            String stateName = "";
            String streetName = "";
            String nombreSucursal;
            String externalIdSucursal;
            double latitud;
            double longitud;
            String nombreCaja;
            String externalIdCaja;

            if (modoExistente) {
                System.out.print("ID de sucursal existente: ");
                existingStoreId = scanner.nextLine().trim();

                System.out.print("ID de POS existente: ");
                existingPosId = scanner.nextLine().trim();

                nombreSucursal = "Sucursal existente";
                externalIdSucursal = "";
                latitud = 0.0;
                longitud = 0.0;
                nombreCaja = "POS existente";
                externalIdCaja = "";
            } else {
                System.out.print("Nombre de sucursal (ej. Perfect Strangers Centro): ");
                nombreSucursal = scanner.nextLine().trim();

                System.out.print("external_id de sucursal (ej. SUC-PS-CENTRO, opcional): ");
                externalIdSucursal = scanner.nextLine().trim();

                System.out.print("Ciudad: ");
                cityName = scanner.nextLine().trim();

                System.out.print("Estado: ");
                stateName = scanner.nextLine().trim();

                System.out.print("Calle: ");
                streetName = scanner.nextLine().trim();

                System.out.print("Latitud de sucursal (ej. 19.4326077): ");
                latitud = parseDoubleSeguro(scanner.nextLine().trim(), 19.4326077);

                System.out.print("Longitud de sucursal (ej. -99.133208): ");
                longitud = parseDoubleSeguro(scanner.nextLine().trim(), -99.1332080);

                System.out.print("Nombre de caja/POS (ej. Caja 1): ");
                nombreCaja = scanner.nextLine().trim();

                System.out.print("external_id de caja/POS (ej. POS-PS-01, opcional): ");
                externalIdCaja = scanner.nextLine().trim();
            }

            MercadoPagoAPI.ConfiguracionTerminalResultado resultado = api.configurarSucursalYCaja(
                userId,
                existingStoreId,
                existingPosId,
                nombreSucursal,
                externalIdSucursal,
                cityName,
                stateName,
                streetName,
                latitud,
                longitud,
                nombreCaja,
                externalIdCaja
            );

            System.out.println();
            if (resultado.ok) {
                System.out.println("OK: " + resultado.mensaje);
                System.out.println("store_id: " + resultado.storeId);
                System.out.println("pos_id: " + resultado.posId);
            } else {
                System.out.println("ERROR: " + resultado.mensaje);
            }

            System.out.println();
            System.out.println("=== TERMINALES DISPONIBLES EN LA CUENTA ===");
            System.out.println(api.listarTerminalesVendedor());

            System.out.println();
            System.out.println("Siguiente paso: en Mercado Pago, asocia la terminal fisica a la caja/POS creada y activa modo PDV.");
        }
    }

    private static double parseDoubleSeguro(String text, double fallback) {
        try {
            return Double.parseDouble(text.replace(',', '.'));
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
}
