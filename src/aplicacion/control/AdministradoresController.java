/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.Administrador;
import hibernate.dao.IdentidadDAO;
import hibernate.dao.RolesDAO;
import hibernate.model.Empresa;
import hibernate.model.Identidad;
import hibernate.model.Roles;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class AdministradoresController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
   
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private TableView administradoresTable;
    
    private ObservableList<Administrador> data;
    
    ArrayList<Administrador> administradores;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    }
    
    @FXML
    private void agregarAdministrador(ActionEvent event) {
        aplicacionControl.mostrarRegistrarAdministrador();
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        administradoresTable.setEditable(Boolean.FALSE);
        administradoresTable.getColumns().clear(); 
        
        IdentidadDAO identidadDAO = new IdentidadDAO();
        RolesDAO rolesDAO = new RolesDAO();
        administradores = new ArrayList<>();
        ArrayList<Identidad> identidades = new ArrayList<>();
        identidades.addAll(identidadDAO.findAllActivo());
        
        for (Identidad ide: identidades) {
            Administrador administrador = new Administrador();
            administrador.setId(ide.getId());
            administrador.setUsuario(ide.getUsuario());
            administrador.setNombreUsuario(ide.getNombreUsuario());
            administrador.setContrasena(ide.getContrasena());
            administrador.setNombre(ide.getUsuario().getNombre());
            administrador.setApellido(ide.getUsuario().getApellido());
            administrador.setCedula(ide.getUsuario().getCedula());
            if (ide.getUsuario().getDetallesEmpleado() != null) {
                administrador.setNombreEmpresa(ide.getUsuario().getDetallesEmpleado()
                        .getEmpresa().getNombre());
            }
            String permisos = "";
            ArrayList<Roles> roles = new ArrayList<>();
            roles.addAll(rolesDAO.findAllByUsuarioId(administrador.getUsuario().getId()));
            int cantidad = roles.size();
            for (Roles rol: roles){
                String next;
                if (cantidad == 1) {
                    next = ".";
                } else {
                    next = ", ";
                }
                permisos = permisos + rol.getNombre() + next;
                cantidad --;
            }
            administrador.setPermisos(permisos);
            administradores.add(administrador);
        }
        
        if (!administradores.isEmpty()) {
           data = FXCollections.observableArrayList(); 
           data.addAll(administradores);
           administradoresTable.setItems(data);
        }
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn apellido = new TableColumn("Apellido");
        apellido.setMinWidth(100);
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        TableColumn cedula = new TableColumn("Cedula");
        cedula.setMinWidth(100);
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
     
        TableColumn nombreEmpresa = new TableColumn("Empresa");
        nombreEmpresa.setMinWidth(100);
        nombreEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        
        TableColumn nombreUsuario = new TableColumn("Usuario");
        nombreUsuario.setMinWidth(100);
        nombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        
        TableColumn permisos = new TableColumn("Permisos");
        permisos.setMinWidth(100);
        permisos.setCellValueFactory(new PropertyValueFactory<>("permisos"));
       
        administradoresTable.getColumns().addAll(cedula, nombre, apellido, nombreEmpresa, nombreUsuario, permisos);
        
        administradoresTable.setRowFactory( (Object tv) -> {
            TableRow<Administrador> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Administrador rowData = row.getItem();
                    //aplicacionControl.mostrarCliente(IdentidadDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
    } 
    
    // Login items
    @FXML
    public Button login;
    
    @FXML 
    public Label usuarioLogin;
    
    @FXML
    public void onClickLoginButton(ActionEvent event) {
        aplicacionControl.login(login, usuarioLogin);
    }
    
}
