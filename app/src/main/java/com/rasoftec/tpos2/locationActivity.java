package com.rasoftec.tpos2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.PreferenceManager;
import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.Funciones.AlmacenarFactura;
import com.rasoftec.tpos2.Preferences.LocationPreferences;
import com.rasoftec.tpos2.beans.FormatoFactura;
import com.rasoftec.tpos2.beans.detalleFactura;
import com.rasoftec.tpos2.beans.factura_encabezado;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static com.rasoftec.ApplicationTpos.detalleVenta;
import static com.rasoftec.ApplicationTpos.newFactura_encabezado;
import static com.rasoftec.ApplicationTpos.p;

public class locationActivity extends AppCompatActivity {
    EditText txtAddress, txtMunicipio, txtDepartamento, txtZona;
    database dbObjetc;
    ProgressDialog pdialog;
    SoapObject enc, detalle, encabezado;
    SoapPrimitive fahtocel, celtofah;
    webservice wsCod;
    String METHOD_NAME1 = "encabezado_insert";
    String METHOD_NAME2 = "detalle_insert";
    String SOAP_ACTION1 = "http://grupomenas.carrierhouse.us/wstposp/encabezado_insert";
    String SOAP_ACTION2 = "http://grupomenas.carrierhouse.us/wstposp/detalle_insert";
    Spinner spnMunicipios;
    Spinner spinner1;
    String fActual;
    Spinner spZona;
    Button btnPref;
    TextView lblZonaPref, lblMunPref, lblDeptoPref;
    String NAMESPACE = "http://grupomenas.carrierhouse.us/wstposp/";
    String SOAP_URL1 = "http://grupomenas.carrierhouse.us/wstposp/GetStockArtWS.asmx";
    String SOAP_URL = "http://grupomenas.carrierhouse.us/wstops2/GetStockArtWS.asmx";
    LocationManager locationManager;
    LocationListener locationListener;
    Double longitude = 0D;
    Spinner spnDireccionRecarga;
    Double latitude = 0D;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        //txtAddress = (EditText) findViewById(R.id.txtAddress);
        spnDireccionRecarga = (Spinner) findViewById(R.id.spnDireccionRecarga);
        lblDeptoPref = (TextView) findViewById(R.id.lblDeptoPref);
        lblMunPref = (TextView) findViewById(R.id.lblMunPref);
        lblZonaPref = (TextView) findViewById(R.id.lblZonaPref);
        btnPref = (Button) findViewById(R.id.btnConfigPrefs);

        dbObjetc = new database(this);
        wsCod = new webservice(this);


        //Set adapter from resource
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.zonas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnDireccionRecarga.setAdapter(adapter);

        /*** Verificacion de preferencias ***/
        try {
            if (PreferenceManager.checkPref(locationActivity.this, PreferenceManager.PREF_STR_DEPTO)) {
                String str = PreferenceManager.getPref(locationActivity.this, PreferenceManager.PREF_STR_DEPTO);
                lblDeptoPref.setText(str);
            } else {
                lblDeptoPref.setText("Guatemala");
            }

            if (PreferenceManager.checkPref(locationActivity.this, PreferenceManager.PREF_STR_MUN)) {
                String str = PreferenceManager.getPref(locationActivity.this, PreferenceManager.PREF_STR_MUN);
                lblMunPref.setText(str);
            } else {
                lblMunPref.setText("Guatemala");
            }

            if (PreferenceManager.checkPref(locationActivity.this, PreferenceManager.PREF_STR_ZONA)) {
                String str = PreferenceManager.getPref(locationActivity.this, PreferenceManager.PREF_STR_ZONA);
                lblZonaPref.setText(str);
            } else {
                lblZonaPref.setText("1");
            }


            btnPref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent changePrefs = new Intent(getApplicationContext(), LocationPreferences.class);
                    startActivity(changePrefs);
                    if (changePrefs.resolveActivity(getPackageManager()) != null) {
                        startActivity(changePrefs);
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }


    }



