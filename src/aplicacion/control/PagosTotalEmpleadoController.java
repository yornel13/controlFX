/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolDePagoIndividual;
import aplicacion.control.tableModel.PagosTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.AbonoDeudaDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.PagoDAO;
import hibernate.dao.PagoMesDAO;
import hibernate.dao.PagoMesItemDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.AbonoDeuda;
import hibernate.model.Constante;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.Pago;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
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
import static aplicacion.control.util.Fechas.getFechaConMes;
import java.util.Objects;

/**
 *
 * @author Yornel
 */
public class PagosTotalEmpleadoController implements Initializable {

    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private ChoiceBox empleadosChoiceBox;
    
    @FXML
    private TextField cedulaField;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
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
    
    private Integer diasTextValor;

    private Integer normalesTextValor;

    private Integer suplementariasTextValor;

    private Integer sobreTiempoTextValor;

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
    
    
    public Timestamp inicio;
    public Timestamp fin;
    
    private Empresa empresa;
    public Usuario empleado;
    
    ArrayList<Usuario> usuarios;
    ArrayList<Pago> pagos;
    ArrayList<PagosTable> pagosTable;
    
    private ObservableList<PagosTable> data;
    
    private ArrayList<PagoMesItem> pagoMesItems;
    
    ArrayList<Deuda> deudasAPagar = new ArrayList<>();
    
    private RolIndividual pagoRol;
    
