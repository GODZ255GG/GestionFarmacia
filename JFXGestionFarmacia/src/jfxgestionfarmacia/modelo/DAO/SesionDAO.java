package jfxgestionfarmacia.modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jfxgestionfarmacia.modelo.ConexionBD;
import jfxgestionfarmacia.modelo.pojo.Empleado;
import jfxgestionfarmacia.utils.Constantes;

public class SesionDAO {
    
    public static Empleado verificarEmpleadoSesion(String usuario, String password){
        Empleado EmpleadoVerificado = new Empleado();
        Connection conexion = ConexionBD.abrirConexionBD();
        if(conexion !=null){
            try{
                String consulta = "SELECT * FROM Empleado WHERE username = ? AND password = ?";
                PreparedStatement prepararSentencia = conexion.prepareStatement(consulta);
                prepararSentencia.setString(1, usuario);
                prepararSentencia.setString(2, password);
                ResultSet resultado = prepararSentencia.executeQuery();
                EmpleadoVerificado.setCodigoRespuesta(Constantes.OPERACION_EXITOSA);
                if(resultado.next()){
                    EmpleadoVerificado.setIdEmpleado(resultado.getInt("idEmpleado"));
                    EmpleadoVerificado.setNombre(resultado.getString("nombre"));
                    EmpleadoVerificado.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    EmpleadoVerificado.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    EmpleadoVerificado.setUsername(resultado.getString("username"));
                    EmpleadoVerificado.setPassword(resultado.getString("password"));
                    EmpleadoVerificado.setTipoEmpleado(resultado.getString("tipoEmpleado"));
                }
                conexion.close();
            } catch (SQLException ex){
                EmpleadoVerificado.setCodigoRespuesta(Constantes.ERROR_CONSULTA);
            }
        }else{
            EmpleadoVerificado.setCodigoRespuesta(Constantes.ERROR_CONEXION);
        }
        return EmpleadoVerificado;
    }
}


