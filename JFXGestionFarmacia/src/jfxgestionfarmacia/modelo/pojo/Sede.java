package jfxgestionfarmacia.modelo.pojo;

public class Sede {
    
    private int idSede;
    private String nombre;
    private String direccion;
    private String ciudad;

    public Sede() {
    }

    public Sede(int idSede, String nombre, String direccion, String ciudad, int idInventarioProducto) {
        this.idSede = idSede;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public String toString(){
        return nombre;
    }
}
