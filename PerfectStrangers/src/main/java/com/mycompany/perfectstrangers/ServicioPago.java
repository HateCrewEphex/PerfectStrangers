package com.mycompany.perfectstrangers;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Servicio de Pagos: Gestión de cobro y registro de transacciones.
 */
public class ServicioPago {
    
    /**
     * Crea un pago para una orden
     */
    public static Pago crearPago(int idOrden, double montoPagado, String metodoPago) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            throw new IllegalArgumentException("Orden no encontrada: " + idOrden);
        }
        
        double total = ServicioPrecio.calcularTotalFinal(idOrden);
        
        if (montoPagado <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor a 0");
        }
        
        if (!metodoPago.equals("Efectivo") && !metodoPago.equals("Tarjeta") && !metodoPago.equals("Transferencia")) {
            throw new IllegalArgumentException("Método de pago inválido: " + metodoPago);
        }
        
        Pago pago = new Pago();
        pago.setIdOrden(idOrden);
        pago.setMontoPagado(montoPagado);
        pago.setMetodoPago(metodoPago);
        pago.setFechaPago(Timestamp.valueOf(LocalDateTime.now()));
        
        // Registrar pago en BD
        PagoDAO.crearPago(pago);
        
        // Actualizar estado de pago
        if (montoPagado >= total) {
            OrdenDAO.actualizarEstadoPago(idOrden, "Pagado");
        } else {
            OrdenDAO.actualizarEstadoPago(idOrden, "Parcial");
        }
        
        return pago;
    }
    
    /**
     * Obtiene el monto total pagado de una orden
     */
    public static double obtenerTotalPagado(int idOrden) throws SQLException {
        double totalPagado = 0;
        Pago pago = PagoDAO.obtenerPagoPorOrden(idOrden);
        if (pago != null) {
            totalPagado = pago.getMontoPagado();
        }
        return totalPagado;
    }
    
    /**
     * Calcula el cambio para un pago en efectivo
     */
    public static double calcularCambio(int idOrden, double montoPagado) throws SQLException {
        double total = ServicioPrecio.calcularTotalFinal(idOrden);
        return Math.max(0, montoPagado - total);
    }
    
    /**
     * Verifica si una orden está completamente pagada
     */
    public static boolean estaPagada(int idOrden) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            return false;
        }
        return "Pagado".equals(orden.getEstadoPago());
    }
    
    /**
     * Obtiene el estado del pago de una orden
     */
    public static String obtenerEstadoPago(int idOrden) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            throw new IllegalArgumentException("Orden no encontrada: " + idOrden);
        }
        return orden.getEstadoPago();
    }
    
    /**
     * Genera comprobante de pago
     */
    public static String generarComprobanteLocal(int idOrden, double montoPagado, String metodoPago) throws SQLException {
        Orden orden = OrdenDAO.obtenerOrdenById(idOrden);
        if (orden == null) {
            throw new IllegalArgumentException("Orden no encontrada: " + idOrden);
        }
        
        double total = ServicioPrecio.calcularTotalFinal(idOrden);
        double cambio = calcularCambio(idOrden, montoPagado);
        
        StringBuilder comprobante = new StringBuilder();
        comprobante.append("====== COMPROBANTE DE PAGO ======\n");
        comprobante.append(String.format("Orden: #%d\n", idOrden));
        comprobante.append(String.format("Mesa: %d\n", orden.getMesa()));
        comprobante.append(String.format("Mesero: %s\n", orden.getNombreEmpleado()));
        comprobante.append(String.format("Fecha: %s\n\n", orden.getFechaHora()));
        
        comprobante.append(String.format("Total: $%.2f\n", total));
        comprobante.append(String.format("Pagado: $%.2f\n", montoPagado));
        comprobante.append(String.format("Cambio: $%.2f\n", cambio));
        comprobante.append(String.format("Método: %s\n\n", metodoPago));
        
        comprobante.append("====== GRACIAS POR SU COMPRA ======\n");
        
        return comprobante.toString();
    }
    
    /**
     * Obtiene informe de ingresos diarios
     */
    public static String obtenerInformeDiario() throws SQLException {
        // Este método necesitaría una consulta más compleja a la BD
        // Por ahora es una estructura base
        StringBuilder informe = new StringBuilder();
        informe.append("===== INFORME DIARIO DE INGRESOS =====\n");
        informe.append("Fecha: " + LocalDateTime.now() + "\n");
        informe.append("Total de órdenes completadas: N/A\n");
        informe.append("Ingreso total: N/A\n");
        informe.append("Efectivo: N/A\n");
        informe.append("Tarjeta: N/A\n");
        informe.append("Transferencia: N/A\n");
        
        return informe.toString();
    }
}
