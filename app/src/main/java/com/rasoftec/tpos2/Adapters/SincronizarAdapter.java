package com.rasoftec.tpos2.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.Beans.FormatoFactura;

import org.jetbrains.annotations.Nullable;

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
       /* View view = super.getView(position,convertView,parent);
        if(position %2 == 1)
        {
            // Set a background color for ListView regular row/item
            view.setBackgroundColor(Color.parseColor("#FFB6B546"));
        }
        else
        {
            // Set the background color for alternate row/item
            view.setBackgroundColor(Color.parseColor("#FFCCCB4C"));
        }*/
        try {
            FormatoFactura currentFormato =getItem(position);

            TextView lblNombreCliente = listItemView.findViewById(R.id.lblNombreClienteSinc);
            TextView lblDpi = listItemView.findViewById(R.id.lblDpiSinc);
            TextView lblDepartamento = listItemView.findViewById(R.id.lblDeptoSinc);
            TextView lblZona = listItemView.findViewById(R.id.lblZonaSinc);
            TextView lblTotal = listItemView.findViewById(R.id.lblTotalSinc);

            lblNombreCliente.setText("Nombre cliente: "+currentFormato.getNombre());
            lblDpi.setText("Venta: "+currentFormato.getCodigoArticulo());
            lblDepartamento.setText("Ubicacion: "+currentFormato.getDepto()+", "+ currentFormato.getMunicipio() +", zona "+ currentFormato.getZona());
            lblZona.setText("Telefono: "+ currentFormato.getNumeroCel());
            lblTotal.setText("Total articulo: "+currentFormato.getTotalFactura().toString());


        } catch (Exception ex) {
            //Toast.makeText()
            Log.i("Error", ex.toString());
        }
        return listItemView;
    }
}

