/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpresaTable;
import hibernate.dao.EmpresaDAO;
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
public class EmpresasController implements Initializable {
    
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
    private TableView empresasTableView;
    
    private ObservableList<EmpresaTable> data;
    
    ArrayList<Empresa> empresas;
    
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
    private void agregarEmpresa(ActionEvent event) {
        aplicacionControl.mostrarRegistrarEmpresa();
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empresasTableView.setEditable(Boolean.FALSE);
        empresasTableView.getColumns().clear(); 
        
        EmpresaDAO empresaDAO = new EmpresaDAO();
        empresas = new ArrayList<>();
        empresas.addAll(empresaDAO.findAll());
        
        if (!empresas.isEmpty()) {
           data = FXCollections.observableArrayList(); 
           for (Empresa emp: empresas) {
               EmpresaTable empresa = new EmpresaTable();
               empresa.id.set(emp.getId());
               empresa.nombre.set(emp.getNombre());
               empresa.siglas.set(emp.getSiglas());
               empresa.numeracion.set(emp.getNumeracion().toString());
               empresa.diaCortePago.set(emp.getDiaCortePago());
               empresa.tipo.set(emp.getTipo());
               data.add(empresa);
           }
           empresasTableView.setItems(data);
        }
        
        TableColumn siglas = new TableColumn("Siglas");
        siglas.setMinWidth(100);
        siglas.setCellValueFactory(new PropertyValueFactory<>("siglas"));
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
     
        TableColumn numeracion = new TableColumn("Numeracion");
        numeracion.setMinWidth(100);
        numeracion.setCellValueFactory(new PropertyValueFactory<>("numeracion"));
        
        TableColumn diaCortePago = new TableColumn("Dia de corte");
        diaCortePago.setMinWidth(100);
        diaCortePago.setCellValueFactory(new PropertyValueFactory<>("diaCortePago"));
        
        TableColumn tipo = new TableColumn("Tipo");
        tipo.setMinWidth(100);
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        
        empresasTableView.getColumns().addAll(siglas, nombre, numeracion, diaCortePago, tipo);
        
        empresasTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpresaTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpresaTable rowData = row.getItem();
                    aplicacionControl.mostrarEmpresa(empresaDAO.findById(rowData.getId()));
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
