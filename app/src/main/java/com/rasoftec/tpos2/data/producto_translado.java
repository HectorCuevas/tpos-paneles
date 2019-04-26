package com.rasoftec.tpos2.data;

/**
 * Created by raaa on 15/08/17.
 */

public class producto_translado {
    public String codigo_articulo;
    public String descripcion;
   public String  total;
    public producto_translado(String codigo,String descripcion ,String total){
        this.codigo_articulo=codigo;
        this.descripcion=descripcion;
        this.total=total;


    }

    @Override
    public String toString(){
        String tem= "Codigo de Articulo"+" "+codigo_articulo+"\n Descripcion"+" "+descripcion.trim()+"\n Total"+" " +total;

        return tem;
    }

}
