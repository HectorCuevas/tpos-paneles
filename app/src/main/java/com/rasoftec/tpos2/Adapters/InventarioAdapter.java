package com.rasoftec.tpos2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rasoftec.tpos2.Beans.inventario;
import com.rasoftec.tpos2.R;
import com.rasoftec.tpos2.nodo_producto;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class InventarioAdapter extends BaseAdapter {

    private Context context;
    private Activity activity;
    private List<inventario> lstProductos;
    private LayoutInflater inflater;
    private static DecimalFormat df2 = new DecimalFormat("#.###");

    public  InventarioAdapter(Context context, Activity activity, List<inventario> listItems){
        this.context = context;
        this.activity = activity;
        this.lstProductos = listItems;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView=convertView;
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (listItemView == null) {
            listItemView = inflater.inflate(R.layout.activity_list_inventario_item, null);
        }

        inventario producto = getItem(position);

        TextView lbltitulo = listItemView.findViewById(R.id.lblInvetarioTitulo);
        TextView lblDesc = listItemView.findViewById(R.id.lblInventarioDescripcion);
        TextView lblprecio = listItemView.findViewById(R.id.lblInventarioPrecio);
        TextView lblStock = listItemView.findViewById(R.id.lblInvetarioStock);

        lbltitulo.setText(producto.getCo_art());
        lblDesc.setText(producto.getArt_des());
        lblprecio.setText("Q" +String.valueOf( producto.getPrec_vta1()));
        int stock = (int)producto.getStock_act();
        lblStock.setText("Stock: " + stock);



        return listItemView;
    }

    @Override
    public int getCount() {
        return lstProductos.size();
    }

    @Override
    public inventario getItem(int position) {
        return lstProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
