/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteHorasTrabajadas;
import aplicacion.control.reports.ReporteRolDePagoIndividual;
import aplicacion.control.tableModel.PagosTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.PagoMesDAO;
import hibernate.dao.PagoMesItemDAO;
import hibernate.model.Constante;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.RolCliente;
import hibernate.model.PagoMes;
import hibernate.model.PagoMesItem;
import hibernate.model.RolIndividual;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
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
import java.util.Objects;
import javafx.scene.control.TableRow;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.RolIndividualDAO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import aplicacion.control.util.Numeros;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import hibernate.dao.ControlExtrasDAO;
import hibernate.model.ControlExtras;
import java.util.List;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;
import hibernate.dao.CuotaDeudaDAO;
import hibernate.model.CuotaDeuda;
import net.sf.jasperreports.engine.JREmptyDataSource;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;

/**
 *
 * @author Yornel
 */
public class PagoMensualPagadoController implements Initializable {

    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private PagoMensualController pagoMensualController;
    
    @FXML
    private ChoiceBox empleadosChoiceBox;
    
    @FXML
    private TextField cedulaField;
    
    @FXML
    private TableView controlClienteTableView;
    
    // Usuario datos
    @FXML
    private Label nombreText;
    
    @FXML
    private Label apellidoText;
    
    @FXML
    private Label cedulaText;
    
    @FXML
    private Label sueldoText;
    
    @FXML
    private Label quincenaText;
    
    @FXML
    private Label decimosText;
    
    @FXML
    private Label cargoText;
   
    /// Columnas
    @FXML
    private TableColumn columnaCliente;
    
    @FXML
    private TableColumn columnaDias;
    
    @FXML
    private TableColumn columnaHoras;
    
    @FXML
    private TableColumn columnaNormales;
    
    @FXML
    private TableColumn columnaSumplementarias;
    
    @FXML
    private TableColumn columnaSobreTiempo;
    
    @FXML
    private TableColumn columnaIngresos;
    
    @FXML
    private TableColumn columnaSueldo;
    
    @FXML
    private TableColumn columnaExtra;
    
    @FXML
    private TableColumn columnaBonos;
    
    @FXML
    private TableColumn columnaVacaciones;
    
    @FXML
    private TableColumn columnaSubTotal;
    
    @FXML
    private TableColumn columnaDecimos;
    
    @FXML
    private TableColumn columnaTercero;
    
    @FXML
    private TableColumn columnaCuarto;
    
    @FXML
    private TableColumn columnaTotal;
    
    // Totales Text
    
    @FXML
    private Label diasText;
    
    @FXML
    private Label normalesText;
    
    @FXML
    private Label suplementariasText;
    
    @FXML
    private Label sobreTiempoText;
    
    @FXML
    private Label sueldoTotalText;
    
    @FXML
    private Label extraText;
    
    @FXML
    private Label bonosText;
    
    @FXML
    private Label vacacionesText;
    
    @FXML
    private Label subTotalText;
    
    @FXML
    private Label decimosTotalText;
    
    @FXML
    private Label totalText;
    
     
    // Totales pagos y deducciones labels
    
    @FXML
    private Label montoIngresoText;
    
    @FXML
    private Label montoIessText;
    
    @FXML
    private Label montoQuincenaText;
    
    @FXML
    private Label montoDeudasText;
    
    @FXML
    private Label montoDeduccionesText;
    
    @FXML
    private Label montoAPercibirText;
    
    @FXML
    private Label iessPorcentaje;
    
    @FXML
    private Label textError;
    
    // Totales de el empleado
    
    private Double diasTextValor;

    private Double normalesTextValor;

    private Double suplementariasTextValor;

    private Double sobreTiempoTextValor;

    private Double sueldoTotalTextValor;

    private Double extraTextValor;

    private Double bonosTextValor;

    private Double vacacionesTextValor;

    private Double subTotalTextValor;

    private Double decimosTotalTextValor;
    
    private Double decimoTerceroTotalTextValor;
    
    private Double decimoCuartoTotalTextValor;

    private Double totalTextValor;
    
    private Double montoSumplementariasTextValor; // No Visible en ventana
    
    private Double montoSobreTiempoTextValor; // No Visible en ventana
    
    private Double montoBonoTextValor; // No Visible en ventana
    
    private Double montoTransporteTextValor; // No Visible en ventana
    
