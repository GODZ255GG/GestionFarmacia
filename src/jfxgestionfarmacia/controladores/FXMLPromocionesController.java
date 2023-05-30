package jfxgestionfarmacia.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxgestionfarmacia.JFXGestionFarmacia;
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionPromocion;
import jfxgestionfarmacia.modelo.DAO.PromocionDAO;
import jfxgestionfarmacia.modelo.pojo.Promocion;
import jfxgestionfarmacia.modelo.pojo.PromocionRespuesta;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLPromocionesController implements Initializable, INotificacionOperacionPromocion {

    @FXML
    private TableColumn colNombrePromo;
    @FXML
    private TableColumn colFechaIni;
    @FXML
    private TableColumn colFechaFin;
    @FXML
    private TableColumn colDescuento;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colNombreProdu;
    @FXML
    private TableView<Promocion> tvPromociones;
    private ObservableList<Promocion> promociones; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    }    
    
    private void configurarTabla(){
        colNombrePromo.setCellValueFactory(new PropertyValueFactory("nombre"));
        colFechaIni.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory("fechaFin"));
        colDescuento.setCellValueFactory(new PropertyValueFactory("descuento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colNombreProdu.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
    }
    
    private void cargarInformacionTabla(){
        promociones = FXCollections.observableArrayList();
        PromocionRespuesta respuestaBD = PromocionDAO.obtenerInformacionPromocion();
        switch(respuestaBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Sin conexión",
                            "Los sentimos por el momento no hay conexión para poder cargar la información", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error al cargar los datos", 
                            "Hubo un error al cargar la información por favor inténtelo más tarde", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    promociones.addAll(respuestaBD.getPromociones());
                    tvPromociones.setItems(promociones);
                break;
        }
    }
    
    @FXML
    private void clicCerrarVentana(MouseEvent event) {
        Stage escearioPrincipal = (Stage) tvPromociones.getScene().getWindow();
        escearioPrincipal.close();
    }

    @FXML
    private void clicBtnRegistrar(ActionEvent event) {
        irFormulario(false,null);
    }

    @FXML
    private void clicBtnModificar(ActionEvent event) {
        int posicion = tvPromociones.getSelectionModel().getSelectedIndex();
        if(posicion != -1){
            irFormulario(true, promociones.get(posicion));
            //tvAlumnos.getSelectionModel().getSelectedItem();
        }else{
            Utilidades.mostrarDialogoSimple("Seleciona una promocion", 
                    "Selecciona el registro en la tabla de la promocion para su edición", 
                    Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(boolean esEdicion, Promocion promocionEdicion){
        try{
            FXMLLoader accesoControlador = new FXMLLoader
                    (JFXGestionFarmacia.class.getResource("vistas/FXMLPromocionFormulario.fxml"));
            Parent vista = accesoControlador.load();
            FXMLPromocionFormularioController formulario = accesoControlador.getController();
            formulario.inicializarInformacionFormulario(esEdicion, promocionEdicion, this);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene (vista));
            escenarioFormulario.setTitle("Formulario");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex)
        {
            Logger.getLogger(FXMLPromocionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void clicBtnBaja(ActionEvent event) {
        int posicion = tvPromociones.getSelectionModel().getSelectedIndex();
        if(posicion != -1){
            boolean borrarRegistro = Utilidades.mostrarDialogoConfirmacion("Eliminar registro de alumno", 
                    "¿Estás seguro de que deseas eliminar el registro de la promocion: "
                            +promociones.get(posicion).getNombre());
            if(borrarRegistro){
                int codigoRespuesta = PromocionDAO.bajaPromocion(promociones.get(posicion).getIdPromociones());
                switch(codigoRespuesta){
                    case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Sin conexión",
                            "Los sentimos por el momento no hay conexión para poder cargar la información", 
                            Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error al cargar los datos", 
                            "Hubo un error al cargar la información por favor inténtelo más tarde", 
                            Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    Utilidades.mostrarDialogoSimple("Registro eliminado",
                            "Se ha eliminado exitosamente el registro",
                            Alert.AlertType.INFORMATION);
                    cargarInformacionTabla();
                break;
                }
            }
        }else{
            Utilidades.mostrarDialogoSimple("Selecciona una promoción", 
                    "Para eliminar una promoción debes seleccionarla previamente de la tabla", 
                    Alert.AlertType.WARNING);
        }
    }

    @Override
    public void notificarOperacionGuardarPromocion(String nombre) {
        cargarInformacionTabla();
        Utilidades.mostrarDialogoSimple("Notificación", 
                "Promocion "+nombre+" guardada", 
                Alert.AlertType.INFORMATION);
    }

    @Override
    public void notificarOperacionActualizarPromocion(String nombrePromo) {
        cargarInformacionTabla();
        Utilidades.mostrarDialogoSimple("Notificación", 
                "Promocion "+nombrePromo+" guardada", 
                Alert.AlertType.INFORMATION);
    }

    
    @FXML
    private void clicBtnDobleClicConsulta(MouseEvent event) {
        Promocion promocionSeleccionado = tvPromociones.getSelectionModel().getSelectedItem();
        if(event.getClickCount() == 2){
            irConsulta(promocionSeleccionado);
        }
    }
    
    private void irConsulta(Promocion promocionConsulta){
        try{
            FXMLLoader accesoControlador = new FXMLLoader
                    (JFXGestionFarmacia.class.getResource("vistas/FXMLDetallesPromocion.fxml"));
            Parent vista = accesoControlador.load();
            FXMLDetallesPromocionController consulta = accesoControlador.getController();
            
            consulta.inicializarInformacionConsulta(promocionConsulta);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene (vista));
            escenarioFormulario.setTitle("Consultar Promoción");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex){
            Logger.getLogger(FXMLDetallesPromocionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}