package com.rasoftec.tpos2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.webservice;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;


public class login extends AppCompatActivity {
    private EditText pin;
    private JSONArray t;
    private ErrorRed error;
    private static String IMEI="";   //Guarda el IMEI DEL DISPOSITIVO.
database base;
    private ProgressDialog d;
    private static String yase="";
    private static final int PERMISO_READ_PHONE_STATE=100; //Bandera testigo de permiso para obtener el imei del telefono.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        configurar();
        error= new ErrorRed(this);





        estado_app();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void estado_app() {



    }

    private String getimei() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >=23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISO_READ_PHONE_STATE);
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return  tel.getDeviceId();
            }
        }
        return tel.getDeviceId();

    }

    private void configurar() {

        pin=(EditText)findViewById(R.id.pin);
        base=new database(this);
        TextView ver= (TextView) findViewById(R.id.version);
        ver.setText("Version 1.2.3");

    }


    public void click(View view) throws JSONException, IOException {

//
        try{
            if (pin.getText().length() > 0) {
                if(error.conexion()&&error.existedatos()==0)

                new webservice(this).execute("999", "0", getimei(), pin.getText().toString());
                else   mensaje("Error de Red","Por el Momento no se Cuenta con Internet para Iniciar el Sistema ");
            }else{
                mensaje("Pin", "No El Pin no Es Valido");

            }}catch (Exception e){

            mensaje("Error de Red","Por el Momento no se Cuenta con Internet para Iniciar el Sistema "+e.getLocalizedMessage());

        }
//           if(error.conexion()&&error.existedatos()==0) {
//
//               AsyncTask<String, Void, String> t12 = new webservice(this);
//
//               String t14 = null, t15 = null;
//               try {
////                    getimei()
//
//                   t14 = t12.execute("999", "0", getimei(), pin.getText().toString()).get();
//                   Log.i("info traida", t14);
//
//               } catch (InterruptedException e) {
//                   e.printStackTrace();
//               } catch (ExecutionException e) {
//                   e.printStackTrace();
//               }
//
//               JSONArray info = new JSONArray(t14);
//               JSONObject info_r = null;
//
//               Log.i("info traida revisar", "" + info.getString(0));
//               if (info.getString(0)=="null") {
//                   mensaje("No Registrado", "No Existe el usuario en TPOS o Pin Incorrecto");
//
//
//               } else {
//
//               if (info.length() > 0) {
//
//
//                   info_r = info.getJSONObject(0);
//                   if (info_r.get("Telefono").toString().equals("Puede vender solo ORGA")) {
//
//                       base.estado(1);
//
//                   } else {
//
//
//                       if (info_r.get("Usuario_Dsc").toString().equals("Bienvenido a TPOS...")) {
//                           base.limpiar_configuracion();
//
//                           base.insert_configuracion(info_r);
////                            int t16=Integer.parseInt(t15);
////                            base.set_movilizandome(t16);
//                           carga_clientes();
//                           carga_productos();
//                           obtener_facturas();
//                           base.estado(2);
//
//                       }
//// else{
////                           base.limpiar_configuracion();
////
////                           base.insert_configuracion(info_r);
////
////
////
//////
////                           carga_clientes();
////                           carga_productos();
////                           obtener_facturas();
////                           base.estado(2);
////
////                       }
//
//
////                        } else {
//
//                       //}
//                   }
//                   Log.i("info traida cliente", t14);
//
//                   Intent i2 = new Intent(this, menu_principal.class);
//
//                   startActivity(i2);
//                   finish();
//
//               } else {
//
//                   mensaje("No Registrado", "No Existe en POS o Pin Inorrecto");
//
//               }
//
//
//           }
//
//
//            }else{
////                No hay Conexion //
//            mensaje("Error de Red","Por el Momento no se Cuenta con Internet para Iniciar el Sistema ");
//
//           }
//            }
//
//
//
//
//
//
//
//


//        AsyncTask<String, Integer, String> tem = new LoadRecipesTask2().execute("999", "0", getimei(), pin.getText().toString());



    }


//    Area de Progress Bar


//    protected class LoadRecipesTask2 extends AsyncTask<String,Integer, String> {
//
//        @Override
//        protected void onPreExecute() {
//            d = new ProgressDialog(login.this);
//            d.setTitle("Iniciando Session");
//            d.setMessage("Esperar Mientras Se carga la Informacion ...");
//            d.setIndeterminate(true);
//            d.setCancelable(false);
//            d.setIcon(R.mipmap.icono);
//
//
//                    d.show();
//
//
//        }
//
//        @Override
//        protected String doInBackground(String...params) {
//
//
//
//            String url = "http://204.110.55.232/Tpos/PROSISCO_REST/api/Log_User";
//
//            HashMap tem = new HashMap();
//
//            tem.put("usuario_id",params[0]);
//            tem.put("Imei",params[2]);
//            tem.put("pin",params[3]);
//
//            Log.i("info de login",tem.toString());
//            RestTemplate restTemplate = new RestTemplate();
//            // Add the Jackson message converter
//            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//            publishProgress(5);
//            JSONArray t_c = restTemplate.postForObject(url, tem, JSONArray.class);
//            Log.i("info de login 2",t_c.toString());
//
//                return t_c.toString();
//
//
//
//        }
//
//
//        protected void onProgressUpdate(Void... values) {
//
////
////        this.barra.setMessage("Iniciando Sincronizacion");
////        this.barra.setIndeterminate(true);
////        this.barra.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////        this.barra.setCancelable(true);
////        this.barra.show();
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//
//            try {
//                info_logueo(result);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            if(d.isShowing())
//                        d.dismiss();
//
//
//
//            // 6
//        }

