/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.PagosTotalEmpleadoController.getToday;
import aplicacion.control.reports.ReporteAcumulacionDecimosVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Roboto;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.RolIndividual;
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
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.joda.time.DateTime;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;

/**
 *
 * @author Yornel
 */
public class DecimosAcumuladoEmpleadosController implements Initializable {
    
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
    private TableColumn cargoColumna;
    
    @FXML 
    private TableColumn terceroColumna;
    
    @FXML 
    private TableColumn cuartoColumna;
    
    @FXML 
    private TableColumn detallesD3Columna;
    
    @FXML 
    private TableColumn detallesD4Columna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonCambiarVentana;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    public Timestamp inicio;
    public Timestamp fin;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    private Stage dialogLoading;
    
    public static final String PAGADO = "Pagado";
    public static final String RETENIDO = "Retenido";
    public static final String SIN_GENERAR = "Sin Generar";    
    
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
    public void onClickMore(ActionEvent event) throws ParseException {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());   
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) throws ParseException  {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        setTableInfo();
    }
    
    @FXML
    public void cambiarVentana(ActionEvent event) {
        aplicacionControl.mostrarDecimosEmpleados(empresa, stagePrincipal);
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
        /*
        Double pagado3 = 0d;
        Double pagado4 = 0d;
        Double pagado;
        Double retenido3 = 0d;
        Double retenido4 = 0d;
        Double retenido;
        Double total3 = 0d;
        Double total4 = 0d;
        Double total;
        
        dialogWait();
        
        List<EmpleadoTable> empleadosImprimir = (List<EmpleadoTable>) empleadosTableView.getItems();
        
        ReporteAcumulacionDecimosVarios datasource = new ReporteAcumulacionDecimosVarios();
        datasource.addAll(empleadosImprimir);
        
        for (EmpleadoTable empleadoTable: empleadosImprimir) {
            total3 += empleadoTable.getDecimo3();
            total4 += empleadoTable.getDecimo4();
            if (empleadoTable.getDetalles().equalsIgnoreCase(PAGADO)) {
                pagado3 += empleadoTable.getDecimo3();
                pagado4 += empleadoTable.getDecimo4();
            } else if (empleadoTable.getDetalles().equalsIgnoreCase(RETENIDO)) {
                retenido3 += empleadoTable.getDecimo3();
                retenido4 += empleadoTable.getDecimo4();
            }
        }
        
        pagado = pagado3 + pagado4;
        retenido = retenido3 + retenido4;
        total = total3 + total4;
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DECIMOS_ACUMULADOS_POR_MES);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            parametros.put("pagado3", round(pagado3).toString());
            parametros.put("pagado4", round(pagado4).toString());
            parametros.put("pagado", round(pagado).toString());
            parametros.put("retenido3", round(retenido3).toString());
            parametros.put("retenido4", round(retenido4).toString());
            parametros.put("retenido", round(retenido).toString());
            parametros.put("total3", round(total3).toString());
            parametros.put("total4", round(total4).toString());
            parametros.put("total", round(total).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if(empleadosImprimir.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else 
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "acumulado_decimos_mensual_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo general de acumulacion de decimos de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }*/
    }
    
    public void imprimirDecimoTercero(File file) {
        
        Double pagado3 = 0d;
        Double retenido3 = 0d;
        Double total3 = 0d;
        
        dialogWait();
        
        List<EmpleadoTable> empleadosImprimir = (List<EmpleadoTable>) empleadosTableView.getItems();
        
        ReporteAcumulacionDecimosVarios datasource = new ReporteAcumulacionDecimosVarios();
        datasource.addAll(empleadosImprimir);
        
        for (EmpleadoTable empleadoTable: empleadosImprimir) {
            total3 += empleadoTable.getDecimo3();
            if (empleadoTable.getDetallesD3().equalsIgnoreCase(PAGADO)) {
                pagado3 += empleadoTable.getDecimo3();
            } else if (empleadoTable.getDetallesD3().equalsIgnoreCase(RETENIDO)) {
                retenido3 += empleadoTable.getDecimo3();
            }
        }
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DECIMO_TERCERO_ACUMULADOS_POR_MES);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            parametros.put("pagado3", round(pagado3).toString());
            parametros.put("retenido3", round(retenido3).toString());
            parametros.put("total3", round(total3).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if(empleadosImprimir.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else 
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "acumulado_decimo_tercero_mensual_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo general de acumulacion de decimo "
                    + "tercero de todos los empleado de " 
                    + getFechaConMes(inicio) + " al " + getFechaConMes(fin);
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    public void imprimirDecimoCuarto(File file) {
        
        Double pagado4 = 0d;
        Double retenido4 = 0d;
        Double total4 = 0d;
        
        dialogWait();
        
        List<EmpleadoTable> empleadosImprimir = (List<EmpleadoTable>) empleadosTableView.getItems();
        
        ReporteAcumulacionDecimosVarios datasource = new ReporteAcumulacionDecimosVarios();
        datasource.addAll(empleadosImprimir);
        
        for (EmpleadoTable empleadoTable: empleadosImprimir) {
            total4 += empleadoTable.getDecimo4();
            if (empleadoTable.getDetallesD4().equalsIgnoreCase(PAGADO)) {
                pagado4 += empleadoTable.getDecimo4();
            } else if (empleadoTable.getDetallesD4().equalsIgnoreCase(RETENIDO)) {
                retenido4 += empleadoTable.getDecimo4();
            }
        }
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DECIMO_CUARTO_ACUMULADOS_POR_MES);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            parametros.put("pagado4", round(pagado4).toString());
            parametros.put("retenido4", round(retenido4).toString());
            parametros.put("total4", round(total4).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if(empleadosImprimir.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else 
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "acumulado_decimo_cuarto_mensual_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo general de acumulacion de decimo "
                    + "cuarto de todos los empleado de " 
                    + getFechaConMes(inicio) + " al " + getFechaConMes(fin);
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
        dialogStage.setTitle("Decimos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("La impresión fue completado."), buttonOk).
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
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonTercero = new MaterialDesignButton("DECIMO TERCERO");
        Button buttonCuarto = new MaterialDesignButton("DECIMO CUARTO");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonTercero, buttonCuarto)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Que desea imprimir?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
       
        buttonTercero.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
        buttonTercero.setMinHeight(25);
        buttonTercero.setMaxWidth(90);
        buttonTercero.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            rutaGuardadoDecimoTercero();
        });
        buttonTercero.setOnMouseEntered((MouseEvent t) -> {
            buttonTercero.setStyle("-fx-background-color: #E0E0E0; "
                    + "-fx-text-fill: white;");
        });
        buttonTercero.setOnMouseExited((MouseEvent t) -> {
            buttonTercero.setStyle("-fx-background-color: #039BE5; "
                    + "-fx-text-fill: white;");
        });
        buttonTercero.setFont(Roboto.MEDIUM(9));
            
        buttonCuarto.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
        buttonCuarto.setMinHeight(25);
        buttonCuarto.setMaxWidth(90);
        buttonCuarto.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            rutaGuardadoDecimoCuarto();
        });
        buttonCuarto.setOnMouseEntered((MouseEvent t) -> {
            buttonCuarto.setStyle("-fx-background-color: #E0E0E0; "
                    + "-fx-text-fill: white;");
        });
        buttonCuarto.setOnMouseExited((MouseEvent t) -> {
            buttonCuarto.setStyle("-fx-background-color: #039BE5; "
                    + "-fx-text-fill: white;");
        });
        buttonCuarto.setFont(Roboto.MEDIUM(9));
        
        dialogStage.show();
    }
    
    public void rutaGuardadoDecimoTercero() {
         Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new MaterialDesignButton("SELECCIONAR RUTA");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se imprimira el informe de los decimos tercero acumulados en el mes selecionado,"), 
                new Text("Seleccione la ruta de guardado"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonSiDocumento.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
        buttonSiDocumento.setMinHeight(30);
        buttonSiDocumento.setMaxWidth(120);
        buttonSiDocumento.setOnMouseEntered((MouseEvent t) -> {
            buttonSiDocumento.setStyle("-fx-background-color: #E0E0E0; "
                    + "-fx-text-fill: white;");
        });
        buttonSiDocumento.setOnMouseExited((MouseEvent t) -> {
            buttonSiDocumento.setStyle("-fx-background-color: #039BE5; "
                    + "-fx-text-fill: white;");
        });
        buttonSiDocumento.setFont(Roboto.MEDIUM(9));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimirDecimoTercero(file);
            }
        });
        dialogStage.show();
    }
    
     public void rutaGuardadoDecimoCuarto() {
         Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new MaterialDesignButton("SELECCIONAR RUTA");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se imprimira el informe de los decimos cuarto acumulados en el mes selecionado,"), 
                new Text("Seleccione la ruta de guardado"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonSiDocumento.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
        buttonSiDocumento.setMinHeight(30);
        buttonSiDocumento.setMaxWidth(120);
        buttonSiDocumento.setOnMouseEntered((MouseEvent t) -> {
            buttonSiDocumento.setStyle("-fx-background-color: #E0E0E0; "
                    + "-fx-text-fill: white;");
        });
        buttonSiDocumento.setOnMouseExited((MouseEvent t) -> {
            buttonSiDocumento.setStyle("-fx-background-color: #039BE5; "
                    + "-fx-text-fill: white;");
        });
        buttonSiDocumento.setFont(Roboto.MEDIUM(9));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimirDecimoCuarto(file);
            }
        });
        dialogStage.show();
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
       
        DateTime dateTime = new DateTime(getToday().getTime());
        if (dateTime.getDayOfMonth() >= empresa.getComienzoMes() ) {
            inicio = new Timestamp(dateTime.withDayOfMonth(empresa
                    .getComienzoMes()).getMillis());
            fin = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                    .plusMonths(1).minusDays(1).getMillis());
        } else {
            fin = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                    .minusDays(1).getMillis());
            inicio = new Timestamp(dateTime.withDayOfMonth(empresa
                    .getComienzoMes()).minusMonths(1).getMillis());
        }
        pickerDe.setValue(Fechas.getLocalFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getLocalFromTimestamp(fin));
       
       setTableInfo();
    }
    
    public void setTableInfo() {
        
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
            empleado.setEmpresa(user.getDetallesEmpleado()
                    .getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado()
                    .getCargo().getNombre());
            empleado.setAcumulaDecimos(user.getDetallesEmpleado()
                    .getAcumulaDecimos());
            
            RolIndividualDAO rolDao = new RolIndividualDAO();
            
            RolIndividual rolIndividual = rolDao.findByFechaAndEmpleadoIdAndDetalles(inicio, 
                    user.getId(), Const.ROL_PAGO_INDIVIDUAL);
            
            if (rolIndividual != null) {
                
                empleado.setDecimo3(round(rolIndividual.getDecimoTercero()));
                empleado.setDecimo4(round(rolIndividual.getDecimoCuarto()));
                
                if (rolIndividual.getDecimosPagado()) {
                    empleado.setDetallesD3(PAGADO);
                    empleado.setDetallesD4(PAGADO);
                } else {
                    empleado.setDetallesD3(RETENIDO);
                    empleado.setDetallesD4(RETENIDO);
                    if (rolIndividual.getPagoDecimoTercero() != null ) {
                        empleado.setDetallesD3(PAGADO);
                    }
                    if (rolIndividual.getPagoDecimoCuarto() != null ) {
                        empleado.setDetallesD4(PAGADO);
                    }
                }
            } else {
                empleado.setDecimo3(0d);
                empleado.setDecimo4(0d);
                empleado.setDetallesD3(SIN_GENERAR);
                empleado.setDetallesD4(SIN_GENERAR);
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
    
    public void mostrarDecimosGenerados(Usuario empleado, EmpleadoTable empleadoTable) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDecimosGenerado.fxml"));
            AnchorPane ventanaAuditar = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Decimos del empleado " + empleado.getApellido()+ " " 
                    + empleado.getNombre());
            String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaAuditar);
            ventana.setScene(scene);
            DecimosGeneradoController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(aplicacionControl);
            controller.setEmpleado(empleado, inicio, fin, empleadoTable);
            ventana.show();

        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        } 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        terceroColumna.setCellValueFactory(new PropertyValueFactory<>("decimo3"));
        
        cuartoColumna.setCellValueFactory(new PropertyValueFactory<>("decimo4"));
        
        detallesD3Columna.setCellValueFactory(new PropertyValueFactory<>("detallesD3"));
        
        detallesD4Columna.setCellValueFactory(new PropertyValueFactory<>("detallesD4"));
       
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    //if (!rowData.getDetalles().equalsIgnoreCase(SIN_GENERAR))
                        mostrarDecimosGenerados(new UsuarioDAO()
                                .findById(rowData.getId()), rowData);
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
        buttonCambiarVentana.setTooltip(
            new Tooltip("Ver quienes acumulan")
        );
        buttonCambiarVentana.setOnMouseEntered((MouseEvent t) -> {
            buttonCambiarVentana.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cambiar_ventana.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonCambiarVentana.setOnMouseExited((MouseEvent t) -> {
            buttonCambiarVentana.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cambiar_ventana.png'); "
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
