package com.rasoftec.tpos2.data;

import android.media.Image;
import android.widget.ImageView;

public class factura_encabezado {
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

    public ImageView getDpiFrontal() {
        return dpiFrontal;
    }

    public void setDpiFrontal(ImageView dpiFrontal) {
        this.dpiFrontal = dpiFrontal;
    }

    public ImageView getDpiTrasero() {
        return dpiTrasero;
    }

    public void setDpiTrasero(ImageView dpiTrasero) {
        this.dpiTrasero = dpiTrasero;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    String numeroDpi;
    String nombre;
    String nit;
    String dpi;
    String direccion="";
    String depto="";
    String municipio="";
    String zona="";
    String email;
    ImageView dpiFrontal;
    ImageView dpiTrasero;
    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    String cel;
    public factura_encabezado(){

    }
    public factura_encabezado(String numeroDpi, String nombre, String nit, String direccion, String depto, String municipio, String zona, String email) {
        this.numeroDpi = numeroDpi;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.depto = depto;
        this.municipio = municipio;
        this.zona = zona;
        this.email = email;
    }
}
