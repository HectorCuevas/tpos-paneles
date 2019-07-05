package com.rasoftec.tpos2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.Adapters.SincronizarAdapter;
import com.rasoftec.tpos2.beans.FormatoFactura;
import com.rasoftec.tpos2.data.database;

import java.util.ArrayList;

public class FacturasPorEnviarActivity extends AppCompatActivity {

    database dbObject;
    TextView res;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       try {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_sincronizar);
           res = findViewById(R.id.lblres);
           final ArrayList<FormatoFactura> formatoFacturas = new ArrayList<FormatoFactura>();
           ListView lstFacturas = findViewById(R.id.lstSinc);
           dbObject = new database(this);
           dbObject.getFacturasPorEnviar();
         //  Toast.makeText(this,  dbObject.getFacturasPorEnviar().get(0).toString(), Toast.LENGTH_LONG).show();
           for (int i = 0; i < dbObject.getFacturasPorEnviar().size(); i++) {
               FormatoFactura formatoFactura = new FormatoFactura();
               formatoFactura.setNombre(dbObject.getFacturasPorEnviar().get(i).getNombre());
               formatoFactura.setCodigoArticulo(dbObject.getFacturasPorEnviar().get(i).getCodigoArticulo());
               formatoFactura.setDepto(dbObject.getFacturasPorEnviar().get(i).getDepto());
               formatoFactura.setZona(dbObject.getFacturasPorEnviar().get(i).getZona());
               formatoFactura.setTotalFactura(dbObject.getFacturasPorEnviar().get(i).getPrecioArticulo());
               formatoFacturas.add(formatoFactura);
           }


           SincronizarAdapter sincronizarAdapter = new SincronizarAdapter(this, formatoFacturas);
           lstFacturas.setAdapter(sincronizarAdapter);
/*
           lstFacturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               }
           });*/


       }catch (Exception ex){
           Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
       }
    }

}
