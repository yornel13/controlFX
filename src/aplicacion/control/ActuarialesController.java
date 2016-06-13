/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.HibernateSessionFactory;
import hibernate.dao.ActuarialesDAO;
import hibernate.model.Actuariales;
import hibernate.model.Usuario;
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
public class ActuarialesController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private ActuarialesEmpleadosController actuarialesEmpleadosController;
    
    @FXML
    private Label oldActuarial1;
    
    @FXML
    private Label oldActuarial2;
    
    @FXML
    private TextField newActuarial1;
    
    @FXML
    private TextField newActuarial2;
    
    Actuariales oldActuariales;
    
    Usuario empleado;
    
    @FXML 
    public void onClickGuardar(ActionEvent event) {
        if (newActuarial1.getText().isEmpty()) {
            
        } else if (newActuarial2.getText().isEmpty()) {
            
        } else {
            if (oldActuariales != null) {
                oldActuariales.setActivo(Boolean.FALSE);
                HibernateSessionFactory.getSession().flush();
            }
            Actuariales newActuariales = new Actuariales();
            newActuariales.setActivo(true);
            newActuariales.setPrimario(Double.parseDouble(newActuarial1.getText()));
            newActuariales.setSecundario(Double.parseDouble(newActuarial2.getText()));
            newActuariales.setFecha(new Timestamp(new Date().getTime()));
            newActuariales.setUsuario(empleado);
            new ActuarialesDAO().save(newActuariales);
            
            stagePrincipal.close();
            
            actuarialesEmpleadosController.empleadoEditado(empleado);
            
            String detalles = "edito los actuariales de " + empleado.getNombre() + " " + empleado.getApellido();
            aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Completado");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Actuariales editados satisfactoriamente."), buttonOk).
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
    
    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
        ActuarialesDAO actuarialesDAO = new ActuarialesDAO();
        oldActuariales = actuarialesDAO.findByEmpleadoId(empleado.getId());
        if (oldActuariales == null) {
            
        } else {
            oldActuarial1.setText(oldActuariales.getPrimario().toString());
            oldActuarial2.setText(oldActuariales.getSecundario().toString());
        }
    }
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setProgramaActuariales(ActuarialesEmpleadosController actuarialesEmpleadosController) {
        this.actuarialesEmpleadosController = actuarialesEmpleadosController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newActuarial1.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        newActuarial2.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
    }
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
}