package jfxgestionfarmacia.controladores;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxgestionfarmacia.modelo.DAO.HorarioDAO;
import jfxgestionfarmacia.modelo.DAO.SedeDAO;
import jfxgestionfarmacia.modelo.pojo.Empleado;
import jfxgestionfarmacia.modelo.pojo.Horario;
import jfxgestionfarmacia.modelo.pojo.HorarioRespuesta;
import jfxgestionfarmacia.modelo.pojo.Sede;
import jfxgestionfarmacia.modelo.pojo.SedeRespuesta;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLConsultarEmpleadoController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private ImageView ivImagenEmpleado;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfCorreoElectronico;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<Sede> cbSede;
    
    private Empleado empleadoConsulta;
    
    private ObservableList<Sede> sedes;
    @FXML
    private TextField tfHorario;
    @FXML
    private ComboBox<Horario> cbTurno;
    private ObservableList<Horario> horarios;
    @FXML
    private ComboBox<String> cbTipoEmpleado;
    private ObservableList<String> tipos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarInformacionSede();
        cargarInformacionHorario();
        cargarInformacionTipoEmpleado();
    }

    public void inicializarInformacionConsulta(Empleado empleadoConsulta){
        this.empleadoConsulta = empleadoConsulta;
        
        lbTitulo.setText("Consultar información del empleado(a) "+empleadoConsulta.getNombre());
        cargarInformacionConsulta();
    }
    
    public void cargarInformacionConsulta(){
        tfNombre.setText(empleadoConsulta.getNombre());
        tfNombre.setEditable(false);
        tfApellidoPaterno.setText(empleadoConsulta.getApellidoPaterno());
        tfApellidoPaterno.setEditable(false);
        tfApellidoMaterno.setText(empleadoConsulta.getApellidoMaterno());
        tfApellidoMaterno.setEditable(false);
        tfCorreoElectronico.setText(empleadoConsulta.getCorreoElectronico());
        tfCorreoElectronico.setEditable(false);
        tfUsername.setText(empleadoConsulta.getUsername());
        tfUsername.setEditable(false);
        tfPassword.setText(empleadoConsulta.getPassword());
        tfPassword.setEditable(false);
        String tipoEmpleado = empleadoConsulta.getTipoEmpleado();
        cbTipoEmpleado.getSelectionModel().select(tipoEmpleado);
        cbTipoEmpleado.setEditable(false);
        int posicionSede = obtenerPosicionComboSede(empleadoConsulta.getIdSede());
        cbSede.getSelectionModel().select(posicionSede);
        cbSede.setEditable(false);
        int posicionTurno = obtenerPosicionComboTurno(empleadoConsulta.getIdHorario());
        cbTurno.getSelectionModel().select(posicionTurno);
        cbTurno.setEditable(false);
        tfHorario.setText(empleadoConsulta.getHorario());
        tfHorario.setEditable(false);
        try{
            ByteArrayInputStream inputFoto = new ByteArrayInputStream(empleadoConsulta.getFoto());
            Image imgFotoEmpleado = new Image(inputFoto);
            ivImagenEmpleado.setImage(imgFotoEmpleado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private int obtenerPosicionComboSede(int idSede){
        for (int i = 0; i < sedes.size(); i++){
            if(sedes.get(i).getIdSede() == idSede)
                return i;
        }
        return 0;
    }
    
    private int obtenerPosicionComboTurno(int idTurno){
        for (int i = 0; i < sedes.size(); i++){
            if(sedes.get(i).getIdSede() == idTurno)
                return i;
        }
        return 0;
    }
    
    private void cargarInformacionSede(){
        sedes = FXCollections.observableArrayList();
        SedeRespuesta sedesBD = SedeDAO.obtenerInformacionSedes();
        switch(sedesBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Error de Conexión", 
                            "Error de conexion con la base de datos.", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error de Consulta", 
                            "Por el momento no se puede obtener la información.", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    sedes.addAll(sedesBD.getSede());
                    cbSede.setItems(sedes); 
                break;
        }
    }
    
    private void cargarInformacionHorario(){
        horarios = FXCollections.observableArrayList();
        HorarioRespuesta horarioBD = HorarioDAO.obtenerInformacionHorarios();
        switch(horarioBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Error de Conexión", 
                            "Error de conexion con la base de datos.", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error de Consulta", 
                            "Por el momento no se puede obtener la información.", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    horarios.addAll(horarioBD.getHorario());
                    cbTurno.setItems(horarios); 
                break;
        }
    }
    
    private void cargarInformacionTipoEmpleado(){
        tipos = FXCollections.observableArrayList("Administrador", "Encargado");
        cbTipoEmpleado.setItems(tipos);
    }
    
    @FXML
    private void clicRegresar(MouseEvent event) {
        Stage escearioPrincipal = (Stage) tfNombre.getScene().getWindow();
        escearioPrincipal.close();
    }
    
}
