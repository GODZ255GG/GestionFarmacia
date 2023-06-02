package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Empleado;
import jfxgestionfarmacia.utils.Constantes;

public class EmpleadoDAO {
    public static Empleado obtenerInformacionEmpleado(){
        Empleado respuesta = new Empleado();
        respuesta.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String consulta = "SELECT  idEmpleado, Empleado.nombre, apellidoPaterno, apellidoMaterno, correoElectronico, username, password, " +
                        "tipoEmpleado, Empleado.Sede_idSede, Sede.nombre AS nombreSede, Empleado.Horario_idHorario, Horario.turno " +
                        "AS Turno, Horario.horario AS Horario,foto from Empleado " +
                        "INNER JOIN Sede ON Empleado.Sede_idSede = Sede.idSede " +
                        "INNER JOIN Horario ON Empleado.Horario_idHorario = Horario.idHorario;";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                ResultSet resultado = prepararSentencia.executeQuery();
                ArrayList<Empleado> empleadoConsulta = new ArrayList();
                while(resultado.next()){
                    Empleado empleado = new Empleado();
                    empleado.setIdEmpleado(resultado.getInt("idEmpleado"));
                    empleado.setNombre(resultado.getString("nombre"));
                    empleado.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    empleado.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    empleado.setCorreoElectronico(resultado.getString("correoElectronico"));
                    empleado.setUsername(resultado.getString("username"));
                    empleado.setPassword(resultado.getString("password"));
                    empleado.setTipoEmpleado(resultado.getString("tipoEmpleado"));
                    empleado.setIdSede(resultado.getInt("Sede_idSede"));
                    empleado.setNombreSede(resultado.getString("nombreSede"));
                    empleado.setIdHorario(resultado.getInt("Horario_idHorario"));
                    empleado.setTurno(resultado.getString("turno"));
                    empleado.setHorario(resultado.getString("horario"));
                    empleado.setFoto(resultado.getBytes("foto"));
                    empleadoConsulta.add(empleado);
                }
                respuesta.setEmpleados(empleadoConsulta);
                conexionBD.close();
            }catch (SQLException e){
                respuesta.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            respuesta.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return respuesta;
    }
    
    public static int guardarEmpleado(Empleado empleadoNuevo){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "INSERT INTO Empleado (nombre, apellidoPaterno, "
                        + "apellidoMaterno,correoElectronico, username, password, tipoEmpleado, Sede_idSede, Horario_idHorario,foto) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1,empleadoNuevo.getNombre());
                prepararSentencia.setString(2, empleadoNuevo.getApellidoPaterno());
                prepararSentencia.setString(3,empleadoNuevo.getApellidoPaterno());
                prepararSentencia.setString(4, empleadoNuevo.getCorreoElectronico());
                prepararSentencia.setString(5, empleadoNuevo.getUsername());
                prepararSentencia.setString(6, empleadoNuevo.getPassword());
                prepararSentencia.setString(7, empleadoNuevo.getTipoEmpleado());
                prepararSentencia.setInt(8, empleadoNuevo.getIdSede());
                prepararSentencia.setInt(9, empleadoNuevo.getIdHorario());
                prepararSentencia.setBytes(10, empleadoNuevo.getFoto());
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
    
    public static int modificarEmpleado(Empleado empleadoEdicion){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "UPDATE Empleado SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, correoElectronico = ?, username = ?, password = ?, " +
                        "tipoEmpleado = ?, Sede_idSede = ?, Horario_idHorario = ?,foto = ? WHERE idEmpleado = ?; ";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, empleadoEdicion.getNombre());
                prepararSentencia.setString(2, empleadoEdicion.getApellidoPaterno());
                prepararSentencia.setString(3, empleadoEdicion.getApellidoMaterno());
                prepararSentencia.setString(4, empleadoEdicion.getCorreoElectronico());
                prepararSentencia.setString(5, empleadoEdicion.getUsername());
                prepararSentencia.setString(6, empleadoEdicion.getPassword());
                prepararSentencia.setString(7, empleadoEdicion.getTipoEmpleado());
                prepararSentencia.setInt(8, empleadoEdicion.getIdSede());
                prepararSentencia.setInt(9, empleadoEdicion.getIdHorario());
                prepararSentencia.setBytes(10, empleadoEdicion.getFoto());
                prepararSentencia.setInt(11, empleadoEdicion.getIdEmpleado());
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
    
    public static int eliminarEmpleado(int idEmpleado){
        int respuesta;
        Connection conexionBD = ConexionBD.abrirConexionBD();
        if(conexionBD != null){
            try{
                String sentencia = "DELETE FROM Empleado WHERE idEmpleado = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idEmpleado);
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
}
