/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Numeros;
import hibernate.dao.DeudaDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EmpleadosParaRolController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
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
    
    @FXML
    private Button buttonAtras;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    public void empleadoEditado(Usuario user) {
        for (EmpleadoTable empleadoTable: data) {
            if(Objects.equals(empleadoTable.getId(), user.getId())) {
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
               ArrayList<Deuda> deudas = new ArrayList<>();
               deudas.addAll(new DeudaDAO().findAllByEmpleadoId(user.getId()));
               Double montoDeuda = 0d;
               for (Deuda deuda: deudas) {
                   montoDeuda += deuda.getRestante();
               }
               empleado.setTotalMontoDeudas(montoDeuda);
               data.set(data.indexOf(empleadoTable), empleado);
               return;
            }
        }
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivo(empresa.getId()));
        
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
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
            ArrayList<Deuda> deudas = new ArrayList<>();
            deudas.addAll(new DeudaDAO().findAllByEmpleadoId(user.getId()));
            Double montoDeuda = 0d;
            Integer cantidad = 0;
            for (Deuda deuda: deudas) {
                montoDeuda += deuda.getRestante();
                if (!deuda.getPagada()) {
                    cantidad ++;
               }
            }
           empleado.setTotalDeudas(cantidad);
           empleado.setTotalMontoDeudas(Numeros.round(montoDeuda));
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
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
                    stagePrincipal.close();
                    aplicacionControl.mostrarRolIndividual(empresa, 
                            new UsuarioDAO().findById(rowData.getId()), stagePrincipal);
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
