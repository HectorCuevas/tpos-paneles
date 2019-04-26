package com.rasoftec.tpos2.data;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by raaa on 22/08/17.
 */

public class venta2 {
    private final int movilizandome;
    int codigo;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    String cliente;
    public ArrayList<venta_detalle> detalles;
    public  venta2(int codigo,String cliente,int movilizadomen){
        this.codigo=codigo;
        this.cliente=cliente;
        this.movilizandome=movilizadomen;
        detalles= new ArrayList<>();

    }

    @Override
    public String toString(){
        String tem="Venta #"+" "+codigo+" "+"ID:"+" "+movilizandome+"\nTotal Q"+" "+ nodo_factura.round(Double.parseDouble(total()),2)+"\nCliente"+" "+cliente;

        return tem;
    }

    private String total() {
        String valor;
        double total=0;
        Iterator<venta_detalle> t = detalles.iterator();
        while(t.hasNext()){
            venta_detalle t2 = t.next();
            total=total+t2.getCantidad()*t2.getPrecio();


        }
        valor=""+total;
        return valor;

    }
}
