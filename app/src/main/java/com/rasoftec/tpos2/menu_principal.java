package com.rasoftec.tpos2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.nodo_factura;
import com.rasoftec.tpos2.data.producto_translado;
import com.rasoftec.tpos2.data.traslado;
import com.rasoftec.tpos2.data.venta;
import com.rasoftec.tpos2.data.venta2;
import com.rasoftec.tpos2.data.venta_detalle;
import com.rasoftec.tpos2.data.webservice;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;
import com.rasoftec.tpos2.sensor.ssdi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static com.rasoftec.tpos2.data.venta_detalle.round;

public class menu_principal extends AppCompatActivity {
    String ruta_actual;
    database base;
    ErrorRed ero;
    hilo replicar;
    private ProgressDialog progressDialog;
    private ProgressDialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        base = new database(this);
        ero = new ErrorRed(this);
        ruta_actual = base.get_ruta();
        activo();
        setTitle("Menu de Ruta" + " " + base.get_ruta());
// Area del inicio del Hilo  para ver si puedo enviar la informacion
//        replicar= null;
//       replicar= new hilo(this);
//        replicar.start();


    }

    private void activo() {
        String[] t12 = base.get_access();
        ImageButton t = (ImageButton) findViewById(R.id.imageButton9);
        Log.i("error de logueo segundo", t12[0]);
        int t13 = 0;
        if (t12[1] != null) {
            t13 = Integer.parseInt(t12[1]);
        }
        if (t13 == 3) {

            t.setEnabled(false);
            t.setBackgroundColor(getResources().getColor(R.color.gris));

        }

    }

    //    Area de Acciones de los Botones
    public void cliente(View v) {
        Intent i2 = new Intent(this, lista.class);
        i2.putExtra("ruta", ruta_actual);
        i2.putExtra("tipo_venta", 1);
        startActivity(i2);
        finish();

    }

    public void v_f_r(View v) {
        Intent i2 = new Intent(this, lista.class);
        i2.putExtra("ruta", ruta_actual);
        i2.putExtra("tipo_venta", 2);
        startActivity(i2);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void translado(View v) {
        AsyncTask<String, String, String> t12 = new webservice(this);
        ArrayList<traslado> lista_info = new ArrayList<>();
        try {
            if (ero.conexion() && ero.existedatos() == 0) {
                String t14 = null;
                try {
                    t14 = t12.execute(ruta_actual, "6").get();

                    JSONArray t16 = new JSONArray(t14);

                    for (int i = 0; i < t16.length(); i++) {
                        JSONObject t15 = t16.getJSONObject(i);

                        if (lista_info.size() <= 0) {


                            traslado tem17 = new traslado(t15.getInt("tras_num"), t15.getString("fecha"), t15.getString("alm_orig"), t15.getString("alm_dest"));
                            producto_translado tem18 = new producto_translado(t15.getString("co_art"), t15.getString("art_des"), t15.getString("total_art"));
                            tem17.lista.add(tem18);
                            lista_info.add(tem17);


                        } else {
                            traslado tem18 = existe(t15.getInt("tras_num"), lista_info);
                            if (tem18 != null) {
                                producto_translado tem19 = new producto_translado(t15.getString("co_art"), t15.getString("art_des"), t15.getString("total_art"));
                                tem18.lista.add(tem19);

                            } else {
                                traslado tem17 = new traslado(t15.getInt("tras_num"), t15.getString("fecha"), t15.getString("alm_orig"), t15.getString("alm_dest"));
                                producto_translado tem188 = new producto_translado(t15.getString("co_art"), t15.getString("art_des"), t15.getString("total_art"));
                                tem17.lista.add(tem188);
                                lista_info.add(tem17);

                            }

                        }


                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                View v2 = inflater.inflate(R.layout.traslado, null);
                ListView t = (ListView) v2.findViewById(R.id.lista_transaldo);
                Log.i("Revisar", " " + lista_info.size());
                ArrayAdapter adapter = new ArrayAdapter<>(menu_principal.this, android.R.layout.simple_list_item_1, lista_info);

                t.setAdapter(adapter);


                builder.setView(v2);


                final AlertDialog dialog = builder.create();

                t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        traslado reviso = (traslado) parent.getItemAtPosition(position);
                        aceptar_traslado(reviso);
                        dialog.dismiss();


                    }
                });


                dialog.show();
            } else {
                mensa2("Conexion de Red", "Error en Conexion a la Red");

            }
        } catch (IOException e) {
            mensa2("Conexion de Red", "Error en Conexion a la Red" + e.getMessage());
        }

    }

    private traslado existe(int a, ArrayList<traslado> lista) {
        traslado t12 = null;
        Iterator<traslado> t13 = lista.iterator();
        while (t13.hasNext()) {
            traslado t14 = t13.next();
            if (t14.getCodigo_traslado() == a) {

                t12 = t14;
                break;
            }


        }


        return t12;
    }

    private void enviartras(int traslado) {

        ArrayList<traslado> lista_info = new ArrayList<>();
        try {
            if (ero.conexion() && ero.existedatos() == 0) {
                String t14 = null;
                String envio = String.valueOf(traslado);
                new webservice(this).execute(envio, "7");
//                Log.i("Reviso envio", t14);
            } else {
                mensa2("Conexion de Red", "Error en Conexion a la Red");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void aceptar_traslado(traslado reviso) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final traslado re = reviso;
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.autorizar_traslado, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_deta);

        ArrayAdapter adapter = new ArrayAdapter<>(menu_principal.this, android.R.layout.simple_list_item_1, reviso.lista);

        t.setAdapter(adapter);

        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Realizar Traslado", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        enviartras(re.getCodigo_traslado());
                        try {
                            actualizar_producto();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                })

                .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
        ;


        TextView codigo = (TextView) v2.findViewById(R.id.codigo_tr);

        codigo.setText("Codigo de Traslado" + "\n" + reviso.getCodigo_traslado());
//        TextView origen=(TextView)v2.findViewById(R.id.origen_t);
//        TextView destino=(TextView)v2.findViewById(R.id.destino_t);
//        TextView articulo=(TextView)v2.findViewById(R.id.articulo_t);
//        TextView descripcion=(TextView)v2.findViewById(R.id.descripcion_t);
//        TextView fecha=(TextView)v2.findViewById(R.id.fecha_t);
//        TextView total=(TextView)v2.findViewById(R.id.total_t);
////
////
//
//        origen.setText(" Almacen Origen"+"\n "+reviso.getOrigen());
//        destino.setText(" Almacen Destino"+"\n "+reviso.getDestino());
//        articulo.setText(" Articulo"+"\n"+reviso.getArticulo().trim());
//        descripcion.setText(" Descripcion de Articulo"+"\n "+reviso.getDescripcion_articulo().trim());
//        fecha.setText(" Fecha de Emision"+"\n "+reviso.getFecha());
//        total.setText(" Total de Articulos"+"\n "+reviso.getTotal_articulo());


        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void actualizar_producto() throws JSONException {

        AsyncTask<String, String, String> t12 = new webservice(this);
        try {
            if (ero.conexion() && ero.existedatos() == 0) {
                String t14 = null;
                try {
                    t14 = t12.execute(base.get_ruta(), "2").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                JSONArray t6 = new JSONArray(t14);
                base.limpiar_producto();
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

            } else {
                mensa2("Conexion de Red", "Error en Conexion a la Red");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void saldo(View v) {
        final CharSequence[] pos;
        if (base.get_estado() == 3)
            pos = new CharSequence[]{"Venta Piso", "Venta Profit", ""};
        else
            pos = new CharSequence[]{"Venta Local", "Venta Profit", "Reporte Totales", "Reporte grafico"};


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Elegir Tipo de Reporte de Diario")
                .setSingleChoiceItems(pos, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        reporte(pos[which].toString());
                        dialog.dismiss();


                    }
                });


        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
//

    }

    private void reporte(String s) {
        if (s.toLowerCase().equals("reporte totales")) {
            //descomentar esta linea y comentar TableReport
            //total();
            TableReport();
        } else if (s.toLowerCase().equals("reporte grafico")) {
            graphicReport();

        } else if (s.toLowerCase().equals("venta local")) {
            venta_tipo(s);


        } else if (s.toLowerCase().equals("venta piso")) {

            venta_detalle(s.toLowerCase());

        } else if (s.toLowerCase().equals("venta profit")) {
            total_venta();
        }


    }

    private void graphicReport(){
        Intent cambiarActividad = new Intent(getApplicationContext(), ChartActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
    private void TableReport(){
        Intent cambiarActividad = new Intent(getApplicationContext(), TableActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
    class venta3 {
        String tipo_venta, cliente, total_saldo;

        public venta3(String tipo_venta, String cliente, String total_saldo) {
            this.tipo_venta = tipo_venta;
            this.cliente = cliente;
            this.total_saldo = total_saldo;

        }

        @Override
        public String toString() {
            String tem = "Tipo de Venta " + " " + tipo_venta + "\n" + "Cliente" + " " + cliente + "\n" + total_saldo;

            return tem;
        }
    }

    private void total_venta() {

        try {
            if (ero.conexion() && ero.existedatos() == 0) {


                AsyncTask<String, String, String> t12 = new webservice(this);

                String t14 = null;


                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                View v2 = inflater.inflate(R.layout.inventario, null);
                ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
                TextView t13 = (TextView) v2.findViewById(R.id.encabezado);
                t13.setTextSize(25);
                t13.setText("Venta Total Server");
                builder.setView(v2)
                        // Add action buttons
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                try {
                    t14 = t12.execute("R" + base.get_ruta().trim(), "2").get();
                    ArrayAdapter adapter = null;
                    JSONArray t15 = new JSONArray(t14);
                    ArrayList<venta3> lista_info = new ArrayList<>();
                    if (t15.length() > 0) {
                        int limite = t15.length();
                        //cobrado facturado
                        float tota = 0;
                        float saldo = 0;
                        for (int i = 0; i < limite; i++) {
                            JSONObject t16 = (JSONObject) t15.get(i);
                            float t_t = Float.parseFloat(t16.getString("stock_act"));
                            float s_s = Float.parseFloat(t16.getString("prec_vta1"));
                            tota += t_t;
                            saldo += s_s;


                            venta3 t17 = new venta3(t16.getString("co_alma") + " Factura #" + " " + t16.get("co_art"), t16.getString("art_des"), "Total:" + " " + t16.getString("stock_act") + " Cobrado:" + " " + t16.get("prec_vta1"));
                            lista_info.add(t17);

                        }
                        double to2 = nodo_factura.round(tota, 2);

                        double s2 = nodo_factura.round(saldo, 2);

                        t13.setText("Venta Profit" + "\n" + "Total Facturado en Q" + to2 + "\n" + "Cobrado en Q" + s2);

                        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);
                        t.setAdapter(adapter);
                    }

//                        Log.i("Actual Valor de R",t15.toString());


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AlertDialog dialog = builder.create();
                dialog.show();


            } else {
                mensa2("Conexion de Red", "Error en Conexion a la Red");

            }
        } catch (IOException e) {
            mensa2("Error de Servidor", "Error en Conexion a la Red" + e.getMessage());
        }


    }

    private void venta_tipo(String s) {
        final CharSequence[] pos = {"Venta General", "Venta por Articulo"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Elegir Tipo de Reporte de Ventas")
                .setSingleChoiceItems(pos, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        venta_detalle(pos[which].toString());
                        dialog.dismiss();


                    }
                });


        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void venta_detalle(String s) {
        final String s2 = s;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);


        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.inventario, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
        TextView t12 = (TextView) v2.findViewById(R.id.encabezado);
        t12.setText(s.toUpperCase());
        t12.setText(s.toUpperCase() + "\nTotal Venta Q" + " " + nodo_factura.round(base.get_venta_total(), 2));
        t12.setTextSize(23);
        ArrayAdapter adapter = null;
        if (s.toLowerCase().equals("venta general")) {
            ArrayList<venta2> lista_info = venta2(base.get_movimiento());
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);
        } else if (s.toLowerCase().equals("venta por articulo")) {

            ArrayList<venta_detalle> lista_info = base.get_detalle();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);
        } else if (s.toLowerCase().equals("venta piso")) {
            t12.setText(s.toUpperCase() + "\nTotal Venta Q" + " " + nodo_factura.round(base.get_venta_total_piso(), 2));
            ArrayList<venta2> lista_info = venta2(base.get_venta_piso());
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);


        }


        t.setAdapter(adapter);

        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (s2.toLowerCase().equals("venta general")) {
                    venta2 reviso = (venta2) parent.getItemAtPosition(position);
                    detalle_movimiento(reviso);

                } else if (s2.toLowerCase().equals("venta por articulo")) {
                    venta_detalle reviso = (venta_detalle) parent.getItemAtPosition(position);
                    detalle_movimiento(reviso);


                }


            }
        });


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
        ;


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void detalle_movimiento(venta_detalle reviso) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.inventario, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
        TextView t12 = (TextView) v2.findViewById(R.id.encabezado);
        t12.setText("Detalle de Movimiento(s)" + "\n" + "Venta" + " " + base.get_producto(reviso.getProducto()));
        t12.setTextSize(20);
        ArrayList<venta2> lista_info = venta_articulo(base.get_venta(reviso));

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);

        t.setAdapter(adapter);

        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                venta reviso = (venta) parent.getItemAtPosition(position);
