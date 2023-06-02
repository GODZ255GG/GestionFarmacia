package jfxgestionfarmacia.controladores;

import java.io.IOException;
import jfxgestionfarmacia.modelo.DAO.PedidoDAO;
import jfxgestionfarmacia.modelo.pojo.Pedido;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxgestionfarmacia.JFXGestionFarmacia;
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionPedido;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLAdministrarcionReadquisicionesController implements Initializable, INotificacionOperacionPedido {

    @FXML
    private TableView<Pedido> tvPedidos;
    @FXML
    private TableColumn tcFechaPedido;
    @FXML
    private TableColumn tcTotalPagar;
    @FXML
    private TableColumn tcRastreoPedido;
    @FXML
    private TableColumn tcFechaLlegada;
    @FXML
    private TableColumn tcDireccion;
    @FXML
    private TableColumn tcNombreProveedor;
    @FXML
    private TableColumn tcNombreSede;
    @FXML
    private TextField tfBusqueda;
    @FXML
    private ComboBox<String> cbBusqueda;
    
    ObservableList<Pedido> listaPedidos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosTabla(); 
        iniciarCbFiltroRecurso();

        
        cbBusqueda.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Fecha")) {
                tfBusqueda.setPromptText("Ingresa la fecha yyyy-mm-dd");
            } else if (newValue.equals("Sede")) {
                tfBusqueda.setPromptText("Ingresa el nombre de la sede");
            }
            actualizarTabla();
        });

        tfBusqueda.setOnKeyReleased(event -> {
            String selectedOption = cbBusqueda.getValue();
            String busqueda = tfBusqueda.getText().trim();
        
            if (event.getCode() == KeyCode.ENTER) {
                String parametroBusqueda = tfBusqueda.getText().trim();
                if (parametroBusqueda.isEmpty()) {
                    cargarDatosTabla();
                } else {
                    ObservableList<Pedido> resultadosBusqueda = FXCollections.observableArrayList();
                    for (Pedido pedido : listaPedidos) {
                        if (pedido.getFechaPedido().equals(parametroBusqueda)) {
                            resultadosBusqueda.add(pedido);
                        }
                        if (pedido.getNombreSede().equalsIgnoreCase(parametroBusqueda)) {
                            resultadosBusqueda.add(pedido);
                        }
                    }
                tvPedidos.setItems(resultadosBusqueda);
                }
            }
        });
    }
    
    private void actualizarTabla() {
        
        String parametroBusqueda = tfBusqueda.getText().trim();
        String tipoBusqueda = cbBusqueda.getValue();

        ObservableList<Pedido> pedidosFiltrados = listaPedidos.filtered(pedido -> {
            if (tipoBusqueda.equals("Fecha")) {
                String fechaPedido = pedido.getFechaPedido().toString();
                return fechaPedido.equals(parametroBusqueda);
            } else if (tipoBusqueda.equals("Sede")) {
                String nombreSede = pedido.getNombreSede();
                return nombreSede.equalsIgnoreCase(parametroBusqueda);
            }
            return false;
        });

        tvPedidos.setItems(pedidosFiltrados);
    }
    
    private void configurarTabla() {
        tcFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        tcTotalPagar.setCellValueFactory(new PropertyValueFactory<>("total"));
        tcRastreoPedido.setCellValueFactory(new PropertyValueFactory<>("rastreo"));
        tcFechaLlegada.setCellValueFactory(new PropertyValueFactory<>("fechaEnvio"));
        tcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        tcNombreProveedor.setCellValueFactory(new PropertyValueFactory<>("nombreProveedor"));
        tcNombreSede.setCellValueFactory(new PropertyValueFactory<>("nombreSede"));

        tvPedidos.setRowFactory(row -> new TableRow<Pedido>() {
            @Override
            protected void updateItem(Pedido pedido, boolean empty) {
                super.updateItem(pedido, empty);

                if (pedido == null || empty) {
                    setStyle(""); 
                } else {
                    String rastreo = pedido.getRastreo();
                    if ("Preparando".equalsIgnoreCase(rastreo)) {
                        setStyle("-fx-background-color: KHAKI;"); 
                    } else if ("En Camino".equalsIgnoreCase(rastreo)) {
                        setStyle("-fx-background-color: STEELBLUE;"); 
                    } else if ("Entregado".equalsIgnoreCase(rastreo)) {
                        setStyle("-fx-background-color: LAWNGREEN;"); 
                    } else {
                        setStyle(""); 
                    }
                }
            }
        });
    }

    
    private void cargarDatosTabla(){
        listaPedidos = FXCollections.observableArrayList();
        Pedido respuestaBD = PedidoDAO.obtenerPedidos();
        switch(respuestaBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("SIN CONEXION",
                            "Los sentimos por el momento no hay conexión para poder cargar la información", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("ERROR AL CARGAR LOS DATOS", 
                            "Hubo un error al cargar la información por favor inténtelo más tarde", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    listaPedidos.addAll(respuestaBD.getPedidos());
                    tvPedidos.setItems(listaPedidos);
                break;
        }
    }

    private void iniciarCbFiltroRecurso(){
        ObservableList<String> busquedas = FXCollections.observableArrayList();
        busquedas.add("Fecha");
        busquedas.add("Sede");
        cbBusqueda.setItems(busquedas);
    }
    
    @FXML
    private void btnCancelarPedido(ActionEvent event) {
        Pedido pedidoSeleccionado = tvPedidos.getSelectionModel().getSelectedItem();
    
        if (pedidoSeleccionado != null) {
            int pedidoId = pedidoSeleccionado.getIdPedido();

            PedidoDAO.cancelarEstadoPedido(pedidoId);

            mostrarMensaje("Pedido cancelado correctamente.","estado del pedido");
            refreshTable();
        } else {
            mostrarMensaje("Por favor, seleccione un pedido.","estado del pedido");
        }
    }

    @FXML
    private void btnModificarPedido(ActionEvent event) {
        int posicion = tvPedidos.getSelectionModel().getSelectedIndex();
        if (posicion != -1) {
            Pedido pedidoSeleccionado = listaPedidos.get(posicion);
            LocalDate fechaPedido = LocalDate.parse(pedidoSeleccionado.getFechaPedido(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate fechaActual = LocalDate.now();

            if (fechaPedido.isEqual(fechaActual)) {
                irFormulario(true, listaPedidos.get(posicion));
            } else {
                Utilidades.mostrarDialogoSimple("MODIFICACION NO DISPONIBLE",
                    "El pedido ya no está en estatus de preparación por lo que ya no es posible modificarlo",
                    Alert.AlertType.WARNING);
            }
        } else {
            Utilidades.mostrarDialogoSimple("SELECCIONA UN PEDIDO",
                "Selecciona el registro en la tabla del pedido para modificarlo",
                Alert.AlertType.WARNING);
        }
    } 
    

    @FXML
    private void btnConsultarPedido(ActionEvent event) throws IOException {        
        Pedido pedidoSeleccionado = obtenerPedidoSeleccionado();
        if (pedidoSeleccionado != null) {
            mostrarInformacionPedido(pedidoSeleccionado);
        } else {
            Utilidades.mostrarDialogoSimple("SELECCIONA UN PEDIDO",
                    "Selecciona el registro en la tabla del pedido para poder consultarla",
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void btnRegistrarPedido(ActionEvent event) {
        irFormulario(false,null);
    }
    
    @FXML
    private void clickSalir(MouseEvent event) {
        Stage escearioPrincipal = (Stage) tvPedidos.getScene().getWindow();
        escearioPrincipal.close();
    }
    
    
    private void irFormulario(boolean esEdicion, Pedido pedidoRegistro){
        try{
            FXMLLoader accesoControlador = new FXMLLoader
                    (JFXGestionFarmacia.class.getResource("vistas/FXMLRegistrarReaquisicionProductos.fxml"));
            Parent vista = accesoControlador.load();                       
            FXMLRegistrarReaquisicionProductosController formulario = accesoControlador.getController();

            formulario.inicializarInformacionFormulario(esEdicion, pedidoRegistro, this);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene (vista));
            escenarioFormulario.setTitle("Formulario");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLPromocionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Pedido obtenerPedidoSeleccionado() {
        return tvPedidos.getSelectionModel().getSelectedItem();
    }
    
    private void mostrarInformacionPedido(Pedido pedidoConsulta) throws IOException {
        try{
            FXMLLoader accesoControlador = new FXMLLoader
                    (JFXGestionFarmacia.class.getResource("vistas/FXMLInformacionReadquisicion.fxml"));
            Parent vista = accesoControlador.load();
            FXMLInformacionReadquisicionController consulta = accesoControlador.getController();
            
            consulta.inicializarInformacionConsulta(pedidoConsulta);
            
            Stage escenarioFormulario = new Stage();
            escenarioFormulario.setScene(new Scene (vista));
            escenarioFormulario.setTitle("Consultar Pedido");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex){
            Logger.getLogger(FXMLInformacionReadquisicionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    
    @Override
    public void notificarOperacionGuardar(String nombre) {
        cargarDatosTabla();
        Utilidades.mostrarDialogoSimple("Notificación", 
                "Pedido guardado", 
                Alert.AlertType.INFORMATION);
    }

    @Override
    public void notificarOperacionActualizar(String nombrePediProducto) {
        cargarDatosTabla();
        Utilidades.mostrarDialogoSimple("Notificación", 
                "Pedido guardado", 
                Alert.AlertType.INFORMATION);
    }
    
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    private void refreshTable() {
        tvPedidos.getItems().clear();

        PedidoDAO pedidoDAO = new PedidoDAO();
        Pedido respuesta = pedidoDAO.obtenerPedidos();
        List<Pedido> pedidos = respuesta.getPedidos();

        tvPedidos.getItems().addAll(pedidos);
    }
    
    
}