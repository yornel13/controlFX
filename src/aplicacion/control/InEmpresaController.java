/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Const;
import hibernate.dao.DetallesEmpleadoDAO;
import hibernate.dao.UsuariosDAO;
import hibernate.model.Empresa;
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
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class InEmpresaController implements Initializable {
    
    Empresa empresa;
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button empresaButton;
    
    @FXML
    private Pane contentPane;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Label labelTest;
    
    @FXML
    private Label numeracionLabel;
    
    @FXML
    private Label empresaLabel;
    
    @FXML
    private Label totalLabel;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarVentanaPrincipal();
        stagePrincipal.close();
    }
    
    @FXML
    private void verEmpleados(ActionEvent event) {
        aplicacionControl.mostarEmpleados();
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        empresaLabel.setText(empresa.getNombre());
        numeracionLabel.setText("RUC: " + empresa.getNumeracion().toString());
        DetallesEmpleadoDAO detallesEmpleadoDAO = new DetallesEmpleadoDAO();
        totalLabel.setText("Total de empleados: " 
                + String.valueOf(detallesEmpleadoDAO.findByProperty("empresa", empresa).size()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        homeButton.setStyle(Const.BACKGROUND_COLOR_SEMI_TRANSPARENT);
        String image2 = AplicacionControl.class.getResource("imagenes/home_32dp.png").toExternalForm();
        Image homeImage = new Image(image2);
        homeButton.setGraphic(new ImageView(homeImage));
        
        contentPane.setStyle(Const.BACKGROUND_COLOR_SEMI_TRANSPARENT);
        
        labelTest.setText("Este es un texto \n de prueba");
        
    }    
    
}
