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
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionProducto;
import jfxgestionfarmacia.modelo.DAO.ProductoDAO;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.modelo.pojo.ProductoRespuesta;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;


public class FXMLAdminInventarioController implements Initializable, INotificacionOperacionProducto{

    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn colnombreProducto;
    @FXML
    private TableColumn colTipoProducto;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colFechaCaducidad;
    @FXML
    private TableColumn colControlado;
    @FXML
    private TableColumn colStock;
    @FXML
    private TextField tfBusqueda;
    private ObservableList<Producto> productos;
    private INotificacionOperacionProducto interfazNotificacionProducto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    }    

    private void configurarTabla(){
        colnombreProducto.setCellValueFactory(new PropertyValueFactory("nombre"));
        colTipoProducto.setCellValueFactory(new PropertyValueFactory("tipoProducto"));
        colPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
        colFechaCaducidad.setCellValueFactory(new PropertyValueFactory("fechaCaducidad"));
        colControlado.setCellValueFactory(new PropertyValueFactory("esControlado"));
        colStock.setCellValueFactory(new PropertyValueFactory("stock"));
    }
    
    private void cargarInformacionTabla(){
        productos = FXCollections.observableArrayList();
        
        ProductoRespuesta respuestaBD = ProductoDAO.obtenerInformacionProductos();
        
        switch(respuestaBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Sin conexion en cargarInformacionTabla",
                        "Lo sentimos, por el momento no hay conexion para poder cargar la informacion",
                        Alert.AlertType.ERROR);
                break;
            
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error al cargar los datos en cargarInformacionTabla",
                        "Hubo un error al cargar la informacion, por favor intentelo mas tarde",
                        Alert.AlertType.WARNING);
                break;
                
            case Constantes.OPERACION_EXITOSA:              
                    productos.addAll(respuestaBD.getProductos());
                    tvProductos.setItems(productos);
                    configurarBusquedaTabla();
                break;
        }
    }

    @FXML
    private void clickCerrarVentana(MouseEvent event) {
        Stage escenarioPrincipal = (Stage) tfBusqueda.getScene().getWindow();
        escenarioPrincipal.close();
    }

    @FXML
    private void clickBtnRegistrar(ActionEvent event) {
        irFormulario(false, null);
    }

    @FXML
    private void clickBtnEditar(ActionEvent event) {
        Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if(productoSeleccionado != null){
            irFormulario(true, productoSeleccionado);
        }else{
            Utilidades.mostrarDialogoSimple("Selecciona un producto",
                    "Selecciona el registro en la tabla del producto para su edicion",
                    Alert.AlertType.WARNING);
        }
    }

    private void irFormulario(boolean esEdicion, Producto productoEdicion){        

        try{
            FXMLLoader accesoControlador = new FXMLLoader(JFXGestionFarmacia.class.getResource("vistas/FXMLFormularioProducto.fxml"));
            Parent vista = accesoControlador.load();
            FXMLFormularioProductoController formulario = accesoControlador.getController();
            formulario.inicializarInformacionFormulario(esEdicion, productoEdicion, this);      
                        
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene(vista));
            escenarioFormulario.setTitle("Formulario");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();  
        }catch(IOException ex){
             Logger.getLogger(FXMLAdminInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void configurarBusquedaTabla(){
        if(productos.size() > 0){
            FilteredList<Producto> filtradoProducto = new FilteredList<>(productos, p -> true);
            tfBusqueda.textProperty().addListener(new ChangeListener<String>() {
                
                @Override
                public void changed(ObservableValue<? extends String> observable, 
                        String oldValue, String newValue) {
                   filtradoProducto.setPredicate(productoFiltro ->{
                       if(newValue == null || newValue.isEmpty()){
                           return true;
                       }
                       String lowerNewValue = newValue.toLowerCase();
                       if(productoFiltro.getNombre().toLowerCase().contains(lowerNewValue)){
                        return true;
                        }else if(productoFiltro.getTipoProducto().toLowerCase().contains(lowerNewValue)){
                            return true;
                        }
                       return false;
                   });
                }
            });
            
            SortedList<Producto> sortedListaProducto = new SortedList<>(filtradoProducto);
            sortedListaProducto.comparatorProperty().bind(tvProductos.comparatorProperty());
            tvProductos.setItems(sortedListaProducto);        
        }
    }
    
    @FXML
    private void clickBtnEliminar(ActionEvent event) {
        Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if(productoSeleccionado != null){
            boolean borrarRegistro = Utilidades.mostrarDialogoConfirmacion("Eliminar registro del producto",
                    "¿Estas seguro que deseas eliminar el registro del producto: "
                                +productoSeleccionado.getNombre()+ "?");
            
            if(borrarRegistro){
                int codigoRespuesta = ProductoDAO.eliminarProducto(productoSeleccionado.getIdProducto());
                switch(codigoRespuesta){                    
                    case Constantes.ERROR_CONEXION:
                        Utilidades.mostrarDialogoSimple("Error de conexión",
                        "El producto no pudo ser eliminado debido a un error en su conexión...",
                        Alert.AlertType.ERROR);
                    break;
                    case Constantes.ERROR_CONSULTA:
                        Utilidades.mostrarDialogoSimple("Error al eliminar",
                        "La información del producto no puede ser eliminada, por favor verifica sus datos nuevamente.",
                        Alert.AlertType.WARNING);
                      break;
                    case Constantes.OPERACION_EXITOSA:
                        Utilidades.mostrarDialogoSimple("Producto eliminado",
                        "La información del producto fue eliminado correctamente",
                        Alert.AlertType.INFORMATION);
                        cargarInformacionTabla();
                    break;
                }
                
            }
        }else{
            Utilidades.mostrarDialogoSimple("Selecciona un producto",
                    "Para eliminar un producto debes seleccionarlo previamente de la tabla",
                    Alert.AlertType.WARNING);
        }
    }
  
    @Override
    public void notificarOperacionGuardarProducto(String nombreProducto) {
        cargarInformacionTabla();
  
    }

    @Override
    public void notificarOperacionActualizarProducto(String nombreProducto) {
        cargarInformacionTabla();    
    }
}
