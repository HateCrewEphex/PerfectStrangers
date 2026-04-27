package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PromocionDAO {

    public static void crearPromocion(Promocion promo) throws SQLException {
        String sql = "INSERT INTO promociones (nombre_promo, tipo_descuento, valor_descuento, " +
                     "fecha_inicio, fecha_fin, id_producto_afectado, estado_promo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, promo.getNombrePromo());
            stmt.setString(2, promo.getTipoDescuento());
            stmt.setDouble(3, promo.getValorDescuento());
            stmt.setString(4, promo.getFechaInicio());
            stmt.setString(5, promo.getFechaFin());
            if (promo.getIdProductoAfectado() != null) {
                stmt.setInt(6, promo.getIdProductoAfectado());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setBoolean(7, promo.isEstadoPromo());
            stmt.executeUpdate();
        }
    }

    public static Promocion obtenerPromocionById(int idPromocion) throws SQLException {
        String sql = "SELECT p.*, pr.nombre AS nombre_producto FROM promociones p " +
                     "LEFT JOIN productos pr ON p.id_producto_afectado = pr.id_producto " +
                     "WHERE p.id_promocion = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPromocion);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPromocion(rs);
            }
        }
        return null;
    }

    public static List<Promocion> obtenerTodasPromociones() throws SQLException {
        String sql = "SELECT p.*, pr.nombre AS nombre_producto FROM promociones p " +
                     "LEFT JOIN productos pr ON p.id_producto_afectado = pr.id_producto " +
                     "ORDER BY p.fecha_inicio DESC";
        
        List<Promocion> promociones = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                promociones.add(mapResultSetToPromocion(rs));
            }
        }
        return promociones;
    }

    public static List<Promocion> obtenerPromcionesActivas() throws SQLException {
        String sql = "SELECT p.*, pr.nombre AS nombre_producto FROM promociones p " +
                     "LEFT JOIN productos pr ON p.id_producto_afectado = pr.id_producto " +
                     "WHERE p.estado_promo = TRUE " +
                     "AND NOW() BETWEEN p.fecha_inicio AND p.fecha_fin " +
                     "ORDER BY p.nombre_promo";
        
        List<Promocion> promociones = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                promociones.add(mapResultSetToPromocion(rs));
            }
        }
        return promociones;
    }

    public static List<Promocion> obtenerPromcionesActivasPorProducto(int idProducto) throws SQLException {
        String sql = "SELECT p.*, pr.nombre AS nombre_producto FROM promociones p " +
                     "LEFT JOIN productos pr ON p.id_producto_afectado = pr.id_producto " +
                     "WHERE p.estado_promo = TRUE " +
                     "AND NOW() BETWEEN p.fecha_inicio AND p.fecha_fin " +
                     "AND (p.id_producto_afectado = ? OR p.id_producto_afectado IS NULL) " +
                     "ORDER BY p.nombre_promo";
        
        List<Promocion> promociones = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                promociones.add(mapResultSetToPromocion(rs));
            }
        }
        return promociones;
    }

    public static void actualizarPromocion(Promocion promo) throws SQLException {
        String sql = "UPDATE promociones SET nombre_promo = ?, tipo_descuento = ?, " +
                     "valor_descuento = ?, fecha_inicio = ?, fecha_fin = ?, " +
                     "id_producto_afectado = ?, estado_promo = ? WHERE id_promocion = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, promo.getNombrePromo());
            stmt.setString(2, promo.getTipoDescuento());
            stmt.setDouble(3, promo.getValorDescuento());
            stmt.setString(4, promo.getFechaInicio());
            stmt.setString(5, promo.getFechaFin());
            if (promo.getIdProductoAfectado() != null) {
                stmt.setInt(6, promo.getIdProductoAfectado());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setBoolean(7, promo.isEstadoPromo());
            stmt.setInt(8, promo.getIdPromocion());
            stmt.executeUpdate();
        }
    }

    public static void desactivarPromocion(int idPromocion) throws SQLException {
        String sql = "UPDATE promociones SET estado_promo = FALSE WHERE id_promocion = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPromocion);
            stmt.executeUpdate();
        }
    }

    public static void eliminarPromocion(int idPromocion) throws SQLException {
        String sql = "DELETE FROM promociones WHERE id_promocion = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPromocion);
            stmt.executeUpdate();
        }
    }

    private static Promocion mapResultSetToPromocion(ResultSet rs) throws SQLException {
        Promocion promo = new Promocion();
        promo.setIdPromocion(rs.getInt("id_promocion"));
        promo.setNombrePromo(rs.getString("nombre_promo"));
        promo.setTipoDescuento(rs.getString("tipo_descuento"));
        promo.setValorDescuento(rs.getDouble("valor_descuento"));
        promo.setFechaInicio(rs.getString("fecha_inicio"));
        promo.setFechaFin(rs.getString("fecha_fin"));
        int idProd = rs.getInt("id_producto_afectado");
        promo.setIdProductoAfectado(rs.wasNull() ? null : idProd);
        promo.setEstadoPromo(rs.getBoolean("estado_promo"));
        promo.setNombreProducto(rs.getString("nombre_producto"));
        return promo;
    }
}
