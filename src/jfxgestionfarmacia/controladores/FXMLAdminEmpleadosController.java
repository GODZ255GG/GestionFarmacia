package jfxgestionfarmacia.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxgestionfarmacia.JFXGestionFarmacia;
import jfxgestionfarmacia.modelo.DAO.EmpleadoDAO;
import jfxgestionfarmacia.modelo.pojo.Empleado;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionEmpleado;

public class FXMLAdminEmpleadosController implements Initializable, INotificacionOperacionEmpleado {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcApellidoPaterno;
    @FXML
    private TableColumn tcApellidoMaterno;
    @FXML
    private TableColumn tcCorreoElectronico;
    @FXML
    private TableColumn tcUsername;
    @FXML
    private TableColumn tcPassword;
    @FXML
    private TableColumn tcTipoEmpleado;
    @FXML
    private TableColumn tcSede;
    
    private ObservableList<Empleado> empleados;
    @FXML
    private TableView<Empleado> tvEmpleados;
    @FXML
    private TableColumn tcTurno;
    @FXML
    private TableColumn tcHorario;

   @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTable();
        cargarInformacionTabla();
    }

    private void configurarTable(){
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        tcApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        tcCorreoElectronico.setCellValueFactory(new PropertyValueFactory("correoElectronico"));
        tcUsername.setCellValueFactory(new PropertyValueFactory("username"));
        tcPassword.setCellValueFactory(new PropertyValueFactory("password"));
        tcTipoEmpleado.setCellValueFactory(new PropertyValueFactory("tipoEmpleado"));
        tcSede.setCellValueFactory(new PropertyValueFactory("nombreSede"));
        tcTurno.setCellValueFactory(new PropertyValueFactory("Turno"));
        tcHorario.setCellValueFactory(new PropertyValueFactory("Horario"));
    }
    
    private void cargarInformacionTabla(){
        empleados = FXCollections.observableArrayList();
        Empleado respuestaBD = EmpleadoDAO.obtenerInformacionEmpleado();
        switch(respuestaBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Sin conexion", 
                        "Los sentimos por el momento no hay conexión para poder cargar la información", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error al cargar los datos", 
                        "Hubo un error al cargar la información por favor inténtelo más tarde", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    empleados.addAll(respuestaBD.getEmpleados());
                    tvEmpleados.setItems(empleados);
                    configurarBusquedaTabla();
                break;
        }
    }

    @FXML
    private void clicBtnRegistrar(ActionEvent event) {
        irFormulario(false, null);
    }

    @FXML
    private void clicBtnModificar(ActionEvent event) {
        Empleado empleadoSeleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
        if(empleadoSeleccionado != null){
            irFormulario(true,empleadoSeleccionado);
        }else{
            Utilidades.mostrarDialogoSimple("Seleciona un alumno", 
                    "Selecciona el registro en la tabla del alumno para su edición", 
                    Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void dobleClicConsultarEmpleado(MouseEvent event) {
        Empleado empleadoSeleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2){
            irConsulta(empleadoSeleccionado);
        }
    }

    private void irFormulario(boolean esEdicion, Empleado empleadoEdicion){
        try{
            FXMLLoader accesoControlador = new FXMLLoader
                    (JFXGestionFarmacia.class.getResource("vistas/FXMLFormularioEmpleado.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioEmpleadoController formulario = accesoControlador.getController();
            
            formulario.inicializarInformacionFormlario(esEdicion, empleadoEdicion, 
                    this);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene (vista));
            escenarioFormulario.setTitle("Formulario");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex){
            Logger.getLogger(FXMLAdminEmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void irConsulta(Empleado empleadoConsulta){
        try{
            FXMLLoader accesoControlador = new FXMLLoader
                    (JFXGestionFarmacia.class.getResource("vistas/FXMLConsultarEmpleado.fxml"));
            Parent vista = accesoControlador.load();
            FXMLConsultarEmpleadoController consulta = accesoControlador.getController();
            
            consulta.inicializarInformacionConsulta(empleadoConsulta);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene (vista));
            escenarioFormulario.setTitle("Consultar Empleado");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex){
            Logger.getLogger(FXMLConsultarEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void configurarBusquedaTabla(){
        if(empleados.size() >0){
            FilteredList<Empleado> filtradorEmpleado = new FilteredList<>(empleados, p-> true);
            tfBuscar.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtradorEmpleado.setPredicate(empleadoFiltro ->{
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        String lowerNewValue = newValue.toLowerCase();
                        if(empleadoFiltro.getNombre().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                        return false;
                    });
                }
            });
            SortedList<Empleado> sortedListaEmpleado = new SortedList<>(filtradorEmpleado);
            sortedListaEmpleado.comparatorProperty().bind(tvEmpleados.comparatorProperty());
            tvEmpleados.setItems(sortedListaEmpleado);
        }
    }
    
    @FXML
    private void clicBtnDarBaja(ActionEvent event) {
        Empleado empleadoSeleccionado = tvEmpleados.getSelectionModel().getSelectedItem();
        if(empleadoSeleccionado != null){
            boolean borrarRegistro = Utilidades.mostrarDialogoConfirmacion("Eliminar Registro", 
                    "¿Esta seguro de que deseas eliminar el registro del Empleado(a): "+empleadoSeleccionado.getNombre()+" "+empleadoSeleccionado.getApellidoPaterno()+" "+empleadoSeleccionado.getApellidoMaterno()+"?");
            if(borrarRegistro){
                int codigoRespuesta = EmpleadoDAO.eliminarEmpleado(empleadoSeleccionado.getIdEmpleado());
                switch(codigoRespuesta){
                    case Constantes.ERROR_CONEXION:
                            Utilidades.mostrarDialogoSimple("Error de conexión", 
                                    "El empleado no pudo ser eliminado debido a un error en su conexió", Alert.AlertType.ERROR);
                        break;
                    case Constantes.ERROR_CONSULTA:
                            Utilidades.mostrarDialogoSimple("Error al cargar los datos", 
                                    "La información del empleado no puede ser eliminado, por favor intente de nuevo", Alert.AlertType.WARNING);
                        break;
                    case Constantes.OPERACION_EXITOSA:
                            Utilidades.mostrarDialogoSimple("Empleado Eliminado", 
                                    "La información del empleado ya fue eliminada correctamente", Alert.AlertType.INFORMATION);
                            cargarInformacionTabla();
                        break;
                }
            }
        }else{
            Utilidades.mostrarDialogoSimple("Selecciona un empleado", 
                    "Para eliminar a un empleado debes seleccionarlo previamente de la tabla", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicRegresar(MouseEvent event) {
        Stage escearioPrincipal = (Stage) tfBuscar.getScene().getWindow();
        escearioPrincipal.close();
    }

    @Override
    public void notificarOperacionGuardarEmpleado(String nombreEmpleado) {
        Utilidades.mostrarDialogoSimple("Notificación", 
                "Empleado(a) "+nombreEmpleado + " guardado", Alert.AlertType.INFORMATION);
        cargarInformacionTabla();
    }

    @Override
    public void notificarOperacionActualizarEmpleado(String nombreEmpleado) {
        Utilidades.mostrarDialogoSimple("Notificación", 
                "Empleado(a) "+nombreEmpleado + " actualizado", Alert.AlertType.INFORMATION);
        cargarInformacionTabla();
    }
}
