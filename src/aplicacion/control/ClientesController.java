/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.HibernateSessionFactory;
import hibernate.dao.ClienteDAO;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    
    public void deleteCliente(Cliente cliente) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Confirmación de borrado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));;
        Button buttonConfirmar = new Button("Si Borrar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Borrar el cliente " + cliente.getNombre()+ "?"), buttonConfirmar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        dialogStage.show();
        buttonConfirmar.setOnAction((ActionEvent e) -> {
            
            new ClienteDAO().findById(cliente.getId()).setActivo(Boolean.FALSE);
            HibernateSessionFactory.getSession().flush();
            data.remove(cliente);
            dialogStage.close();
            
            // Registro para auditar
            String detalles = "elimino el cliente " 
                    + cliente.getNombre();
            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
        });
    }
    
    public void clienteEditado(Cliente cliente) {
        int posicion = 0;
        for (Cliente cli: data) {
            if (cli.getId() == cliente.getId()) {
               posicion = data.indexOf(cli);
               break;
            }
        }
        data.set(posicion, cliente);
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
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(200);
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
        direccion.setMinWidth(200);
        direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        
        TableColumn<Cliente, Cliente> delete = new TableColumn<>("Borrar");
        delete.setMinWidth(40);
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);

                if (cliente == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteCliente(cliente);
                });
            }
        });
       
        clientesTableView.getColumns().addAll(nombre, detalles, numeracion, telefono, direccion, delete);
        
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
