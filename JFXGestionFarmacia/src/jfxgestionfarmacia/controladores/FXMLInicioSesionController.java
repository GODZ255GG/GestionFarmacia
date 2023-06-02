package jfxgestionfarmacia.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxgestionfarmacia.modelo.DAO.SesionDAO;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;
import jfxgestionfarmacia.modelo.pojo.Empleado;

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;
    @FXML
    private PasswordField tfPassword;
    private String nombreEmpleado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        // Notificacion de clic
        lbErrorUsuario.setText("");
        lbErrorPassword.setText("");
        validarCampos();
    }
    
    // Validar campos
    private void validarCampos(){
        String usuario = tfUsuario.getText();
        String password = tfPassword.getText();
        boolean sonValidos = true;
        
        if(usuario.isEmpty()){
            sonValidos = false;
            lbErrorUsuario.setText("El campo usuario es obligatorio");
        }
        if(password.length() == 0){
            sonValidos = false;
            lbErrorPassword.setText("El campo contraseña es requerido");
        }
        if(sonValidos)
            validarCredencialesUsuario(usuario, password);
    }
    
    private void validarCredencialesUsuario(String usuario, String password){
        Empleado usuarioRespuesta = SesionDAO.verificarEmpleadoSesion(usuario, password);
        switch(usuarioRespuesta.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexión", 
                        "Por el momento no hay conexión, por favor inténtelo más tarde.", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error en la solicitud", 
                        "Por el momento no se puede procesar la solicitud de verificación.", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                if(usuarioRespuesta.getIdEmpleado() > 0){
                    Utilidades.mostrarDialogoSimple("Usuario verificado",
                            "Bienvenido(a) "+ usuarioRespuesta.toString()+" al sistema...", Alert.AlertType.INFORMATION);
                    irPantallaPrincipal();
                }else{
                    Utilidades.mostrarDialogoSimple("Credenciales incorrectas", 
                            "El usuario y/o contraseña no son correctos, por favor verifica la información", Alert.AlertType.WARNING);
                }
                break;
            default:
                    Utilidades.mostrarDialogoSimple("Erro de petición",
                            "El sistema no esta disponible por el momento", Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaPrincipal() {
        Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
        escenarioBase.setScene(Utilidades.inicializarEscena("vistas/FXMLMain.fxml"));
        escenarioBase.setTitle("Home");
        escenarioBase.show();
    }

    @FXML
    private void clicCerrarPrograma(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
