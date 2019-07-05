package com.rasoftec.tpos2.Data;

/**
 * Created by raaa on 9/08/17.
 */

public class detalle {
    String co_art;
    double prec_vta;
    int Cantidad;
    int numero_cel;

    public int getNumero_cel() {
        return numero_cel;
    }

    public void setNumero_cel(int numero_cel) {
        this.numero_cel = numero_cel;
    }



    public String getCo_art() {
        return co_art;
    }

    public void setCo_art(String co_art) {
        this.co_art = co_art;
    }

    public double getPrec_vta() {
        return prec_vta;
    }

    public void setPrec_vta(double prec_vta) {
        this.prec_vta = prec_vta;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public double getTotal_art() {
        return total_art;
    }



    public void setTotal_art(double total_art) {
        this.total_art = total_art;
    }

    double total_art;

    public detalle(String codigo,double precio,int cantidad,double total, int numero_cel){
        this.co_art=codigo;
        this.prec_vta=precio;
        this.Cantidad=cantidad;
        this.total_art=total;
        this.numero_cel=numero_cel;
    }
}
