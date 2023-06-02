package jfxgestionfarmacia.modelo.pojo;

import java.util.ArrayList;

public class Proveedor {
    private int idProveedor;
    private String nombre;
    private String telefono;
    private String direccion;
    private int codigoRespuesta;
    private ArrayList<Proveedor> proveedores;

    public Proveedor() {
    }

    public Proveedor(int idProveedor, String nombre, String telefono, String direccion, int codigoRespuesta, ArrayList<Proveedor> proveedores) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.codigoRespuesta = codigoRespuesta;
        this.proveedores = proveedores;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public ArrayList<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public void setProveedores(ArrayList<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    
}
