package jfxgestionfarmacia.controladores;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxgestionfarmacia.modelo.DAO.ProductoDAO;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.modelo.pojo.ProductoRespuesta;
import jfxgestionfarmacia.modelo.pojo.Promocion;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLDetallesPromocionController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbDetallesPromocion;
    @FXML
    private Label lbNombrePromocion;
    @FXML
    private ComboBox<Producto> cbNombreProducto;
    @FXML
    private Label lbPrecioProducto;
    @FXML
    private Label lbPrecioDescuento;
    @FXML
    private Label lbCantidadDescuento;
    @FXML
    private Label lbFechaInicioPromocion;
    @FXML
    private Label lbFechaFinPromocion;
    @FXML
    private TextArea lbDescripcionPromocion;
    @FXML
    private ImageView ivFotoPromocion;
    private ObservableList<Producto> productos;
    private Promocion promocionConsulta;
    private Producto productoConsulta;
    
    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cargarInformacionProductos();
       cbNombreProducto.valueProperty().addListener(new ChangeListener<Producto>() {
            @Override
            public void changed(ObservableValue<? extends Producto> observable, 
                    Producto oldValue, Producto newValue) {
                cargarInformacionPrecio();
            }
        });
       lbDescripcionPromocion.setEditable(false);
       cbNombreProducto.setDisable(true);
       cbNombreProducto.setStyle("-fx-opacity: 1; -fx-background-color: #ffffff;");
    }    
    
    public void inicializarConsultaPrecio(Producto productoConsulta){
        this.productoConsulta = productoConsulta;
        
        cargarInformacionPrecio();
    }
    
    private void cargarInformacionPrecio(){
        Producto producto = cbNombreProducto.getSelectionModel().getSelectedItem();
        lbPrecioProducto.setText(String.valueOf(producto.getPrecio()));
        lbPrecioDescuento.setText(String.valueOf(calcularDescuento(producto.getPrecio(), promocionConsulta.getDescuento())));
            
    }
    
    private float calcularDescuento(float precioProducto, float descuento) {
        float cantidadDescuento = precioProducto * (descuento / 100);
        float precioDescontado = precioProducto - cantidadDescuento;
        return precioDescontado;
    }
    
    public void inicializarInformacionConsulta(Promocion promocionConsulta){
        this.promocionConsulta = promocionConsulta;
        
        lbTitulo.setText("Consultar información de la promoción: "+promocionConsulta.getNombre());
        cargarInformacionConsulta();
    }
    
    public void cargarInformacionConsulta(){
        lbNombrePromocion.setText(promocionConsulta.getNombre());
        int posicionProducto = obtenerPosicionComboProducto(promocionConsulta.getIdProducto());
        cbNombreProducto.getSelectionModel().select(posicionProducto);
        lbCantidadDescuento.setText(String.valueOf(promocionConsulta.getDescuento()));
        
        lbFechaInicioPromocion.setText(promocionConsulta.getFechaInicio());
        lbFechaFinPromocion.setText(promocionConsulta.getFechaFin());
        lbDescripcionPromocion.setText(promocionConsulta.getDescripcion());
    
        try{
            ByteArrayInputStream inputFoto = new ByteArrayInputStream(promocionConsulta.getFotoPromocion());
            Image imgFotoEmpleado = new Image(inputFoto);
            ivFotoPromocion.setImage(imgFotoEmpleado);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void cargarInformacionProductos(){
        productos = FXCollections.observableArrayList();
        ProductoRespuesta productosBD = ProductoDAO.obtenerInformacionProductos();
        switch(productosBD.getCodigoRespuesta()){
            case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Error de conexión", 
                            "Error de conexion con la base de datos.", Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error de consulta", 
                            "Por el momento no se puede obtener la información.", Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                    productos.addAll(productosBD.getProductos());
                    cbNombreProducto.setItems(productos);
                break;
        }
    }
    
    private int obtenerPosicionComboProducto(int idProducto){
        for (int i = 0; i < productos.size(); i++) {
            if(productos.get(i).getIdProducto()== idProducto)
                return i;
        }
        return 0;
    }
    
    @FXML
    private void clicBtnCerrarVentana(MouseEvent event) {
        Stage escearioPrincipal = (Stage) lbDetallesPromocion.getScene().getWindow();
        escearioPrincipal.close();
    }
}
