package com.rasoftec.tpos2.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.Beans.FormatoFactura;
import com.rasoftec.tpos2.Beans.factura_encabezado;

import java.util.ArrayList;

public class EncabezadoFacturaAdapter extends ArrayAdapter<factura_encabezado> {
    public EncabezadoFacturaAdapter(Activity context, ArrayList<factura_encabezado> listItems) {
        super(context, 0, listItems);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_enc_facturas, parent, false);
        }
        try{
            factura_encabezado current = getItem(position);
            TextView lblNombre = listItem.findViewById(R.id.lblNombreClienteFactura);
            TextView lblDireccion= listItem.findViewById(R.id.lblDireccionFactura);
            TextView lblDpi = listItem.findViewById(R.id.lblDpiFacturas);
            TextView lblTotalFact = listItem.findViewById(R.id.lblTotalFactura);

            lblNombre.setText("Nombre cliente: "+current.getNombre());
            lblDireccion.setText("Direccion cliente: "+current.getDepto()+", " + current.getMunicipio()+", zona " + current.getZona());
            lblDpi.setText("Dpi: "+current.getDpi());
            lblTotalFact.setText("Total factura: Q"+current.getTotalFact());
        }catch (Exception er){
            Log.i("Error", er.toString());
        }
        return listItem;
    }
}

