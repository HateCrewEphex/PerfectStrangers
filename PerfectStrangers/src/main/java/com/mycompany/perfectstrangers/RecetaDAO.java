package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO {

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

    /**
     * Guarda o actualiza la receta completa de un producto de forma transaccional.
     * Primero elimina la receta anterior y luego inserta la nueva.
     * @param idProducto El ID del producto.
     * @param descripcion La descripción de la receta.
     * @param ingredientes La lista de ingredientes para la nueva receta.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public static void guardarRecetaCompleta(int idProducto, String descripcion, List<Receta> ingredientes) throws SQLException {
        String sqlDeleteDetails = "DELETE FROM detalles_receta WHERE id_receta IN (SELECT id_receta FROM recetas WHERE id_producto = ?)";
        String sqlDeleteRecetaHeader = "DELETE FROM recetas WHERE id_producto = ?";
        String sqlInsertRecetaHeader = "INSERT INTO recetas (id_producto, descripcion) VALUES (?, ?)";
        String sqlInsertDetalle = "INSERT INTO detalles_receta (id_receta, id_ingrediente, cantidad) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // 1. Eliminar la receta anterior completa (detalles y cabecera)
            try (PreparedStatement stmt = con.prepareStatement(sqlDeleteDetails)) {
                stmt.setInt(1, idProducto);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = con.prepareStatement(sqlDeleteRecetaHeader)) {
                stmt.setInt(1, idProducto);
                stmt.executeUpdate();
            }

            // 2. Si hay nuevos ingredientes, crear la nueva receta
            if (ingredientes != null && !ingredientes.isEmpty()) {
                int idRecetaGenerado;
                try (PreparedStatement stmt = con.prepareStatement(sqlInsertRecetaHeader, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, idProducto);
                    stmt.setString(2, descripcion);
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            idRecetaGenerado = rs.getInt(1);
                        } else {
                            throw new SQLException("No se pudo obtener el ID de la receta creada.");
                        }
                    }
                }

                try (PreparedStatement stmt = con.prepareStatement(sqlInsertDetalle)) {
                    for (Receta ingrediente : ingredientes) {
                        stmt.setInt(1, idRecetaGenerado);
                        stmt.setInt(2, ingrediente.getIdInsumo());
                        stmt.setDouble(3, ingrediente.getCantidadRequerida());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
            }

            con.commit();

        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public static void eliminarRecetasPorProducto(int idProducto) throws SQLException {
        String sqlDeleteDetails = "DELETE FROM detalles_receta WHERE id_receta IN (SELECT id_receta FROM recetas WHERE id_producto = ?)";
        String sqlDeleteRecetas = "DELETE FROM recetas WHERE id_producto = ?";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                // First delete details to avoid foreign key constraint violations
                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteDetails)) {
                    stmt.setInt(1, idProducto);
                    stmt.executeUpdate();
                }
                // Then delete the recipes themselves
                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteRecetas)) {
                    stmt.setInt(1, idProducto);
                    stmt.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
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
