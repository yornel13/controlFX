/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.ClienteDAO;
import hibernate.dao.RegistroAccionesDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
import hibernate.model.Pago;
import hibernate.model.RegistroAcciones;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class AuditoriaController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private ArrayList<Usuario> usuarios;
   
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private TableView accionesTableView;
    
    @FXML
    private ChoiceBox usuariosSelector;
    
    @FXML
    private CheckBox checkBoxUsuarios;
    
    @FXML
    private TableColumn detalles;
    
    @FXML
    private TableColumn fecha;
    
    private ObservableList<RegistroAcciones> data;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void onCheckUsuarios(ActionEvent event) {
        if (checkBoxUsuarios.isSelected()) {
            usuariosSelector.setDisable(false);
        } else {
            usuariosSelector.setDisable(true);
        }
    }
    
    @FXML
    private void onVerUsuarios(ActionEvent event) {
        if (!usuariosSelector.getSelectionModel().isEmpty() && checkBoxUsuarios.isSelected()) {
            setAccionesUsuario();
        } else {
            setAcciones();
        }
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarConfiguracion();
    } 
    
    public void setAcciones() {
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(registroAccionesDAO.findAllDesc());
        accionesTableView.setItems(data); 
    }
    
    public void setAccionesUsuario() {
        Integer usuarioId = usuarios.get(usuariosSelector.getSelectionModel().getSelectedIndex()).getId();
        
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(registroAccionesDAO.findAllDescByUsuarioId(usuarioId));
        accionesTableView.setItems(data); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        accionesTableView.setEditable(Boolean.FALSE);
        
        detalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        detalles.setMaxWidth(620);
        detalles.setMinWidth(620);
        detalles.setPrefWidth(620);
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        
        
        setAcciones();
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAll());
        
        String[] items = new String[usuarios.size()];
        usuarios.stream().forEach((user) -> {
            items[usuarios.indexOf(user)] = user.getNombre() + " " + user.getApellido();
        });
        
        usuariosSelector.setItems(FXCollections.observableArrayList(items));
        usuariosSelector.setDisable(true);
        
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
