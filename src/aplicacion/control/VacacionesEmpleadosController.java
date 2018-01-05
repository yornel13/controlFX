/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDiasVacaciones;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import hibernate.HibernateSessionFactory;
import hibernate.dao.DiasVacacionesDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.DiasVacaciones;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
public class VacacionesEmpleadosController implements Initializable {
    
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
    private TableColumn diasColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonGuardar;
    
    @FXML
    private Button buttonImprimir;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    ArrayList<DiasVacaciones> diasVac;
    private Empresa empresa;
    private Stage dialogLoading;
    Dialog<Void> dialog;
    
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
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir Dias Vacaciones");
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
    
    public void dialogoNoCambios() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No has modificado ningun dato."), buttonOk).
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
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Ruta de guardado");
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
    
    public void imprimir(File file) {
        
        dialogWait();
        
        ReporteDiasVacaciones datasource = new ReporteDiasVacaciones();
        datasource.addAll((List<EmpleadoTable>) empleadosTableView.getItems());
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DIAS_DERECHO_VACACIONES);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "dias_vacaciones_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero un reporte de dias de vacaciones de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    @FXML
    public void dialogoGuardar(ActionEvent event) throws ParseException {
        
        boolean guardar = false;
        
        for (EmpleadoTable empleado: data) {
            if (empleado.getEditado()) {
                 guardar = true;   
            }
        }
        
        if (guardar) {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Runnable worker = new VacacionesEmpleadosController.DataBaseThread();
            executor.execute(worker);
            executor.shutdown();

            loadingMode();
        } else {
            dialogoNoCambios();
        }
        
    }
    
    private void loadingMode(){
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stagePrincipal);//stage here is the stage of your webview
        dialog.initStyle(StageStyle.TRANSPARENT);
        Label loader = new Label("   Cargando, por favor espere...");
        loader.setContentDisplay(ContentDisplay.LEFT);
        loader.setGraphic(new ProgressIndicator());
        dialog.getDialogPane().setGraphic(loader);
        dialog.getDialogPane().setStyle("-fx-background-color: #E0E0E0;");
        dialog.getDialogPane().setPrefSize(250, 75);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3); 
        ds.setOffsetY(1.3); 
        ds.setColor(Color.DARKGRAY);
        dialog.getDialogPane().setEffect(ds);
        dialog.show();
    }
    
    public void closeDialogMode() {
        if (dialog != null) {
           Stage toClose = (Stage) dialog.getDialogPane()
                   .getScene().getWindow();
           toClose.close();
           dialog.close();
           dialog = null;
        }
    }
    
    void guardadoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Se modificaron los dias de vacaciones con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivo(empresa.getId()));
        DiasVacacionesDAO diasDAO = new DiasVacacionesDAO();
        diasVac = new ArrayList<>();
        diasVac.addAll((ArrayList<DiasVacaciones>) diasDAO.findAllByEmpresaId(empresa.getId()));
        
        setTableInfo();
    }
    
    void setTableInfo() {
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.setId(user.getId());
            empleado.setUsuario(user);
            empleado.setNombre(user.getNombre());
            empleado.setApellido(user.getApellido());
            empleado.setCedula(user.getCedula());
            empleado.setEmpresa(user.getDetallesEmpleado().getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
            empleado.setDiasVacaciones("0");
            for (DiasVacaciones vacaciones: diasVac) {
                if (vacaciones.getUsuario().getId().equals(user.getId())) {
                    empleado.setDiasVacaciones(vacaciones.getDias().toString());
                    empleado.setObjectVacaciones(vacaciones);
                }
            }
            empleado.setEditado(Boolean.FALSE);
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
       empleadosTableView.setItems(data);

        callFilter();
    }
    
    void callFilter() {
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
        empleadosTableView.setEditable(Boolean.TRUE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        diasColumna.setCellValueFactory(new PropertyValueFactory<>("diasVacaciones"));
        diasColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        diasColumna.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<EmpleadoTable, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<EmpleadoTable, String> t) {
                    Integer newValue;
                    EmpleadoTable empleadoTable = ((EmpleadoTable) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()));
                    try {
                        newValue = Integer.valueOf(t.getNewValue());
                        if (!newValue.equals(Integer.valueOf(t.getOldValue()))) {
                            empleadoTable.setEditado(true);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // TODO, quitar
                        newValue = Integer.valueOf(t.getOldValue());
                        //dialogoErrorCuotas();
                    }   
                    empleadoTable.setDiasVacaciones(newValue.toString());
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                }
            }
        );
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
        buttonGuardar.setTooltip(
            new Tooltip("Guardar")
        );
        buttonGuardar.setOnMouseEntered((MouseEvent t) -> {
            buttonGuardar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/guardar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonGuardar.setOnMouseExited((MouseEvent t) -> {
            buttonGuardar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/guardar.png'); "
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
    
    public class DataBaseThread implements Runnable {
        
        public DataBaseThread(){
        }

        @Override
        public void run() {
    
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        try {
                            guardar();
                        } catch (ParseException ex) {
                            closeDialogMode();
                        }
                    }
             }, 1000, 1000);
            
        }
        
        private void guardar() throws ParseException {
            for (EmpleadoTable empleado: data) {
                if (empleado.getEditado()) {
                    DiasVacaciones diasVacaciones = new DiasVacaciones();
                    diasVacaciones.setUsuario(empleado.getUsuario());
                    diasVacaciones.setAumentado(Boolean.TRUE);
                    diasVacaciones.setActivo(Boolean.TRUE);
                    diasVacaciones.setDias(Integer.valueOf(empleado.getDiasVacaciones()));
                    diasVacaciones.setFecha(new Timestamp(new Date().getTime()));
                    new DiasVacacionesDAO().save(diasVacaciones);
                    if (empleado.getObjectVacaciones() != null) {
                        new DiasVacacionesDAO().delete(empleado.getObjectVacaciones());
                        HibernateSessionFactory.getSession().flush();
                    }
                    
                    String detalles = "cambio los dias de derecho a vacaciones de " 
                            + empleado.getApellido()+ " " + empleado.getNombre() 
                            + " a " + empleado.getDiasVacaciones();
                    aplicacionControl.au.saveEdito(detalles, 
                            aplicacionControl.permisos.getUsuario());
                }
            }
            DiasVacacionesDAO diasDAO = new DiasVacacionesDAO();
            diasVac = new ArrayList<>();
            diasVac = (ArrayList<DiasVacaciones>) diasDAO.findAllByEmpresaId(empresa.getId());
            for (DiasVacaciones diass: diasVac) {
                System.out.println(diass.getDias());
            }
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    setTableInfo();
                    closeDialogMode();
                    guardadoCompletado();
                }
            });
        }
        
    }
}
