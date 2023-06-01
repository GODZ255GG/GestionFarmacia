
package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
               String consulta = "SELECT idProducto, nombre, tipoProducto, precio, fechaCaducidad, esControlado, stock, fotoProducto "+
                       "FROM Producto";
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
                producto.setFotoProducto(resultado.getBytes("fotoProducto"));
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
     public static int guardarProducto(Producto productoNuevo){
         int respuesta;
         Connection conexionBD = ConexionBD.abrirConexionBD();
         
         if(conexionBD != null){
             try{
                 String sentencia = "INSERT INTO Producto (nombre, tipoProducto, precio, fechaCaducidad, esControlado, stock, fotoProducto) "
                        +"VALUES(?, ?, ?, ?, ?, ?, ?)";
                 PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                 prepararSentencia.setString(1, productoNuevo.getNombre());
                 prepararSentencia.setString(2, productoNuevo.getTipoProducto());
                 prepararSentencia.setFloat(3, productoNuevo.getPrecio());
                 prepararSentencia.setString(4, productoNuevo.getFechaCaducidad());
                 prepararSentencia.setString(5, productoNuevo.getEsControlado());
                 prepararSentencia.setInt(6, productoNuevo.getStock());
                 prepararSentencia.setBytes(7,productoNuevo.getFotoProducto());
                 int filasAfectadas = prepararSentencia.executeUpdate();
                
                respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                conexionBD.close();
                 
             }catch(SQLException e){
                 respuesta = Constantes.ERROR_CONSULTA;
             }
         }else{
            respuesta = Constantes.ERROR_CONEXION;

         }
         return respuesta;
     }
     
     public static int modificarProducto(Producto productoEdicion){
         int respuesta;
         Connection conexionBD = ConexionBD.abrirConexionBD();         
         if(conexionBD != null){
             try{
                 String sentencia = "UPDATE producto "
                         +"SET nombre = ?, tipoProducto = ?, precio = ?, fechaCaducidad = ?, "
                         +"esControlado = ?, stock = ?, fotoProducto = ? "
                         +"WHERE idProducto = ?";
                 
                 PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                 prepararSentencia.setString(1,productoEdicion.getNombre());
                 prepararSentencia.setString(2, productoEdicion.getTipoProducto());
                 prepararSentencia.setFloat(3,productoEdicion.getPrecio());
                 prepararSentencia.setString(4, productoEdicion.getFechaCaducidad());
                 prepararSentencia.setString(5, productoEdicion.getEsControlado());
                 prepararSentencia.setInt(6, productoEdicion.getStock());
                 prepararSentencia.setBytes(7, productoEdicion.getFotoProducto());
                 prepararSentencia.setInt(8, productoEdicion.getIdProducto());
                
                 
                 int filasAfectadas = prepararSentencia.executeUpdate();
                 respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA :
                         Constantes.ERROR_CONSULTA;
                 conexionBD.close();

             }catch(SQLException e){
                 e.printStackTrace();
                respuesta = Constantes.ERROR_CONSULTA;
             }
         }else{
             respuesta = Constantes.ERROR_CONEXION;
         }     
            return respuesta;  
     }
     
     public static int eliminarProducto(int idProducto){
         int respuesta;
         Connection conexionBD = ConexionBD.abrirConexionBD();
         
         if(conexionBD !=null){
             try{
                 String sentencia = "DELETE FROM producto "
                         +"WHERE idProducto = ?";
                 PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                 prepararSentencia.setInt(1, idProducto);
                 
                 int filasAfectadas = prepararSentencia.executeUpdate();
                 respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : 
                         Constantes.ERROR_CONSULTA;
                 conexionBD.close();
             }catch(SQLException e){
                 respuesta = Constantes.ERROR_CONSULTA;
             }
         }else{
             respuesta = Constantes.ERROR_CONEXION;
         }
         return respuesta;
     }
}
