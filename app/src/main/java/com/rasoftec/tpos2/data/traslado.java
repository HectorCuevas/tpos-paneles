package com.rasoftec.tpos2.Data;

import java.util.ArrayList;

/**
 * Created by raaa on 11/08/17.
 */

public class traslado {


    public int getCodigo_traslado() {
        return codigo_traslado;
    }



    public String getFecha() {
        return fecha;
    }



    public String getOrigen() {
        return origen;
    }



    public String getDestino() {
        return destino;
    }


    int codigo_traslado;
    String fecha;
    String origen;
    String destino;
    public   ArrayList<producto_translado> lista;
    public traslado(int codigo,String fecha,String origen,String destino){
        this.codigo_traslado=codigo;
        this.fecha=fecha;
        this.origen=origen;
        this.destino=destino;
        lista= new ArrayList<>();




    }

    @Override
    public String toString(){
        String tem="Codigo de Origen"+" "+this.getCodigo_traslado()+" Almacen Origen"+" "+this.getOrigen()+"\n Almacen Destino"+" "+getDestino()+"\n Fecha"+" " +getFecha();

        return tem;
    }

}
