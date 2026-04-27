package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InsumoDAO {

    public static void crearInsumo(Insumo insumo) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            crearInsumo(con, insumo);
        }
    }

    public static void crearInsumo(Connection con, Insumo insumo) throws SQLException {
        String sql = "INSERT INTO inventario (nombre, descripcion, tipo_producto, ubicacion, medicion, cod_barras_individual, stock, alert_stock, crit_stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, insumo.getNombreInsumo());
            stmt.setString(2, insumo.getDescripcion());
            stmt.setString(3, insumo.getTipoProducto());
            stmt.setString(4, insumo.getUbicacion());
            stmt.setString(5, insumo.getUnidadMedida());
            stmt.setString(6, insumo.getCodigoBarras());
            stmt.setDouble(7, insumo.getCantidadActual());
            stmt.setDouble(8, insumo.getCantidadMinima());
            stmt.setDouble(9, insumo.getCantidadCritica());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    insumo.setIdInsumo(rs.getInt(1));
                }
            }
        }
    }

    public static Insumo obtenerInsumoById(int idInsumo) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE id_ingrediente = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idInsumo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToInsumo(rs);
            }
        }
        return null;
    }

    public static Insumo obtenerInsumoPorCodigo(String codigoBarras) throws SQLException {
        String sql = "SELECT * FROM inventario WHERE cod_barras_individual = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, codigoBarras);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToInsumo(rs);
            }
        }
        return null;
    }

    public static List<Insumo> obtenerTodosInsumos() throws SQLException {
        String sql = "SELECT * FROM inventario ORDER BY nombre";
        List<Insumo> insumos = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                insumos.add(mapResultSetToInsumo(rs));
            }
        }
        return insumos;
    }

    public static List<Insumo> obtenerInsumosConBajoInventario() throws SQLException {
        String sql = "SELECT * FROM inventario WHERE stock <= crit_stock ORDER BY nombre";
        List<Insumo> insumos = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                insumos.add(mapResultSetToInsumo(rs));
            }
        }
        return insumos;
    }

    public static void actualizarInsumo(Insumo insumo) throws SQLException {
        String sql = "UPDATE inventario SET nombre = ?, descripcion = ?, tipo_producto = ?, ubicacion = ?, medicion = ?, cod_barras_individual = ?, stock = ?, alert_stock = ?, crit_stock = ? WHERE id_ingrediente = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, insumo.getNombreInsumo());
            stmt.setString(2, insumo.getDescripcion());
            stmt.setString(3, insumo.getTipoProducto());
            stmt.setString(4, insumo.getUbicacion());
            stmt.setString(5, insumo.getUnidadMedida());
            stmt.setString(6, insumo.getCodigoBarras());
            stmt.setDouble(7, insumo.getCantidadActual());
            stmt.setDouble(8, insumo.getCantidadMinima());
            stmt.setDouble(9, insumo.getCantidadCritica());
            stmt.setInt(10, insumo.getIdInsumo());
            stmt.executeUpdate();
        }
    }

    public static void actualizarCantidad(int idInsumo, double nuevaCantidad) throws SQLException {
        String sql = "UPDATE inventario SET stock = ? WHERE id_ingrediente = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, nuevaCantidad);
            stmt.setInt(2, idInsumo);
            stmt.executeUpdate();
        }
    }

    public static void incrementarCantidad(int idInsumo, double cantidad) throws SQLException {
        String sql = "UPDATE inventario SET stock = stock + ? WHERE id_ingrediente = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, cantidad);
            stmt.setInt(2, idInsumo);
            stmt.executeUpdate();
        }
    }

    public static void decrementarCantidad(int idInsumo, double cantidad) throws SQLException {
        String sql = "UPDATE inventario SET stock = GREATEST(0, stock - ?) WHERE id_ingrediente = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, cantidad);
            stmt.setInt(2, idInsumo);
            stmt.executeUpdate();
        }
    }

    public static void eliminarInsumo(int idInsumo) throws SQLException {
        String sql = "DELETE FROM inventario WHERE id_ingrediente = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idInsumo);
            stmt.executeUpdate();
        }
    }

    private static Insumo mapResultSetToInsumo(ResultSet rs) throws SQLException {
        Insumo insumo = new Insumo();
        insumo.setIdInsumo(rs.getInt("id_ingrediente"));
        insumo.setNombreInsumo(rs.getString("nombre"));
        insumo.setDescripcion(rs.getString("descripcion"));
        insumo.setTipoProducto(rs.getString("tipo_producto"));
        insumo.setUbicacion(rs.getString("ubicacion"));
        insumo.setUnidadMedida(rs.getString("medicion"));
        insumo.setCodigoBarras(rs.getString("cod_barras_individual"));
        insumo.setCantidadActual(rs.getDouble("stock"));
        insumo.setCantidadMinima(rs.getDouble("alert_stock"));
        insumo.setCantidadCritica(rs.getDouble("crit_stock"));
        return insumo;
    }
}
