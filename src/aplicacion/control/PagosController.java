/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.ControlEmpleado;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class PagosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button empresaButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarVentanaPrincipal();
    }
    
    @FXML
    private void registrarEmpresa(ActionEvent event) {
        aplicacionControl.mostrarRegistrarEmpresa();
    }
    
    @FXML
    private void mostrarEmpresas(ActionEvent event) {
        aplicacionControl.mostrarEmpresas();
        stagePrincipal.close();
    }
            
    @FXML
    private void registrarCliente(ActionEvent event) {
        aplicacionControl.mostrarRegistrarCliente();
    }
    
    @FXML
    private void mostrarClientes(ActionEvent event) {
        aplicacionControl.mostrarClientes();
        stagePrincipal.close();
    }
    
    private void metodo(Empresa empresa) {
        UsuarioDAO usuariosDAO = new UsuarioDAO();
        ArrayList<Usuario> empleados = new ArrayList<>();
        empleados.addAll(usuariosDAO.findByEmpresaId(empresa.getId()));
        
        for (Usuario emp: empleados){
            ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
            
            Integer normales = 0;
            Integer sobreTiempo = 0;
            Integer suplementarias = 0;
            
            for (ControlEmpleado control: controlDAO.findAllByEmpleadoId(emp.getId())) {
                normales = normales + 8;
                sobreTiempo = sobreTiempo + control.getHorasExtras();
                suplementarias = suplementarias + control.getHorasSuplementarias();
            }
            
            System.out.println(normales);
            System.out.println(sobreTiempo);
            System.out.println(suplementarias);
        }
            
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String image = AplicacionControl.class.getResource("imagenes/administracion.jpg").toExternalForm();
        imagenLabel.setStyle("-fx-background-image: url('" + image + "'); " 
                + "-fx-background-position: center top; " 
                + "-fx-background-repeat: stretch;");
        
        
    }    
    
}
