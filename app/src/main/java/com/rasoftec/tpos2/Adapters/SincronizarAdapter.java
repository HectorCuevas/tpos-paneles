package com.rasoftec.tpos2.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rasoftec.tpos.R;
import com.rasoftec.tpos2.beans.FormatoFactura;
import com.rasoftec.tpos2.beans.detalleFactura;

import java.util.ArrayList;

public class SincronizarAdapter extends ArrayAdapter<FormatoFactura> {
    public SincronizarAdapter(Activity context, ArrayList<FormatoFactura> listItems){
        super(context,0, listItems);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_sincronizar_item, parent, false);
        }

        FormatoFactura currentFormato = new FormatoFactura();

        TextView lblNombreCliente = listItemView.findViewById(R.id.lblNombreCliente);
        TextView lblDpi = listItemView.findViewById(R.id.lblDpi);
        TextView lblDepartamento = listItemView.findViewById(R.id.lblDeptoSinc);
        TextView lblZona =  listItemView.findViewById(R.id.lblZonaSinc);
        TextView lblTotal =  listItemView.findViewById(R.id.lblTotalSinc);

        lblNombreCliente.setText(currentFormato.getNombre());
        lblDpi.setText(currentFormato.getDpi());
        lblDepartamento.setText(currentFormato.getDepto());
        //lblZona.setText(currentFormato.getZona());
        //lblZona.setText(currentFormato.getZona());


        return super.getView(position, convertView, parent);
    }
}

