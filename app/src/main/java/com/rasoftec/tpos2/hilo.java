package com.rasoftec.tpos2;

import android.content.Context;
import android.util.Log;

import com.rasoftec.tpos2.Data.database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by raaa on 27/08/17.
 */

public class hilo extends Thread {
    Context actual;
    database base;
    public hilo(Context c){
        actual=c;
        base= new database(actual);
    }
    @Override
    public void run() {
        while (true) {

            try {


                prueba_movilizandome();
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void prueba_movilizandome() {
        String url="http://38.130.214.134/PROSISCO_REST/api/dt_sp_id_virtual_movilizandome";
        RestTemplate restTemplate = new RestTemplate();
        // Add the Jackson message converter
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HashMap tem= new HashMap();
        tem.put("ruta",base.get_ruta());
        int movilizandome_cod=0;
        JSONArray movilizandome = restTemplate.postForObject(url, tem, JSONArray.class);
        for(int t=0;t<movilizandome.length();t++){
            try {
                JSONObject t2=movilizandome.getJSONObject(t);
                movilizandome_cod=t2.getInt("id_movilizandome");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        Log.i("Revisar Hilo",""+movilizandome_cod);

    }


}
