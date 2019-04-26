package com.rasoftec.tpos2;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class lista extends AppCompatActivity implements SearchView.OnQueryTextListener {
    String  ruta_actual;
    ArrayList<Nodo_tarea> lista_info=new ArrayList<>();
    private ListView lista_g;
    database base;
    int tipo;
    ArrayAdapter<Nodo_tarea> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
        base=new database(this);

        ruta_actual=getIntent().getStringExtra("ruta");
        tipo=getIntent().getIntExtra("tipo_venta",0);
        get_ruta(tipo);
        nodo_lista_todo();
        setTitle("Lista de Clientes ");

    }


    private void get_ruta( int tipo) {




            if(tipo==1)
           lista_info=base.getallcliente();
            if(tipo==2)
                lista_info=base.getallcliente_fuera();



    }

    private void nodo_lista_todo() {

       




        lista_g=(ListView) findViewById(R.id.lista);

         adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,lista_info);

        lista_g.setAdapter(adapter);

        lista_g.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Nodo_tarea   reviso= (Nodo_tarea) parent.getItemAtPosition(position);
                mensaje(reviso.descripcion,reviso);

            }
        });



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



    private void mensaje(String reviso,Nodo_tarea reviso2){
        Toast.makeText(this,reviso, LENGTH_SHORT).show();
        Intent i= new Intent(this,nodo_lista.class);
    i.putExtra("cliente",  reviso2.cod_cliente);
        i.putExtra("ruta",ruta_actual);
        i.putExtra("tipo_venta",tipo);
        startActivity(i);
        finish();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }



// Area de Menu para agregar search

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

        switch (item.getItemId()){

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
        final Intent t= new Intent(this,login.class);
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
        final Intent t= new Intent(this,menu_principal.class);
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
}
