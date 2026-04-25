package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db_PerfectStrangers?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static boolean detalleOrdenEsquemaVerificado = false;

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static synchronized void asegurarEstadoDetalleOrden() throws SQLException {
        if (detalleOrdenEsquemaVerificado) {
            return;
        }

        try (Connection con = getConnection();
             java.sql.Statement st = con.createStatement()) {
            try {
                st.executeUpdate("ALTER TABLE detalle_orden ADD COLUMN estado_detalle VARCHAR(20) NOT NULL DEFAULT 'Pendiente'");
            } catch (SQLException ex) {
                if (ex.getErrorCode() != 1060) {
                    throw ex;
                }
            }

            st.executeUpdate(
                "UPDATE detalle_orden d " +
                "INNER JOIN ordenes o ON d.id_orden = o.id_orden " +
                "SET d.estado_detalle = 'Entregada' " +
                "WHERE o.estado_preparacion = 'Entregada' AND (d.estado_detalle IS NULL OR d.estado_detalle = '' OR d.estado_detalle = 'Pendiente')"
            );
        }

        detalleOrdenEsquemaVerificado = true;
    }
}
