/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Const;
import aplicacion.control.util.Roboto;
import hibernate.dao.EmpresaDAO;
import hibernate.model.Empresa;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class PrincipalController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label label;
    
    @FXML
    private ChoiceBox selector;
    
    @FXML
    private Button buttonGlobal;
    
    @FXML
    private Button buttonEntrar;
    
    @FXML
    private Label labelEmpresa;
    
    ArrayList<Empresa> empresas;
    
    private Stage stagePrincipal;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (!empresas.isEmpty() && !selector.getSelectionModel().isEmpty()) {
            aplicacionControl.mostrarInEmpresa(empresas.get(selector.getSelectionModel().getSelectedIndex()));
        }  else {
            label.setText("Selecciona una empresa primero.");
        }
    }
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me menu!");
        //label.setText(menuButton.getCursor().toString());
    }
    
    @FXML
    private void onCLickConfiguration(ActionEvent event) throws IOException, Exception {
        aplicacionControl.mostrarConfiguracion();
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
        try {
            EmpresaDAO empresaDao = new EmpresaDAO();
            empresas = new ArrayList<>();
            empresas.addAll(empresaDao.findAll());

            String[] items = new String[empresas.size()];

            empresas.stream().forEach((emp) -> {
                items[empresas.indexOf(emp)] = emp.getNombre();
            });

            selector.setItems(FXCollections.observableArrayList(items)); 
        } catch (Exception e) {
            label.setText("ERROR DE CONEXION A BASE DE DATOS.");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        buttonGlobal.setTooltip(
            new Tooltip("Configuración Global")
        );
        buttonGlobal.setOnMouseEntered((MouseEvent t) -> {
            buttonGlobal.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/global.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonGlobal.setOnMouseExited((MouseEvent t) -> {
            buttonGlobal.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/global.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        buttonEntrar.setFont(Roboto.MEDIUM(12));
        buttonEntrar.setOnMouseEntered((MouseEvent t) -> {
            buttonEntrar.setStyle("-fx-background-color: #E0E0E0;");
        });
        buttonEntrar.setOnMouseExited((MouseEvent t) -> {
            buttonEntrar.setStyle("-fx-background-color: #039BE5;");
        });
        
        labelEmpresa.setFont(Roboto.MEDIUM(14));
        label.setFont(Roboto.MEDIUM(14));
        
        
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
