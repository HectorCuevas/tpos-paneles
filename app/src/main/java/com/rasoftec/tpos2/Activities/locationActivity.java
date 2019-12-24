package com.rasoftec.tpos2.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.PreferenceManager;
import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.Funciones.AlmacenarFactura;
import com.rasoftec.tpos2.Preferences.LocationPreferences;
import com.rasoftec.tpos2.Beans.FormatoFactura;
import com.rasoftec.tpos2.Beans.detalleFactura;
import com.rasoftec.tpos2.Beans.factura_encabezado;
import com.rasoftec.tpos2.Data.database;
import com.rasoftec.tpos2.Data.webservice;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;
import com.rasoftec.tpos2.menu_principal;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.rasoftec.ApplicationTpos.LLEVA_FOTO;
import static com.rasoftec.ApplicationTpos.detalleVenta;
import static com.rasoftec.ApplicationTpos.newFactura_encabezado;
import static com.rasoftec.ApplicationTpos.p;

public class LocationActivity extends AppCompatActivity {
    EditText txtAddress, txtMunicipio, txtDepartamento, txtZona;
    database dbObjetc;
    ProgressDialog pdialog;
    SoapObject enc, detalle, encabezado;
    SoapPrimitive fahtocel, celtofah;
    webservice wsCod;
    String METHOD_NAME1 = "encabezado_insert";
    String METHOD_NAME2 = "detalle_insert";
    String SOAP_ACTION1 = "http://grupomenas.carrierhouse.us/ws-imagenes/encabezado_insert";
    String SOAP_ACTION2 = "http://grupomenas.carrierhouse.us/ws-imagenes/detalle_insert";
    Button btnPref;
    TextView lblZonaPref, lblMunPref, lblDeptoPref;
    String NAMESPACE = "http://grupomenas.carrierhouse.us/ws-imagenes/";
    String SOAP_URL1 = "http://grupomenas.carrierhouse.us/ws-imagenes/GetStockArtWS.asmx";
    String SOAP_URL = "http://grupomenas.carrierhouse.us/ws-imagenes/GetStockArtWS.asmx";
    Double longitude = 0D;
    EditText txtDireccionRecarga;
    Double latitude = 0D;
    ImageView clear;
    private ErrorRed error;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        error = new ErrorRed(this);
        //txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtDireccionRecarga = (EditText) findViewById(R.id.txtDireccionRecarga);
        lblDeptoPref = (TextView) findViewById(R.id.lblDeptoPref);
        lblMunPref = (TextView) findViewById(R.id.lblMunPref);
        lblZonaPref = (TextView) findViewById(R.id.lblZonaPref);
        btnPref = (Button) findViewById(R.id.btnConfigPrefs);
        clear =(ImageView)  findViewById(R.id.clearDireccion);
        dbObjetc = new database(this);
        wsCod = new webservice(this);

        ImageView imageView = findViewById(R.id.imgPrueba);

       // Toast.makeText(this, String.valueOf(LLEVA_FOTO), Toast.LENGTH_SHORT).show();



