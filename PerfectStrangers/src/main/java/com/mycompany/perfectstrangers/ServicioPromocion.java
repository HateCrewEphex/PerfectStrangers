package com.mycompany.perfectstrangers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de Promociones: Auto-aplicación inteligente de promos
 * - Detecta promos específicas de productos (aplicación automática)
 * - Detecta promos genéricas sin producto (sugiere al mesero)
 */
public class ServicioPromocion {
    
    /**
     * Obtiene promociones que aplican directamente a un producto específico
     * Útil para auto-aplicar sin confirmación del mesero
     */
    public static List<Promocion> obtenerPromosPorProducto(int idProducto) throws SQLException {
        List<Promocion> promos = PromocionDAO.obtenerPromcionesActivasPorProducto(idProducto);
        List<Promocion> promosProducto = new ArrayList<>();
        
        for (Promocion promo : promos) {
            if (promo.getIdProductoAfectado() != null && 
                promo.getIdProductoAfectado() == idProducto) {
                promosProducto.add(promo);
            }
        }
        return promosProducto;
    }
    
    /**
     * Obtiene promociones genéricas (sin producto específico)
     * Útil para sugerir al mesero si desea activarlas
     */
    public static List<Promocion> obtenerPromosGenericas() throws SQLException {
        List<Promocion> promos = PromocionDAO.obtenerPromcionesActivas();
        List<Promocion> promosGenericas = new ArrayList<>();
        
        for (Promocion promo : promos) {
            if (promo.getIdProductoAfectado() == null) {
                promosGenericas.add(promo);
            }
        }
        return promosGenericas;
    }
    
    /**
     * Obtiene la mejor promoción (mayor descuento) para un producto
     * Si hay múltiples, retorna la de mayor valor de descuento
     */
    public static Promocion obtenerMejorPromo(int idProducto) throws SQLException {
        List<Promocion> promos = obtenerPromosPorProducto(idProducto);
        
        if (promos.isEmpty()) {
            return null;
        }
        
        Promocion mejorPromo = promos.get(0);
        double mejorDescuento = mejorPromo.getValorDescuento();
        
        for (Promocion promo : promos) {
            if (promo.getValorDescuento() > mejorDescuento) {
                mejorPromo = promo;
                mejorDescuento = promo.getValorDescuento();
            }
        }
        
        return mejorPromo;
    }

    /**
     * Obtiene la mejor promoción (mayor descuento real) para un producto y monto dado.
     * Útil cuando el tipo de descuento afecta el cálculo final.
     */
    public static Promocion obtenerMejorPromo(int idProducto, double monto) throws SQLException {
        List<Promocion> promos = obtenerPromosPorProducto(idProducto);

        if (promos.isEmpty()) {
            return null;
        }

        Promocion mejorPromo = null;
        double mejorDescuento = -1;

        for (Promocion promo : promos) {
            double descuento = promo.calcularDescuento(monto);
            if (descuento > mejorDescuento) {
                mejorPromo = promo;
                mejorDescuento = descuento;
            }
        }

        return mejorPromo;
    }
    
    /**
     * Valida si una promoción puede aplicarse ahora
     */
    public static boolean esPromoValida(int idPromocion) throws SQLException {
        Promocion promo = PromocionDAO.obtenerPromocionById(idPromocion);
        return promo != null && promo.esValidaAhora();
    }
    
    /**
     * Calcula el descuento de una promo sobre un monto
     */
    public static double calcularDescuento(int idPromocion, double monto) throws SQLException {
        Promocion promo = PromocionDAO.obtenerPromocionById(idPromocion);
        if (promo == null) return 0;
        return promo.calcularDescuento(monto);
    }
}
