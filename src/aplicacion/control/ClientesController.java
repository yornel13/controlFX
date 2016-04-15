/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpresaTable;
import hibernate.dao.ClienteDAO;
import hibernate.dao.EmpresaDAO;
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
public class ClientesController implements Initializable {
    
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
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    }
    
    @FXML
    private void agregarCliente(ActionEvent event) {
        aplicacionControl.mostrarRegistrarCliente();
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        clientesTableView.setEditable(Boolean.FALSE);
        clientesTableView.getColumns().clear(); 
        
        ClienteDAO clientesDAO = new ClienteDAO();
        clientes = new ArrayList<>();
        clientes.addAll(clientesDAO.findAll());
        
        if (!clientes.isEmpty()) {
           data = FXCollections.observableArrayList(); 
           data.addAll(clientes);
           clientesTableView.setItems(data);
        }
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn detalles = new TableColumn("Detalles");
        detalles.setMinWidth(100);
        detalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));
     
        TableColumn numeracion = new TableColumn("Numeracion");
        numeracion.setMinWidth(100);
        numeracion.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        
        TableColumn telefono = new TableColumn("Telefono");
        telefono.setMinWidth(100);
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        TableColumn direccion = new TableColumn("Direccion");
        direccion.setMinWidth(100);
        direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
       
        clientesTableView.getColumns().addAll(nombre, detalles, numeracion, telefono, direccion);
        
        clientesTableView.setRowFactory( (Object tv) -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Cliente rowData = row.getItem();
                    aplicacionControl.mostrarCliente(clientesDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
    }    
    
}
