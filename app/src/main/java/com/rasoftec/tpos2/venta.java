package com.rasoftec.tpos2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.detalle;
import com.rasoftec.tpos2.data.nodo_factura;
import com.rasoftec.tpos2.data.webservice;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;
import com.rasoftec.tpos2.sensor.ssdi;
import com.rasoftec.tpos2.data.venta_detalle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;

public class venta extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String CustomerName;

    private BottomSheetDialog mBottomSheetDialog;
    private String CustomerNit;
    String m_text;
    private String actual;
    private String nombre;
    View bottomSheet;
    int pos;
    private String ruta;
    ArrayList<nodo_producto> carrito = new ArrayList<>();
    ArrayList<nodo_producto> lista_info = new ArrayList<>();
    private ListView lista_g;
    RadioButton radio10;
    private CheckBox abono;
    EditText cobro_venta;
    ArrayList<nodo_factura> facturas_actual;
    database base;
    String numero;
    private Boolean isPhoneNumber;
    int tipo, cantidad;
    int PhoneNumber = 0;
    int contadorCantidadRecargas = 0;
    int cantidadRecarga = 10;
    int total_pos;
    double abono_actual_v = 0.00;
    EditText numeroDeTelefono;
    private ErrorRed ero;
    int codigo = 0;
    ArrayAdapter<nodo_producto> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);
        ero = new ErrorRed(this);
        base = new database(this);
        actual = getIntent().getStringExtra("actual");
        nombre = getIntent().getStringExtra("nombre");
        ruta = getIntent().getStringExtra("ruta");
        tipo = getIntent().getIntExtra("tipo_venta", 1);
        facturas_actual = base.get_factura();
//        prueba_factura(base.get_factura_activa());
        lista_g = (ListView) findViewById(R.id.listap);
        producto();
        nodo_lista_todo();
        configuracion();


        bottomSheet = findViewById(R.id.framelayout_bottom_sheet);



        setTitle("Area de Venta(s)");
    }


    private void configuracion() {


    }

    private void mensaje(String reviso, final nodo_producto reviso2) {
        try {
            if (reviso.equals("ORGA.01")) {
                isPhoneNumber = false;
            }
            else {
                isPhoneNumber = true;
            }
            final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
            numeroDeTelefono = bottomSheetLayout.findViewById(R.id.txtNumeroDeTelefono);
            numeroDeTelefono.requestFocus();
            mBottomSheetDialog = new BottomSheetDialog(this);
            mBottomSheetDialog.setContentView(bottomSheetLayout);
            mBottomSheetDialog.show();

            (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

            (bottomSheetLayout.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        CheckBox bonificacion = bottomSheetLayout.findViewById(R.id.checkBoxBono);
                        radio10 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_10);
                        RadioButton radio15 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_15);
                        RadioButton radio20 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_20);
                        RadioButton radio25 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_25);
                        RadioButton radio50 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_50);
                        RadioButton radio100 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_100);
                        EditText txtOtros = (EditText) bottomSheetLayout.findViewById(R.id.txtOtros);
                        if( !isEmpty(txtOtros.getText().toString())){
                            m_text = txtOtros.getText().toString();
                        }else if (radio10.isChecked()) {
                            m_text = "10";
                        }else if (radio15.isChecked()) {
                            m_text = "15";
                        }else if (radio20.isChecked()) {
                            m_text = "20";
                        }else if (radio25.isChecked()) {
                            m_text = "25";
                        }else if (radio50.isChecked()) {
                            m_text = "50";
                        }else if (radio100.isChecked()) {
                            m_text = "100";
                        }

