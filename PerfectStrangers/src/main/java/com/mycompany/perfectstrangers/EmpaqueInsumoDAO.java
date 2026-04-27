package com.mycompany.perfectstrangers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpaqueInsumoDAO {

    public static void crearEmpaque(Connection con, EmpaqueInsumo empaque) throws SQLException {
        String sql = "INSERT INTO empaques_insumos (id_ingrediente, nombre_empaque, cantidad_que_trae, cod_barras_empaque) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, empaque.getIdIngrediente());
            stmt.setString(2, empaque.getNombreEmpaque());
            stmt.setDouble(3, empaque.getCantidadQueTrae());
            stmt.setString(4, empaque.getCodBarrasEmpaque());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empaque.setIdEmpaque(rs.getInt(1));
                }
            }
        }
    }

    public static void actualizarEmpaque(Connection con, EmpaqueInsumo empaque) throws SQLException {
        String sql = "UPDATE empaques_insumos SET id_ingrediente = ?, nombre_empaque = ?, cantidad_que_trae = ?, cod_barras_empaque = ? WHERE id_empaque = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, empaque.getIdIngrediente());
            stmt.setString(2, empaque.getNombreEmpaque());
            stmt.setDouble(3, empaque.getCantidadQueTrae());
            stmt.setString(4, empaque.getCodBarrasEmpaque());
            stmt.setInt(5, empaque.getIdEmpaque());
            stmt.executeUpdate();
        }
    }

    public static EmpaqueInsumo obtenerEmpaquePorCodigo(String codBarrasEmpaque) throws SQLException {
        String sql = "SELECT * FROM empaques_insumos WHERE cod_barras_empaque = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, codBarrasEmpaque);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    public static List<EmpaqueInsumo> obtenerEmpaquesPorIngrediente(int idIngrediente) throws SQLException {
        String sql = "SELECT * FROM empaques_insumos WHERE id_ingrediente = ? ORDER BY nombre_empaque";
        List<EmpaqueInsumo> empaques = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idIngrediente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                empaques.add(mapResultSet(rs));
            }
        }
        return empaques;
    }

    private static EmpaqueInsumo mapResultSet(ResultSet rs) throws SQLException {
        EmpaqueInsumo empaque = new EmpaqueInsumo();
        empaque.setIdEmpaque(rs.getInt("id_empaque"));
        empaque.setIdIngrediente(rs.getInt("id_ingrediente"));
        empaque.setNombreEmpaque(rs.getString("nombre_empaque"));
        empaque.setCantidadQueTrae(rs.getDouble("cantidad_que_trae"));
        empaque.setCodBarrasEmpaque(rs.getString("cod_barras_empaque"));
        return empaque;
    }
}