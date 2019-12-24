package com.rasoftec;

import android.app.Application;

import java.util.ArrayList;

import com.rasoftec.tpos2.Beans.detalleFactura;
import com.rasoftec.tpos2.Beans.factura_encabezado;
import com.rasoftec.tpos2.nodo_producto;

public class ApplicationTpos extends Application {
    public static  ArrayList<String> params = new ArrayList<>();
    public static factura_encabezado newFactura_encabezado = new factura_encabezado();
    public static  ArrayList<factura_encabezado> p = new ArrayList<factura_encabezado>();
    public static double totalEncabezado;
    public static String codigoCliente;
    public static String usuarioMovilizandome;
    public static int codigoMovilizandome;
    public static int newCodeEnc;
    public static ArrayList<nodo_producto> detalleVenta = new ArrayList<>();
    public static ArrayList<detalleFactura> detalleFactura = new ArrayList<>();
    public static  factura_encabezado currentEncabezado = new factura_encabezado();
    public static String tipo_venta = "required";
    public static ArrayList<String> codigos = new ArrayList<>();
    public static int LLEVA_FOTO = 1;
    public static String URL="http://tarjetazo.eastus2.cloudapp.azure.com/Tpos/PROSISCO_REST/api/Lst_productos_de_la_ruta/C";

    /*** Facturas pendientes por enviar ***/

  //  public static List<factura_encabezado> p = new List<factura_encabezado>() {

}
