package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    public static void crearPago(Pago pago) throws SQLException {
        String sql = "INSERT INTO pagos (id_orden, monto_pagado, metodo_pago, estado_pago, referencia_externa) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pago.getIdOrden());
            stmt.setDouble(2, pago.getMontoPagado());
            stmt.setString(3, pago.getMetodoPago());
            stmt.setString(4, pago.getEstadoPago() != null ? pago.getEstadoPago() : "Completado");
            stmt.setString(5, pago.getReferenciaExterna());
            stmt.executeUpdate();
            
            // Obtener el ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pago.setIdPago(rs.getInt(1));
                }
            }
        }
    }

    public static Pago obtenerPagoById(int idPago) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE id_pago = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPago);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPago(rs);
            }
        }
        return null;
    }

    public static Pago obtenerPagoPorOrden(int idOrden) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE id_orden = ? ORDER BY fecha_pago DESC LIMIT 1";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPago(rs);
            }
        }
        return null;
    }

    public static List<Pago> obtenerTodosPagos() throws SQLException {
        String sql = "SELECT * FROM pagos ORDER BY fecha_pago DESC";
        List<Pago> pagos = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        }
        return pagos;
    }

    public static List<Pago> obtenerPagosPorMetodo(String metodoPago) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE metodo_pago = ? ORDER BY fecha_pago DESC";
        List<Pago> pagos = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, metodoPago);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pagos.add(mapResultSetToPago(rs));
            }
        }
        return pagos;
    }

    public static double obtenerIngresoTotal() throws SQLException {
        String sql = "SELECT SUM(monto_pagado) as total FROM pagos";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }

    public static void actualizarPago(Pago pago) throws SQLException {
        String sql = "UPDATE pagos SET id_orden = ?, monto_pagado = ?, metodo_pago = ?, estado_pago = ?, referencia_externa = ? " +
                     "WHERE id_pago = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, pago.getIdOrden());
            stmt.setDouble(2, pago.getMontoPagado());
            stmt.setString(3, pago.getMetodoPago());
            stmt.setString(4, pago.getEstadoPago() != null ? pago.getEstadoPago() : "Completado");
            stmt.setString(5, pago.getReferenciaExterna());
            stmt.setInt(6, pago.getIdPago());
            stmt.executeUpdate();
        }
    }

    public static void eliminarPago(int idPago) throws SQLException {
        String sql = "DELETE FROM pagos WHERE id_pago = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPago);
            stmt.executeUpdate();
        }
    }

    private static Pago mapResultSetToPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();
        pago.setIdPago(rs.getInt("id_pago"));
        pago.setIdOrden(rs.getInt("id_orden"));
        pago.setMontoPagado(rs.getDouble("monto_pagado"));
        pago.setMetodoPago(rs.getString("metodo_pago"));
        pago.setEstadoPago(rs.getString("estado_pago"));
        pago.setFechaPago(rs.getTimestamp("fecha_pago"));
        pago.setReferenciaExterna(rs.getString("referencia_externa"));
        return pago;
    }
}
