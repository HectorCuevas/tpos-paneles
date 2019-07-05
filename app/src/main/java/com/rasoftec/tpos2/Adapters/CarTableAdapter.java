package com.rasoftec.tpos2.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.rasoftec.tpos2.Beans.Report;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class CarTableAdapter extends TableDataAdapter<Report> {
    public CarTableAdapter(Context context, List<Report> data) {
        super(context, data);
    }

   /* @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Report report = getRowData(rowIndex);
        View renderedView = null;
        return renderedView;
        switch (columnIndex) {
            case 0:
                renderedView = report.getNombre()//renderProducerLogo(report);
                break;
            case 1:
                renderedView = renderCatName(report);
                break;
            case 2:
                renderedView = renderPower(report);
                break;
            case 3:
                renderedView = renderPrice(report);
                break;
        }

        return renderedView;
    }*/

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        return null;
    }
}

