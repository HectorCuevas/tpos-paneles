package com.rasoftec.tpos2;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos2.activities.DetailActivity;
import com.rasoftec.tpos2.Funciones.Categorias;
import com.rasoftec.tpos2.Data.database;
import com.rasoftec.tpos2.Data.nodo_factura;
import com.rasoftec.tpos2.manejo_errores.ErrorRed;
import com.rasoftec.tpos2.sensor.ssdi;
import com.rasoftec.tpos2.Data.venta_detalle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static android.text.TextUtils.isEmpty;

public class venta extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Categorias productos = new Categorias();
    private BottomSheetDialog mBottomSheetDialog, mBottomSheetDialog1, ventanaModal, ventanaS3;
    private int nuevoPrecio = 15;
    private String actual;
    private String nombre;
    private View bottomSheet, bottomSheeet1, modal;
    private String ruta;
    private ArrayList<nodo_producto> carrito = new ArrayList<>();
    private ArrayList<nodo_producto> lista_info = new ArrayList<>();
    private ListView lista_g;
    private RadioButton radio10Gratis;
    private ArrayList<nodo_factura> facturas_actual;
    private database base;
    private String numero;
    private Boolean isPhoneNumber;
    private int tipo, cantidad = 10;
    private int PhoneNumber = 0;
    private int cantidadRecarga = 10;
    private int total_pos;
    private EditText numeroDeTelefono;
    private ErrorRed ero;
    private int codigo = 0;
    private ArrayAdapter<nodo_producto> adapter;

    /*** Nuevas variables y  constantes ***/
    private CheckBox bonificacion;
    private RadioButton radio15;
    private RadioButton radio20;
    private RadioButton radio25Gratis;
    private EditText txtOtros;
    private View bottomSheetLayout;
    private String valorRecarga = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ApplicationTpos.codigos.clear();
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

        setTitle("Area de Venta(s)");


        bottomSheet = findViewById(R.id.framelayout_bottom_sheet);


        bottomSheeet1 = findViewById(R.id.framelayout_bottom_sheet1);



    }


    private void setCategorias(String codigo ,nodo_producto articulo, String codCategoria) {
       // Toast.makeText(getApplicationContext(), codCategoria, LENGTH_SHORT).show();
        switch (codCategoria) {
            case "A1":
                esCantidadProducto(articulo, codCategoria);
                break;
            case "S2":
                isPhoneNumber = true;
                logicaProductos(codigo, articulo);
                //agregarProductoSinCantidad(valorRecarga, articulo, codCategoria);
                break;
            case "S1":
                isPhoneNumber = false;
                logicaProductos(codigo, articulo);
                esTarjetaSim();
                break;
            case "O1":
                esCantidadProducto(articulo, codCategoria);
                break;
            case "T1":
                esCantidadProducto(articulo, codCategoria);
                break;
            case "S3":
                productoSinContrato(articulo, codCategoria);
                break;
            default:
                break;
        }
    }

    private void logicaProductos(final String codigoArticulo, final nodo_producto articulo) {
        try {
            /*** Parte del layout ***/
            //  Toast.makeText(getApplicationContext(), articulo.getDescripcion(), Toast.LENGTH_LONG).show();
            bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
            numeroDeTelefono = bottomSheetLayout.findViewById(R.id.txtNumeroDeTelefono);
            numeroDeTelefono.requestFocus();
            showKeyboard();
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
                public void onClick(View view) {

                   // CheckBox bonificacion = bottomSheetLayout.findViewById(R.id.checkBoxBono);
                   try{
                       ApplicationTpos.codigos.add("S2");
                       radio10Gratis = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_10);
                       radio15 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_15);
                       radio25Gratis = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_20);
                       radio20 = (RadioButton) bottomSheetLayout.findViewById(R.id.radio_25);
                       txtOtros = (EditText) bottomSheetLayout.findViewById(R.id.txtOtros);

                       /*** Obtenemos el codigo del articulo ***/
                       final String codigoCategoria = productos.getCategoria(articulo.getDescripcion());
                       //  Toast.makeText(getApplicationContext(), codigoCategoria, Toast.LENGTH_LONG).show();
                       /*** Validamos si es sim para hacer recarga o numero de telefono ***/
                       isPhoneNumber = productos.validarSiEsNumero(codigoCategoria);
                       String valorRadio = "10";
                       if (!isEmpty(txtOtros.getText().toString())) {
                           valorRadio = txtOtros.getText().toString();
                           radio10Gratis.setChecked(false);
                       } else if (radio10Gratis.isChecked()) {
                           valorRadio = "10";
                       } else if (radio15.isChecked()) {
                           valorRadio = "10";
                       } else if (radio20.isChecked()) {
                           valorRadio = "25";
                       } else if (radio25Gratis.isChecked()) {
                           valorRadio = "25";
                       }
                       valorRecarga = valorRadio;
                       agregarProductoSinCantidad(valorRecarga, articulo, codigoCategoria);
                   }catch (Exception ex){
                       Toast.makeText(getApplicationContext(), "Agrege un numero de telefono "+ex, Toast.LENGTH_LONG).show();
                   }

                    mBottomSheetDialog.dismiss();
                }
            });
            String codCategoria = productos.getCategoria(articulo.getDescripcion()) ;
         //   setCategorias(codCategoria, articulo,codCategoria );
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    public void esTarjetaSim() {
        final View bottomSheetLayoutSim = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog1, null);
        mBottomSheetDialog1 = new BottomSheetDialog(this);
        mBottomSheetDialog1.setContentView(bottomSheetLayoutSim);
        mBottomSheetDialog1.show();
        (bottomSheetLayoutSim.findViewById(R.id.button_closeSim)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog1.dismiss();
            }
        });
        /**** i ***/
        (bottomSheetLayoutSim.findViewById(R.id.button_okSim)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radio0 = (RadioButton) bottomSheetLayoutSim.findViewById(R.id.radio_0);
                RadioButton radios10 = (RadioButton) bottomSheetLayoutSim.findViewById(R.id.radio_s10);
                RadioButton radios15 = (RadioButton) bottomSheetLayoutSim.findViewById(R.id.radio_s15);
                TextView txtOTrosSim = (TextView) bottomSheetLayoutSim.findViewById(R.id.txtOtroValorSim);

                if (!isEmpty(txtOTrosSim.getText().toString())) {
                    nuevoPrecio = Integer.parseInt(txtOTrosSim.getText().toString());
                } else if (radio0.isChecked()) {
                    nuevoPrecio = 0;
                } else if (radios10.isChecked()) {
                    nuevoPrecio = 10;
                } else if (radios15.isChecked()) {
                    nuevoPrecio = 15;
                }
                mBottomSheetDialog1.dismiss();
            }
        });
    }


    private void esCantidadProducto(final nodo_producto producto, final String codigoCategoria) {
        final View ventanaCantidad = getLayoutInflater().inflate(R.layout.bottom_sheet_cantidad, null);
        ventanaModal = new BottomSheetDialog(this);
        ventanaModal.setContentView(ventanaCantidad);
        ventanaModal.show();

        (ventanaCantidad.findViewById(R.id.button_closeDialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaModal.dismiss();
            }
        });
        (ventanaCantidad.findViewById(R.id.button_okDialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationTpos.codigos.add("A1");
                EditText txtcantidad =  ventanaModal.findViewById(R.id.txtCantidadDialog);
                int cantidad = Integer.parseInt(txtcantidad.getText().toString());
                producto.setCompra(cantidad);
               // Toast.makeText(getApplicationContext(), "WENAS", Toast.LENGTH_LONG).show();
                agregarProductoConCantidad(producto, codigoCategoria);
                ventanaModal.dismiss();
            }
        });
    }

    private void productoSinContrato(final nodo_producto producto, final String codigoCategoria) {
        final View ventanaCantidad = getLayoutInflater().inflate(R.layout.botton_sheet_productos_sin_contrato, null);
        ventanaS3 = new BottomSheetDialog(this);
        ventanaS3.setContentView(ventanaCantidad);
        ventanaS3.show();

        (ventanaCantidad.findViewById(R.id.btnSinContratoClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventanaS3.dismiss();
            }
        });
        (ventanaCantidad.findViewById(R.id.btnSinContratoOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try{
                   EditText txtValor =  ventanaS3.findViewById(R.id.txtSinContratoValor);
                   EditText txtNumero =  ventanaS3.findViewById(R.id.txtSinContratoNumero);
                   EditText txtcel =  ventanaS3.findViewById(R.id.txtSinContratoTel);
                   double precio = Double.parseDouble(txtValor.getText().toString());
                   String numero = txtNumero.getText().toString();
                   int cel = Integer.parseInt(txtcel.getText().toString());
                   ApplicationTpos.codigos.add("A1");
                   producto.setCompra(1);
                   producto.setPrecio(precio);
                   producto.setNumeroCel(cel);
                   producto.setSerial("SM:"+numero+"F:"+precio);
                   productoS3(producto, codigoCategoria);
                   ventanaS3.dismiss();
               }catch (Exception e){
                   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    private void productoS3(nodo_producto producto, String codigoCategoria){
        /** Para codigos S3 **/
        nodo_producto bodega = existe(producto.getCodigo(), cantidad);
        nodo_producto articulo = new nodo_producto();
        String codigo = producto.getCodigo();
        int cantidad = producto.getCompra();
        String descripcion =producto.getDescripcion();
        int stock = producto.getStock();
        int numeroCel = producto.getNumerocel();
        double precio = producto.getPrecio();
        nuevoPrecio =(int) precio;
        articulo.setSerial(producto.getSerial());
        articulo.setCodigo(codigo);
        articulo.setCompra(cantidad);
        articulo.setDescripcion(descripcion);
        articulo.setStock(stock);
        articulo.setNumeroCel(numeroCel);
        articulo.setPrecio(precio);
        hayStock(bodega, articulo, cantidad, codigoCategoria );
        actualizar_total(carrito);
    }

    private void agregarProductoConCantidad(nodo_producto producto, String codigoCategoria){
        /** Para codigos T1, O1, A1 **/
        nodo_producto bodega = existe(producto.getCodigo(), cantidad);
        nodo_producto articulo = new nodo_producto();
        String codigo = producto.getCodigo();
        int cantidad = producto.getCompra();
        String descripcion =producto.getDescripcion();
        int stock = producto.getStock();
        int numeroCel = 0;
        double precio = producto.getPrecio();

        articulo.setCodigo(codigo);
        articulo.setCompra(cantidad);
        articulo.setDescripcion(descripcion);
        articulo.setStock(stock);
        articulo.setNumeroCel(numeroCel);
        articulo.setPrecio(precio);
        hayStock(bodega, articulo, cantidad, codigoCategoria );
        actualizar_total(carrito);
    }

    private void agregarProductoSinCantidad(String valorRadio, nodo_producto articulo, String codigoCategoria) {
        isPhoneNumber = true;
        cantidadRecarga = Integer.parseInt(valorRadio);
        String phone = numeroDeTelefono.getText().toString();
        int numeroCel = Integer.parseInt(phone);
        if (cantidadRecarga > 0) {
            /*** creamos un nuevo articulo ***/
            nodo_producto stock = existe(articulo.getCodigo(), cantidadRecarga);
            articulo.setNumeroCel(numeroCel);

            /*** Cambiamos el valor si es numero o recarga ***/
            validateIfPhoneNumber(cantidadRecarga);
            /*** Valida si hay disponibles, si hay lo agrega al carrito ***/
            hayStock(stock, articulo, cantidad, codigoCategoria);
            /*** Agregamos la recarga automatica ***/
            actualizar_total(carrito);
            agregarRecarga(phone);
        }else{
            nodo_producto existe = existe(articulo.getCodigo(), cantidad);
            carrito.remove(existe);
            actualizar_total(carrito);
            nodo_lista_todo();
        }
    }

    private void agregarRecarga(String numero) {
        nodo_producto recarga = new nodo_producto();
        recarga.setCodigo("ORGA.01");
        recarga.setCompra(cantidadRecarga);
        recarga.setDescripcion("Recarga electronica");
        recarga.setStock(1500);
        recarga.setNumeroCel(Integer.parseInt(numero));
        Double precio = 0D;
        if (radio10Gratis.isChecked() || radio25Gratis.isChecked()) {
            precio = 0.0D;
        } else
            precio = 1.0D;
        recarga.setPrecio(precio);
        // Toast.makeText(getApplicationContext(), precio + "", Toast.LENGTH_LONG).show();
        carrito.add(recarga);
        actualizar_total(carrito);
        nodo_lista_todo();
    }

    public void hayStock(nodo_producto stock, nodo_producto articulo, int cantidad, String codigo) {
        if (stock == null && codigo.equals("S1")) {
            /*** INVESTIGAR DE DONDE SALE NUEVO PRECIO XXDXDXXDXDXD pd sale de la ventana
             *  de precio del sim alli se setea el nuevo precio
             *
             *  ***/
            articulo.setPrecio(nuevoPrecio);
            articulo.setCompra(1);
            carrito.add(articulo);
        } else if (stock == null) {
            articulo.setCompra(cantidad);
            carrito.add(articulo);
        }
    }

    private int validateIfPhoneNumber(int quantity) {
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
                String codCategoria = productos.getCategoria(reviso.getDescripcion()) ;
                setCategorias(reviso.getCodigo(), reviso, codCategoria);
                //logicaProductos(reviso.getCodigo(), reviso);
                // mensaje(reviso.getCodigo(), reviso);
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
                tem.put("forma_pag", "CONT");
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

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void configuracion() {


    }

}