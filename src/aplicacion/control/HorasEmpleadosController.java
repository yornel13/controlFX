/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.Permisos;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.ControlEmpleado;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import static aplicacion.control.util.Numeros.round;

/**
 *
 * @author Yornel
 */
public class HorasEmpleadosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
   
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Label textFecha;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField cedulaField;
    
    @FXML
    private Label errorText;
    
    @FXML
    private TextField filterField;
    
    private Timestamp inicio;
    private Timestamp fin;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarVentanaPrincipal();
        stagePrincipal.close();
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        mostrarRegistro(null);
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        mostrarRegistro(null);
    }
    
     public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        DateTime dateTime = new DateTime(getToday().getTime());
        
        inicio = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                .getMillis());
        fin = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                .plusMonths(1).minusDays(1).getMillis());
        
        pickerDe.setValue(Fechas.getLocalFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getLocalFromTimestamp(fin));
        
        setTableInfo(empresa, inicio, fin);
        
    }
    
    @FXML
    private void verRol(ActionEvent event) {
        if (cedulaField != null) {
            Usuario emp = new UsuarioDAO().findByCedulaAndEmpresaId(cedulaField.getText(), empresa.getId());
            if (emp != null) {
                errorText.setText("");
                rolDePagoPorEmpleado(emp);
            } else {
                errorText.setText("No encontrado");
            }
        } else {
            errorText.setText("No encontrado");
        }
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarInEmpresa(empresa);
        stagePrincipal.close();
    } 
    
    @FXML
    private void mostrarRegistro(ActionEvent event) {
        if (pickerDe.getValue() == null) {
            errorText.setText("Fechas incorrectas");
        } else if (pickerHasta.getValue() == null) {
            errorText.setText("Fechas incorrectas");
        } else if (pickerDe.getValue().isAfter(pickerHasta.getValue())){
            errorText.setText("Fechas incorrectas");
        } else {  
            fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
            inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
            setTableInfo(empresa, inicio, fin);
        }
    } 
    
    private void rolDePagoPorEmpleado(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
               
               aplicacionControl.mostrarRolDePago(empleado, inicio, fin);
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void setTableInfo(Empresa empresa, Timestamp inicio, Timestamp fin) {
        this.inicio = inicio;
        this.fin = fin;
        UsuarioDAO usuariosDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuariosDAO.findByEmpresaId(empresa.getId()));
        
        empleadosTableView.getColumns().clear(); 
        
        if (!usuarios.isEmpty()) {
            
           ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
           data = FXCollections.observableArrayList(); 
           
           for (Usuario user: usuarios) {
            
                Double dias = 0d;
                Double normales = 0d;
                Double sobreTiempo = 0d;
                Double suplementarias = 0d;
                
                for (ControlEmpleado control: controlDAO
                        .findAllByEmpleadoIdInDeterminateTime(user.getId(), inicio, fin)) {
                    /*if (!control.getFalta()) {
                        dias = dias + 1;
                        normales = normales + 8d;
                        sobreTiempo = sobreTiempo + control.getSobretiempo();
                        suplementarias = suplementarias + control.getRecargo();
                    }*/
                }
               
                EmpleadoTable empleado = new EmpleadoTable();
                empleado.setId(user.getId());
                empleado.setNombre(user.getNombre());
                empleado.setApellido(user.getApellido());
                empleado.setCedula(user.getCedula());
                empleado.setEmpresa(user.getDetallesEmpleado()
                        .getEmpresa().getNombre());
                empleado.setTelefono(user.getTelefono());
                empleado.setDepartamento(user.getDetallesEmpleado()
                        .getDepartamento().getNombre());
                empleado.setCargo(user.getDetallesEmpleado()
                        .getCargo().getNombre());
               empleado.setDias(dias);
               empleado.setHoras(normales);
               empleado.setSobreTiempo(round(sobreTiempo));
               empleado.setSuplementarias(round(suplementarias));
               
               data.add(empleado);
           }
           empleadosTableView.setItems(data);
        }
        
        TableColumn cedula = new TableColumn("Cedula");
        cedula.setMinWidth(100);
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        TableColumn apellido = new TableColumn("Apellido");
        apellido.setMinWidth(100);
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        TableColumn cargo = new TableColumn("Cargo");
        cargo.setMinWidth(100);
        cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        TableColumn dias = new TableColumn("Dias");
        dias.setMinWidth(100);
        dias.setCellValueFactory(new PropertyValueFactory<>("dias"));
        
        TableColumn horas = new TableColumn("Horas(N)");
        horas.setMinWidth(100);
        horas.setCellValueFactory(new PropertyValueFactory<>("horas"));
        
        TableColumn suplementarias = new TableColumn("Horas(RC)");
        suplementarias.setMinWidth(100);
        suplementarias.setCellValueFactory(new PropertyValueFactory<>("suplementarias"));
        
        TableColumn sobreTiempo = new TableColumn("Horas(ST)");
        sobreTiempo.setMinWidth(100);
        sobreTiempo.setCellValueFactory(new PropertyValueFactory<>("sobreTiempo"));
        
        
        empleadosTableView.getColumns().addAll(cedula, apellido, nombre, cargo, dias, horas, sobreTiempo, suplementarias);
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    System.out.println(inicio);
                    rolDePagoPorEmpleado(usuariosDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
        
        
        filtro();
    }
    
    public void filtro() {
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
        empleadosTableView.getColumns().clear(); 
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
