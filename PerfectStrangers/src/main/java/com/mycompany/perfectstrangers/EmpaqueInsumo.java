package com.mycompany.perfectstrangers;

public class EmpaqueInsumo {
    private int idEmpaque;
    private int idIngrediente;
    private String nombreEmpaque;
    private double cantidadQueTrae;
    private String codBarrasEmpaque;

    public EmpaqueInsumo() {
    }

    public EmpaqueInsumo(int idIngrediente, String nombreEmpaque, double cantidadQueTrae, String codBarrasEmpaque) {
        this.idIngrediente = idIngrediente;
        this.nombreEmpaque = nombreEmpaque;
        this.cantidadQueTrae = cantidadQueTrae;
        this.codBarrasEmpaque = codBarrasEmpaque;
    }

    public int getIdEmpaque() {
        return idEmpaque;
    }

    public void setIdEmpaque(int idEmpaque) {
        this.idEmpaque = idEmpaque;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNombreEmpaque() {
        return nombreEmpaque;
    }

    public void setNombreEmpaque(String nombreEmpaque) {
        this.nombreEmpaque = nombreEmpaque;
    }

    public double getCantidadQueTrae() {
        return cantidadQueTrae;
    }

    public void setCantidadQueTrae(double cantidadQueTrae) {
        this.cantidadQueTrae = cantidadQueTrae;
    }

    public String getCodBarrasEmpaque() {
        return codBarrasEmpaque;
    }

    public void setCodBarrasEmpaque(String codBarrasEmpaque) {
        this.codBarrasEmpaque = codBarrasEmpaque;
    }
}