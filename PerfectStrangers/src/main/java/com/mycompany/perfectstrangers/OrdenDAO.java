package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class OrdenDAO {

    public static void crearOrden(Orden orden) throws SQLException {
        String sql = "INSERT INTO ordenes (id_empleado, id_cocinero, mesa, estado_preparacion, estado_pago, total_calculado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, orden.getIdEmpleado());
            if (orden.getIdCocinero() != null) {
                stmt.setInt(2, orden.getIdCocinero());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, orden.getMesa());
            stmt.setString(4, orden.getEstadoPreparacion());
            stmt.setString(5, orden.getEstadoPago());
            stmt.setDouble(6, orden.getTotalCalculado());
            stmt.executeUpdate();
            
            // Obtener el ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orden.setIdOrden(rs.getInt(1));
                }
            }
        }
    }

    public static Orden obtenerOrdenById(int idOrden) throws SQLException {
        String sql = "SELECT o.*, e.nombre AS nombre_empleado, c.nombre AS nombre_cocinero " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "LEFT JOIN empleados c ON o.id_cocinero = c.id_empleado " +
                     "WHERE o.id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOrden(rs);
            }
        }
        return null;
    }

    public static List<Orden> obtenerOrdenesPorMesa(int mesa) throws SQLException {
        String sql = "SELECT o.*, e.nombre AS nombre_empleado, c.nombre AS nombre_cocinero " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "LEFT JOIN empleados c ON o.id_cocinero = c.id_empleado " +
                     "WHERE o.mesa = ? AND o.estado_pago != 'Pagado' " +
                     "ORDER BY o.fecha_hora DESC";
        
        List<Orden> ordenes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, mesa);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapResultSetToOrden(rs));
            }
        }
        return ordenes;
    }

    public static List<Orden> obtenerOrdenesPendientes() throws SQLException {
        String sql = "SELECT o.*, e.nombre AS nombre_empleado, c.nombre AS nombre_cocinero " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "LEFT JOIN empleados c ON o.id_cocinero = c.id_empleado " +
                     "WHERE o.estado_preparacion = 'Pendiente' " +
                     "ORDER BY o.fecha_hora ASC";
        
        List<Orden> ordenes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapResultSetToOrden(rs));
            }
        }
        return ordenes;
    }

    public static List<Orden> obtenerOrdenesEnPreparacion() throws SQLException {
        String sql = "SELECT o.*, e.nombre AS nombre_empleado, c.nombre AS nombre_cocinero " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "LEFT JOIN empleados c ON o.id_cocinero = c.id_empleado " +
                     "WHERE o.estado_preparacion = 'En Preparacion' " +
                     "ORDER BY o.fecha_hora ASC";
        
        List<Orden> ordenes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapResultSetToOrden(rs));
            }
        }
        return ordenes;
    }

    public static List<Orden> obtenerOrdenesPorCocinero(int idCocinero) throws SQLException {
        String sql = "SELECT o.*, e.nombre AS nombre_empleado, c.nombre AS nombre_cocinero " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "LEFT JOIN empleados c ON o.id_cocinero = c.id_empleado " +
                     "WHERE o.id_cocinero = ? AND o.estado_preparacion != 'Entregado' " +
                     "ORDER BY o.fecha_hora ASC";
        
        List<Orden> ordenes = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCocinero);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapResultSetToOrden(rs));
            }
        }
        return ordenes;
    }

    public static void asignarCocinero(int idOrden, int idCocinero) throws SQLException {
        String sql = "UPDATE ordenes SET id_cocinero = ? WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idCocinero);
            stmt.setInt(2, idOrden);
            stmt.executeUpdate();
        }
    }

    public static void actualizarEstadoPreparacion(int idOrden, String nuevoEstado) throws SQLException {
        String sql = "UPDATE ordenes SET estado_preparacion = ? WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idOrden);
            stmt.executeUpdate();
        }
    }

    public static void actualizarEstadoPago(int idOrden, String nuevoEstado) throws SQLException {
        String sql = "UPDATE ordenes SET estado_pago = ? WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idOrden);
            stmt.executeUpdate();
        }
    }

    public static void actualizarTotal(int idOrden, double nuevoTotal) throws SQLException {
        String sql = "UPDATE ordenes SET total_calculado = ? WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, nuevoTotal);
            stmt.setInt(2, idOrden);
            stmt.executeUpdate();
        }
    }

    public static void actualizarOrden(Orden orden) throws SQLException {
        String sql = "UPDATE ordenes SET id_empleado = ?, id_cocinero = ?, mesa = ?, " +
                     "estado_preparacion = ?, estado_pago = ?, total_calculado = ? WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orden.getIdEmpleado());
            if (orden.getIdCocinero() != null) {
                stmt.setInt(2, orden.getIdCocinero());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, orden.getMesa());
            stmt.setString(4, orden.getEstadoPreparacion());
            stmt.setString(5, orden.getEstadoPago());
            stmt.setDouble(6, orden.getTotalCalculado());
            stmt.setInt(7, orden.getIdOrden());
            stmt.executeUpdate();
        }
    }

    public static void eliminarOrden(int idOrden) throws SQLException {
        String sql = "DELETE FROM ordenes WHERE id_orden = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            stmt.executeUpdate();
        }
    }

    private static Orden mapResultSetToOrden(ResultSet rs) throws SQLException {
        Orden orden = new Orden();
        orden.setIdOrden(rs.getInt("id_orden"));
        orden.setIdEmpleado(rs.getInt("id_empleado"));
        int idCocinero = rs.getInt("id_cocinero");
        orden.setIdCocinero(rs.wasNull() ? null : idCocinero);
        orden.setMesa(rs.getInt("mesa"));
        orden.setFechaHora(rs.getTimestamp("fecha_hora"));
        orden.setTotalCalculado(rs.getDouble("total_calculado"));
        orden.setEstadoPreparacion(rs.getString("estado_preparacion"));
        orden.setEstadoPago(rs.getString("estado_pago"));
        orden.setNombreEmpleado(rs.getString("nombre_empleado"));
        orden.setNombreCocinero(rs.getString("nombre_cocinero"));
        return orden;
    }
}