    private Double montoJubilacionTextValor; // No Visible en ventana
    
    private Double montoAportePatronalTextValor; // No Visible en ventana
    
    private Double montoSegurosTextValor; // No Visible en ventana
    
    private Double montoUniformasTextValor; // No Visible en ventana
   
    // Totales de a percibir
    
    private Double ingresoValor;

    private Double ieesValor;

    private Double quincenaValor;

    private Double deudasValor;
    
    private Double deduccionesValor;

    private Double aPercibirValor;
    
    
    public Fecha inicio;
    public Fecha fin;
    
    private Empresa empresa;
    public Usuario empleado;
    public RolIndividual rolIndividual;
    
    ArrayList<Usuario> usuarios;
    ArrayList<RolCliente> pagos;
    ArrayList<PagosTable> pagosTable;
    
    private ObservableList<PagosTable> data;
    
    private PagoMes pagoMes;
    private ArrayList<PagoMesItem> pagoMesItems;
    
    ArrayList<Deuda> deudasAPagar = new ArrayList<>();
    
    @FXML
    private Label errorText;
    
    Stage dialogLoading;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonBorrar;
    
    DeudaDAO deudaDAO = new DeudaDAO();
    ConstanteDAO constanteDAO = new ConstanteDAO();
    CuotaDeudaDAO cuotaDeudaDAO = new CuotaDeudaDAO();
    PagoMesDAO pagoMesDAO = new PagoMesDAO();
    RolClienteDAO rolClienteDAO = new RolClienteDAO();
    RolIndividualDAO rolIndividualDAO = new RolIndividualDAO();
    PagoMesItemDAO pagoMesItemDAO = new PagoMesItemDAO();
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setPagoMensualController(PagoMensualController pagoMensualController) {
        this.pagoMensualController = pagoMensualController;
    }
    
    @FXML
    private void selectCedula(ActionEvent event) {
        empleadosChoiceBox.setVisible(false);
        cedulaField.setVisible(true);
    }
    
    @FXML
    private void selectLista(ActionEvent event) {
        cedulaField.setVisible(false);
        empleadosChoiceBox.setVisible(true);
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        aplicacionControl.mostrarInEmpresa(empresa);
        stagePrincipal.close();
    }
   
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        ReporteRolDePagoIndividual datasource = new ReporteRolDePagoIndividual();
        datasource.addAll(pagoMesItems);
        
        List<ControlExtras> controlEmpleado = new ControlExtrasDAO()
                        .findAllByEmpleadoIdInDeterminateTime(empleado.getId(), inicio.minusDays(7).getDate(), fin.minusDays(7).getDate());

        String horasDetallado = "";
        
        for (RolCliente rolCliente: pagos) {
            horasDetallado += "Cliente "+rolCliente.getClienteNombre()
                    +":\n               Dias                     "+rolCliente.getDias()
                    +"\n               H. Normales        "+rolCliente.getHorasNormales()
                    +"\n               H. Recargo          "+rolCliente.getHorasSuplementarias()
                    +"\n               H. Sobretiempo   "+rolCliente.getHorasSobreTiempo()+"\n\n";
        }
        
