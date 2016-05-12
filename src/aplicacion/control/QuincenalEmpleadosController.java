/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.ConfiguracionEmpresaController.numDecimalFilter;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Actuariales;
import hibernate.model.Empresa;
import hibernate.model.Seguro;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class QuincenalEmpleadosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private Button administradoresButton;
    
    @FXML
    private TableView empleadosTableView;
    
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
    private TableColumn quincenalColumna;
    
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
    
    public void mostrarEditarQuincenal(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.A_EMPLEADOS, Permisos.Nivel.EDITAR)) {
                try {
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Adelanto Quincenal");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonGuardar = new Button("Cambiar");
                    Button buttonCancelar = new Button("Cancelar");
                    TextField fieldAdelanto = new TextField();
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("Ingrese el nuevo valor del adelanto"), fieldAdelanto, buttonGuardar, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    fieldAdelanto.setPrefWidth(150);
                    buttonGuardar.setPrefWidth(100);
                    buttonCancelar.setPrefWidth(100);
                    fieldAdelanto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                    dialogStage.show();
                    buttonGuardar.setOnAction((ActionEvent e) -> {
                        Double newAdelantoValue;
                        if (fieldAdelanto.getText().isEmpty()) {
                            newAdelantoValue = 0d;
                        } else {
                            newAdelantoValue = Double.parseDouble(fieldAdelanto.getText());
                        }
                        
                        double oldMonto = 0;
                        if (empleado.getDetallesEmpleado().getQuincena() != null) {
                            oldMonto = empleado.getDetallesEmpleado().getQuincena();
                        }
                        
                        empleado.getDetallesEmpleado().setQuincena(newAdelantoValue);
                        HibernateSessionFactory.getSession().flush();
                        
                        dialogStage.close();
                        
                        empleadoEditado(empleado);
                        
                        completado();

                        // Registro para auditar
                        String detalles = "edito el adelanto quincenal de " 
                                + empleado.getNombre() + " " + empleado.getApellido()
                                + " de " + oldMonto + " a " + newAdelantoValue;
                        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    }); 
 
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
        children(new Text("Adelanto quincenal modificado con exito."), buttonOk).
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
               if (user.getDetallesEmpleado().getQuincena() != null) {
                    empleado.quincenal.set(user.getDetallesEmpleado().getQuincena());
               } else {
                    empleado.quincenal.set(0d);
               }
               data.set(data.indexOf(empleadoTable), empleado);
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
               if (user.getDetallesEmpleado().getQuincena() != null) {
                    empleado.quincenal.set(user.getDetallesEmpleado().getQuincena());
               } else {
                    empleado.quincenal.set(0d);
               }
               
                return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
           empleadosTableView.setItems(data);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        quincenalColumna.setCellValueFactory(new PropertyValueFactory<>("quincenal"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    mostrarEditarQuincenal(new UsuarioDAO().findById(rowData.getId()));
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
