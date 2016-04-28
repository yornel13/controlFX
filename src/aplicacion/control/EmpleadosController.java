/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.Administrador;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.IdentidadDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class EmpleadosController implements Initializable {
    
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
    private TableView empleadosTableView;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void pagos(Usuario empleado) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Agregar Datos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonActuariales = new Button("Actuariales");
        Button buttonContrasena = new Button("Contraseña");
        Button buttonPermisos = new Button("Permisos");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Que desea agregar?"), buttonActuariales).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        buttonPermisos.setPrefWidth(150);
        buttonContrasena.setPrefWidth(150);
        buttonActuariales.setPrefWidth(150);
        dialogStage.show();
        buttonActuariales.setOnAction((ActionEvent e) -> {
            mostrarEditarActuariales(empleado);
            dialogStage.close();
        });
        buttonContrasena.setOnAction((ActionEvent e) -> {
            //aplicacionControl.mostrarEditarContrasena(identidad);
            dialogStage.close();
        });
        buttonPermisos.setOnAction((ActionEvent e) -> {
            //aplicacionControl.mostrarEditarRol(identidad);
            dialogStage.close();
        });
    }
    
    public void mostrarEditarActuariales(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.EDITAR)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarActuariales.fxml"));
                    AnchorPane ventanaHoras = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaHoras);
                    ventana.setScene(scene);
                    ActuarialesController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setEmpleado(empleado);
                    ventana.show();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarVentanaPrincipal();
        stagePrincipal.close();
    }
    
    @FXML
    private void agregarEmpleado(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.CREAR)) {
               
               aplicacionControl.mostrarRegistrarEmpleado(empresa);
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    private void verEmpleado(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.VER)) {
               
               aplicacionControl.mostrarEmpleado(empleado);
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarInEmpresa(empresa);
        stagePrincipal.close();
    } 
    
    public void deleteEmpleado(EmpleadoTable empleadoTable) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.ELIMINAR)) {
               
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Confirmación de borrado");
                String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));;
                Button buttonConfirmar = new Button("Si Borrar");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Borrar el empleado " + empleadoTable.getNombre()+ "?"), buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                dialogStage.show();
                buttonConfirmar.setOnAction((ActionEvent e) -> {

                    new UsuarioDAO().findById(empleadoTable.getId()).setActivo(Boolean.FALSE);
                    HibernateSessionFactory.getSession().flush();
                    data.remove(empleadoTable);
                    dialogStage.close();
                });
                  
            } else {
               aplicacionControl.noPermitido();
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
               empleado.telefono.set(user.getTelefono());
               empleado.departamento.set(user.getDetallesEmpleado().getDepartamento().getNombre());
               empleado.cargo.set(user.getDetallesEmpleado().getCargo().getNombre());
                return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
           empleadosTableView.setItems(data);
        }
        
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        TableColumn apellido = new TableColumn("Apellido");
        apellido.setMinWidth(100);
        apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        TableColumn cedula = new TableColumn("Cedula");
        cedula.setMinWidth(100);
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        TableColumn telefono = new TableColumn("Telefono");
        telefono.setMinWidth(100);
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        TableColumn departamento = new TableColumn("Departamento");
        departamento.setMinWidth(100);
        departamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        TableColumn cargo = new TableColumn("Cargo");
        cargo.setMinWidth(100);
        cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        TableColumn<EmpleadoTable, EmpleadoTable> pagos = new TableColumn<>("Pagos");
        pagos.setMinWidth(40);
        pagos.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pagos.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final Button pagosButton = new Button("Agregar");

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(pagosButton);
                pagosButton.setOnAction(event -> {
                    pagos(new UsuarioDAO().findById(empleadoTable.getId()));
                });
            }
        });
        
        TableColumn<EmpleadoTable, EmpleadoTable> delete = new TableColumn<>("Borrar");
        delete.setMinWidth(40);
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteEmpleado(empleadoTable);
                });
            }
        });
        
        empleadosTableView.getColumns().addAll(nombre, apellido, cedula, 
                telefono, departamento, cargo, pagos, delete);
        
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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        empleadosTableView.getColumns().clear(); 
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
