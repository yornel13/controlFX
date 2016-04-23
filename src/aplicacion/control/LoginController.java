/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.dao.IdentidadDAO;
import hibernate.dao.RolesDAO;
import hibernate.model.Identidad;
import hibernate.model.Roles;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class LoginController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Stage stagePrincipal;
    
    @FXML
    private TextField usuarioField;
    
    @FXML
    private TextField contrasenaField;
    
    @FXML
    private Label textError;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void salirVentana(ActionEvent event) {
        stagePrincipal.close();
    }
    
    @FXML
    private void login(ActionEvent event) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        IdentidadDAO idao = new IdentidadDAO();
        Identidad identidad;
        ArrayList<Identidad> iden = (ArrayList<Identidad>) idao.findByNombreUsuario(usuarioField.getText());
        if (iden.isEmpty()) {
            textError.setText("el usuario no existe");
        } else {
            identidad = iden.get(0);
            if (identidad.getContrasena().equals(MD5(contrasenaField.getText()))) {
                salirVentana(event);
                obtenerPermisos(identidad.getUsuario().getId());
                aplicacionControl.loginCompletado(identidad.getUsuario());
                //aplicacionControl.mostrarConfiguracion();
            } else {
                textError.setText("contraseña erronea");
            }
        }
    }
    
    public void obtenerPermisos(Integer usuarioId) {
        ArrayList<Roles> roles = new ArrayList<>();
        RolesDAO rolesDAO = new RolesDAO();
        roles.addAll(rolesDAO.findAllByUsuarioId(usuarioId));
        Permisos permisos = new Permisos();
        permisos.setRoles(roles);
        aplicacionControl.permisos = permisos;
    }
    
    public String MD5(String md5) {
        try {
             java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
             byte[] array = md.digest(md5.getBytes());
             StringBuffer sb = new StringBuffer();
             for (int i = 0; i < array.length; ++i) {
               sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
             return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            // nothing to do
        }
    return null;
}

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
