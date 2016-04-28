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
import javafx.scene.image.Image;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EditarContrasenaController implements Initializable {
    
    private Stage stagePrincipal;
    
    private Identidad identidad;
    
    @FXML
    private PasswordField oldPassword;
    
    @FXML
    private PasswordField newPassword;
    
    @FXML
    private PasswordField repeatPassword;
    
    @FXML
    private Label errorText;
    
    @FXML
    public void onCLickGuardar(ActionEvent event) {
        if (!identidad.getContrasena().equals(Password.MD5(oldPassword.getText()))) {
            errorText.setText("La contraseña antigua es incorrecta");
        } else if (newPassword.getText().isEmpty()) {
            errorText.setText("Debe ingresar una contraseña");
        } else if (newPassword.getText().length() < 4) {
            errorText.setText("Debe ingresar una contraseña de minimo 4 caracteres");
        } else if (!newPassword.getText().equals(repeatPassword.getText())) {
            errorText.setText("Las contraseñas no son las mismas"); 
        } else {
            identidad.setContrasena(Password.MD5(newPassword.getText()));
            HibernateSessionFactory.getSession().flush();
            
            stagePrincipal.close();
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Dialogo");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Cambio de contraseña exitoso"), buttonOk).
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
    
    public void setIdentidad(Identidad identidad) {
        this.identidad = new IdentidadDAO().findById(identidad.getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
