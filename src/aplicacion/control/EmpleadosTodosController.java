/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
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
public class EmpleadosTodosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
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
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarConfiguracion();
    } 
    
    public void setDataToTable() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllEmpleadosActivosOrderByCedula());
        if (!usuarios.isEmpty()) {
           data = FXCollections.observableArrayList(); 
           usuarios.stream().map((user) -> {
               EmpleadoTable empleado = new EmpleadoTable();
               empleado.id.set(user.getId());
               empleado.nombre.set(user.getNombre());
               empleado.apellido.set(user.getApellido());
               empleado.cedula.set(user.getCedula());
               empleado.empresa.set(user.getDetallesEmpleado().getEmpresa().getNombre());
               empleado.telefono.set(user.getTelefono());
               empleado.departamento.set(user.getDetallesEmpleado().getDepartamento().getNombre());
               empleado.cargo.set(user.getDetallesEmpleado().getCargo().getNombre());
                return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
           empleadosTableView.setItems(data);
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
        
        TableColumn telefono = new TableColumn("Telefono");
        telefono.setMinWidth(100);
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        TableColumn empresa = new TableColumn("Empresa");
        empresa.setMinWidth(100);
        empresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        
        TableColumn departamento = new TableColumn("Departamento");
        departamento.setMinWidth(100);
        departamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        TableColumn cargo = new TableColumn("Cargo");
        cargo.setMinWidth(100);
        cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        empleadosTableView.getColumns().addAll(cedula, nombre, apellido, 
                telefono, empresa, departamento, cargo);
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    aplicacionControl.mostrarEmpleado(usuarioDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        empleadosTableView.getColumns().clear(); 
        setDataToTable();
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
