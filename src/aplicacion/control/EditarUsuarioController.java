/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Password;
import hibernate.HibernateSessionFactory;
import hibernate.dao.IdentidadDAO;
import hibernate.model.Identidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EditarUsuarioController implements Initializable {
    
    private Stage stagePrincipal;
    
    
    private AplicacionControl aplicacionControl;
    
    private Identidad identidad;
    
    @FXML
    private PasswordField oldPassword;
    
    @FXML
    private TextField nombreUsuario;
    
    @FXML
    private Label errorText;
    
    @FXML
    public void onCLickGuardar(ActionEvent event) {
        if (!identidad.getContrasena().equals(Password.MD5(oldPassword.getText()))) {
            errorText.setText("La contraseña es incorrecta");
        } else if (nombreUsuario.getText().isEmpty()) {
            errorText.setText("Debe ingresar un nombre de usuario");
        } else if (nombreUsuario.getText().length() < 4) {
            errorText.setText("Debe ingresar un usuario de minimo 4 caracteres");
        } else if (!(new IdentidadDAO().findByNombreUsuarioActivo(nombreUsuario.getText()) == null)) {
            errorText.setText("El usuario ya esta en uso");
        } else {
            identidad.setNombreUsuario(nombreUsuario.getText());
            HibernateSessionFactory.getSession().flush();
            
            // Registro para auditar
            String detalles = "edito el nombre de usuario de " 
                    + identidad.getUsuario().getNombre() + " " 
                    + identidad.getUsuario().getApellido();
            aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
            
            stagePrincipal.close();
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Dialogo");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Cambio de nombre de usuario exitoso"), buttonOk).
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
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setIdentidad(Identidad identidad) {
        this.identidad = new IdentidadDAO().findById(identidad.getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
