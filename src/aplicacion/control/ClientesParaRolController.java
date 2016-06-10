/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.ClienteDAO;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
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
public class ClientesParaRolController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    @FXML
    private TableColumn columnaNumeracion;
    
    @FXML
    private TableColumn columnaNombre;
    
    @FXML
    private TableColumn columnaDetalles;
    
    @FXML
    private TableColumn columnaTelefono;
    
    @FXML
    private TableColumn columnaDireccion;
   
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private TableView clientesTableView;
    
    private ObservableList<Cliente> data;
    
    ArrayList<Cliente> clientes;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void mostrarEmpleadosSinCliente(ActionEvent event) {
        aplicacionControl.mostrarHorasEmpleadosSinCliente(empresa);
    }
    
    @FXML
    private void agregarCliente(ActionEvent event) {
        aplicacionControl.mostrarRegistrarCliente();
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        clientesTableView.setEditable(Boolean.FALSE);
        clientesTableView.getColumns().clear(); 
        
        ClienteDAO clientesDAO = new ClienteDAO();
        clientes = new ArrayList<>();
        clientes.addAll(clientesDAO.findAllActivo());
        
        if (!clientes.isEmpty()) {
           data = FXCollections.observableArrayList(); 
           data.addAll(clientes);
           clientesTableView.setItems(data);
        }
        
        columnaNumeracion.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        columnaDetalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));
     
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
       
        clientesTableView.getColumns().addAll(columnaNumeracion, columnaNombre, 
                columnaDetalles, columnaTelefono, columnaDireccion);
        
        clientesTableView.setRowFactory( (Object tv) -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Cliente rowData = row.getItem();
                    stagePrincipal.close();
                    aplicacionControl.mostrarRolCliente(empresa, 
                            clientesDAO.findById(rowData.getId()), stagePrincipal);
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

    void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}