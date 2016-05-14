/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class DecimosEmpleadosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private Button administradoresButton;
    
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
    private TableColumn<EmpleadoTable, EmpleadoTable>  decimosColumna;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    public void mostrarNoAcumulaDecimos(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_PAGOS, Permisos.Nivel.EDITAR)) {
                try {
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonOk = new Button("Si, no acumula");
                    Button buttonCancelar = new Button("No, cancelar.");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("¿Seguro que desea marcar que NO acumula decimos el empleado?"), buttonOk, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    buttonOk.setPrefWidth(100);
                    buttonCancelar.setPrefWidth(100);
                    buttonOk.setOnAction((ActionEvent e) -> {
                        
                        empleado.getDetallesEmpleado().setAcumulaDecimos(false);
                        HibernateSessionFactory.getSession().flush();
                        
                        dialogStage.close();
                        
                        empleadoEditado(empleado);
                        
                        completado();

                        // Registro para auditar
                        String detalles = "marco que el empleado " 
                                + empleado.getNombre() + " " + empleado.getApellido() + " no acumula decimos";
                        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    }); 
                    buttonCancelar.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                        empleadoEditado(empleado);
                    }); 
                    
                    dialogStage.showAndWait();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    public void mostrarAcumulaDecimos(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_PAGOS, Permisos.Nivel.EDITAR)) {
                try {
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonOk = new Button("Si, si acumula");
                    Button buttonCancelar = new Button("No, cancelar.");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("¿Seguro que desea marcar que SI acumula decimos el empleado?"), buttonOk, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    buttonOk.setPrefWidth(100);
                    buttonCancelar.setPrefWidth(100);
                    buttonOk.setOnAction((ActionEvent e) -> {
                        
                        empleado.getDetallesEmpleado().setAcumulaDecimos(true);
                        HibernateSessionFactory.getSession().flush();
                        
                        dialogStage.close();
                        
                        empleadoEditado(empleado);
                        
                        completado();

                        // Registro para auditar
                        String detalles = "marco que el empleado " 
                                + empleado.getNombre() + " " + empleado.getApellido() + " si acumula decimos";
                        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    }); 
                    buttonCancelar.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                        empleadoEditado(empleado);
                    }); 
                    
                    dialogStage.showAndWait();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    public void completado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Acumulación de decimos modificado con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void empleadoEditado(Usuario user) {
        for (EmpleadoTable empleadoTable: data) {
            if(empleadoTable.getId() == user.getId()) {
               EmpleadoTable empleado = new EmpleadoTable();
               empleado.id.set(user.getId());
               empleado.nombre.set(user.getNombre());
               empleado.apellido.set(user.getApellido());
               empleado.cedula.set(user.getCedula());
               empleado.empresa.set(user.getDetallesEmpleado().getEmpresa().getNombre());
               empleado.telefono.set(user.getTelefono());
               empleado.departamento.set(user.getDetallesEmpleado().getDepartamento().getNombre());
               empleado.cargo.set(user.getDetallesEmpleado().getCargo().getNombre());
               empleado.acumulaDecimos.set(user.getDetallesEmpleado().getAcumulaDecimos());
               data.set(data.indexOf(empleadoTable), empleado);
               return;
            }
        }
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findByEmpresaIdActivo(empresa.getId()));
        if (!usuarios.isEmpty()) {
           data = FXCollections.observableArrayList(); 
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
               empleado.acumulaDecimos.set(user.getDetallesEmpleado().getAcumulaDecimos());
               
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
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        decimosColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        decimosColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxDeuda = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleado, boolean empty) {
                super.updateItem(empleado, empty);

                if (empleado == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxDeuda);
                if (checkBoxDeuda != null)
                    checkBoxDeuda.setSelected(empleado.getAcumulaDecimos());
                checkBoxDeuda.setOnAction(event -> {
                     if (empleado.getAcumulaDecimos()) {
                         mostrarNoAcumulaDecimos(new UsuarioDAO().findById(empleado.getId()));
                     } else {
                         mostrarAcumulaDecimos(new UsuarioDAO().findById(empleado.getId()));
                     }
                });
            }

            
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    //mostrarEditarQuincenal(new UsuarioDAO().findById(rowData.getId()));
                }
            });
            return row ;
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
