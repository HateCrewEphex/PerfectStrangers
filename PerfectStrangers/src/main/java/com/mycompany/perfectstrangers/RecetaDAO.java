package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO {

    public static void crearReceta(Receta receta) throws SQLException {
        String sqlReceta = "INSERT INTO recetas (id_producto, descripcion) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlReceta, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, receta.getIdProducto());
            stmt.setString(2, receta.getDescripcionReceta() != null ? receta.getDescripcionReceta() : "");
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    receta.setIdReceta(rs.getInt(1));
                }
            }
        }

        if (receta.getIdInsumo() > 0 && receta.getCantidadRequerida() > 0) {
            String sqlDetalle = "INSERT INTO detalles_receta (id_receta, id_ingrediente, cantidad) VALUES (?, ?, ?)";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sqlDetalle)) {
                stmt.setInt(1, receta.getIdReceta());
                stmt.setInt(2, receta.getIdInsumo());
                stmt.setDouble(3, receta.getCantidadRequerida());
                stmt.executeUpdate();
            }
        }
    }

    public static Receta obtenerRecetaById(int idReceta) throws SQLException {
        String sql = "SELECT r.id_receta, r.id_producto, r.descripcion AS descripcion_receta, " +
                     "d.id_ingrediente, d.cantidad, p.nombre AS nombre_producto, i.nombre AS nombre_insumo, i.medicion " +
                     "FROM recetas r " +
                     "LEFT JOIN detalles_receta d ON r.id_receta = d.id_receta " +
                     "LEFT JOIN productos p ON r.id_producto = p.id_producto " +
                     "LEFT JOIN inventario i ON d.id_ingrediente = i.id_ingrediente " +
                     "WHERE r.id_receta = ? LIMIT 1";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idReceta);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReceta(rs);
            }
        }
        return null;
    }

    public static List<Receta> obtenerRecetasPorProducto(int idProducto) throws SQLException {
        String sql = "SELECT r.id_receta, r.id_producto, r.descripcion AS descripcion_receta, " +
                     "d.id_ingrediente, d.cantidad, p.nombre AS nombre_producto, i.nombre AS nombre_insumo, i.medicion " +
                     "FROM recetas r " +
                     "JOIN detalles_receta d ON r.id_receta = d.id_receta " +
                     "JOIN productos p ON r.id_producto = p.id_producto " +
                     "JOIN inventario i ON d.id_ingrediente = i.id_ingrediente " +
                     "WHERE r.id_producto = ?";
        
        List<Receta> recetas = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                recetas.add(mapResultSetToReceta(rs));
            }
        }
        return recetas;
    }

    public static List<Receta> obtenerTodasRecetas() throws SQLException {
        String sql = "SELECT r.id_receta, r.id_producto, r.descripcion AS descripcion_receta, " +
                     "d.id_ingrediente, d.cantidad, p.nombre AS nombre_producto, i.nombre AS nombre_insumo, i.medicion " +
                     "FROM recetas r " +
                     "JOIN detalles_receta d ON r.id_receta = d.id_receta " +
                     "JOIN productos p ON r.id_producto = p.id_producto " +
                     "JOIN inventario i ON d.id_ingrediente = i.id_ingrediente " +
                     "ORDER BY p.nombre, i.nombre";
        
        List<Receta> recetas = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                recetas.add(mapResultSetToReceta(rs));
            }
        }
        return recetas;
    }

    /**
     * Verifica si hay suficientes insumos para preparar un producto
     */
    public static boolean verificarDisponibilidad(int idProducto, int cantidad) throws SQLException {
        List<Receta> recetas = obtenerRecetasPorProducto(idProducto);
        
        for (Receta receta : recetas) {
            Insumo insumo = InsumoDAO.obtenerInsumoById(receta.getIdInsumo());
            if (insumo == null || insumo.getCantidadActual() < (receta.getCantidadRequerida() * cantidad)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Consume los insumos necesarios para preparar un producto
     */
    public static void consumirInsumos(int idProducto, int cantidad) throws SQLException {
        List<Receta> recetas = obtenerRecetasPorProducto(idProducto);
        
        for (Receta receta : recetas) {
            double cantidadAConsumir = receta.getCantidadRequerida() * cantidad;
            InsumoDAO.decrementarCantidad(receta.getIdInsumo(), cantidadAConsumir);
        }
    }

    /**
     * Devuelve los insumos (para órdenes canceladas)
     */
    public static void devolverInsumos(int idProducto, int cantidad) throws SQLException {
        List<Receta> recetas = obtenerRecetasPorProducto(idProducto);
        
        for (Receta receta : recetas) {
            double cantidadADevolver = receta.getCantidadRequerida() * cantidad;
            InsumoDAO.incrementarCantidad(receta.getIdInsumo(), cantidadADevolver);
        }
    }

    public static void actualizarReceta(Receta receta) throws SQLException {
        String sql = "UPDATE recetas SET id_producto = ?, id_insumo = ?, cantidad_requerida = ? WHERE id_receta = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, receta.getIdProducto());
            stmt.setInt(2, receta.getIdInsumo());
            stmt.setDouble(3, receta.getCantidadRequerida());
            stmt.setInt(4, receta.getIdReceta());
            stmt.executeUpdate();
        }
    }

    public static void eliminarReceta(int idReceta) throws SQLException {
        String sql = "DELETE FROM recetas WHERE id_receta = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idReceta);
            stmt.executeUpdate();
        }
    }

    public static void eliminarRecetasPorProducto(int idProducto) throws SQLException {
        String sql = "DELETE FROM recetas WHERE id_producto = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
        }
    }

    private static Receta mapResultSetToReceta(ResultSet rs) throws SQLException {
        Receta receta = new Receta();
        receta.setIdReceta(rs.getInt("id_receta"));
        receta.setIdProducto(rs.getInt("id_producto"));
        receta.setDescripcionReceta(rs.getString("descripcion_receta"));
        receta.setIdInsumo(rs.getInt("id_ingrediente"));
        receta.setCantidadRequerida(rs.getDouble("cantidad"));
        receta.setNombreProducto(rs.getString("nombre_producto"));
        receta.setNombreInsumo(rs.getString("nombre_insumo"));
        receta.setUnidadMedida(rs.getString("medicion"));
        return receta;
    }
}
