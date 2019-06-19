package com.rasoftec.tpos2.Funciones;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rasoftec.tpos2.beans.FormatoFactura;
import com.rasoftec.tpos2.beans.detalleFactura;
import com.rasoftec.tpos2.beans.factura_encabezado;
import com.rasoftec.tpos2.data.database;

import java.util.ArrayList;

public class AlmacenarFactura extends Activity {
    database dbObjetc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbObjetc = new database(this.getApplicationContext());
    }




}
