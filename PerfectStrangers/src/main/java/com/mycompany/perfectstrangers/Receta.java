package com.mycompany.perfectstrangers;

public class Receta {
    private int idReceta;
    private int idProducto;
    private int idInsumo;
    private double cantidadRequerida;
    private String descripcionReceta;

    // Datos adicionales para facilitar consultas (no almacenados en BD)
    private String nombreProducto;
    private String nombreInsumo;
    private String unidadMedida;

    public Receta() {
    }

    public Receta(int idProducto, int idInsumo, double cantidadRequerida) {
        this.idProducto = idProducto;
        this.idInsumo = idInsumo;
        this.cantidadRequerida = cantidadRequerida;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public double getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(double cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public String getDescripcionReceta() {
        return descripcionReceta;
    }

    public void setDescripcionReceta(String descripcionReceta) {
        this.descripcionReceta = descripcionReceta;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "idReceta=" + idReceta +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", nombreInsumo='" + nombreInsumo + '\'' +
                ", cantidadRequerida=" + cantidadRequerida +
                ", unidadMedida='" + unidadMedida + '\'' +
                '}';
    }
}
