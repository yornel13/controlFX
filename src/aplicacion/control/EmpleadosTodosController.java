/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.PagoQuincenaDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EmpleadosTodosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private Button buttonAtras;
    
    @FXML 
    private TableColumn cedulaColumna;
    
    @FXML 
    private TableColumn nombreColumna;
    
    @FXML 
    private TableColumn apellidoColumna;
    
    @FXML 
    private TableColumn telefonoColumna;
    
    @FXML 
    private TableColumn empresaColumna;
    
    @FXML 
    private TableColumn cargoColumna;
    
    @FXML
    private TableColumn<EmpleadoTable, EmpleadoTable>  borrarColumna;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField filterField;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarConfiguracion();
    } 
    
    public void deleteEmpleado(EmpleadoTable empleadoTable) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.EMPLEADOS, Permisos.Nivel.ELIMINAR)) {
                
                if (empleadoTable.getActivo()) {
                
                    Boolean borrar = true;

                    if (!new RolClienteDAO()  // Si no tiene roles cliente
                            .findAllByUsuarioId(empleadoTable.getId()).isEmpty()) {
                        borrar = false;
                    }
                    if (!new RolIndividualDAO()  // Si no tiene roles individuales
                            .findAllByEmpleadoId(empleadoTable.getId()).isEmpty()) {
                        borrar = false;
                    }
                    if (!new ControlEmpleadoDAO()  // Si no tiene horas trabajadas
                            .findAllByEmpleadoId(empleadoTable.getId()).isEmpty()) {
                        borrar = false;
                    }
                    if (!new DeudaDAO()   // Si no tiene Deudas
                            .findAllByEmpleadoId(empleadoTable.getId()).isEmpty()) {
                        borrar = false;
                    }
                    if (!new PagoQuincenaDAO()   // Si no tiene Deudas
                            .findAllByUsuarioId(empleadoTable.getId()).isEmpty()) {
                        borrar = false;
                    }

                    if (borrar) { 

                        Stage dialogStage = new Stage();
                        dialogStage.initModality(Modality.APPLICATION_MODAL);
                        dialogStage.setResizable(false);
                        dialogStage.setTitle("Confirmación de borrado");
                        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
                        dialogStage.getIcons().add(new Image(stageIcon));;
                        Button buttonConfirmar = new Button("Si Borrar");
                        Button buttonCancelar = new Button("No, no estoy seguro");
                        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                        children(new Text("¿Borrar el empleado " + empleadoTable.getApellido()+ "?"), 
                                buttonConfirmar, buttonCancelar).
                        alignment(Pos.CENTER).padding(new Insets(25)).build()));
                        buttonConfirmar.setOnAction((ActionEvent e) -> {

                            new UsuarioDAO().delete(new UsuarioDAO()
                                    .findById(empleadoTable.getId()));
                            HibernateSessionFactory.getSession().flush();
                            data.remove(empleadoTable);
                            dialogStage.close();

                            // Registro para auditar
                            String detalles = "elimino el empleado " 
                                    + empleadoTable.getApellido() + " " 
                                    + empleadoTable.getNombre();
                            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                        });
                        buttonCancelar.setOnAction((ActionEvent e) -> {
                           dialogStage.close();
                        });
                        dialogStage.showAndWait();

                    } else {
                        Stage dialogStage = new Stage();
                        dialogStage.initModality(Modality.APPLICATION_MODAL);
                        dialogStage.setResizable(false);
                        dialogStage.setTitle("Confirmación de borrado");
                        String stageIcon = AplicacionControl.class
                                .getResource("imagenes/icon_error.png").toExternalForm();
                        dialogStage.getIcons().add(new Image(stageIcon));
                        MaterialDesignButton buttonOk = new MaterialDesignButton("Si, desactivar");
                        MaterialDesignButton buttonNo = new MaterialDesignButton("no");
                        HBox hBox = HBoxBuilder.create()
                                .spacing(10.0) //In case you are using HBoxBuilder
                                .padding(new Insets(5, 5, 5, 5))
                                .alignment(Pos.CENTER)
                                .children(buttonOk, buttonNo)
                                .build();
                        hBox.maxWidth(120);
                        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                        children(new Text("No se puede borrar el empleado porque tiene roles generados."),
                                 new Text("¿Desea desactivar el empleado?"),
                                 new Text("Los empleados desactivados no aparecen en gestiones, horas, pagos, etc."), hBox).
                        alignment(Pos.CENTER).padding(new Insets(20)).build()));
                        buttonOk.setMinWidth(50);
                        buttonNo.setMinWidth(50);
                        buttonOk.setOnAction((ActionEvent e) -> {

                            new UsuarioDAO().findById(empleadoTable.getId())
                                    .setActivo(Boolean.FALSE);
                            HibernateSessionFactory.getSession().flush();
                            empleadoTable.setActivo(false);
                            data.set(data.indexOf(empleadoTable), empleadoTable);
                            dialogStage.close();

                            // Registro para auditar
                            String detalles = "desactivo el empleado " 
                                    + empleadoTable.getApellido() + " " 
                                    + empleadoTable.getNombre();
                            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());

                            dialogStage.close();
                        });
                        buttonNo.setOnAction((ActionEvent e) -> {
                            dialogStage.close();
                        });
                        dialogStage.showAndWait();
                    }
                } else {
                    Stage dialogStage = new Stage();
                        dialogStage.initModality(Modality.APPLICATION_MODAL);
                        dialogStage.setResizable(false);
                        dialogStage.setTitle("Confirmación de Activación");
                        String stageIcon = AplicacionControl.class
                                .getResource("imagenes/icon_error.png").toExternalForm();
                        dialogStage.getIcons().add(new Image(stageIcon));
                        MaterialDesignButton buttonOk = new MaterialDesignButton("Si, activar");
                        MaterialDesignButton buttonNo = new MaterialDesignButton("no");
                        HBox hBox = HBoxBuilder.create()
                                .spacing(10.0) //In case you are using HBoxBuilder
                                .padding(new Insets(5, 5, 5, 5))
                                .alignment(Pos.CENTER)
                                .children(buttonOk, buttonNo)
                                .build();
                        hBox.maxWidth(120);
                        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                        children(new Text("¿Seguro que desea activar este empleado?"), hBox).
                        alignment(Pos.CENTER).padding(new Insets(20)).build()));
                        buttonOk.setMinWidth(50);
                        buttonNo.setMinWidth(50);
                        buttonOk.setOnAction((ActionEvent e) -> {

                            new UsuarioDAO().findById(empleadoTable.getId())
                                    .setActivo(Boolean.TRUE);
                            HibernateSessionFactory.getSession().flush();
                            empleadoTable.setActivo(true);
                            data.set(data.indexOf(empleadoTable), empleadoTable);
                            dialogStage.close();

                            // Registro para auditar
                            String detalles = "activo el empleado " 
                                    + empleadoTable.getApellido() + " " 
                                    + empleadoTable.getNombre();
                            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());

                            dialogStage.close();
                        });
                        buttonNo.setOnAction((ActionEvent e) -> {
                            dialogStage.close();
                        });
                        dialogStage.showAndWait();
                }
                  
            } else {
                aplicacionControl.noPermitido();
            }
        } 
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
                empleado.setActivo(user.getActivo());
                data.set(data.indexOf(empleadoTable), empleado);
                return;
            }
        }
    }
    
    public void setDataToTable() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllEmpleados());
        data = FXCollections.observableArrayList(); 
        if (!usuarios.isEmpty()) {
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
                empleado.setActivo(user.getActivo());
                return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
        }
        
        empleadosTableView.setItems(data);
          
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        telefonoColumna.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        empresaColumna.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteEmpleado(empleadoTable);
                });
                
                if (empleadoTable.getActivo()) {
                    deleteButton.setText("Borrar");
                    getTableRow().setStyle(null);
                } else {
                    deleteButton.setText("Activar");
                    getTableRow().setStyle("-fx-background-color: lightcoral");
                    
                }
            }
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    verEmpleado(usuarioDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
        
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
                } else if (empleado.getEmpresa().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getTelefono().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<EmpleadoTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(empleadosTableView.comparatorProperty());
        empleadosTableView.setItems(sortedData);
    }
    
     private void verEmpleado(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.EMPLEADOS, Permisos.Nivel.VER)) {
               
               aplicacionControl.mostrarEmpleado(empleado);
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
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
        
        setDataToTable();
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
