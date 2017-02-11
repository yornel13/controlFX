/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteIessVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.dao.UsuarioDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.DatePicker;
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
import org.joda.time.DateTime;
import aplicacion.control.util.MaterialDesignButtonBlue;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.PlanillaIessDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.Constante;
import hibernate.model.PlanillaIess;
import hibernate.model.RolIndividual;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Fechas.getToday;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Yornel
 */
public class PlanillaIessController implements Initializable {
    
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
    private TableColumn ingresoColumna;
    
    @FXML 
    private TableColumn iessColumna;
    
    @FXML 
    private TableColumn totalColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> marcarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonGuardar;
    
    @FXML
    private Button buttonBorrar;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private CheckBox checkBoxTodos;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<RolIndividual> rolIndividuals;
    ArrayList<PlanillaIess> planillaIesses;
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    @FXML 
    private Label contador;
    
     @FXML
    private ChoiceBox selectorDiaDe;
    
    @FXML
    private ChoiceBox selectorMesDe;
    
    @FXML
    private ChoiceBox selectorAnoDe;
    
    @FXML
    private ChoiceBox selectorDiaHa;
    
    @FXML
    private ChoiceBox selectorMesHa;
    
    @FXML
    private ChoiceBox selectorAnoHa;
    
    private Fecha inicio;
    private Fecha fin;
    
    Stage dialogLoading;
    private Constante iess;
    
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
    
