/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteActuarialesVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Actuariales;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Yornel
 */
public class ActuarialesEmpleadosController implements Initializable {
    
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
    private TableColumn actuarial1Columna;
    
    @FXML 
    private TableColumn actuarial2Columna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    private Stage dialogLoading;
    
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
    
    public void mostrarEditarActuariales(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
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
                    controller.setProgramaPrincipal(aplicacionControl);
                    controller.setProgramaActuariales(this);
                    controller.setEmpleado(empleado);
                    ventana.showAndWait();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    public void dialogWait() {
        dialogLoading = new Stage();
        dialogLoading.initModality(Modality.APPLICATION_MODAL);
        dialogLoading.setResizable(false);
        dialogLoading.setTitle("Cargando...");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_loading.png").toExternalForm();
        dialogLoading.getIcons().add(new Image(stageIcon));
        dialogLoading.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Cargando espere...")).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogLoading.show();
    }
    
    public void imprimir(File file) {
        
        dialogWait();
        
        ReporteActuarialesVarios datasource = new ReporteActuarialesVarios();
        datasource.addAll((List<EmpleadoTable>) empleadosTableView.getItems());
        
        Double total1 = 0d;
        Double total2 = 0d;
        
        for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) 
                empleadosTableView.getItems()) {
            total1 += empleadoTable.getActuarial1();
            total2 += empleadoTable.getActuarial2();
        }
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ACTUARIALES_EMPLEADOS);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("total1", round(total1).toString());
            parametros.put("total2", round(total2).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "actuariales_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo de actuariales de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            ex.printStackTrace();
        } finally {
            dialogLoading.close();
        }
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir Actuariales");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Completado."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        if (!empleadosTableView.getItems().isEmpty()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Imprimir Actuariales");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonSiDocumento = new Button("Seleccionar ruta");
            Button buttonNoDocumento = new Button("Salir");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Seleccione la ruta de guardado"), 
                    buttonSiDocumento, buttonNoDocumento).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonSiDocumento.setOnAction((ActionEvent e) -> {
                File file = seleccionarDirectorio();
                if (file != null) {
                    dialogStage.close();
                    imprimir(file);
                }
            });
            buttonNoDocumento.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
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
                empleado.setEmpresa(user.getDetallesEmpleado().getEmpresa().getNombre());
                empleado.setTelefono(user.getTelefono());
                empleado.setDepartamento(user.getDetallesEmpleado()
                        .getDepartamento().getNombre());
                empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
                Actuariales actuariales = new Actuariales();
                actuariales = new ActuarialesDAO().findByEmpleadoId(user.getId());
                if (actuariales != null) {
                   empleado.setActuarial1(actuariales.getPrimario());
                   empleado.setActuarial2(actuariales.getSecundario());
                } else {
                   empleado.setActuarial1(0d);
                   empleado.setActuarial2(0d);
                }
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
            empleado.setEmpresa(user.getDetallesEmpleado().getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
            Actuariales actuariales = new Actuariales();
            actuariales = new ActuarialesDAO().findByEmpleadoId(user.getId());
            if (actuariales != null) {
               empleado.setActuarial1(actuariales.getPrimario());
               empleado.setActuarial2(actuariales.getSecundario());
            } else {
               empleado.setActuarial1(0d);
               empleado.setActuarial2(0d);
            }
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
        
        actuarial1Columna.setCellValueFactory(new PropertyValueFactory<>("actuarial1"));
        
        actuarial2Columna.setCellValueFactory(new PropertyValueFactory<>("actuarial2"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    mostrarEditarActuariales(new UsuarioDAO().findById(rowData.getId()));
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
        buttonImprimir.setTooltip(
            new Tooltip("Imprimir")
        );
        buttonImprimir.setOnMouseEntered((MouseEvent t) -> {
            buttonImprimir.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/imprimir.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonImprimir.setOnMouseExited((MouseEvent t) -> {
            buttonImprimir.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/imprimir.png'); "
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
