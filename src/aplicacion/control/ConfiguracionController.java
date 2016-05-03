/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    
    @FXML
    private Button homeButton;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarVentanaPrincipal();
    }
    
    @FXML
    private void mostrarDepartamentosCargos(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarCargosDepartamentos();
    }
    
    @FXML
    private void mostarAuditorias(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarAuditoria();
    }
    
    @FXML
    private void registrarEmpresa(ActionEvent event) {
        aplicacionControl.mostrarRegistrarEmpresa();
    }
    
    @FXML
    private void registrarAdministrador(ActionEvent event) {
        aplicacionControl.mostrarRegistrarAdministrador();
    }
    
    @FXML
    private void mostrarEmpresas(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarEmpresas();
    }
            
    @FXML
    private void registrarCliente(ActionEvent event) {
        aplicacionControl.mostrarRegistrarCliente();
    }
    
    @FXML
    private void mostrarClientes(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarClientes();
    }
    
    @FXML
    private void verAdministradores(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarAdminitradores();
    }
            
    @FXML
    private void verEmpleados(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarEmpleadosTodos();
    }       
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String image = AplicacionControl.class.getResource("imagenes/fondo_administracion.jpg").toExternalForm();
        imagenLabel.setStyle("-fx-background-image: url('" + image + "'); " 
               + "-fx-background-position: center top; " 
                + "-fx-background-repeat: stretch;");
        
        
        /*
        homeButton.setStyle(Const.BACKGROUND_COLOR_SEMI_TRANSPARENT);
        String image2 = AplicacionControl.class.getResource("imagenes/home_32dp.png").toExternalForm();
        Image homeImage = new Image(image2);
        homeButton.setGraphic(new ImageView(homeImage));
        */
    }  
    
    
    // Login items
    @FXML
    public Button login;
    
    @FXML 
    public Label usuarioLogin;
    
    @FXML
    public void onClickLoginButton(ActionEvent event) {
        aplicacionControl.login(login, usuarioLogin);
    }
    
}
