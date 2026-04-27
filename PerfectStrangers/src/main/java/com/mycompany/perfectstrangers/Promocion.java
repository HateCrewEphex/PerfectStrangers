package com.mycompany.perfectstrangers;

public class Promocion {
    private int idPromocion;
    private String nombrePromo;
    private String tipoDescuento; // Porcentaje, Fijo, 2x1
    private double valorDescuento;
    private String fechaInicio;
    private String fechaFin;
    private Integer idProductoAfectado; // NULL si aplica a todos
    private boolean estadoPromo;

    // Datos adicionales (no almacenados en BD)
    private String nombreProducto;

    public Promocion() {
    }

    public Promocion(String nombrePromo, String tipoDescuento, double valorDescuento, 
                    String fechaInicio, String fechaFin, Integer idProductoAfectado) {
        this.nombrePromo = nombrePromo;
        this.tipoDescuento = tipoDescuento;
        this.valorDescuento = valorDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idProductoAfectado = idProductoAfectado;
        this.estadoPromo = true;
    }

    public int getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(int idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getNombrePromo() {
        return nombrePromo;
    }

    public void setNombrePromo(String nombrePromo) {
        this.nombrePromo = nombrePromo;
    }

    public String getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public double getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(double valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getIdProductoAfectado() {
        return idProductoAfectado;
    }

    public void setIdProductoAfectado(Integer idProductoAfectado) {
        this.idProductoAfectado = idProductoAfectado;
    }

    public boolean isEstadoPromo() {
        return estadoPromo;
    }

    public void setEstadoPromo(boolean estadoPromo) {
        this.estadoPromo = estadoPromo;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double calcularDescuento(double monto) {
        if (!estadoPromo) return 0;
        
        return switch (tipoDescuento) {
            case "Porcentaje" -> monto * (valorDescuento / 100);
            case "Fijo" -> valorDescuento;
            case "2x1" -> monto / 2; // El segundo item cuesta la mitad
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return "Promocion{" +
                "idPromocion=" + idPromocion +
                ", nombrePromo='" + nombrePromo + '\'' +
                ", tipoDescuento='" + tipoDescuento + '\'' +
                ", valorDescuento=" + valorDescuento +
                '}';
    }
}