//                detalle_movimiento(reviso);

            }
        });


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })


//                .setPositiveButton("Consultar Saldo", new DialogInterface.OnClickListener() {
//                    @Override
//                    public String onClick(DialogInterface dialog, int id) {
//                        saldo_actual();
//
//                    }
//                })
        ;


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private ArrayList<venta2> venta_articulo(ArrayList<venta2> venta) {
        Iterator<venta2> t = venta.iterator();

        while (t.hasNext()) {
            venta2 t14 = t.next();
            Nodo_tarea t24 = base.getcliente_actual(t14.getCliente());
            t14.setCliente(t24.descripcion);


        }
        return venta;
    }

    private void detalle_movimiento(venta2 reviso) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.inventario, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
        TextView t12 = (TextView) v2.findViewById(R.id.encabezado);
        t12.setText("Detalle de Movimiento(s)" + "\n" + "Venta #" + "" + reviso.getCodigo());
        t12.setTextSize(20);
        ArrayList<venta_detalle> lista_info = reviso.detalles;
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);

        t.setAdapter(adapter);

        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                venta reviso = (venta) parent.getItemAtPosition(position);
//                detalle_movimiento(reviso);

            }
        });


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })


//                .setPositiveButton("Consultar Saldo", new DialogInterface.OnClickListener() {
//                    @Override
//                    public String onClick(DialogInterface dialog, int id) {
//                        saldo_actual();
//
//                    }
//                })
        ;


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<venta2> venta2(ArrayList<venta> movimiento) {
        ArrayList<venta2> t = new ArrayList<>();
        Iterator<venta> t2 = movimiento.iterator();
        while (t2.hasNext()) {
            venta t3 = t2.next();
            Nodo_tarea clie = base.getcliente_actual(t3.getCliente());

            venta2 t4 = new venta2(t3.getCodigo(), clie.descripcion, t3.movilizandome);

            t4.detalles = t3.detalles;
            t.add(t4);

        }

        return t;
    }

    private void cobro_reporte(String s) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.inventario, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
        TextView t12 = (TextView) v2.findViewById(R.id.encabezado);
        t12.setText(s);
        t12.setText(s + "\nTotal de Cobro Q" + " " + nodo_factura.round(base.get_cobro_total(), 2));
        t12.setTextSize(23);
        ArrayList<nodo_factura> lista_info = base.get_cobro();
        ArrayAdapter adapter = null;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);
        t.setAdapter(adapter);

        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                venta reviso = (venta) parent.getItemAtPosition(position);
