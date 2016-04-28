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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.joda.time.DateTime;

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
    private void agregarEmpleado(ActionEvent event) {
        //aplicacionControl.mostrarRegistrarEmpleado(empresa);
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarInEmpresa(empresa);
        stagePrincipal.close();
    } 
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        DateTime dateTime = new DateTime(getToday().getTime());
           
        Timestamp fin = new Timestamp(dateTime.withDayOfMonth(24).getMillis());
        Timestamp inicio = new Timestamp(dateTime.withDayOfMonth(24).minusMonths(1).plusDays(1).getMillis());
        
        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        setTableInfo(empresa, inicio, fin);
        
    }
    
    @FXML
    private void mostrarRegistro(ActionEvent event) {
        if (pickerDe.getValue() == null) {
            // error
        } else if (pickerHasta.getValue() == null) {
            // error 
        } else if (pickerDe.getValue().isAfter(pickerHasta.getValue())){
            // error
        } else {       
            Timestamp fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
            Timestamp inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
            setTableInfo(empresa, inicio, fin);
        }
    } 
    
    private void rolDePagoPorEmpleado(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_ROL_DE_PAGO, Permisos.Nivel.VER)) {
               
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
            
                Integer dias = 0;
                Integer normales = 0;
                Integer sobreTiempo = 0;
                Integer suplementarias = 0;
                
                for (ControlEmpleado control: controlDAO.findAllByEmpleadoIdInDeterminateTime(user.getId(), inicio, fin)) {
                    dias = dias + 1;
                    normales = normales + 8;
                    sobreTiempo = sobreTiempo + control.getHorasExtras();
                    suplementarias = suplementarias + control.getHorasSuplementarias();
                }
               
               EmpleadoTable empleado = new EmpleadoTable();
               empleado.id.set(user.getId());
               empleado.nombre.set(user.getNombre());
               empleado.apellido.set(user.getApellido());
               empleado.cedula.set(user.getCedula());
               empleado.cargo.set(user.getDetallesEmpleado().getCargo().getNombre());
               empleado.dias.set(dias);
               empleado.horas.set(normales);
               empleado.sobreTiempo.set(sobreTiempo);
               empleado.suplementarias.set(suplementarias);
               
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
        
        
        empleadosTableView.getColumns().addAll(cedula, nombre, apellido, cargo, dias, horas, sobreTiempo, suplementarias);
        
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
