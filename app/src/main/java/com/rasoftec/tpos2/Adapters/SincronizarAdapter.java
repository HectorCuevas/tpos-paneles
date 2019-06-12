package com.rasoftec.tpos2.Adapters;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.rasoftec.tpos2.beans.detalleFactura;

import java.util.ArrayList;

public class SincronizarAdapter extends ArrayAdapter<detalleFactura> {
    public SincronizarAdapter(Activity context, ArrayList<detalleFactura> listItems){
        super(context,0, listItems);

    }
}