        /*** Verificacion de preferencias ***/
        try {
            if (PreferenceManager.checkPref(LocationActivity.this, PreferenceManager.PREF_STR_DEPTO)) {
                String str = PreferenceManager.getPref(LocationActivity.this, PreferenceManager.PREF_STR_DEPTO);
                lblDeptoPref.setText(str);
            } else {
                lblDeptoPref.setText("Guatemala");
            }

            if (PreferenceManager.checkPref(LocationActivity.this, PreferenceManager.PREF_STR_MUN)) {
                String str = PreferenceManager.getPref(LocationActivity.this, PreferenceManager.PREF_STR_MUN);
                lblMunPref.setText(str);
            } else {
                lblMunPref.setText("Guatemala");
            }

            if (PreferenceManager.checkPref(LocationActivity.this, PreferenceManager.PREF_STR_ZONA)) {
                String str = PreferenceManager.getPref(LocationActivity.this, PreferenceManager.PREF_STR_ZONA);
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
       // byte[] blob= newFactura_encabezado.getImagen2();
        //Bitmap bmp= BitmapFactory.decodeByteArray(blob,0,blob.length);

         //imageView.setImageBitmap(bmp);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDireccionRecarga.getText().clear();
            }
        });

    }


    public void addJsonArray() {
        try {
            Date currentTime = Calendar.getInstance().getTime();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("usuario_movilizandome", dbObjetc.get_ruta().trim());
            jsonObject.put("cod_cliente", ApplicationTpos.codigoCliente);
            jsonObject.put("forma_pago", p.get(0).getForma_pag());
            jsonObject.put("total", ApplicationTpos.totalEncabezado);
            jsonObject.put("cobrado", ApplicationTpos.totalEncabezado);
            jsonObject.put("procesado", "S");
            jsonObject.put("num_factura", 0);
            jsonObject.put("cobrado_2", 0.00);
            jsonObject.put("fecha", p.get(0).getFecha());
          //  p.set(0).setFecha(currentTime);
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
            //para probar imagenes
            jsonObject.put("imagen", p.get(0).getImagen());
            jsonObject.put("imagen2", p.get(0).getImagen2());
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
            p.clear();
            //String address = txtAddress.getText().toString();
            String direccionRecarga =txtDireccionRecarga.getText().toString();
            String municipio = lblMunPref.getText().toString();
            String departamento = lblDeptoPref.getText().toString();
            String zona = lblZonaPref.getText().toString();
            newFactura_encabezado.setDireccion(direccionRecarga);
            newFactura_encabezado.setMunicipio(municipio);
            newFactura_encabezado.setDepto(departamento);
            newFactura_encabezado.setZona(zona);
            Date currentTime = Calendar.getInstance().getTime();
            newFactura_encabezado.setFecha(currentTime.toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
        }

        p.add(newFactura_encabezado);
        addJsonArray();


        try{
            if (!error.conexion()  && error.existedatos() != 0) {
                mensaje("Error de Red", "Por el Momento no se Cuenta con Internet para Iniciar el Sistema \n" +
                        "Revise pendientes de envio");
              //  Toast.makeText(getApplicationContext(),ApplicationTpos.totalEncabezado +"", Toast.LENGTH_LONG ).show();
                AlmacenarEncabezado(dbObjetc.get_ruta().trim(), ApplicationTpos.codigoCliente,
                        Double.toString(ApplicationTpos.totalEncabezado), p.get(0), ApplicationTpos.detalleFactura);
                //NO QUITAR ESTA LINEA -  LIMPIA EL DETALLE DEL CARRITO DEL ENCABEZADO
                ApplicationTpos.detalleFactura.clear();

            }else{
                CelsiusAsync celsiustofahr = new CelsiusAsync();
                celsiustofahr.execute();
            }
        }catch (Exception e){
            mensaje("Error de Red", "Por el Momento no se Cuenta con Internet para Iniciar el Sistema \n" +
                    "Revise pendientes de envio");
            AlmacenarEncabezado(dbObjetc.get_ruta().trim(), ApplicationTpos.codigoCliente,
                    Double.toString(ApplicationTpos.totalEncabezado), p.get(0), ApplicationTpos.detalleFactura);
        }

    }

    /*** Clase asincrona para transporte de datos ***/
    private class CelsiusAsync extends AsyncTask<Void, Void, Void> {
        AlmacenarFactura almacenarFactura = new AlmacenarFactura();

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
                detalle.addProperty("serial", ApplicationTpos.detalleFactura.get(j).getSerial());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(detalle);
                HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL1);
                try {
                    httpTransport.call(SOAP_ACTION2, envelope);
                    fahtocel = (SoapPrimitive) envelope.getResponse();
                } catch (Exception e) {
                   // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.getMessage();
                }
            }
            ApplicationTpos.detalleFactura.clear();
            encabezado = new SoapObject(NAMESPACE, METHOD_NAME1);
            encabezado.addProperty("id_mov", cod);
            encabezado.addProperty("usuarioMov", dbObjetc.get_ruta().trim());
            encabezado.addProperty("codCliente", ApplicationTpos.codigoCliente);
            encabezado.addProperty("forma_pag", p.get(0).getForma_pag());
            encabezado.addProperty("total", Double.toString(ApplicationTpos.totalEncabezado));
            encabezado.addProperty("procesado", "S");
            encabezado.addProperty("cobrado", Double.toString(ApplicationTpos.totalEncabezado));
            if ((p.get(0).getLatitude() == null) || (p.get(0).getLongitude() == null)) {
                encabezado.addProperty("lat", "0");
                encabezado.addProperty("longt", "0");
            } else {
                encabezado.addProperty("lat", p.get(0).getLatitude());
                encabezado.addProperty("longt", p.get(0).getLongitude());
            }
            encabezado.addProperty("dpi", p.get(0).getDpi());
            encabezado.addProperty("nombre", p.get(0).getNombre());
            encabezado.addProperty("nit", p.get(0).getNit());
            encabezado.addProperty("direccion", p.get(0).getDireccion());
            encabezado.addProperty("municipio", p.get(0).getMunicipio());
            encabezado.addProperty("departamento", p.get(0).getDepto());
            encabezado.addProperty("zona", p.get(0).getZona());
            encabezado.addProperty("email", p.get(0).getEmail());

            if((p.get(0).getImagen() != null) || (p.get(0).getImagen2() != null) ){
                String img1_base64 = android.util.Base64.encodeToString(p.get(0).getImagen(), Base64.DEFAULT);
                String img2_base64 = android.util.Base64.encodeToString(p.get(0).getImagen2(), Base64.DEFAULT);
                if(LLEVA_FOTO != 0){
                    encabezado.addProperty("imagen", img1_base64);
                    encabezado.addProperty("imagen2", img2_base64);
                }else{
                    encabezado.addProperty("imagen",null);
                    encabezado.addProperty("imagen2", null);
                }
            }


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(encabezado);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
            httpTransport.debug = true;
            try {
                httpTransport.call(SOAP_ACTION1, envelope);
                fahtocel = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
               // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.getMessage();
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
            pdialog = new ProgressDialog(LocationActivity.this);
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
                                    factura_encabezado factura_encabezado, ArrayList<detalleFactura> detalleFacturas) {
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
    private void mensaje(String enca, String cuer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v2 = inflater.inflate(R.layout.mensaje, null);


        builder.setView(v2)
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Intent cambiarActividad = new Intent(getApplicationContext(), menu_principal.class);
                        startActivity(cambiarActividad);
                        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
                            startActivity(cambiarActividad);
                        }
                        dialog.dismiss();
                    }
                });
        TextView codigo = (TextView) v2.findViewById(R.id.info_mensa);
        codigo.setText(enca);
        TextView codigo2 = (TextView) v2.findViewById(R.id.info_mensa2);
        codigo2.setText(cuer);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

}