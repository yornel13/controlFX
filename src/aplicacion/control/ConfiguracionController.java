/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Roboto;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private Button buttonAtras;
    
    @FXML
    private Button buttonEmpleados;
    
    @FXML
    private Button buttonConstantes;
    
    @FXML
    private Button buttonVariables;
    
    @FXML
    private Button buttonClientes;
    
    @FXML
    private Button buttonEmpresas;
    
    @FXML
    private Button buttonAdministradores;
    
     @FXML
    private Button buttonAuditoria;
    
    
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
        aplicacionControl.mostrarCargosDepartamentos(stagePrincipal);
    }
    
    @FXML
    private void mostarAuditorias(ActionEvent event) {
        aplicacionControl.mostrarAuditoria(stagePrincipal);
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
        aplicacionControl.mostrarEmpresas(stagePrincipal);
    }
            
    @FXML
    private void registrarCliente(ActionEvent event) {
        aplicacionControl.mostrarRegistrarCliente();
    }
    
    @FXML
    private void mostrarClientes(ActionEvent event) {
        aplicacionControl.mostrarClientes(stagePrincipal);
    }
    
    @FXML
    private void verAdministradores(ActionEvent event) {
        aplicacionControl.mostrarAdminitradores(stagePrincipal);
    }
            
    @FXML
    private void verEmpleados(ActionEvent event) {
        aplicacionControl.mostrarEmpleadosTodos(stagePrincipal);
    }   

    @FXML
    private void verConstantes(ActionEvent event) {
        aplicacionControl.mostrarConstantes(stagePrincipal);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        buttonAtras.setOnMouseEntered((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAtras.setOnMouseExited((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_guardia.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonEmpleados.setGraphic(imageView);
            buttonEmpleados.setFont(Roboto.MEDIUM(15));
            buttonEmpleados.setOnMouseEntered((MouseEvent t) -> {
                buttonEmpleados.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonEmpleados.setOnMouseExited((MouseEvent t) -> {
                buttonEmpleados.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_constantes.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonConstantes.setGraphic(imageView);
            buttonConstantes.setFont(Roboto.MEDIUM(15));
            buttonConstantes.setOnMouseEntered((MouseEvent t) -> {
                buttonConstantes.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonConstantes.setOnMouseExited((MouseEvent t) -> {
                buttonConstantes.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_variables.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonVariables.setGraphic(imageView);
            buttonVariables.setFont(Roboto.MEDIUM(15));
            buttonVariables.setOnMouseEntered((MouseEvent t) -> {
                buttonVariables.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonVariables.setOnMouseExited((MouseEvent t) -> {
                buttonVariables.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_clientes.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonClientes.setGraphic(imageView);
            buttonClientes.setFont(Roboto.MEDIUM(15));
            buttonClientes.setOnMouseEntered((MouseEvent t) -> {
                buttonClientes.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonClientes.setOnMouseExited((MouseEvent t) -> {
                buttonClientes.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_empresa.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonEmpresas.setGraphic(imageView);
            buttonEmpresas.setFont(Roboto.MEDIUM(15));
            buttonEmpresas.setOnMouseEntered((MouseEvent t) -> {
                buttonEmpresas.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonEmpresas.setOnMouseExited((MouseEvent t) -> {
                buttonEmpresas.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_admin.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonAdministradores.setGraphic(imageView);
            buttonAdministradores.setFont(Roboto.MEDIUM(15));
            buttonAdministradores.setOnMouseEntered((MouseEvent t) -> {
                buttonAdministradores.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonAdministradores.setOnMouseExited((MouseEvent t) -> {
                buttonAdministradores.setStyle("-fx-background-color: #039BE5;");
            });
        }
        {
            Image imageGuardia = new Image(getClass().getResourceAsStream("imagenes/bt_auditar.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonAuditoria.setGraphic(imageView);
            buttonAuditoria.setFont(Roboto.MEDIUM(15));
            buttonAuditoria.setOnMouseEntered((MouseEvent t) -> {
                buttonAuditoria.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonAuditoria.setOnMouseExited((MouseEvent t) -> {
                buttonAuditoria.setStyle("-fx-background-color: #039BE5;");
            });
        }
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
