package com.rasoftec.tpos2.Data;

/**
 * Created by raaa on 22/08/17.
 */

public class venta_detalle {
    String producto;
    int cantidad;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    String nombre;

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    double precio;
    public venta_detalle(String nombre,String p,int c,double pp){
        this.producto=p; this.cantidad=c;this.precio=pp;
this.nombre=nombre;

    }
    @Override
    public String toString(){
        String tem="Producto"+" "+nombre+"\nPrecio"+" "+precio+" \t Cantidad"+" "+cantidad+"\nTotal"+" "+round(precio*cantidad,2);

        return tem;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