    public void addJsonArray() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usuario_movilizandome", dbObjetc.get_ruta().trim());
            jsonObject.put("cod_cliente", ApplicationTpos.codigoCliente);
            jsonObject.put("forma_pago", "CONT");
            jsonObject.put("total", ApplicationTpos.totalEncabezado);
            jsonObject.put("cobrado", ApplicationTpos.totalEncabezado);
            jsonObject.put("procesado", "S");
            jsonObject.put("num_factura", 0);
            jsonObject.put("cobrado_2", 0.00);
            //    jsonObject.put("fecha", getDate());
            /*** required fields ***/
            jsonObject.put("dpi", p.get(0).getDpi());
            jsonObject.put("nombre_cliente", p.get(0).getNombre());
            jsonObject.put("nit", p.get(0).getNit());
            jsonObject.put("direccion", p.get(0).getDireccion());
            jsonObject.put("municipio", p.get(0).getMunicipio());
            jsonObject.put("departamento", p.get(0).getDepto());
            jsonObject.put("zona", p.get(0).getZona());
            jsonObject.put("email", p.get(0).getEmail());
            jsonObject.put("latitud", p.get(0).getLatitude());
            jsonObject.put("longitud", p.get(0).getLongitude());
            storeDatabase(jsonObject);
        } catch (JSONException e) {
            /*Intent cambiarActividad = new Intent(getApplicationContext(), menu_principal.class);
            startActivity(cambiarActividad);
            if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
                startActivity(cambiarActividad);
            }*/
            Toast.makeText(this, "No se ha podido crear el objeto json", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void storeDatabase(JSONObject jsonObject) {
        try {
            dbObjetc.setVenta(detalleVenta, jsonObject);
        } catch (Exception e) {
            Toast.makeText(this, "No se ha podido crear el objeto BD json", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void checkout(View view) {
        try {
            //String address = txtAddress.getText().toString();
            String direccionRecarga = spnDireccionRecarga.getSelectedItem().toString();
            String municipio = lblMunPref.getText().toString();
            String departamento = lblDeptoPref.getText().toString();
            String zona = lblZonaPref.getText().toString();
            newFactura_encabezado.setDireccion(direccionRecarga);
            newFactura_encabezado.setMunicipio(municipio);
            newFactura_encabezado.setDepto(departamento);
            newFactura_encabezado.setZona(zona);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
        }

        p.add(newFactura_encabezado);
        addJsonArray();
        CelsiusAsync celsiustofahr = new CelsiusAsync();
        celsiustofahr.execute();

    }

    /*** Clase asincrona para transporte de datos ***/
    private class CelsiusAsync extends AsyncTask<Void, Void, Void> {
        AlmacenarFactura almacenarFactura = new AlmacenarFactura();

        @Override
        protected Void doInBackground(Void... params) {
            int cod = wsCod.movilizandome();
            for (int j = 0; j < ApplicationTpos.detalleFactura.size(); j++) {

            //    almacenarFactura.AlmacenarDetalle(ApplicationTpos.detalleFactura.get(j));
                detalle = new SoapObject(NAMESPACE, METHOD_NAME2);
                detalle.addProperty("id_mov", cod);
                detalle.addProperty("usuarioMov", dbObjetc.get_ruta().trim());
                detalle.addProperty("co_art", ApplicationTpos.detalleFactura.get(j).getCodigoArticulo());
                detalle.addProperty("prec_vta", ApplicationTpos.detalleFactura.get(j).getPrecioArticulo().toString());
                detalle.addProperty("cantidad", ApplicationTpos.detalleFactura.get(j).getCantidad());
                detalle.addProperty("total_art", ApplicationTpos.detalleFactura.get(j).getTotalFactura().toString());
                detalle.addProperty("telefono", ApplicationTpos.detalleFactura.get(j).getNumeroCel());
                detalle.addProperty("serial", "1234679");
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
            /*** Metodo para almacenar la factura temporalmente ***/
            AlmacenarEncabezado(dbObjetc.get_ruta().trim(), ApplicationTpos.codigoCliente, Double.toString(ApplicationTpos.totalEncabezado), p.get(0), ApplicationTpos.detalleFactura );ApplicationTpos.detalleFactura.clear();

            encabezado = new SoapObject(NAMESPACE, METHOD_NAME1);
            encabezado.addProperty("id_mov", cod);
            encabezado.addProperty("usuarioMov", dbObjetc.get_ruta().trim());
            encabezado.addProperty("codCliente", ApplicationTpos.codigoCliente);
            encabezado.addProperty("forma_pag", "CONT");
            encabezado.addProperty("total", Double.toString(ApplicationTpos.totalEncabezado));
            encabezado.addProperty("procesado", "S");
            encabezado.addProperty("cobrado", Double.toString(ApplicationTpos.totalEncabezado));
            encabezado.addProperty("fecha", "2019-04-04");
            if((p.get(0).getLatitude()==null) || (p.get(0).getLongitude()==null)){
                encabezado.addProperty("latitud", "0");
                encabezado.addProperty("longitud", "0");
            }else{
                encabezado.addProperty("latitud", p.get(0).getLatitude());
                encabezado.addProperty("longitud", p.get(0).getLongitude());
            }
            encabezado.addProperty("dpi", p.get(0).getDpi());
            encabezado.addProperty("nombre", p.get(0).getNombre());
            encabezado.addProperty("nit", p.get(0).getNit());
            encabezado.addProperty("direccion", p.get(0).getDireccion());
            encabezado.addProperty("municipio", p.get(0).getMunicipio());
            encabezado.addProperty("departamento", p.get(0).getDepto());
            encabezado.addProperty("zona", p.get(0).getZona());
            encabezado.addProperty("email", p.get(0).getEmail());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(encabezado);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
            httpTransport.debug = true;
            try {
                httpTransport.call(SOAP_ACTION1, envelope);
                fahtocel = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.getMessage();

            } finally {
          //      almacenarFactura.AlmacenarEncabezado(dbObjetc.get_ruta().trim(), ApplicationTpos.codigoCliente,Double.toString(ApplicationTpos.totalEncabezado), p.get(0), ApplicationTpos.detalleFactura);
               /* Toast.makeText(getApplicationContext(), "ERROR: No se pudo completar la transacción, revisar Pendientes de envio", Toast.LENGTH_SHORT).show();
                Intent cambiarActividad = new Intent(getApplicationContext(), menu_principal.class);
                startActivity(cambiarActividad);
                if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
                    startActivity(cambiarActividad);
                }*/
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
            pdialog = new ProgressDialog(locationActivity.this);
            pdialog.setMessage("Sincronizando...");
            pdialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent cambiarActividad = new Intent(this, DetailActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
    public void AlmacenarEncabezado(String usuarioMov, String codCliente, String totalEncabezado,
                                    factura_encabezado factura_encabezado, ArrayList<detalleFactura> detalleFacturas){
        factura_encabezado.setUsuarioMov(usuarioMov);
        factura_encabezado.setCodCliente(codCliente);
        factura_encabezado.setTotalFact(totalEncabezado);
        factura_encabezado.setCobrado(totalEncabezado);
        factura_encabezado.setForma_pag("CONT");

        dbObjetc.insertFacturaSinEnviar(detalleFacturas, factura_encabezado);
        FormatoFactura nformato = new FormatoFactura();
        nformato.setDetalleFacturas(detalleFacturas);
       // nformato.getDetalleFacturas().get(0).getCantidad();
    }
}