        ReporteHorasTrabajadas horasSource = new ReporteHorasTrabajadas(inicio, fin);
        horasSource.addAll(controlEmpleado);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_PAGO_INDIVIDUAL);
            InputStream inputHoras = new FileInputStream(Const.REPORTE_HORAS_TRABAJADAS);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empleado", empleado.getNombre() + " " + empleado.getApellido());
            parametros.put("cedula", empleado.getCedula());
            parametros.put("cargo", empleado.getDetallesEmpleado().getCargo().getNombre());
            parametros.put("empresa", empleado.getDetallesEmpleado().getEmpresa().getNombre());
            parametros.put("numero", rolIndividual.getId().toString()); 
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
                parametros.put("total", round(aPercibirValor).toString());
            parametros.put("fecha_recibo", Fechas.getFechaConMes(rolIndividual.getFecha()));
            parametros.put("horas_detallado", horasDetallado);
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "rol_pago_" + rolIndividual.getId();
            
            ///////////////////// Horas trabajadas
            JasperDesign jasperDesignHoras = JRXmlLoader.load(inputHoras);
            JasperReport jasperReportHoras = JasperCompileManager.compileReport(jasperDesignHoras);
            JasperPrint jasperPrintHoras;
            if (controlEmpleado.isEmpty())
                jasperPrintHoras = JasperFillManager.fillReport(jasperReportHoras, parametros, new JREmptyDataSource());
            else
                jasperPrintHoras = JasperFillManager.fillReport(jasperReportHoras, parametros, horasSource);

            String filenameHoras = "hora_trabajadas_" + rolIndividual.getId();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrintHoras, file.getPath() + "\\" + filenameHoras +".pdf"); 
            } 
            if (enviarCorreo) {
                File pdf = File.createTempFile(filename, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                CorreoUtil.mandarCorreo(empleado.getDetallesEmpleado().getEmpresa().getNombre(), 
                        empleado.getEmail(), Const.ASUNTO_ROL_INDIVIDUAL, 
                        "Recibo de rol de pago del mes que empieza el " 
                                + getFechaConMes(inicio) 
                                + " y termina el " 
                                + getFechaConMes(fin), 
                        pdf.getPath(), filename + ".pdf");
                
                File pdfHoras = File.createTempFile(filenameHoras, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrintHoras, new FileOutputStream(pdfHoras));  
                CorreoUtil.mandarCorreo(empleado.getDetallesEmpleado().getEmpresa().getNombre(), 
                        empleado.getEmail(), Const.ASUNTO_HORAS, 
                        "Recibo de horas trabajadas en el mes que comienza el " 
                                + getFechaConMes(inicio) 
                                + " y termina el " 
                                + getFechaConMes(fin), 
                        pdfHoras.getPath(), filenameHoras + ".pdf");
            }
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagoMensualPagadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }  
    }
    
    @FXML
    public void borrarPago(ActionEvent event) {
        
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
                
                if (rolIndividual.getPagoDecimoTercero() == null 
                        && rolIndividual.getPagoDecimoCuarto() == null) {
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Precaución");
                    String stageIcon = AplicacionControl.class
                            .getResource("imagenes/icon_error.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    MaterialDesignButton buttonOk = new MaterialDesignButton("Si");
                    MaterialDesignButton buttonNo = new MaterialDesignButton("no");
                    HBox hBox = HBoxBuilder.create()
                            .spacing(10.0) //In case you are using HBoxBuilder
                            .padding(new Insets(5, 5, 5, 5))
                            .alignment(Pos.CENTER)
                            .children(buttonOk, buttonNo)
                            .build();
                    hBox.maxWidth(120);
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("¿Seguro que desea Borrar este Pago?"),
                             new Text("No podra recuperarlo!"), hBox).
                    alignment(Pos.CENTER).padding(new Insets(20)).build()));
                    buttonOk.setMinWidth(50);
                    buttonNo.setMinWidth(50);
                    buttonOk.setOnAction((ActionEvent e) -> {
                        dialogWait();
                        ExecutorService executor = Executors.newFixedThreadPool(1);
                        Runnable worker = new DataBaseThread();
                        executor.execute(worker);
                        executor.shutdown();
                        dialogStage.close();
                    });
                    buttonNo.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                    });
                    dialogStage.showAndWait();
                } else {
                    dialogoBorradoNoPermitido();
                }
     
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    public void dialogoBorradoNoPermitido() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Rol individua");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No se puede borrar el pago mensual porque tiene"),
                new Text("pagos de decimos asociados, borrelos primero."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void borradoTerminado() {
        for (Deuda deuda: deudaDAO
                .findAllByEmpleadoId(pagoMes.getUsuario().getId())) {
            HibernateSessionFactory.getSession().update(deuda);
        }
        stagePrincipal.close();
        dialogLoading.close();
        dialogoBorradoCompletado();
        pagoMensualController.setTableInfo(); 
    }
    
    @FXML
    public void reimprimirRecibo(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Rol individua");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Ya se genero el rol de pago individual de este empleado, \n"
                + " ¿Desea guardar el documento de pago nuevamente?."), 
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
        dialogStage.showAndWait();
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    public void dialogoBorradoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago Mensual");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se borro este pago con exito."), 
                new Text("(se reintegraron los abonos de deudas sí los habia)"),
                new Text("Verifique las deudas antes de volver a hacer el pago."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        buttonOk.prefWidth(80);
        dialogStage.showAndWait();
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Rol individua");
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
    
    public void dialogWait() {
        dialogLoading = new Stage();
        dialogLoading.initModality(Modality.APPLICATION_MODAL);
        dialogLoading.setResizable(false);
        dialogLoading.setTitle("Cargando...");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_loading.png").toExternalForm();
        dialogLoading.getIcons().add(new Image(stageIcon));
        dialogLoading.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new ProgressBar()).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogLoading.show();
    }
    
    public void setPago(RolIndividual pago, 
            Fecha inicio, Fecha fin) throws ParseException {
        this.rolIndividual = pago;
        this.empleado = pago.getUsuario();
        this.empresa = pago.getUsuario().getDetallesEmpleado().getEmpresa();
        this.inicio = inicio;
        this.fin = fin;
        
        setTableInfo(this.empleado.getId());
    }
    
    public void setTableInfo(Integer empleadoId) {
        pagos = new ArrayList<>();
        pagosTable = new ArrayList<>();
        pagos.addAll(rolClienteDAO.findAllByFechaAndEmpleadoIdConCliente(inicio.getFecha(), empleadoId));
        if (pagos.isEmpty())
            pagos.addAll(rolClienteDAO.findAllByFechaAndEmpleadoIdSinCliente(inicio.getFecha(), empleadoId));
        
        pagoMesItems = new ArrayList<>();
        deudasAPagar = new ArrayList<>();
        
        diasTextValor = 0d;
        normalesTextValor = 0d;
        suplementariasTextValor = 0d;
        sobreTiempoTextValor = 0d;
        sueldoTotalTextValor = 0d;
        extraTextValor = 0d;
        bonosTextValor = 0d;
        vacacionesTextValor = 0d;
        subTotalTextValor = 0d;
        decimosTotalTextValor = 0d;
        decimoTerceroTotalTextValor = 0d;
        decimoCuartoTotalTextValor = 0d;
        totalTextValor = 0d;
        montoSumplementariasTextValor = 0d; // No Visible en ventana
        montoSobreTiempoTextValor = 0d; // No Visible en ventana
        montoBonoTextValor = 0d; // No Visible en ventana
        montoTransporteTextValor = 0d; // No Visible en ventana
        montoJubilacionTextValor = 0d; // No Visible en ventana
        montoAportePatronalTextValor = 0d; // No Visible en ventana
        montoSegurosTextValor = 0d; // No Visible en ventana
        montoUniformasTextValor = 0d; // No Visible en ventana
        
        ingresoValor = 0d;
        ieesValor = 0d;
        quincenaValor = 0d;
        deudasValor = 0d;
        deduccionesValor = 0d;
        aPercibirValor = 0d;
        
        pagos.stream().forEach((pago) -> {
            PagosTable pagoTable = new PagosTable();
            pagoTable.setId(pago.getId());
            if (pago.getClienteNombre() == null)
                pagoTable.setCliente("*Administrativo*");
            else
                pagoTable.setCliente(pago.getClienteNombre());
            pagoTable.setDias(pago.getDias());
            pagoTable.setNormales(pago.getHorasNormales());
            pagoTable.setSuplementarias(pago.getHorasSuplementarias());
            pagoTable.setSobreTiempo(pago.getHorasSobreTiempo());
            pagoTable.setSueldo(pago.getSalario());
            pagoTable.setExtra(round(pago.getMontoHorasSuplementarias() 
                    + pago.getMontoHorasSobreTiempo()));
            pagoTable.setBonos(pago.getTotalBonos());
            pagoTable.setVacaciones(pago.getVacaciones());
            pagoTable.setSubtotal(pago.getSubtotal());
            pagoTable.setDecimos(pago.getDecimoCuarto() + pago.getDecimoTercero());
            pagoTable.setTercero(pago.getDecimoTercero());
            pagoTable.setCuarto(pago.getDecimoCuarto());
            pagoTable.setTotal(pago.getTotalIngreso());
            pagosTable.add(pagoTable);
            
            diasTextValor += pagoTable.getDias();
            normalesTextValor += pagoTable.getNormales();
            suplementariasTextValor += pagoTable.getSuplementarias();
            sobreTiempoTextValor += pagoTable.getSobreTiempo();
            sueldoTotalTextValor += pagoTable.getSueldo();
            extraTextValor += pagoTable.getExtra();
            bonosTextValor += pagoTable.getBonos();
            vacacionesTextValor += pagoTable.getVacaciones();
            subTotalTextValor += pagoTable.getSubtotal();
            decimosTotalTextValor += pagoTable.getDecimos();
            decimoTerceroTotalTextValor += pagoTable.getTercero();
            decimoCuartoTotalTextValor += pagoTable.getCuarto();
            totalTextValor += pagoTable.getTotal();
            
            montoSumplementariasTextValor += pago.getMontoHorasSuplementarias(); // No Visible en ventana
            montoSobreTiempoTextValor += pago.getMontoHorasSobreTiempo(); // No Visible en ventana
            montoBonoTextValor += pago.getBono(); // No Visible en ventana
            montoTransporteTextValor += pago.getTransporte(); // No Visible en ventana
            montoJubilacionTextValor += pago.getJubilacionPatronal(); // No Visible en ventana
            montoAportePatronalTextValor += pago.getAportePatronal(); // No Visible en ventana
            montoSegurosTextValor += pago.getSeguros(); // No Visible en ventana
            montoUniformasTextValor += pago.getUniformes(); // No Visible en ventana
        });
        
        data = FXCollections.observableArrayList(); 
        data.addAll(pagosTable);
        
        controlClienteTableView.setItems(data);
        errorText.setText("");
        
        nombreText.setText(empleado.getNombre());
        apellidoText.setText(empleado.getApellido());
        cedulaText.setText(empleado.getCedula());
        cargoText.setText(empleado.getDetallesEmpleado().getCargo().getNombre());
        sueldoText.setText(rolIndividual.getSueldo().toString());
        quincenaText.setText(empleado.getDetallesEmpleado().getQuincena().toString());
        decimosText.setText("Si");
        
        
        diasText.setText(Numeros.roundInt(rolIndividual.getDias()).toString());   
        normalesText.setText(rolIndividual.getHorasNormales().toString());
        suplementariasText.setText(rolIndividual.getHorasSuplementarias().toString());
        sobreTiempoText.setText(rolIndividual.getHorasSobreTiempo().toString());
        sueldoTotalText.setText(String.format( "%.2f", rolIndividual.getSalario()));
        extraText.setText(String.format( "%.2f", 
                rolIndividual.getMontoHorasSobreTiempo() 
                + rolIndividual.getMontoHorasSuplementarias()));
        bonosText.setText(String.format( "%.2f", rolIndividual.getTotalBonos()));
        vacacionesText.setText(String.format( "%.2f", rolIndividual.getVacaciones()));
        subTotalText.setText(String.format( "%.2f", rolIndividual.getSubtotal()));
        decimosTotalText.setText(String.format( "%.2f", rolIndividual.getDecimoTercero()) 
                + " + " + String.format( "%.2f", rolIndividual.getDecimoCuarto()));
        totalText.setText(String.format( "%.2f", rolIndividual.getTotalIngreso()));
        
        pagoMes = pagoMesDAO.findByRolIndividual(rolIndividual.getId());
        pagoMesItems.addAll(pagoMesItemDAO.findByPagoMesId(pagoMes.getId()));
        for (PagoMesItem pagoMesItem: pagoMesItems) {
            if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_IESS)) {
                ieesValor = pagoMesItem.getDeduccion();
                iessPorcentaje.setText(pagoMesItem.getDescripcion());
            } else if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_ADELANTO_QUINCENAL)) {
                quincenaValor = pagoMesItem.getDeduccion();
            } else if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DEUDA)) {
                deudasValor += pagoMesItem.getDeduccion();
            } 
            
            if (pagoMesItem.getIngreso() != null) {
                ingresoValor += pagoMesItem.getIngreso();
            }
            
            if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DECIMO_CUARTO)) {
               decimosText.setText("No");
            }
        }
        deduccionesValor = ieesValor + quincenaValor + deudasValor;
        aPercibirValor = ingresoValor - deduccionesValor;
        montoIngresoText.setText(String.format( "%.2f", ingresoValor));
        montoIessText.setText(String.format( "%.2f", ieesValor));
        montoQuincenaText.setText(String.format( "%.2f", quincenaValor));
        montoDeudasText.setText(String.format( "%.2f", deudasValor));
        montoDeduccionesText.setText(String.format( "%.2f", deduccionesValor));
        montoAPercibirText.setText(String.format( "%.2f", pagoMes.getMonto()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        controlClienteTableView.setEditable(Boolean.FALSE);
        
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        columnaDias.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<PagosTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<PagosTable, String> data) {
                return new ReadOnlyStringWrapper(Numeros.roundInt(data.getValue()
                        .getDias()).toString());
            }
        });
        
        columnaNormales.setCellValueFactory(new PropertyValueFactory<>("normales"));
        
        columnaSumplementarias.setCellValueFactory(new PropertyValueFactory<>("suplementarias"));
        
        columnaSobreTiempo.setCellValueFactory(new PropertyValueFactory<>("sobreTiempo"));
        
        columnaHoras.getColumns().clear();
        columnaHoras.getColumns().addAll(columnaNormales, columnaSumplementarias, columnaSobreTiempo);
        
        columnaSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        
        columnaExtra.setCellValueFactory(new PropertyValueFactory<>("extra"));
        
        columnaBonos.setCellValueFactory(new PropertyValueFactory<>("bonos"));
        
        columnaIngresos.getColumns().clear();
        columnaIngresos.getColumns().addAll(columnaSueldo, columnaExtra, columnaBonos);
        
        columnaVacaciones.setCellValueFactory(new PropertyValueFactory<>("vacaciones"));
        
        columnaSubTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        columnaTercero.setCellValueFactory(new PropertyValueFactory<>("tercero"));
        columnaCuarto.setCellValueFactory(new PropertyValueFactory<>("cuarto"));
        
        columnaDecimos.getColumns().clear();
        columnaDecimos.getColumns().addAll(columnaTercero, columnaCuarto);
        
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        controlClienteTableView.setRowFactory((Object tv) -> {
            TableRow<PagosTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    PagosTable rowData = row.getItem();
                    aplicacionControl.mostrarRolClienteEmpleado(rolClienteDAO
                            .findById(rowData.getId()));
                }
            });
            return row ;
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
        
        buttonBorrar.setTooltip(
            new Tooltip("Borrar Pago")
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
    }   
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public double getIess() {
        Constante constante;
        constante = (Constante) constanteDAO.findUniqueResultByNombre(Const.IESS);
        if (constante == null) {
            iessPorcentaje.setText("IESS (0.0%)");
            return 0.0;
        } else {
            iessPorcentaje.setText(Const.IP_IESS + " (" + constante.getValor() + "%)");
            return Double.valueOf(constante.getValor());
        }
    }
    
    public Double getDeudas() {
        Double monto = 0d;
        ArrayList<Deuda> deudas = new ArrayList<>();
        deudas.addAll(deudaDAO.findAllByUsuarioIdNoPagadaSinAplazar(empleado
                .getId()));
        deudasAPagar.addAll(deudas);
        for (Deuda deuda: deudas) {
            monto += (deuda.getRestante() / deuda.getCuotas());
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Deuda - " + deuda.getTipo());
                rol.setDeduccion(deuda.getRestante() / deuda.getCuotas());
                rol.setClave(Const.IP_DEUDA);
                pagoMesItems.add(rol);
                
            }
            
        }
        return monto;
    }
    
    public RolCliente findPagoById(Integer id) {
        for (RolCliente pago: pagos) {
            if (Objects.equals(pago.getId(), id)) {
                return  pago;
            }
        }
        return null;
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

            try {
                int pagoId = pagoMes.getId();
        
                for (CuotaDeuda cuota: cuotaDeudaDAO.findAllByPagoId(pagoId)) {
                    Deuda deuda = cuota.getDeuda();
                    deuda.setRestante(deuda.getRestante() + cuota.getMonto());
                    deuda.setCuotas(deuda.getCuotas() + 1);
                    deuda.setPagada(Boolean.FALSE);
                    
                    cuota.setEditado(new Timestamp(new Date().getTime()));
                    cuota.setPagoMes(null);
                    
                    HibernateSessionFactory.getSession().flush();
                }
                for (PagoMesItem pagoMesItem: pagoMesItems) {
                    pagoMesItemDAO.delete(pagoMesItem);
                }
                pagoMesDAO.delete(pagoMesDAO.findById(pagoId));
                rolIndividualDAO.delete(rolIndividualDAO
                        .findById(rolIndividual.getId()));
                HibernateSessionFactory.getSession().flush();
                String detalles = "elemino el pago mensual numero "+pagoId 
                        +", del empleado " + pagoMes.getUsuario().getNombre() 
                        +" "+pagoMes.getUsuario().getApellido();
                aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
              
            } catch (Exception e) {
                e.printStackTrace();
            }
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    borradoTerminado();  
                }
            });
        }

    }
}
