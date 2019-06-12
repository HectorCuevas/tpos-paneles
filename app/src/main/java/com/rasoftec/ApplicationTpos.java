package com.rasoftec;

import android.app.Application;

import java.util.ArrayList;

import com.rasoftec.tpos2.beans.detalleFactura;
import com.rasoftec.tpos2.beans.factura_encabezado;
import com.rasoftec.tpos2.nodo_producto;

public class ApplicationTpos extends Application {
    public static  ArrayList<String> params = new ArrayList<>();
    public static factura_encabezado newFactura_encabezado = new factura_encabezado();
    public static  ArrayList<factura_encabezado> p = new ArrayList<factura_encabezado>();
    public static byte[] byteArray;
    public static double totalEncabezado;
    public static String codigoCliente;
    public static String usuarioMovilizandome;
    public static int codigoMovilizandome;
    public static int newCodeEnc;
    public static ArrayList<nodo_producto> detalleVenta = new ArrayList<>();
    public static ArrayList<detalleFactura> detalleFactura = new ArrayList<>();
    public static String locationCode;
    public static int posMun = 0;
  //  public static List<factura_encabezado> p = new List<factura_encabezado>() {

}
