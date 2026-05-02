package com.mycompany.perfectstrangers;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

/**
 * Monitor de actividad de sesión
 * Detecta cuando el usuario ha estado inactivo por un tiempo determinado
 * y reproduce una alerta de sonido
 */
public class SessionActivityMonitor {
    
    private static final long INACTIVITY_TIME_MS = 5 * 60 * 1000; // 5 minutos
    private static final String ALERT_SOUND = "PerfectStrangers-alert.wav";
    
    private JFrame frame;
    private Timer inactivityTimer;
    private long lastActivityTime;
    private boolean isMonitoring = false;
    private boolean alertaYaReproducida = false;
    
    /**
     * Constructor
     * @param frame el frame principal a monitorear
     */
    public SessionActivityMonitor(JFrame frame) {
        this.frame = frame;
        this.lastActivityTime = System.currentTimeMillis();
    }
    
    /**
     * Inicia el monitoreo de actividad
     */
    public void iniciarMonitoreo() {
        if (isMonitoring) return;
        
        isMonitoring = true;
        alertaYaReproducida = false;
        lastActivityTime = System.currentTimeMillis();
        
        // Agregar detectores de eventos al frame
        agregarDetectoresDeActividad();
        
        // Timer para verificar inactividad cada 30 segundos
        inactivityTimer = new Timer("SessionActivityMonitor", true);
        inactivityTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                verificarInactividad();
            }
        }, 30000, 30000); // Comienza después de 30s, verifica cada 30s
        
        System.out.println("[SessionActivityMonitor] Monitoreo iniciado - Alerta después de " + 
                         (INACTIVITY_TIME_MS / 1000 / 60) + " minutos de inactividad");
    }
    
    /**
     * Detiene el monitoreo de actividad
     */
    public void detenerMonitoreo() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
            inactivityTimer = null;
        }
        isMonitoring = false;
        alertaYaReproducida = false;
    }
    
    /**
     * Verifica si hay inactividad
     */
    private void verificarInactividad() {
        long tiempoInactivo = System.currentTimeMillis() - lastActivityTime;
        
        if (tiempoInactivo >= INACTIVITY_TIME_MS) {
            if (!alertaYaReproducida) {
                reproducirAlertaInactividad();
                alertaYaReproducida = true;
            }
        } else {
            // Resetear bandera cuando vuelve a haber actividad
            if (alertaYaReproducida && tiempoInactivo < INACTIVITY_TIME_MS) {
                alertaYaReproducida = false;
            }
        }
    }
    
    /**
     * Reproduce la alerta de inactividad
     */
    private void reproducirAlertaInactividad() {
        System.out.println("[SessionActivityMonitor] ⚠️ ALERTA DE INACTIVIDAD - Reproduciendo: " + ALERT_SOUND);
        SoundService.reproducirSonido(ALERT_SOUND);
    }
    
    /**
     * Registra actividad del usuario
     */
    private void registrarActividad() {
        lastActivityTime = System.currentTimeMillis();
        alertaYaReproducida = false;
    }
    
    /**
     * Agrega detectores de eventos de actividad al frame y sus componentes
     */
    private void agregarDetectoresDeActividad() {
        // Detectar movimiento del ratón
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                registrarActividad();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                registrarActividad();
            }
        });
        
        // Detectar pulsaciones de teclado
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                registrarActividad();
            }
        });
        
        // Detectar movimiento del ratón global (aplicar a todos los componentes)
        agregarDetectorRatonGlobal(frame);
    }
    
    /**
     * Agrega detector de ratón a todos los componentes recursivamente
     */
    private void agregarDetectorRatonGlobal(Component component) {
        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                registrarActividad();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                registrarActividad();
            }
        });
        
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                registrarActividad();
            }
        });
        
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                registrarActividad();
            }
        });
        
        // Aplicar recursivamente a componentes hijos
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                agregarDetectorRatonGlobal(child);
            }
        }
    }
    
    /**
     * Obtiene el tiempo de inactividad en milisegundos
     */
    public long obtenerTiempoInactivo() {
        return System.currentTimeMillis() - lastActivityTime;
    }
    
    /**
     * Obtiene si está monitoreando
     */
    public boolean estaMonitoreando() {
        return isMonitoring;
    }
}
