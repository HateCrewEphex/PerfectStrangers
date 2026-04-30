package com.mycompany.perfectstrangers;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Servicio para monitorear el nivel de stock de insumos
 * Reproduce alerta cuando el stock está bajo o crítico
 */
public class InventarioAlertService {
    
    private static final String STOCK_ALERT_SOUND = "child in time alert.mp3";
    private static final long CHECK_INTERVAL_MS = 30 * 1000; // Verificar cada 30 segundos
    
    private Map<Integer, InsumoState> insumoStates = new HashMap<>();
    private Timer checkTimer;
    private boolean isRunning = false;
    private boolean soloUnaVezPorNivel = true; // Solo reproducir alerta una vez por nivel
    
    /**
     * Clase interna para rastrear el estado del insumo
     */
    private static class InsumoState {
        int idInsumo;
        String nombre;
        double cantidadActual;
        double stockBajo;
        double stockCritico;
        String nivelAnterior; // "normal", "bajo", "critico"
        
        InsumoState(int id, String nombre, double cantidad, double bajo, double critico) {
            this.idInsumo = id;
            this.nombre = nombre;
            this.cantidadActual = cantidad;
            this.stockBajo = bajo;
            this.stockCritico = critico;
            this.nivelAnterior = "normal";
        }
    }
    
    /**
     * Inicia el servicio de alertas de stock
     */
    public void iniciar() {
        if (isRunning) return;
        
        isRunning = true;
        checkTimer = new Timer("InventarioAlertService", true);
        checkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                verificarStockTodos();
            }
        }, CHECK_INTERVAL_MS, CHECK_INTERVAL_MS);
        
        System.out.println("[InventarioAlertService] Servicio iniciado - Verificando cada " + 
                         (CHECK_INTERVAL_MS / 1000) + " segundos");
    }
    
    /**
     * Detiene el servicio de alertas de stock
     */
    public void detener() {
        if (checkTimer != null) {
            checkTimer.cancel();
            checkTimer = null;
        }
        isRunning = false;
        insumoStates.clear();
    }
    
    /**
     * Registra un insumo para monitoreo
     * @param idInsumo ID del insumo
     * @param nombre Nombre del insumo
     * @param cantidadActual Cantidad actual en stock
     * @param stockBajo Umbral de stock bajo
     * @param stockCritico Umbral de stock crítico
     */
    public void registrarInsumo(int idInsumo, String nombre, double cantidadActual, 
                                 double stockBajo, double stockCritico) {
        InsumoState state = new InsumoState(idInsumo, nombre, cantidadActual, stockBajo, stockCritico);
        insumoStates.put(idInsumo, state);
    }
    
    /**
     * Actualiza la cantidad actual de un insumo
     * @param idInsumo ID del insumo
     * @param cantidadActual Nueva cantidad
     */
    public void actualizarCantidad(int idInsumo, double cantidadActual) {
        InsumoState state = insumoStates.get(idInsumo);
        if (state != null) {
            state.cantidadActual = cantidadActual;
        }
    }
    
    /**
     * Verifica el stock de todos los insumos registrados
     */
    private void verificarStockTodos() {
        for (InsumoState state : insumoStates.values()) {
            verificarStock(state);
        }
    }
    
    /**
     * Verifica el nivel de stock de un insumo específico
     */
    private void verificarStock(InsumoState state) {
        String nivelActual = determinarNivel(state);
        
        // Solo reproducir alerta si cambió de nivel
        if (soloUnaVezPorNivel && !nivelActual.equals(state.nivelAnterior)) {
            if ("critico".equals(nivelActual) || "bajo".equals(nivelActual)) {
                reproducirAlerta(state, nivelActual);
            }
            state.nivelAnterior = nivelActual;
        }
    }
    
    /**
     * Determina el nivel de stock (normal, bajo, critico)
     */
    private String determinarNivel(InsumoState state) {
        if (state.cantidadActual <= state.stockCritico) {
            return "critico";
        } else if (state.cantidadActual <= state.stockBajo) {
            return "bajo";
        }
        return "normal";
    }
    
    /**
     * Reproduce la alerta de stock
     */
    private void reproducirAlerta(InsumoState state, String nivel) {
        String mensaje = String.format("[InventarioAlertService] 🚨 ALERTA DE STOCK %s - %s (%.2f unidades)",
                                      nivel.toUpperCase(), state.nombre, state.cantidadActual);
        System.out.println(mensaje);
        System.out.println("  Reproduciendo: " + STOCK_ALERT_SOUND);
        
        SoundService.reproducirSonido(STOCK_ALERT_SOUND);
    }
    
    /**
     * Obtiene el estado de un insumo
     */
    public Map<String, Object> obtenerEstado(int idInsumo) {
        InsumoState state = insumoStates.get(idInsumo);
        if (state == null) return null;
        
        Map<String, Object> info = new HashMap<>();
        info.put("id", state.idInsumo);
        info.put("nombre", state.nombre);
        info.put("cantidad", state.cantidadActual);
        info.put("stockBajo", state.stockBajo);
        info.put("stockCritico", state.stockCritico);
        info.put("nivel", determinarNivel(state));
        
        return info;
    }
    
    /**
     * Obtiene si está ejecutándose
     */
    public boolean estaEjecutandose() {
        return isRunning;
    }
}
