
package com.rasoftec.tpos2;

import org.ksoap2.SoapEnvelope;

import android.app.ProgressDialog;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.webservice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static android.support.v4.content.FileProvider.getUriForFile;
import static com.rasoftec.ApplicationTpos.detalleVenta;
import static com.rasoftec.ApplicationTpos.newFactura_encabezado;
import static com.rasoftec.ApplicationTpos.p;


public class PhotoActivity extends AppCompatActivity {

    private final String CARPETA_RAIZ = "misImagenesPrueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";

    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;
    Bitmap bitmap;
    database base;
    Button botonCargar;
    ImageView imagen;
    String path;
    database dbObjetc;
    byte[] byteArray;

    //EditText ed_input;
    //RadioGroup rg_temp;
    //RadioButton radioTemp;
    //Button btn_convert;
    String tempValue;
    ProgressDialog pdialog;
    SoapObject enc, detalle, encabezado;
    SoapPrimitive fahtocel, celtofah;
    webservice wsCod;
    String METHOD_NAME1 = "encabezado_insert";
    String METHOD_NAME2 = "detalle_insert";
    String SOAP_ACTION1 = "http://grupomenas.carrierhouse.us/wstposp/encabezado_insert";
    String SOAP_ACTION2 = "http://grupomenas.carrierhouse.us/wstposp/detalle_insert";

    String NAMESPACE = "http://grupomenas.carrierhouse.us/wstposp/";
    String SOAP_URL1 = "http://grupomenas.carrierhouse.us/wstposp/GetStockArtWS.asmx";
    String SOAP_URL = "http://grupomenas.carrierhouse.us/wstops2/GetStockArtWS.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        imagen = (ImageView) findViewById(R.id.imagemId);
        botonCargar = (Button) findViewById(R.id.btnCargarImg);
        dbObjetc = new database(this);
        wsCod = new webservice(this);

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
            storeDatabase(jsonObject);
        } catch (JSONException e) {
            Toast.makeText(this, "No se ha podido crear el objeto json", Toast.LENGTH_SHORT).show();
        }
    }

    public void storeDatabase(JSONObject jsonObject) {
        try {
            dbObjetc.setVenta(detalleVenta, jsonObject);
        } catch (Exception e) {
            Toast.makeText(this, "No se ha podido crear el objeto json", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkout(View view) {
        addJsonArray();
        CelsiusAsync celsiustofahr = new CelsiusAsync();
        celsiustofahr.execute();
        Toast.makeText(this, "Hecho", Toast.LENGTH_SHORT).show();
        //
        // Toast.makeText(get, "Almacenado con exito", Toast.LENGTH_SHORT).show();
        /*Intent cambiarActividad = new Intent(this, PhotoActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }*/
       /* Intent i = new Intent(venta.this, menu_principal.class);
        nodo_producto t2 = existeorga(carrito);
        if (t2 != null) {
            //           Log.i("Existe","si");
            total_pos = t2.getCompra();
            orga();
        } else {
            i.putExtra("ruta", ruta);
            startActivity(i);
        }
        info("Se Realizo la Venta con Exito");
        base.setcliente(actual);*/

    }

    private class CelsiusAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int cod = wsCod.movilizandome();
            for (int j = 0; j < ApplicationTpos.detalleFactura.size(); j++) {
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
            encabezado = new SoapObject(NAMESPACE, METHOD_NAME1);
            encabezado.addProperty("id_mov", cod);
            encabezado.addProperty("usuarioMov", "99204");
            encabezado.addProperty("codCliente", "99201001");
            encabezado.addProperty("forma_pag", "CONT");
            encabezado.addProperty("total", "54");
            encabezado.addProperty("procesado", "S");
            encabezado.addProperty("cobrado", "54");
            encabezado.addProperty("fecha", "2019-04-01");
            encabezado.addProperty("dpi", "1346789");
            encabezado.addProperty("nombre", "asd");
            encabezado.addProperty("nit", "1235");
            encabezado.addProperty("direccion", "guatemala");
            encabezado.addProperty("municipio", "guatemala");
            encabezado.addProperty("departamento", "guatemala");
            encabezado.addProperty("zona", 9);
            encabezado.addProperty("email", "assdasd");
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
                fahtocel.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Enc success", Toast.LENGTH_LONG).show();
            Intent cambiarActividad = new Intent(getApplicationContext(), menu_principal.class);
            startActivity(cambiarActividad);
            if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
                startActivity(cambiarActividad);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(PhotoActivity.this);
            pdialog.setMessage("Sincronizando...");
            pdialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent cambiarActividad = new Intent(getApplicationContext(), DetailActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
}
