package jfxgestionfarmacia.modelo.pojo;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private String fechaPedido;
    private float total;
    private String rastreo;
    private String fechaEnvio;
    private String direccion;
    private int idProveedor;
    private String nombreProveedor;
    private int idSede;
    private String nombreSede;
    private int codigoRespuesta;
    private ArrayList<Pedido> pedidos;
    private List<Producto> productos;
    

    public Pedido() {
    }

    public Pedido(int idPedido, String fechaPedido, float total, String rastreo, String fechaEnvio, String direccion, int idProveedor, String nombreProveedor, int idSede, String nombreSede, int codigoRespuesta, ArrayList<Pedido> pedidos) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.total = total;
        this.rastreo = rastreo;
        this.fechaEnvio = fechaEnvio;
        this.direccion = direccion;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.idSede = idSede;
        this.nombreSede = nombreSede;
        this.codigoRespuesta = codigoRespuesta;
        this.pedidos = pedidos;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public float getTotal() {
        return total;
    }

    public String getRastreo() {
        return rastreo;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }
    
    public int getIdSede() {
        return idSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setRastreo(String rastreo) {
        this.rastreo = rastreo;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }
    
    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }
    
    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    
    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