    @FXML
    public void onClickMore(ActionEvent event) {
        inicio = inicio.plusMonths(1);
        fin = fin.plusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa); 
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        inicio = inicio.minusMonths(1);
        fin = fin.minusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa); 
        setTableInfo();
    }
    
    @FXML
    private void marcarTodos(ActionEvent event) throws ParseException {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxTodos.isSelected()) {
                empleadoTable.setAgregar(true);
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
        contarSelecciones();
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
        
        Double total = 0d;
        
        for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) 
                empleadosTableView.getItems()) {
            empleadoTable.setTotalIess(Double.valueOf(empleadoTable.getMonto()));
            total += Double.valueOf(empleadoTable.getMonto());
        }
        
        ReporteIessVarios datasource = new ReporteIessVarios();
        datasource.addAll((List<EmpleadoTable>) empleadosTableView.getItems());
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_PLANILLA_IESS_EMPLEADOS);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("lapso", getFechaConMes(inicio)  + " al " + getFechaConMes(fin));
            parametros.put("total", round(total).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "planilla_iess_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero una planilla IESS de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Planilla IESS");
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
    
    @FXML
    public void dialogoGuardar(ActionEvent event) {
        if (!contador.getText().equals("")) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            MaterialDesignButtonBlue buttonOk = new MaterialDesignButtonBlue("Si");
            MaterialDesignButtonBlue buttonNo = new MaterialDesignButtonBlue("no");
            HBox hBox = HBoxBuilder.create()
                    .spacing(10.0) //In case you are using HBoxBuilder
                    .padding(new Insets(5, 5, 5, 5))
                    .alignment(Pos.CENTER)
                    .children(buttonOk, buttonNo)
                    .build();
            hBox.maxWidth(120);
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("¿Seguro que desea guardar estas planillas?"), hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                guardarPlanillas();
                dialogStage.close();
            });
            buttonNo.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new MaterialDesignButtonBlue("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
            children(new Text("No has seleccionado ningun empleado."), 
                    buttonOk).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            dialogStage.show();
            buttonOk.setMaxWidth(60);
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                dialogStage.close();
            });
        }
    }
    
    public void guardarPlanillas() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PlanillaIessController.DataBaseThread(0);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
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
    
    @FXML
    public void dialogoBorrar(ActionEvent event) {
        if (!contador.getText().equals("")) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            MaterialDesignButtonBlue buttonOk = new MaterialDesignButtonBlue("Si");
            MaterialDesignButtonBlue buttonNo = new MaterialDesignButtonBlue("no");
            HBox hBox = HBoxBuilder.create()
                    .spacing(10.0) //In case you are using HBoxBuilder
                    .padding(new Insets(5, 5, 5, 5))
                    .alignment(Pos.CENTER)
                    .children(buttonOk, buttonNo)
                    .build();
            hBox.maxWidth(120);
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("¿Seguro que desea borrar estas planillas?"),hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                borrarPlanillas();
                dialogStage.close();
            });
            buttonNo.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new MaterialDesignButtonBlue("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
            children(new Text("No has seleccionado ningun empleado."), 
                    buttonOk).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            dialogStage.show();
            buttonOk.setMaxWidth(60);
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                dialogStage.close();
            });
        }
    }
    
    public void borrarPlanillas() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PlanillaIessController.DataBaseThread(1);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    public void error() {
        closeDialogMode();
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Hubo un error en el proceso."), 
                buttonOk).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        buttonOk.setMaxWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void updateWindows() {
        closeDialogMode();
        guardadoCompletado();
    }
    
    public void updateWindowsBorrado() {
        closeDialogMode();
        borradoCompletado();
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
    
    public void guardadoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se guardaron las planillas con exito."),
                new Text("Para que aparezca el monto real debe borrar las planillas"), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setMaxWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    public void borradoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se borraron las planillas con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setMaxWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa; 
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivo(empresa.getId()));
        
        inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
       
        setTableInfo();
    }
    
    public void setTableInfo() {
        
        checkBoxTodos.setSelected(false);
        contador.setText("");
        
        RolIndividualDAO rolIndividualDAO = new RolIndividualDAO();
        rolIndividuals = new ArrayList<>();
        rolIndividuals.addAll(rolIndividualDAO.findAllByFechaAndEmpresaId(inicio.getFecha(), empresa.getId()));
        
        PlanillaIessDAO planillaIessDAO = new PlanillaIessDAO();
        planillaIesses = new ArrayList<>();
        planillaIesses.addAll(planillaIessDAO.findAllByFechaAndEmpresaId(inicio.getFecha(), empresa.getId()));
        
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
            Double totalIngreso = 0d; 
            Boolean encontrado = false;
            for (PlanillaIess planillaIess: planillaIesses) {
                if (planillaIess.getUsuario().getId().equals(user.getId())) {
                    totalIngreso = planillaIess.getMonto();
                    encontrado = true;
                }
            }
            if (!encontrado) {
                for (RolIndividual rolIndividual: rolIndividuals) {
                    if (rolIndividual.getUsuario().getId().equals(user.getId())) {
                        totalIngreso = rolIndividual.getTotalIngreso();
                    }
                }
            }
            empleado.setPlanilla(encontrado);
            empleado.setMonto(totalIngreso.toString());
            Double iessDescuento = (totalIngreso/100d) * getIess();
            empleado.setTotalIess(round(iessDescuento));
            empleado.setNuevoSueldo(round(totalIngreso - iessDescuento));
            empleado.setUsuario(user);
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
        chequearFiltro(filteredData);
    }
    
    void chequearFiltro(FilteredList<EmpleadoTable> filteredData) {
        filteredData.setPredicate(empleado -> {
            // If filter text is empty, display all persons.
            if (filterField.getText() == null || filterField.getText().isEmpty()) {
                return true;
            }
            // Compare first name and last name of every person with filter text.
            String lowerCaseFilter = filterField.getText().toLowerCase();

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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.TRUE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        ingresoColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        ingresoColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        ingresoColumna.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<EmpleadoTable, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<EmpleadoTable, String> t) {
                    Double newValue;
                    try {
                        newValue = round(t.getNewValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // TODO, quitar
                        newValue = Double.valueOf(t.getOldValue());
                        //dialogoErrorCuotas();
                    }
                    EmpleadoTable empleadoTable = ((EmpleadoTable) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()));     
                    
                    empleadoTable.setAgregar(true);
                    Double totalIngreso = newValue; 
                    empleadoTable.setMonto(totalIngreso.toString());
                    Double iessDescuento = (totalIngreso/100d) * getIess();
                    empleadoTable.setTotalIess(round(iessDescuento));
                    empleadoTable.setNuevoSueldo(round(totalIngreso - iessDescuento));
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                    contarSelecciones();
                }
            }
        );
        
        iessColumna.setCellValueFactory(new PropertyValueFactory<>("totalIess"));
        
        totalColumna.setCellValueFactory(new PropertyValueFactory<>("nuevoSueldo"));
        
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxSeleccionar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
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
                
                if (empleadoTable.getPlanilla()) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
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
        buttonBorrar.setTooltip(
            new Tooltip("Borrar")
        );
        buttonBorrar.setOnMouseEntered((MouseEvent t) -> {
            buttonBorrar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/borrar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonBorrar.setOnMouseExited((MouseEvent t) -> {
            buttonBorrar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/borrar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        buttonAnterior.setTooltip(
            new Tooltip("Mes Anterior")
        );
        buttonAnterior.setOnMouseEntered((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonAnterior.setOnMouseExited((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #039BE5;");
        });
        buttonSiguiente.setTooltip(
            new Tooltip("Mes Siguiente")
        );
        buttonSiguiente.setOnMouseEntered((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonSiguiente.setOnMouseExited((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #039BE5;");
        });
        
        iess = (Constante) new ConstanteDAO().findUniqueResultByNombre(Const.IESS);
        if (iess != null)
            iessColumna.setText(Const.IP_IESS + " (" + iess.getValor() + "%)");
        
        selectorDiaDe.setItems(Fechas.arraySpinnerDia());
        selectorMesDe.setItems(Fechas.arraySpinnerMes());
        selectorAnoDe.setItems(Fechas.arraySpinnerAno());
        selectorDiaHa.setItems(Fechas.arraySpinnerDia());
        selectorMesHa.setItems(Fechas.arraySpinnerMes());
        selectorAnoHa.setItems(Fechas.arraySpinnerAno());
        
        selectorDiaDe.setDisable(true);
        selectorMesDe.setDisable(true);
        selectorAnoDe.setDisable(true);
        selectorDiaHa.setDisable(true);
        selectorMesHa.setDisable(true);
        selectorAnoHa.setDisable(true);
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
                contador.setText("");
                break;
            case 1:
                contador.setText("1 empleado seleccionado");
                break;
            default:
                contador.setText(count + " empleados seleccionados");
                break;
        }
        if (((List<EmpleadoTable>) data).size() == count) {
            checkBoxTodos.setSelected(true);
        } else {
            checkBoxTodos.setSelected(false);
        }
    }
    
    public double getIess() {
        if (iess == null) {
            return 0.0;
        } else {
            return Double.valueOf(iess.getValor());
        }
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
        
        public final Integer GUARDAR = 0;
        public final Integer BORRAR = 1;
        
        Integer opcion;

        public DataBaseThread(Integer opcion){
            this.opcion = opcion;
        }

        @Override
        public void run() {
    
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        if (Objects.equals(opcion, GUARDAR)) {
                            guardar();
                        } else if (Objects.equals(opcion, BORRAR)) {
                            borrar();
                        } 
                    }
             }, 1000, 1000);
            
        }
        
        private void guardar() {
            try {
                ArrayList<EmpleadoTable> empleadosMarcados = new ArrayList<>();
        
                for (EmpleadoTable empleadoTable: 
                        (List<EmpleadoTable>) empleadosTableView.getItems()) {
                    if (empleadoTable.getAgregar()) {
                        empleadoTable.setPlanilla(true);
                        data.set(data.indexOf(empleadoTable), empleadoTable);
                        empleadosMarcados.add(empleadoTable);
                    }
                }

                for (EmpleadoTable empleadoTable: empleadosMarcados) {
                    PlanillaIess planillaIess = new PlanillaIess();
                    planillaIess.setFecha(new Timestamp((new DateTime()).getMillis()));
                    planillaIess.setInicioMes(inicio.getFecha());
                    planillaIess.setFinMes(fin.getFecha());
                    planillaIess.setUsuario(empleadoTable.getUsuario());
                    planillaIess.setMonto(round(empleadoTable.getMonto()));
                    new PlanillaIessDAO().save(planillaIess);

                    for (PlanillaIess planillaIessDelete: planillaIesses) {
                        if (planillaIessDelete.getUsuario().getId() 
                                == empleadoTable.getUsuario().getId()) {
                            System.out.println("Borrando planilla: " + planillaIessDelete.getId());
                            new PlanillaIessDAO().delete(planillaIessDelete);
                            HibernateSessionFactory.getSession().flush();
                            planillaIesses.remove(planillaIessDelete);
                            break;
                        }
                    }
                    planillaIesses.add(planillaIess);
                }
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        updateWindows();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        error();
                    }
                });
            }
            
        }
        
        private void borrar() {
            try {
                for (EmpleadoTable empleadoTable: 
                        (List<EmpleadoTable>) empleadosTableView.getItems()) {
                    if (empleadoTable.getAgregar()) {
                        for (PlanillaIess planillaIessDelete: planillaIesses) {
                            if (planillaIessDelete.getUsuario().getId() 
                                    == empleadoTable.getUsuario().getId()) {
                                System.out.println("Borrando planilla: " + planillaIessDelete.getId());
                                new PlanillaIessDAO().delete(planillaIessDelete);
                                HibernateSessionFactory.getSession().flush();
                                planillaIesses.remove(planillaIessDelete);

                                Double totalIngreso = 0d;
                                for (RolIndividual rolIndividual: rolIndividuals) {
                                    if (rolIndividual.getUsuario().getId().equals(empleadoTable.getId())) {
                                        totalIngreso = rolIndividual.getTotalIngreso();
                                    }
                                }
                                empleadoTable.setPlanilla(false);
                                empleadoTable.setMonto(totalIngreso.toString());
                                Double iessDescuento = (totalIngreso/100d) * getIess();
                                empleadoTable.setTotalIess(round(iessDescuento));
                                empleadoTable.setNuevoSueldo(round(totalIngreso - iessDescuento));
                                data.set(data.indexOf(empleadoTable), empleadoTable);
                                
                                break;
                            }
                        }
                    }
                }
                
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        updateWindowsBorrado();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        error();
                    }
                });
            }
            
        }
        
    }
    
}