    @FXML
    private Label errorText;
    
    Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
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
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().plusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        if (empleado != null) {
            setTableInfo(inicio, fin, empleado.getId());
        }
    }
    
    @FXML
    public void onClickLess(ActionEvent event)  {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().minusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        if (empleado != null) {
            setTableInfo(inicio, fin, empleado.getId());
        }
    }
    
    @FXML
    public void onClickGestionarDeudas(ActionEvent event) {
        if (empleado != null) {
            if (aplicacionControl.permisos == null) {
                aplicacionControl.noLogeado();
            } else {
                if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaGestionDeudas.fxml"));
                        AnchorPane ventanaDeudas = (AnchorPane) loader.load();
                        Stage ventana = new Stage();
                        ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                        ventana.getIcons().add(new Image(stageIcon));
                        ventana.setResizable(false);
                        ventana.setMaxWidth(608);
                        ventana.initOwner(stagePrincipal);
                        Scene scene = new Scene(ventanaDeudas);
                        ventana.setScene(scene);
                        GestionDeudasController controller = loader.getController();
                        controller.setStagePrincipal(ventana);
                        controller.setProgramaPrincipal(aplicacionControl);
                        //controller.setPagoTotalController(this);
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
        } else {
            
        }
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        ReporteRolDePagoIndividual datasource = new ReporteRolDePagoIndividual();
        datasource.addAll(pagoMesItems);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_PAGO_INDIVIDUAL);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empleado", empleado.getNombre() + " " + empleado.getApellido());
            parametros.put("cedula", empleado.getCedula());
            parametros.put("cargo", empleado.getDetallesEmpleado().getCargo().getNombre());
            parametros.put("empresa", empleado.getDetallesEmpleado().getEmpresa().getNombre());
            parametros.put("numero", pagoRol.getId().toString()); 
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            parametros.put("total", round(aPercibirValor).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "rol_individual_" + pagoRol.getId();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
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
            }
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
        
    }
    
    public void generarRolIndividual() {
       
        dialogWait();
        
        new RolIndividualDAO().save(pagoRol);
        
        PagoMes pagoMes = new PagoMes();
        pagoMes.setFecha(new Timestamp(new Date().getTime()));
        pagoMes.setInicioMes(inicio);
        pagoMes.setFinMes(fin);
        pagoMes.setMonto(round(aPercibirValor));
        pagoMes.setUsuario(empleado);
        pagoMes.setRolIndividual(pagoRol);
        new PagoMesDAO().save(pagoMes);
        
        for (PagoMesItem pago: pagoMesItems) {
            pago.setPagoMes(pagoMes);
            new PagoMesItemDAO().save(pago);
        }
        
        for (Deuda deuda: deudasAPagar) {
            Double montoAPagar = round(deuda.getRestante() / (double) deuda.getCuotas());
            Integer newCuotas = deuda.getCuotas() - 1;
            if (newCuotas == 0) {
                deuda.setPagada(Boolean.TRUE);
                deuda.setRestante(0d);  
                deuda.setCuotas(newCuotas);
            } else {
                deuda.setPagada(Boolean.FALSE);
                deuda.setRestante(round(deuda.getRestante() - montoAPagar));
                deuda.setCuotas(newCuotas);
            }
            deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
            HibernateSessionFactory.getSession().flush();
            
            AbonoDeuda abonoDeuda = new AbonoDeuda();
            abonoDeuda.setDeuda(deuda);
            abonoDeuda.setFecha(new Timestamp(new Date().getTime()));
            abonoDeuda.setMonto(montoAPagar);
            abonoDeuda.setPagoMes(pagoMes);
            new AbonoDeudaDAO().save(abonoDeuda);  
        }
        
        textError.setTextFill(Color.YELLOW);
        textError.setText("Ya se creo el rol de pago individual de este mes");
        
        // Registro para auditar
        String detalles = "genero el rol individual nro: " + pagoRol.getId() 
                + " del lapso " + getFechaConMes(inicio)+ " a " + getFechaConMes(fin) 
                + " para el empleado " + empleado.getNombre() + " " + empleado.getApellido();
        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
        
        dialogLoading.close();
        
        dialogoGenerarRolCompletado();
    }
    
    public void dialogoGenerarRolCompletado() {
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
        children(new Text("Se genero el rol de pago individual con exito, \n"
                + " ¿Desea guardar el documento de pago?."), 
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
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_loading.png").toExternalForm();
        dialogLoading.getIcons().add(new Image(stageIcon));
        dialogLoading.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Cargando espere...")).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogLoading.show();
    }
    
    @FXML
    public void onClickGenerarRecibo(ActionEvent event) {
        
        if (empleado != null) {
           
            if (new RolIndividualDAO().findByFechaAndEmpleadoIdAndDetalles(fin, empleado.getId(), Const.ROL_PAGO_INDIVIDUAL) == null) {
            
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Generar Rol individual");
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
                children(new Text("¿Seguro que desea generar el rol individual para este empleado?"), hBox).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                buttonOk.setMinWidth(50);
                buttonNo.setMinWidth(50);
                buttonOk.setOnAction((ActionEvent e) -> {
                    dialogStage.close();
                    generarRolIndividual();
                    
                });
                buttonNo.setOnAction((ActionEvent e) -> {
                    dialogStage.close();
                });
                dialogStage.showAndWait();
                
            } else {
               {
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Rol individua");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonOk = new Button("ok");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
                    children(new Text("Ya el empleado tiene un rol individual para esta fecha."), buttonOk).
                    alignment(Pos.CENTER).padding(new Insets(10)).build()));
                    buttonOk.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                    });
                    buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                        dialogStage.close();
                    });
                    dialogStage.showAndWait();
                } 
            }
            
            
        } else {
            {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Rol individua");
                String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button buttonOk = new Button("ok");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
                children(new Text("No hay empleado seleccionado."), buttonOk).
                alignment(Pos.CENTER).padding(new Insets(10)).build()));
                buttonOk.setOnAction((ActionEvent e) -> {
                    dialogStage.close();
                });
                buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                    dialogStage.close();
                });
                dialogStage.showAndWait();
            }
        }
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        DateTime dateTime;
        
        dateTime = new DateTime(getToday().getTime());
        fin = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).getMillis());
        inicio = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).minusMonths(1).plusDays(1).getMillis());

        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
    }
    
    @FXML
    private void mostrarRegistro(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaFiltroEmpleados.fxml"));
        AnchorPane ventanaFiltro = (AnchorPane) loader.load();
        Stage ventana = new Stage();
        ventana.setTitle("Selecciona el empleado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        ventana.getIcons().add(new Image(stageIcon));
        ventana.setResizable(false);
        ventana.initOwner(stagePrincipal);
        Scene scene = new Scene(ventanaFiltro);
        ventana.setScene(scene);
        FiltrarEmpleadoController controller = loader.getController();
        controller.setStagePrincipal(ventana);
        controller.setProgramaPrincipal(aplicacionControl);
        controller.setPagosController(this);
        controller.setEmpresa(empresa);
        ventana.showAndWait();
   
    } 
    
    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
        setTableInfo(inicio, fin, this.empleado.getId());
    }
    
    public void setTableInfo(Timestamp inicio, Timestamp fin, Integer empleadoId) {
        this.inicio = inicio;
        this.fin = fin;
        PagoDAO pagoDAO = new PagoDAO();
        pagos = new ArrayList<>();
        pagosTable = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdConCliente(fin, empleadoId));
        if (pagos.isEmpty())
            pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdSinCliente(fin, empleadoId));
        
        pagoMesItems = new ArrayList<>();
        deudasAPagar = new ArrayList<>();
        
        diasTextValor = 0;
        normalesTextValor = 0;
        suplementariasTextValor = 0;
        sobreTiempoTextValor = 0;
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
            pagoTable.setExtra(pago.getMontoHorasSuplementarias() + pago.getMontoHorasSobreTiempo());
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
        sueldoText.setText(empleado.getDetallesEmpleado().getSueldo().toString());
        quincenaText.setText(empleado.getDetallesEmpleado().getQuincena().toString());
        if (empleado.getDetallesEmpleado().getAcumulaDecimos()) {
            decimosText.setText("Si");
        } else {
            decimosText.setText("No");
        }
        
        diasText.setText(diasTextValor.toString());   
        normalesText.setText(normalesTextValor.toString());
        suplementariasText.setText(suplementariasTextValor.toString());
        sobreTiempoText.setText(sobreTiempoTextValor.toString());
        sueldoTotalText.setText(String.format( "%.2f", sueldoTotalTextValor));
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Sueldo");
            rol.setIngreso(round(sueldoTotalTextValor));
            rol.setDias(diasTextValor);
            rol.setHoras(normalesTextValor);
            rol.setClave(Const.IP_SUELDO);
            pagoMesItems.add(rol);
        }
        extraText.setText(String.format( "%.2f", extraTextValor));
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Horas Extras");
            rol.setIngreso(extraTextValor);
            rol.setHoras(suplementariasTextValor + sobreTiempoTextValor);
            rol.setClave(Const.IP_HORAS_EXTRAS);
            pagoMesItems.add(rol);
        }
        bonosText.setText(String.format( "%.2f", bonosTextValor));
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Bonos");
            rol.setIngreso(bonosTextValor);
            rol.setClave(Const.IP_BONOS);
            pagoMesItems.add(rol);
        }
        vacacionesText.setText(String.format( "%.2f", vacacionesTextValor));
        subTotalText.setText(String.format( "%.2f", subTotalTextValor));
        decimosTotalText.setText(String.format( "%.2f", decimoTerceroTotalTextValor) + " + " + String.format( "%.2f", decimoCuartoTotalTextValor));
        totalText.setText(String.format( "%.2f", totalTextValor));
        
        // Calculando montos
        if (empleado.getDetallesEmpleado().getAcumulaDecimos()) {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor;
        } else {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor + decimosTotalTextValor;
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Decimo Tercero");
                rol.setIngreso(round(decimoTerceroTotalTextValor));
                rol.setClave(Const.IP_DECIMO_TERCERO);
                pagoMesItems.add(rol);
            }
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Decimo Cuarto");
                rol.setIngreso(round(decimoCuartoTotalTextValor));
                rol.setClave(Const.IP_DECIMO_CUARTO);
                pagoMesItems.add(rol);
            }
        }
        ieesValor = (ingresoValor/100d) * getIess();  // TODO, sacar de data base
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion(iessPorcentaje.getText());
            rol.setDeduccion(round(ieesValor));
            rol.setClave(Const.IP_IESS);
            pagoMesItems.add(rol);
        }
        quincenaValor = empleado.getDetallesEmpleado().getQuincena();
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Adelanto Quincenal");
            rol.setDeduccion(quincenaValor);
            rol.setClave(Const.IP_ADELANTO_QUINCENAL);
            pagoMesItems.add(rol);
        }
        deudasValor = getDeudas();
        deduccionesValor = ieesValor + quincenaValor + deudasValor;
        aPercibirValor = ingresoValor - deduccionesValor;
        
        montoIngresoText.setText(String.format( "%.2f", ingresoValor));
        montoIessText.setText(String.format( "%.2f", ieesValor));
        montoQuincenaText.setText(String.format( "%.2f", quincenaValor));
        montoDeudasText.setText(String.format( "%.2f", deudasValor));
        montoDeduccionesText.setText(String.format( "%.2f", deduccionesValor));
        montoAPercibirText.setText(String.format( "%.2f", aPercibirValor));
        
        {
            pagoRol = new RolIndividual();
            pagoRol.setDetalles(Const.ROL_PAGO_INDIVIDUAL);
            pagoRol.setFecha(new Timestamp(new Date().getTime()));
            pagoRol.setInicio(inicio);
            pagoRol.setFinalizo(fin);
            pagoRol.setDias(diasTextValor);
            pagoRol.setHorasNormales(Double.valueOf(normalesTextValor));
            pagoRol.setHorasSuplementarias(Double.valueOf(suplementariasTextValor));  // RC
            pagoRol.setHorasSobreTiempo(Double.valueOf(sobreTiempoTextValor));         // ST
            pagoRol.setTotalHorasExtras(Double.valueOf(sobreTiempoTextValor + suplementariasTextValor));
            pagoRol.setSalario(sueldoTotalTextValor);
            pagoRol.setMontoHorasSuplementarias(montoSumplementariasTextValor);
            pagoRol.setMontoHorasSobreTiempo(montoSobreTiempoTextValor);
            pagoRol.setBono(montoBonoTextValor);
            pagoRol.setTransporte(montoTransporteTextValor);
            pagoRol.setTotalBonos(bonosTextValor);
            pagoRol.setVacaciones(vacacionesTextValor);
            pagoRol.setSubtotal(subTotalTextValor);
            pagoRol.setDecimoTercero(decimoTerceroTotalTextValor);
            pagoRol.setDecimoCuarto(decimoCuartoTotalTextValor);
            pagoRol.setJubilacionPatronal(montoJubilacionTextValor);
            pagoRol.setAportePatronal(montoAportePatronalTextValor);
            pagoRol.setSeguros(montoSegurosTextValor);
            pagoRol.setUniformes(montoUniformasTextValor);
            pagoRol.setTotalIngreso(totalTextValor);
            pagoRol.setEmpleado(empleado.getNombre() + " " + empleado.getApellido());
            pagoRol.setCedula(empleado.getCedula());
            pagoRol.setEmpresa(empleado.getDetallesEmpleado().getEmpresa().getNombre());
            pagoRol.setSueldo(empleado.getDetallesEmpleado().getSueldo());
            pagoRol.setUsuario(empleado);
        }
        
        if (new RolIndividualDAO().findByFechaAndEmpleadoIdAndDetalles(fin, empleado.getId(), Const.ROL_PAGO_INDIVIDUAL) != null) {
            textError.setTextFill(Color.YELLOW);
            textError.setText("Ya se creo el rol de pago individual de este mes");
        } else {
            textError.setTextFill(Color.BLACK);
            textError.setText("Al creal el rol de pago individual los roles asociados (clientes o administrativos) no podran ser borrados");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        controlClienteTableView.setEditable(Boolean.FALSE);
        
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        columnaDias.setCellValueFactory(new PropertyValueFactory<>("dias"));
        
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
        
        pickerDe.setEditable(false);
        pickerHasta.setEditable(false);
        
    }   
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public double getIess() {
        ConstanteDAO constanteDao = new ConstanteDAO();
        Constante constante;
        constante = (Constante) constanteDao.findUniqueResultByNombre(Const.IESS);
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
        deudas.addAll(new DeudaDAO().findAllByUsuarioIdNoPagadaSinAplazar(empleado
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
    
    public Pago findPagoById(Integer id) {
        for (Pago pago: pagos) {
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
}
