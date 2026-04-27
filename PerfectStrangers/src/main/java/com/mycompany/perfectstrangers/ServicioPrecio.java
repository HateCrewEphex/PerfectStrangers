package com.mycompany.perfectstrangers;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de Precios: Gestión de cálculos de totales, descuentos y promociones.
 */
public class ServicioPrecio {
    
    /**
     * Calcula el subtotal de una orden sin descuentos
     */
    public static double calcularSubtotal(int idOrden) throws SQLException {
        return DetalleOrdenDAO.calcularTotalOrden(idOrden);
    }
    
    /**
     * Calcula descuentos aplicables a una orden completa
     */
    public static double calcularDescuentoOrden(int idOrden) throws SQLException {
        List<DetalleOrden> detalles = DetalleOrdenDAO.obtenerDetallesPorOrden(idOrden);
        double descuentoTotal = 0;
        
        for (DetalleOrden detalle : detalles) {
            double descuentoItem = calcularDescuentoProducto(detalle.getIdProducto(), detalle.getPrecioUnitario() * detalle.getCantidad());
            descuentoTotal += descuentoItem;
        }
        
        return descuentoTotal;
    }
    
    /**
     * Calcula descuentos para un producto específico
     */
    public static double calcularDescuentoProducto(int idProducto, double monto) throws SQLException {
        List<Promocion> promos = PromocionDAO.obtenerPromcionesActivasPorProducto(idProducto);
        double descuentoTotal = 0;
        
        for (Promocion promo : promos) {
            double descuento = promo.calcularDescuento(monto);
            descuentoTotal += descuento;
        }
        
        return descuentoTotal;
    }
    
    /**
     * Obtiene promociones activas para un producto
     */
    public static List<Promocion> obtenerPromociones(int idProducto) throws SQLException {
        return PromocionDAO.obtenerPromcionesActivasPorProducto(idProducto);
    }
    
    /**
     * Calcula el total final de una orden (con descuentos)
     */
    public static double calcularTotalFinal(int idOrden) throws SQLException {
        double subtotal = calcularSubtotal(idOrden);
        double descuento = calcularDescuentoOrden(idOrden);
        return Math.max(0, subtotal - descuento);
    }
    
    /**
     * Calcula información detallada de precios para una orden
     */
    public static DetallesPrecio obtenerDetallesPrecio(int idOrden) throws SQLException {
        double subtotal = calcularSubtotal(idOrden);
        double descuento = calcularDescuentoOrden(idOrden);
        double total = calcularTotalFinal(idOrden);
        double porcentajeDescuento = subtotal > 0 ? (descuento / subtotal) * 100 : 0;
        
        return new DetallesPrecio(subtotal, descuento, porcentajeDescuento, total);
    }
    
    /**
     * Aplica descuento adicional manual (ej. cortesía, descuento gerente)
     */
    public static double aplicarDescuentoManual(double total, double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje debe estar entre 0 y 100");
        }
        return total * (1 - (porcentaje / 100));
    }
    
    /**
     * Calcula cambio de un pago en efectivo
     */
    public static double calcularCambio(double total, double montoPagado) {
        return Math.max(0, montoPagado - total);
    }
    
    /**
     * Genera desglose de promociones aplicadas a una orden
     */
    public static String generarDesglosetPromo(int idOrden) throws SQLException {
        List<DetalleOrden> detalles = DetalleOrdenDAO.obtenerDetallesPorOrden(idOrden);
        StringBuilder desglose = new StringBuilder();
        
        desglose.append("=== DESGLOSE DE PROMOCIONES ===\n");
        
        boolean tienePromo = false;
        for (DetalleOrden detalle : detalles) {
            List<Promocion> promos = obtenerPromociones(detalle.getIdProducto());
            if (!promos.isEmpty()) {
                tienePromo = true;
                desglose.append(String.format("\n• %s (x%d):\n", detalle.getNombreProducto(), detalle.getCantidad()));
                
                for (Promocion promo : promos) {
                    double descuento = promo.calcularDescuento(detalle.getSubtotal());
                    desglose.append(String.format("  - %s: -$%.2f\n", promo.getNombrePromo(), descuento));
                }
            }
        }
        
        if (!tienePromo) {
            desglose.append("Sin promociones aplicables\n");
        }
        
        return desglose.toString();
    }
    
    /**
     * Clase auxiliar para detalles de precio
     */
    public static class DetallesPrecio {
        public double subtotal;
        public double descuento;
        public double porcentajeDescuento;
        public double total;
        
        public DetallesPrecio(double sub, double desc, double porcDesc, double tot) {
            this.subtotal = sub;
            this.descuento = desc;
            this.porcentajeDescuento = porcDesc;
            this.total = tot;
        }
        
        @Override
        public String toString() {
            return String.format("Subtotal: $%.2f | Descuento: $%.2f (%.1f%%) | Total: $%.2f",
                subtotal, descuento, porcentajeDescuento, total);
        }
    }
}
