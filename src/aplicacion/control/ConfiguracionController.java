/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.IdentidadDAO;
import hibernate.model.Identidad;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class ConfiguracionController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button empresaButton;
    
    @FXML
    private Pane imagenLabel;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void salirVentana(ActionEvent event) {
        stagePrincipal.close(); 
    }
    
    @FXML
    private void registrarEmpresa(ActionEvent event) {
        aplicacionControl.mostarRegistrarEmpresa();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String image = AplicacionControl.class.getResource("imagenes/administracion.jpg").toExternalForm();
        imagenLabel.setStyle("-fx-background-image: url('" + image + "'); " + "-fx-background-position: center top; " + "-fx-background-repeat: stretch;");
    }    
    
}