//        private void info_logueo(String t14) throws IOException, JSONException {
//            if(error.conexion()&&error.existedatos()==0) {
////
////               AsyncTask<String, Void, String> t12 = new webservice(this);
////
////               String t14 = null, t15 = null;
////               try {
//////                    getimei()
////
////                   t14 = t12.execute("999", "0", getimei(), pin.getText().toString()).get();
////                   Log.i("info traida", t14);
////
////               } catch (InterruptedException e) {
////                   e.printStackTrace();
////               } catch (ExecutionException e) {
////                   e.printStackTrace();
////               }
////
//               JSONArray info = new JSONArray(t14);
//               JSONObject info_r = null;
////
//               Log.i("info traida revisar", "" + info.getString(0));
//               if (info.getString(0)=="null") {
//                   mensaje("No Registrado", "No Existe el usuario en TPOS o Pin Incorrecto");
////
////
//               } else {
////
//               if (info.length() > 0) {
////
////
//                   info_r = info.getJSONObject(0);
//                   if (info_r.get("Telefono").toString().equals("Puede vender solo ORGA")) {
////
//                       base.estado(1);
////
//                   } else {
////
////
//                       if (info_r.get("Usuario_Dsc").toString().equals("Bienvenido a TPOS...")) {
//                           base.limpiar_configuracion();
////
//                           base.insert_configuracion(info_r);
//////                            int t16=Integer.parseInt(t15);
//////                            base.set_movilizandome(t16);
////                           carga_clientes();
////                           carga_productos();
////                           obtener_facturas();
//                         base.estado(2);
////
//                       }
////// else{
//////                           base.limpiar_configuracion();
//////
//////                           base.insert_configuracion(info_r);
//////
//////
//////
////////
//////                           carga_clientes();
//////                           carga_productos();
//////                           obtener_facturas();
//////                           base.estado(2);
//////
//////                       }
////
////
//////                        } else {
////
////                       //}
//                   }
////                   Log.i("info traida cliente", t14);
////
//                   Intent i2 = new Intent(login.this, menu_principal.class);
////
//                   startActivity(i2);
//                   finish();
////
//               } else {
////
////                   mensaje("No Registrado", "No Existe en POS o Pin Inorrecto");
////
//               }
////
////
//           }
////
////
//            }else{
//////                No hay Conexion //
//            mensaje("Error de Red","Por el Momento no se Cuenta con Internet para Iniciar el Sistema ");
//
//           }
////            }
////
//
//
//        }
//
//
//    }

//     Prueba de Progress Bar
    private void obtener_facturas() {

        AsyncTask<String, String, String> t18 = new webservice(this);

        String t14 = null;


            try {
                t14 = t18.execute("lista","10").get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }




    }

    private void mensaje(String enca,String cuer) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.mensaje, null);




        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        dialog.dismiss();
                    }
                })

//                .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        dialog.cancel();
//                    }
//                })
        ;


        TextView codigo = (TextView) v2.findViewById(R.id.info_mensa);
        codigo.setText(enca);
        TextView codigo2 = (TextView) v2.findViewById(R.id.info_mensa2);
        codigo2.setText(cuer);


        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void carga_productos() throws JSONException {
        AsyncTask<String, String, String> t12 = new webservice(this);

        String t14 = null;
        try {
            t14 = t12.execute(base.get_ruta().trim().toString(),"2").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JSONArray t6=new JSONArray(t14);
            base.limpiar_producto();
            for(int i=0;i<t6.length();i++) {
                JSONObject t7 = null;
                try {
                    t7 = t6.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    base.insertproducto(t7);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.i("Fue insertado",""+exito);


            }



        }

    private void carga_clientes() {
        AsyncTask<String, String, String> t12 = new webservice(this);


        String t14 = null;
        try {
            Log.i("Revisar Ruta",base.get_ruta());
            t14 = t12.execute(base.get_ruta().trim().toString(),"8").get();


          base.limpiar_cliente();
            JSONArray info=new JSONArray(t14);
            for(int i=0;i<info.length();i++){
                JSONObject tem= info.getJSONObject(i);

                base.insertcliente(tem);






            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //Manejo de resultados para solicitud de acceso de permisos al dispositivo.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISO_READ_PHONE_STATE :

                if (permissions[0].toString().equals(Manifest.permission.READ_PHONE_STATE)){
                    if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        IMEI=getimei();
                    }
                    else { //No acepto darle permisos a la aplicacion, se le dara la oportunidad una vez mas..
                        View view = View.inflate(this,R.layout.mensaje,null);
                        TextView encabezado = (TextView)view.findViewById(R.id.info_mensa);
                        TextView cuerpo = (TextView)view.findViewById(R.id.info_mensa2);
                        encabezado.setText("Error de permisos");
                        cuerpo.setText("Usted debe de conceder los permisos para que la app funcione.\n"+
                                "Desea darle permisos ahora?\n");

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setView(view);
                        builder.setPositiveButton("Dar permisos", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" +getPackageName()));
                                /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);*/
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No dar Permisos", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                IMEI="";
                            }
                        });
                        builder.show();



                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


}
