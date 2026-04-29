package com.mycompany.perfectstrangers;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de Recetas: Gestiona la creación y actualización de recetas para productos.
 */
public class ServicioReceta {

    /**
     * Obtiene la receta completa (lista de ingredientes) para un producto.
     * @param idProducto El ID del producto.
     * @return Una lista de objetos Receta, donde cada objeto representa un ingrediente.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public static List<Receta> obtenerRecetaDeProducto(int idProducto) throws SQLException {
        return RecetaDAO.obtenerRecetasPorProducto(idProducto);
    }

    /**
     * Guarda o actualiza la receta completa para un producto.
     * Este método primero elimina la receta anterior y luego guarda la nueva de forma transaccional.
     * @param idProducto El ID del producto cuya receta se va a guardar.
     * @param descripcion La descripción o instrucciones de la receta.
     * @param ingredientes La nueva lista de ingredientes para la receta.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
    public static void guardarRecetaCompleta(int idProducto, String descripcion, List<Receta> ingredientes) throws SQLException {
        RecetaDAO.guardarRecetaCompleta(idProducto, descripcion, ingredientes);
    }
}