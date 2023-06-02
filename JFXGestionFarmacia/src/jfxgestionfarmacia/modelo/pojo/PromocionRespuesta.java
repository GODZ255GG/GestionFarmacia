package jfxgestionfarmacia.modelo.pojo;

import java.util.ArrayList;

public class PromocionRespuesta {
    private int codigoRespuesta;
    private ArrayList<Promocion> promociones;

    public PromocionRespuesta() {
    }

    public PromocionRespuesta(int codigoRespuesta, ArrayList<Promocion> promociones) {
        this.codigoRespuesta = codigoRespuesta;
        this.promociones = promociones;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public ArrayList<Promocion> getPromociones() {
        return promociones;
    }

    public void setPromociones(ArrayList<Promocion> promociones) {
        this.promociones = promociones;
    }
}
