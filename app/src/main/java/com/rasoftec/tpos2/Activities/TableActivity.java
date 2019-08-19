package com.rasoftec.tpos2.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rasoftec.tpos2.R;

import de.codecrafters.tableview.SortState;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.providers.SortStateViewProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

public class TableActivity extends AppCompatActivity {
    private static final String[] TABLE_HEADERS = { "Ruta", "Vendedor", "Producto", "Cantidad", "Fecha XYZ" };
    private static final String[][] DATA_TO_SHOW = { { "0000", "ABC", "ABC", "10" },
            { "0001", "XYZ", "XYZ", "20", "20145" },
            { "0002", "XYZ", "XYZ", "20" },
            { "0003", "XYZ", "XYZ", "20" , "12345"},
            { "0004", "XYZ", "XYZ", "20" },
            { "0005", "XYZ", "XYZ", "20", "445" },
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);



        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);

        //Cantidad de columnas a utilizar
        tableView.setColumnCount(5);

        //Agrega los encabezados y el cuerpo de la tabla
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, DATA_TO_SHOW));

        //Color de las columnas
        int colorEvenRows = getResources().getColor(R.color.white);
        int colorOddRows = getResources().getColor(R.color.gray);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));

        //sortableTableView.setHeaderSortStateViewProvider(new MySortStateViewProvider());
    }
    public static class MySortStateViewProvider implements SortStateViewProvider {

        private static final int NO_IMAGE_RES = -1;

        @Override
        public int getSortStateViewResource(SortState state) {
            switch (state) {
                case SORTABLE:
                    return R.mipmap.ic_dark_sortable;
                case SORTED_ASC:
                    return R.mipmap.ic_dark_sorted_asc;
                case SORTED_DESC:
                    return R.mipmap.ic_dark_sorted_desc;
                default:
                    return NO_IMAGE_RES;
            }
        }
    }
}

