/*
 * Servicio para obtener estadísticas de rendimiento de empleados
 */
package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServicioRendimiento {

    public static LinkedHashMap<Integer, String> obtenerPersonalPorPuesto(String puesto) throws SQLException {
        LinkedHashMap<Integer, String> personal = new LinkedHashMap<>();
        String sql = "SELECT id_empleado, nombre " +
                     "FROM empleados " +
                     "WHERE (TRIM(UPPER(puesto)) = ? OR TRIM(UPPER(puesto)) = 'GERENTE') AND estado_empleado = TRUE " +
                     "ORDER BY nombre";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, puesto.trim().toUpperCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                personal.put(rs.getInt("id_empleado"), rs.getString("nombre"));
            }
        }

        return personal;
    }
    
    // ===== MESEROS =====
    
    public static Map<String, Integer> obtenerOrdenesAtendidas() throws SQLException {
        return obtenerOrdenesAtendidas(null, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesAtendidas(Integer idMesero) throws SQLException {
        return obtenerOrdenesAtendidas(idMesero, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesAtendidas(Integer idMesero, Date fechaInicio, Date fechaFin) throws SQLException {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        if (idMesero != null) {
            String sql = "SELECT e.nombre, COUNT(o.id_orden) as total_ordenes " +
                         "FROM ordenes o " +
                         "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                         "WHERE (TRIM(UPPER(e.puesto)) = 'MESERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND TRIM(UPPER(o.estado_preparacion)) = 'ENTREGADO' AND e.id_empleado = ? " +
                         (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                         "GROUP BY e.id_empleado, e.nombre ORDER BY total_ordenes DESC";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                int idx = 1;
                stmt.setInt(idx++, idMesero);
                if (fechaInicio != null && fechaFin != null) {
                    stmt.setDate(idx++, fechaInicio);
                    stmt.setDate(idx++, fechaFin);
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    estadisticas.put(rs.getString("nombre"), rs.getInt("total_ordenes"));
                }
            }
            return estadisticas;
        }

        String sqlAssigned = "SELECT e.nombre, COUNT(o.id_orden) as total_ordenes " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "WHERE (TRIM(UPPER(e.puesto)) = 'MESERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND (o.id_empleado IS NOT NULL AND o.id_empleado <> 0) AND TRIM(UPPER(o.estado_preparacion)) = 'ENTREGADO' " +
                     (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                     "GROUP BY e.id_empleado, e.nombre ORDER BY total_ordenes DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlAssigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                estadisticas.put(rs.getString("nombre"), rs.getInt("total_ordenes"));
            }
        }

        String sqlUnassigned = "SELECT COUNT(*) as total_unassigned FROM ordenes WHERE (id_empleado IS NULL OR id_empleado = 0) AND TRIM(UPPER(estado_preparacion)) = 'ENTREGADO'" +
                               (fechaInicio != null && fechaFin != null ? " AND DATE(fecha_hora) BETWEEN ? AND ?" : "");
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlUnassigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int sinAsignar = rs.getInt("total_unassigned");
                if (sinAsignar > 0) {
                    estadisticas.put("Sin asignar", sinAsignar);
                }
            }
        }

        return estadisticas;
    }

    public static Map<String, Integer> obtenerOrdenesCanceladasPorMesero() throws SQLException {
        return obtenerOrdenesCanceladasPorMesero(null, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesCanceladasPorMesero(Integer idMesero) throws SQLException {
        return obtenerOrdenesCanceladasPorMesero(idMesero, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesCanceladasPorMesero(Integer idMesero, Date fechaInicio, Date fechaFin) throws SQLException {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        if (idMesero != null) {
            String sql = "SELECT e.nombre, COUNT(o.id_orden) as total_canceladas " +
                         "FROM ordenes o " +
                         "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                         "WHERE (TRIM(UPPER(e.puesto)) = 'MESERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND TRIM(UPPER(o.estado_preparacion)) = 'CANCELADO' AND e.id_empleado = ? " +
                         (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                         "GROUP BY e.id_empleado, e.nombre ORDER BY total_canceladas DESC";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                int idx = 1;
                stmt.setInt(idx++, idMesero);
                if (fechaInicio != null && fechaFin != null) {
                    stmt.setDate(idx++, fechaInicio);
                    stmt.setDate(idx++, fechaFin);
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    estadisticas.put(rs.getString("nombre"), rs.getInt("total_canceladas"));
                }
            }
            return estadisticas;
        }

        String sqlAssigned = "SELECT e.nombre, COUNT(o.id_orden) as total_canceladas " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_empleado = e.id_empleado " +
                     "WHERE (TRIM(UPPER(e.puesto)) = 'MESERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND TRIM(UPPER(o.estado_preparacion)) = 'CANCELADO' " +
                     (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                     "GROUP BY e.id_empleado, e.nombre ORDER BY total_canceladas DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlAssigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                estadisticas.put(rs.getString("nombre"), rs.getInt("total_canceladas"));
            }
        }

        String sqlUnassigned = "SELECT COUNT(*) as total_unassigned FROM ordenes WHERE (id_empleado IS NULL OR id_empleado = 0) AND TRIM(UPPER(estado_preparacion)) = 'CANCELADO'" +
                               (fechaInicio != null && fechaFin != null ? " AND DATE(fecha_hora) BETWEEN ? AND ?" : "");
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlUnassigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int sinAsignar = rs.getInt("total_unassigned");
                if (sinAsignar > 0) {
                    estadisticas.put("Sin asignar", sinAsignar);
                }
            }
        }

        return estadisticas;
    }
    
    // ===== COCINEROS =====
    
    public static Map<String, Integer> obtenerOrdenesPreparadas() throws SQLException {
        return obtenerOrdenesPreparadas(null, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesPreparadas(Integer idCocinero) throws SQLException {
        return obtenerOrdenesPreparadas(idCocinero, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesPreparadas(Integer idCocinero, Date fechaInicio, Date fechaFin) throws SQLException {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        if (idCocinero != null) {
            String sql = "SELECT e.nombre, COUNT(o.id_orden) as total_ordenes " +
                         "FROM ordenes o " +
                         "JOIN empleados e ON o.id_cocinero = e.id_empleado " +
                         "WHERE (TRIM(UPPER(e.puesto)) = 'COCINERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND (o.id_cocinero IS NOT NULL AND o.id_cocinero <> 0) AND e.id_empleado = ? AND TRIM(UPPER(o.estado_preparacion)) = 'ENTREGADO' " +
                         (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                         "GROUP BY e.id_empleado, e.nombre ORDER BY total_ordenes DESC";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                int idx = 1;
                stmt.setInt(idx++, idCocinero);
                if (fechaInicio != null && fechaFin != null) {
                    stmt.setDate(idx++, fechaInicio);
                    stmt.setDate(idx++, fechaFin);
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    estadisticas.put(rs.getString("nombre"), rs.getInt("total_ordenes"));
                }
            }
            return estadisticas;
        }

        String sqlAssigned = "SELECT e.nombre, COUNT(o.id_orden) as total_ordenes " +
                     "FROM ordenes o " +
                     "JOIN empleados e ON o.id_cocinero = e.id_empleado " +
                     "WHERE (TRIM(UPPER(e.puesto)) = 'COCINERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND (o.id_cocinero IS NOT NULL AND o.id_cocinero <> 0) AND TRIM(UPPER(o.estado_preparacion)) = 'ENTREGADO' " +
                     (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                     "GROUP BY e.id_empleado, e.nombre ORDER BY total_ordenes DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlAssigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                estadisticas.put(rs.getString("nombre"), rs.getInt("total_ordenes"));
            }
        }

        String sqlUnassigned = "SELECT COUNT(*) as total_unassigned FROM ordenes WHERE (id_cocinero IS NULL OR id_cocinero = 0) AND TRIM(UPPER(estado_preparacion)) = 'ENTREGADO'" +
                               (fechaInicio != null && fechaFin != null ? " AND DATE(fecha_hora) BETWEEN ? AND ?" : "");
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlUnassigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int sinAsignar = rs.getInt("total_unassigned");
                if (sinAsignar > 0) {
                    estadisticas.put("Sin asignar", sinAsignar);
                }
            }
        }

        return estadisticas;
    }

    public static Map<String, Integer> obtenerOrdenesCanceladasPorCocinero() throws SQLException {
        return obtenerOrdenesCanceladasPorCocinero(null, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesCanceladasPorCocinero(Integer idCocinero) throws SQLException {
        return obtenerOrdenesCanceladasPorCocinero(idCocinero, null, null);
    }

    public static Map<String, Integer> obtenerOrdenesCanceladasPorCocinero(Integer idCocinero, Date fechaInicio, Date fechaFin) throws SQLException {
        Map<String, Integer> estadisticas = new HashMap<>();
        
        if (idCocinero != null) {
            String sql = "SELECT e.nombre, COUNT(o.id_orden) as total_canceladas " +
                         "FROM ordenes o " +
                         "JOIN empleados e ON o.id_cocinero = e.id_empleado " +
                         "WHERE (TRIM(UPPER(e.puesto)) = 'COCINERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND TRIM(UPPER(o.estado_preparacion)) = 'CANCELADO' AND e.id_empleado = ? " +
                         (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                         "GROUP BY e.id_empleado, e.nombre ORDER BY total_canceladas DESC";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                int idx = 1;
                stmt.setInt(idx++, idCocinero);
                if (fechaInicio != null && fechaFin != null) {
                    stmt.setDate(idx++, fechaInicio);
                    stmt.setDate(idx++, fechaFin);
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    estadisticas.put(rs.getString("nombre"), rs.getInt("total_canceladas"));
                }
            }
            return estadisticas;
        }

        String sqlAssigned = "SELECT e.nombre, COUNT(o.id_orden) as total_canceladas " +
                             "FROM ordenes o " +
                             "JOIN empleados e ON o.id_cocinero = e.id_empleado " +
                             "WHERE (TRIM(UPPER(e.puesto)) = 'COCINERO' OR TRIM(UPPER(e.puesto)) = 'GERENTE') AND TRIM(UPPER(o.estado_preparacion)) = 'CANCELADO' " +
                             (fechaInicio != null && fechaFin != null ? "AND DATE(o.fecha_hora) BETWEEN ? AND ? " : "") +
                             "GROUP BY e.id_empleado, e.nombre ORDER BY total_canceladas DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlAssigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                estadisticas.put(rs.getString("nombre"), rs.getInt("total_canceladas"));
            }
        }

        String sqlUnassigned = "SELECT COUNT(*) as total_unassigned FROM ordenes WHERE (id_cocinero IS NULL OR id_cocinero = 0) AND TRIM(UPPER(estado_preparacion)) = 'CANCELADO'" +
                               (fechaInicio != null && fechaFin != null ? " AND DATE(fecha_hora) BETWEEN ? AND ?" : "");
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sqlUnassigned)) {
            int idx = 1;
            if (fechaInicio != null && fechaFin != null) {
                stmt.setDate(idx++, fechaInicio);
                stmt.setDate(idx++, fechaFin);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int sinAsignar = rs.getInt("total_unassigned");
                if (sinAsignar > 0) {
                    estadisticas.put("Sin asignar", sinAsignar);
                }
            }
        }

        return estadisticas;
    }
}
