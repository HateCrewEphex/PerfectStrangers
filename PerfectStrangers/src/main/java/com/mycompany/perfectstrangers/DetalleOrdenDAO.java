package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DetalleOrdenDAO {

    public static void crearDetalleOrden(DetalleOrden detalle) throws SQLException {
        String sql = "INSERT INTO detalle_orden (id_orden, id_producto, cantidad, precio_unitario, notas_especiales) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detalle.getIdOrden());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecioUnitario());
            stmt.setString(5, detalle.getNotasEspeciales());
            stmt.executeUpdate();
            
            // Obtener el ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setIdDetalle(rs.getInt(1));
                }
            }
        }
    }

    public static DetalleOrden obtenerDetalleById(int idDetalle) throws SQLException {
        String sql = "SELECT d.*, p.nombre FROM detalle_orden d " +
                     "JOIN productos p ON d.id_producto = p.id_producto " +
                     "WHERE d.id_detalle = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idDetalle);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToDetalleOrden(rs);
            }
        }
        return null;
    }

    public static List<DetalleOrden> obtenerDetallesPorOrden(int idOrden) throws SQLException {
        String sql = "SELECT d.*, p.nombre FROM detalle_orden d " +
                     "JOIN productos p ON d.id_producto = p.id_producto " +
                     "WHERE d.id_orden = ? " +
                     "ORDER BY d.id_detalle";
        
        List<DetalleOrden> detalles = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapResultSetToDetalleOrden(rs));
            }
        }
        return detalles;
    }

    public static double calcularTotalOrden(int idOrden) throws SQLException {
        String sql = "SELECT SUM(cantidad * precio_unitario) as total FROM detalle_orden WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }

    public static void actualizarDetalleOrden(DetalleOrden detalle) throws SQLException {
        String sql = "UPDATE detalle_orden SET id_producto = ?, cantidad = ?, precio_unitario = ?, " +
                     "notas_especiales = ? WHERE id_detalle = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getIdProducto());
            stmt.setInt(2, detalle.getCantidad());
            stmt.setDouble(3, detalle.getPrecioUnitario());
            stmt.setString(4, detalle.getNotasEspeciales());
            stmt.setInt(5, detalle.getIdDetalle());
            stmt.executeUpdate();
        }
    }

    public static void eliminarDetalleOrden(int idDetalle) throws SQLException {
        String sql = "DELETE FROM detalle_orden WHERE id_detalle = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idDetalle);
            stmt.executeUpdate();
        }
    }

    public static void eliminarDetallesPorOrden(int idOrden) throws SQLException {
        String sql = "DELETE FROM detalle_orden WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            stmt.executeUpdate();
        }
    }

    private static DetalleOrden mapResultSetToDetalleOrden(ResultSet rs) throws SQLException {
        DetalleOrden detalle = new DetalleOrden();
        detalle.setIdDetalle(rs.getInt("id_detalle"));
        detalle.setIdOrden(rs.getInt("id_orden"));
        detalle.setIdProducto(rs.getInt("id_producto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
        detalle.setNotasEspeciales(rs.getString("notas_especiales"));
        detalle.setNombreProducto(rs.getString("nombre"));
        return detalle;
    }
}
