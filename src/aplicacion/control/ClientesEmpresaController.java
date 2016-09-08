/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Roboto;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class ClientesEmpresaController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    @FXML
    private TableColumn numeracionColumna;
    
    @FXML
    private TableColumn nombreColumna;
    
    @FXML
    private TableColumn direccionColumna;
    
    @FXML
    private TableColumn telefonoColumna;
    
    @FXML
    private TableColumn detallesColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonAdministrativo;
    
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
        aplicacionControl.mostrarHorasEmpleadosCliente(empresa, null, stagePrincipal);
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
        
        ClienteDAO clientesDAO = new ClienteDAO();
        clientes = new ArrayList<>();
        clientes.addAll(clientesDAO.findAllActivo());
        
        data = FXCollections.observableArrayList(); 
        data.addAll(clientes);
        clientesTableView.setItems(data);

        numeracionColumna.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        telefonoColumna.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        direccionColumna.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        
        detallesColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        
        clientesTableView.setRowFactory( (Object tv) -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Cliente rowData = row.getItem();
                    aplicacionControl.mostrarHorasEmpleadosCliente(empresa, rowData, stagePrincipal);
                }
            });
            return row ;
        });
        
        buttonAtras.setOnMouseEntered((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAtras.setOnMouseExited((MouseEvent t) -> {
            buttonAtras.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/atras.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        {
            Image imageGuardia = new Image(getClass()
                    .getResourceAsStream("imagenes/bt_administrativo.png"));
            ImageView imageView = new ImageView(imageGuardia);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            buttonAdministrativo.setGraphic(imageView);
            buttonAdministrativo.setFont(Roboto.MEDIUM(15));
            buttonAdministrativo.setOnMouseEntered((MouseEvent t) -> {
                buttonAdministrativo.setStyle("-fx-background-color: #E0E0E0;");
            });
            buttonAdministrativo.setOnMouseExited((MouseEvent t) -> {
                buttonAdministrativo.setStyle("-fx-background-color: #039BE5;");
            });
        }
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
