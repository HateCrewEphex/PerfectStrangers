package com.mycompany.perfectstrangers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de Inventario: Gestión centralizada de insumos y stock.
 * Maneja reorden, alertas y validaciones de disponibilidad.
 */
public class ServicioInventario {
    
    private static final double PORCENTAJE_ALERTA = 0.75; // Alertar cuando stock cae al 75%

    public static Insumo registrarNuevoInsumoConEmpaque(Insumo insumo, EmpaqueInsumo empaque) throws SQLException {
        if (insumo == null) {
            throw new IllegalArgumentException("El insumo es obligatorio");
        }
        if (empaque == null) {
            throw new IllegalArgumentException("El empaque es obligatorio");
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                InsumoDAO.crearInsumo(con, insumo);
                empaque.setIdIngrediente(insumo.getIdInsumo());
                EmpaqueInsumoDAO.crearEmpaque(con, empaque);
                con.commit();
                return insumo;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public static void registrarEntradaExistente(int idIngrediente, EmpaqueInsumo empaque, double paquetes) throws SQLException {
        if (paquetes <= 0) {
            throw new IllegalArgumentException("La cantidad de paquetes debe ser mayor a 0");
        }

        Insumo insumo = InsumoDAO.obtenerInsumoById(idIngrediente);
        if (insumo == null) {
            throw new IllegalArgumentException("No existe el insumo seleccionado");
        }

        try (java.sql.Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                EmpaqueInsumo empaqueExistente = null;
                if (empaque != null && empaque.getCodBarrasEmpaque() != null && !empaque.getCodBarrasEmpaque().isBlank()) {
                    empaqueExistente = EmpaqueInsumoDAO.obtenerEmpaquePorCodigo(empaque.getCodBarrasEmpaque());
                }

                if (empaqueExistente == null) {
                    if (empaque == null) {
                        throw new IllegalArgumentException("Debes capturar los datos del empaque");
                    }
                    empaque.setIdIngrediente(idIngrediente);
                    EmpaqueInsumoDAO.crearEmpaque(con, empaque);
                } else {
                    empaqueExistente.setIdIngrediente(idIngrediente);
                    EmpaqueInsumoDAO.actualizarEmpaque(con, empaqueExistente);
                    empaque = empaqueExistente;
                }

                double unidadesAAgregar = empaque.getCantidadQueTrae() * paquetes;
                InsumoDAO.incrementarCantidad(idIngrediente, unidadesAAgregar);
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Obtiene todos los insumos con bajo stock (por debajo del mínimo)
     */
    public static List<Insumo> obtenerInsumosBajoStock() throws SQLException {
        return InsumoDAO.obtenerInsumosConBajoInventario();
    }
    
    /**
     * Obtiene insumos que necesitan reorden próxima (75% del mínimo)
     */
    public static List<Insumo> obtenerInsumosConAlerta() throws SQLException {
        List<Insumo> insumosAlerta = new ArrayList<>();
        List<Insumo> todos = InsumoDAO.obtenerTodosInsumos();
        
        for (Insumo insumo : todos) {
            double umbral = insumo.getCantidadMinima();
            if (insumo.getCantidadActual() <= umbral && insumo.getCantidadActual() > insumo.getCantidadCritica()) {
                insumosAlerta.add(insumo);
            }
        }
        
        return insumosAlerta;
    }
    
    /**
     * Obtiene todos los insumos del sistema
     * @return Lista de todos los insumos
     * @throws SQLException si ocurre un error en la base de datos
     */
    public static List<Insumo> obtenerTodosLosInsumos() throws SQLException {
        return InsumoDAO.obtenerTodosInsumos();
    }
    
    /**
     * Obtiene una lista de productos que no se pueden preparar debido a la falta de insumos.
     * @return Lista de productos no disponibles.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public static List<Producto> obtenerProductosNoDisponiblesPorInventario() throws SQLException {
        List<Producto> noDisponibles = new ArrayList<>();
        // We should check products that have recipes, typically 'Platillo' and 'Bebidas'
        List<Producto> productosConReceta = new ArrayList<>();
        productosConReceta.addAll(ProductoDAO.obtenerProductosPorCategoria("Platillo"));
        productosConReceta.addAll(ProductoDAO.obtenerProductosPorCategoria("Bebidas"));

        for (Producto producto : productosConReceta) {
            if (!puedePrepararse(producto.getIdProducto(), 1)) {
                noDisponibles.add(producto);
            }
        }
        return noDisponibles;
    }
    
    /**
     * Verifica si un producto puede prepararse en la cantidad solicitada
     * @return true si hay suficientes insumos, false si no
     */
    public static boolean puedePrepararse(int idProducto, int cantidad) throws SQLException {
        return RecetaDAO.verificarDisponibilidad(idProducto, cantidad);
    }
    
    /**
     * Registra el ingreso de insumo al inventario
     */
    public static void registrarIngreso(int idInsumo, double cantidad) throws SQLException {
        InsumoDAO.incrementarCantidad(idInsumo, cantidad);
    }
    
    /**
     * Registra el egreso de insumo del inventario
     */
    public static void registrarEgreso(int idInsumo, double cantidad) throws SQLException {
        InsumoDAO.decrementarCantidad(idInsumo, cantidad);
    }
    
    /**
     * Obtiene información de disponibilidad de insumos para un producto
     */
    public static List<InsumoDisponibilidad> obtenerDisponibilidadProducto(int idProducto) throws SQLException {
        List<InsumoDisponibilidad> disponibilidades = new ArrayList<>();
        List<Receta> recetas = RecetaDAO.obtenerRecetasPorProducto(idProducto);
        
        for (Receta receta : recetas) {
            Insumo insumo = InsumoDAO.obtenerInsumoById(receta.getIdInsumo());
            if (insumo != null) {
                int cantidadMaxima = (int) (insumo.getCantidadActual() / receta.getCantidadRequerida());
                disponibilidades.add(new InsumoDisponibilidad(
                    insumo.getNombreInsumo(),
                    insumo.getCantidadActual(),
                    receta.getCantidadRequerida(),
                    cantidadMaxima,
                    insumo.necesitaReorden()
                ));
            }
        }
        
        return disponibilidades;
    }
    
    /**
     * Genera reporte de inventario
     */
    public static String generarReporteInventario() throws SQLException {
        List<Insumo> todos = InsumoDAO.obtenerTodosInsumos();
        List<Insumo> bajoStock = obtenerInsumosBajoStock();
        List<Insumo> conAlerta = obtenerInsumosConAlerta();
        
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE INVENTARIO ===\n");
        reporte.append(String.format("Total de Insumos: %d\n", todos.size()));
        reporte.append(String.format("Bajo Stock: %d\n", bajoStock.size()));
        reporte.append(String.format("Con Alerta: %d\n\n", conAlerta.size()));
        
        reporte.append("--- INSUMOS CON BAJO STOCK ---\n");
        for (Insumo insumo : bajoStock) {
            reporte.append(String.format("• %s: %.2f %s (Mínimo: %.2f)\n",
                insumo.getNombreInsumo(),
                insumo.getCantidadActual(),
                insumo.getUnidadMedida(),
                insumo.getCantidadMinima()
            ));
        }
        
        reporte.append("\n--- INSUMOS CON ALERTA ---\n");
        for (Insumo insumo : conAlerta) {
            reporte.append(String.format("• %s: %.2f %s\n",
                insumo.getNombreInsumo(),
                insumo.getCantidadActual(),
                insumo.getUnidadMedida()
            ));
        }
        
        return reporte.toString();
    }
    
    /**
     * Clase auxiliar para información de disponibilidad
     */
    public static class InsumoDisponibilidad {
        public String nombreInsumo;
        public double cantidadDisponible;
        public double cantidadRequerida;
        public int cantidadMaximaProductos;
        public boolean bajoStock;
        
        public InsumoDisponibilidad(String nombre, double disponible, double requerida, 
                                   int maxProductos, boolean bajo) {
            this.nombreInsumo = nombre;
            this.cantidadDisponible = disponible;
            this.cantidadRequerida = requerida;
            this.cantidadMaximaProductos = maxProductos;
            this.bajoStock = bajo;
        }
    }
}
