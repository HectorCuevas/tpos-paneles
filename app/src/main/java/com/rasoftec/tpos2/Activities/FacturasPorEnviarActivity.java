package com.rasoftec.tpos2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.Adapters.SincronizarAdapter;
import com.rasoftec.tpos2.Beans.FormatoFactura;
import com.rasoftec.tpos2.Data.database;
import java.util.ArrayList;

public class FacturasPorEnviarActivity extends AppCompatActivity {

    database dbObject;
    TextView res;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       try {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_sincronizar);
         //  res = findViewById(R.id.lblres);
           final ArrayList<FormatoFactura> formatoFacturas = new ArrayList<FormatoFactura>();
           ListView lstFacturas = findViewById(R.id.lstSinc);
           dbObject = new database(this);

           //dbObject.getFacturasPorEnviar(ApplicationTpos.currentEncabezado.getCodCliente());
           ArrayList<FormatoFactura> db = dbObject.getFacturasPorEnviar(ApplicationTpos.currentEncabezado.getCodFact());

           for (int i = 0; i < db.size(); i++) {
               FormatoFactura formatoFactura = new FormatoFactura();
               formatoFactura.setNombre(db.get(i).getNombre());
               formatoFactura.setCodigoArticulo(db.get(i).getCodigoArticulo());
               formatoFactura.setDepto(db.get(i).getDepto());
               formatoFactura.setZona(db.get(i).getZona());
               formatoFactura.setTotalFactura(db.get(i).getPrecioArticulo());
               formatoFactura.setMunicipio(db.get(i).getMunicipio());
               formatoFactura.setNumeroCel(db.get(i).getNumeroCel());
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent cambiarActividad = new Intent(getApplicationContext(), EncabezadoFacturaActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
}
