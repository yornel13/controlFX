/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.model.Usuarios;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EmpleadoController implements Initializable {
    
    private Usuarios empleado;
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button empresaButton;
    
    @FXML
    private Pane profileImage;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Label cedula;
    
    @FXML
    private Label telefono;
    
    @FXML
    private Label direccion;
    
    @FXML
    private Label email;
    
    @FXML
    private Label estadoCivil;
    
    @FXML
    private Label empresa;
    
    @FXML
    private Label departamento;
    
    @FXML
    private Label cargo;
    
    @FXML
    private Label cuenta;
    
    @FXML
    private Label sueldo;
    
    @FXML
    private Label extra;
    
    @FXML
    private Label nombre;
    
    @FXML
    private Button editarButton;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void editarEmpleado(ActionEvent event) {
        aplicacionControl.mostrarEditarEmpleado(empleado);
        stagePrincipal.close();
    }
    
    public void setEmpleado(Usuarios empleado   ) {
        this.empleado = empleado;
        nombre.setText(empleado.getNombre() + " " + empleado.getApellido());
        cedula.setText(empleado.getCedula());
        telefono.setText(empleado.getTelefono());
        direccion.setText(empleado.getDireccion());
        email.setText(empleado.getEmail());
        estadoCivil.setText(empleado.getEstadoCivil().getNombre());
        empresa.setText(empleado.getDetallesEmpleado().getEmpresa().getNombre());
        departamento.setText(empleado.getDetallesEmpleado().getDepartamento().getNombre());
        cargo.setText(empleado.getDetallesEmpleado().getCargo().getNombre());
        cuenta.setText(empleado.getDetallesEmpleado().getNroCuenta());
        sueldo.setText(empleado.getDetallesEmpleado().getSueldo() + " $");
        extra.setText(empleado.getDetallesEmpleado().getExtra());
    }
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String profile = AplicacionControl.class.getResource("imagenes/empty_profile.jpg").toExternalForm();
        /*profileImage.setStyle("-fx-background-image: url('" + profile + "'); " 
                + "-fx-background-position: center top; " 
                + "-fx-background-repeat: stretch;"); */
      
        Image image = new Image(profile);
        Rectangle rekt = new Rectangle(40, 40); //haha
//    ☐ rekt
//    ☐ not rekt
//    ☑ tyrannosaurus rekt 
        ImagePattern imagePattern = new ImagePattern(image);
        rekt.setFill(imagePattern);
        

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        
        profileImage.getChildren().add(imageView);
        
    }    
    
}