//                detalle_movimiento(reviso);

            }
        });


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
        ;


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void total() {
        try {
            if (ero.conexion() && ero.existedatos() == 0) {


                AsyncTask<String, String, String> t12 = new webservice(this);

                String t14 = null;


                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                View v2 = inflater.inflate(R.layout.orga, null);
                builder.setView(v2)
                        // Add action buttons
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                            }
                        });

                try {
                    t14 = t12.execute(base.get_ruta(), "4").get();

                    JSONArray t15 = new JSONArray(t14);

                    TextView venta = (TextView) v2.findViewById(R.id.venta_reporte);
                    TextView cobro = (TextView) v2.findViewById(R.id.cobro_dia);

                    JSONObject t1 = t15.getJSONObject(0);

                    venta.setText("Q" + " " + nodo_factura.round(Double.parseDouble(t1.getString("Venta")), 2));
                    cobro.setText("Q" + " " + nodo_factura.round(Double.parseDouble(t1.getString("Saldo")), 2));


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AlertDialog dialog = builder.create();
                dialog.show();


            } else {
                mensa2("Conexion de Red", "Error en Conexion a la Red");

            }
        } catch (IOException e) {
            mensa2("Error de Servidor", "Error en Conexion a la Red" + e.getMessage());
        }
    }

    private void mensa2(String enca, String cue) {


        final Intent t = new Intent(this, menu_principal.class);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.mensaje, null);


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Terminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(t);
                        finish();

                        dialog.dismiss();
                    }
                })

