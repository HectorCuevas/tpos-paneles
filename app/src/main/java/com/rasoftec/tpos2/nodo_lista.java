package com.rasoftec.tpos2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rasoftec.ApplicationTpos;
import  com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.nodo_factura;
import com.rasoftec.tpos2.data.venta_detalle;
import com.rasoftec.tpos2.data.webservice;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;
import com.rasoftec.tpos2.sensor.ssdi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class nodo_lista extends AppCompatActivity {
    String codigo;
    String nombre="";
    private String ruta_act;
    database base;
    Nodo_tarea cliente_actual;
    Button consulta;
    Button cobro_b;
    Button venta_b;
    int tipo;
    ErrorRed ero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodo_lista);
      codigo= getIntent().getStringExtra("cliente");
        tipo= getIntent().getIntExtra("tipo_venta",1);
        ero= new ErrorRed(this);
        base=new database(this);


        ruta_act=base.get_ruta();




        cliente_actual=base.getcliente_actual(codigo);
        String [] t12=base.get_access();
        Log.i("acceso1",t12[1]);
        Log.i("acceso2",t12[0]);
        int t13=Integer.parseInt(t12[1]);
        if(t13==1 || t13==2){

            try {
                web_service();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(t13==3){

            cobro_b=(Button) findViewById(R.id.button2);
            cobro_b.setEnabled(false);
            cobro_b.setBackgroundColor(getResources().getColor(R.color.gris));

            venta_b=(Button) findViewById(R.id.button3);
            venta_b.setEnabled(true);

        }

        TextView t=(TextView)findViewById(R.id.cliente);
        t.setText(cliente_actual.descripcion);


//        Log.i("info actual",t2.nombre);
        setTitle("Operaciones");


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i2 = new Intent(this, menu_principal.class);

            startActivity(i2);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void web_service() throws IOException {


            AsyncTask<String, String, String> t12 = new webservice(this);

            //            Log.i("Actual Cliente",cliente_actual.cod_cliente);
            String t14="" ; //t12.execute(cliente_actual.cod_cliente, "1").get();
            Log.i("Actual Cliente",cliente_actual.cod_cliente);
            Log.i("Actual Cliente",base.get_cobro(cliente_actual.cod_cliente).toString());
//            JSONArray t15 = new JSONArray(t14);
//            if (t15.length() > 0) factura(t15);

            configuracion(base.get_cobro(cliente_actual.cod_cliente).size());



    }

    private void bloqueartodo() {
        venta_b=(Button) findViewById(R.id.button3);
        venta_b.setEnabled(false);// valor original true
        venta_b.setBackgroundColor(getResources().getColor(R.color.gris));
        venta_b=(Button) findViewById(R.id.button2);
        venta_b.setEnabled(false);// valor original true
        venta_b.setBackgroundColor(getResources().getColor(R.color.gris));
        venta_b=(Button) findViewById(R.id.button7);
        venta_b.setEnabled(false);// valor original true
        venta_b.setBackgroundColor(getResources().getColor(R.color.gris));
        venta_b=(Button) findViewById(R.id.button6);
        venta_b.setEnabled(false);// valor original true
        venta_b.setBackgroundColor(getResources().getColor(R.color.gris));

    }

    private void factura(JSONArray t15) {
        base.limpiar_factura();
      for(int i=0;i<t15.length();i++){
          try {
              JSONObject tem=t15.getJSONObject(i);
              base.insert_factura(tem);
          } catch (JSONException e) {
              e.printStackTrace();
          }


      }


    }
    private void mensaje(String enca,String cue){
     final   Intent t= new Intent(this,menu_principal.class);

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
//                    public void onClick(DialogInterface dialog, int id) {
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

    private void configuracion(int length) {
        Log.i("Valor Actual",""+length);
        if(length>0)
        {
            venta_b=(Button) findViewById(R.id.button3);
            venta_b.setEnabled(false);// valor original true
            venta_b.setBackgroundColor(getResources().getColor(R.color.gris));


        }
        else{
            cobro_b=(Button) findViewById(R.id.button2);
            cobro_b.setEnabled(false);
            cobro_b.setBackgroundColor(getResources().getColor(R.color.gris));

        }

        if(cliente_actual.telefono.toLowerCase().equals("Tarjetas")){
            consulta=(Button) findViewById(R.id.button6);
            consulta.setEnabled(false);
            consulta.setBackgroundColor(getResources().getColor(R.color.gris));

        }

    }

    public  void cobrar(View vi){
    Intent i=new Intent(this,cobro.class);
    i.putExtra("actual",codigo);
    i.putExtra("nombre",nombre);
    i.putExtra("ruta",ruta_act);
    startActivity(i);
        finish();


}

public void venta(View vi){
    Intent i=new Intent(this,venta.class);
    i.putExtra("actual",codigo);
    ApplicationTpos.codigoCliente=codigo;
    i.putExtra("nombre",nombre);
    i.putExtra("ruta",ruta_act);
    i.putExtra("tipo_venta",tipo);
    startActivity(i);
    finish();

}

public void consulta(View v){

    final CharSequence[] pos = cliente_actual.telefono.split(",");
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(cliente_actual.descripcion+" "+"POS Activo(s)"+" ")
            .setSingleChoiceItems(pos, -1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                       Log.i("Actual Valor",pos[which].toString());
                         consulta_saldo(pos[which].toString());




                }
            });


    AlertDialog alertDialog = builder.create();
    alertDialog.show();




}

    private void consulta_saldo(String s) {

        ssdi saldo = new ssdi();
        String numero =s;
        saldo.get_consulta_saldo(numero,this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_p, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.closes2:
                cierro();
                break;
            case R.id.mepri2:
               menu();
                break;

        }



        return true;
    }

    private void cierro() {
        final Intent t= new Intent(this,login.class);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

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

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void menu() {
        final Intent t= new Intent(this,menu_principal.class);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

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

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();


    }
public void movimiento(View v){

    final CharSequence[] pos = {"Venta","Cobro"};
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Elegir Tipo de Reporte de Movimiento(s)")
            .setSingleChoiceItems(pos, -1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                    reporte(pos[which].toString());
                        dialog.dismiss();



                }
            });


    AlertDialog alertDialog = builder.create();
    alertDialog.show();


}

    private void reporte(String s) {
final String s2=s;

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.inventario, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
        TextView t12=(TextView)v2.findViewById(R.id.encabezado);
        t12.setText(s);
        ArrayAdapter adapter = null;
        if(s.equals("Venta")){
            t12.setText(s);
            ArrayList<com.rasoftec.tpos2.data.venta> lista_info = base.get_movimiento(codigo);
            adapter = new ArrayAdapter<>(nodo_lista.this, android.R.layout.simple_list_item_1, lista_info);
        }else{

            t12.setText(s+"\nTotal de Cobro Q"+" "+round(base.get_cobro_total(codigo),2));
            t12.setTextSize(23);
            ArrayList<nodo_factura> lista_info = base.get_cobro(codigo);
            adapter = new ArrayAdapter<>(nodo_lista.this, android.R.layout.simple_list_item_1, lista_info);
        }



        t.setAdapter(adapter);

        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(s2.equals("Venta")){  com.rasoftec.tpos2.data.venta reviso = (com.rasoftec.tpos2.data.venta) parent.getItemAtPosition(position);
                            detalle_movimiento(reviso);}

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
//                    public void onClick(DialogInterface dialog, int id) {
//                        saldo_actual();
//
//                    }
//                })
;


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private void detalle_movimiento( com.rasoftec.tpos2.data.venta reviso) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v2 = inflater.inflate(R.layout.inventario, null);
        ListView t = (ListView) v2.findViewById(R.id.lista_inventario);
        TextView t12=(TextView)v2.findViewById(R.id.encabezado);
        t12.setText("Detalle de Movimiento(s)"+"\n"+"Venta #"+""+reviso.getCodigo());
        t12.setTextSize(20);
        ArrayList<venta_detalle> lista_info = reviso.detalles;
        ArrayAdapter adapter = new ArrayAdapter<>(nodo_lista.this, android.R.layout.simple_list_item_1, lista_info);

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
//                    public void onClick(DialogInterface dialog, int id) {
//                        saldo_actual();
//
//                    }
//                })
        ;


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

}
