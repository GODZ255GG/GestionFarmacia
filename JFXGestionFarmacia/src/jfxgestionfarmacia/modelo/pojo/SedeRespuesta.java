package jfxgestionfarmacia.modelo.pojo;

import java.util.ArrayList;

public class SedeRespuesta {
    private int codigoRespuesta;
    private ArrayList<Sede> sede;

    public SedeRespuesta() {
    }

    public SedeRespuesta(int codigoRespuesta, ArrayList<Sede> sede) {
        this.codigoRespuesta = codigoRespuesta;
        this.sede = sede;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public ArrayList<Sede> getSede() {
        return sede;
    }

    public void setSede(ArrayList<Sede> sede) {
        this.sede = sede;
    }
}
