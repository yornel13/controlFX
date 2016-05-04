/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.FilterMaxValue;
import hibernate.HibernateSessionFactory;
import hibernate.model.Cliente;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EditarClienteController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Stage stagePrincipal;
    
    private Cliente cliente;
    
    @FXML
    private TextField nombreField;
    
    @FXML
    private TextField detallesField;
    
    @FXML
    private TextField numeracionField;
    
    @FXML
    private TextField telefonoField;
   
    @FXML
    private TextField direccionField;
  
    @FXML
    private Label errorText;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        nombreField.setText(this.cliente.getNombre());
        detallesField.setText(this.cliente.getDetalles());
        numeracionField.setText(this.cliente.getRuc().toString());
        telefonoField.setText(this.cliente.getTelefono());
        direccionField.setText(this.cliente.getDireccion());
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
            errorText.setText("Debe ingresar el nombre del cliente");
        }  else if (numeracionField.getText().isEmpty()) {
            errorText.setText("Ingrese el RUC");
        } else if (telefonoField.getText().isEmpty()) {
            errorText.setText("Debe ingresar un numero telefonico");
        } else if (direccionField.getText().isEmpty()) {
            errorText.setText("Debe ingresar la direccion del cliente");
        }  else {
            cliente.setNombre(nombreField.getText());
            cliente.setDetalles(detallesField.getText());
            cliente.setRuc(Integer.parseInt(numeracionField.getText()));
            cliente.setTelefono(telefonoField.getText());
            cliente.setDireccion(direccionField.getText());
            cliente.setUltimaModificacion(new Timestamp(new Date().getTime()));
            HibernateSessionFactory.getSession().flush();
            
            stagePrincipal.close();
            
            // Registro para auditar
            String detalles = "edito el cliente " 
                    + cliente.getNombre();
            aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Dialogo");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Cliente editado satisfactoriamente"), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            dialogStage.show();
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
        }
    }
    
    @FXML
    public void onClickCancelar(ActionEvent event) {
        stagePrincipal.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        numeracionField.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        numeracionField.addEventFilter(KeyEvent.KEY_TYPED, new FilterMaxValue(999999999));
    }    
    
    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();

            }
        };
        return aux;
    }
}
