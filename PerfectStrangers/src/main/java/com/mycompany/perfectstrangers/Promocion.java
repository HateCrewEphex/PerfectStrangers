package com.mycompany.perfectstrangers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

public class Promocion {
    private int idPromocion;
    private String nombrePromo;
    private String tipoDescuento; // Porcentaje, Fijo, 2x1
    private double valorDescuento;
    private String fechaInicio;
    private String fechaFin;
    private String horaInicio;
    private String horaFin;
    private boolean aplicaLunes;
    private boolean aplicaMartes;
    private boolean aplicaMiercoles;
    private boolean aplicaJueves;
    private boolean aplicaViernes;
    private boolean aplicaSabado;
    private boolean aplicaDomingo;
    private Integer idProductoAfectado; // NULL si aplica a todos
    private boolean estadoPromo;

    // Datos adicionales (no almacenados en BD)
    private String nombreProducto;

    public Promocion() {
        this.aplicaLunes = true;
        this.aplicaMartes = true;
        this.aplicaMiercoles = true;
        this.aplicaJueves = true;
        this.aplicaViernes = true;
        this.aplicaSabado = true;
        this.aplicaDomingo = true;
        this.estadoPromo = true;
    }

    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }

    public String getNombrePromo() { return nombrePromo; }
    public void setNombrePromo(String nombrePromo) { this.nombrePromo = nombrePromo; }

    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }

    public double getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(double valorDescuento) { this.valorDescuento = valorDescuento; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public boolean isAplicaLunes() { return aplicaLunes; }
    public void setAplicaLunes(boolean aplicaLunes) { this.aplicaLunes = aplicaLunes; }

    public boolean isAplicaMartes() { return aplicaMartes; }
    public void setAplicaMartes(boolean aplicaMartes) { this.aplicaMartes = aplicaMartes; }

    public boolean isAplicaMiercoles() { return aplicaMiercoles; }
    public void setAplicaMiercoles(boolean aplicaMiercoles) { this.aplicaMiercoles = aplicaMiercoles; }

    public boolean isAplicaJueves() { return aplicaJueves; }
    public void setAplicaJueves(boolean aplicaJueves) { this.aplicaJueves = aplicaJueves; }

    public boolean isAplicaViernes() { return aplicaViernes; }
    public void setAplicaViernes(boolean aplicaViernes) { this.aplicaViernes = aplicaViernes; }

    public boolean isAplicaSabado() { return aplicaSabado; }
    public void setAplicaSabado(boolean aplicaSabado) { this.aplicaSabado = aplicaSabado; }

    public boolean isAplicaDomingo() { return aplicaDomingo; }
    public void setAplicaDomingo(boolean aplicaDomingo) { this.aplicaDomingo = aplicaDomingo; }

    public Integer getIdProductoAfectado() { return idProductoAfectado; }
    public void setIdProductoAfectado(Integer idProductoAfectado) { this.idProductoAfectado = idProductoAfectado; }

    public boolean isEstadoPromo() { return estadoPromo; }
    public void setEstadoPromo(boolean estadoPromo) { this.estadoPromo = estadoPromo; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public boolean esValidaAhora() {
        if (!estadoPromo) return false;
        try {
            LocalDate hoy = LocalDate.now();
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            if (hoy.isBefore(inicio) || hoy.isAfter(fin)) return false;

            LocalTime ahora = LocalTime.now();
            LocalTime hInicio = LocalTime.parse(horaInicio);
            LocalTime hFin = LocalTime.parse(horaFin);
            if (ahora.isBefore(hInicio) || ahora.isAfter(hFin)) return false;

            DayOfWeek dia = hoy.getDayOfWeek();
            return switch (dia) {
                case MONDAY -> aplicaLunes;
                case TUESDAY -> aplicaMartes;
                case WEDNESDAY -> aplicaMiercoles;
                case THURSDAY -> aplicaJueves;
                case FRIDAY -> aplicaViernes;
                case SATURDAY -> aplicaSabado;
                case SUNDAY -> aplicaDomingo;
            };
        } catch (Exception e) {
            return false; // Ante errores de parseo o datos nulos asume que no aplica
        }
    }

    public double calcularDescuento(double monto) {
        if (!esValidaAhora()) return 0;
        
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
