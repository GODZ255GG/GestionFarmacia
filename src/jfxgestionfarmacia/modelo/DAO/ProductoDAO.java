package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.modelo.pojo.ProductoRespuesta;
import jfxgestionfarmacia.utils.Constantes;

public class ProductoDAO {
    public static ProductoRespuesta obtenerInformacionProductos(){
       ProductoRespuesta respuesta = new ProductoRespuesta();
       Connection conexionBD = ConexionBD.abrirConexionBD();
       respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
       
       if(conexionBD != null){
           try{
               String consulta = "SELECT idProducto, nombre, tipoProducto, precio, fechaCaducidad, esControlado, stock "
                       +"FROM Producto";
               PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
               ResultSet resultado = prepararSentencia.executeQuery();               
               ArrayList<Producto>productosConsulta = new ArrayList();
               
               while(resultado.next()){
                Producto producto = new Producto();   
                producto.setIdProducto(resultado.getInt("idProducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setTipoProducto(resultado.getString("tipoProducto"));
                producto.setPrecio(resultado.getFloat("precio"));
                producto.setFechaCaducidad(resultado.getString("fechaCaducidad"));
                producto.setEsControlado(resultado.getString("esControlado"));
                producto.setStock(resultado.getInt("stock"));
          
               productosConsulta.add(producto);           
               }               
             respuesta.setProductos(productosConsulta);
             conexionBD.close();
                     
           }catch(SQLException e){
               e.printStackTrace();
               respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
           }
       }else{
           respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
       }
        return respuesta;    
    }
    
    public List<Producto> obtenerIdNombrePrecioProductos() {
        List<Producto> productos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexionBD();
        
        if (conexionBD != null) {
            try {
                String consulta = "SELECT idProducto, nombre, precio FROM Producto";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                
                while (resultado.next()) {
                    int idProducto = resultado.getInt("idProducto");
                    String nombre = resultado.getString("nombre");
                    float precio = resultado.getFloat("precio");
                    System.out.println("idProducto: "+idProducto);
                    Producto producto = new Producto();
                    producto.setIdProducto(idProducto);
                    producto.setNombre(nombre);
                    producto.setPrecio(precio);
                    
                    productos.add(producto);
                }
                
                conexionBD.close();
            } catch (SQLException e) {
                // Manejo de excepciones
            }
        }
        
        return productos;
    }
  
}
