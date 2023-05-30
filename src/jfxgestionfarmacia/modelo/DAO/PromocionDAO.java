package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Promocion;
import jfxgestionfarmacia.modelo.pojo.PromocionRespuesta;
import jfxgestionfarmacia.utils.Constantes;

public class PromocionDAO {
    public static PromocionRespuesta obtenerInformacionPromocion(){
        PromocionRespuesta respuesta = new PromocionRespuesta();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try {
                String consulta = "SELECT idPromociones, Promocion.nombre, fechaInicio, fechaFin, " + 
                    "descuento, descripcion, Promocion.Producto_idProducto, Producto.nombre AS nombreProducto, fotoPromocion " +
                    "FROM  Promocion " +
                    "INNER JOIN producto ON Promocion.Producto_idProducto = Producto.idProducto;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Promocion> promocionConsulta = new ArrayList();
                while(resultado.next()){
                    Promocion promocion = new Promocion();
                    promocion.setIdPromociones(resultado.getInt("idPromociones"));
                    promocion.setNombre(resultado.getString("nombre"));
                    promocion.setFechaInicio(resultado.getString("fechaInicio"));
                    promocion.setFechaFin(resultado.getString("fechaFin"));
                    promocion.setDescuento(resultado.getFloat("descuento"));
                    promocion.setDescripcion(resultado.getString("descripcion"));
                    promocion.setIdProducto(resultado.getInt("Producto_idProducto"));
                    promocion.setNombreProducto(resultado.getString("nombreProducto"));
                    promocion.setFotoPromocion(resultado.getBytes("fotoPromocion"));
                    promocionConsulta.add(promocion);
                }
                respuesta.setPromociones(promocionConsulta);
                conexionBD.close();
            } catch (SQLException e){
                e.printStackTrace();
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    }
    
    public static int guardarPromocion(Promocion promocionNueva){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD !=null){
            try{
                String sentencia = "INSERT INTO Promocion (nombre, descripcion, descuento, "
                        + "fechaInicio, fechaFin, fotoPromocion, Producto_idProducto) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, promocionNueva.getNombre());
                prepararSentencia.setString(2, promocionNueva.getDescripcion());
                prepararSentencia.setFloat(3, promocionNueva.getDescuento());
                prepararSentencia.setString(4, promocionNueva.getFechaInicio());
                prepararSentencia.setString(5, promocionNueva.getFechaFin());
                prepararSentencia.setBytes(6, promocionNueva.getFotoPromocion());
                prepararSentencia.setInt(7, promocionNueva.getIdProducto());
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
            }catch (SQLException e){
                respuesta = Constantes.ERROR_CONSULTA;
            }
        }else{
            respuesta = Constantes.ERROR_CONEXION;      
        }
        return respuesta;
    }
    
    public static int modificarPromocion(Promocion promocionEdicion){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD !=null){
            try{
                String sentencia = "UPDATE Promocion "
                        + "SET nombre = ?, descripcion = ?, descuento = ?, fechaInicio = ?, "
                        + "fechaFin = ?, Producto_IdProducto  = ?, fotoPromocion = ? "
                        + "WHERE idPromociones = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, promocionEdicion.getNombre());
                prepararSentencia.setString(2, promocionEdicion.getDescripcion());
                prepararSentencia.setFloat(3, promocionEdicion.getDescuento());
                prepararSentencia.setString(4, promocionEdicion.getFechaInicio());
                prepararSentencia.setString(5, promocionEdicion.getFechaFin());
                prepararSentencia.setInt(6, promocionEdicion.getIdProducto());
                prepararSentencia.setBytes(7, promocionEdicion.getFotoPromocion());
                prepararSentencia.setInt(8, promocionEdicion.getIdPromociones());
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                conexionBD.close();
            } catch (SQLException e){
                e.printStackTrace();
                respuesta = Constantes.ERROR_CONSULTA;
            }
        }else{
            respuesta = Constantes.ERROR_CONEXION;
        }
        return respuesta;
    }
    
    public static int bajaPromocion(int idPromociones){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD !=null){
            try {
                String sentencia = " DELETE FROM Promocion WHERE idPromociones = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idPromociones);
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
                conexionBD.close();
            } catch (SQLException e) {
                respuesta = Constantes.ERROR_CONSULTA;
            }
        }else{
            respuesta = Constantes.ERROR_CONEXION;
        }
        return respuesta;
    }
    
}
