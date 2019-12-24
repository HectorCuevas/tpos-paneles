package com.rasoftec.tpos2.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos2.Adapters.InventarioAdapter;
import com.rasoftec.tpos2.Beans.inventario;
import com.rasoftec.tpos2.Data.database;
import com.rasoftec.tpos2.Data.webservice;
import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.interfaces.InventarioService;
import com.rasoftec.tpos2.menu_principal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InventarioActivity extends AppCompatActivity {
    private ListView lstInventario;
    private ProgressDialog pdialog;
    private InventarioAdapter inventarioAdapter;
    private ArrayList<inventario> listInventario = new ArrayList<inventario>();
    private JSONObject jsonObject;
    database base;
    JSONArray jsonArray = null;
    RestTemplate restTemplate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        base = new database(this);
        setContentView(R.layout.activity_list_inventario);
        lstInventario = findViewById(R.id.lstInvntario);
        getInvetario();
    }

    private void getInvetario() {
        try {
            pdialog = new ProgressDialog(InventarioActivity.this);
            pdialog.setMessage("Sincronizando ...");
            pdialog.show();
            restTemplate = new RestTemplate();
            String t14 = null;
            AsyncTask<String, String, String> t12 = new webservice(this);
            t14 = t12.execute(base.get_ruta().trim(), "Inventario").get();
            JSONArray response = new JSONArray(t14);
            for (int i = 0; i < response.length(); i++) {
                JSONObject item = (JSONObject) response.get(i);
                inventario inventarios = new inventario();
                inventarios.setArt_des(item.getString("art_des"));
                inventarios.setCo_alma(item.getString("co_alma"));
                inventarios.setCo_art(item.getString("co_art"));
               // double precio = Double.parseDouble();
                inventarios.setPrec_vta1(Double.parseDouble(item.getString("prec_vta1")));
                inventarios.setStock_act(Integer.parseInt(item.getString("stock_act")));
                listInventario.add(inventarios);
            }
            inventarioAdapter = new InventarioAdapter(getApplicationContext(), InventarioActivity.this, listInventario);
            lstInventario.setAdapter(inventarioAdapter);
            inventarioAdapter.notifyDataSetChanged();
            pdialog.dismiss();
        } catch (Exception t) {
            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            pdialog = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent changePrefs = new Intent(getApplicationContext(), menu_principal.class);
        startActivity(changePrefs);
        if (changePrefs.resolveActivity(getPackageManager()) != null) {
            startActivity(changePrefs);
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

}
