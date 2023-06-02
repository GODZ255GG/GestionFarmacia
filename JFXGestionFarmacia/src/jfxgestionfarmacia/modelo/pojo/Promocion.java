package jfxgestionfarmacia.modelo.pojo;

public class Promocion {
    private int idPromociones;
    private String nombre;
    private String descripcion;
    private float descuento;
    private String fechaInicio;
    private String fechaFin;
    private int idProducto;
    private String nombreProducto;
    private byte[] fotoPromocion;

    public Promocion() {
    }

    public Promocion(int idPromociones, String nombre, String descripcion, float descuento, String fechaInicio, String fechaFin, int idProducto, String nombreProducto, byte[] fotoPromocion) {
        this.idPromociones = idPromociones;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.fotoPromocion = fotoPromocion;
    }

    public int getIdPromociones() {
        return idPromociones;
    }

    public void setIdPromociones(int idPromociones) {
        this.idPromociones = idPromociones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public byte[] getFotoPromocion() {
        return fotoPromocion;
    }

    public void setFotoPromocion(byte[] fotoPromocion) {
        this.fotoPromocion = fotoPromocion;
    }
}
