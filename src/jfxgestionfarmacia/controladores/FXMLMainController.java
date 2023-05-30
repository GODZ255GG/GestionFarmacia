package jfxgestionfarmacia.controladores;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxgestionfarmacia.utils.Utilidades;

public class FXMLMainController implements Initializable {

    @FXML
    private Label lbReloj;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarHora();
    }    
 
    public void mostrarHora(){
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        Timeline timeline = new Timeline (new KeyFrame (Duration.seconds(1),event ->{
            LocalTime horaActual = LocalTime.now();
            lbReloj.setText(horaActual.format(formatoHora));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    @FXML
    private void clicAdminEmpleados(MouseEvent event) {
        Stage escenarioEmpleados = new Stage();
        Scene esceneAdminEmpleados = Utilidades.inicializarEscena("vistas/FXMLAdminEmpleados.fxml");
        escenarioEmpleados.setScene(esceneAdminEmpleados);
        escenarioEmpleados.setTitle("Administración de Empleados");
        escenarioEmpleados.initModality(Modality.APPLICATION_MODAL);
        escenarioEmpleados.showAndWait();
    }

    @FXML
    private void clicPromociones(MouseEvent event) {
        Stage escenarioEmpleados = new Stage();
        Scene esceneAdminEmpleados = Utilidades.inicializarEscena("vistas/FXMLPromociones.fxml");
        escenarioEmpleados.setScene(esceneAdminEmpleados);
        escenarioEmpleados.setTitle("Administración de Promociones");
        escenarioEmpleados.initModality(Modality.APPLICATION_MODAL);
        escenarioEmpleados.showAndWait();
    }

    @FXML
    private void clicCerrarSesion(MouseEvent event) {
        boolean cerrarSesion = Utilidades.mostrarDialogoConfirmacion("Cerrar Sesion", 
                "¿Esta seguro que desea cerrar sesion?");
        if(cerrarSesion){
            Stage escenarioBase = (Stage) lbReloj.getScene().getWindow();
                escenarioBase.setScene(Utilidades.inicializarEscena("vistas/FXMLInicioSesion.fxml"));
                escenarioBase.setTitle("Inicio Sesión");
                escenarioBase.show();
        }
    }
}
