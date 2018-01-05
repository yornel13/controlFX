/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteHorasTrabajadas;
import aplicacion.control.reports.ReporteRolDePagoIndividual;
import aplicacion.control.reports.ReporteRolGeneralMensual;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.PagoMesDAO;
import hibernate.dao.PagoMesItemDAO;
import hibernate.dao.PagoQuincenaDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Constante;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.RolCliente;
import hibernate.model.PagoMes;
import hibernate.model.PagoMesItem;
import hibernate.model.PagoQuincena;
import hibernate.model.RolIndividual;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import aplicacion.control.util.GuardarText;
import aplicacion.control.util.Numeros;
import hibernate.dao.ControlExtrasDAO;
import hibernate.model.ControlExtras;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;
import aplicacion.control.util.MaterialDesignButton;
import hibernate.dao.CuotaDeudaDAO;
import hibernate.dao.PagoVacacionesDAO;
import hibernate.model.CuotaDeuda;
import hibernate.model.PagoVacaciones;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class PagoMensualController implements Initializable {
    
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
    private TableColumn sueldoColumna;
    
    @FXML 
    private TableColumn pagadoColumna;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML
    private CheckBox checkBoxPagarTodos1;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> imprimirColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Button buttonBank;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
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
    
    private ObservableList<EmpleadoTable> data;
    
    private ArrayList<EmpleadoTable> empleadosParaImprimir;
    private ArrayList<Usuario> usuariosParaImprimir;
    private ArrayList<String> textosDAT;
    private ArrayList<String> textosTXT;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Dialog<Void> dialog;
    
    Stage dialogLoading;
    
    Label loader;
    
    Constante iess;
    
    String iessText = "IESS (0.0%)";
    
    ArrayList<PagoQuincena> pagosQuincena;
    
    DeudaDAO deudaDAO = new DeudaDAO();
    CuotaDeudaDAO cuotaDeudaDAO = new CuotaDeudaDAO();
    ConstanteDAO constanteDAO = new ConstanteDAO();
    PagoMesDAO pagoMesDAO = new PagoMesDAO();
   
    
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
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoMensualController.DataBaseThread(99);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        inicio = inicio.minusMonths(1);
        fin = fin.minusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa); 
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoMensualController.DataBaseThread(99);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    @FXML
    private void pagarATodos(ActionEvent event) throws ParseException {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxPagarTodos.isSelected()) {
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")
                            || empleadoTable.getSinRoles()
                        || empleadoTable.getSueldo() < 0d) {
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
    private void pagarATodos1(ActionEvent event) throws ParseException {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            
            empleadoTable.setAgregar(checkBoxPagarTodos1.isSelected());
            
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
     public void imprimir(File file) {
        
        dialogWait();
        Double dias = 0d;
        Double salario = 0d;
        Double sobretiempo = 0d;
        Double recargo = 0d;
        Double transporte = 0d;
        Double subtotal = 0d;
        Double tercero = 0d;
        Double cuarto = 0d;
        Double ingresos = 0d;
        Double anticipo = 0d;
        Double iess = 0d;
        Double descuentos = 0d;
        Double neto = 0d;
        
        List<EmpleadoTable> lista = new ArrayList<>();
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) data) {
            if (empleadoTable.getAgregar()) {
                lista.add(empleadoTable);
                dias += empleadoTable.getRolIndividual().getDias();
                salario += empleadoTable.getRolIndividual().getSalario();
                sobretiempo += empleadoTable.getRolIndividual().getMontoHorasSobreTiempo();
                recargo += empleadoTable.getRolIndividual().getMontoHorasSuplementarias();
                transporte += empleadoTable.getRolIndividual().getTransporte();
                transporte += empleadoTable.getRolIndividual().getBono();
                subtotal += empleadoTable.getRolIndividual().getSubtotal();
                for (PagoMesItem pagoMesItem: empleadoTable.getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DECIMO_TERCERO)) {
                        tercero += pagoMesItem.getIngreso();
                    }
                }
                for (PagoMesItem pagoMesItem: empleadoTable.getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DECIMO_CUARTO)) {
                        cuarto += pagoMesItem.getIngreso();
                    }
                }
                for (PagoMesItem pagoMesItem: empleadoTable.getPagoMesItems()){
                    if (pagoMesItem.getIngreso() != null) {
                        ingresos += pagoMesItem.getIngreso();
                    }
                }
                for (PagoMesItem pagoMesItem: empleadoTable.getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_ADELANTO_QUINCENAL)) {
                        anticipo += pagoMesItem.getDeduccion();
                    }
                }
                for (PagoMesItem pagoMesItem: empleadoTable.getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_IESS)) {
                        iess += pagoMesItem.getDeduccion();
                    }
                }
                for (PagoMesItem pagoMesItem: empleadoTable.getPagoMesItems()){
                    if (pagoMesItem.getClave().equalsIgnoreCase(Const.IP_DEUDA)) {
                        descuentos += pagoMesItem.getDeduccion();
                    }
                }
                
                neto += empleadoTable.getSueldo();
                
            } 
        }
        
        ReporteRolGeneralMensual datasource = new ReporteRolGeneralMensual();
        datasource.addAll(lista);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_GENERAL_MENSUAL);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("fecha", Fechas.getFechaConMes(new DateTime()));
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            parametros.put("dias", round(dias).toString());
            parametros.put("salario", round(salario).toString());
            parametros.put("sobretiempo", round(sobretiempo).toString());
            parametros.put("recargo", round(recargo).toString());
            parametros.put("transporte", round(transporte).toString());
            parametros.put("subtotal", round(subtotal).toString());
            parametros.put("tercero", round(tercero).toString());
            parametros.put("cuarto", round(cuarto).toString());
            parametros.put("ingresos", round(ingresos).toString());
            parametros.put("anticipo", round(anticipo).toString());
            parametros.put("iess", round(iess).toString());
            parametros.put("descuentos", round(descuentos).toString());
            parametros.put("neto", round(neto).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "reporte_general_"+(new DateTime()).getMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo general de pagos mensuales";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
        
    }
    
    @FXML
    public void pagarAdelanto(ActionEvent event) {
        if (hayParaPagar()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Pagar Rol Individual");
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
            children(new Text("¿Seguro que desea pagar el Rol Individual "
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
            dialogStage.showAndWait();
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Roles Individuales");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("No hay empleados seleccionados para hacer el pago."), buttonOk).
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
    }
    
    @FXML
    public void generarBank(ActionEvent event) {
        
        textosDAT = new ArrayList<>();
        textosTXT = new ArrayList<>();
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) data) {
            if (empleadoTable.getAgregar() && empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    textosDAT.add(crearLineaDAT(empleadoTable.getUsuario(), empleadoTable.getRolIndividual()));
                    textosTXT.add(crearLineaTXT(empleadoTable.getUsuario(), empleadoTable.getSueldo()));
            }
        }
        if (textosDAT.size() > 0 && textosTXT.size() > 0) {
            dialogoGenerarDatTxt();
        } else {
            dialogoErrorBizBank();
        }
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoMensualController.DataBaseThread(1, file, enviarCorreo);
        executor.execute(worker);
        executor.shutdown();

        loadingModeImprimir();
        
    }
    
    public void hacerPago() {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoMensualController.DataBaseThread(0);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
        
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
    
    public void dialogoGenerarDatTxt() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Roles Individuales");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Seleccionar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione donde guardar los archivos .TXT y .DAT"),
                new Text("Solo los empleados marcados que ya tenga rol generado quedaran incluidos."), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                new GuardarText().saveFile(textosDAT, getFileNameDat(file));
                new GuardarText().saveFile(textosTXT, getFileNameTXT(file));
                dialogoCompletado();
            }
        });
        dialogStage.show();
        
    }
    
    public void dialogoGenerarRolCompletado() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Roles Individuales");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los roles de pago individual con exito, \n"
                + " ¿Desea guardar los documento de pagos individuales (.PDF)?."), 
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
        dialogStage.setTitle("Roles");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Completado."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
        
        
        HibernateSessionFactory.getSession().clear();
        HibernateSessionFactory.getSession().flush();
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoMensualController.DataBaseThread(88);
        executor.execute(worker);
        executor.shutdown();
    }
    
    public void setTableInfo() {
        HibernateSessionFactory.getSession().clear();
        HibernateSessionFactory.getSession().flush();
        checkBoxPagarTodos.setSelected(false);
        
        iess = (Constante) constanteDAO.findUniqueResultByNombre(Const.IESS);
        if (iess != null) 
            iessText = Const.IP_IESS + " (" + iess.getValor() + "%)";
        
        pagosQuincena = new ArrayList<>();
        pagosQuincena.addAll(new PagoQuincenaDAO()
                .findAllInDeterminateTime(inicio.getFecha()));
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO().findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), inicio));
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
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
            empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
            calcularPago(empleado, user);
            if (!checkBoxPagarTodos.isSelected()) {
                empleado.setPagar(false);
            }
           
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);
        
        filtro();
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
    public void imprimirGeneral(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Rol de pagos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
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
        children(new Text("¿Desea imprimir el rol general de pagos"),
                new Text("De los empleados seleccionados?"),
                hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            File file = seleccionarDirectorio();
            imprimir(file);
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void empleadoEditado(Usuario user) {
        for (EmpleadoTable empleadoTable: data) {
            if(Objects.equals(empleadoTable.getId(), user.getId())) {
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
                calcularPago(empleado, user);
                if (!checkBoxPagarTodos.isSelected()) {
                    empleado.setPagar(false);
                }
                data.set(data.indexOf(empleadoTable), empleado);
                return;
            }
        }
    }
    
    public Boolean hayParaPagar() {
        for (EmpleadoTable empleadoTable: data) {
            if (empleadoTable.getPagar()) {
                return true;
            }
        }
        return false;
    }
    
    public void calcularPago(EmpleadoTable empleadoTable, Usuario empleado) {
        RolClienteDAO pagoDAO = new RolClienteDAO();
        ArrayList<RolCliente> pagos = new ArrayList<>();
        ArrayList<PagoMesItem> pagoMesItems = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdConCliente(inicio.getFecha(), 
                empleadoTable.getId()));
        if (pagos.isEmpty())
            pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdSinCliente(inicio.getFecha(), 
                    empleadoTable.getId()));
        if (pagos.isEmpty()) 
            empleadoTable.setSinRoles(Boolean.TRUE);
        Double diasTextValor = 0d;
        Double normalesTextValor = 0d;
        Double suplementariasTextValor = 0d;
        Double sobreTiempoTextValor = 0d;
        Double sueldoTotalTextValor = 0d;
        Double extraTextValor = 0d;
        Double bonosTextValor = 0d;
        Double vacacionesTextValor = 0d;
        Double subTotalTextValor = 0d;
        Double decimosTotalTextValor = 0d;
        Double decimoTerceroTotalTextValor = 0d;
        Double decimoCuartoTotalTextValor = 0d;
        Double totalTextValor = 0d;
        Double montoSumplementariasTextValor = 0d;
        Double montoSobreTiempoTextValor = 0d;
        Double montoBonoTextValor = 0d;
        Double montoTransporteTextValor = 0d;
        Double montoJubilacionTextValor = 0d;
        Double montoAportePatronalTextValor = 0d;
        Double montoSegurosTextValor = 0d; 
        Double montoUniformasTextValor = 0d;
        Double ingresoValor = 0d;
        Double ieesValor = 0d;
        Double quincenaValor = 0d;
        Double deudasValor = 0d;
        Double deduccionesValor = 0d;
        Double aPercibirValor = 0d;
        
        empleadoTable.setPagos(pagos);
        
        for (RolCliente pago: pagos) {
            
            diasTextValor += pago.getDias();
            normalesTextValor += pago.getHorasNormales();
            suplementariasTextValor += pago.getHorasSuplementarias();
            sobreTiempoTextValor += pago.getHorasSobreTiempo();
            sueldoTotalTextValor += pago.getSalario();
            extraTextValor += (pago.getMontoHorasSuplementarias() 
                    + pago.getMontoHorasSobreTiempo());
            bonosTextValor += pago.getTotalBonos();
            vacacionesTextValor += pago.getVacaciones();
            subTotalTextValor += pago.getSubtotal();
            decimosTotalTextValor += (pago.getDecimoCuarto() 
                    + pago.getDecimoTercero());
            decimoTerceroTotalTextValor += pago.getDecimoTercero();
            decimoCuartoTotalTextValor += pago.getDecimoCuarto();
            totalTextValor += pago.getTotalIngreso();
            
            montoSumplementariasTextValor += pago.getMontoHorasSuplementarias();
            montoSobreTiempoTextValor += pago.getMontoHorasSobreTiempo();
            montoBonoTextValor += pago.getBono();
            montoTransporteTextValor += pago.getTransporte();
            montoJubilacionTextValor += pago.getJubilacionPatronal(); 
            montoAportePatronalTextValor += pago.getAportePatronal(); 
            montoSegurosTextValor += pago.getSeguros(); 
            montoUniformasTextValor += pago.getUniformes(); 
            
             if (!empleado.getDetallesEmpleado().getAcumulaDecimos()) {
                decimoTerceroTotalTextValor -= pago.getVacaciones()/12;
                decimosTotalTextValor -= pago.getVacaciones()/12;
                totalTextValor -= pago.getVacaciones()/12;
            }
        }
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Sueldo");
            rol.setIngreso(round(sueldoTotalTextValor));
            rol.setDias(diasTextValor);
            rol.setHoras(normalesTextValor);
            rol.setClave(Const.IP_SUELDO);
            pagoMesItems.add(rol);
        }
        subTotalTextValor -= vacacionesTextValor;
        decimoTerceroTotalTextValor = round(subTotalTextValor / 12);
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Horas Extras");
            rol.setIngreso(extraTextValor);
            rol.setHoras(suplementariasTextValor + sobreTiempoTextValor);
            rol.setClave(Const.IP_HORAS_EXTRAS);
            pagoMesItems.add(rol);
        }
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Bonos");
            rol.setIngreso(bonosTextValor);
            rol.setClave(Const.IP_BONOS);
            pagoMesItems.add(rol);
        }
        if (empleado.getDetallesEmpleado().getAcumulaDecimos()) {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor;
        } else {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor 
                    + decimosTotalTextValor;
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
        ieesValor = ((sueldoTotalTextValor + extraTextValor + bonosTextValor)/100d) * getIess(); 
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion(iessText);
            rol.setDeduccion(round(ieesValor));
            rol.setClave(Const.IP_IESS);
            pagoMesItems.add(rol);
        }
        for (PagoQuincena pagoQuincena: pagosQuincena) {
            if (pagoQuincena.getUsuario().getId().equals(empleado.getId())) {
                quincenaValor = pagoQuincena.getMonto();
                {
                    PagoMesItem rol = new PagoMesItem();
                    rol.setDescripcion("Adelanto Quincenal");
                    rol.setDeduccion(quincenaValor);
                    rol.setClave(Const.IP_ADELANTO_QUINCENAL);
                    pagoMesItems.add(rol);
                }
                break;
            }
        }
        deudasValor = getDeudas(empleadoTable, pagoMesItems);
        deduccionesValor = ieesValor + quincenaValor + deudasValor;
        aPercibirValor = ingresoValor - deduccionesValor;
        
        RolIndividual pagoRol;
        pagoRol = new RolIndividualDAO().findByFechaAndEmpleadoIdAndDetalles(inicio.getFecha(), 
                empleado.getId(), Const.ROL_PAGO_INDIVIDUAL);
        
        if (pagoRol == null) {
            pagoRol = new RolIndividual();
            pagoRol.setDetalles(Const.ROL_PAGO_INDIVIDUAL);
            pagoRol.setFecha(new Timestamp(new Date().getTime()));
            pagoRol.setInicio(inicio.getFecha());
            pagoRol.setFinalizo(fin.getFecha());
            pagoRol.setDias(diasTextValor);
            pagoRol.setHorasNormales(normalesTextValor);
            pagoRol.setHorasSuplementarias(suplementariasTextValor);  // RC
            pagoRol.setHorasSobreTiempo(sobreTiempoTextValor);        // ST
            pagoRol.setTotalHorasExtras(sobreTiempoTextValor 
                    + suplementariasTextValor);
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
            pagoRol.setReserva(decimoTerceroTotalTextValor);
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
            if (empleado.getDetallesEmpleado().getAcumulaDecimos()) 
                pagoRol.setDecimosPagado(Boolean.FALSE);
            else 
                pagoRol.setDecimosPagado(Boolean.TRUE);
            
            empleadoTable.setPagado("No");
            empleadoTable.setPagar(true);
        } else {
           empleadoTable.setPagado("Si");
           empleadoTable.setPagar(false);
           PagoMes pagoMes = new PagoMesDAO()
                   .findInDeterminateTimeByUsuarioId(inicio.getFecha(), empleado.getId());
           pagoMesItems = new ArrayList<>();
           pagoMesItems.addAll(new PagoMesItemDAO().findByPagoMesId(pagoMes.getId()));
           aPercibirValor = pagoMes.getMonto();
           empleadoTable.setPagoMes(pagoMes);
        }
        
        empleadoTable.setSueldo(round(aPercibirValor));
        empleadoTable.setPagoMesItems(pagoMesItems);
        empleadoTable.setRolIndividual(pagoRol);
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
    
    public void mostrarPagoMensualDetalles(Usuario empleado) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoMensualDetalles.fxml"));
            AnchorPane ventanaAuditar = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Pago para el empleado " + empleado.getNombre() + " " 
                    + empleado.getApellido());
            String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaAuditar);
            ventana.setScene(scene);
            PagoMensualDetallesController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(aplicacionControl);
            controller.setPagoMensualController(this);
            controller.setEmpleado(empleado, inicio, fin);
            ventana.show();

        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        } 
    }
    
    public void mostrarPagado(RolIndividual rolIndividual) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoMensualPagado.fxml"));
            AnchorPane ventanaAuditar = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Pago para el empleado " + rolIndividual.getUsuario().getNombre() + " " 
                    + rolIndividual.getUsuario().getApellido());
            String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaAuditar);
            ventana.setScene(scene);
            PagoMensualPagadoController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(aplicacionControl);
            controller.setPagoMensualController(this);
            controller.setPago(rolIndividual, new Fecha(rolIndividual.getInicio()), 
                    new Fecha(rolIndividual.getFinalizo()));
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
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        sueldoColumna.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        
        pagadoColumna.setCellValueFactory(new PropertyValueFactory<>("pagado"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    if (rowData.getPagado().equalsIgnoreCase("Si")) {
                        mostrarPagado(rowData.getRolIndividual());
                    } else {
                        mostrarPagoMensualDetalles(new UsuarioDAO()
                                .findById(rowData.getId()));
                    }
                }
            });
            return row ;
        });
        
        pagarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pagarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxpagar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                setGraphic(checkBoxpagar);
                if (checkBoxpagar != null) {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")
                            || empleadoTable.getSinRoles() 
                            || empleadoTable.getSueldo() < 0d) {
                        checkBoxpagar.setDisable(true);
                        empleadoTable.setPagar(false);
                    } else {
                        checkBoxpagar.setDisable(false);
                    }
                    checkBoxpagar.setSelected(empleadoTable.getPagar());
                }
                
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
                checkBoxpagar.setOnAction(event -> {
                     empleadoTable.setPagar(checkBoxpagar.isSelected());
                     //guardar();
                });
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
        buttonPagar.setTooltip(
            new Tooltip("Pagar \n(Selecionados en \"Pagar\")")
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
    
    public double getIess() {
        if (iess == null) {
            return 0.0;
        } else {
            return Double.valueOf(iess.getValor());
        }
    }
    
    public Double getDeudas(EmpleadoTable empleadoTable, ArrayList<PagoMesItem> pagoMesItems) {
        
        ArrayList<CuotaDeuda> deudasAPagar = new ArrayList<>();
        Double monto = 0d;
        deudasAPagar.addAll(cuotaDeudaDAO
                .findAllByFechaAndEmpleadoId(empleadoTable.getId(), fin.getFecha()));
        for (CuotaDeuda cuota: deudasAPagar) {
            monto += cuota.getMonto();
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Deuda - " + cuota.getDeuda().getTipo());
                rol.setDeduccion(cuota.getMonto());
                rol.setClave(Const.IP_DEUDA);
                pagoMesItems.add(rol);
            }
        }
        empleadoTable.setDeudas(deudasAPagar);
        return monto;
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
        public final Integer UPDATE = 99;
        public final Integer START = 88;
        
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
                                imprimir(file, Boolean.TRUE);
                            } else if (Objects.equals(opcion, UPDATE)) {
                                updateWindows();
                            } else if (Objects.equals(opcion, START)) {
                                updateWindowsStart();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    closeDialogMode(); 
                                }
                            });
                        }
                }
            }, 1000, 1000);
        }
        
        public void hacerPago() {
            empleadosParaImprimir = new ArrayList<>();
            usuariosParaImprimir = new ArrayList<>();

            for (Usuario user: usuarios) {
                EmpleadoTable empleadoTable = data.get(usuarios.indexOf(user));
                empleadosParaImprimir.add(empleadoTable);
                usuariosParaImprimir.add(user);
                if (empleadoTable.getPagar() && empleadoTable.getSueldo() > 0d) {
                    new RolIndividualDAO().save(empleadoTable.getRolIndividual());

                    PagoMes pagoMes = new PagoMes();
                    pagoMes.setFecha(new Timestamp(new Date().getTime()));
                    pagoMes.setInicioMes(inicio.getFecha());
                    pagoMes.setFinMes(fin.getFecha());
                    pagoMes.setMonto(round(empleadoTable.getSueldo()));
                    pagoMes.setUsuario(user);
                    pagoMes.setRolIndividual(empleadoTable.getRolIndividual());
                    pagoMesDAO.save(pagoMes);

                    for (PagoMesItem pago: empleadoTable.getPagoMesItems()) {
                        pago.setPagoMes(pagoMes);
                        new PagoMesItemDAO().save(pago);
                    }
                    System.out.println("Deudas:" +empleadoTable.getDeudas().size());
                    
                    if (!empleadoTable.getDeudas().isEmpty()) 
                        for (CuotaDeuda cuota: empleadoTable.getDeudas()) {
                            Deuda deuda = deudaDAO.findById(cuota.getDeuda().getId());
                            Double montoAPagar = cuota.getMonto();
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

                            CuotaDeuda cuotaDeuda = new CuotaDeudaDAO().findById(cuota.getId());
                            cuotaDeuda.setEditado(new Timestamp(new Date().getTime()));
                            cuotaDeuda.setPagoMes(pagoMes);
                        }

                    // Registro para auditar
                    String detalles = "genero el rol individual nro: " 
                            + empleadoTable.getRolIndividual().getId() 
                            + " del lapso " + getFechaConMes(inicio)+ " a " 
                            + getFechaConMes(fin) + " para el empleado " 
                            + empleadoTable.getNombre() + " " + empleadoTable.getApellido();
                    aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    
                }
            }
            HibernateSessionFactory.getSession().flush();
            setTableInfo();
            
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoGenerarRolCompletado();
                }
            });
        }
        
        public void imprimir(File file, Boolean enviarCorreo) {
            
            ArrayList<EmpleadoTable> listToUse = new ArrayList<>();
            
            for (Usuario user: usuariosParaImprimir) {
                EmpleadoTable empleadoTable = empleadosParaImprimir
                        .get(usuariosParaImprimir.indexOf(user));
                if (empleadoTable.getPagar()) {
                    listToUse.add(empleadoTable);
                }
            }
            
            for (EmpleadoTable empleadoTable: listToUse) {
                
                Usuario user = empleadoTable.getUsuario();

                ReporteRolDePagoIndividual datasource = new ReporteRolDePagoIndividual();
                datasource.addAll(empleadoTable.getPagoMesItems());
                
                List<ControlExtras> controlEmpleado = new ControlExtrasDAO()
                        .findAllByEmpleadoIdInDeterminateTime(user.getId(), 
                                inicio.minusDays(7).getDate(), fin.minusDays(7).getDate());
                
                String horasDetallado = "";
        
                for (RolCliente rolCliente: empleadoTable.getPagos()) {
                    horasDetallado += "Cliente "+rolCliente.getClienteNombre()
                            +":\n               Dias                     "+rolCliente.getDias()
                            +"\n               H. Normales        "+rolCliente.getHorasNormales()
                            +"\n               H. Recargo          "+rolCliente.getHorasSuplementarias()
                            +"\n               H. Sobretiempo   "+rolCliente.getHorasSobreTiempo()+"\n\n";
                }
                
                ReporteHorasTrabajadas horasSource = new ReporteHorasTrabajadas(inicio, fin);
                horasSource.addAll(controlEmpleado);

                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        loadingModeUpdate(listToUse.indexOf(empleadoTable)
                                +1,listToUse.size());
                    }
                });

                try {
                    InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_PAGO_INDIVIDUAL);
                    InputStream inputHoras = new FileInputStream(Const.REPORTE_HORAS_TRABAJADAS);

                    Map<String, Object> parametros = new HashMap();
                    parametros.put("empleado", user.getNombre() + " " + user.getApellido());
                    parametros.put("cedula", user.getCedula());
                    parametros.put("cargo", user.getDetallesEmpleado().getCargo().getNombre());
                    parametros.put("empresa", user.getDetallesEmpleado().getEmpresa().getNombre());
                    parametros.put("numero", empleadoTable.getRolIndividual().getId().toString()); 
                    parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
                    parametros.put("total", round(empleadoTable.getSueldo()).toString());
                    parametros.put("fecha_recibo", Fechas.getFechaConMes(empleadoTable.getRolIndividual().getFecha()));
                    parametros.put("horas_detallado", horasDetallado);
                    ////////////////////// Rol mensual
                    JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                    String filename = "rol_pago_" + empleadoTable.getRolIndividual().getId();
                    ///////////////////// Horas trabajadas
                    JasperDesign jasperDesignHoras = JRXmlLoader.load(inputHoras);
                    JasperReport jasperReportHoras = JasperCompileManager.compileReport(jasperDesignHoras);
                    JasperPrint jasperPrintHoras;
                    if (controlEmpleado.isEmpty())
                        jasperPrintHoras = JasperFillManager.fillReport(jasperReportHoras, parametros, new JREmptyDataSource());
                    else
                        jasperPrintHoras = JasperFillManager.fillReport(jasperReportHoras, parametros, horasSource);

                    String filenameHoras = "hora_trabajadas_" + empleadoTable.getRolIndividual().getId();
                    
                    if (file != null) {
                        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                        JasperExportManager.exportReportToPdfFile(jasperPrintHoras, file.getPath() + "\\" + filenameHoras +".pdf"); 
                    } 
                    if (enviarCorreo) {
                        File pdf = File.createTempFile(filename, ".pdf");
                        JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                        CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                                user.getEmail(), Const.ASUNTO_ROL_INDIVIDUAL, 
                                "Recibo de rol de pago del mes que empieza el " 
                                        + getFechaConMes(inicio) 
                                        + " y termina el " 
                                        + getFechaConMes(fin), 
                                pdf.getPath(), filename + ".pdf");
                        
                        File pdfHoras = File.createTempFile(filenameHoras, ".pdf");
                        JasperExportManager.exportReportToPdfStream(jasperPrintHoras, new FileOutputStream(pdfHoras));  
                        CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                                user.getEmail(), Const.ASUNTO_HORAS, 
                                "Recibo de horas trabajadas en el mes que comienza " 
                                        + getFechaConMes(inicio) 
                                        + " y termina el " 
                                        + getFechaConMes(fin), 
                                pdfHoras.getPath(), filenameHoras + ".pdf");
                    }

                } catch (JRException | IOException ex) {
                    Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            empleadosParaImprimir.clear();
            usuariosParaImprimir.clear();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoCompletado();
                }
            });
        }
        
        public void updateWindows() {
            setTableInfo();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                }
            });
        }
        
        public void updateWindowsStart() {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    loadingMode();
                }
            });
            setTableInfo();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                }
            });
        }
        
    }
    
    String crearLineaDAT(Usuario user, RolIndividual rolIndividual) {
        
        Double montoDouble = rolIndividual.getSubtotal() 
                + getVacaciones(user)
                - rolIndividual.getSalario();
        
        String monto = Numeros.roundToString(montoDouble);
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
    
    Double getVacaciones(Usuario user) { // TODO; revisar
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
    
    String crearLineaTXT(Usuario user, Double sueldo) {
        
        String monto = Numeros.roundToString(sueldo);
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
                +espacios+partEntera+partDecimal+"ROL MENSUAL    "
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
