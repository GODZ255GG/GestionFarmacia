package jfxgestionfarmacia.controladores;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jfxgestionfarmacia.modelo.DAO.EmpleadoDAO;
import jfxgestionfarmacia.modelo.DAO.HorarioDAO;
import jfxgestionfarmacia.modelo.DAO.SedeDAO;
import jfxgestionfarmacia.modelo.pojo.Empleado;
import jfxgestionfarmacia.modelo.pojo.Horario;
import jfxgestionfarmacia.modelo.pojo.HorarioRespuesta;
import jfxgestionfarmacia.modelo.pojo.Sede;
import jfxgestionfarmacia.modelo.pojo.SedeRespuesta;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionEmpleado;

public class FXMLFormularioEmpleadoController implements Initializable {

    @FXML
    private ImageView ivImagenEmpleado;
    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCorreoElectronico;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private ComboBox<Sede> cbSede;
    private File archivoFoto;
    private boolean esEdicion;
    private Empleado empleadoEdicion;
    private INotificacionOperacionEmpleado interfazNotificacion;
    private ObservableList<Sede> sedes;
    @FXML
    private ComboBox<Horario> cbTurno;
    @FXML
    private TextField tfHorario;
    private ObservableList<Horario> horarios;
    String estiloError = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 2;";
    String estiloNormal = "-fx-border-width: 0;";
    @FXML
    private ComboBox<String> cbTipoEmpleado;
    private ObservableList<String> tipos;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarInformacionSede();
        cargarInformacionTurno();
        cargarInformacionTipoEmpleado();
        cbTurno.setOnAction(this::actualizarHorario);
    }    
    
    public void inicializarInformacionFormlario(boolean esEdicion, Empleado empleadoEdicion, INotificacionOperacionEmpleado interfazNotificacion){
        this.esEdicion = esEdicion;
        this.empleadoEdicion = empleadoEdicion;
        this.interfazNotificacion = interfazNotificacion;
        //TODO
        if(esEdicion){
            lbTitulo.setText("Editar información del empleado(a) "+empleadoEdicion.getNombre());
            cargarInformacionEdicion();
        }else{
            lbTitulo.setText("Registrar nuevo Empleado(a)");
            tfHorario.setEditable(false);
        }
    }
    
    public void cargarInformacionEdicion(){
        tfNombre.setText(empleadoEdicion.getNombre());
        tfApellidoPaterno.setText(empleadoEdicion.getApellidoPaterno());
        tfApellidoMaterno.setText(empleadoEdicion.getApellidoMaterno());
        tfCorreoElectronico.setText(empleadoEdicion.getCorreoElectronico());
        tfUsername.setText(empleadoEdicion.getUsername());
        tfPassword.setText(empleadoEdicion.getPassword());
        String tipoEmpleado = empleadoEdicion.getTipoEmpleado();
        cbTipoEmpleado.getSelectionModel().select(tipoEmpleado);
        int posicionSede = obtenerPosicionComboSede(empleadoEdicion.getIdSede());
        cbSede.getSelectionModel().select(posicionSede);
        int posicionTurno = obtenerPosicionComboTurno(empleadoEdicion.getIdHorario());
        cbTurno.getSelectionModel().select(posicionTurno);
        tfHorario.setText(empleadoEdicion.getHorario());
        tfHorario.setEditable(false);
        try{
            ByteArrayInputStream inputFoto = new ByteArrayInputStream(empleadoEdicion.getFoto());
            Image imgFotoEmpleado = new Image(inputFoto);
            ivImagenEmpleado.setImage(imgFotoEmpleado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void clicBtnRegistrarEmpleado(ActionEvent event) {
        validarCamposRegistro();
    }
    
    @FXML
    private void clicBtnCancelarRegistro(ActionEvent event) {
        cerrarVentana();
    }
    
    private void validarCamposRegistro(){
        establecerEstiloNormal();
        boolean datosValidados = true;
        
        String nombre = tfNombre.getText();
        String apellidoPaterno = tfApellidoPaterno.getText();
        String apellidoMaterno = tfApellidoMaterno.getText();
        String correoElectronico = tfCorreoElectronico.getText();
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        String tipoEmpleado = cbTipoEmpleado.getSelectionModel().getSelectedItem();
        int posicionSede = cbSede.getSelectionModel().getSelectedIndex();
        int posicionTurno = cbTurno.getSelectionModel().getSelectedIndex();
        String horario = tfHorario.getText();
        //TODO
        if(nombre.isEmpty()){
            tfNombre.setStyle(estiloError);
            datosValidados = false;
        }
        if(apellidoPaterno.isEmpty()){
            tfApellidoPaterno.setStyle(estiloError);
            datosValidados = false;
        }
        if(apellidoMaterno.isEmpty()){
            tfApellidoMaterno.setStyle(estiloError);
            datosValidados = false;
        }
        if(!Utilidades.correoValido(correoElectronico)){
            tfCorreoElectronico.setStyle(estiloError);
            datosValidados = false;
        }
        if(username.isEmpty()){
            tfUsername.setStyle(estiloError);
            datosValidados = false;
        }
        if(password.isEmpty()){
            tfPassword.setStyle(estiloError);
            datosValidados = false;
        }
        if(tipoEmpleado.isEmpty()){
            cbTipoEmpleado.setStyle(estiloError);
            datosValidados = false;
        }
        if(horario.isEmpty()){
            tfHorario.setStyle(estiloError);
            datosValidados = false;
        }
        if(posicionSede == -1){
            cbSede.setStyle(estiloError);
            datosValidados = false;
        }
        if(posicionTurno == -1){
            cbTurno.setStyle(estiloError);
            datosValidados = false;
        }
        
        Empleado empleadoValidado = new Empleado();
        empleadoValidado.setNombre(nombre);
        empleadoValidado.setApellidoPaterno(apellidoPaterno);
        empleadoValidado.setApellidoMaterno(apellidoMaterno);
        empleadoValidado.setCorreoElectronico(correoElectronico);
        empleadoValidado.setUsername(username);
        empleadoValidado.setPassword(password);
        empleadoValidado.setTipoEmpleado(tipoEmpleado);
        empleadoValidado.setIdSede(sedes.get(posicionSede).getIdSede());
        empleadoValidado.setIdHorario(horarios.get(posicionTurno).getIdHorario());
        empleadoValidado.setHorario(horario);
        
        try{
            if(esEdicion){
                if(archivoFoto != null || empleadoEdicion.getFoto().length > 0){
                    if(archivoFoto != null){
                    empleadoValidado.setFoto(Files.readAllBytes(archivoFoto.toPath()));
                }else{
                    empleadoValidado.setFoto(empleadoEdicion.getFoto());
                }
                empleadoValidado.setIdEmpleado((empleadoEdicion.getIdEmpleado()));
                actualizarEmplado(empleadoValidado);
                }else{
                    Utilidades.mostrarDialogoSimple("Selecciona una Foto para el empleado", 
                            "Para editar el registro del Emleado debes seleccionar su foto desde el boton de Seleccionar Foto", 
                            AlertType.WARNING);
                }   
            }else{
                if(archivoFoto != null){
                    empleadoValidado.setFoto(Files.readAllBytes(archivoFoto.toPath()));
                    registrarEmpleado(empleadoValidado);
                }else{
                    Utilidades.mostrarDialogoSimple("Seleccionae una foto para el empleado", 
                            "Para guardar el registro del Emleado debes seleccionar su foto desde el boton de Seleccionar Foto", 
                            AlertType.WARNING);
                }
            }
        }catch(IOException e){
            Utilidades.mostrarDialogoSimple("Error con el archivo", 
                    "Hubo un error al intentar guardar la imagen, vuelva a seleccinar el archivo", AlertType.ERROR);
        } 
    }
    
    private void registrarEmpleado(Empleado empleadoRegistro){
        int codigoRespuesta = EmpleadoDAO.guardarEmpleado(empleadoRegistro);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Error de conexión", 
                            "El empleado no puede ser guardado debido a un error en su conexión...", AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error en la información", 
                            "La información del empleado no puede ser guardada, por favor verifique su información", AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    Utilidades.mostrarDialogoSimple("Empleado Registrado", 
                            "La información del empleado fue guardada correctamente", AlertType.INFORMATION);
                    cerrarVentana();
                    interfazNotificacion.notificarOperacionGuardarEmpleado(empleadoRegistro.getNombre());
                break;
        }
    }
    
    private void actualizarEmplado(Empleado empleadoActualizar){
        int codigoRespuesta = EmpleadoDAO.modificarEmpleado(empleadoActualizar);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Error de conexión", 
                            "El empleado no puede ser modificar debido a un error en su conexión...", AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error en la información", 
                            "La información del empleado no puede ser modificar, por favor verifique su información", AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    Utilidades.mostrarDialogoSimple("Empleado Registrado", 
                            "La información del empleado fue modificada correctamente", AlertType.INFORMATION);
                    cerrarVentana();
                    interfazNotificacion.notificarOperacionActualizarEmpleado(empleadoActualizar.getNombre());
                break;
        }
    }
    
    private void cargarInformacionSede(){
        sedes = FXCollections.observableArrayList();
        SedeRespuesta sedesBD = SedeDAO.obtenerInformacionSedes();
        switch(sedesBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Error de Conexión", 
                            "Error de conexion con la base de datos.", AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error de Consulta", 
                            "Por el momento no se puede obtener la información.", AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    sedes.addAll(sedesBD.getSede());
                    cbSede.setItems(sedes); 
                break;
        }
    }
    
    private void cargarInformacionTipoEmpleado(){
        tipos = FXCollections.observableArrayList("Administrador", "Encargado");
        cbTipoEmpleado.setItems(tipos);
    }
    
    private void cargarInformacionTurno(){
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
    
    private void cerrarVentana(){
        Stage escenarioBase = (Stage) lbTitulo.getScene().getWindow();
        escenarioBase.close();
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
    
    @FXML
    private void clicSeleccionarImagen(ActionEvent event) {
        FileChooser dialogoSeleccionImg = new FileChooser();
        dialogoSeleccionImg.setTitle("Selecciona una imagen");
        FileChooser.ExtensionFilter filtroDialogo = new FileChooser.ExtensionFilter("Archivos PNG (*.png)", "*.PNG","Archivo JPG (*.jpg)","*.JPG");
        dialogoSeleccionImg.getExtensionFilters().add(filtroDialogo);
        Stage escenarioBase = (Stage) lbTitulo.getScene().getWindow();
        archivoFoto =  dialogoSeleccionImg.showOpenDialog(escenarioBase);
        
        if(archivoFoto != null){
            try{
                BufferedImage bufferImg = ImageIO.read(archivoFoto);
                Image imagenFoto = SwingFXUtils.toFXImage(bufferImg, null);
                ivImagenEmpleado.setImage(imagenFoto);
            }catch (Exception e){
                e.printStackTrace();
            }
            
        }
    }
    
    private void actualizarHorario(ActionEvent event){
        Horario horarioSeleccionado = cbTurno.getSelectionModel().getSelectedItem();
        if(horarioSeleccionado != null){
            String horario = horarioSeleccionado.getHorario();
            tfHorario.setText(horario);
        }
    }
    
    private void establecerEstiloNormal(){
        tfNombre.setStyle(estiloNormal);
        tfApellidoMaterno.setStyle(estiloNormal);
        tfApellidoPaterno.setStyle(estiloNormal);
        tfCorreoElectronico.setStyle(estiloNormal);
        tfUsername.setStyle(estiloNormal);
        tfPassword.setStyle(estiloNormal);
        cbTipoEmpleado.setStyle(estiloNormal);
        tfHorario.setStyle(estiloNormal);
        cbSede.setStyle(estiloNormal);
        cbTurno.setStyle(estiloNormal);
    }
}
