/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.model.Usuario;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class EmpleadoController implements Initializable {
    
    private Usuario empleado;
    
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
    private Label fechaInicio;
    
    @FXML
    private Label fechaContrato;
    
    @FXML
    private Button editarButton;
    
    
    @FXML
    public void changeImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setTitle("Selecciona la foto");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));                 
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("GIF", "*.gif"),
            new FileChooser.ExtensionFilter("BMP", "*.bmp"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        
        File file = fileChooser.showOpenDialog(stagePrincipal);
        System.out.println(file);
        String profile = file.toURI().toString();
        
        Image image = new Image(profile);
        Rectangle rekt = new Rectangle(40, 40); 
        ImagePattern imagePattern = new ImagePattern(image);
        rekt.setFill(imagePattern);
        

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        
        profileImage.getChildren().clear();
        profileImage.getChildren().add(imageView);
    }
    
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
    
    public void setEmpleado(Usuario empleado   ) {
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
        DateTime inicio = new DateTime(empleado.getDetallesEmpleado().getFechaInicio().getTime());
        fechaInicio.setText(inicio.getDayOfMonth() + " de " + getMonthName(inicio.getMonthOfYear()) + " " + inicio.getYear());
        DateTime contrato = new DateTime(empleado.getDetallesEmpleado().getFechaContrato().getTime());
        fechaContrato.setText(contrato.getDayOfMonth() + " de " + getMonthName(contrato.getMonthOfYear()) + " " + contrato.getYear());
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String profile = AplicacionControl.class.getResource("imagenes/empty_profile.jpg").toExternalForm();
        /*profileImage.setStyle("-fx-background-image: url('" + profile + "'); " 
                + "-fx-background-position: center top; " 
                + "-fx-background-repeat: stretch;"); */
      
        Image image = new Image(profile);
        Rectangle rekt = new Rectangle(40, 40); 
        ImagePattern imagePattern = new ImagePattern(image);
        rekt.setFill(imagePattern);
        

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        
        profileImage.getChildren().add(imageView);
        
    }  
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
}
