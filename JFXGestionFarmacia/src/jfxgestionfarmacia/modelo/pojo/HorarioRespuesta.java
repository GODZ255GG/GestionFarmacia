package jfxgestionfarmacia.modelo.pojo;

import java.util.ArrayList;

public class HorarioRespuesta {
    private int codigoRespuesta;
    private ArrayList<Horario> horario;

    public HorarioRespuesta() {
    }

    public HorarioRespuesta(int codigoRespuesta, ArrayList<Horario> horario) {
        this.codigoRespuesta = codigoRespuesta;
        this.horario = horario;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public ArrayList<Horario> getHorario() {
        return horario;
    }

    public void setHorario(ArrayList<Horario> horario) {
        this.horario = horario;
    }
}
