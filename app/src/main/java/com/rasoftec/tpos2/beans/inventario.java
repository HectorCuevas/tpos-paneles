package com.rasoftec.tpos2.Beans;

public class inventario {

    private String co_alma;
    private String co_art;
    private String art_des;

    public double getStock_act() {
        return stock_act;
    }

    public void setStock_act(double stock_act) {
        this.stock_act = stock_act;
    }

    private double stock_act;
    private double prec_vta1;


    public inventario(){}

    public String getCo_alma() {
        return co_alma;
    }

    public void setCo_alma(String co_alma) {
        this.co_alma = co_alma;
    }

    public String getCo_art() {
        return co_art;
    }

    public void setCo_art(String co_art) {
        this.co_art = co_art;
    }

    public String getArt_des() {
        return art_des;
    }

    public void setArt_des(String art_des) {
        this.art_des = art_des;
    }

    public double getPrec_vta1() {
        return prec_vta1;
    }

    public void setPrec_vta1(double prec_vta1) {
        this.prec_vta1 = prec_vta1;
    }


}
