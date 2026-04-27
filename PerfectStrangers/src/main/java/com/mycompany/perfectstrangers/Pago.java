package com.mycompany.perfectstrangers;

import java.sql.Timestamp;

public class Pago {
    private int idPago;
    private int idOrden;
    private double montoPagado;
    private String metodoPago; // Efectivo, Tarjeta, Transferencia
    private String estadoPago; // Completado, Cancelado
    private Timestamp fechaPago;
    private String referenciaExterna; // Para tarjeta o transferencia

    public Pago() {
    }

    public Pago(int idOrden, double montoPagado, String metodoPago) {
        this.idOrden = idOrden;
        this.montoPagado = montoPagado;
        this.metodoPago = metodoPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Timestamp getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Timestamp fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", idOrden=" + idOrden +
                ", montoPagado=" + montoPagado +
                ", metodoPago='" + metodoPago + '\'' +
                ", estadoPago='" + estadoPago + '\'' +
                ", fechaPago=" + fechaPago +
                '}';
    }
}
