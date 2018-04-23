/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDecimoGeneral;
import aplicacion.control.reports.ReporteRolDecimoCuarto;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.GuardarText;
import aplicacion.control.util.MaterialDesignButtonBlue;
import aplicacion.control.util.Numeros;
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.PagoQuincena;
import hibernate.model.Usuario;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import hibernate.dao.PagoDecimoDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.DiasVacaciones;
import hibernate.model.PagoDecimo;
import hibernate.model.RolIndividual;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.joda.time.DateTime;
import aplicacion.control.util.Permisos;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.ControlDiarioDAO;
import hibernate.dao.PagoVacacionesDAO;
import hibernate.model.Constante;
import hibernate.model.ControlDiario;
import hibernate.model.PagoVacaciones;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import javafx.scene.control.ChoiceBox;
import net.sf.jasperreports.engine.JREmptyDataSource;

/**
 *
 * @author Yornel
 */
public class PagoDecimoCuartoAnualController implements Initializable {
    
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
    private TableColumn montoColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> imprimirColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonBank;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Label yearLabel;
    
    @FXML
    private Label periodoLabel;
    
    private ObservableList<EmpleadoTable> data;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML
    private CheckBox checkBoxImpTodos;
    
    @FXML
    private Button buttonChange;
    
    ArrayList<PagoQuincena> pagosQuincenal;
    private ArrayList<String> textosDAT;
    private ArrayList<String> textosTXT;
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    ArrayList<DiasVacaciones> diasVac;
    Dialog<Void> dialog;
    String periodoALiquidar;
    
    ArrayList<PagoDecimo> pagosDecimo;
    
    List<EmpleadoTable> empladosImprimir;
    
    Stage dialogLoading;
    
    Label loader;
    
    Integer count;
    
    PagoDecimoDAO pdDAO = new PagoDecimoDAO();
    
    PagoDecimo pdStart;
    PagoDecimo pdEnd;
    
