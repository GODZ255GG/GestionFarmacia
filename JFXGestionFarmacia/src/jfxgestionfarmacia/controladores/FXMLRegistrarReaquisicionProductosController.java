    package jfxgestionfarmacia.controladores;

import java.io.ByteArrayInputStream;
import jfxgestionfarmacia.modelo.DAO.ProductoDAO;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionPedido;
import jfxgestionfarmacia.modelo.DAO.PedidoDAO;
import jfxgestionfarmacia.modelo.DAO.ProveedorDAO;
import jfxgestionfarmacia.modelo.DAO.SedeDAO;
import jfxgestionfarmacia.modelo.pojo.Pedido;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.modelo.pojo.Proveedor;
import jfxgestionfarmacia.modelo.pojo.Sede;
import jfxgestionfarmacia.modelo.pojo.SedeRespuesta;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;


public class FXMLRegistrarReaquisicionProductosController implements Initializable, INotificacionOperacionPedido{

    @FXML
    private TableView<Producto> tvCompraProducto;
    @FXML
    private TableColumn tcProducto;
    @FXML
    private TableColumn tcCantidad;
    @FXML
    private TableColumn tcPrecio;
    @FXML
    private TextField tfIngresarCantidad;
    @FXML
    private Label lbCostoProducto;
    @FXML
    private Label lbCostoTotalPedido;
    @FXML
    private ComboBox<Proveedor> cbProveedor;
    @FXML
    private ComboBox<Sede> cbSede;
    @FXML
    private Label lbTitulo;
    @FXML
    private DatePicker dpFechaDeseada;
    @FXML
    private ComboBox<Producto> cbProducto;
    @FXML
    private Label lbTotal;
    
    ObservableList<String> listaSedes;
    private ObservableList<Producto> productosRegistrados = FXCollections.observableArrayList();
    private ObservableList<Proveedor> proveedores = FXCollections.observableArrayList();
    private ObservableList<Sede> sedes  = FXCollections.observableArrayList();
    private ObservableList<Producto> productosSeleccionados = FXCollections.observableArrayList();

