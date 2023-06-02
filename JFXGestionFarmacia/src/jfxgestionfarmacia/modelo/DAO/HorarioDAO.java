package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Horario;
import jfxgestionfarmacia.modelo.pojo.HorarioRespuesta;
import jfxgestionfarmacia.utils.Constantes;

public class HorarioDAO {
    public static HorarioRespuesta obtenerInformacionHorarios(){
        HorarioRespuesta respuesta = new HorarioRespuesta();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String consulta = "SELECT * FROM Horario";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Horario> horarioConsulta = new ArrayList();
                while(resultado.next()){
                    Horario horarios = new Horario();
                    horarios.setIdHorario(resultado.getInt("idHorario"));
                    horarios.setTurno(resultado.getString("turno"));
                    horarios.setHorario(resultado.getString("horario"));
                    horarioConsulta.add(horarios);
                }
                respuesta.setHorario(horarioConsulta);
                conexionBD.close();
            }catch(SQLException e){
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    }
    
    public static int guardarHorario(Horario horarioNuevo){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
               String sentencia = "INSERT INTO Horario (turno, horario) "+
                       "VALUES (?, ?)";
               PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
               prepararSentencia.setString(1, horarioNuevo.getTurno());
               prepararSentencia.setString(2, horarioNuevo.getHorario());
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
    
    public static int modificarHorario(Horario horarioEdicion){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
              String sentencia = "UPDATE Horario "
                      + "SET turno = ?, horario = ? "
                      + "WHERE idHorario = ?";
              PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
              prepararSentencia.setString(1, horarioEdicion.getTurno());
              prepararSentencia.setString(2, horarioEdicion.getHorario());
              prepararSentencia.setInt(3, horarioEdicion.getIdHorario());
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
    
    public static int eliminarHorario(Horario horarioEliminar){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "DELECT FROM Horario WHERE idHorario = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareCall(sentencia);
                prepararSentencia.setInt(1, horarioEliminar.getIdHorario());
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
