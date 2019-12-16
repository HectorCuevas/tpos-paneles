package com.rasoftec.tpos2.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.venta;

import java.io.ByteArrayOutputStream;

import static android.text.TextUtils.isEmpty;
import static com.rasoftec.ApplicationTpos.LLEVA_FOTO;
import static com.rasoftec.ApplicationTpos.newFactura_encabezado;

public class DetailActivity extends AppCompatActivity {
    EditText txtName, txtDpi, txtNit, txtCel, txtEmail;
    String str;
    private BottomSheetDialog mBottomSheetDialog;
    String strArray[];
    LocationManager locationManager;
    LocationListener locationListener;
    Double longitude = 0D;
    Double latitude = 0D;
    ImageView imagen1, imagen2;
    private RadioButton rbContado;
    private RadioButton rbCredito;
    private RadioGroup rgOpcionesPago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        txtName = (EditText) findViewById(R.id.txtName);
        txtDpi = (EditText) findViewById(R.id.txtDpi);
        txtNit = (EditText) findViewById(R.id.txtNit);
        txtCel = (EditText) findViewById(R.id.txtTel);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        rbContado = findViewById(R.id.radioContado);
        rbCredito = findViewById(R.id.radioCredito);
        rgOpcionesPago = (RadioGroup) findViewById(R.id.radioGroup_opciones_pago);

        imagen1 = new ImageView(this);
        imagen2 = new ImageView(this);



     //   Toast.makeText(this, String.valueOf(ApplicationTpos.codigos.size()), Toast.LENGTH_SHORT).show();

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
        newFactura_encabezado.setForma_pag("CONT");
        rgOpcionesPago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbContado.isChecked()){
                    newFactura_encabezado.setForma_pag("CONT");
                }else{
                    newFactura_encabezado.setForma_pag("CRED");
                }
              //  Toast.makeText(getApplicationContext(), newFactura_encabezado.getForma_pag(), Toast.LENGTH_SHORT).show();
            }
        });


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
            //Toast.makeText(getApplicationContext(),latitude.toString() , Toast.LENGTH_SHORT).show();
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

    public void changeActivity(View v) {
        String name = txtName.getText().toString();
        String dpi = txtDpi.getText().toString();
        String nit = txtNit.getText().toString();
        String cel = txtCel.getText().toString();
        String email = txtEmail.getText().toString();

        if (getCodigo() == 0) {

            imagen1.setImageResource(R.drawable.food);
            imagen2.setImageResource(R.drawable.food);
            if (isEmpty(name)) {

                newFactura_encabezado.setNombre("CF");
                newFactura_encabezado.setDpi(dpi);
                newFactura_encabezado.setNit(nit);
                newFactura_encabezado.setCel(cel);
                newFactura_encabezado.setEmail("t:" + cel + " e:" + email);


                newFactura_encabezado.setImagen(imageViewToByte(imagen1));
                newFactura_encabezado.setImagen2(imageViewToByte(imagen2));
                ApplicationTpos.codigos.clear();
                Intent changeActivity = new Intent(this, LocationActivity.class);
                startActivity(changeActivity);
            } else {
                newFactura_encabezado.setNombre(name);
                newFactura_encabezado.setDpi(dpi);
                newFactura_encabezado.setNit(nit);
                newFactura_encabezado.setCel(cel);
                newFactura_encabezado.setImagen(imageViewToByte(imagen1));
                newFactura_encabezado.setImagen2(imageViewToByte(imagen2));
                newFactura_encabezado.setEmail("t:" + cel + " e:" + email);
                Log.i("asdsa", newFactura_encabezado.getCel());
                ApplicationTpos.codigos.clear();
                Intent changeActivity = new Intent(this, LocationActivity.class);
                startActivity(changeActivity);
            }
            ApplicationTpos.LLEVA_FOTO = 0;

        } else {
            if ((isEmpty(name)) || (isEmpty(dpi)) || (isEmpty(cel))) {
                Toast.makeText(this, "Nombre, DPI y numero de telefono son requeridos", Toast.LENGTH_SHORT).show();
            } else {

                newFactura_encabezado.setNombre(name);
                newFactura_encabezado.setDpi(dpi);
                newFactura_encabezado.setNit(nit);
                newFactura_encabezado.setCel(cel);
                newFactura_encabezado.setEmail("t:" + cel + " e:" + email);
                ApplicationTpos.codigos.clear();
                LLEVA_FOTO = 1;
                Intent changeActivity = new Intent(this, PhotoActivity.class);
                startActivity(changeActivity);
                if (changeActivity.resolveActivity(getPackageManager()) != null) {
                    startActivity(changeActivity);
                }
            }
        }
    }


    /*** Public methods to clear text boxes.3 ***/

    public void clearName(View view) {
        txtName.getText().clear();
    }

    public void clearDpi(View view) {
        txtDpi.getText().clear();
    }

    public void clearNit(View view) {
        txtNit.getText().clear();
    }

    public void clearCel(View view) {
        txtCel.getText().clear();
    }

    public void clearEmail(View view) {
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

    private int getCodigo() {
        int codigoDetalles = 0;
        for (int i = 0; i < ApplicationTpos.codigos.size(); i++) {
            if (ApplicationTpos.codigos.get(i).equals("S2")) {
                codigoDetalles++;
            }
        }
        return codigoDetalles;
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        // bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}









