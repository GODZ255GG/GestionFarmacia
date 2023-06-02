package jfxgestionfarmacia.modelo.pojo;

public class Horario {
    private int idHorario;
    private String turno;
    private String horario;

    public Horario() {
    }

    public Horario(int idHorario, int idEmpleado, String turno, String horario) {
        this.idHorario = idHorario;
        this.turno = turno;
        this.horario = horario;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
    
    public String toString(){
        return turno;
    }
}
