package jfxgestionfarmacia.modelo.pojo;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class ProductoPedido {
    private int idProducto;
    private int idPedido;
    private int cantidad;
    private float total;
    private List<Producto> listaProductos;
    private ObservableList<Producto> productos;
    private Producto producto;
    private int codigoRespuesta;
    private ArrayList<ProductoPedido> productosPedidos;
    private String nombreProducto;

    public ProductoPedido() {
    }

    public ProductoPedido(int idProducto, int idPedido, int cantidad, float total, List<Producto> listaProductos, ObservableList<Producto> productos, Producto producto, int codigoRespuesta, ArrayList<ProductoPedido> productosPedidos, String nombreProducto) {
        this.idProducto = idProducto;
        this.idPedido = idPedido;
        this.cantidad = cantidad;
        this.total = total;
        this.listaProductos = listaProductos;
        this.productos = productos;
        this.producto = producto;
        this.codigoRespuesta = codigoRespuesta;
        this.productosPedidos = productosPedidos;
        this.nombreProducto = nombreProducto;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getTotal() {
        return total;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public ObservableList<Producto> getProductos() {
        return productos;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public ArrayList<ProductoPedido> getProductosPedidos() {
        return productosPedidos;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void setProductos(ObservableList<Producto> productos) {
        this.productos = productos;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public void setProductosPedidos(ArrayList<ProductoPedido> productosPedidos) {
        this.productosPedidos = productosPedidos;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}
