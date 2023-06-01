package jfxgestionfarmacia.controladores;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jfxgestionfarmacia.interfazempleado.INotificacionOperacionProducto;
import jfxgestionfarmacia.modelo.DAO.ProductoDAO;
import jfxgestionfarmacia.modelo.pojo.Producto;
import jfxgestionfarmacia.utils.Constantes;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLFormularioProductoController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbImagen;
    @FXML
    private TextField tfNombreProducto;
    @FXML
    private TextField tfPrecio;
    private TextField tfFechaCaducidad;
    @FXML
    private TextField tfCuantosAgregar;
    @FXML
    private ComboBox<String> cbTipoProducto;
    @FXML
    private ComboBox<String> cbProductoControlado;

    private Producto productoEdicion;
    private boolean esEdicion;
    private INotificacionOperacionProducto interfazNotificacionProducto;
    private File archivoFoto;
    private ObservableList<Producto> productos;
    String estiloError = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 2;";
    String estiloNormal = "-fx-border-width: 0;";
    @FXML
    private ImageView IVFotoProducto;
    @FXML
    private DatePicker DpFechaCaducidad;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarComboBox();
        DpFechaCaducidad.setEditable(false);
        
    }    
    
     public void inicializarInformacionFormulario(boolean esEdicion,
            Producto productoEdicion, INotificacionOperacionProducto interfazNotificacionProducto){
        
        this.esEdicion = esEdicion;
        this.productoEdicion = productoEdicion;
        this.interfazNotificacionProducto = interfazNotificacionProducto;
        
        
        if(esEdicion){
            lbTitulo.setText("Editar informacion del producto "+productoEdicion.getNombre());
           cargarInformacionEdicion();
        }else{
            lbTitulo.setText("Registrar nuevo producto");
        }       
    }

    private void cargarInformacionEdicion(){
        tfNombreProducto.setText(productoEdicion.getNombre());
        
        float precio = productoEdicion.getPrecio();
        String precioTexto = String.valueOf(precio);
        tfPrecio.setText(precioTexto);
        
        int stock = productoEdicion.getStock();
        String stockTexto = String.valueOf(stock);
        tfCuantosAgregar.setText(stockTexto);
        DpFechaCaducidad.setValue(LocalDate.parse( productoEdicion.getFechaCaducidad()));

        String tipoProducto = productoEdicion.getTipoProducto();
        String esControlado = productoEdicion.getEsControlado();

        if (tipoProducto != null && !tipoProducto.isEmpty()) {
        cbTipoProducto.setValue(tipoProducto);
        }

        if (esControlado != null && !esControlado.isEmpty()) {
        cbProductoControlado.setValue(esControlado);
        } 
        
        try{          
            byte[] fotoProducto = productoEdicion.getFotoProducto();
            if (fotoProducto != null && fotoProducto.length > 0) {
            ByteArrayInputStream inputFoto = new ByteArrayInputStream(productoEdicion.getFotoProducto());
            Image imgFotoProducto = new Image(inputFoto);
            IVFotoProducto.setImage(imgFotoProducto);
            }else {
            IVFotoProducto.setImage(null);
        }
            
        }catch(Exception e){
            e.printStackTrace();
        } 
    }
     
    @FXML
    private void clicBtnCancelarNuevoProducto(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicBtnRegistrarProducto(ActionEvent event) {
         validarCamposRegistro(); 
    }
    
    private void validarCamposRegistro(){
        establecerEstiloNormal();
        boolean datosValidados = true;
         
        String nombre = tfNombreProducto.getText();
        float precio;
        String tipoProducto = cbTipoProducto.getValue();
        String esControlado = cbProductoControlado.getValue();         
        int stock;
        LocalDate fechaCaducidad = DpFechaCaducidad.getValue();
      
        try {
             precio = Float.parseFloat(tfPrecio.getText());
             stock = Integer.parseInt(tfCuantosAgregar.getText());
             
        } catch (NumberFormatException e) {
            tfPrecio.setStyle(estiloError);
            tfCuantosAgregar.setStyle(estiloError);
            
            if (nombre.isEmpty()) {
            tfNombreProducto.setStyle(estiloError);
            datosValidados = false;
        }        
        if (tipoProducto == null || tipoProducto.isEmpty()) {
            cbTipoProducto.setStyle(estiloError);
            datosValidados = false;
        }
        if (esControlado == null || esControlado.isEmpty()) {
            cbProductoControlado.setStyle(estiloError);
            datosValidados = false;
        }
        
        if(fechaCaducidad == null){
            DpFechaCaducidad.setStyle(estiloError);
            datosValidados = false;
        }
            datosValidados = false;
            return; // Salir del método si los campos no son válidos
        }

            
        if (datosValidados) {            
            Producto productoValidado = new Producto();
            
            productoValidado.setNombre(nombre);
            productoValidado.setTipoProducto(tipoProducto);
            productoValidado.setPrecio(precio);           
            productoValidado.setEsControlado(esControlado);
            productoValidado.setStock(stock);
             productoValidado.setFechaCaducidad(fechaCaducidad.toString());

          
        
            try{
                if (esEdicion) {
                    productoValidado.setIdProducto(productoEdicion.getIdProducto());                
                    if (archivoFoto != null) {
                        productoValidado.setFotoProducto(Files.readAllBytes(archivoFoto.toPath()));
                    } else {
                        productoValidado.setFotoProducto(productoEdicion.getFotoProducto());
                    }               

                    actualizarProducto(productoValidado);
                } else {
                    registrarProducto(productoValidado);
                }
                
        
            }catch(IOException e){
                e.printStackTrace();
                Utilidades.mostrarDialogoSimple("Error con el archivo",
                    "Hubo un error al intentar gurdar la imagen en validarCamposRegistro, vuelva a seleccionar el archivo",
                    Alert.AlertType.ERROR);
            }
        }
    }
    
     private void agregarComboBox(){
                cbTipoProducto.setItems(FXCollections.observableArrayList("Medicamento", "Producto"));
                cbProductoControlado.setItems(FXCollections.observableArrayList("Si", "No"));
    }
     
    private void registrarProducto(Producto productoRegistro){
        int codigoRespuesta = ProductoDAO.guardarProducto(productoRegistro);
        switch(codigoRespuesta){
            case Constantes.ERROR_CONEXION:
                Utilidades.mostrarDialogoSimple("Error de conexion en registrarProducto",
                        "El producto no pudo ser guardado debido a un error de conexion",
                        Alert.AlertType.ERROR);
                break;
                
            case Constantes.ERROR_CONSULTA:
                Utilidades.mostrarDialogoSimple("Error en la informacion en registrarProducto",
                        "La informacion del alumno no puede ser guardada, por favor verifique su conexion",
                        Alert.AlertType.WARNING);
                break;
                
            case Constantes.OPERACION_EXITOSA:
                Utilidades.mostrarDialogoSimple("Producto registrado",
                        "La informacion del producto fue guardada correctamente",
                        Alert.AlertType.INFORMATION);
                
                cerrarVentana();     
                interfazNotificacionProducto.notificarOperacionGuardarProducto(productoRegistro.getNombre());
                
                break;
        }
    }
    
    private void actualizarProducto(Producto productoActualizar){
            int codigoRespuesta = ProductoDAO.modificarProducto(productoActualizar);
                switch(codigoRespuesta){
                    case Constantes.ERROR_CONEXION:
                    Utilidades.mostrarDialogoSimple("Error de conexion en actualizarProducto",
                        "El producto no pudo ser actualizado debido a un error de conexion",
                        Alert.AlertType.ERROR);
                     break;
                
                case Constantes.ERROR_CONSULTA:
                    Utilidades.mostrarDialogoSimple("Error en la informacion en actualizarProducto",
                        "La informacion del producto no puede ser actualizada, por favor verifique su conexion",
                        Alert.AlertType.WARNING);
                    break;
                
                case Constantes.OPERACION_EXITOSA:
                    Utilidades.mostrarDialogoSimple("Producto actualizado",
                        "La informacion del producto fue guardada correctamente",
                        Alert.AlertType.INFORMATION);
                
                    cerrarVentana();
                    //NOTIFICACION de la operacion, 
                    interfazNotificacionProducto.notificarOperacionActualizarProducto(productoActualizar.getNombre());
                
                    break;
                }               
                              
        }
    
    private void establecerEstiloNormal(){
        tfNombreProducto.setStyle(estiloNormal);
        tfPrecio.setStyle(estiloNormal);        
        cbTipoProducto.setStyle(estiloNormal);
        cbProductoControlado.setStyle(estiloNormal);
        tfCuantosAgregar.setStyle(estiloNormal);
        DpFechaCaducidad.setStyle(estiloNormal);
    }
    
    
    
       
    
    
    private void cerrarVentana(){
        //Referencia al stage, usamos cualquier vista, el titulo
        Stage escenarioBase = (Stage) lbTitulo.getScene().getWindow();
        escenarioBase.close();
        
    }

    @FXML
    private void clickBtnFotoProducto(ActionEvent event) {
        List<String> tiposDeArchivo = Arrays.asList("*.png", "*.jpg", "*.jpeg");
        FileChooser dialogoImagen = new FileChooser();
        dialogoImagen.setTitle("Selecciona una imagen");        
        FileChooser.ExtensionFilter filtroImg =
                new FileChooser.ExtensionFilter("Archivos de Imagen(*.png, *.jpg, *.jpeg)",
                        tiposDeArchivo);
        dialogoImagen.getExtensionFilters().add(filtroImg);
        Stage escenarioActual = (Stage) lbImagen.getScene().getWindow();
        archivoFoto = dialogoImagen.showOpenDialog(escenarioActual);
        
        if(archivoFoto != null){
            try{
                BufferedImage bufferImg = ImageIO.read(archivoFoto);
                Image imagenFoto = SwingFXUtils.toFXImage(bufferImg, null);
                IVFotoProducto.setImage(imagenFoto);
            }catch(Exception e){
                e.printStackTrace();
                 
             }
        }   
    }
    
}
