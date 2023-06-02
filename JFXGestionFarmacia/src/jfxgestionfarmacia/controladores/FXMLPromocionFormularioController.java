package jfxgestionfarmacia.controladores;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionPromocion;
import jfxgestionfarmacia.modelo.DAO.ProductoDAO;
import jfxgestionfarmacia.modelo.DAO.PromocionDAO;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.modelo.pojo.ProductoRespuesta;
import jfxgestionfarmacia.modelo.pojo.Promocion;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLPromocionFormularioController implements Initializable {

    @FXML
    private TextField tfNombrePromo;
    @FXML
    private ComboBox<Producto> cbProducto;
    
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;
    
    @FXML
    private TextArea tfDescripcion;
    @FXML
    private TextField tfDescuento;
    @FXML
    private ImageView ivPromocion;
    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbPrecioProducto;
    @FXML
    private Label lbTituloPrecioProducto;
    @FXML
    private Label lbPrecioDescuento;
    @FXML
    private Label lbTituloPrecioDescuento;
    @FXML
    private Button btnAccionFormulario;
    private ObservableList<Producto> productos;
    private Producto precioConsulta;
    private Promocion promocionEdicion;
    private boolean esEdicion;
    private INotificacionOperacionPromocion interfazNotificacion;
    private File archivoSeleccionado;
    String estiloError = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 2;";
    String estiloNormal = "-fx-border-width: 0";
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarInformacionProductos();
        cbProducto.valueProperty().addListener(new ChangeListener<Producto>() {
            @Override
            public void changed(ObservableValue<? extends Producto> observable, 
                    Producto oldValue, Producto newValue) {
                cargarInformacionPrecio();
            }
        });
        dpFechaInicio.setEditable(false);
        dpFechaFin.setEditable(false);
    }    

    public void inicializarInformacionFormulario(boolean esEdicion, Promocion promocionEdicion, 
            INotificacionOperacionPromocion interfazNotificacion){
        this.esEdicion = esEdicion;
        this.promocionEdicion = promocionEdicion;
        this.interfazNotificacion = interfazNotificacion;
        
        if(esEdicion){
            lbTitulo.setText("Editar información de la promocion "+promocionEdicion.getNombre());
            btnAccionFormulario.setText("Modificar promocion");
            cargarInformacionEdicion();
        }else{
            lbTitulo.setText("Registrar nueva promocion ");
            AnchorPane.setTopAnchor(lbPrecioProducto, 324.0);
            AnchorPane.setLeftAnchor(lbPrecioProducto, 117.0);
            AnchorPane.setTopAnchor(lbTituloPrecioProducto, 304.0);
            AnchorPane.setLeftAnchor(lbTituloPrecioProducto, 111.0);
            lbPrecioDescuento.setVisible(false);
            lbTituloPrecioDescuento.setVisible(false);
            
        }
    }
    
    public void inicializarConsultaPrecio(Producto precioConsulta){
        this.precioConsulta = precioConsulta;
        cargarInformacionPrecio();
    }
    
    private void cargarInformacionPrecio(){
        Producto producto = cbProducto.getSelectionModel().getSelectedItem();
        lbPrecioProducto.setText(String.valueOf(producto.getPrecio()));
            if (producto != null && promocionEdicion != null) {
                lbPrecioDescuento.setText(String.valueOf(calcularDescuento(producto.getPrecio(), promocionEdicion.getDescuento())));
            }
    }
    
    private float calcularDescuento(float precioProducto, float descuento) {
        float cantidadDescuento = precioProducto * (descuento / 100);
        float precioDescontado = precioProducto - cantidadDescuento;
        return precioDescontado;
    }
    
    private void cargarInformacionEdicion(){
        tfNombrePromo.setText(promocionEdicion.getNombre());
        int posicionProducto = obtenerPosicionComboProducto(promocionEdicion.getIdProducto());
        cbProducto.getSelectionModel().select(posicionProducto); //index
        dpFechaInicio.setValue(LocalDate.parse(promocionEdicion.getFechaInicio()));
        dpFechaFin.setValue(LocalDate.parse(promocionEdicion.getFechaFin()));
        tfDescuento.setText(String.valueOf(promocionEdicion.getDescuento()));
        tfDescripcion.setText(promocionEdicion.getDescripcion());
        
        try {
            ByteArrayInputStream inputFoto = new ByteArrayInputStream(promocionEdicion.getFotoPromocion());
            Image imgFotoPromocion = new Image(inputFoto);
            ivPromocion.setImage(imgFotoPromocion);   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicBtnRegistrar(ActionEvent event) {
        validarCamposRegistro();
    }

    @FXML
    private void clicBtnCancelar(ActionEvent event) {
        cerrarVentana();
    }
    //
    private void establecerEstiloNormal(){
        tfNombrePromo.setStyle(estiloNormal);
        cbProducto.setStyle(estiloNormal);
        dpFechaInicio.setStyle(estiloNormal);
        dpFechaFin.setStyle(estiloNormal);
        tfDescuento.setStyle(estiloNormal);
        tfDescripcion.setStyle(estiloNormal);
    }
    
    private void validarCamposRegistro(){
        establecerEstiloNormal();
        boolean fechaValida = true;
        
        String nombre = tfNombrePromo.getText();
        int idProducto = cbProducto.getSelectionModel().getSelectedIndex();
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();
        String descuento = tfDescuento.getText();
        String descripcion = tfDescripcion.getText(); 
       
        if(nombre.isEmpty()){
            tfNombrePromo.setStyle(estiloError);
            fechaValida = false;
        }
        
        if(idProducto == -1){
            cbProducto.setStyle(estiloError);
            fechaValida = false;
        }
        
        if(fechaInicio == null){
            dpFechaInicio.setStyle(estiloError);
            fechaValida = false;
        }
        
        if(!Utilidades.descuentoValido(descuento)){
            tfDescuento.setStyle(estiloError);
            fechaValida = false;
        }
        
        if(fechaFin == null){
            dpFechaFin.setStyle(estiloError);
            fechaValida = false;
        }
        
        if(descripcion.isEmpty()){
            tfDescripcion.setStyle(estiloError);
            fechaValida = false;
        }

        if (dpFechaFin.getValue() != null && dpFechaInicio.getValue() != null) {
            if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
                Utilidades.mostrarDialogoSimple("Fecha no valida",
                "La fecha del fin de la promoción no puede ser una fecha anterior a la fecha de inicio",
                Alert.AlertType.WARNING);
                dpFechaInicio.setStyle(estiloError);
                dpFechaFin.setStyle(estiloError);
                fechaValida = false;
            }
        }
        if(fechaValida){
            Promocion promocionValidada = new Promocion();
            promocionValidada.setNombre(nombre);
            promocionValidada.setIdProducto(productos.get(idProducto).getIdProducto());
            promocionValidada.setFechaInicio(fechaInicio.toString());
            promocionValidada.setFechaFin(fechaFin.toString());
            promocionValidada.setDescuento(Float.parseFloat(descuento));
            promocionValidada.setDescripcion(descripcion);
            try{
                if(esEdicion){
                    if(archivoSeleccionado != null || promocionEdicion.getFotoPromocion().length > 0){
                        if(archivoSeleccionado != null){
                            promocionValidada.setFotoPromocion(Files.readAllBytes(archivoSeleccionado.toPath()));
                        }else{
                            promocionValidada.setFotoPromocion(promocionEdicion.getFotoPromocion());
                        }
                            promocionValidada.setIdPromociones(promocionEdicion.getIdPromociones());
                            actualizarPromocion(promocionValidada);
                        }else{
                            Utilidades.mostrarDialogoSimple("Selecciona una imagen", 
                                    "Para guardar el registro de la promocion debes seleccionar una foto", 
                                    Alert.AlertType.WARNING);
                        }
                }else{
                    if(archivoSeleccionado != null){
                    promocionValidada.setFotoPromocion(Files.readAllBytes(archivoSeleccionado.toPath()));
                    registrarPromocion(promocionValidada);
                    }else{
                        Utilidades.mostrarDialogoSimple("Selecciona una imagen", 
                                "Para guardar el registro de la promocion debes seleccionar una foto", 
                                Alert.AlertType.WARNING);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                Utilidades.mostrarDialogoSimple("Error con el archivo", 
                        "Hubo un error al intentar guardar la imagen, vuelva a seleccionar el archivo", Alert.AlertType.ERROR);
            }
        }
    }
    
    private void registrarPromocion(Promocion promocionRegistro){
        int codigoRespuesta = PromocionDAO.guardarPromocion(promocionRegistro);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexion xd", 
                        "La promocion no pudo ser guardado debido a un error en su conexión...", 
                        Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error en la información", 
                        "La información de la promocion no puede ser guardada, por favor verifique su información", 
                        Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Promocion registrada", 
                        "La información de la promocion fue guardada correctamente", 
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
                interfazNotificacion.notificarOperacionGuardarPromocion(promocionRegistro.getNombre());
                break;
        }
    }
    
    private void actualizarPromocion(Promocion promocionActualizar){
        int codigoRespuesta = PromocionDAO.modificarPromocion(promocionActualizar);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexion xd", 
                        "La promocion no pudo ser actualizado debido a un error en su conexión...", 
                        Alert.AlertType.ERROR);
                break;
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error en la información", 
                        "La información de la promocion no puede ser actualizada, por favor verifique su información", 
                        Alert.AlertType.WARNING);
                break;
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Alumno registrado", 
                        "La información de la promocion fue actualizada correctamente", 
                        Alert.AlertType.INFORMATION);
                cerrarVentana();
                interfazNotificacion.notificarOperacionActualizarPromocion(promocionActualizar.getNombre());
                break;
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
                    cbProducto.setItems(productos);
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
    
    private void cerrarVentana(){
        Stage escenarioBase = (Stage) lbTitulo.getScene().getWindow();
        escenarioBase.close();
    }
    
    @FXML
    private void clicBtnSeleccionFoto(ActionEvent event) {
        FileChooser dialogoSeleccionImg = new FileChooser();
        dialogoSeleccionImg.setTitle("Selecciona una imagen");
        FileChooser.ExtensionFilter filtroDialogo = 
                new FileChooser.ExtensionFilter("Archivos PNG (*.png)", "*.PNG"); 
        dialogoSeleccionImg.getExtensionFilters().add(filtroDialogo);
        
        Stage escenarioActual = (Stage) lbTitulo.getScene().getWindow();
        archivoSeleccionado = dialogoSeleccionImg.showOpenDialog(escenarioActual);
        if(archivoSeleccionado != null){
            try {
                BufferedImage bufferImg = ImageIO.read(archivoSeleccionado);
                Image imagenDecodificada = SwingFXUtils.toFXImage(bufferImg, null);
                ivPromocion.setImage(imagenDecodificada);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
