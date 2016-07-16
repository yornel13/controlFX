/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDecimosGenerado;
import aplicacion.control.reports.ReporteRolDePagoQuincenal;
import aplicacion.control.tableModel.EmpleadoTable;
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
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.RolCliente;
import hibernate.model.RolIndividual;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JREmptyDataSource;

/**
 *
 * @author Yornel
 */
public class DecimosGeneradoController implements Initializable {

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
    private Label decimo3Text;
     
    @FXML
    private Label decimo4Text;
     
    @FXML
    private Label totalDecimosText;
     
    @FXML
    private Label totalAcumuladoText;
     
    @FXML
    private Label totalPagadoText;
     
    @FXML
    private Label estadoText;
    
    
    public Timestamp inicio;
    public Timestamp fin;
    
    public Usuario usuario;
    
    Stage dialogLoading;
    
    @FXML
    private Button buttonImprimir;
    
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
        
        Integer dias = 0;
        Double horas = 0d;
        Double decimo3 = 0d;
        Double decimo4 = 0d;
        Double suma;
        
        RolClienteDAO pagoDAO = new RolClienteDAO();
        ArrayList<RolCliente> pagos = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdConCliente(fin, usuario.getId()));
        if (pagos.isEmpty())
            pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdSinCliente(fin, usuario.getId()));
        
        for (RolCliente rolCliente: pagos) {
            dias += rolCliente.getDias();
            horas += rolCliente.getHorasNormales();
            horas += rolCliente.getTotalHorasExtras();
            decimo3 += rolCliente.getDecimoTercero();
            decimo4 += rolCliente.getDecimoCuarto();
        }
        
        suma = decimo3 + decimo4;
      
        ReporteDecimosGenerado datasource = new ReporteDecimosGenerado();
        datasource.addAll(pagos);

            try {
                InputStream inputStream = new FileInputStream(Const.REPORTE_DECIMOS_MES_EMPLEADO);

                Map<String, String> parametros = new HashMap();
                parametros.put("empleado", usuario.getNombre() + " " + usuario.getApellido());
                parametros.put("cedula", usuario.getCedula());
                parametros.put("cargo", usuario.getDetallesEmpleado().getCargo().getNombre());
                parametros.put("empresa", usuario.getDetallesEmpleado().getEmpresa().getNombre());
                parametros.put("siglas", usuario.getDetallesEmpleado().getEmpresa().getSiglas());
                parametros.put("correo", "Correo: " + usuario.getDetallesEmpleado().getEmpresa().getEmail());
                parametros.put("detalles", 
                             "Ruc: " + usuario.getDetallesEmpleado().getEmpresa().getNumeracion() 
                        + " - Direccion: " + usuario.getDetallesEmpleado().getEmpresa().getDireccion() 
                        + " - Tel: " + usuario.getDetallesEmpleado().getEmpresa().getTelefono1());
                parametros.put("lapso", getFechaConMes(inicio)  + " al " + getFechaConMes(fin));
                 parametros.put("estado", estadoText.getText());
                parametros.put("dias", dias.toString());
                parametros.put("horas", horas.toString());
                parametros.put("decimo3", round(decimo3).toString());
                parametros.put("decimo4", round(decimo4).toString());
                parametros.put("suma", round(suma).toString());

                JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                
                JasperPrint jasperPrint;
                if (pagos.isEmpty())
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
                else
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                String filename = "decimos_generados_" + System.currentTimeMillis();

                if (file != null) {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                } 
                if (enviarCorreo) {
                    File pdf = File.createTempFile(filename, ".pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                    CorreoUtil.mandarCorreo(usuario.getDetallesEmpleado().getEmpresa().getNombre(), 
                            usuario.getEmail(), Const.ASUNTO_ADELANTO_QUINCENAL, 
                            "Recibo de decimos generados del mes que empieza el " 
                                    + getFechaConMes(inicio) 
                                    + " y termina el " 
                                    + getFechaConMes(fin), 
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
        dialogStage.setTitle("Decimos generados");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta para guardar, \n"
                + " el recibo de decimos generados de el mes seleccionado."), 
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
        dialogStage.setTitle("Decimos Generados");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Impresion de decimos completado."), buttonOk).
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
    
    public void setEmpleado(Usuario empleado, Timestamp inicio, Timestamp fin, 
            EmpleadoTable empleadoTable) {
        this.usuario = empleado;
        this.inicio = inicio;
        this.fin = fin;
        
        empleadoText.setText(usuario.getApellido()+ " " + usuario.getNombre());
        cedulaText.setText("Cedula: " + usuario.getCedula());
        cargoText.setText(usuario.getDetallesEmpleado()
                .getCargo().getNombre());
        lapsoText.setText("Lapso: " + Fechas.getFechaConMes(inicio) + " a " 
                + Fechas.getFechaConMes(fin));
        
        RolIndividualDAO rolDao = new RolIndividualDAO();
        List<RolIndividual> rolIndividuales = rolDao.findAllByEmpleadoId(usuario.getId());
        
        Double acumulado = 0d;
        Double pagado = 0d;
        for (RolIndividual rolIndividual: rolIndividuales) {
            Double decimos = rolIndividual.getDecimoTercero() 
                        + rolIndividual.getDecimoCuarto();
            if (rolIndividual.getDecimosPagado()) {
                pagado += decimos;
            } else {
                acumulado += decimos;
            }
        }
        totalAcumuladoText.setText("$"+round(acumulado).toString());
        totalPagadoText.setText("$"+round(pagado).toString());
        estadoText.setText(empleadoTable.getDetalles());
        decimo3Text.setText("$"+empleadoTable.getDecimo3().toString());
        decimo4Text.setText("$"+empleadoTable.getDecimo4().toString());
        totalDecimosText.setText("$"+round(empleadoTable.getDecimo3() 
                + empleadoTable.getDecimo4()).toString());
        
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
    }   
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
}
