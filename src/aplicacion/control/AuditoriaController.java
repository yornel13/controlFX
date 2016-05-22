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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private ChoiceBox accionesSelector;
    
    @FXML 
    private TextField filterField;
    
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
    private void onVerUsuarios(ActionEvent event) {
        filterField.clear();
        if (!accionesSelector.getSelectionModel().isEmpty()
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
        setFiltro();
    }
    
    public void setAccionesTipo() {
        Integer accionId = acciones.get(accionesSelector.getSelectionModel().getSelectedIndex()).getId();
        
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(registroAccionesDAO.findAllByAccionIdDesc(accionId));
        accionesTableView.setItems(data); 
        setFiltro();
    }
    
    public void setFiltro() {
       FilteredList<RegistroAcciones> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(acciones -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (acciones.getUsuario().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (acciones.getUsuario().getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (acciones.getDetalles().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<RegistroAcciones> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(accionesTableView.comparatorProperty());
        accionesTableView.setItems(sortedData); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        accionesTableView.setEditable(Boolean.FALSE);
        
        detalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        detalles.setMinWidth(620);
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        
        setAcciones();
        
        AccionTipoDAO accionTipoDAO = new AccionTipoDAO();
        acciones = new ArrayList<>();
        acciones.addAll(accionTipoDAO.findAll());
        
        String[] itemsAccion = new String[acciones.size() + 1];
        acciones.stream().forEach((accion) -> {
            System.out.println(accion.getNombre());
            itemsAccion[acciones.indexOf(accion)] = accion.getNombre();
        });
        itemsAccion[acciones.size()] = "TODO";
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