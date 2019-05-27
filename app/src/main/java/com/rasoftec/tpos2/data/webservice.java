package com.rasoftec.tpos2.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.Nodo_tarea;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;
import com.rasoftec.tpos2.menu_principal;
import com.rasoftec.tpos2.nodo_producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by raaa on 10/08/17.
 */

public class webservice extends AsyncTask<String, String, String> {
    ArrayList<nodo_producto> carro = new ArrayList<>();
    ArrayList<nodo_factura> factural = new ArrayList<>();
    database base;
    Context actual;
    ErrorRed errored, ero, error;
    ProgressDialog barra;
    private View v2;

    public webservice(ArrayList<nodo_producto> carrito, Context c) {

        carro = carrito;
        this.actual = c;
        base = new database(this.actual);
        errored = new ErrorRed(this.actual);
        ero = new ErrorRed(this.actual);

    }


    public webservice(ArrayList<nodo_factura> facturas_actual, String ruta, Context c) {


        factural = facturas_actual;
        this.actual = c;
        base = new database(this.actual);
        errored = new ErrorRed(this.actual);
        ero = new ErrorRed(this.actual);

    }

    public webservice(Context c) {
        this.actual = c;
        base = new database(this.actual);
        errored = new ErrorRed(this.actual);
        ero = new ErrorRed(this.actual);
        error = new ErrorRed(this.actual);
        this.barra = new ProgressDialog(this.actual);
    }

    public webservice(Context c, ProgressDialog d) {
        this.actual = c;
        this.barra = d;
        base = new database(this.actual);
        errored = new ErrorRed(this.actual);
        ero = new ErrorRed(this.actual);


    }

    @Override
    protected void onPostExecute(String s) {


        Log.i("Info traida", s);
        if (barra.isShowing())
            barra.dismiss();

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String[] tem2 = values;
        this.barra.setTitle("" + tem2[1]);
        this.barra.setMessage("" + tem2[0]);


    }

    @Override
    protected void onPreExecute() {

        this.barra = new ProgressDialog(actual);
        this.barra.setTitle("Iniciando");
        this.barra.setMessage("Esperar Mientras Se carga la Informacion  ");
        this.barra.setIndeterminate(true);
        this.barra.setCancelable(false);
        this.barra.setIcon(R.mipmap.icono);
        this.barra.show();


    }

