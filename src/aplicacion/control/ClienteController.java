/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.model.Cliente;
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
public class ClienteController implements Initializable {
    
    private Cliente cliente;
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Pane profileImage;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Label detalles;
    
    @FXML
    private Label telefono;
    
    @FXML
    private Label direccion;
    
    @FXML
    private Label numeracion; 

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
    
    public void editarCliente(ActionEvent event) {
        aplicacionControl.mostrarEditarCliente(cliente);
        stagePrincipal.close();
    }
    
    public void setCliente(Cliente cliente   ) {
        this.cliente = cliente;
        nombre.setText(cliente.getNombre());
        telefono.setText(cliente.getTelefono());
        direccion.setText(cliente.getDireccion());
        numeracion.setText(cliente.getRuc().toString());
        detalles.setText(cliente.getDetalles());
    }
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
}
