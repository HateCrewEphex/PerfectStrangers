package com.mycompany.perfectstrangers;

public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria; // Platillo, Bebidas, Complementos
    private byte[] imagen;
    private boolean disponible;

    public Producto() {
    }

    public Producto(String nombre, String descripcion, double precio, String categoria, byte[] imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.imagen = imagen;
        this.disponible = true;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getRutaImagen() {
        return null;
    }

    public void setRutaImagen(String rutaImagen) {
        this.imagen = null;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public boolean esPlatillo() {
        return "Platillo".equals(categoria);
    }

    public boolean esBebida() {
        return "Bebidas".equals(categoria);
    }

    public boolean esComplemento() {
        return "Complementos".equals(categoria);
    }

    @Override
    public String toString() {
        return nombre + " ($" + String.format("%.2f", precio) + ") [" + categoria + "]";
    }
}
