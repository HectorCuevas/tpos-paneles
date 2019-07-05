package com.rasoftec.tpos2.Funciones;

import android.app.Activity;
import android.os.Bundle;

import com.rasoftec.tpos2.Data.database;

public class AlmacenarFactura extends Activity {
    database dbObjetc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbObjetc = new database(this.getApplicationContext());
    }




}
