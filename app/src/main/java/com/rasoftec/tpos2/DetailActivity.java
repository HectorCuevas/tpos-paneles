package com.rasoftec.tpos2;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.factura_encabezado;
import com.rasoftec.ApplicationTpos;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.rasoftec.ApplicationTpos.newFactura_encabezado;
import static com.rasoftec.ApplicationTpos.p;

public class DetailActivity extends AppCompatActivity{
    EditText txtName, txtDpi, txtNit, txtCel, txtEmail;
    String str;
    private BottomSheetDialog mBottomSheetDialog;
    String strArray[];
    LocationManager locationManager;
    LocationListener locationListener;
    Double longitude = 0D;
    Double latitude = 0D;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        txtName = (EditText) findViewById(R.id.txtName);
        txtDpi = (EditText) findViewById(R.id.txtDpi);
        txtNit = (EditText) findViewById(R.id.txtNit);
        txtCel = (EditText) findViewById(R.id.txtTel);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        View bottomSheet = findViewById(R.id.framelayout_bottom_sheet);

            final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        (bottomSheetLayout.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ok button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        /*** Location manager ***/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }


       // mBottomSheetDialog = new BottomSheetDialog(this);
       // mBottomSheetDialog.setContentView(bottomSheetLayout);
       // mBottomSheetDialog.show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
    }

    public void updateLocationInfo(Location location) {
        try {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            newFactura_encabezado.setLatitude(latitude.toString());
            newFactura_encabezado.setLongitude(longitude.toString());
            //Toast.makeText(getApplicationContext(),latitude.toString() , Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Debe activar el GPS", Toast.LENGTH_SHORT).show();
        }
    }


    /*** Public method to Chance activity ***/

    public void changeActivity(View v){
        String name=txtName.getText().toString();
        String dpi = txtDpi.getText().toString();
        String nit = txtNit.getText().toString();
        String cel = txtCel.getText().toString();
       if((isEmpty(name)) || (isEmpty(dpi)) || (isEmpty(cel)) ){
            Toast.makeText(this, "Nombre, DPI y numero de telefono son requeridos", Toast.LENGTH_SHORT).show();
        }else{
        String email = txtEmail.getText().toString();
        newFactura_encabezado.setNombre(name);
        newFactura_encabezado.setDpi(dpi);
        newFactura_encabezado.setNit(nit);
        newFactura_encabezado.setCel(cel);
        newFactura_encabezado.setEmail("t:"+cel+" e:"+email);
       // ApplicationTpos.params.add(a);//last
        Intent changeActivity = new Intent(this, locationActivity.class);
        //changeActivity.putExtra("list", params);
        startActivity(changeActivity);
        if (changeActivity.resolveActivity(getPackageManager()) != null) {
            startActivity(changeActivity);
        }}
    }
    /*** Public methods to clear text boxes ***/

    public void clearName(View view){
        txtName.getText().clear();
    }

    public void clearDpi(View view){
        txtDpi.getText().clear();
    }

    public void clearNit(View view){
        txtNit.getText().clear();
    }

    public void clearCel(View view){
        txtCel.getText().clear();
    }

    public void clearEmail(View view){
        txtEmail.getText().clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent cambiarActividad = new Intent(getApplicationContext(), venta.class);
        startActivity(cambiarActividad);
        if (cambiarActividad.resolveActivity(getPackageManager()) != null) {
            startActivity(cambiarActividad);
        }
    }
}









