/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
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
public class EmpresaController implements Initializable {
    
    private Empresa empresa;
    
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
    private Label tipo;
    
    @FXML
    private Label siglas;
    
    @FXML
    private Label numeracion;
    
    @FXML
    private Label direccion;
    
    @FXML
    private Label telefono1;
    
    @FXML
    private Label telefono2;
    
    @FXML
    private Label fax;
    
    @FXML
    private Label email;
    
    @FXML
    private Label diaCorte;
    
    @FXML
    private Label totalEmpleados;
    
    @FXML
    private Label gerente;
    
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
    
    public void editarEmpresa(ActionEvent event) {
        aplicacionControl.mostrarEditarEmpresa(empresa);
        stagePrincipal.close();
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        nombre.setText(empresa.getNombre());
        tipo.setText(empresa.getTipo());
        siglas.setText(empresa.getSiglas());
        numeracion.setText(empresa.getNumeracion());
        direccion.setText(empresa.getDireccion());
        telefono1.setText(empresa.getTelefono1());
        telefono2.setText(empresa.getTelefono2());
        fax.setText(empresa.getFax());
        email.setText(empresa.getEmail());
        diaCorte.setText(empresa.getDiaCortePago() + " de cada mes");
        UsuarioDAO usuariosDAO = new UsuarioDAO();
        ArrayList<Usuario> empleados = new ArrayList<>();
        empleados.addAll(usuariosDAO.findByEmpresaIdActivo(empresa.getId()));
           
        totalEmpleados.setText(String.valueOf(empleados.size()));
        gerente.setText("no aplica");
    }
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String profile = AplicacionControl.class.getResource("imagenes/empty_logo.jpg").toExternalForm();
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
