/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.dao.RolesDAO;
import hibernate.model.Roles;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class AsignarRolController implements Initializable {
    
    private RegistrarAdministradorController registrarAdministradorController;
    private Stage stagePrincipal;
    
    private Usuario usuario;
    
    @FXML
    private ToggleGroup grupoEmpleados; 
    
    @FXML
    private ToggleGroup grupoEmpresas; 
    
    @FXML
    private ToggleGroup grupoClientes; 
    
    @FXML
    private ToggleGroup grupoHoras; 
    
    @FXML
    private ToggleGroup grupoRol; 
    
    @FXML
    private ToggleGroup grupoDeudas; 
    
    @FXML
    private Pane panelPermisos;
    
    @FXML
    private CheckBox controlTotal;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(RegistrarAdministradorController registrar) {
        this.registrarAdministradorController = registrar;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @FXML
    public void setRoles(ActionEvent event) {
        
        int permisos = 0;
        
        RolesDAO rolesDAO = new RolesDAO();
        
        if (controlTotal.isSelected()) {
            
            Roles rol = new Roles();
            rol.setNombre(Permisos.TOTAL);
            rol.setPermiso(Permisos.Nivel.ELIMINAR);
            rol.setActivo(Boolean.TRUE);
            rol.setUsuario(usuario);
            rolesDAO.save(rol);
            
            permisos ++;
            
        } else {

            if (grupoEmpleados.getSelectedToggle() != null) {

                RadioButton radioButton = (RadioButton) grupoEmpleados.getSelectedToggle();

                Roles rol = new Roles();
                rol.setNombre(Permisos.A_EMPLEADOS);
                rol.setPermiso(radioButton.getText().toLowerCase());
                rol.setActivo(Boolean.TRUE);
                rol.setUsuario(usuario);
                rolesDAO.save(rol);
                
                permisos ++;

            } 
            if (grupoEmpresas.getSelectedToggle() != null) {

                RadioButton radioButton = (RadioButton) grupoEmpresas.getSelectedToggle();

                Roles rol = new Roles();
                rol.setNombre(Permisos.A_EMPRESAS);
                rol.setPermiso(radioButton.getText().toLowerCase());
                rol.setActivo(Boolean.TRUE);
                rol.setUsuario(usuario);
                rolesDAO.save(rol);
                
                permisos ++;

            } 
            if (grupoClientes.getSelectedToggle() != null) {
                
                RadioButton radioButton = (RadioButton) grupoClientes.getSelectedToggle();

                Roles rol = new Roles();
                rol.setNombre(Permisos.A_CLIENTES);
                rol.setPermiso(radioButton.getText().toLowerCase());
                rol.setActivo(Boolean.TRUE);
                rol.setUsuario(usuario);
                rolesDAO.save(rol);
                
                permisos ++;

            } 
            if (grupoHoras.getSelectedToggle() != null) {

                RadioButton radioButton = (RadioButton) grupoHoras.getSelectedToggle();

                Roles rol = new Roles();
                rol.setNombre(Permisos.A_HORAS_EMPLEADO);
                rol.setPermiso(radioButton.getText().toLowerCase());
                rol.setActivo(Boolean.TRUE);
                rol.setUsuario(usuario);
                rolesDAO.save(rol);
                
                permisos ++;

            } 
            if (grupoRol.getSelectedToggle() != null) {

                RadioButton radioButton = (RadioButton) grupoRol.getSelectedToggle();

                Roles rol = new Roles();
                rol.setNombre(Permisos.A_ROL_DE_PAGO);
                rol.setPermiso(radioButton.getText().toLowerCase());
                rol.setActivo(Boolean.TRUE);
                rol.setUsuario(usuario);
                rolesDAO.save(rol);
                
                permisos ++;

            }
            
            if (grupoDeudas.getSelectedToggle() != null) {

                RadioButton radioButton = (RadioButton) grupoDeudas.getSelectedToggle();

                Roles rol = new Roles();
                rol.setNombre(Permisos.A_DEUDAS);
                rol.setPermiso(radioButton.getText().toLowerCase());
                rol.setActivo(Boolean.TRUE);
                rol.setUsuario(usuario);
                rolesDAO.save(rol);
                
                permisos ++;

            }

        }
        
        if (permisos > 0) {
            // Registro para auditar
            String detalles = "agrego permisos al usuario " 
                    + usuario.getNombre() + " " 
                    + usuario.getApellido();
            AplicacionControl aplicacionControl = registrarAdministradorController.aplicacionControl;
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            if (aplicacionControl.changeAdministradoresController != null) {
                aplicacionControl.changeAdministradoresController.setAdministradores();
            }
            
            stagePrincipal.close();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Completado");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Usuario registrado satisfactoriamente"), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            dialogStage.show();
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Dialogo");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Debes seleccionar los permisos del usuario"), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            dialogStage.show();
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
        }
        
    }
    
    @FXML
    public void onSetControlTotal(ActionEvent event) {
        if (controlTotal.isSelected()) {
            panelPermisos.setVisible(false);
            if (grupoClientes.getSelectedToggle() != null){
                ((RadioButton) grupoClientes.getSelectedToggle()).setSelected(false);
            }
            if (grupoEmpleados.getSelectedToggle() != null){
                ((RadioButton) grupoEmpleados.getSelectedToggle()).setSelected(false);
            }
            if (grupoEmpresas.getSelectedToggle() != null){
                ((RadioButton) grupoEmpresas.getSelectedToggle()).setSelected(false);
            }
            if (grupoHoras.getSelectedToggle() != null){
                ((RadioButton) grupoHoras.getSelectedToggle()).setSelected(false);
            }
            if (grupoRol.getSelectedToggle() != null){
                ((RadioButton) grupoRol.getSelectedToggle()).setSelected(false);
            }
            if (grupoDeudas.getSelectedToggle() != null){
                ((RadioButton) grupoDeudas.getSelectedToggle()).setSelected(false);
            }
        } else {
            panelPermisos.setVisible(true);
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
}
