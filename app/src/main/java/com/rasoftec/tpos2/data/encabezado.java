package com.rasoftec.tpos2.Data;

import java.util.ArrayList;

/**
 * Created by raaa on 8/08/17.
 */

public class encabezado {


    public String usuario_movilizandome;
    public int cod_cli;
    public String forma_pag;
    public double total;
    public double cobrado;
    public String Procesado;
    public int fac_num2;
    public double Cobrado2;
    public ArrayList<detalle> detalle;
    public int encabezado;
    public  encabezado(String usuario_movilizandome,int cod_cli,String forma_pag,double total,double cobrado,String procesado,int fac_num2,double cobrado2,int encabe){
        this.usuario_movilizandome=usuario_movilizandome;
        this.forma_pag=forma_pag;
        this.total=total;
        this.cobrado=cobrado;
        this.Procesado=procesado;
        this.fac_num2=fac_num2;
        this.Cobrado2=cobrado2;
        this.encabezado=encabe;
        this.cod_cli=cod_cli;
        this.detalle= new ArrayList<>();
    }


}
