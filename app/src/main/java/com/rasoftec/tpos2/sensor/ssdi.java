package com.rasoftec.tpos2.sensor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.rasoftec.tpos2.Data.database;

/**
 * Created by raaa on 9/08/17.
 */

public class ssdi {



    public void consulta_saldo(String numero, Context co){


        int final2=numero.length();
        int  inicio=final2-4;

        String tem_num=numero.substring(inicio,final2);
        Log.i("Numero del telefono",tem_num);
        String usd="*"+"977"+"*2*"+tem_num+ "%23";
        co.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" +usd)));



    }
    public void  get_consulta_saldo(String numero,Context co){
        database tem=new database(co);

        String num2=tem.get_telefono();
        int final2=num2.length();
        int  inicio=final2-4;
        String tem2=tem.saldo_pdv();
        tem2=tem2.replace("<pos tienda>",numero);
        Log.i("Valor Actual de pdv",tem2);
        String pin=num2.substring(inicio,final2);

        String usd=tem2+ "%23";
        Log.i("numero del pin 2",usd);
        co.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" +usd)));

    }

    public void consulta_saldo_telefono(String numero, Context co) {

        database base= new database(co);

        String usd=base.get_saldo_ss()+"%23";//"*"+"977"+"*2*"+numero+"%23";
        co.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" +usd)));

    }

    public boolean recarga_saldo(String pos_pdv, Context  venta,String cantidad) {

        database tem=new database(venta);




        String recarga=tem.get_recarga();
        double f_orga=tem.get_f_orga();
        double tem2= f_orga* Integer.parseInt(cantidad);
         int envio= (int) Math.floor(tem2);
        recarga=recarga.replace("<pos tienda>",pos_pdv);
        recarga=recarga.replace("<cantidad>",""+envio);
        String usd=recarga+"%23";
//        String usd="*"+"977"+"*2*"+pin+ "%23";

        Log.i("f_orga valor",""+envio);
        venta.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" +usd)));

        return true;

    }
}
