/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolDePagoQuincenal;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Fechas.getFechaConMes;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
import aplicacion.control.util.MaterialDesignButton;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.PagoMesDAO;
import hibernate.dao.PagoQuincenaDAO;
import hibernate.model.PagoQuincena;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

/**
 *
 * @author Yornel
 */
public class PagoQuincenalPagadoController implements Initializable {

    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private PagoQuincenalController pagoQuincenalController;
    
     @FXML
    private Label empleadoText;
    
    @FXML
    private Label cedulaText;
    
    @FXML
    private Label cargoText;
    
    @FXML
    private Label lapsoText;
    
    @FXML
    private Label fechaText;
    
    @FXML
    private Label totalIngresos;
    
    public PagoQuincena pagoQuincena;
    public Usuario usuario;
    
    Stage dialogLoading;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonBorrar;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setPagoQuincenalController(PagoQuincenalController pagoQuincenalController) {
        this.pagoQuincenalController = pagoQuincenalController;
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
      
        ReporteRolDePagoQuincenal datasource = new ReporteRolDePagoQuincenal();
        datasource.add(pagoQuincena);

            try {
                InputStream inputStream = new FileInputStream(Const.REPORTE_PAGO_ADELANTO_QUINCENAL);

                Map<String, String> parametros = new HashMap();
                parametros.put("empleado", usuario.getNombre() + " " + usuario.getApellido());
                parametros.put("cedula", usuario.getCedula());
                parametros.put("cargo", usuario.getDetallesEmpleado().getCargo().getNombre());
                parametros.put("empresa", usuario.getDetallesEmpleado().getEmpresa().getNombre());
                parametros.put("numero", pagoQuincena.getId().toString()); 
                parametros.put("lapso", getFechaConMes(pagoQuincena.getInicioMes()) 
                        + " al " + getFechaConMes(pagoQuincena.getFinMes()));
                parametros.put("total", round(pagoQuincena.getMonto()).toString());

                JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                String filename = "pago_quincenal_" + pagoQuincena.getId();

                if (file != null) {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                } 
                if (enviarCorreo) {
                    File pdf = File.createTempFile(filename, ".pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                    CorreoUtil.mandarCorreo(usuario.getDetallesEmpleado().getEmpresa().getNombre(), 
                            usuario.getEmail(), Const.ASUNTO_ADELANTO_QUINCENAL, 
                            "Recibo de adelanto quincenal del mes que empieza el " 
                                    + getFechaConMes(pagoQuincena.getInicioMes()) 
                                    + " y termina el " 
                                    + getFechaConMes(pagoQuincena.getFinMes()), 
                            pdf.getPath(), filename + ".pdf");
                }
                dialogoCompletado();

            } catch (JRException | IOException ex) {
                Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
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
        children(new Text("Ya se genero el pago quincenal de este empleado, \n"
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
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago Quincenal");
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
    
    @FXML
    public void borrarPago(ActionEvent event) {
        
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
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
                children(new Text("¿Seguro que desea Borrar este Adelanto quincenal?"),
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
                
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    void hacerBorrado() {
        
        if (new PagoMesDAO().findInDeterminateTimeByUsuarioId(pagoQuincena.getFinMes(), 
                        pagoQuincena.getUsuario().getId()) == null) {
            new PagoQuincenaDAO().delete(pagoQuincena);
            HibernateSessionFactory.getSession().flush();
            String detalles = "elemino el pago quincenal numero " + pagoQuincena.getId()
                    + ", del empleado " + pagoQuincena.getUsuario().getNombre() 
                    + " " + pagoQuincena.getUsuario().getApellido();
            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
            dialogLoading.close();
            stagePrincipal.close();
            pagoQuincenalController.setTableInfo();
            dialogoBorradoCompletado();
            
        } else {
            dialogLoading.close();
            dialogoBorradoError();  
        } 
         
    }
    
    public void dialogoBorradoError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago Quincenal");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No se puede borrar este adelanto,"), 
                new Text("borre primero el pago mensual."), buttonOk).
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
        dialogStage.setTitle("Pago Quincenal");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se borro este pago con exito."), buttonOk).
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
    
    public void setPago(PagoQuincena pagoQuincena) throws ParseException {
        
        this.pagoQuincena = pagoQuincena;
        this.usuario = pagoQuincena.getUsuario();
        
        empleadoText.setText(pagoQuincena.getUsuario().getNombre() + " " 
                + pagoQuincena.getUsuario().getApellido());
        cedulaText.setText(pagoQuincena.getUsuario().getCedula());
        cargoText.setText(pagoQuincena.getUsuario().getDetallesEmpleado()
                .getCargo().getNombre());
        lapsoText.setText(Fechas.getFechaConMes(pagoQuincena.getInicioMes()) + " a " 
                + Fechas.getFechaConMes(pagoQuincena.getFinMes()));
        fechaText.setText(Fechas.getFechaConMes(pagoQuincena.getFecha()));
        totalIngresos.setText(round(pagoQuincena.getMonto()).toString());
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        
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
}
