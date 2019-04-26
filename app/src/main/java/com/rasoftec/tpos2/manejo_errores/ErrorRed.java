package com.rasoftec.tpos2.manejo_errores;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by raaa on 21/08/17.
 */

public class ErrorRed {
    Context a;
    public  ErrorRed(Context c){
  this.a=c;

    }

    public boolean conexion(){

        ConnectivityManager cm=(ConnectivityManager)a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo tem = cm.getActiveNetworkInfo();

        if(tem!=null && tem.isConnectedOrConnecting()){

            return true;
        }
        return false;

    }
    public int existedatos() throws IOException {
        int estado=-1;
        Runtime runtime = Runtime.getRuntime();
        Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 www.google.com");
        try {
            estado = mIpAddrProcess.waitFor();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return estado;

    }

}
