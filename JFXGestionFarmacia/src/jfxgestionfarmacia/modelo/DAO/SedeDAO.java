package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Sede;
import jfxgestionfarmacia.modelo.pojo.SedeRespuesta;
import jfxgestionfarmacia.utils.Constantes;

public class SedeDAO {
    
    public static SedeRespuesta obtenerInformacionSedes(){
        SedeRespuesta respuesta = new SedeRespuesta();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String consulta = "SELECT * FROM Sede";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Sede> sedeConsulta = new ArrayList();
                while(resultado.next()){
                    Sede sedes = new Sede();
                    sedes.setIdSede(resultado.getInt("idSede"));
                    sedes.setNombre(resultado.getString("nombre"));
                    sedes.setDireccion(resultado.getString("direccion"));
                    sedes.setCiudad(resultado.getString("ciudad"));
                    sedeConsulta.add(sedes);
                }
                respuesta.setSede(sedeConsulta);
                conexionBD.close();
            }catch (SQLException e){
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    } 
    
    private static int guardarSede(Sede sedeNuevo){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "INSERT INTO Sede (nombre, direccion, ciudad) "
                        + "VALUES (?, ?, ?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, sedeNuevo.getNombre());
                prepararSentencia.setString(2, sedeNuevo.getDireccion());
                prepararSentencia.setString(3, sedeNuevo.getCiudad());
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ? Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
            }catch(SQLException e){
                respuesta = Constantes.ERROR_CONSULTA;
            }
        }else{
            respuesta = Constantes.ERROR_CONEXION;
        }
        return respuesta;
    }
    
    private static int modificarSede(Sede sedeEdicion){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "UPDATE Sede "
                        + "Set nombre = ?, direccion = ?, ciudad = ? "
                        + "WHERE idSede = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareCall(sentencia);
                prepararSentencia.setString(1, sedeEdicion.getNombre());
                prepararSentencia.setString(2, sedeEdicion.getDireccion());
                prepararSentencia.setString(3, sedeEdicion.getCiudad());
                prepararSentencia.setInt(4, sedeEdicion.getIdSede());
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
    
    private static int elimianrSede(Sede sedeEliminar){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "DELECT FROM Sede WHERE idSede = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareCall(sentencia);
                prepararSentencia.setInt(1, sedeEliminar.getIdSede());
                int filasAfectadas = prepararSentencia.executeUpdate();
                respuesta = (filasAfectadas == 1) ?  Constantes.OPERACION_EXITOSA : Constantes.ERROR_CONSULTA;
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
