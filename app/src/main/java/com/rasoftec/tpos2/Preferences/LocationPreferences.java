package com.rasoftec.tpos2.Preferences;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.rasoftec.PreferenceManager;
import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.activities.LocationActivity;

public class LocationPreferences extends AppCompatActivity{

    Spinner spDeptosPref;
    Spinner spMunicipiosPref;
    Spinner spZonaPref;
    Button btnOkPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        btnOkPrefs = (Button) findViewById(R.id.btnConfigPrefs);

        spDeptosPref = (Spinner) findViewById(R.id.spnDeptoPref);
        spMunicipiosPref = (Spinner)findViewById(R.id.spMunicipioPref);
        spZonaPref = (Spinner) findViewById(R.id.spZonaPref);


        //Set adapter from resource
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.deptos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Set adapter to spinner
        spDeptosPref.setAdapter(adapter);

        //Verify if exist preference
        /*** Verificamos si existe la preferencia y la seteamos a los spinners ***/

        if (PreferenceManager.checkPref(LocationPreferences.this, PreferenceManager.PREF_ZONA)){
            int pos = Integer.parseInt(PreferenceManager.getPref(LocationPreferences.this, PreferenceManager.PREF_ZONA));
            spDeptosPref.setSelection(pos);
        }

        if (PreferenceManager.checkPref(LocationPreferences.this, PreferenceManager.PREF_STR_ZONA)){
            int pos = Integer.parseInt(PreferenceManager.getPref(LocationPreferences.this, PreferenceManager.PREF_STR_ZONA));
            spZonaPref.setSelection(pos);
        }


        /*** Asignamos a un spinner un listener para obetener la posicion y guadar la preferencia. ***/


        spDeptosPref.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferenceManager.setPref(LocationPreferences.this, PreferenceManager.PREF_ZONA, ""+position);

                String str = spDeptosPref.getSelectedItem().toString();
                PreferenceManager.setPref(LocationPreferences.this, PreferenceManager.PREF_STR_DEPTO, str);

                String spinnerName;
                if(position == 0){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosGuatemala , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 1){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosPeten , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 2 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosIzabal , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 3){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosAltaVerapaz , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 4 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosQuiche , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 5){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosHuehue , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 6 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosEscuintla , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 7 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosSanMarcos , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 8){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipioJutiapa , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 9){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosBajaVerapaz , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 10 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosSantaRosa , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 11){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosZacapa , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 12 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosSuchi , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }
                else if(position == 13){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosChiqui , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 14 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosJalapa , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 15){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosChimal , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 16 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosQuetzal , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 17 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosElProgreso , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 18){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosReu , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 19 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosSolola , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 20){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosToto , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }else if(position == 21 ){
                    final  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.municipiosSaca , android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMunicipiosPref.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMunicipiosPref.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = spMunicipiosPref.getSelectedItem().toString();
                PreferenceManager.setPref(LocationPreferences.this, PreferenceManager.PREF_STR_MUN, str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spZonaPref.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PreferenceManager.setPref(LocationPreferences.this, PreferenceManager.PREF_STR_ZONA, ""+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void fnOkey(View view){
        Intent cambiarActividad = new Intent(getApplicationContext(), LocationActivity.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent changeLocation = new Intent(getApplicationContext(), LocationActivity.class);
        startActivity(changeLocation);
        if (changeLocation.resolveActivity(getPackageManager()) != null) {
            startActivity(changeLocation);
        }
    }
}
