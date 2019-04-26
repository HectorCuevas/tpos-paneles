package com.rasoftec.tpos2;

/**
 * Created by raaa on 8/08/17.
 */

public class nodo_producto {

    String descripcion;
    String codigo;
    int stock;
    double precio;
    int compra;
    int numero_cel;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCompra() {
        return compra;
    }

    public void setCompra(int compra) {
        this.compra = compra;
    }

    public void setNumeroCel(int numero_cel) {
        this.numero_cel=numero_cel;
    }

    public int getNumerocel() {
        return numero_cel;
    }

    public nodo_producto(String c, String d, int s, double p, int c2, int numero_cel) {
        codigo = c;
        descripcion = d;
        stock = s;
        precio = p;
        compra = c2;
        this.numero_cel = numero_cel;

    }
    public nodo_producto() {

    }

    @Override
    public String toString() {
        String tem ="Numero tel "+ getNumerocel()+" "+ this.descripcion + "\n Stock" + " " + (getStock() - getCompra()) + "\t Cantidad" + " " + getCompra() + "\n Precio" + " " + getPrecio() + "\t Total Comprado" + " " + nodo_lista.round(getCompra() * getPrecio(), 2);

        return tem;
    }

}
