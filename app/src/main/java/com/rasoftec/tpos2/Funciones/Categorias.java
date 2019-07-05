package com.rasoftec.tpos2.Funciones;

import android.app.Application;
import android.widget.Switch;

import com.rasoftec.tpos2.nodo_producto;
import com.rasoftec.tpos2.venta;

public class Categorias extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        venta venta = new venta();
    }

   /* public String cambiarNumeroPorCantidad(String codigo, int cantidad, int numeroCel, boolean esTelefono){
        if(isPhoneNumber){
            cantidad = 1;

        }
        return 1;
    }*/


    public String setCategorias(String codigo){
        final String sbCodigo = codigo.substring(0, 2);
        switch(sbCodigo){
            case "S1":
              //  venta.esRecarga();
                break;
            case "A1":
                break;
            case "S2":
                break;
            case "O1":
                break;
            default:
                break;
        }
        return sbCodigo;
    }
    public String getCategoria(String codigo){
        final String sbCodigo = codigo.substring(0, 2);
        switch(sbCodigo){
            case "S1":
                //validatePhoneNumber(cantidad, false);
                break;
            case "A1":
                break;
            case "S2":
                break;
            case "O1":
                break;
            default:
                break;
        }
        return sbCodigo;
    }
    public boolean validarSiEsNumero(String codigo){
        boolean esTelefono;
        switch(codigo) {
            case "S1":
               esTelefono = true;
                break;
            default:
                esTelefono = false;
                break;
        }
        return esTelefono;
    }
    public boolean esRecarga(String codigo){

        return true;
    }

}
