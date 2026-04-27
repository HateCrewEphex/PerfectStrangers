package com.mycompany.perfectstrangers;

import java.sql.SQLException;

/**
 * Servicio de Sesión: Gestión de autenticación y control de acceso por rol.
 */
public class ServicioSesion {
    
    private static Empleado empleadoActual = null;
    
    /**
     * Autentica un usuario con usuario y contraseña
     */
    public static Empleado autenticar(String usuario, String contrasena) throws SQLException {
        if (usuario == null || usuario.isEmpty() || contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("Usuario y contraseña requeridos");
        }
        
        Empleado empleado = EmpleadoDAO.obtenerEmpleadoPorUsuario(usuario);
        if (empleado == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        if (!empleado.getContrasena().equals(contrasena)) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }
        
        if (!empleado.isEstadoEmpleado()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }
        
        empleadoActual = empleado;
        return empleado;
    }
    
    /**
     * Cierra la sesión actual
     */
    public static void cerrarSesion() {
        empleadoActual = null;
    }
    
    /**
     * Obtiene el empleado actualmente autenticado
     */
    public static Empleado getEmpleadoActual() {
        return empleadoActual;
    }
    
    /**
     * Verifica si hay sesión activa
     */
    public static boolean haySesionActiva() {
        return empleadoActual != null;
    }
    
    /**
     * Obtiene el ID del empleado actual
     */
    public static Integer getIdEmpleadoActual() {
        return empleadoActual != null ? empleadoActual.getIdEmpleado() : null;
    }
    
    /**
     * Obtiene el nombre del empleado actual
     */
    public static String getNombreEmpleadoActual() {
        return empleadoActual != null ? empleadoActual.getNombre() : null;
    }
    
    /**
     * Obtiene el puesto del empleado actual
     */
    public static String getPuestoEmpleadoActual() {
        return empleadoActual != null ? empleadoActual.getPuesto() : null;
    }
    
    /**
     * Verifica si el empleado actual es mesero
     */
    public static boolean esMesero() {
        return empleadoActual != null && empleadoActual.esMesero();
    }
    
    /**
     * Verifica si el empleado actual es cajero
     */
    public static boolean esCajero() {
        return empleadoActual != null && empleadoActual.esCajero();
    }
    
    /**
     * Verifica si el empleado actual es cocinero
     */
    public static boolean esCocinero() {
        return empleadoActual != null && empleadoActual.esCocinero();
    }
    
    /**
     * Verifica si el empleado actual es gerente
     */
    public static boolean esGerente() {
        return empleadoActual != null && empleadoActual.esGerente();
    }
    
    /**
     * Valida permisos para una acción
     */
    public static boolean tienePermiso(String accion) {
        if (empleadoActual == null) return false;
        
        switch (accion) {
            case "crear_orden":
                return esMesero() || esGerente();
            case "ver_ordenes":
                return esMesero() || esCocinero() || esCajero() || esGerente();
            case "preparar_orden":
                return esCocinero() || esGerente();
            case "cobrar":
                return esCajero() || esGerente();
            case "gestionar_inventario":
                return esGerente();
            case "gestionar_empleados":
                return esGerente();
            case "gestionar_promociones":
                return esGerente();
            case "ver_reportes":
                return esGerente();
            default:
                return false;
        }
    }
    
    /**
     * Valida permiso o lanza excepción
     */
    public static void requerirPermiso(String accion) throws IllegalAccessException {
        if (!tienePermiso(accion)) {
            throw new IllegalAccessException("Permiso denegado para: " + accion);
        }
    }
    
    /**
     * Genera registro de auditoría de sesión
     */
    public static String generarRegistroAuditoria(String accion) {
        if (empleadoActual == null) {
            return "[SIN SESIÓN] " + accion;
        }
        
        return String.format("[%s - %s] %s - %s",
            empleadoActual.getUsuario(),
            empleadoActual.getPuesto(),
            empleadoActual.getNombre(),
            accion
        );
    }
}