    @Override
    protected String doInBackground(String... params) {


        JSONArray t_c = null;
        String url = "";
        RestTemplate restTemplate;
        double total_encabezado = 0.00;


        if (params[1].equals("0")) {
            int mov = movilizandome();
            t_c = logueo(params);

            Log.i("info de login 2", t_c.toString());
            try {
                info_logueo(t_c);
            } catch (IOException e) {

                try {
                    publishProgress("Error el Login No  ", "Error en Conexion");
                    Thread.sleep(4000);
                } catch (InterruptedException e2) {

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                t_c.put(1, mov);
            } catch (JSONException e) {

                publishProgress("Error el Login   ", "Error en Conexion");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }


        } else if (params[1].equals("1")) {
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/ret_lst_saldo_limite_de_credito_y_facturas_del_cliente/" + params[0];


            restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            t_c = restTemplate.getForObject(url, JSONArray.class);


        } else if (params[1].equals("2")) {
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/lst_productos_de_la_ruta/" + params[0];

            try {
                restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                t_c = restTemplate.getForObject(url, JSONArray.class);
            } catch (Exception e) {
                publishProgress("Error en Reporte", "Reporte Error");

            }

        } else if (params[1].equals("3")) {
            //
            //            iniciamos obteniendo el codigo de movilizandome
            int codigo_movi = movilizandome();

            //          Agregamos el detalle ///
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_insert_movilizandome_detalle";
            Iterator<nodo_producto> ite = carro.iterator();
            while (ite.hasNext()) {
                HashMap tem = new HashMap();
                nodo_producto t14 = ite.next();
                tem.put("id_movilizandome", codigo_movi);
                tem.put("co_art", t14.getCodigo());
                tem.put("prec_vta", t14.getPrecio());
                tem.put("Cantidad", t14.getCompra());
                double total = t14.getPrecio() * t14.getCompra();
                tem.put("total_art", t14.getNumerocel());
                Log.i("Detalle cuerpo", tem.toString());
                restTemplate = new RestTemplate();
                // Add the Jackson message converter
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                total_encabezado += total;
                t_c = restTemplate.postForObject(url, tem, JSONArray.class);
            }
            Log.i("Detalle Ingresado", t_c.toString());
            // Inicia el Area de Encabezado
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_insert_movilizandome_encabezado/";
            String tipo = "CRED";
            double cobrado = 0.00;
            if (params[3] != "vacio") {
                cobrado = Double.parseDouble(params[3].trim());
                JSONObject t2 = new JSONObject();
                try {
                    t2.put("codigo", 14);
                    t2.put("monto", total_encabezado);
                    t2.put("cliente", String.valueOf(params[2]));
                    t2.put("pago", cobrado);
                    base.cobro(t2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (cobrado == total_encabezado)
                    tipo = "CONT";
                else if (cobrado < total_encabezado)
                    tipo = "CRED";


            }

            HashMap tem = new HashMap();
            tem.put("id_movilizandome", codigo_movi);
            tem.put("usuario_movilizandome", base.get_ruta());
            tem.put("co_cli", String.valueOf(params[2]));
            tem.put("forma_pag", tipo);
            tem.put("total", total_encabezado);
            tem.put("cobrado", cobrado);
            tem.put("Procesado", "S");
            tem.put("fact_num2", 0);
            tem.put("Cobrado2", 0.0);


            Log.i("info", "" + tem.toString());
            Log.i("codigo de molizandome", "" + codigo_movi);
            restTemplate = new RestTemplate();
            // Add the Jackson message converter
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            t_c = restTemplate.postForObject(url, tem, JSONArray.class);
            Log.i("Encabezado Ingresado", t_c.toString());


        } else if (params[1].equals("4")) {

            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Ret_Lst_reporte_diario/" + base.get_ruta().trim().toString();


            restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            t_c = restTemplate.getForObject(url, JSONArray.class);


        } else if (params[1].equals("5")) {
            //                 Area para Generar el codigo de movilizando me


            //              Area del Envio de Pago actia;//
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_insert_movilizandome_encabezado/";


            Iterator<nodo_factura> ite = factural.iterator();
            restTemplate = new RestTemplate();
            while (ite.hasNext()) {
                int codigo_movi = movilizandome();

                HashMap tem = new HashMap();
                nodo_factura t14 = ite.next();
                if (t14.getPago() > 0) {

                    tem.put("id_movilizandome", codigo_movi);
                    tem.put("usuario_movilizandome", base.get_ruta());
                    tem.put("co_cli", String.valueOf(t14.getCliente()));
                    tem.put("forma_pag", "CRED");
                    tem.put("total", 0);
                    tem.put("cobrado", 0);
                    tem.put("Procesado", "S");
                    tem.put("fact_num2", t14.getCodigo_factura());
                    tem.put("Cobrado2", t14.getPago());

                    // Add the Jackson message converter
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                    t_c = restTemplate.postForObject(url, tem, JSONArray.class);


                }

            }


            //              restTemplate = new RestTemplate();
            //                // Add the Jackson message converter
            //                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //
            //                t_c=restTemplate.postForObject(url,tem, JSONArray.class);


        } else if (params[1].equals("6")) {
            //            Area de Translados
            publishProgress("Cargando Translado", "Translado");
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Ret_Lst_Traslados_pendientes_de_confirmar/" + base.get_ruta().trim().toString();
            restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            t_c = restTemplate.getForObject(url, JSONArray.class);


        } else if (params[1].equals("7")) {
            publishProgress("Sincronizando Translado", "Translado");
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_Ret_Confirma_Traslados/";
            HashMap tem = new HashMap();
            int codigo = Integer.parseInt(params[0]);
            tem.put("no_traslado", codigo);
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            t_c = restTemplate.postForObject(url, tem, JSONArray.class);

        } else if (params[1].equals("8")) {

            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Ret_Lst_clientes_fuera_de_ruta/" + params[0];
//                Log.i("Revisar Ruta de envio",url);

            restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            t_c = restTemplate.getForObject(url, JSONArray.class);

        } else if (params[1].equals("9")) {
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/log_user";

            HashMap tem = new HashMap();

            tem.put("usuario_id", params[0]);
            tem.put("Imei", params[2]);
            tem.put("pin", params[3]);


            restTemplate = new RestTemplate();
            // Add the Jackson message converter
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            t_c = restTemplate.postForObject(url, tem, JSONArray.class);
            //  kwcomunicacion  188

        } else if (params[1].equals("10")) {
            ArrayList<nodo_factura> envio_factura = new ArrayList<>();
            ArrayList<Nodo_tarea> t12 = base.getallcliente_fac();
            Iterator<Nodo_tarea> t13 = t12.iterator();
            while (t13.hasNext()) {
                Nodo_tarea t14 = t13.next();
                url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Ret_Lst_Saldo_limite_de_credito_y_facturas_del_cliente/" + t14.cod_cliente;


                restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                t_c = restTemplate.getForObject(url, JSONArray.class);


                if (t_c.length() > 0) {
                    base.limpiar_cobro();

                    for (int i = 0; i < t_c.length(); i++) {

                        try {
                            JSONObject tem2 = t_c.getJSONObject(i);


                            nodo_factura tem = new nodo_factura(tem2.getString("fact_num"), tem2.getString("fec_emis"), tem2.getDouble("saldo"), tem2.getInt("co_cli"));
                            envio_factura.add(tem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }
            }


            base.cobro(envio_factura);
        } else if (params[1].equals("20")) {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (params[1].equals("11")) {

//                Area de envio de Cobros

            url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_insert_movilizandome_encabezado/";


            Log.i("Valor de Pago_", "aqui estoy reviso");
            ArrayList<encabezado> lista = base.get_encabezado_cobro();

            Iterator<encabezado> ite = lista.iterator();
            restTemplate = new RestTemplate();
            while (ite.hasNext()) {
                int codigo_movi = movilizandome();
                Log.i("codigo movilizandome", "" + codigo_movi);
                HashMap tem = new HashMap();
                encabezado t14 = ite.next();


                tem.put("id_movilizandome", codigo_movi);
                tem.put("usuario_movilizandome", t14.usuario_movilizandome);
                tem.put("co_cli", String.valueOf(t14.cod_cli));
                tem.put("forma_pag", t14.forma_pag);
                tem.put("total", t14.total);
                tem.put("cobrado", t14.cobrado);
                tem.put("Procesado", t14.Procesado);
                tem.put("fact_num2", t14.fac_num2);
                tem.put("Cobrado2", t14.Cobrado2);
                Log.i("envio Pago", tem.toString());
                // Add the Jackson message converter
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                t_c = restTemplate.postForObject(url, tem, JSONArray.class);
                Log.i("Valor de Pago", t_c.toString());
                base.set_encabezado(t14.encabezado);


                ///  );


            }


//                Area de envio de Ventas
            lista = base.get_encabezado_venta();
            ite = lista.iterator();
            while (ite.hasNext()) {
                int codigo_movi = movilizandome();
                Log.i("Detalle ", "" + codigo_movi);
                HashMap tem = new HashMap();
                encabezado t14 = ite.next();


                tem.put("id_movilizandome", codigo_movi);
                tem.put("usuario_movilizandome", t14.usuario_movilizandome);
                tem.put("co_cli", String.valueOf(t14.cod_cli));
                tem.put("forma_pag", t14.forma_pag);
                tem.put("total", t14.total);
                tem.put("cobrado", t14.cobrado);
                tem.put("Procesado", t14.Procesado);
                tem.put("fact_num2", t14.fac_num2);
                tem.put("Cobrado2", t14.Cobrado2);
                Log.i("envio", tem.toString());


                String lurl = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_insert_movilizandome_detalle";
                Iterator<detalle> ite2 = t14.detalle.iterator();
                while (ite2.hasNext()) {
                    HashMap tem2 = new HashMap();
                    detalle t19 = ite2.next();
                    Log.i("Detalle envio", t19.getCo_art());
                    tem2.put("id_movilizandome", codigo_movi);
                    tem2.put("usuario_movilizandome", t14.usuario_movilizandome);
                    tem2.put("co_art", t19.getCo_art());
                    tem2.put("prec_vta", t19.getPrec_vta());
                    tem2.put("Cantidad", t19.getCantidad());
                    tem2.put("total_art", t19.getTotal_art());
                    Log.i("Detalle envio", tem2.toString());
                    restTemplate = new RestTemplate();
                    // Add the Jackson message converter
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    JSONArray t_c2 = restTemplate.postForObject(lurl, tem2, JSONArray.class);
                    Log.i("Valor de Retorno ", "" + t_c2.toString());
                }
//                    Area de ingreso de Encabezado
                // Add the Jackson message converter
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                t_c = restTemplate.postForObject(url, tem, JSONArray.class);

                base.set_encabezado(t14.encabezado);


            }


// Area de Baja Producto  y Cobros


//                            int t16=Integer.parseInt(t15);
//                            base.set_movilizandome(t16);


//                carga_clientes();
//                obtener_facturas();


//


        } else if (params[1].equals("12")) {
            publishProgress("Sincronizando Ventas", "Sincronizacion");
            /*** Para sincronizar ventas con los ws antiguos descomentar la linea de abajo ***/
            //ventas();
            String[] actual_local = base.get_access();
            int estado = Integer.parseInt(actual_local[1].toString());
            if (estado < 3) {
                publishProgress("Sincronizando Cobros", "Sincronizacion");
                cobros();
                publishProgress("Sincronizado Cuentas Por Cobrar", "Sincronizacion");
                obtener_facturas();
                publishProgress("Sincronizando Productos", "Sincronizacion");
                try {
                    carga_productos();
                } catch (JSONException e) {

                }
            }
        } else if (params[1].equals("13")) {
            publishProgress("Sincronizando Clientes", "Sincronizacion");
            carga_clientes();


        } else if (params[1].equals("14")) {

//                cobros();
//                obtener_facturas();
            try {
                publishProgress("Sincronizando Productos", "Sincronizacion");
                carga_productos();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (t_c == null) return "" + "&" + params[1];
        return t_c.toString() + "&" + params[1];


    }

    private void info_logueo(JSONArray t14) throws IOException, JSONException {

        if (error.conexion() && error.existedatos() == 0) {
            Log.i("info traida revisar", "" + t14.toString());
//
//               AsyncTask<String, Void, String> t12 = new webservice(this);
//
//               String t14 = null, t15 = null;
//               try {
////                    getimei()
//
//                   t14 = t12.execute("999", "0", getimei(), pin.getText().toString()).get();


//
//               } catch (InterruptedException e) {
//                   e.printStackTrace();
//               } catch (ExecutionException e) {
//                   e.printStackTrace();
//               }
//
            JSONArray info = t14;
            JSONObject info_r = null;
//

            if (info.length() <= 0) {


                try {
                    publishProgress("Error No Existe Personal ", "Error en Inicio");
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                mensaje("No Registrado", "No Existe el usuario en TPOS o Pin Incorrecto");
//
//
            } else {
////


                if (info.length() > 0) {
////
////
                    info_r = info.getJSONObject(0);
                    Log.i("informacion de login arreglo", info_r.toString());

                    int estado = Integer.parseInt(info_r.get("Estado").toString());
                    base.limpiar_configuracion();
                    base.insert_configuracion(info_r);

                    if (estado > 0) {
////

                        publishProgress("Cargando Clientes", "Iniciando Session");
                        carga_clientes();
                        publishProgress("Cargando Producto(s)", "Iniciando Session");
                        carga_productos();
                        publishProgress("Cargando Cuentas Por Cobrar", "Iniciando Session");
                        obtener_facturas();
                        publishProgress("Sincronizando de Ventas Piso", "Sincronizando");
                        ventas();
                        Intent i2 = new Intent(actual, menu_principal.class);
////
                        actual.startActivity(i2);


//

                    }

//
// else {
////
////
//                        if (info_r.get("Usuario_Dsc").toString().equals("Bienvenido a TPOS...")) {
//                            base.limpiar_configuracion();
////
//                            base.insert_configuracion(info_r);
//

//////                            int t16=Integer.parseInt(t15);
//////                            base.set_movilizandome(t16);
////                           carga_clientes();
////                           carga_productos();
////                           obtener_facturas();
//                            base.estado(2);
////
//                        }else if(info_r.get("Usuario_Dsc").toString().equals("Ya esta logueado a la APP...")){
//                            base.limpiar_configuracion();
////
//                            base.insert_configuracion(info_r);
//                            publishProgress("Cargando Clientes","Iniciando Session");
//                            carga_clientes();
//                            publishProgress("Cargando Producto(s)","Iniciando Session");
//                            carga_productos();
//                            publishProgress("Cargando Cuentas Por Cobrar","Iniciando Session");
//                            obtener_facturas();
//                            publishProgress("Sincronizando de Ventas Piso","Sincronizando");
//                            ventas();
//                            base.estado(2);
//
//                        }
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
//////
//////
//////                       }
////
////
                }
/// else {
////
////                       //}
//                    }
////                   Log.i("info traida cliente", t14);
////
//                    Intent i2 = new Intent(actual, menu_principal.class);
////
//                    actual.startActivity(i2);
////                    actual.finish();
////
//                } else {
////
////                   mensaje("No Registrado", "No Existe en POS o Pin Inorrecto");
////
//                }
//
//
            }
//

        } else {
////                No hay Conexion //

            try {
                publishProgress("Error de Conexion ", "Error en Datos");
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            mensaje("Error de Red","Por el Momento no se Cuenta con Internet para Iniciar el Sistema ");

        }
//            }
//


    }


    private JSONArray logueo(String[] params) {

        try {
            String url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Log_User/";
            HashMap tem = new HashMap();
            tem.put("usuario_id", params[0]);
            tem.put("Imei", params[2]);
            tem.put("pin", params[3]);

            Log.i("info de login", tem.toString());
            RestTemplate restTemplate = new RestTemplate();
            // Add the Jackson message converter
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            JSONArray t2 = restTemplate.postForObject(url, tem, JSONArray.class);

            Log.i("info de traigo", t2.toString());
            return t2;

        } catch (Exception e) {

            String erf = "No fue posible obtener las credenciales de inicio de sesion. codigo del error: " + e.getLocalizedMessage();

            publishProgress(erf, "Error en Sincronizacion");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return null;
        }


    }

    private void ventas() {
        try {
            JSONArray t_c = null;
            String url = "";
            RestTemplate restTemplate;
            restTemplate = new RestTemplate();
            ArrayList<encabezado> lista = null;
            //url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_insert_movilizandome_encabezado/";
            url = "http://tarjetazo.eastus2.cloudapp.azure.com/wsTposDEV/api/sp_insert_movilizandome_encabezado/";
//          Area de envio de Ventas
            lista = base.get_encabezado_venta();
            Iterator<encabezado> ite = lista.iterator();
            while (ite.hasNext()) {
                int codigo_movi = movilizandome();
                HashMap tem = new HashMap();
                encabezado t14 = ite.next();
                tem.put("id_movilizandome", codigo_movi);
                tem.put("usuario_movilizandome", t14.usuario_movilizandome);
                tem.put("co_cli", String.valueOf(t14.cod_cli));
                tem.put("forma_pag", t14.forma_pag);
                tem.put("total", t14.total);
                tem.put("cobrado", t14.cobrado);
                tem.put("Procesado", t14.Procesado);
                tem.put("fact_num2", t14.fac_num2);
                tem.put("Cobrado2", t14.Cobrado2);
                Log.i("envio reviso Ahora ", tem.toString());
                base.ver_codigo_encabezado();
                base.ver_detalle();
                //
                String lurl = "http://tarjetazo.eastus2.cloudapp.azure.com/wsTposDEV/api/sp_insert_movilizandome_detalle/";
                Iterator<detalle> ite2 = t14.detalle.iterator();
                while (ite2.hasNext()) {
                    HashMap tem2 = new HashMap();
                    detalle t19 = ite2.next();
                    Log.i("Detalle envio", t19.getCo_art());
                    tem2.put("id_movilizandome", codigo_movi);
                    tem2.put("usuario_movilizandome", t14.usuario_movilizandome);
                    tem2.put("co_art", t19.getCo_art());
                    tem2.put("prec_vta", t19.getPrec_vta());
                    tem2.put("Cantidad", t19.getCantidad());
                    tem2.put("total_art", t19.getNumero_cel());
                    Log.i("Detalle envio", "\n" + tem2.toString());
                    restTemplate = new RestTemplate();
                    // Add the Jackson message converter
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    JSONArray t_c2 = restTemplate.postForObject(lurl, tem2, JSONArray.class);
                    Log.i("detalle enviado", t_c2.toString());
                }
//                    Area de ingreso de Encabezado
                // Add the Jackson message converter
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                t_c = restTemplate.postForObject(url, tem, JSONArray.class);
//                try {
//                  //  JSONObject estado = t_c.getJSONObject(0);
////
////                    if (estado.getString("resultado").equals("true")) {
//////                        base.set_encabezado(t14.encabezado);
////                        base.set_venta(t14.encabezado, codigo_movi);
////                        Log.i("Valor Encabezado", estado.getString("resultado"));
////                    }
//                } catch (JSONException e) {
//
//                    String erf = "Error irrecuperable al sincronizar ventas, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();
//                    publishProgress(erf, "Error en Sincronizacion(JSON)");
//                    try {
//                        Thread.sleep(4000);
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
//                }
            }
        } catch (Exception e) {
            String erf = "Error irrecuperable al sincronizar en Ventas, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();
            publishProgress(erf, "Error en Sincronizacion");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void cobros() {

        String url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/sp_insert_movilizandome_encabezado/";

        try {

            ArrayList<encabezado> lista = base.get_encabezado_cobro();

            Iterator<encabezado> ite = lista.iterator();
            RestTemplate restTemplate = new RestTemplate();
            while (ite.hasNext()) {
                int codigo_movi = movilizandome();
                Log.i("codigo movilizandome", "" + codigo_movi);
                HashMap tem = new HashMap();
                encabezado t14 = ite.next();


                tem.put("id_movilizandome", codigo_movi);
                tem.put("usuario_movilizandome", t14.usuario_movilizandome);
                tem.put("co_cli", String.valueOf(t14.cod_cli));
                tem.put("forma_pag", t14.forma_pag);
                tem.put("total", t14.total);
                tem.put("cobrado", t14.cobrado);
                tem.put("Procesado", t14.Procesado);
                tem.put("fact_num2", t14.fac_num2);
                tem.put("Cobrado2", t14.Cobrado2);
                Log.i("envio Pago", tem.toString());
                // Add the Jackson message converter
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                JSONArray t_c = restTemplate.postForObject(url, tem, JSONArray.class);
                Log.i("Valor de Cobro", t_c.toString());
                base.set_venta(t14.encabezado, codigo_movi);

                ///  );


            }
        } catch (Exception e) {

            String erf = "Error irrecuperable al sincronizar los cobros, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();


            publishProgress(erf, "Error en Sincronizacion");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }


    }


    private void carga_clientes() {


        try {
            Log.i("Revisar Ruta Ya", base.get_ruta());
            if (ero.conexion() && ero.existedatos() == 0) {

                try {

                    String url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Ret_Lst_clientes_fuera_de_ruta/" + base.get_ruta().trim();
//                Log.i("Revisar Ruta de envio",url);

                    RestTemplate restTemplate = new RestTemplate();

                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                    JSONArray t_c = restTemplate.getForObject(url, JSONArray.class);
                    Log.i("Revisar Ruta de envio", "" + t_c.length());

                    base.limpiar_cliente();
                    JSONArray info = t_c;
                    for (int i = 0; i < info.length(); i++) {
                        JSONObject tem = info.getJSONObject(i);

                        base.insertcliente(tem);


                    }
                } catch (JSONException e) {
                    String erf = "Error Al Recuperar Clientes al sincronizar los clientes, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();
                    publishProgress(erf, "Error en Sincronizacion");
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                }

            }

        } catch (IOException e) {
            String erf = "Error irrecuperable al sincronizar los clientes, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();
            publishProgress(erf, "Error en Sincronizacion");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void carga_productos() throws JSONException {
        try {
            String url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Lst_productos_de_la_ruta/" + base.get_ruta().trim();


            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            JSONArray t_c = restTemplate.getForObject(url, JSONArray.class);
            JSONArray t6 = t_c;
            base.limpiar_producto();
            if (t6.length() > 0) {
                Log.i("Fue insertado de producto", "" + t6.toString());
                for (int i = 0; i < t6.length(); i++) {
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
        } catch (Exception e) {

            String erf = "Error irrecuperable al sincronizar los productos, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();
            publishProgress(erf, "Error en Sincronizacion");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        }


    }

    private void obtener_facturas() {
        try {
            ArrayList<nodo_factura> envio_factura = new ArrayList<>();
            ArrayList<Nodo_tarea> t12 = base.getallcliente_fac();
            Iterator<Nodo_tarea> t13 = t12.iterator();
            while (t13.hasNext()) {
                Nodo_tarea t14 = t13.next();
                String url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Ret_Lst_Saldo_limite_de_credito_y_facturas_del_cliente/" + t14.cod_cliente;


                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                JSONArray t_c = restTemplate.getForObject(url, JSONArray.class);
                Log.i("cobros user", t_c.toString());

                if (t_c.length() > 0) {


                    for (int i = 0; i < t_c.length(); i++) {

                        try {
                            JSONObject tem2 = t_c.getJSONObject(i);


                            nodo_factura tem = new nodo_factura(tem2.getString("fact_num"), tem2.getString("fec_emis"), tem2.getDouble("saldo"), tem2.getInt("co_cli"));
                            envio_factura.add(tem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }
            }

            base.limpiar_cobro();
            base.cobro(envio_factura);
        } catch (Exception e) {

            String erf = "Error irrecuperable al sincronizar facturas, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();
            publishProgress(erf, "Error en Sincronizacion");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public int movilizandome() {
        String url = "http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/dt_SP_ID_Virtual_Movilizandome";
        RestTemplate restTemplate = new RestTemplate();
        // Add the Jackson message converter
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HashMap tem = new HashMap();
        tem.put("ruta", base.get_ruta());
        int movilizandome_cod = 0;
        JSONArray movilizandome = restTemplate.postForObject(url, tem, JSONArray.class);
        for (int t = 0; t < movilizandome.length(); t++) {
            try {
                JSONObject t2 = movilizandome.getJSONObject(t);
                movilizandome_cod = t2.getInt("id_movilizandome");

            } catch (JSONException e) {

                String erf = "Error irrecuperable al crear el codigo movilizandome, intente de nuevo la sincronizacion.\n" + e.getLocalizedMessage();
                publishProgress(erf, "Error en Sincronizacion");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }


        }
        return movilizandome_cod;

    }


}
