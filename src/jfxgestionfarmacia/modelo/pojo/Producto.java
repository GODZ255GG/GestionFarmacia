/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jfxgestionfarmacia.modelo.pojo;

/**
 *
 * @author Alvaro
 */
public class Producto {
    private int idProducto;
    private String nombre;
    private String tipoProducto;
    private float precio;
    private String fechaCaducidad;
    private String esControlado;
    private int stock;
    private int cantidad;
    private float precioTotal;

    public Producto(int idProducto, String nombre, int cantidad, float precioTotal) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Producto() {
    }

    public Producto(int idProducto, String nombre, String tipoProducto, float precio, String fechaCaducidad, String esControlado, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.precio = precio;
        this.fechaCaducidad = fechaCaducidad;
        this.esControlado = esControlado;
        this.stock = stock;
    }
    
    public Producto(String productoSeleccionado, int cantidad, float precioTotal) {
        this.nombre = productoSeleccionado;
        this.cantidad=cantidad;
        this.precioTotal=precioTotal;
        
    }
    public Producto(String nombre, float precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
  
    //

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getEsControlado() {
        return esControlado;
    }

    public void setEsControlado(String esControlado) {
        this.esControlado = esControlado;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
     public String toString(){
        return nombre;
    }
}
