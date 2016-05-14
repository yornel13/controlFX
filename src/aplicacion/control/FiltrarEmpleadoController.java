/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class FiltrarEmpleadoController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
   
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML
    private TableColumn cedulaColumna;
    
    @FXML
    private TableColumn nombreColumna;
    
    @FXML
    private TableColumn apellidoColumna;
    
    @FXML
    private TableColumn departamentoColumna;
    
    @FXML
    private TableColumn cargoColumna;    
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    private PagosTotalEmpleadoController pagosController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setPagosController(PagosTotalEmpleadoController pagosController) {
        this.pagosController = pagosController;
    }
  
    private void returnEmpleado(Usuario empleado) {
        stagePrincipal.close();
        pagosController.setEmpleado(empleado);
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
       
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findByEmpresaId(empresa.getId()));
        if (!usuarios.isEmpty()) {
            usuarios.stream().map((user) -> {
               EmpleadoTable empleado = new EmpleadoTable();
               empleado.id.set(user.getId());
               empleado.nombre.set(user.getNombre());
               empleado.apellido.set(user.getApellido());
               empleado.cedula.set(user.getCedula());
               empleado.empresa.set(user.getDetallesEmpleado().getEmpresa().getNombre());
               empleado.telefono.set(user.getTelefono());
               empleado.departamento.set(user.getDetallesEmpleado().getDepartamento().getNombre());
               empleado.cargo.set(user.getDetallesEmpleado().getCargo().getNombre());
              
               return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
        }
        empleadosTableView.setItems(data);
        
        FilteredList<EmpleadoTable> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(empleado -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (empleado.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (empleado.getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getCedula().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getDepartamento().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getCargo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<EmpleadoTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(empleadosTableView.comparatorProperty());
        empleadosTableView.setItems(sortedData);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        data = FXCollections.observableArrayList(); 
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    returnEmpleado(new UsuarioDAO().findById(rowData.getId()));
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
