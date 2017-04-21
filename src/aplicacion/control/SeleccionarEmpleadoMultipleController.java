/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Roboto;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Yornel
 */
public class SeleccionarEmpleadoMultipleController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private Button buttonOk;
    
    @FXML
    private CheckBox checkBoxMarcarTodos;
    
    @FXML
    private Label contador;
    
    @FXML
    private TextField filterField;
    
    @FXML
    private TableColumn cedulaColumna;
    
    @FXML
    private TableColumn apellidoColumna;
    
    @FXML
    private TableColumn nombreColumna;
    
    @FXML
    private TableColumn cargoColumna; 
    
    @FXML
    private TableColumn<EmpleadoTable, EmpleadoTable> marcarColumna; 
    
    private ObservableList<EmpleadoTable> data;
    
    List<Usuario> usuarios;
    
    private RegistrarAdministradorController registrarAdministradorController;
    private AsignarHorariosController asignarHorariosController;
    private AsignarHorasExtrasController asignarHorasExtrasController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    void setParentController(RegistrarAdministradorController registrarAdministradorController) {
        this.registrarAdministradorController = registrarAdministradorController;
    }
    
    void setParentController(AsignarHorariosController asignarHorariosController) {
        this.asignarHorariosController = asignarHorariosController;
    }
    
    void setParentController(AsignarHorasExtrasController asignarHorasExtrasController) {
        this.asignarHorasExtrasController = asignarHorasExtrasController;
    }
    
    @FXML
    public void onSave(ActionEvent event) {
        stagePrincipal.close();
        List<Usuario> empleados = new ArrayList<>();
        
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleados.add(empleado.getUsuario());
        }
        if (asignarHorasExtrasController != null)
            asignarHorasExtrasController.setEmpleado(empleados);
        else
            asignarHorariosController.setEmpleado(empleados);
    }
    
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
        
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.setId(user.getId());
            empleado.setNombre(user.getNombre());
            empleado.setApellido(user.getApellido());
            empleado.setCedula(user.getCedula());
            empleado.setEmpresa(user.getDetallesEmpleado().getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
            empleado.setUsuario(user);
            empleado.setAgregar(false);
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);
        
        FilteredList<EmpleadoTable> filteredData = new FilteredList<>(data, p -> true);
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
                } else if (usuario.getDepartamento().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (usuario.getCargo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<EmpleadoTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(empleadosTableView.comparatorProperty());
        empleadosTableView.setItems(sortedData);
    }
    
    @FXML
    public void marcarTodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxMarcarTodos.isSelected()) {
                empleadoTable.setAgregar(true);
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
        contarSelecciones();
    }
    
    public void contarSelecciones() {
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        int count = empleadosTable.size();
        switch (count) {
            case 0:
                contador.setText("Marque los empleados deseados y luego precione ok");
                break;
            case 1:
                contador.setText("1 empleado seleccionado");
                break;
            default:
                contador.setText(count + " empleados seleccionados");
                break;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        empleadosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxSeleccionar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxSeleccionar);
                if (checkBoxSeleccionar != null) {
                    checkBoxSeleccionar.setSelected(empleadoTable.getAgregar());
                }
                checkBoxSeleccionar.setOnAction(event -> {
                     empleadoTable.setAgregar(checkBoxSeleccionar.isSelected());
                     contarSelecciones();
                });
            }
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    row.getItem().setAgregar(!row.getItem().getAgregar());
                    data.set(data.indexOf(row.getItem()), row.getItem());
                }
            });
            return row ;
        });
        
        {
            buttonOk.setFont(Roboto.MEDIUM(12));
            buttonOk.setOnMouseEntered((MouseEvent t) -> {
                buttonOk.setStyle("-fx-background-color: #0390E5;");
            });
            buttonOk.setOnMouseExited((MouseEvent t) -> {
                buttonOk.setStyle("-fx-background-color: #E0E0E0;");
            });
        }
    } 

}
