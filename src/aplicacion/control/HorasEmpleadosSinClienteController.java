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
import hibernate.model.Cliente;
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
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class HorasEmpleadosSinClienteController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    private Cliente cliente;
    
    @FXML
    private TableColumn cedulaColumna;
    
    @FXML
    private TableColumn empleadoColumna;
    
    @FXML
    private TableColumn cargoColumna;
    
    @FXML
    private TableColumn diasColumna;
    
    @FXML
    private TableColumn normalesColumna;
    
    @FXML
    private TableColumn sobretiempoColumna;
    
    @FXML
    private TableColumn recargoColumna;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField filterField;
    
    private Timestamp inicio;
    private Timestamp fin;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Stage stagePrincipal;
    
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
    private void agregarEmpleado(ActionEvent event) {
        //aplicacionControl.mostrarRegistrarEmpleado(empresa);
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarInEmpresa(empresa);
        stagePrincipal.close();
    } 
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().plusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        setTableInfo(empresa, inicio, fin);
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().minusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        setTableInfo(empresa, inicio, fin);
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        DateTime dateTime = new DateTime(getToday().getTime());
           
        fin = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).getMillis());
        inicio = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).minusMonths(1).plusDays(1).getMillis());
        
        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        setTableInfo(empresa, inicio, fin);
        
    }
    
    private void rolDePagoSinCliente(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
              
               aplicacionControl.mostrarRolDePagoSinCliente(empleado, inicio, fin);
                  
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
        
        if (!usuarios.isEmpty()) {
            
           ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
           data = FXCollections.observableArrayList(); 
           
           usuarios.stream().map((user) -> {
               Integer dias = 0;
               Double normales = 0d;
               Double sobreTiempo = 0d;
               Double suplementarias = 0d;
               for (ControlEmpleado control: controlDAO
                       .findAllByEmpleadoIdSinClienteInDeterminateTime(user.getId(),
                               inicio, fin)) {
                   dias = dias + 1;
                   normales = normales + 8;
                   sobreTiempo = sobreTiempo + control.getHorasExtras();
                   suplementarias = suplementarias + control.getHorasSuplementarias();
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
                empleado.setSobreTiempo(sobreTiempo);
                empleado.setSuplementarias(suplementarias);
                return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
           empleadosTableView.setItems(data);
        }
        
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
        
        pickerDe.setEditable(false);
        pickerHasta.setEditable(false);
        
        buttonAnterior.setTooltip(
            new Tooltip("Mes Anterior")
        );
        buttonAnterior.setOnMouseEntered((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonAnterior.setOnMouseExited((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #039BE5;");
        });
        buttonSiguiente.setTooltip(
            new Tooltip("Mes Siguiente")
        );
        buttonSiguiente.setOnMouseEntered((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonSiguiente.setOnMouseExited((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #039BE5;");
        });
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        empleadoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getApellido() 
                        + " " + data.getValue().getNombre());
            }
        });
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        diasColumna.setCellValueFactory(new PropertyValueFactory<>("dias"));
        
        normalesColumna.setCellValueFactory(new PropertyValueFactory<>("horas"));
        
        recargoColumna.setCellValueFactory(new PropertyValueFactory<>("suplementarias"));
        
        sobretiempoColumna.setCellValueFactory(new PropertyValueFactory<>("sobreTiempo"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    rolDePagoSinCliente(new UsuarioDAO()
                            .findById(rowData.getId()));
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
