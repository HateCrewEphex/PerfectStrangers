package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static void crearProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, categoria, imagen, disponible) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getCategoria());
            stmt.setString(5, producto.getRutaImagen());  // Por ahora guardamos como string
            stmt.setBoolean(6, producto.isDisponible());
            stmt.executeUpdate();
        }
    }

    public static Producto obtenerProductoById(int idProducto) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProducto(rs);
            }
        }
        return null;
    }

    public static Producto obtenerProductoPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProducto(rs);
            }
        }
        return null;
    }

    public static List<Producto> obtenerTodosProductos() throws SQLException {
        String sql = "SELECT * FROM productos WHERE disponible = TRUE ORDER BY categoria, nombre";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
        }
        return productos;
    }

    public static List<Producto> obtenerProductosPorCategoria(String categoria) throws SQLException {
        String sql = "SELECT * FROM productos WHERE categoria = ? AND disponible = TRUE ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
        }
        return productos;
    }

    public static List<Producto> obtenerPlatillos() throws SQLException {
        return obtenerProductosPorCategoria("Platillo");
    }

    public static List<Producto> obtenerBebidas() throws SQLException {
        return obtenerProductosPorCategoria("Bebidas");
    }

    public static List<Producto> obtenerComplementos() throws SQLException {
        return obtenerProductosPorCategoria("Complementos");
    }

    public static void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, categoria = ?, " +
                     "imagen = ?, disponible = ? WHERE id_producto = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getCategoria());
            stmt.setString(5, producto.getRutaImagen());  // Por ahora guardamos como string
            stmt.setBoolean(6, producto.isDisponible());
            stmt.setInt(7, producto.getIdProducto());
            stmt.executeUpdate();
        }
    }

    public static void desactivarProducto(int idProducto) throws SQLException {
        String sql = "UPDATE productos SET disponible = FALSE WHERE id_producto = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
        }
    }

    public static void eliminarProducto(int idProducto) throws SQLException {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
        }
    }

    private static Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setCategoria(rs.getString("categoria"));
        
        // Intentar leer imagen como string (compatibilidad)
        try {
            byte[] imagenBlob = rs.getBytes("imagen");
            if (imagenBlob != null && imagenBlob.length > 0) {
                producto.setRutaImagen("blob");  // Flag para indicar que hay imagen
            }
        } catch (SQLException e) {
            // Si la columna no existe o hay error, ignorar
            producto.setRutaImagen(null);
        }
        
        producto.setDisponible(rs.getBoolean("disponible"));
        return producto;
    }
}