    private Constante constDecimoCuarto;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        yearLabel.setText(String.valueOf(Integer.valueOf(yearLabel.getText())+1));
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        if (Integer.valueOf(yearLabel.getText()) > 2017)
            yearLabel.setText(String.valueOf(Integer.valueOf(yearLabel.getText())-1));
        setTableInfo();
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
        setTableInfo();
    }
    
    public void updateTable() {
        empleadosTableView.refresh();
    }
    
    @FXML
    private void pagarATodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxPagarTodos.isSelected()) {
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    empleadoTable.setPagar(false);
                } else {
                    empleadoTable.setPagar(true);
                }
            } else {
                empleadoTable.setPagar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
    @FXML
    private void impTodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            empleadoTable.setAgregar(checkBoxImpTodos.isSelected());
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
    @FXML
    public void pagarAdelanto(ActionEvent event) {
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getPagar()) empleadosTable.add(empleado);
        } if (empleadosTable.isEmpty()) return;
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pagar decimo cuarto acumulado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("Si");
        Button buttonNo = new Button("no");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonOk, buttonNo)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Seguro que desea pagar el decimo cuarto acumulado "
                + "a estos empleado?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            hacerPago();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    @FXML
    public void generarBank(ActionEvent event) {
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar()) empleadosTable.add(empleado);
        } if (empleadosTable.isEmpty()) return;
        
        textosDAT = new ArrayList<>();
        textosTXT = new ArrayList<>();
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) data) {
            if (empleadoTable.getAgregar()) {
                textosDAT.add(crearLineaDAT(empleadoTable.getUsuario(), empleadoTable.getDecimo4()));
                textosTXT.add(crearLineaTXT(empleadoTable.getUsuario(), empleadoTable.getDecimo4()));
            }
        }
        if (textosDAT.size() > 0 && textosTXT.size() > 0) {
            dialogoGenerarDatTxt();
        } else {
            dialogoErrorBizBank();
        }
    }
    
    @FXML
    public void imprimirGeneral(ActionEvent event) {
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar()) empleadosTable.add(empleado);
        } if (empleadosTable.isEmpty()) return;
        
        empladosImprimir = empleadosTable;
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Reporte - Decimo Cuartos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonSiDocumento, buttonNoDocumento)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Desea imprimir el reporte general de decimos cuarto"),
                new Text("de los empleados seleccionados?"),
                hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonSiDocumento.setMinWidth(50);
        buttonNoDocumento.setMinWidth(50);
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
       
        ReporteDecimoGeneral datasource = new ReporteDecimoGeneral();
        datasource.addAll(empladosImprimir);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_DECIMO_CUARTO);
            String reportName = "Decimo Cuarto";
            
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("lapso", pdStart.getStringFecha()+ " al "+pdEnd.getStringFecha());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (empladosImprimir.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = reportName + " " + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el reporte de general de decimos cuarto "
                    + "de todos los empleado del "+pdStart.getStringFecha()
                    + " al "+pdEnd.getStringFecha();
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    @FXML
    private void cambiarFecha(ActionEvent event) {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Modificar Fecha");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButtonBlue buttonConfirmar = new MaterialDesignButtonBlue("MODIFICAR");
        ChoiceBox selectorMes = new ChoiceBox();
        ChoiceBox selectorAno = new ChoiceBox();
        ChoiceBox selectorMes2 = new ChoiceBox();
        ChoiceBox selectorAno2 = new ChoiceBox();

        selectorMes.setItems(Fechas.arraySpinnerMesText());
        selectorAno.setItems(Fechas.arraySpinnerAnoCorto(pdEnd.getFecha().getYear()+1900));
        selectorMes.getSelectionModel().select(pdEnd.getFecha().getMonth());
        selectorAno.getSelectionModel().select(String.valueOf(pdEnd.getFecha().getYear()+1900));
        
        String[] mes2 = new String[1];
        mes2[0] = Fechas.getMonthNameCort(pdStart.getFecha().getMonth()+1);
        selectorMes2.setItems(FXCollections.observableArrayList(mes2));
        String[] ano2 = new String[1];
        ano2[0] = String.valueOf(pdStart.getFecha().getYear()+1900);
        selectorAno2.setItems(FXCollections.observableArrayList(ano2));
        selectorMes2.getSelectionModel().select(0);
        selectorAno2.getSelectionModel().select(String.valueOf(pdStart.getFecha().getYear()+1900));
        selectorMes2.setDisable(true);
        selectorAno2.setDisable(true);

        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(0, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(selectorMes, selectorAno)
                .build();
        hBox.maxWidth(120);
        
         HBox hBox2 = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(0, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(selectorMes2, selectorAno2)
                .build();
        hBox.maxWidth(120);

        Text textStart = new Text("Fecha de pago (Desde)");
        Text textEnd = new Text("Fecha de pago (Hasta)");

        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(textStart, hBox2, textEnd, hBox, buttonConfirmar).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonConfirmar.setOnAction((ActionEvent e) -> {

            
            Integer mesIndex = selectorMes.getSelectionModel().getSelectedIndex();
            Integer anoInt = Integer.valueOf((String) selectorAno.getSelectionModel().getSelectedItem());
            LocalDate now = LocalDate.of(anoInt, mesIndex+1, 1);
            Integer dayMax = now.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
            Timestamp newTime = new Timestamp(anoInt-1900, mesIndex, dayMax, 0, 0, 0, 0);
            pdEnd = new PagoDecimo();
            pdEnd.setFecha(newTime);
            pdEnd.setMonto(Integer.valueOf(yearLabel.getText()).doubleValue());
            pdEnd.setDecimo(Const.DECIMO_CUARTO_END);
            calcular();
            dialogStage.close();
            
        });  
        dialogStage.show();
    }
    
    public void dialogoErrorBizBank() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Generador de archivos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No se pueden generar los archivos porque no hay empleados\n"
                + "con pagos hechos, o sus pagos son menores a 0."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void hacerPago() {

        loadingMode();
        
        empladosImprimir = new ArrayList<>();
        textosDAT = new ArrayList<>();
        textosTXT = new ArrayList<>();

        for (EmpleadoTable empleadoTable: data) {
            if (empleadoTable.getPagar()) {
                if (pdDAO.getLastPagoEnd4(Integer.valueOf(yearLabel.getText())) == null) {
                    pdDAO.save(pdStart);
                    pdDAO.save(pdEnd);
                }
                PagoDecimo pagoDecimo = new PagoDecimo();
                pagoDecimo.setUsuario(empleadoTable.getUsuario());
                pagoDecimo.setFecha(new Timestamp(Integer.valueOf(yearLabel.getText())-1900, 0, 1, 0, 0, 0, 0));
                pagoDecimo.setMonto(empleadoTable.getDecimo4());
                pagoDecimo.setDecimo(Const.DECIMO_CUARTO);
                new PagoDecimoDAO().save(pagoDecimo);

                empleadoTable.setPagoDecimo(pagoDecimo);
                empleadoTable.setPagado("Si");
                empleadoTable.setPagar(false);

                empladosImprimir.add(empleadoTable);

                // Registro para auditar
                String detalles = "hizo el pago de decimo cuarto acumulados nro: " + pagoDecimo.getId() 
                        + " del lapso "+pdStart.getStringFecha()
                        + " al "+pdEnd.getStringFecha()+" para el empleado " 
                        + empleadoTable.getApellido()+ " " + empleadoTable.getNombre();
                aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());

                textosDAT.add(crearLineaDAT(empleadoTable.getUsuario(), pagoDecimo.getMonto()));
                textosTXT.add(crearLineaTXT(empleadoTable.getUsuario(), pagoDecimo.getMonto()));
            }
        }
        closeDialogMode();
        setTableInfo();
        dialogoPagoDecimosCompletado(); 
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoDecimoCuartoAnualController.DataBaseThread(1, file, enviarCorreo);
        executor.execute(worker);
        executor.shutdown();

        loadingModeImprimir();
        
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
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
    
    public void dialogoGenerarDatTxt() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Roles Individuales");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Seleccionar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los pagos de decimo cuarto con exito, \n"
                +"Seleccione ahora donde guardar los archivos .TXT y .DAT"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                new GuardarText().saveFile(textosDAT, getFileNameDat(file));
                new GuardarText().saveFile(textosTXT, getFileNameTXT(file));
                dialogoGenerarDatCompletado();
            }
        });
        dialogStage.show(); 
    }
    
    public void dialogoGenerarDatCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Decimo Cuarto");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Generacion de archivos Dat y Txt completada."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void dialogoPagoDecimosCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de decimo cuarto");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los pagos de decimo cuarto con exito, \n"
                + " ¿Desea guardar los documento de pago?."), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimir(file, enviarCorreo.isSelected());
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                imprimir(null, enviarCorreo.isSelected());
            } else {
                dialogoCompletado();
            }
        });
        enviarCorreo.setSelected(true);
        dialogStage.show();
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de decimo Cuarto");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Pagos de decimos cuarto acumulado hecho con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        DateTime fechaActual = new DateTime();
        String anio = String.valueOf(fechaActual.getYear());
        
        yearLabel.setText(anio);
        
        checkBoxPagarTodos.setSelected(false);
        
         new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                loadingMode();
                                new Timer().schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            cancel();
                                            Platform.runLater(new Runnable() {
                                                @Override public void run() {
                                                    setTableInfo(); 
                                                    closeDialogMode();
                                                }
                                            });
                                        }
                                    }, 500, 500);
                            }
                        });
                    }
                }, 500, 500);
    }
    
    public void setTableInfo() {
        
        pdStart = pdDAO.getLastPagoStart4(Integer.valueOf(yearLabel.getText()));
        pdEnd = pdDAO.getLastPagoEnd4(Integer.valueOf(yearLabel.getText()));
        
        if (Integer.valueOf(yearLabel.getText()) > 2017) {
            if (pdStart == null || pdEnd == null) {
                buttonChange.setVisible(true);
            } else buttonChange.setVisible(false);
        } else buttonChange.setVisible(false);
        
        if (pdStart == null || pdEnd == null) {
            if (Integer.valueOf(yearLabel.getText()) == 2017) {
                pdStart = new PagoDecimo();
                pdStart.setFecha(new Timestamp(
                        Integer.valueOf(yearLabel.getText())-1-1900, 2, 1, 0, 0, 0, 0));
                pdStart.setMonto(Integer.valueOf(yearLabel.getText()).doubleValue());
                pdStart.setDecimo(Const.DECIMO_CUARTO_START);

                pdEnd = new PagoDecimo();
                pdEnd.setFecha(new Timestamp(
                        Integer.valueOf(yearLabel.getText())-1900, 1, 28, 0, 0, 0, 0));
                pdEnd.setMonto(Integer.valueOf(yearLabel.getText()).doubleValue());
                pdEnd.setDecimo(Const.DECIMO_CUARTO_END);

                pdDAO.save(pdStart);
                pdDAO.save(pdEnd);
            } else {
                pdEnd = pdDAO.getLastPagoEnd4(Integer.valueOf(yearLabel.getText())-1);
                if (pdEnd != null) {
                    Timestamp timestamp = new Timestamp(pdEnd.getFecha().getTime());
                    timestamp.setMonth(pdEnd.getFecha().getMonth()+1);
                    timestamp.setDate(1);
                    pdStart = new PagoDecimo();
                    pdStart.setFecha(timestamp);
                    pdStart.setMonto(Integer.valueOf(yearLabel.getText()).doubleValue());
                    pdStart.setDecimo(Const.DECIMO_CUARTO_START);

                    Timestamp timestampEnd = new Timestamp(pdEnd.getFecha().getTime());
                    timestampEnd.setYear(pdEnd.getFecha().getYear()+1);
                    pdEnd = new PagoDecimo();
                    pdEnd.setFecha(timestampEnd);
                    pdEnd.setMonto(Integer.valueOf(yearLabel.getText()).doubleValue());
                    pdEnd.setDecimo(Const.DECIMO_CUARTO_END);
                } else {
                    onClickLess(null);
                    return;
                }
            }
        }
        
        calcular();
    }
    
    void calcular() {
        periodoALiquidar = pdStart.getStringFecha()+" al "+pdEnd.getStringFecha();
        periodoLabel.setText("Periodo entre el "+periodoALiquidar);

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), 
                new Fecha("01","01", yearLabel.getText())));
        
        Timestamp fecha = new Timestamp(Integer.valueOf(yearLabel.getText())-1900, 0, 1, 0, 0, 0, 0);
        pagosDecimo = new ArrayList<>();
        pagosDecimo.addAll(pdDAO.findByDecimoAndFecha(Const.DECIMO_CUARTO, fecha));
        if (pagosDecimo.isEmpty() && Integer.valueOf(yearLabel.getText()) > 2017) {
            if (pdDAO.getLastPagoEnd4(Integer.valueOf(yearLabel.getText())) != null) {
                pdDAO.delete(pdStart);
                pdDAO.delete(pdEnd);
                HibernateSessionFactory.getSession().flush();
                setTableInfo();
                return;
            }
        }
        
        data = FXCollections.observableArrayList(); 
        for (Usuario user: usuarios) {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.setUsuario(user);
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
            
            Double monto = 0d;
            
            List<RolIndividual> rolesIndividual = new RolIndividualDAO()
                    .findAllByEmpleadoId(empleado.getId());
            
            ArrayList<RolIndividual> rolesDelParaPagar = new ArrayList<>();
            ArrayList<Double> decimos = new ArrayList<>();
            for (RolIndividual rol: rolesIndividual) {
                
                Fecha inicio = new Fecha(rol.getInicio());
                if (isBetween(inicio, pdStart, pdEnd)) {
                    Fecha inicioVac = new Fecha("01", "01", inicio.getAno())
                                    .minusYears(1);
                    PagoVacaciones pagoVacaciones = new PagoVacacionesDAO()
                            .findInDeterminateTimeByUsuarioId(inicioVac.getFecha(), empleado.getId());
                    
                    Double diasDecimo4to = getDiasDecimo4(user, 
                            new Fecha(rol.getInicio()), new Fecha(rol.getFinalizo()));
                    Double decimoCuarto = getDecimoCuarto()/30d * diasDecimo4to;
                    monto += decimoCuarto;
                    rolesDelParaPagar.add(rol);
                    decimos.add(round(decimoCuarto));
                }
            }
            empleado.setDecimosMes(decimos);
            empleado.setRolesInds(rolesDelParaPagar);
        
            empleado.setDecimo4(round(monto));
            empleado.setPagar(false);
            empleado.setAgregar(false);
            empleado.setPagado("No");
            
            for (PagoDecimo pagoDecimo: pagosDecimo) {
                if (pagoDecimo.getUsuario().getId().equals(user.getId())) {
                    empleado.setPagoDecimo(pagoDecimo);
                    empleado.setPagado("Si");
                    empleado.setDecimo4(round(pagoDecimo.getMonto()));
                }
            }
            
            data.add(empleado);  
        }
    
        empleadosTableView.setItems(data);
       
        filtro();
    }
    
    public Double getDiasDecimo4(Usuario empleado, Fecha fechaInicial, Fecha fechaFinal) {
        ArrayList<ControlDiario> controlesEmpleado = new ArrayList<>();
        controlesEmpleado.addAll(new ControlDiarioDAO()
                .findAllByEmpleadoIdInDeterminateTime(empleado.getId(), 
                        fechaInicial.getFecha(), fechaFinal.getFecha()));
        
        Double diasDecimo4to = 0d;
        for (ControlDiario control: controlesEmpleado) {
        
            if (control.getCaso().equalsIgnoreCase(Const.TRABAJO)
                    || control.getCaso().equalsIgnoreCase(Const.VACACIONES)
                    || control.getCaso().equalsIgnoreCase(Const.LIBRE)
                    || control.getCaso().equalsIgnoreCase(Const.CM)
                    || control.getCaso().equalsIgnoreCase(Const.DM)) {

                diasDecimo4to += 1;

            } 
        } 
        return diasDecimo4to;
    }
    
    public Boolean isBetween(Fecha fechaF, PagoDecimo start, PagoDecimo end) {
        if (start.isSameMore(fechaF)) {
            if (end.isSameLess(fechaF)) {
                return true;
            }
        }
        return false;
    }
    
    public void filtro() {
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
    
    public Double getDecimoCuarto() {
        if (constDecimoCuarto == null)
            constDecimoCuarto = (Constante) new ConstanteDAO().findUniqueResultByNombre(Const.DECIMO_CUARTO);
        if (constDecimoCuarto == null) {
            return 30.5d;
        } else {
            return Double.valueOf(constDecimoCuarto.getValor());
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
        
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("decimo4"));
        
        pagarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pagarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxpagar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxpagar);
                if (checkBoxpagar != null) {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                        checkBoxpagar.setDisable(true);
                    } else {
                        checkBoxpagar.setDisable(false);
                    }
                    checkBoxpagar.setSelected(empleadoTable.getPagar());
                }
                checkBoxpagar.setOnAction(event -> {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                       empleadoTable.setPagar(false);
                       checkBoxpagar.setSelected(false);
                    } else {
                        empleadoTable.setPagar(checkBoxpagar.isSelected());
                    }
                });
                
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
            }
        });
        
        imprimirColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        imprimirColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxImp = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                setGraphic(checkBoxImp);
                if (checkBoxImp != null) {
                    checkBoxImp.setSelected(empleadoTable.getAgregar());
                }
                
                checkBoxImp.setOnAction(event -> {
                    empleadoTable.setAgregar(checkBoxImp.isSelected());
                });
            } 
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    mostrarPagoDecimoCuarto(rowData);
                }
            });
           return row;
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
       
        buttonPagar.setTooltip(
            new Tooltip("Pagar a seleccionados")
        );
        buttonPagar.setOnMouseEntered((MouseEvent t) -> {
            buttonPagar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/pagar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonPagar.setOnMouseExited((MouseEvent t) -> {
            buttonPagar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/pagar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonBank.setTooltip(
            new Tooltip("Generar .DAT y .TXT \n(Selecionados en \"Imp.\")")
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
        buttonImprimir.setTooltip(
            new Tooltip("Imprimir \n(Selecionados en \"Imp.\")")
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
         buttonChange.setTooltip(
            new Tooltip("Cambiar fecha")
        );
        buttonChange.setOnMouseEntered((MouseEvent t) -> {
            buttonChange.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_change.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonChange.setOnMouseExited((MouseEvent t) -> {
            buttonChange.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_change.png'); "
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
        buttonChange.setVisible(false);
    } 
    
    public void mostrarPagoDecimoCuarto(EmpleadoTable empleadoTable) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDecimoCuartoDetalles.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Decimo Cuarto");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    DecimoCuartoDetallesController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(aplicacionControl);
                    controller.setPrograma(this);
                    controller.setEmpleado(empleadoTable, periodoALiquidar, yearLabel.getText());
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
    
    private void loadingModeImprimir(){
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stagePrincipal);//stage here is the stage of your webview
        dialog.initStyle(StageStyle.TRANSPARENT);
        loader = new Label("   Imprimiendo, por favor espere...");
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
    
    private void loadingModeUpdate(int current, int total){
        loader.setText("   Imprimiendo "+current+"/"+total+", espere...");
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
        public final Integer IMPRIMIR = 1;
        public final Integer BORRAR = 2;
        
        Integer opcion;
        File file;
        Boolean enviarCorreo;
        
        public DataBaseThread(Integer opcion){
            this.opcion = opcion;
        }
        
        public DataBaseThread(Integer opcion, File file, Boolean enviarCorreo){
            this.opcion = opcion;
            this.file = file;
            this.enviarCorreo = enviarCorreo;
        }

        @Override
        public void run() {
    
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        try {
                            if (Objects.equals(opcion, GUARDAR)) {
                                hacerPago();
                            } else if (Objects.equals(opcion, IMPRIMIR)) {
                                imprimir(file, enviarCorreo, empladosImprimir);
                            } 
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            closeDialogMode();
                        }
                    }
             }, 1000, 1000);
            
        }
        
        public void hacerPago() {
        
            empladosImprimir = new ArrayList<>();
            textosDAT = new ArrayList<>();
            textosTXT = new ArrayList<>();

            for (EmpleadoTable empleadoTable: data) {
                if (empleadoTable.getPagar()) {
                    PagoDecimo pagoDecimo = new PagoDecimo();
                    pagoDecimo.setUsuario(empleadoTable.getUsuario());
                    pagoDecimo.setFecha(new Timestamp(Integer.valueOf(yearLabel.getText())-1-1899, 0, 1, 0, 0, 0, 0));
                    pagoDecimo.setMonto(empleadoTable.getDecimo4());
                    pagoDecimo.setDecimo(Const.DECIMO_CUARTO);
                    new PagoDecimoDAO().save(pagoDecimo);
                    
                    empleadoTable.setPagoDecimo(pagoDecimo);
                    empleadoTable.setPagado("Si");
                    empleadoTable.setPagar(false);
                    
                    empladosImprimir.add(empleadoTable);
                
                    // Registro para auditar
                    String detalles = "hizo el pago de decimo  acumulados nro: " + pagoDecimo.getId() 
                            + " del lapso 1 de Ene "+String.valueOf(Integer.valueOf(yearLabel.getText())-1)
                            + " al 31 de Dic "+String.valueOf(Integer.valueOf(yearLabel.getText())-1) + " para el empleado " 
                            + empleadoTable.getApellido()+ " " + empleadoTable.getNombre();
                    aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    
                    textosDAT.add(crearLineaDAT(empleadoTable.getUsuario(), pagoDecimo.getMonto()));
                    textosTXT.add(crearLineaTXT(empleadoTable.getUsuario(), pagoDecimo.getMonto()));
                }
            }
            setTableInfo();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoPagoDecimosCompletado();
                }
            });
        }

        public void imprimir(File file, Boolean enviarCorreo, List<EmpleadoTable> empleados) {
        
            for (EmpleadoTable empleado: empleados) {
                PagoDecimo pagoDecimo = empleado.getPagoDecimo();
                Usuario user = pagoDecimo.getUsuario();

                ReporteRolDecimoCuarto datasource = new ReporteRolDecimoCuarto();
                datasource.addAll(empleado.getRolesInds());

                try {
                    InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_DECIMO_CUARTO);

                    Map<String, Object> parametros = new HashMap();
                    parametros.put("numero", pagoDecimo.getId().toString()); 
                    parametros.put("fecha_recibo", Fechas.getFechaConMes(Fechas.getFechaActual()));
                    parametros.put("empleado", empleado.getApellido()+ " " + empleado.getNombre());
                    parametros.put("cedula", empleado.getCedula());
                    parametros.put("cargo", empleado.getUsuario().getDetallesEmpleado().getCargo().getNombre());
                    parametros.put("empresa", empresa.getNombre());
                    parametros.put("siglas", empresa.getSiglas());
                    parametros.put("correo", "Correo: " + empresa.getEmail());
                    parametros.put("detalles", 
                                 "Ruc: " + empresa.getNumeracion() 
                            + " - Direccion: " + empresa.getDireccion() 
                            + " - Tel: " + empresa.getTelefono1());
                    parametros.put("devengado", Numeros.round(pagoDecimo.getMonto()).toString());
                    parametros.put("cobrar", Numeros.round(pagoDecimo.getMonto()).toString());
                    parametros.put("lapso", "del "+pdStart.getStringFecha()+ " al "+pdEnd.getStringFecha());

                    JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                    String filename = "pago_decimo_cuarto_" + pagoDecimo.getId();

                    if (file != null) {
                        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                    } 
                     if (enviarCorreo) {
                        File pdf = File.createTempFile(filename, ".pdf");
                        JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                        CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                                user.getEmail(), Const.ASUNTO_PAGO_DECIMO_TERCERO, 
                                "Recibo del pago del decimo cuarto acumulado desde el " 
                                        +pdStart.getStringFecha()+ " al "+pdEnd.getStringFecha(), 
                                pdf.getPath(), filename + ".pdf");
                    }

                } catch (JRException | IOException ex) {
                    Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            empladosImprimir.clear();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoCompletado();
                }
            });
        }
    }
    
    String crearLineaDAT(Usuario user, Double decimo4to) {
        String monto = Numeros.roundToString(decimo4to);
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
                            .getEmpresa().getNumeracion()+";0001;"+String.valueOf(Integer.valueOf(yearLabel.getText())-1)
                            +";"+"12"+";INS;"+user.getCedula()
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
    
    String crearLineaTXT(Usuario user, Double decimo4to) {
        String monto = Numeros.roundToString(decimo4to);
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
                +espacios+partEntera+partDecimal+"DECIMO CUARTO "
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
    
}