package com.rasoftec.tpos2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.Data.database;
import com.rasoftec.tpos2.Data.nodo_factura;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class cobro extends AppCompatActivity {

    private TableLayout factura;
    Double total_pago=0.00;
    String actual_cliente;
    private String nombre;
    ArrayList<nodo_factura> factural;
    database base;
    Nodo_tarea nodo_cliente;
    String ruta_actual;
    private ErrorRed ero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cobro);
          base=new database(this);
         actual_cliente=getIntent().getStringExtra("actual");
        nombre=getIntent().getStringExtra("nombre");
        ruta_actual=getIntent().getStringExtra("ruta");
        factural= new ArrayList<>();
    nodo_cliente = base.getcliente_actual(actual_cliente);
        TextView t12=(TextView)findViewById(R.id.cliente);
        t12.setText(nodo_cliente.descripcion);
        ver_facturas(actual_cliente);
ero=new ErrorRed(this);
        setTitle("Area de Cobro");

    }
public void pago(View v){

    TextView te= (TextView)findViewById(R.id.abono);

    Double tem1 =0.00;
    if(te.length()>0)  tem1=Double.parseDouble(te.getText().toString());



    if(tem1>0&&tem1<=total_pago){


        asignar_Pago(tem1);


        total_pago=total_pago-tem1;
        TextView tem12=(TextView)findViewById(R.id.total);
        tem12.setText(Html.fromHtml("<b>Total a Pagar es Q"+" "+total_pago+"</b>"));

        try {
            mensaje();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }else{
        Toast.makeText(this,"Error el Valor del Cobro es Menor que Cero o El Pago es Mayor ",Toast.LENGTH_SHORT).show();

    }


}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void mensaje() throws IOException {

        final Intent i = new Intent(this, venta.class);
        final Intent i2 = new Intent(this, menu_principal.class);

//            AsyncTask<String, Void, String> t12 = new webservice(factural, ruta_actual, this);
        Iterator<nodo_factura> ta = factural.iterator();
        while (ta.hasNext()) {

            base.add_factura(ta.next());
        }
//        base.ver_encabezado();
        String t14 = null;
        //            t14 = t12.execute(ruta_actual, "5").get();

        Toast.makeText(this, "Se realizo el Pago de Forma Exitosa", Toast.LENGTH_SHORT).show();
//            pasar_cobro(factural);
        if (total_pago == 0) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Area de Venta(s)");
        builder.setMessage("Desea Realizar una Venta");


        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                i2.putExtra("ruta", ruta_actual);
                startActivity(i2);
                finish();
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actualizar_factura(factural);
                i.putExtra("actual", nodo_cliente.cod_cliente);
                i.putExtra("ruta", ruta_actual);
                startActivity(i);
                finish();


            }
        });
        AlertDialog alertDialog = builder.create();
            base.setcliente(nodo_cliente.cod_cliente);
        alertDialog.show();

    }else{
            i2.putExtra("ruta", ruta_actual);
            startActivity(i2);
            finish();


        }




    }

    private void pasar_cobro(ArrayList<nodo_factura> factural) {
        base.cobro(factural);

    }

    private void info(String info) {
        Toast.makeText(this,info,Toast.LENGTH_LONG).show();
    }
    private void mensaje2() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

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
        codigo.setText("Por el Momento no se Cuenta con Conexion a Internet para Realizar el Cobro ");


        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void actualizar_factura(ArrayList<nodo_factura> factural_2) {
        Iterator<nodo_factura> item = factural_2.iterator();
        while(item.hasNext()){
            nodo_factura tem = item.next();
            if(tem.getPago()>0){
                base.update_factura(tem.getPago(),tem.getCodigo_factura());

            }



        }

    }

    private void versaldo() {
        Iterator<nodo_factura> ite = factural.iterator();
        while(ite.hasNext()){
            nodo_factura tem = ite.next();
            Log.i("Totales",tem.getSaldo()+" " +tem.getPago());


        }
    }

    private void asignar_Pago(Double tem1) {
        Iterator<nodo_factura> ite = factural.iterator();
        while(ite.hasNext()){
            nodo_factura tem = ite.next();
            double saldo = tem.getSaldo();
            if(saldo<tem1){
                tem.setPago(saldo);
                base.asignar_pago(tem,tem.getCodigo_factura());
                tem1=tem1-saldo;

            }
            else if(saldo==tem1  ){
                tem.setPago(tem1);
                base.set_factura(Integer.parseInt(tem.getCodigo_factura()));

                break;

            }else if(tem1<saldo){
                tem.setPago(tem1);
                base.asignar_pago(tem,tem.getCodigo_factura());

            }




        }

    }

    private void ver_facturas(String actual) {
        factura=(TableLayout) findViewById(R.id.tablafac);


        //            AsyncTask<String, Void, String> t12 = new CallWebService();
//
//            String t14 = t12.execute("10236002","1").get();
//            Log.i("info de facturas ",t14);
        Log.i("Actual ",actual);
//            JSONArray facturas= new JSONArray(t14);
        Double total_pagar=0.00;

        factural=base.get_cobro(actual);
        Iterator<nodo_factura> item = factural.iterator();

        while(item.hasNext()){
           nodo_factura  t2 = item.next();
//                t2.get("fact_num");
//                t2.get("saldo");
//                t2.get("fec_emis");

            TableRow tem= new TableRow(this);
            TextView t1=new TextView(this);
            TextView t3=new TextView(this);
            TextView t4=new TextView(this);
            t3.setText(""+t2.getSaldo());
            t1.setText(t2.getCodigo_factura());

            t4.setText(t2.getFecha());
            t1.setTextSize(20);
            t3.setTextSize(20);
            t4.setTextSize(20);
            tem.addView(t1);
            tem.addView(t3);
            tem.addView(t4);
            total_pagar+=t2.getSaldo();

            factura.addView(tem);


        }
        TextView tem12=(TextView)findViewById(R.id.total);
        total_pago+=total_pagar;
        tem12.setText(Html.fromHtml("<b>Total a Pagar es Q"+" "+total_pagar+"</b>"));

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






}
