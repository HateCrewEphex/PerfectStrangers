package com.mycompany.perfectstrangers;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de Órdenes: Gestión del ciclo de vida completo de órdenes.
 * Coordina inventario, precios, estados y asignaciones.
 */
public class ServicioOrden {
    
    /**
     * Crea una nueva orden validando preliminarmente
     */
    public static Orden crearOrden(int idEmpleado, int mesa) throws SQLException {
        Empleado empleado = EmpleadoDAO.obtenerEmpleadoById(idEmpleado);
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no encontrado: " + idEmpleado);
        }
        
        Orden orden = new Orden(idEmpleado, mesa);
        OrdenDAO.crearOrden(orden);
        return orden;
    }
    
    /**
     * Agrega un producto a una orden con validación de inventario
     */
    public static void agregarProductoAOrden(int idOrden, int idProducto, int cantidad, 
                                             double precioUnitario, String notas) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            throw new IllegalArgumentException("Orden no encontrada: " + idOrden);
        }
        
        Producto producto = ProductoDAO.obtenerProductoById(idProducto);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado: " + idProducto);
        }
        
        // Validar disponibilidad de inventario
        if (!ServicioInventario.puedePrepararse(idProducto, cantidad)) {
            throw new IllegalArgumentException("Inventario insuficiente para: " + producto.getNombre());
        }
        
        // Crear detalle de orden
        DetalleOrden detalle = new DetalleOrden(idOrden, idProducto, cantidad, precioUnitario);
        detalle.setNotasEspeciales(notas);
        DetalleOrdenDAO.crearDetalleOrden(detalle);
        
        // Actualizar total
        actualizarTotal(idOrden);
    }
    
    /**
     * Elimina un producto de una orden
     */
    public static void removerProductoDeOrden(int idDetalle) throws SQLException {
        DetalleOrden detalle = DetalleOrdenDAO.obtenerDetalleById(idDetalle);
        if (detalle == null) {
            throw new IllegalArgumentException("Detalle no encontrado: " + idDetalle);
        }
        
        DetalleOrdenDAO.eliminarDetalleOrden(idDetalle);
        actualizarTotal(detalle.getIdOrden());
    }
    
    /**
     * Actualiza el total de una orden
     */
    public static void actualizarTotal(int idOrden) throws SQLException {
        double total = ServicioPrecio.calcularTotalFinal(idOrden);
        OrdenDAO.actualizarTotal(idOrden, total);
    }
    
    /**
     * Asigna un cocinero a una orden
     */
    public static void asignarCocinero(int idOrden, int idCocinero) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            throw new IllegalArgumentException("Orden no encontrada: " + idOrden);
        }
        
        Empleado cocinero = EmpleadoDAO.obtenerEmpleadoById(idCocinero);
        if (cocinero == null || !cocinero.esCocinero()) {
            throw new IllegalArgumentException("Cocinero no válido: " + idCocinero);
        }
        
        OrdenDAO.asignarCocinero(idOrden, idCocinero);
        cambiarEstado(idOrden, "En Preparacion");
    }
    
    /**
     * Cambia el estado de preparación de una orden
     */
    public static void cambiarEstado(int idOrden, String nuevoEstado) throws SQLException {
        String[] estadosValidos = {"Pendiente", "En Preparacion", "Entregado"};
        boolean valido = false;
        for (String estado : estadosValidos) {
            if (estado.equals(nuevoEstado)) {
                valido = true;
                break;
            }
        }
        
        if (!valido) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }
        
        OrdenDAO.actualizarEstadoPreparacion(idOrden, nuevoEstado);
    }
    
    /**
     * Marca una orden como entregada y consume inventario
     */
    public static void entregarOrden(int idOrden) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            throw new IllegalArgumentException("Orden no encontrada: " + idOrden);
        }
        
        // Consumir inventario de todos los detalles
        List<DetalleOrden> detalles = DetalleOrdenDAO.obtenerDetallesPorOrden(idOrden);
        for (DetalleOrden detalle : detalles) {
            RecetaDAO.consumirInsumos(detalle.getIdProducto(), detalle.getCantidad());
        }
        
        // Cambiar estado
        cambiarEstado(idOrden, "Entregado");
    }
    
    /**
     * Cancela una orden y devuelve el inventario
     */
    public static void cancelarOrden(int idOrden) throws SQLException {
        List<DetalleOrden> detalles = DetalleOrdenDAO.obtenerDetallesPorOrden(idOrden);
        
        // Devolver inventario
        for (DetalleOrden detalle : detalles) {
            RecetaDAO.devolverInsumos(detalle.getIdProducto(), detalle.getCantidad());
        }
        
        // Eliminar detalles
        DetalleOrdenDAO.eliminarDetallesPorOrden(idOrden);
        
        // Eliminar orden
        OrdenDAO.eliminarOrden(idOrden);
    }
    
    /**
     * Obtiene órdenes pendientes de preparar
     */
    public static List<Orden> obtenerOrdenesPendientes() throws SQLException {
        return OrdenDAO.obtenerOrdenesPendientes();
    }
    
    /**
     * Obtiene órdenes asignadas a un cocinero
     */
    public static List<Orden> obtenerOrdenesCocinero(int idCocinero) throws SQLException {
        return OrdenDAO.obtenerOrdenesPorCocinero(idCocinero);
    }
    
    /**
     * Obtiene órdenes abiertas de una mesa
     */
    public static List<Orden> obtenerOrdenesMesa(int mesa) throws SQLException {
        return OrdenDAO.obtenerOrdenesPorMesa(mesa);
    }
    
    /**
     * Obtiene todas las órdenes en preparación
     */
    public static List<Orden> obtenerOrdenesEnPreparacion() throws SQLException {
        return OrdenDAO.obtenerOrdenesEnPreparacion();
    }
    
    /**
     * Genera resumen de una orden (para impresión de factura)
     */
    public static String generarResumenOrden(int idOrden) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            throw new IllegalArgumentException("Orden no encontrada: " + idOrden);
        }
        
        List<DetalleOrden> detalles = DetalleOrdenDAO.obtenerDetallesPorOrden(idOrden);
        ServicioPrecio.DetallesPrecio precios = ServicioPrecio.obtenerDetallesPrecio(idOrden);
        
        StringBuilder resumen = new StringBuilder();
        resumen.append("========== RESUMEN DE ORDEN ==========\n");
        resumen.append(String.format("Orden: #%d\n", idOrden));
        resumen.append(String.format("Mesa: %d\n", orden.getMesa()));
        resumen.append(String.format("Mesero: %s\n", orden.getNombreEmpleado()));
        if (orden.getNombreCocinero() != null) {
            resumen.append(String.format("Cocinero: %s\n", orden.getNombreCocinero()));
        }
        resumen.append(String.format("Fecha: %s\n\n", orden.getFechaHora()));
        
        resumen.append("--- ITEMS ---\n");
        for (DetalleOrden detalle : detalles) {
            resumen.append(String.format("%d x %s: $%.2f\n",
                detalle.getCantidad(),
                detalle.getNombreProducto(),
                detalle.getSubtotal()
            ));
            if (detalle.getNotasEspeciales() != null && !detalle.getNotasEspeciales().isEmpty()) {
                resumen.append(String.format("   Nota: %s\n", detalle.getNotasEspeciales()));
            }
        }
        
        resumen.append("\n--- TOTALES ---\n");
        resumen.append(precios.toString()).append("\n");
        resumen.append("\n====================================\n");
        
        return resumen.toString();
    }
    
    /**
     * Obtiene estadísticas de órdenes
     */
    public static EstadisticasOrden obtenerEstadisticas() throws SQLException {
        List<Orden> pendientes = obtenerOrdenesPendientes();
        List<Orden> enPreparacion = obtenerOrdenesEnPreparacion();
        
        double montoPromedio = 0;
        if (!pendientes.isEmpty() || !enPreparacion.isEmpty()) {
            double montoTotal = 0;
            int cantidad = pendientes.size() + enPreparacion.size();
            for (Orden o : pendientes) montoTotal += o.getTotalCalculado();
            for (Orden o : enPreparacion) montoTotal += o.getTotalCalculado();
            montoPromedio = montoTotal / cantidad;
        }
        
        return new EstadisticasOrden(
            pendientes.size(),
            enPreparacion.size(),
            montoPromedio
        );
    }
    
    /**
     * Clase auxiliar para estadísticas
     */
    public static class EstadisticasOrden {
        public int ordenesPendientes;
        public int ordenesEnPreparacion;
        public double montoPromedio;
        
        public EstadisticasOrden(int pend, int prep, double monto) {
            this.ordenesPendientes = pend;
            this.ordenesEnPreparacion = prep;
            this.montoPromedio = monto;
        }
        
        @Override
        public String toString() {
            return String.format("Pendientes: %d | En Preparación: %d | Monto Promedio: $%.2f",
                ordenesPendientes, ordenesEnPreparacion, montoPromedio);
        }
    }
}
