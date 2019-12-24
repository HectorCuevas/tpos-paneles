package com.rasoftec.tpos2.interfaces;

import com.rasoftec.tpos2.Beans.inventario;
import com.rasoftec.tpos2.nodo_producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InventarioService {
    @GET("Lst_productos_de_la_ruta/C{ruta}")
    Call<String> getInventario(
            @Path("ruta") String ruta
    );
}