//                .setPositiveButton("", new DialogInterface.OnClickListener() {
//                    @Override
//                    public String onClick(DialogInterface dialog, int id) {
//
//                        dialog.cancel();
//                    }
//                })
        ;


        TextView encabezado = (TextView) v2.findViewById(R.id.info_mensa);
        TextView cuerpo = (TextView) v2.findViewById(R.id.info_mensa2);
        encabezado.setText(enca);
        cuerpo.setText(cue);


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void inventario(View v) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.inventario, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
        ArrayList<nodo_producto> lista_info = base.getall();
        ArrayAdapter adapter = new ArrayAdapter<>(menu_principal.this, android.R.layout.simple_list_item_1, lista_info);

        t.setAdapter(adapter);

        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Nodo_tarea reviso = (Nodo_tarea) parent.getItemAtPosition(position);


            }
        });


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })


                .setPositiveButton("Consultar Saldo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saldo_actual();

                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void saldo_actual() {
        ssdi t = new ssdi();
        String numero = base.get_telefono().trim();
        int final2 = numero.length();
        int inicio = final2 - 4;

        String tem_num = numero.substring(inicio, final2);
        Log.i("Numero Actual", tem_num);
        t.consulta_saldo_telefono(tem_num, this);
    }

    public void cierre(View v) {
        if (base.existe_sincronizar() <= 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View v2 = inflater.inflate(R.layout.cierre, null);
            final EditText t = (EditText) v2.findViewById(R.id.pin_c);


            builder.setView(v2)
                    // Add action buttons
                    .setNegativeButton("Realizar Cierre", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                            realizar_cierre(t.getText().toString());
                        }
                    })


                    .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            mensa2("Error Falta Sincronizacion Ventas o Cobros", "Se debe Realizar la sincronizacion Para Realizar el Cierre del Dia ");
        }

    }

    private void realizar_cierre(String codigo) {

        AsyncTask<String, String, String> t12 = new webservice(this);
        try {
            if (ero.conexion() && ero.existedatos() == 0) {
                String t14 = null;

                try {
                    t14 = t12.execute(base.get_ruta().trim(), "9", "CIERRE", codigo).get();
                    JSONArray t15 = new JSONArray(t14);
                    JSONObject t16 = t15.getJSONObject(0);
                    if (t15.length() > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        LayoutInflater inflater = this.getLayoutInflater();

                        // Inflate and set the layout for the dialog
                        // Pass null as the parent view because its going in the dialog layout
                        View v2 = inflater.inflate(R.layout.cieree_t, null);
                        TextView credi = (TextView) v2.findViewById(R.id.tvc);
                        credi.setText("Q" + " " + t16.getString("Ruta"));
                        TextView credi2 = (TextView) v2.findViewById(R.id.tvcc);
                        credi2.setText("Q" + " " + t16.getString("Usuario_Id"));
                        TextView credi3 = (TextView) v2.findViewById(R.id.tcc);
                        credi3.setText("Q" + " " + t16.getString("Usuario_Dsc"));
                        TextView credi4 = (TextView) v2.findViewById(R.id.boleta);
                        credi4.setText("Q" + " " + t16.getString("Telefono"));
                        builder.setView(v2)

                                // Add action buttons
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        base.limpiar_cierre();
                                        base.estado(3);
                                        activo();
                                        carga_clientes();


                                        dialog.dismiss();

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    Log.i("Cerrado", t14);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mensa2("Conexion de Red", "Error en Conexion a la Red");

            }
        } catch (IOException e) {
            mensa2("Conexion de Red", "Error en Sincronizacion");
        }

    }

    private void carga_clientes() {
        AsyncTask<String, String, String> t12 = new webservice(this);

        try {
            if (ero.conexion() && ero.existedatos() == 0) {
                String t14 = null;
                try {
//                Log.i("Revisar Ruta",base.get_ruta());
                    t14 = t12.execute(base.get_ruta().trim().toString(), "8").get();


                    base.limpiar_cliente();
                    JSONArray info = new JSONArray(t14);
                    for (int i = 0; i < info.length(); i++) {
                        JSONObject tem = info.getJSONObject(i);

                        base.insertcliente(tem);


                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                mensa2("Conexion de Red", "Error en Conexion a la Red");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_p, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.closes2:
                cierro();
                break;
            case R.id.mepri2:
//                menu();
                break;

        }


        return true;
    }

    private void cierro() {
        final Intent t = new Intent(this, login.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.mensaje, null);


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        base.estado(1);
                        startActivity(t);


                        finish();
                        dialog.dismiss();
                    }
                })

                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
        ;


        TextView codigo = (TextView) v2.findViewById(R.id.info_mensa);
        codigo.setText("Salir del Sistema");
        TextView codigo2 = (TextView) v2.findViewById(R.id.info_mensa2);
        codigo2.setText("Desea Salir del Sistema");

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void menu() {
        final Intent t = new Intent(this, menu_principal.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.mensaje, null);


        builder.setView(v2)
                // Add action buttons
                .setNegativeButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        startActivity(t);
                        finish();
                        dialog.dismiss();
                    }
                })

                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
        ;


        TextView codigo = (TextView) v2.findViewById(R.id.info_mensa);
        codigo.setText("Menu Principal");
        TextView codigo2 = (TextView) v2.findViewById(R.id.info_mensa2);
        codigo2.setText("Desea Regresar al Menu Principal");

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void sincronizar(View v) {

        final CharSequence[] pos = {"Ventas,Cobros y Productos", "Clientes"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Elegir Tipo de Sincronizacion")
                .setSingleChoiceItems(pos, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (ero.conexion() && ero.existedatos() == 0) {
                                sincronizacion(pos[which].toString());
                                dialog.dismiss();
                            } else {
                                mensa2("Error de Conexion", "No se Cuenta con los Suficientes Datos");
                                dialog.dismiss();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();


//
//        Log.i("inicia sincronizacion",""+base.existe_sincronizar());
//
//
////        ProgressDialog   barra= new ProgressDialog(this);
//
//
//
//
//       AsyncTask<String, String, String> t12 = new webservice(this);
//
////
////
////
//               String t14 = null;
//
//
//        try {
//            if (ero.conexion() && ero.existedatos() == 0) {
//
//            try {
//
//                Log.i("inicia sincronizacion","Revisar");
//                t14 = t12.execute("", "11").get();
////                t12.execute("", "20").get();
////
//                mensa2("Sincronizacion","Todo Fue Sincronizado"+t14);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//
//
////
//
//        }else{
//                mensa2("Conexion de Red","Error en Conexion a la Red");
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//


    }

    private void sincronizacion(String s) throws ExecutionException, InterruptedException, IOException {

        Log.i("Sincronizacion", s);
        String t14 = "", estado = "";
        JSONArray tm;
        JSONObject tm2;
        if (ero.conexion() && ero.existedatos() == 0) {
            if (s.toLowerCase().equals("ventas,cobros y productos")) {
                new webservice(this).execute("", "12");
                Log.i("Sincronizacion", t14);
//            try {
//                tm = new JSONArray(t14);
//                tm2=tm.getJSONObject(0);
//                estado=tm2.getString("resultado");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            } else if (s.toLowerCase().equals("clientes")) {
                new webservice(this).execute("", "13");
                Log.i("Sincronizacion", t14);
            } else if (s.toLowerCase().equals("producto(s)")) {
                new webservice(this).execute("", "14");
                Log.i("Sincronizacion", t14);
//            try {
//                tm = new JSONArray(t14);
//                tm2=tm.getJSONObject(0);
//                estado=tm2.getString("resultado");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            }

//        if(estado=="true"){

//          mensa2("Sincronizacion","La Sincronizacion se realizo en forma exitosa de"+" "+s);

//        }else{
//            mensa2("Sincronizacion","Error en Sincronizacion "+" "+s);
//        }
        } else {
            mensa2("Sincronizacion", "Error en Sincronizacion " + " " + s);
        }
    }


}