//                        TextView total = (TextView) bottomSheetLayout.findViewById(R.id.tv_title);
                        cantidadRecarga=Integer.parseInt(m_text);
                        String phone = numeroDeTelefono.getText().toString();
                        PhoneNumber = Integer.parseInt(phone);
                        if (m_text.length() > 0) {
                            cantidad = Integer.parseInt(m_text);
                            if (cantidad > 0) {
                                nodo_producto tem = reviso2;
                                nodo_producto existe = existe(reviso2.getCodigo(), cantidad);
                                ValidateIfPhoneNumber(cantidad); //This function validate if it's phone number or reload / return int:cantidad.
                                tem.setNumeroCel(PhoneNumber);
                                if (existe == null) {
                                    tem.setCompra(cantidad);
                                    if(bonificacion.isChecked()){
                                        tem.setPrecio(0);
                                    }
                                    carrito.add(tem);
                                }
                                actualizar_total(carrito);
                                nodo_producto reviso1 = new nodo_producto();
                                reviso1.setCodigo("ORGA.01");
                                reviso1.setCompra(cantidadRecarga);
                                reviso1.setDescripcion("Recarga electronica");
                                reviso1.setPrecio(1.0);
                                reviso1.setStock(1500);
                                reviso1.setNumeroCel(contadorCantidadRecargas);
                                //mensaje(reviso1.getCodigo(), reviso1);
                                carrito.add(reviso1);
                                contadorCantidadRecargas += 10;
                                actualizar_total(carrito);
                                nodo_lista_todo();
                            } else {
                                nodo_producto existe = existe(reviso2.getCodigo(), cantidad);
                                carrito.remove(existe);
                                actualizar_total(carrito);
                                nodo_lista_todo();
                            }
                        } else {
                            info("Debe Ingresar Una Cantidad para Agregar el Articulo");

                        }

                        Toast.makeText(getApplicationContext(), ""+PhoneNumber, Toast.LENGTH_SHORT).show();
                        mBottomSheetDialog.dismiss();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    private int ValidateIfPhoneNumber(int quantity) {
        if (isPhoneNumber) {
            PhoneNumber = quantity;
            cantidad = 1;
        }
        return cantidad;
    }

    private void info(String info) {
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
    }

    private nodo_producto existe(String codigo, int cantidad) {
        nodo_producto tem = null;
        Iterator<nodo_producto> ite = carrito.iterator();

        while (ite.hasNext()) {
            nodo_producto tem2 = ite.next();
            if (tem2.getCodigo() == codigo) {
                tem2.setCompra(cantidad);
                tem = tem2;
                break;
            }
        }
        return tem;
    }

    private void actualizar_total(ArrayList<nodo_producto> carrito) {
        Iterator<nodo_producto> ite = carrito.iterator();
        double total_actual = 0;
        while (ite.hasNext()) {
            nodo_producto tem2 = ite.next();
            total_actual += tem2.getPrecio() * tem2.getCompra();


        }
        setTitle("Total  es" + " " + "Q" + " " + venta_detalle.round(total_actual, 2));
    }


    private void nodo_lista_todo() {


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_info);

        lista_g.setAdapter(adapter);

        lista_g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nodo_producto reviso = (nodo_producto) parent.getItemAtPosition(position);
                /*if(reviso.getCodigo().equals("ORGA.01")){
                    nodo_producto reviso1 = new nodo_producto();
                    reviso1.setCodigo("ORGA.01");
                    reviso1.setCompra(0);
                    reviso1.setNumeroCel(0);
                    reviso1.setPrecio(1.0);
                    reviso1.setStock(1500);
                    mensaje(reviso.getCodigo(), reviso1);
                }*/
                mensaje(reviso.getCodigo(), reviso);
                //CustomersData("Cantidad de recarga", false);


            }
        });


    }

    private void producto() {
        String[] tem = base.get_access();
        Log.i("info estado", tem.toString());
        int estado = Integer.parseInt(tem[1]);
        if (estado == 2 || estado == 1)
            lista_info = base.getall();
        if (estado == 3)
            lista_info = base.getall_fuera();


    }

    public void vender(View v) {
        Iterator<nodo_producto> ite = carrito.iterator();
        double total_actual = 0;
        String descripcion = "";
        while (ite.hasNext()) {
            nodo_producto tem2 = ite.next();
            total_actual += tem2.getPrecio() * tem2.getCompra();
            descripcion += tem2.getDescripcion() + "\n Q" + " " + venta_detalle.round(tem2.getPrecio() * tem2.getCompra(), 2) + "\n";
        }
        if (venta_detalle.round(total_actual, 2) > 0) {
            forma_pago(descripcion, total_actual);
            //This function displays dialog message if it's name or nit // return Alertdialog type: dialog
            // CustomersData("Cantidad de recarga", false);
            // CustomersData("Ingrese Nit del cliente", false);
        } else {
            info("El Valor de la Venta debe ser Mayor de Q 0.00");
        }

    }

    private void CustomersData(String Description, final boolean isName) {
        String[] singleChoiceItems = getResources().getStringArray(R.array.opcionesRecarga);
        final int itemSelected = 0;
        String DialogMessage = "", DialogButton = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogMessage = Description;
        DialogButton = "Agregar";

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Otro");
        builder.setView(input);
        builder.setTitle(DialogMessage)
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                        String valorRecarga;
                        pos = selectedIndex;
                        valorRecarga = getResources().getStringArray(R.array.opcionesRecarga)[selectedIndex];
                        cantidadRecarga = Integer.parseInt(valorRecarga);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equals("")) {
                            cantidadRecarga = Integer.parseInt(input.getText().toString());
                            // Toast.makeText(getApplicationContext(), ""+cantidadRecarga, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void forma_pago(final String descripcion, Double total_actual) {


        final String descripcion2 = descripcion;
        final Double total = total_actual;
        String[] tem = base.get_access();
        Log.i("info estado", tem.toString());
        int estado = Integer.parseInt(tem[1]);
        if (estado == 3) {
            operacion_venta(descripcion2, total, "credito");
        } else {
            final CharSequence[] pos = {"Contado", "Credito"};
            operacion_venta(descripcion2, total, "Contado");
        }
    }

    public void cambiarActividad(View v) {
        Intent cambiarActividad = new Intent(this, DetailActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }

    private void operacion_venta(String descripcion, Double total_actual, String tipo) {
        final String tipo2 = tipo;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Total es" + " " + "Q" + " " + venta_detalle.round(total_actual, 2))
                .setMessage(descripcion);
        builder.setPositiveButton("Realizar Venta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
// Enviarmos la venta
                try {
                    realizar_venta(tipo2);
                    //             iniciamos enviado los productos para realizar la rebaja de inventario

                    base.rebaja_inventario(carrito);
                    //            Area donde iniciamos  pasar los productos a la otra lista para el envio
//                    detalle(carrito);

//                    Almacenamos el movimiento en forma local
//                movimiento(carrito);

                    //            Iniciamos  revisar si hay un producto de Orga
                    Intent i = new Intent(venta.this, DetailActivity.class);
                    nodo_producto t2 = existeorga(carrito);
                    if (t2 != null) {
                        total_pos = t2.getCompra();
                        //quitar esto y descomentar orga
                        startActivity(i);
                        //orga();

                    } else {
                        i.putExtra("ruta", ruta);
                        startActivity(i);
                    }
                    // info("Se Realizo la Venta con Exito");
                    base.setcliente(actual);
                } catch (Exception e) {

                    Log.i("Error fatal", e.getMessage());
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    private void realizar_venta(String tipo) {
        ApplicationTpos.totalEncabezado = total_carrito(carrito);
        ApplicationTpos.detalleVenta = carrito;
        try {
            if (tipo.toLowerCase().equals("contado")) {
                JSONObject tem = new JSONObject();
                String tipo2 = "CONT";
                tem.put("usuario_movilizandome", base.get_ruta().trim());
                tem.put("co_cli", String.valueOf(actual));
                tem.put("forma_pag", CustomerNit + "." + CustomerName);
                tem.put("total", total_carrito(carrito));
                tem.put("cobrado", total_carrito(carrito));
                tem.put("Procesado", "S");
                tem.put("fact_num2", 0);
                tem.put("Cobrado2", 0.0);
                codigo = base.set_venta(carrito, tem);
                base.ver_detalle();
            } else if (tipo.toLowerCase().equals("credito")) {
                JSONObject tem = new JSONObject();
                String tipo2 = "CRED";
                tem.put("usuario_movilizandome", base.get_ruta());
                tem.put("co_cli", String.valueOf(actual));
                tem.put("forma_pag", tipo2);
                tem.put("total", total_carrito(carrito));
                tem.put("cobrado", 0.00);
                if (base.get_estado() == 3) {
                    tem.put("Procesado", "D");
                } else {
                    tem.put("Procesado", "S");
                }
                tem.put("fact_num2", 0);
                tem.put("Cobrado2", 0.0);
                codigo = base.set_venta(carrito, tem);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void abono_actual(final String descripcion2, final Double total, final String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad de Abono en Q");
        final Double[] cantidad = {0.00};

        final EditText input = new EditText(this);
        String[] t2 = base.get_access();
        int t3 = Integer.parseInt(t2[1]);
        if (t3 < 1) {
            input.setEnabled(false);
            input.setText("0");
        }

        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(input);


        builder.setPositiveButton("Abono", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String m_Text = input.getText().toString();

                if (m_Text.length() > 0) abono_actual_v = Double.parseDouble(m_Text);


                Log.i("Valor Actual del Abono", "" + abono_actual_v);
                operacion_venta(descripcion2, total, s);
                dialog.dismiss();


            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        input.requestFocus();


    }

    private double total_carrito(ArrayList<nodo_producto> carrito) {
        double total_encabezado = 0;
        Iterator<nodo_producto> ite = carrito.iterator();
        while (ite.hasNext()) {
            nodo_producto t14 = ite.next();

            double total = t14.getPrecio() * t14.getCompra();

            total_encabezado += total;


        }
        return total_encabezado;
    }

    private void detalle(ArrayList<nodo_producto> carrito) {
        ArrayList<detalle> tem = new ArrayList<>();
        Iterator<nodo_producto> ite = carrito.iterator();
        while (ite.hasNext()) {
            nodo_producto tem2 = ite.next();
            //detalle tem3 = new detalle(tem2.getCodigo(), tem2.getPrecio(), tem2.getCompra(), tem2.getPrecio() * tem2.getCompra());
            // tem.add(tem3);
        }


    }

    private void orga() {
        Nodo_tarea esta = null;
        if (tipo == 1)
            esta = base.getcliente_actual(actual);
        else if (tipo == 2) esta = base.getcliente_actual_fuera(actual);
        final CharSequence[] t3 = esta.telefono.split(",");

        if (t3.length == 1) {
            actual_numero(t3[0].toString());
            recargar(total_pos);
        } else {


            final EditText input = new EditText(this);
            input.setEnabled(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Set the dialog title

            builder.setTitle("POS Activo" + " " + "Total de Compra POS Q" + " " + total_pos)
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setSingleChoiceItems(t3, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            actual_numero(t3[which].toString());
                            input.setEnabled(true);


                        }
                    })


                    // Set the action buttons
                    .setPositiveButton("Recarga", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Valor para recargar", "" + total_pos);
                            if (input.getText().toString().length() > 0) {
                                int total = Integer.parseInt(input.getText().toString());
                                Log.i("Valor para recargar", "" + total);
                                if (total > 0 && total <= total_pos) recargar(total);
                                else orga();

                            } else {

                                orga();
                            }
                            input.setEnabled(false);


                        }
                    });
//                .setNegativeButton("Reviso", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });


            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Ingresar Cantidad de Recarga");
            builder.setView(input);

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }

    }

    private void recargar(int total) {
        ssdi saldo = new ssdi();
        Intent i2 = new Intent(this, menu_principal.class);
        if (saldo.recarga_saldo(numero, this, "" + total)) {
            total_pos = total_pos - total;

            if (total_pos <= 0) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("Espero", "Espero");
                i2.putExtra("ruta", ruta);
                startActivity(i2);
            } else {
                orga();

            }


        }

//            i2.putExtra("ruta", ruta);
//        startActivity(i2);}


    }

    private void actual_numero(String s) {
        numero = s;
    }

    private nodo_producto existeorga(ArrayList<nodo_producto> carrito) {
        Iterator<nodo_producto> t = carrito.iterator();
        boolean si = false;
        while (t.hasNext()) {
            nodo_producto t2 = t.next();
            if (t2.getCodigo().trim().equals("ORGA.01") || t2.getCodigo().toLowerCase().trim().equals("tae rutas")) {

                return t2;

            }


        }

        return null;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.busqueda, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.busco);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        // searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.closes:
                cierro();
                break;
            case R.id.mepri:
                menu();
                break;

        }


        return true;
    }

    private void cierro() {
        final Intent t = new Intent(this, login.class);
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
        codigo.setText("Salir del Sistema");
        TextView codigo2 = (TextView) v2.findViewById(R.id.info_mensa2);
        codigo2.setText("Desea Salir del Sistema");

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void menu() {
        final Intent t = new Intent(this, menu_principal.class);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i2 = new Intent(this, menu_principal.class);

            startActivity(i2);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}