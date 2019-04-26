package com.rasoftec.tpos2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos2.data.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public  class transmition extends AsyncTask<Void, Void, Void>  {
    SoapObject enc, detalle;
    SoapPrimitive fahtocel;
    ProgressDialog pdialog;
    webservice wsCod;
    private static String  METHOD_NAME1 = "encabezado_insert";
    private static String SOAP_ACTION1 = "http://grupomenas.carrierhouse.us/wstposp/encabezado_insert";

    private static String NAMESPACE = "http://grupomenas.carrierhouse.us/wstposp/";
    private static String  SOAP_URL = "http://grupomenas.carrierhouse.us/wstops2/GetStockArtWS.asmx";
    @Override
    protected Void doInBackground(Void... voids) {
        double cob = 55.00;
        double t = 55.00;
        enc = new SoapObject(NAMESPACE, METHOD_NAME1);
        enc.addProperty("id_mov", 90004591);
        enc.addProperty("usuarioMov", "99204");
        enc.addProperty("codCliente", "99201001");
        enc.addProperty("forma_pag", "cont");
        enc.addProperty("total", Double.toString(t));
        enc.addProperty("procesado", "s");
        enc.addProperty("cobrado", Double.toString(cob));
        enc.addProperty("dpi", "1346789");
        enc.addProperty("nombre", "Norman");
        enc.addProperty("nit", "12345678");
        enc.addProperty("direccion", "Guatemala");
        enc.addProperty("municipio", "guatemala");
        enc.addProperty("departamento", "guatemala");
        enc.addProperty("zona", 9);
        enc.addProperty("email", "assdasd");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes=true;
        envelope.setOutputSoapObject(enc);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
        httpTransport.debug=true;
        try {
            httpTransport.call(SOAP_ACTION1, envelope);
            fahtocel = (SoapPrimitive) envelope.getResponse();
        } catch (Exception e) {
            e.getMessage();
        }finally{
            fahtocel.toString();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pdialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog.setMessage("Enc");
        pdialog.show();
    }
}
