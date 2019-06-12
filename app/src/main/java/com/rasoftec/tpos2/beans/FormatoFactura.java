package com.rasoftec.tpos2.beans;

import android.widget.ImageView;

public class FormatoFactura {
    /*** Encabezado ***/
    private String numeroDpi;
    private String nombre;
    private String nit;
    private String dpi;
    private String direccion="";
    private String depto="";
    private String municipio="";
    private String zona="";
    private String email;
    private String latitude;
    private String longitude;
    /*** Detalle ***/
    private long idMovilizandome;
    private String usuarioMovilizandome;
    private String codigoArticulo;
    private Double precioArticulo;
    private int cantidad;
    private Double totalFactura;
    private int numeroCel;

    /*** Contructor ***/

    public FormatoFactura() {

    }
    public FormatoFactura(String numeroDpi, String nombre, String nit,
                          String direccion, String depto, String municipio, String zona,
                          String email, String latitude, String longitude, long idMovilizandome,
                          String usuarioMovilizandome, String codigoArticulo, Double precioArticulo,
                          int cantidad, Double totalFactura, int numeroCel) {
        this.numeroDpi = numeroDpi;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.depto = depto;
        this.municipio = municipio;
        this.zona = zona;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;

        this.idMovilizandome = idMovilizandome;
        this.usuarioMovilizandome = usuarioMovilizandome;
        this.codigoArticulo = codigoArticulo;
        this.precioArticulo = precioArticulo;
        this.cantidad = cantidad;
        this.totalFactura = totalFactura;
        this.numeroCel = numeroCel;
    }



    public String getNumeroDpi() {
        return numeroDpi;
    }

    public void setNumeroDpi(String numeroDpi) {
        this.numeroDpi = numeroDpi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getIdMovilizandome() {
        return idMovilizandome;
    }

    public void setIdMovilizandome(long idMovilizandome) {
        this.idMovilizandome = idMovilizandome;
    }

    public String getUsuarioMovilizandome() {
        return usuarioMovilizandome;
    }

    public void setUsuarioMovilizandome(String usuarioMovilizandome) {
        this.usuarioMovilizandome = usuarioMovilizandome;
    }

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public Double getPrecioArticulo() {
        return precioArticulo;
    }

    public void setPrecioArticulo(Double precioArticulo) {
        this.precioArticulo = precioArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(Double totalFactura) {
        this.totalFactura = totalFactura;
    }

    public int getNumeroCel() {
        return numeroCel;
    }

    public void setNumeroCel(int numeroCel) {
        this.numeroCel = numeroCel;
    }

}
