package com.mycompany.perfectstrangers;

import java.sql.Timestamp;

public class Orden {
    private int idOrden;
    private int idEmpleado;
    private Integer idCocinero; // NULL si aún no se asigna
    private int mesa;
    private Timestamp fechaHora;
    private double totalCalculado;
    private String estadoPreparacion; // Pendiente, En Preparacion, Entregado
    private String estadoPago; // Pendiente, Parcial, Pagado

    // Datos adicionales (no almacenados en BD)
    private String nombreEmpleado;
    private String nombreCocinero;

    public Orden() {
    }

    public Orden(int idEmpleado, int mesa) {
        this.idEmpleado = idEmpleado;
        this.mesa = mesa;
        this.estadoPreparacion = "Pendiente";
        this.estadoPago = "Pendiente";
        this.totalCalculado = 0;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdCocinero() {
        return idCocinero;
    }

    public void setIdCocinero(Integer idCocinero) {
        this.idCocinero = idCocinero;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getTotalCalculado() {
        return totalCalculado;
    }

    public void setTotalCalculado(double totalCalculado) {
        this.totalCalculado = totalCalculado;
    }

    public String getEstadoPreparacion() {
        return estadoPreparacion;
    }

    public void setEstadoPreparacion(String estadoPreparacion) {
        this.estadoPreparacion = estadoPreparacion;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getNombreCocinero() {
        return nombreCocinero;
    }

    public void setNombreCocinero(String nombreCocinero) {
        this.nombreCocinero = nombreCocinero;
    }

    public boolean estaPendiente() {
        return "Pendiente".equals(estadoPreparacion);
    }

    public boolean estanEnPreparacion() {
        return "En Preparacion".equals(estadoPreparacion);
    }

    public boolean estaEntregada() {
        return "Entregado".equals(estadoPreparacion);
    }

    @Override
    public String toString() {
        return "Orden #" + idOrden + " - Mesa " + mesa + " - Total: $" + String.format("%.2f", totalCalculado);
    }
}
