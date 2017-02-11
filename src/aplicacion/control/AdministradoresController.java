/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.Administrador;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.IdentidadDAO;
import hibernate.dao.RolesDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Identidad;
import hibernate.model.Roles;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class AdministradoresController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
   
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private TableView administradoresTable;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonAtras;
    
     @FXML 
    private TableColumn cedulaColumna;
    
    @FXML 
    private TableColumn nombreColumna;
    
    @FXML 
    private TableColumn empresaColumna;
    
    @FXML 
    private TableColumn usuarioColumna;
    
    @FXML 
    private TableColumn permisoColumna;
    
    @FXML
    private TableColumn<Administrador, Administrador>  editarColumna;
    
    @FXML
    private TableColumn<Administrador, Administrador>  borrarColumna;
    
    private ObservableList<Administrador> data;
    
    ArrayList<Administrador> administradores;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void agregarAdministrador(ActionEvent event) {
        aplicacionControl.mostrarRegistrarAdministrador();
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        aplicacionControl.mostrarConfiguracion();
        stagePrincipal.close();
    } 
    
    public void editarAdministrador(Identidad identidad) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Dialogo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonNombreUsuario = new Button("Nombre Usuario");
        Button buttonContrasena = new Button("Contraseña");
        Button buttonPermisos = new Button("Permisos");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Que desea editar?"), buttonNombreUsuario, buttonContrasena, buttonPermisos).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        buttonPermisos.setPrefWidth(150);
        buttonContrasena.setPrefWidth(150);
        buttonNombreUsuario.setPrefWidth(150);
        dialogStage.show();
        buttonNombreUsuario.setOnAction((ActionEvent e) -> {
            aplicacionControl.mostrarEditarUsuario(identidad);
            dialogStage.close();
        });
        buttonContrasena.setOnAction((ActionEvent e) -> {
            aplicacionControl.mostrarEditarContrasena(identidad);
            dialogStage.close();
        });
        buttonPermisos.setOnAction((ActionEvent e) -> {
            aplicacionControl.mostrarEditarRol(identidad);
            dialogStage.close();
        });
    }
    
    public void deleteAdministrador(Administrador administrador) {
        
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Confirmación de borrado");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));;
                Button buttonConfirmar = new Button("Si Borrar");
                Button buttonCancelar = new Button("No, no estoy seguro");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Estas seguro que deseas borrar el usuario " +administrador.getNombreUsuario()+ "?"), 
                        buttonConfirmar, buttonCancelar).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                buttonConfirmar.setOnAction((ActionEvent e) -> {
                    new IdentidadDAO().delete(new IdentidadDAO().findById(administrador.getId()));
                    if (administrador.getUsuario().getDetallesEmpleado() == null) {
                        new UsuarioDAO().findById(administrador.getUsuario().getId()).setActivo(Boolean.FALSE);
                    }
                    new RolesDAO().findAllByUsuarioId(administrador.getUsuario().getId()).stream().forEach((rol) -> {
                        new RolesDAO().delete(rol);
                    });
                    HibernateSessionFactory.getSession().flush();
                    data.remove(administrador);
                    dialogStage.close();

                    // Registro para auditar
                    String detalles = "elimino el administrador " 
                            + administrador.getUsuario().getNombre() + " " 
                            + administrador.getUsuario().getApellido();
                    aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                });
                buttonCancelar.setOnAction((ActionEvent e) -> {
                       dialogStage.close();
                    });
                dialogStage.showAndWait();
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void administradorEditado(Identidad ide) {
        for (Administrador admin: data) {
            if(admin.getId().equals(ide.getId())) {
                Administrador administrador = new Administrador();
                administrador.setId(ide.getId());
                administrador.setUsuario(ide.getUsuario());
                administrador.setNombreUsuario(ide.getNombreUsuario());
                administrador.setContrasena(ide.getContrasena());
                administrador.setNombre(ide.getUsuario().getNombre() + " " + ide.getUsuario().getApellido());
                administrador.setApellido(ide.getUsuario().getApellido());
                administrador.setCedula(ide.getUsuario().getCedula());
                if (ide.getUsuario().getDetallesEmpleado() != null) {
                    administrador.setNombreEmpresa(ide.getUsuario().getDetallesEmpleado()
                            .getEmpresa().getNombre());
                }
                String permisos = "";
                ArrayList<Roles> roles = new ArrayList<>();
                roles.addAll(new RolesDAO().findAllByUsuarioId(administrador.getUsuario().getId()));
                int cantidad = roles.size();
                for (Roles rol: roles){
                    String next;
                    if (cantidad == 1) {
                        next = ".";
                    } else {
                        next = ", ";
                    }
                    permisos = permisos + rol.getNombre() + next;
                    cantidad --;
                }
                administrador.setPermisos(permisos);
                data.set(data.indexOf(admin), administrador);
            }
        }
    }
    
    public void setAdministradores() {
        
        IdentidadDAO identidadDAO = new IdentidadDAO();
        RolesDAO rolesDAO = new RolesDAO();
        administradores = new ArrayList<>();
        ArrayList<Identidad> identidades = new ArrayList<>();
        identidades.addAll(identidadDAO.findAllActivo());
        
        for (Identidad ide: identidades) {
            Administrador administrador = new Administrador();
            administrador.setId(ide.getId());
            administrador.setUsuario(ide.getUsuario());
            administrador.setNombreUsuario(ide.getNombreUsuario());
            administrador.setContrasena(ide.getContrasena());
            administrador.setNombre(ide.getUsuario().getNombre() + " " + ide.getUsuario().getApellido());
            administrador.setApellido(ide.getUsuario().getApellido());
            administrador.setCedula(ide.getUsuario().getCedula());
            if (ide.getUsuario().getDetallesEmpleado() != null) {
                administrador.setNombreEmpresa(ide.getUsuario().getDetallesEmpleado()
                        .getEmpresa().getNombre());
            }
            String permisos = "";
            ArrayList<Roles> roles = new ArrayList<>();
            roles.addAll(rolesDAO.findAllByUsuarioId(administrador.getUsuario().getId()));
            int cantidad = roles.size();
            for (Roles rol: roles){
                String next;
                if (cantidad == 1) {
                    next = ".";
                } else {
                    next = ", ";
                }
                permisos = permisos + rol.getNombre() + next;
                cantidad --;
            }
            administrador.setPermisos(permisos);
            administradores.add(administrador);
        }
        data = FXCollections.observableArrayList(); 
        if (!administradores.isEmpty()) {
           data.addAll(administradores);
        }
        administradoresTable.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        administradoresTable.setEditable(Boolean.FALSE); 
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        empresaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
     
        usuarioColumna.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        
        permisoColumna.setCellValueFactory(new PropertyValueFactory<>("permisos"));
        
        editarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        editarColumna.setCellFactory(param -> new TableCell<Administrador, Administrador>() {
            private final Button editarButton = new Button("Editar");

            @Override
            protected void updateItem(Administrador administrador, boolean empty) {
                super.updateItem(administrador, empty);

                if (administrador == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(editarButton);
                editarButton.setOnAction(event -> {
                    editarAdministrador(new IdentidadDAO().findById(administrador.getId()));
                });
            }
        });
        
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<Administrador, Administrador>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(Administrador administrador, boolean empty) {
                super.updateItem(administrador, empty);

                if (administrador == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteAdministrador(administrador);
                });
            }
        });
        
        administradoresTable.setRowFactory( (Object tv) -> {
            TableRow<Administrador> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Administrador rowData = row.getItem();
                    //aplicacionControl.mostrarCliente(IdentidadDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
        
        setAdministradores();
        
        buttonAgregar.setTooltip(
            new Tooltip("Agregar nuevo administrador")
        );
        buttonAgregar.setOnMouseEntered((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAgregar.setOnMouseExited((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
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
