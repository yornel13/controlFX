/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolVacaciones;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Numeros;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.PagoVacacionesDAO;
import hibernate.model.Constante;
import hibernate.model.Empresa;
import hibernate.model.PagoMes;
import hibernate.model.PagoMesItem;
import hibernate.model.PagoVacaciones;
import hibernate.model.RolIndividual;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class VacacionesDetallesController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    private EmpleadoTable empleadoTable;
    
    private String periodoLiquidacion;
    
    // Columnas
    @FXML
    private TableColumn mesesColumna;
    
    @FXML
    private TableColumn diasColumna;
    
    @FXML
    private TableColumn valorColumna;
    
    @FXML
    private TableColumn vacacionesColumna;
 
    @FXML
    private TableView rolesTableView;
    
    // Labels
    @FXML
    private Label empleadoLabel;
    
    @FXML
    private Label sueldoLabel;
    
    @FXML
    private Label devengadoLabel;
    
    @FXML
    private Label aniosLabel;
    
    @FXML
    private Label periodoLabel;
    
    @FXML
    private Label diasLabel;
    
    @FXML
    private Label valorLabel;
    
    @FXML
    private Label aporteLabel;
    
    @FXML
    private Label cobrarLabel;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonBorrar;
    
    public ObservableList<RolIndividual> data;
    
    ArrayList<RolIndividual> rolIndividuales;
    
    Date inicio;
    Date fin;
    Double devengado;
    Double valor;
    Double deduccion;
    Double aCobrar;
    Integer dias;
    Double sueldo;
    
    Stage dialogLoading;
    
    public PagoVacaciones pagoVacaciones;
    
    public PagoVacacionesController pagoVacacionesController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setPrograma(PagoVacacionesController pagoVacacionesController) {
        this.pagoVacacionesController = pagoVacacionesController;
    }
    
    @FXML
    public void onPicketAction(ActionEvent event) {
        inicio = Date.valueOf(datePicker.getValue());
        DateTime inicioJT = new DateTime(inicio.getTime());
        DateTime finJT = inicioJT.plusDays(empleadoTable.getObjectVacaciones().getDias());
        fin = new Date(finJT.getMillis());
        
        periodoLabel.setText("Goce del "
                +Fechas.getFechaCorta(inicioJT)
                +" al "+
                Fechas.getFechaCorta(finJT));
        
        if (rolIndividuales != null && !rolIndividuales.isEmpty())
            buttonPagar.setDisable(false);
    }
    
    @FXML
    public void pagarVacaciones(ActionEvent event) {
        dialogConfirm();
    }
    
    public void dialogConfirm() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pagar Vacaciones");
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
        children(new Text("¿Seguro que desea hacer el pago de vacaciones?"), hBox).
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
    
    public void generacionCompletada() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda creada satisfactoriamente"), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        ReporteRolVacaciones datasource = new ReporteRolVacaciones();
        datasource.addAll(empleadoTable.getPagosMensuales());
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_VACACIONES);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("numero", pagoVacaciones.getId().toString()); 
            parametros.put("fecha_recibo", Fechas.getFechaConMes(pagoVacaciones.getFecha()));
            parametros.put("empleado", empleadoTable.getApellido()+ " " + empleadoTable.getNombre());
            parametros.put("cedula", empleadoTable.getCedula());
            parametros.put("cargo", empleadoTable.getUsuario().getDetallesEmpleado().getCargo().getNombre());
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("devengado", Numeros.round(pagoVacaciones.getDevengado()).toString());
            parametros.put("dias", pagoVacaciones.getDias().toString());
            parametros.put("valor", Numeros.round(pagoVacaciones.getValor()).toString());
            parametros.put("aporte", Numeros.round(pagoVacaciones.getAporte()).toString());
            parametros.put("cobrar", Numeros.round(pagoVacaciones.getMonto()).toString());
            parametros.put("periodo", Fechas.getFechaConMes(pagoVacaciones.getGoceInicio())
                        +" al "+
                        Fechas.getFechaConMes(pagoVacaciones.getGoceFin()));
            parametros.put("regreso", Fechas.getFechaConMes(new DateTime(pagoVacaciones.getGoceFin()).plusDays(1)));
            parametros.put("sueldo", Numeros.round(pagoVacaciones.getSueldo()).toString());
            parametros.put("ingreso", Fechas.getFechaCorta(empleadoTable.getUsuario().getDetallesEmpleado().getFechaInicio()));
            DateTime newDate = new DateTime();
            Timestamp timestamp = empleadoTable.getUsuario().getDetallesEmpleado().getFechaInicio();
            DateTime contratoDate = new DateTime(timestamp.getTime());
            Integer anios = newDate.getYear() - contratoDate.getYear();
            parametros.put("anios", anios.toString());
            parametros.put("lapso", periodoLiquidacion);
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "vacaciones_"+pagoVacaciones.getId();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            if (enviarCorreo) {
                File pdf = File.createTempFile(filename, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                CorreoUtil.mandarCorreo(empleadoTable.getUsuario().getDetallesEmpleado().getEmpresa().getNombre(), 
                        empleadoTable.getUsuario().getEmail(), Const.ASUNTO_VACACIONES, 
                        "Reportes de Vacaciones", 
                        pdf.getPath(), filename + ".pdf");
            }
            
            // Registro para auditar
            String detalles = "genero el recibo de liquidacion de vacaciones del empleado "
                    + empleadoTable.getApellido()+ " " + empleadoTable.getNombre();
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
        
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
        children(new Text("Ya se hizo el pago de vacaciones del empleado, \n"
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
    
    @FXML
    public void borrarPago(ActionEvent event) {
        
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
                
                confirmacionBorrado();
     
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    public void confirmacionBorrado() {
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
            dialogStage.close();
            dialogWait();
            hacerBorrado();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    void hacerBorrado() {
        
        if (pagoVacaciones.getId() != null) {
            new PagoVacacionesDAO().delete(pagoVacaciones);
            HibernateSessionFactory.getSession().flush();
            String detalles = "elemino el pago de vacaciones numero " + pagoVacaciones.getId()
                    + ", del empleado " + pagoVacaciones.getUsuario().getNombre() 
                    + " " + pagoVacaciones.getUsuario().getApellido();
            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
            dialogLoading.close();
            stagePrincipal.close();
            
            {
                empleadoTable.setVacaciones(null);
                empleadoTable.setDiasVacaciones(null);
                empleadoTable.setPagado("No");
                empleadoTable.setPagoVacaciones(null);
                empleadoTable.setPeriodo(null);
                pagoVacacionesController.empleadoEditado(empleadoTable);
            }

            dialogoBorradoCompletado();
            
        } else {
            dialogLoading.close();
            dialogoBorradoError();  
        } 
         
    }
    
    public void hacerPago() {
        pagoVacaciones = new PagoVacaciones();
        pagoVacaciones.setSueldo(sueldo);
        pagoVacaciones.setDevengado(devengado);
        pagoVacaciones.setAporte(deduccion);
        pagoVacaciones.setDias(dias);
        pagoVacaciones.setValor(valor);
        pagoVacaciones.setMonto(aCobrar);
        pagoVacaciones.setGoceInicio(inicio);
        pagoVacaciones.setGoceFin(fin);
        pagoVacaciones.setUsuario(empleadoTable.getUsuario());
        pagoVacaciones.setFecha(Fechas.getToday());
        pagoVacaciones.setInicio(new Fecha("01","01",new Fecha(rolIndividuales.get(0).getInicio()).getAno()).getFecha());
        pagoVacaciones.setFinalizo(new Fecha("30","12",new Fecha(rolIndividuales.get(0).getInicio()).getAno()).getFecha());
        
        new PagoVacacionesDAO().save(pagoVacaciones);
        
        buttonPagar.setVisible(false);
        buttonBorrar.setVisible(true);
        buttonImprimir.setVisible(true);
        datePicker.setDisable(true);
        
        {
            empleadoTable.setVacaciones(pagoVacaciones.getMonto().toString());
            empleadoTable.setDiasVacaciones(pagoVacaciones.getDias().toString());
            empleadoTable.setPagado("Si");
            empleadoTable.setPagoVacaciones(pagoVacaciones);
            empleadoTable.setPeriodo(Fechas.getFechaCorta(pagoVacaciones.getGoceInicio())
                    +" al "+
                    Fechas.getFechaCorta(pagoVacaciones.getGoceFin()));
            pagoVacacionesController.empleadoEditado(empleadoTable);
        }
        
        dialogoImprimir(null);
        
    }
    
    public void dialogoBorradoError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de Vacaciones");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No se puede borrar este pago."), buttonOk).
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
    
    public void dialogoBorradoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de Vacaciones");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se borro el pago con exito."), buttonOk).
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
        dialogStage.setTitle("Pago de Vacaciones");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Impresion de pago completada!."), buttonOk).
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
        dialogStage.setTitle("Imprimir pago");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar documento al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Pago de Vacaciones hecho con exito \n"
                + "¿Que imprimir el documento de pago?"), 
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
            } 
        });
        enviarCorreo.setSelected(true);
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
    
    public void borradoCompleto() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda eliminada con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void setEmpleado(EmpleadoTable empleadoTable, String periodo) {
        this.periodoLiquidacion = periodo;
        this.empleadoTable = empleadoTable;
        this.empresa = empleadoTable.getUsuario().getDetallesEmpleado().getEmpresa();
        
        if (empleadoTable.getPagoVacaciones() == null) {
        
            devengado = 0d;
            valor = 0d;
            deduccion = 0d;
            aCobrar = 0d;
            dias = empleadoTable.getObjectVacaciones().getDias();
            sueldo = empleadoTable.getUsuario().getDetallesEmpleado().getSueldo();
            // falta anios
            for (PagoMes pagoMes: empleadoTable.getPagosMensuales()) {
                for (PagoMesItem pagoMesItem: pagoMes.getPagosItems()){
                    if (pagoMesItem.getIngreso() != null) {
                        devengado += pagoMesItem.getIngreso();
                    }
                }
            }
            for (RolIndividual rol: empleadoTable.getRolesVac()) {
                valor += rol.getVacaciones();
            }

            deduccion = (valor/100d) * getIess();
            aCobrar = valor - deduccion;

            sueldoLabel.setText("Sueldo: $"+sueldo.toString());
            diasLabel.setText("Dias de vacaciones:  "+dias.toString());
            devengadoLabel.setText("Devengada anual:  $"+Numeros.round(devengado).toString());
            valorLabel.setText("$"+Numeros.round(valor).toString());
            aporteLabel.setText("$"+Numeros.round(deduccion).toString());
            cobrarLabel.setText("$"+Numeros.round(aCobrar).toString());
        
        } else {
            pagoVacaciones = empleadoTable.getPagoVacaciones();
            
            sueldoLabel.setText("Sueldo: $"+pagoVacaciones.getSueldo().toString());
            diasLabel.setText("Dias de vacaciones:  "+pagoVacaciones.getDias().toString());
            devengadoLabel.setText("Devengada anual:  $"+Numeros.round(pagoVacaciones.getDevengado()).toString());
            valorLabel.setText("$"+Numeros.round(pagoVacaciones.getValor()).toString());
            aporteLabel.setText("$"+Numeros.round(pagoVacaciones.getAporte()).toString());
            cobrarLabel.setText("$"+Numeros.round(pagoVacaciones.getMonto()).toString());
            periodoLabel.setText("Goce del "+Fechas.getFechaCorta(pagoVacaciones.getGoceInicio())
                        +" al "+
                        Fechas.getFechaCorta(pagoVacaciones.getGoceFin()));
            
            buttonPagar.setVisible(false);
            buttonBorrar.setVisible(true);
            buttonImprimir.setVisible(true);
            datePicker.setDisable(true);
            
        }
        
        DateTime newDate = new DateTime();
        Timestamp timestamp = empleadoTable.getUsuario().getDetallesEmpleado().getFechaInicio();
        DateTime contratoDate = new DateTime(timestamp.getTime());
        Integer anios = newDate.getYear() - contratoDate.getYear();

        aniosLabel.setText("Años de servicio: "+anios.toString());
        empleadoLabel.setText(empleadoTable.getApellido()+" "+empleadoTable.getNombre());
        
        rolIndividuales = new ArrayList<>();
        rolIndividuales.addAll(empleadoTable.getRolesVac());
        data = FXCollections.observableArrayList();
        data.addAll(rolIndividuales);
        
        rolesTableView.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        mesesColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<RolIndividual, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<RolIndividual, String> data) {
                
                Fecha fec = new Fecha(data.getValue().getInicio());
                String fechaToTable = fec.getMonthNameCort()+" "+fec.getAno();
                
                return new ReadOnlyStringWrapper(fechaToTable);
            }
        });
        diasColumna.setCellValueFactory(new PropertyValueFactory<>("dias"));
        valorColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<RolIndividual, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<RolIndividual, String> data) {
                
                Double monto = 0d;
                for (PagoMes pagoMes: empleadoTable.getPagosMensuales()) {
                    if (data.getValue().getInicio().equals(pagoMes.getInicioMes())) {
                        for (PagoMesItem pagoMesItem: pagoMes.getPagosItems()){
                            if (pagoMesItem.getIngreso() != null) {
                                monto += pagoMesItem.getIngreso();
                            }
                        }
                    }
                }
                
                return new ReadOnlyStringWrapper(Numeros.round(monto).toString());
            }
        });
        vacacionesColumna.setCellValueFactory(new PropertyValueFactory<>("vacaciones"));
        
        rolesTableView.setEditable(Boolean.FALSE);
        
        buttonPagar.setTooltip(
            new Tooltip("Pagar Vacaciones")
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
                    + "-fx-background-color: rgba(0,0,0,0.1);");
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
                    + "-fx-background-color: rgba(0,0,0,0.1);");
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
                    + "-fx-background-color: rgba(0,0,0,0.1);");
        });
    } 
    
    public double getIess() {
        ConstanteDAO constanteDao = new ConstanteDAO();
        Constante constante;
        constante = (Constante) constanteDao.findUniqueResultByNombre(Const.IESS);
        if (constante == null) {
            return 0.0;
        } else {
            return Double.valueOf(constante.getValor());
        }
    }
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
    
    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
}
