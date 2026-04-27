package com.mycompany.perfectstrangers;

public class Empleado {
    private int idEmpleado;
    private String nombre;
    private String puesto; // Mesero, Cajero, Cocinero, Gerente
    private String usuario;
    private String contrasena;
    private boolean estadoEmpleado;

    public Empleado() {
    }

    public Empleado(String nombre, String puesto, String usuario, String contrasena) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estadoEmpleado = true;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isEstadoEmpleado() {
        return estadoEmpleado;
    }

    public void setEstadoEmpleado(boolean estadoEmpleado) {
        this.estadoEmpleado = estadoEmpleado;
    }

    public boolean esMesero() {
        return "Mesero".equals(puesto);
    }

    public boolean esCajero() {
        return "Cajero".equals(puesto);
    }

    public boolean esCocinero() {
        return "Cocinero".equals(puesto);
    }

    public boolean esGerente() {
        return "Gerente".equals(puesto);
    }

    @Override
    public String toString() {
        return nombre + " (" + puesto + ")";
    }
}
