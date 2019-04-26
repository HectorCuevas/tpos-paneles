package com.rasoftec.tpos2;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.data.database;
import com.rasoftec.tpos2.data.factura_encabezado;
import com.rasoftec.ApplicationTpos;

import java.util.ArrayList;
import java.util.List;

import static com.rasoftec.ApplicationTpos.newFactura_encabezado;
import static com.rasoftec.ApplicationTpos.p;

public class DetailActivity extends AppCompatActivity{
    EditText txtName, txtDpi, txtNit, txtCel, txtEmail;
    String str;
    private BottomSheetDialog mBottomSheetDialog;
    String strArray[];
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

      //  mBottomSheetDialog = new BottomSheetDialog(this);
        //mBottomSheetDialog.setContentView(bottomSheetLayout);
        //mBottomSheetDialog.show();

    }


    /*** Public method to Chance activity ***/

    public void changeActivity(View v){
      //  String dpi= txtDpi.getText().toString();
      /*  int i = dpi.length();
        if(dpi.length() <=  13){
            str = (String)dpi.substring(9, 13);
            strArray = dpi.split("");
            ApplicationTpos.locationCode = str;
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Dpi invalido", Toast.LENGTH_SHORT).show();
        }*/
       // Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        String name=txtName.getText().toString();
        String dpi = txtDpi.getText().toString();
        String nit = txtNit.getText().toString();
        String cel = txtCel.getText().toString();
        String email = txtEmail.getText().toString();
        newFactura_encabezado.setNombre(name);
        newFactura_encabezado.setDpi(dpi);
        newFactura_encabezado.setNit(nit);
        newFactura_encabezado.setCel(cel);
        newFactura_encabezado.setEmail(email);
       // ApplicationTpos.params.add(a);//last
        Intent changeActivity = new Intent(this, locationActivity.class);
        //changeActivity.putExtra("list", params);
        startActivity(changeActivity);
        if (changeActivity.resolveActivity(getPackageManager()) != null) {
            startActivity(changeActivity);
        }
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









