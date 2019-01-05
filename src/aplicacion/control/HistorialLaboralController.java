/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteIessVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.DialogUtil;
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
import hibernate.dao.PlanillaIessDAO;
import hibernate.model.PlanillaIess;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import hibernate.dao.PagoMesItemDAO;
import hibernate.dao.PagoVacacionesDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.PagoVacaciones;
import hibernate.model.RolIndividual;
import javafx.scene.control.ChoiceBox;
import static aplicacion.control.util.Fechas.getFechaConMes;
import aplicacion.control.util.GuardarText;
import aplicacion.control.util.Numeros;
import static aplicacion.control.util.Numeros.round;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ControlDiarioDAO;
import hibernate.model.ControlDiario;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

/**
 *
 * @author Yornel
 */
public class HistorialLaboralController implements Initializable {
    
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
    private TableColumn<EmpleadoTable, EmpleadoTable> marcarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonBank;
    
    @FXML
    private Button buttonChange;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private CheckBox checkBoxTodos;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Double> ingresos;
    
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
    
    Dialog<Void> dialog;
    
    private ArrayList<String> textosDAT;
    private ArrayList<String> textosTXT;
    
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
    
     @FXML
    public void generarBank() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Bizbank");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new MaterialDesignButtonBlue("Seleccionar ruta");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta de guardado de los bizbank"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                generadorBank(file);
            }
        });
        dialogStage.show();
    }
    
    public void generadorBank(File file) {
        
        dialogWait();
    
        textosDAT = new ArrayList<>();
        textosTXT = new ArrayList<>();
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        
        for (EmpleadoTable table: 
                empleadosTable) {
            
            String reportName = "";
            Double reportMonto = 0d;
            
            reportMonto = Double.valueOf(table.getMonto());
            reportName = "HISTORIAL LABO ";
               
            
            textosDAT.add(crearLineaDAT(table.getUsuario(), reportMonto));
            textosTXT.add(crearLineaTXT(table.getUsuario(), reportMonto, reportName));
            
        }
        if (textosDAT.size() > 0 && textosTXT.size() > 0) {
            if (file != null) {
                new GuardarText().saveFile(textosDAT, getFileNameDat(file));
                new GuardarText().saveFile(textosTXT, getFileNameTXT(file));
                dialogoCompletado();
            } 
        } else {
            DialogUtil.error("Generador de archivos", "No se pueden generar "
                    + "los archivos porque\n no hay pagos en la fecha seleccionada.");
        }
        dialogLoading.close();
    }

    @FXML
    public void changeHistorial(ActionEvent event) {
        if (getCount() > 0) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Historial Laboral");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("Cambiar");
            Button buttonNo = new Button("Cancelar");
            CheckBox checkBox1 = new CheckBox("salario + horas extras + bonos + vacaciones - sueldo básico");
            checkBox1.setSelected(true);
            CheckBox checkBox2 = new CheckBox("horas extras + bonos + vacaciones                                         ");
            checkBox1.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    checkBox2.setSelected(!newValue);
                }
            });
            checkBox2.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    checkBox1.setSelected(!newValue);
                }
            });
            HBox hBox = HBoxBuilder.create()
                    .spacing(10.0) //In case you are using HBoxBuilder
                    .padding(new Insets(5, 5, 5, 5))
                    .alignment(Pos.CENTER)
                    .children(buttonOk, buttonNo)
                    .build();
            hBox.maxWidth(120);
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(
                    new Text("Esta opcion le permite cambiar la manera de calcular el historial laboral.\n"
                            + "al precionar cambiar los montos de este mes seran calculados con dicha formula."), 
                    new Text("Por defecto el historial laboral siempre sera calculado con:\n"
                            + "salario + sobretiempos + bonos + vacaciones - sueldo básico"),
                    new Text(""),
                    checkBox1,
                    checkBox2,
                    hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
                if (checkBox2.isSelected()) {
                    cambiarValores();
                } else {
                    borrarValores();
                }
            });
            buttonNo.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.show();
        } else {
            DialogUtil.error("Historial Laboral", "No has seleccionado ningun empleado.");
        }
    }
    
    public void cambiarValores() {
        loadingMode();
        try {
            ArrayList<EmpleadoTable> empleadosMarcados = new ArrayList<>();

            for (EmpleadoTable empleadoTable: 
                    (List<EmpleadoTable>) empleadosTableView.getItems()) {
                if (empleadoTable.getAgregar()) {
                    empleadosMarcados.add(empleadoTable);
                }
            }

            for (EmpleadoTable empleadoTable: empleadosMarcados) {
                PlanillaIess planillaIess = new PlanillaIess();
                planillaIess.setFecha(new Timestamp((new DateTime()).getMillis()));
                planillaIess.setInicioMes(inicio.getFecha());
                planillaIess.setFinMes(fin.getFecha());
                planillaIess.setUsuario(empleadoTable.getUsuario());
                planillaIess.setMonto(round(empleadoTable.getMontoAlternativo()));
                new PlanillaIessDAO().save(planillaIess);

                if (empleadoTable.getPlanillaIess() != null) {
                    PlanillaIess planillaDelete = empleadoTable.getPlanillaIess();
                    System.out.println("Borrando planilla: " + planillaDelete.getId());
                    new PlanillaIessDAO().delete(planillaDelete);
                    HibernateSessionFactory.getSession().flush();
                }

                empleadoTable.setMonto(planillaIess.getMonto().toString());
                empleadoTable.setPlanillaIess(planillaIess);
                empleadoTable.setPlanilla(true);
                data.set(data.indexOf(empleadoTable), empleadoTable);
                // Registro para auditar
                String detalles = "modifico el calculo de historial laboral"
                        + " del mes "+Fechas.getFechaSoloMesYAno(inicio)
                        +" para el empleado "+ empleadoTable.getNombre()+" "
                        +empleadoTable.getApellido()+ " a la formula especial.";
                aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
            }
            updateWindows();
        } catch (Exception e) {
            e.printStackTrace();
            error();
        }
    }
    
    public void borrarValores() {
        loadingMode();
        try {
            ArrayList<EmpleadoTable> empleadosMarcados = new ArrayList<>();

            for (EmpleadoTable empleadoTable: 
                    (List<EmpleadoTable>) empleadosTableView.getItems()) {
                if (empleadoTable.getAgregar()) {
                    empleadosMarcados.add(empleadoTable);
                }
            }
            for (EmpleadoTable empleadoTable: empleadosMarcados) {
                if (empleadoTable.getPlanillaIess() != null) {
                    System.out.println("Borrando planilla: " + empleadoTable.getPlanillaIess().getId());
                    PlanillaIess planillaIessDelete = new PlanillaIessDAO()
                            .findById(empleadoTable.getPlanillaIess().getId());
                    new PlanillaIessDAO().delete(planillaIessDelete);
                    HibernateSessionFactory.getSession().flush();

                    empleadoTable.setPlanillaIess(null);
                    empleadoTable.setPlanilla(false);
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                }

                // Registro para auditar
                String detalles = "modifico el calculo de historial laboral"
                    + " del mes "+Fechas.getFechaSoloMesYAno(inicio)
                    +" para el empleado "+ empleadoTable.getNombre()+" "
                    +empleadoTable.getApellido()+ " a la formula original.";
                aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
            }
            updateWindowsBorrado();;

        } catch (Exception e) {
            e.printStackTrace();
            error();
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
        
            Map<String, Object> parametros = new HashMap();
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
            
            String filename = "hirtorial_laboral_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el historial laboral de todos los empleado";
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
        dialogStage.setTitle("Historial Laboral");
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
        dialogStage.setTitle("Historial Laboral");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se modifico la formula de historial laboral con exito."), buttonOk).
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
        
        inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
       
        setTableInfo();
    }
    
    public void setTableInfo() {
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), inicio));
        
        checkBoxTodos.setSelected(false);
        contador.setText("");
       
        PlanillaIessDAO planillaIessDAO = new PlanillaIessDAO();
        List<PlanillaIess> planillaIesses = new ArrayList<>();
        planillaIesses.addAll(planillaIessDAO
                .findAllByFechaAndEmpresaId(inicio.getFecha(), empresa.getId()));
        
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
            empleado.setUsuario(user);
            Double historial = 0d; 
            Double montoCalculado = 0d; 
            Double montoAlternativo = 0d; 
            Boolean encontrado = false;
            
            for (PlanillaIess planillaIess: planillaIesses) {
                if (planillaIess.getUsuario().getId().equals(user.getId())) {
                    historial = planillaIess.getMonto();
                    encontrado = true;
                    empleado.setPlanillaIess(planillaIess);
                }
            }
            
            RolIndividual rol = new RolIndividualDAO()
                    .findByFechaAndEmpleadoIdAndDetalles(inicio.getFecha(), 
                        user.getId(), Const.ROL_PAGO_INDIVIDUAL);

            Fecha inicioVac = new Fecha("01", "01", inicio.getAno())
                .minusYears(1);

            PagoVacaciones pagoVacaciones = new PagoVacacionesDAO()
                .findInDeterminateYearByUsuarioId(inicioVac.getAno(), empleado.getId());

            if (rol != null) {
                empleado.setRolIndividual(rol);
                Double sueldo = rol.getSueldo();
                if (rol.getDias() < 30.0d) {
                    List<ControlDiario> controlesDiarios = new ControlDiarioDAO()
                            .findAllByEmpleadoIdInDeterminateTime(empleado.getId(), 
                                    inicio.getFecha(), fin.getFecha());
                    Integer permisos = 0;
                    int descansosMedicos = 0;
                    for (ControlDiario control:
                            controlesDiarios) {
                        if (control.getCaso().equals(Const.PERMISO)) {
                            permisos++;
                        }
                        if (control.getCaso().equals(Const.DM)) {
                            descansosMedicos++;
                            if (descansosMedicos > 3) {
                                permisos++;
                            }
                        } else {
                            descansosMedicos = 0;
                        }
                    }
                    if (permisos > 0) {
                        System.out.println("dias permiso "+permisos.toString()+" "+user.getApellido());
                        sueldo = (sueldo/30.0d)*(30.0d-permisos.doubleValue());
                    }
                }
                /**********************************************************/
                //sueldo + sobretiempos + bonos + vacaciones - sueldo básico
                /**********************************************************/
                Double vacaciones = getVacacionesFromThisMonth(pagoVacaciones);
                montoCalculado = rol.getSalario() + rol.getTotalMontoHorasExtras() 
                        + vacaciones
                        + rol.getTotalBonos() - sueldo;
                montoAlternativo = rol.getTotalMontoHorasExtras() 
                        + vacaciones
                        + rol.getTotalBonos();
            }
            
            if (encontrado) { historial = round(historial); }
            else { historial = round(montoCalculado); }
            empleado.setPlanilla(encontrado);
            empleado.setMonto(historial.toString());
            empleado.setMontoAlternativo(montoAlternativo.toString());
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
    
    private Double getVacacionesFromThisMonth(PagoVacaciones pagoVacaciones) {
        
        if (pagoVacaciones != null) {
            
            Calendar calIni = Calendar.getInstance();
            calIni.setTime(pagoVacaciones.getGoceInicio());

            Calendar calFin = Calendar.getInstance();
            calFin.setTime(pagoVacaciones.getGoceFin());


            Integer mes1int = calIni.get(Calendar.MONTH)+1;
            Integer mes2int = calFin.get(Calendar.MONTH)+1;
            Integer dias1int = 0;
            Integer dias2int = 0;
            Double montoMes1Dou = 0d;
            Double montoMes2Dou = 0d;
            if (mes1int != mes2int) {
                if (mes1int+1 == mes2int) {
                    dias2int = calFin.get(Calendar.DAY_OF_MONTH);
                    dias1int = pagoVacaciones.getDias() - dias2int;
                    Double valorDia = pagoVacaciones.getValor()
                            /pagoVacaciones.getDias().doubleValue();
                    montoMes1Dou = round(valorDia*dias1int);
                    montoMes2Dou = round(valorDia*dias2int);
                } 
            } else {
                if (inicio.getMesInt() == mes1int) {
                    return pagoVacaciones.getValor();
                }
            }
            if (inicio.getMesInt() == mes1int) {
                return montoMes1Dou;
            }
            if (inicio.getMesInt() == mes2int) {
                return montoMes2Dou;
            } 
        }
        return 0d;
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
        empleadosTableView.setEditable(Boolean.FALSE);
     
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
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                    contarSelecciones();
                }
            }
        );
        
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
                    getTableRow().setStyle("-fx-background-color:#EEE8AA");
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
        buttonBank.setTooltip(
            new Tooltip("Generar .DAT y .TXT")
        );
        buttonBank.setOnMouseEntered((MouseEvent t) -> {
            buttonBank.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/bank.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonBank.setOnMouseExited((MouseEvent t) -> {
            buttonBank.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/bank.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonChange.setTooltip(
            new Tooltip("Cambiar Monto")
        );
        buttonChange.setOnMouseEntered((MouseEvent t) -> {
            buttonChange.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/change_historial.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonChange.setOnMouseExited((MouseEvent t) -> {
            buttonChange.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/change_historial.png'); "
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
    
    public int getCount() {
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
         int count = empleadosTable.size();
        return count;
    }
    
    private Double getVacaciones(Usuario user) { // TODO; revisar
        Fecha inicioY = new Fecha("01","01",inicio.getAno());
        PagoVacaciones pagoVacaciones = new PagoVacacionesDAO()
                    .findInDeterminateTimeByUsuarioId(inicioY.getFecha(), user.getId()); 
        if (pagoVacaciones == null) {
            return 0d;
        } else {
            DateTime goceInicio = new DateTime(pagoVacaciones.getGoceInicio().getTime());
            DateTime goceFin = new DateTime(pagoVacaciones.getGoceFin().getTime());
            Integer monthNumber = inicio.getMesInt();

            if (goceInicio.getMonthOfYear() == goceFin.getMonthOfYear()) {
                if (goceInicio.getMonthOfYear() == monthNumber) {
                    return pagoVacaciones.getMonto();
                }
            } else {
                Integer days1 = 31 - goceInicio.getDayOfMonth();
                Integer days2 = goceFin.getDayOfMonth();
                Integer totalDays = days1 + days2;
                
                Double mont1 = (pagoVacaciones.getValor()/totalDays)*days1;
                Double mont2 = (pagoVacaciones.getValor()/totalDays)*days2;

                if (goceInicio.getMonthOfYear() == monthNumber) {
                    return round(mont1);
                } else if (goceFin.getMonthOfYear() == monthNumber) {
                    return round(mont2);
                }
            } 
        }
        return 0d;
    }
    
    String crearLineaDAT(Usuario user, Double reportMonto) {
        String monto = Numeros.roundToString(reportMonto);
        String espacios = "";
        String[] parts = monto.split(Pattern.quote("."));
        String partEntera = parts[0];
        switch (partEntera.length()) {
            case 0:
                espacios = "           ";
                break;
            case 1:
                espacios = "          ";
                break;
            case 2:
                espacios = "         ";
                break;
            case 3:
                espacios = "        ";
                break;
            case 4:
                espacios = "       ";
                break;
            case 5:
                espacios = "      ";
                break;
        }
        String text = user.getDetallesEmpleado()
                            .getEmpresa().getNumeracion()+";0001;"+inicio.getAno()
                            +";"+inicio.getMes()+";INS;"+user.getCedula()
                            +";"+espacios+monto+";0";
        return text;
    }
    
    String getFileNameDat(File file) {
        String nombre = empresa.getNombre();
        if (nombre.length() >= 8) {
            return  file.getPath()+"\\"+empresa.getNombre().substring(0,8)+".DAT";
        } else {
            return  file.getPath()+"\\"+empresa.getNombre()+".DAT";
        }
    }
    
    String crearLineaTXT(Usuario user, Double reportMonto, String reportName) {
        String monto = Numeros.roundToString(reportMonto);
        String espacios = "";
        String[] parts = monto.split(Pattern.quote("."));
        String partEntera = parts[0];
        String partDecimal = parts[1];
        switch (partEntera.length()) {
            case 0:
                espacios = "0000000000000";
                break;
            case 1:
                espacios = "000000000000";
                break;
            case 2:
                espacios = "00000000000";
                break;
            case 3:
                espacios = "0000000000";
                break;
            case 4:
                espacios = "000000000";
                break;
            case 5:
                espacios = "00000000";
                break;
        }
        String text = "10CPRP"+user.getDetallesEmpleado().getNroCuenta()
                +espacios+partEntera+partDecimal+reportName
                +user.getDetallesEmpleado().getEmpresa().getNombre()+"CUUSD";
        return text;
    }
    
    String getFileNameTXT(File file) {
        String nombre = empresa.getNombre();
        if (nombre.length() >= 8) {
            return  file.getPath()+"\\"+empresa.getNombre().substring(0,8)+".TXT";
        } else {
            return  file.getPath()+"\\"+empresa.getNombre()+".TXT";
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
}
