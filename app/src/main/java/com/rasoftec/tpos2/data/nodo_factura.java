package com.rasoftec.tpos2.data;

/**
 * Created by raaa on 9/08/17.
 */

public class nodo_factura {
    public String getCodigo_factura() {
        return codigo_factura;
    }

    public void setCodigo_factura(String codigo_factura) {
        this.codigo_factura = codigo_factura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    String codigo_factura;
    String fecha;
    double saldo;


    double pago;

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    int cliente;
    public nodo_factura(String codigo,String fecha,double saldo,int cliente){
        this.codigo_factura=codigo;
        this.fecha=fecha;
        this.saldo=saldo;
        this.pago=0;
        this.cliente=cliente;

    }

    public nodo_factura(String codigo,String fecha,double saldo,int cliente,double pago){
        this.codigo_factura=codigo;
        this.fecha=fecha;
        this.saldo=saldo;
        this.pago=pago;
        this.cliente=cliente;

    }

    @Override
    public String toString(){
        String tem="Factura"+" "+codigo_factura+"\nFecha"+" "+fecha+" \nMonto Q"+" "+round(saldo,2)+"\tAbono Q"+" "+round(pago,2)+"\nSaldo Q"+" "+round(saldo-pago,2);

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