    private Pedido pedidoEdicion;
    private Pedido pedidoRegistro;
    private boolean esEdicion;
    private INotificacionOperacionPedido interfazNotificacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarComboBoxProveedor();
        cargarComboBoxSede();
        cargarNombresProductos();
        configurarColumnas();
    }    
    
    private void cargarComboBoxSede() {
        SedeDAO sedesDAO = new SedeDAO();
        ArrayList<Sede> sedes = sedesDAO.obtenerIdsYnombresSedes();

        cbSede.setItems(FXCollections.observableArrayList(sedes));
    }
    
    private void cargarComboBoxProveedor() {
        ProveedorDAO proveedorDAO = new ProveedorDAO();
        List<Proveedor> proveedores = proveedorDAO.obtenerNombresProveedores();

        cbProveedor.setItems(FXCollections.observableArrayList(proveedores));
    }
    
    public void cargarNombresProductos() {
        ProductoDAO productoDAO = new ProductoDAO(); // Crear una instancia de ProductoDAO

        // Obtener la lista de productos con id, nombre y precio
        List<Producto> productos = productoDAO.obtenerIdNombrePrecioProducto();

        // Limpiar el ComboBox
        //cbProducto.getItems().clear();
        ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
        listaProductos.addAll(productos);
        cbProducto.setItems(listaProductos);
        // Agregar solo los nombres de los productos al ComboBox
        /*for (Producto producto : productos) {           
            cbProducto.setItems(FXCollections.observableList(productos));
            System.out.println("Prueba idProducto: "+producto.getIdProducto());
        }*/
        // Establecer el evento de selección en el ComboBox
        cbProducto.setOnAction(event -> {
            String productoSeleccionado = cbProducto.getValue().toString();
            float precio = obtenerPrecioProducto(productos, productoSeleccionado);
            lbTotal.setText(String.valueOf(precio));
        });
    }
    
    private void configurarColumnas() {
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
        tcPrecio.setCellFactory(column -> {
            TableCell<Producto, Float> cell = new TableCell<Producto, Float>() {
                private final DecimalFormat decimalFormat = new DecimalFormat("#.00");

                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(decimalFormat.format(item));
                    }
                }
            };
            return cell;
        });
    }

    private float obtenerPrecioProducto(List<Producto> productos, String nombreProducto) {
        for (Producto producto : productos) {
            if (producto.getNombre().equals(nombreProducto)) {
                return producto.getPrecio();
            }
        }
        return 0.0f;
    }
    
    
   @FXML
    private void btnAgregarProducto(ActionEvent event) {
        String productoSeleccionado = cbProducto.getValue().toString();
        float precio = Float.parseFloat(lbTotal.getText()); 
        int cantidad = Integer.parseInt(tfIngresarCantidad.getText());
        float precioTotal = precio * cantidad;
        int idProductoSeleccionado = cbProducto.getValue().getIdProducto();
        
        Producto producto = new Producto(idProductoSeleccionado, productoSeleccionado, cantidad, precioTotal);
        
        tvCompraProducto.getItems().add(producto);
        actualizarCostoTotal();
        
        productosRegistrados.add(producto);
    }


    @FXML
    private void btnEliminarProducto(ActionEvent event) {
        Producto productoSeleccionado = tvCompraProducto.getSelectionModel().getSelectedItem();

        if (productoSeleccionado != null) {

            tvCompraProducto.getItems().remove(productoSeleccionado);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Eliminar Producto");
            alert.setHeaderText(null);
            alert.setContentText("El producto ha sido eliminado correctamente.");
            alert.showAndWait();
            
            actualizarCostoTotal();
        } else {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Eliminar Producto");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un producto para eliminar.");
            alert.showAndWait();
        }
        productosRegistrados.remove(productoSeleccionado);
    }
    
    private void actualizarCostoTotal() {
        float costoTotal = 0.0f;

        for (Producto producto : tvCompraProducto.getItems()) {
            costoTotal += producto.getPrecioTotal();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String costoTotalFormateado = decimalFormat.format(costoTotal);

        lbCostoTotalPedido.setText(costoTotalFormateado);
        
    }
    
    
    @FXML
    private void btnRegistrarPedido(ActionEvent event) {
        if (tvCompraProducto.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("TABLA VACIA");
            alert.setHeaderText(null);
            alert.setContentText("La tabla de productos está vacía. Agrega al menos un producto antes de registrar el pedido.");
            alert.showAndWait();
        } else {
            validarCamposRegistro();
        }
    }

    @FXML
    private void btnCancelarPedido(ActionEvent event) {
        cerrarVentana();
    }
    
    public void inicializarInformacionFormulario(boolean esEdicion, Pedido pedidoRegistro, 
        INotificacionOperacionPedido interfazNotificacion){
            this.esEdicion = esEdicion;
            this.pedidoRegistro = pedidoRegistro;
            this.pedidoEdicion = pedidoRegistro;
            this.interfazNotificacion = interfazNotificacion;
            if(esEdicion){
                lbTitulo.setText("Modificar información del pedido");
                Pedido pedidoEdicion = PedidoDAO.getPedidoConProductos(pedidoRegistro.getIdPedido());
                this.pedidoEdicion = pedidoEdicion;
                // Asignar el resultado de la consulta de base de datos del pedido con sus productos
                // a la variable pedidoEdicion.
                // this.pedidoEdicion.
                cargarInformacionEdicion();
            }else{
                lbTitulo.setText("Registrar pedido");
            }
        }
    
    public void cargarInformacionEdicion(){       
        int posicionProveedor = obtenerPosicionComboProveedor(pedidoEdicion.getIdProveedor());
        cbProveedor.getSelectionModel().select(posicionProveedor);
        int posicionSede = obtenerPosicionComboSede(pedidoEdicion.getIdSede());
        cbSede.getSelectionModel().select(posicionSede);
        String fechaString = "2023-06-01"; 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        LocalDate fecha = LocalDate.parse(fechaString, formatter); 
        String fechaEnvio = pedidoEdicion.getFechaEnvio();
        dpFechaDeseada.setValue(fecha);
        List<Producto> listaProductos = pedidoEdicion.getProductos();
        ObservableList<Producto> observableListaProductos = FXCollections.observableArrayList(listaProductos);
        tvCompraProducto.setItems(observableListaProductos); 
    }
    
    private void validarCamposRegistro(){
        Proveedor proveedorSeleccionado = cbProveedor.getSelectionModel().getSelectedItem();
        Sede sedeSeleccionada = cbSede.getSelectionModel().getSelectedItem();
        LocalDate fechaSeleccionada = dpFechaDeseada.getValue();
        String totalPedido = lbCostoTotalPedido.getText();
    
        int idProveedor = proveedorSeleccionado.getIdProveedor();
        int idSede = sedeSeleccionada.getIdSede();
    
        if (proveedorSeleccionado == null || sedeSeleccionada == null || fechaSeleccionada == null) {
            Utilidades.mostrarDialogoSimple("Campos vacios", 
                        "Por favor seleccione los combos de sede, proveedor y elija una fecha", 
                        Alert.AlertType.WARNING);
        } else {
            LocalDate fechaActual = LocalDate.now();
            if (fechaSeleccionada.isBefore(fechaActual) || fechaSeleccionada.isEqual(fechaActual)) {
                Utilidades.mostrarDialogoSimple("Fecha incorrecta", 
                        "No es posible seleccionar una fecha anterior o una fecha actual para realizar el pedido, elija otra porfavor", 
                        Alert.AlertType.WARNING);
            } else {     
                
                List<Producto> productosRegistrados = new ArrayList<>(tvCompraProducto.getItems());

                Pedido pedidoValidado = new Pedido();
                pedidoValidado.setFechaEnvio(fechaSeleccionada.toString());
                pedidoValidado.setTotal(Float.parseFloat(totalPedido));
                pedidoValidado.setIdProveedor(idProveedor);
                pedidoValidado.setIdSede(idSede); 

                pedidoValidado.setProductos(productosRegistrados);

                if(esEdicion){
                    actualizarPedido(pedidoValidado);
                } else {
                    registrarPedido(pedidoValidado, productosRegistrados);
                }
            }
        }
    }
    
    private void registrarPedido(Pedido pedidoRegistro, List<Producto> productos){

        int codigoRespuesta = PedidoDAO.registrarPedido(pedidoRegistro, productosRegistrados);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexion xd", 
                        "La promocion no pudo ser guardado debido a un error en su conexión...", 
                        Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error en la información", 
                        "La información del pedido no puede ser guardada, por favor verifique su información", 
                        Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Pedido registrada", 
                        "La información del pedido ha sido guardada correctamente", 
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
                break;
        }
    }
    
    private void actualizarPedido(Pedido pedidoRegistro){
        int codigoRespuesta = PedidoDAO.modificarPedido(pedidoRegistro, productosRegistrados);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexion", 
                        "La promocion no pudo ser actualizado debido a un error en su conexión...", 
                        Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error en la información", 
                        "La información del pedido no puede ser actualizada, por favor verifique su información", 
                        Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Pedido registrado", 
                        "La información del pedido ha sido actualizada correctamente", 
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
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
    
    private int obtenerPosicionComboSede(int idSede){
        for (int i = 0; i < sedes.size(); i++){
            if(sedes.get(i).getIdSede() == idSede)
                return i;
        }
        return 0;
    }
    
    private int obtenerPosicionComboProveedor(int idProveedor){
        for (int i = 0; i < proveedores.size(); i++){
            if(proveedores.get(i).getIdProveedor()== idProveedor)
                return i;
        }
        return 0;
    }
    
    private void cerrarVentana(){
        Stage escenarioBase = (Stage) lbTitulo.getScene().getWindow();
        escenarioBase.close();
    }

    @Override
    public void notificarOperacionGuardar(String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notificarOperacionActualizar(String nombrePediProducto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
