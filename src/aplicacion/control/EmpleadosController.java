/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.UsuariosDAO;
import hibernate.model.Empresa;
import hibernate.model.Usuarios;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EmpleadosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private TableView empleadosTableView;
    
    private ObservableList<Usuarios> data;
    
    ArrayList<Usuarios> usuarios;
    
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
    private void agregarEmpleado(ActionEvent event) {
        aplicacionControl.mostarRegistrarEmpleado(empresa);
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        UsuariosDAO usuariosDAO = new UsuariosDAO();
        usuarios = new ArrayList<>();
        for (Usuarios usuario: (ArrayList<Usuarios>) usuariosDAO.findAll()) {
            if (usuario.getDetallesEmpleado() != null && 
                    usuario.getDetallesEmpleado().getEmpresa().getId() == empresa.getId()) {
                usuarios.add(usuario);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        empleadosTableView.getColumns().clear();
        
        TableColumn nombre = new TableColumn("Nombres");
        TableColumn apellido = new TableColumn("Apellidos");
        TableColumn cedula = new TableColumn("Cedula");
        TableColumn telefono = new TableColumn("Telefono");
        TableColumn departamento = new TableColumn("Departamento");
        TableColumn cargo = new TableColumn("Cargo");
        TableColumn sueldo = new TableColumn("Sueldo");
        
        empleadosTableView.getColumns().addAll(nombre, apellido, cedula, 
                telefono, departamento, cargo, sueldo);
    }    
    
}
