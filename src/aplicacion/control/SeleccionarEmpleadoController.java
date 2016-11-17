/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class SeleccionarEmpleadoController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private TableView usuariosTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML
    private TableColumn cedulaColumna;
    
    @FXML
    private TableColumn apellidoColumna;
    
    @FXML
    private TableColumn nombreColumna;
    
    @FXML
    private TableColumn detallesColumna;  
    
    private ObservableList<Usuario> data;
    
    List<Usuario> usuarios;
    
    private RegistrarAdministradorController registrarAdministradorController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    void setParentController(RegistrarAdministradorController registrarAdministradorController) {
        this.registrarAdministradorController = registrarAdministradorController;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
  
    private void returnUsuario(Usuario usuario) {
        stagePrincipal.close();
        registrarAdministradorController.setEmpleado(usuario);
       
    }
    
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
        
        data = FXCollections.observableArrayList(); 
        
        if (!usuarios.isEmpty()) {
            data.addAll(usuarios);
        }
                
        usuariosTableView.setItems(data);
        
        FilteredList<Usuario> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(usuario -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (usuario.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (usuario.getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (usuario.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<Usuario> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usuariosTableView.comparatorProperty());
        usuariosTableView.setItems(sortedData);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        
        usuariosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        usuariosTableView.setRowFactory( (Object tv) -> {
            TableRow<Usuario> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    returnUsuario(row.getItem());
                }
            });
            return row ;
        });
    }   
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

}
