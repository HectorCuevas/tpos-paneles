package com.rasoftec.tpos2;

import android.text.Html;

/**
 * Created by raaa on 7/08/17.
 */


    public class Nodo_tarea {
     public String cod_cliente;
    public Double credito;
    public String descripcion;
    public String direccion;
    public String telefono;
    public String fax;
    public String encargado;
        public Nodo_tarea(String codigo,Double credito,String descripcion,String direccion,String telefono,String fax,String encargado){
          this.cod_cliente=codigo;
            this.credito=credito;
            this.descripcion=descripcion;
            this.direccion=direccion;
            this.telefono=telefono;
            this.fax=fax;
            this.encargado=encargado;


        }
        @Override
        public String toString(){
            String tem=String.valueOf(Html.fromHtml("<b>"+this.descripcion+"</b>"+"\n"+ "\n"+this.direccion+"\n"+"\n"+this.telefono+"\n"));

            return tem;
        }

    }




