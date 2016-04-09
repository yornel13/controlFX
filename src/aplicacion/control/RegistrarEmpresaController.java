/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.EmpresaDAO;
import hibernate.model.Empresa;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class RegistrarEmpresaController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Stage stagePrincipal;
    
    @FXML
    private TextField nombreField;
    
    @FXML
    private TextField siglasField;
    
    @FXML
    private TextField numeracionField;
    
    @FXML
    private TextField telefono1Field;
    
    @FXML
    private TextField telefono2Field;
    
    @FXML
    private TextField faxField;
    
    @FXML
    private TextField direccionField;
    
    @FXML
    private TextField webField;
    
    @FXML
    private TextField emailField;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
   
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me menu!");
        //label.setText(menuButton.getCursor().toString());
    }
    
    @FXML
    private void onCLickGuardar(ActionEvent event) throws IOException, Exception {
        
        if (nombreField.getText().isEmpty()) {
            
        } else if (siglasField.getText().isEmpty()) {
            
        } else if (numeracionField.getText().isEmpty()) {
            
        } else if (telefono1Field.getText().isEmpty()) {
            
        } else if (direccionField.getText().isEmpty()) {
            
        } else if (webField.getText().isEmpty()) {
            
        } else if (emailField.getText().isEmpty()) {
            
        } else {
            Empresa empresa = new Empresa();
            empresa.setNombre(nombreField.getText());
            empresa.setSiglas(siglasField.getText());
            empresa.setNumeracion(Integer.getInteger(numeracionField.getText()));
            empresa.setTelefono1(telefono1Field.getText());
            empresa.setTelefono2(telefono2Field.getText());
            empresa.setFax(faxField.getText());
            empresa.setWeb(webField.getText());
            empresa.setEmail(emailField.getText());
            empresa.setActivo(Boolean.TRUE);
            
            EmpresaDAO empresaDAO = new EmpresaDAO();
            
        }
    }
    
    @FXML
    public void onClickCancelar(ActionEvent event) {
        stagePrincipal.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}
