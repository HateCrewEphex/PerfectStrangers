package com.mycompany.perfectstrangers;

public class Insumo {
    private int idInsumo;
    private String codigoBarras;
    private String nombreInsumo;
    private String descripcion;
    private String tipoProducto;
    private String ubicacion;
    private String unidadMedida; // Pieza, Kilogramo, Litro, Gramo
    private double cantidadActual;
    private double cantidadMinima;
    private double cantidadCritica;

    public Insumo() {
    }

    public Insumo(int idInsumo, String codigoBarras, String nombreInsumo,
                  double cantidadActual, double cantidadMinima, String unidadMedida) {
        this.idInsumo = idInsumo;
        this.codigoBarras = codigoBarras;
        this.nombreInsumo = nombreInsumo;
        this.cantidadActual = cantidadActual;
        this.cantidadMinima = cantidadMinima;
        this.unidadMedida = unidadMedida;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(double cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public double getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(double cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public double getCantidadCritica() {
        return cantidadCritica;
    }

    public void setCantidadCritica(double cantidadCritica) {
        this.cantidadCritica = cantidadCritica;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public boolean necesitaReorden() {
        return cantidadActual <= cantidadMinima;
    }

    public boolean estaEnCritico() {
        return cantidadActual <= cantidadCritica;
    }

    public int getIdIngrediente() {
        return getIdInsumo();
    }

    public void setIdIngrediente(int idIngrediente) {
        setIdInsumo(idIngrediente);
    }

    public String getCodBarras() {
        return getCodigoBarras();
    }

    public void setCodBarras(String codBarras) {
        setCodigoBarras(codBarras);
    }

    public double getStock() {
        return getCantidadActual();
    }

    public void setStock(double stock) {
        setCantidadActual(stock);
    }

    public double getAlertStock() {
        return getCantidadMinima();
    }

    public void setAlertStock(double alertStock) {
        setCantidadMinima(alertStock);
    }

    public double getCritStock() {
        return getCantidadCritica();
    }

    public void setCritStock(double critStock) {
        setCantidadCritica(critStock);
    }

    @Override
    public String toString() {
        return "Insumo{" +
                "idInsumo=" + idInsumo +
                ", nombreInsumo='" + nombreInsumo + '\'' +
                ", cantidadActual=" + cantidadActual +
                ", unidadMedida='" + unidadMedida + '\'' +
                '}';
    }
}
