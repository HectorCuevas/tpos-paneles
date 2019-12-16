package com.rasoftec.tpos2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.Adapters.EncabezadoFacturaAdapter;
import com.rasoftec.tpos2.Beans.FormatoFactura;
import com.rasoftec.tpos2.Beans.factura_encabezado;
import com.rasoftec.tpos2.Data.database;
import com.rasoftec.tpos2.Data.webservice;
import com.rasoftec.tpos2.Funciones.AlmacenarFactura;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;
import com.rasoftec.tpos2.menu_principal;
import com.rasoftec.tpos2.venta;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static com.rasoftec.ApplicationTpos.p;

public class EncabezadoFacturaActivity extends AppCompatActivity {

    webservice wsCod;
    String METHOD_NAME1 = "encabezado_insert";
    String METHOD_NAME2 = "detalle_insert";
    String SOAP_ACTION1 = "http://grupomenas.carrierhouse.us/wstposp/encabezado_insert";
    String SOAP_ACTION2 = "http://grupomenas.carrierhouse.us/wstposp/detalle_insert";

    String NAMESPACE = "http://grupomenas.carrierhouse.us/wstposp/";
    String SOAP_URL1 = "http://grupomenas.carrierhouse.us/wstposp/GetStockArtWS.asmx";
    String SOAP_URL = "http://grupomenas.carrierhouse.us/wstops2/GetStockArtWS.asmx";
    private ErrorRed error;
    database dbObjetc;
    ProgressDialog pdialog;
    SoapObject enc, detalle, encabezado;
    SoapPrimitive fahtocel, celtofah;
    database dbObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc_facturas);

        error = new ErrorRed(this);

        Button btnEnvio = findViewById(R.id.btnEnviar);

        btnEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!error.conexion() && error.existedatos() != 0) {
                        Toast.makeText(getApplicationContext(), "Por el Momento no se Cuenta con Internet para Iniciar el Sistema ", Toast.LENGTH_LONG).show();
                    } else {
                        CelsiusAsync celsiustofahr = new CelsiusAsync();
                        celsiustofahr.execute();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Por el Momento no se Cuenta con Internet para Iniciar el Sistema ", Toast.LENGTH_LONG).show();
                }
            }
        });

        ListView lst = findViewById(R.id.lstEncFacturas);

        final ArrayList<factura_encabezado> facturas = new ArrayList<factura_encabezado>();

        dbObject = new database(this);
        wsCod = new webservice(this);

        ArrayList<factura_encabezado> db = dbObject.getEncabezadoFacturasPorEnviar();

        for (int i = 0; i < db.size(); i++) {
            factura_encabezado encabezado = new factura_encabezado();
            encabezado.setNombre(db.get(i).getNombre());
            encabezado.setDireccion(db.get(i).getDireccion());
            encabezado.setDepto(db.get(i).getDepto());
            encabezado.setMunicipio(db.get(i).getMunicipio());
            encabezado.setZona(db.get(i).getZona());
            encabezado.setDpi(db.get(i).getDpi());
            encabezado.setTotalFact(db.get(i).getTotalFact());
            encabezado.setCodFact(db.get(i).getCodFact());
            facturas.add(encabezado);
        }

        EncabezadoFacturaAdapter encabezadoFacturaAdapter = new EncabezadoFacturaAdapter(this, facturas);
        lst.setAdapter(encabezadoFacturaAdapter);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent cambiarActividad = new Intent(getApplicationContext(), FacturasPorEnviarActivity.class);
                ApplicationTpos.currentEncabezado = facturas.get(position);
                //  Toast.makeText(getApplicationContext(),  ""+ApplicationTpos.currentEncabezado.getCodFact(), Toast.LENGTH_LONG ).show();
                startActivity(cambiarActividad);
                if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
                    startActivity(cambiarActividad);
                }
            }
        });
    }

    /*** Clase asincrona para transporte de datos ***/
    private class CelsiusAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            /*** REVISAR CONTEXTO DE LAS VARIABLES Y EL FINALLY AREGGLARLO ***/

            ArrayList<FormatoFactura> db = dbObject.getFacturasPorEnviar();
            int w = db.get(0).getDetalleFacturas().size();
            int cod = wsCod.movilizandome();
            String umov = "";
            for (int j = 0; j < db.size(); j++) {
                for (int k = 0; k < db.get(j).getDetalleFacturas().size(); k++) {
                    detalle = new SoapObject(NAMESPACE, METHOD_NAME2);
                    detalle.addProperty("id_mov", cod);
                    detalle.addProperty("usuarioMov", db.get(j).getDetalleFacturas().get(k).getUsuarioMovilizandome());
                    umov = db.get(j).getDetalleFacturas().get(k).getUsuarioMovilizandome();
                    detalle.addProperty("co_art", db.get(j).getDetalleFacturas().get(k).getCodigoArticulo());
                    detalle.addProperty("prec_vta", db.get(j).getDetalleFacturas().get(k).getPrecioArticulo().toString());
                    detalle.addProperty("cantidad", db.get(j).getDetalleFacturas().get(k).getCantidad());
                    detalle.addProperty("total_art", db.get(j).getDetalleFacturas().get(k).getTotalFactura().toString());
                    detalle.addProperty("telefono", db.get(j).getDetalleFacturas().get(k).getNumeroCel());
                    detalle.addProperty("serial", db.get(j).getDetalleFacturas().get(k).getSerial());
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(detalle);
                    HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL1);
                    try {
                        httpTransport.call(SOAP_ACTION2, envelope);
                        fahtocel = (SoapPrimitive) envelope.getResponse();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                // db.clear();
                encabezado = new SoapObject(NAMESPACE, METHOD_NAME1);
                encabezado.addProperty("id_mov", cod);
                encabezado.addProperty("usuarioMov", umov);
                encabezado.addProperty("codCliente", db.get(j).getCodCliente());
                encabezado.addProperty("forma_pag", "CONT");
                encabezado.addProperty("total", db.get(j).getTotalFactura().toString());
                encabezado.addProperty("procesado", "S");
                encabezado.addProperty("cobrado", db.get(j).getTotalFactura().toString());
                encabezado.addProperty("fecha", "2019-04-04");
                if ((db.get(j).getLatitude() == null) || db.get(j).getLongitude() == null) {
                    encabezado.addProperty("latitud", "0");
                    encabezado.addProperty("longitud", "0");
                } else {
                    encabezado.addProperty("latitud", db.get(j).getLatitude());
                    encabezado.addProperty("longitud", db.get(j).getLongitude());
                }
                encabezado.addProperty("dpi", db.get(j).getDpi());
                encabezado.addProperty("nombre", db.get(j).getNombre());
                encabezado.addProperty("nit", db.get(j).getNit());
                encabezado.addProperty("direccion", db.get(j).getDireccion());
                encabezado.addProperty("municipio", db.get(j).getMunicipio());
                encabezado.addProperty("departamento", db.get(j).getDepto());
                encabezado.addProperty("zona", db.get(j).getZona());
                encabezado.addProperty("email", db.get(j).getEmail());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(encabezado);
                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
                httpTransport.debug = true;
                try {
                    httpTransport.call(SOAP_ACTION1, envelope);
                    fahtocel = (SoapPrimitive) envelope.getResponse();
                    dbObject.limpiarEnvioFacturas();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Proceso exitoso", Toast.LENGTH_LONG).show();
            Intent cambiarActividad = new Intent(getApplicationContext(), menu_principal.class);
            startActivity(cambiarActividad);
            if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
                startActivity(cambiarActividad);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(EncabezadoFacturaActivity.this);
            pdialog.setMessage("Sincronizando...");
            pdialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent cambiarActividad = new Intent(getApplicationContext(), menu_principal.class);

        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
}
