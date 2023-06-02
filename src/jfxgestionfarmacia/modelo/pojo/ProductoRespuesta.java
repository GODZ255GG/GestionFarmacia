package jfxgestionfarmacia.modelo.pojo;

import java.util.ArrayList;

/**
 *
 * @author Alvaro
 */
public class ProductoRespuesta {
    private int codigoRespuesta;
    private ArrayList<Producto>productos;

    public ProductoRespuesta() {
    }

    public ProductoRespuesta(int codigoRespuesta, ArrayList<Producto> productos) {
        this.codigoRespuesta = codigoRespuesta;
        this.productos = productos;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
    
}
