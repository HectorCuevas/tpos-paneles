package com.rasoftec.tpos2.Beans;

public class factura_encabezado {

    /*** datos cliente ***/
    private String usuarioMov;
    private String codCliente;
    private String forma_pag;
    private String  totalFact;
    private String procesado;
    private String cobrado;
    private String fecha;
    /*** cobrado  en la factura ***/
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

    public String getUsuarioMov() {
        return usuarioMov;
    }

    public void setUsuarioMov(String usuarioMov) {
        this.usuarioMov = usuarioMov;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getForma_pag() {
        return forma_pag;
    }

    public void setForma_pag(String forma_pag) {
        this.forma_pag = forma_pag;
    }

    public String getTotalFact() {
        return totalFact;
    }

    public void setTotalFact(String total) {
        this.totalFact = total;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }

    public String getCobrado() {
        return cobrado;
    }

    public void setCobrado(String cobrado) {
        this.cobrado = cobrado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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


    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
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


    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    String cel;
    public factura_encabezado(){

    }

    //agregar dpi
    public factura_encabezado(
                              String totalFact,
                              String nombre,  String direccion, String depto,
                              String municipio, String zona) {

        this.totalFact = totalFact;
        this.nombre = nombre;

        this.direccion = direccion;
        this.depto = depto;
        this.municipio = municipio;

        this.zona = zona;

    }
}
