package jfxgestionfarmacia.controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxgestionfarmacia.modelo.DAO.PedidoDAO;
import jfxgestionfarmacia.modelo.DAO.ProductoPedidoDAO;
import jfxgestionfarmacia.modelo.pojo.Pedido;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.modelo.pojo.ProductoPedido;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLInformacionReadquisicionController implements Initializable {

    @FXML
    private Label lbFechaPedido;
    @FXML
    private Label lbFechaLlegada;
    @FXML
    private Label lbDireccion;
    @FXML
    private Label lbTotal;
    @FXML
    private TableColumn tcProducto;
    @FXML
    private TableColumn tcCantidad;
    @FXML
    private TableColumn tcPrecio;
    @FXML
    private TableView<ProductoPedido> tvProductos;
    
    private ObservableList<Producto> productos;
    
    private Pedido pedidoConsulta;
    
    ObservableList<ProductoPedido> listaProductosPedidos;
    @FXML
    private Label lbRastreo;
    @FXML
    private ImageView ivImagenRastreo;
    
    private Image imagenPreparando;
    private Image imagenEnCamino;
    private Image imagenEntregado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productos = FXCollections.observableArrayList();
        configurarTabla();
    }
       
    public void inicializarInformacionConsulta(Pedido pedidoConsulta){
        this.pedidoConsulta = pedidoConsulta;
        cargarInformacionConsulta();
        cargarDatosTabla(pedidoConsulta.getIdPedido());
    }
    
    public void cargarInformacionConsulta(){
        lbFechaPedido.setText(pedidoConsulta.getFechaPedido());
        lbFechaLlegada.setText(pedidoConsulta.getFechaEnvio());
        lbDireccion.setText(pedidoConsulta.getDireccion());
        float total = pedidoConsulta.getTotal();
        String totalString = Float.toString(total);
        lbTotal.setText(totalString);
        
        String estadoSeguimiento = pedidoConsulta.getRastreo();          
        Image imagenEstado;
        switch (estadoSeguimiento) {
            case "Preparando":
                imagenEstado = imagenPreparando;
                break;
            case "En camino":
                imagenEstado = imagenEnCamino;
                break;
            case "Entregado":
                imagenEstado = imagenEntregado;
            default:
                imagenEstado = null; 
                break;
    }

    ivImagenRastreo.setImage(imagenEstado);
    }
    
    private void configurarTabla() {
        tcProducto.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        tcCantidad.setCellValueFactory(new PropertyValueFactory("cantidad"));
        tcPrecio.setCellValueFactory(new PropertyValueFactory("total"));
    }
        
    private void cargarDatosTabla(int idPedido){
        listaProductosPedidos = FXCollections.observableArrayList();
        ProductoPedido respuestaBD = ProductoPedidoDAO.obtenerProductosPedidos2(idPedido);
            switch (respuestaBD.getCodigoRespuesta()) {
                case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("SIN CONEXION",
                        "Lo sentimos, por el momento no hay conexión para poder cargar la información", Alert.AlertType.ERROR);
                break;
                case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("ERROR AL CARGAR LOS DATOS",
                        "Hubo un error al cargar la información. Por favor, inténtelo más tarde.", Alert.AlertType.WARNING);
                break;
                case Constantes.OPERACION_EXITOSA:
                    listaProductosPedidos.addAll(respuestaBD.getProductosPedidos());
                    tvProductos.setItems(listaProductosPedidos);
                    break;
    }
    }

    @FXML
    private void clickSalirConsulta(MouseEvent event) {
        Stage escearioPrincipal = (Stage) lbFechaPedido.getScene().getWindow();
        escearioPrincipal.close();
    }
}
