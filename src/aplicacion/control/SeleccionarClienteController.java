/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.model.Cliente;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class SeleccionarClienteController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private TableView clientesTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML
    private TableColumn numeracionColumna;
    
    @FXML
    private TableColumn nombreColumna;
    
    @FXML
    private TableColumn detallesColumna;  
    
    private ObservableList<Cliente> data;
    
    List<Cliente> clientes;
    
    private HorarioEmpleadoController horarioEmpleadoController;
    
    private HorarioEmpleadoClienteController horarioEmpleadoClienteController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setHorarioEmpleadoController(HorarioEmpleadoController 
            horarioEmpleadoController) {
        this.horarioEmpleadoController = horarioEmpleadoController;
    }
    
    public void setHorarioEmpleadoController(HorarioEmpleadoClienteController 
            horarioEmpleadoClienteController) {
        this.horarioEmpleadoClienteController = horarioEmpleadoClienteController;
    }
  
    private void returnCliente(Cliente cliente) {
        stagePrincipal.close();
        if (horarioEmpleadoController != null)
            horarioEmpleadoController.setCliente(cliente);
        else if (horarioEmpleadoClienteController != null)
            horarioEmpleadoClienteController.setCliente(cliente);
    }
    
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
        
        data = FXCollections.observableArrayList(); 
        
        if (!clientes.isEmpty()) {
            data.addAll(clientes);
        }
                
        clientesTableView.setItems(data);
        
        FilteredList<Cliente> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cliente -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (cliente.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (cliente.getRuc().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<Cliente> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(clientesTableView.comparatorProperty());
        clientesTableView.setItems(sortedData);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        clientesTableView.setEditable(Boolean.FALSE);
        
        numeracionColumna.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        detallesColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        
        clientesTableView.setRowFactory( (Object tv) -> {
            TableRow<Cliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    returnCliente(row.getItem());
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
