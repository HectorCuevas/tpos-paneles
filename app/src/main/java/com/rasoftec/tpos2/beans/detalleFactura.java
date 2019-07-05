package com.rasoftec.tpos2.Beans;

public class detalleFactura {

    public long getIdMovilizandome() {
        return idMovilizandome;
    }

    public void setIdMovilizandome(long idMovilizandome) {
        this.idMovilizandome = idMovilizandome;
    }

    /*** Atributes ***/
    private long idMovilizandome;
    private String usuarioMovilizandome;
    private String codigoArticulo;
    private Double precioArticulo;
    private int cantidad;
    private Double totalFactura;
    private int numeroCel;

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

    public void setPrecioArticulo(Double     precioArticulo) {
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

    public detalleFactura() {
    }



}
