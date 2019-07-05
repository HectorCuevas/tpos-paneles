package com.rasoftec.tpos2.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.Beans.FormatoFactura;

import java.util.ArrayList;

public class SincronizarAdapter extends ArrayAdapter<FormatoFactura> {
    public SincronizarAdapter(Activity context, ArrayList<FormatoFactura> listItems) {
        super(context, 0, listItems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_sincronizar_item, parent, false);
        }
        try {
            FormatoFactura currentFormato =getItem(position);

            TextView lblNombreCliente = listItemView.findViewById(R.id.lblNombreClienteSinc);
            TextView lblDpi = listItemView.findViewById(R.id.lblDpiSinc);
            TextView lblDepartamento = listItemView.findViewById(R.id.lblDeptoSinc);
            TextView lblZona = listItemView.findViewById(R.id.lblZonaSinc);
            TextView lblTotal = listItemView.findViewById(R.id.lblTotalSinc);

            lblNombreCliente.setText("Nombre cliente: "+currentFormato.getNombre());
            lblDpi.setText("Venta: "+currentFormato.getCodigoArticulo());
            lblDepartamento.setText("Departamento :"+currentFormato.getDepto());
            lblZona.setText("Zona :"+ currentFormato.getZona());
            lblTotal.setText("Total articulo :"+currentFormato.getTotalFactura().toString());


        } catch (Exception ex) {
            //Toast.makeText()
            Log.i("Error", ex.toString());
        }
        return listItemView;
    }
}

