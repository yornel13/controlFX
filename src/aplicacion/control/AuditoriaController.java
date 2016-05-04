/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.AccionTipoDAO;
import hibernate.dao.RegistroAccionesDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.AccionTipo;
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
    
    private ArrayList<AccionTipo> acciones;
   
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
    private ChoiceBox accionesSelector;
    
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
        if (!usuariosSelector.getSelectionModel().isEmpty() 
                && checkBoxUsuarios.isSelected() 
                && !accionesSelector.getSelectionModel().isEmpty()
                && accionesSelector.getSelectionModel().getSelectedIndex() != acciones.size()) {
            setAccionesUsuarioYTipo();
        } else if (!usuariosSelector.getSelectionModel().isEmpty() && checkBoxUsuarios.isSelected()) {
            setAccionesUsuario();
        } else if (!accionesSelector.getSelectionModel().isEmpty()
                && accionesSelector.getSelectionModel().getSelectedIndex() != acciones.size()) {
            setAccionesTipo();
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
    
    public void setAccionesTipo() {
        Integer accionId = acciones.get(accionesSelector.getSelectionModel().getSelectedIndex()).getId();
        
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(registroAccionesDAO.findAllByAccionIdDesc(accionId));
        accionesTableView.setItems(data); 
    }
    
    public void setAccionesUsuarioYTipo() {
        Integer usuarioId = usuarios.get(usuariosSelector.getSelectionModel().getSelectedIndex()).getId();
        Integer accionId = acciones.get(accionesSelector.getSelectionModel().getSelectedIndex()).getId();
        
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(registroAccionesDAO.findAllDescByUsuarioIdAndAccionId(usuarioId, accionId));
        accionesTableView.setItems(data); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        accionesTableView.setEditable(Boolean.FALSE);
        
        detalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        detalles.setMinWidth(620);
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
        
        AccionTipoDAO accionTipoDAO = new AccionTipoDAO();
        acciones = new ArrayList<>();
        acciones.addAll(accionTipoDAO.findAll());
        
        String[] itemsAccion = new String[acciones.size() + 1];
        acciones.stream().forEach((accion) -> {
            System.out.println(accion.getNombre());
            itemsAccion[acciones.indexOf(accion)] = accion.getNombre();
        });
        
        accionesSelector.setItems(FXCollections.observableArrayList(itemsAccion));
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