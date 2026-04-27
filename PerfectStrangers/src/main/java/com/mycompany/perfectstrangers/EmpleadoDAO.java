package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public static void crearEmpleado(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, puesto, usuario, contrasena, estado_empleado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getPuesto());
            stmt.setString(3, empleado.getUsuario());
            stmt.setString(4, empleado.getContrasena());
            stmt.setBoolean(5, empleado.isEstadoEmpleado());
            stmt.executeUpdate();
        }
    }

    public static Empleado obtenerEmpleadoById(int idEmpleado) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmpleado(rs);
            }
        }
        return null;
    }

    public static Empleado obtenerEmpleadoPorUsuario(String usuario) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE usuario = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmpleado(rs);
            }
        }
        return null;
    }

    public static List<Empleado> obtenerTodosEmpleados() throws SQLException {
        String sql = "SELECT * FROM empleados WHERE estado_empleado = TRUE ORDER BY nombre";
        List<Empleado> empleados = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                empleados.add(mapResultSetToEmpleado(rs));
            }
        }
        return empleados;
    }

    public static List<Empleado> obtenerEmpleadosPorPuesto(String puesto) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE puesto = ? AND estado_empleado = TRUE ORDER BY nombre";
        List<Empleado> empleados = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, puesto);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                empleados.add(mapResultSetToEmpleado(rs));
            }
        }
        return empleados;
    }

    public static List<Empleado> obtenerCocineros() throws SQLException {
        return obtenerEmpleadosPorPuesto("Cocinero");
    }

    public static void actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleados SET nombre = ?, puesto = ?, usuario = ?, contrasena = ?, estado_empleado = ? " +
                     "WHERE id_empleado = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getPuesto());
            stmt.setString(3, empleado.getUsuario());
            stmt.setString(4, empleado.getContrasena());
            stmt.setBoolean(5, empleado.isEstadoEmpleado());
            stmt.setInt(6, empleado.getIdEmpleado());
            stmt.executeUpdate();
        }
    }

    public static void desactivarEmpleado(int idEmpleado) throws SQLException {
        String sql = "UPDATE empleados SET estado_empleado = FALSE WHERE id_empleado = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.executeUpdate();
        }
    }

    public static void eliminarEmpleado(int idEmpleado) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id_empleado = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.executeUpdate();
        }
    }

    private static Empleado mapResultSetToEmpleado(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(rs.getInt("id_empleado"));
        empleado.setNombre(rs.getString("nombre"));
        empleado.setPuesto(rs.getString("puesto"));
        empleado.setUsuario(rs.getString("usuario"));
        empleado.setContrasena(rs.getString("contrasena"));
        empleado.setEstadoEmpleado(rs.getBoolean("estado_empleado"));
        return empleado;
    }
}
