/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolCliente;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UniformeDAO;
import hibernate.model.Actuariales;
import hibernate.model.Cliente;
import hibernate.model.Constante;
import hibernate.model.Empresa;
import hibernate.model.Pago;
import hibernate.model.Seguro;
import hibernate.model.Uniforme;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
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

/**
 *
 * @author Yornel
 */
public class RolClienteEmpleadoController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label totalDias;
           
    @FXML
    private Label totalHorasN;
    
    @FXML
    private Label totalHorasST;
    
    @FXML
    private Label totalHorasRC;
    
    @FXML
    private Label totalHorasExtras;
    
    @FXML
    private Label totalSalario;
    
    @FXML
    private Label totalSobreTiempo;
    
    @FXML
    private Label totalRecargo;
    
    @FXML
    private Label totalBono;
    
    @FXML
    private Label totalTransporte;
    
    @FXML
    private Label totalBonos;
    
    @FXML
    private Label totalVacaciones;
    
    @FXML
    private Label subTotal;
    
    @FXML
    private Label totalDecimo3;
    
    @FXML
    private Label totalDecimo4;
    
    @FXML
    private Label totalReserva;
    
    @FXML
    private Label totalJubilacion;
    
    @FXML
    private Label totalAporte;
    
    @FXML
    private Label totalSeguros;
    
    @FXML
    private Label totalUniformes;
    
    @FXML
    private Label totalIngresos;  
    
    @FXML
    private Label textEmpleado;
    
    @FXML
    private Label empleadoText;
    
    @FXML
    private Label cedulaText;
    
    @FXML
    private Label cargoText;
    
    @FXML
    private Label clienteText;
    
    @FXML
    private Label numeracionText;
    
    @FXML
    private Label lapsoText;
    
    @FXML
    private GridPane gridPaneTotal;
    
    private Cliente cliente;
    private Empresa empresa;
    
    private Pago pago;
    private Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
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
        
        ReporteRolCliente datasource = new ReporteRolCliente();
        datasource.add(pago);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_UNITARIO);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empresa", pago.getUsuario().getDetallesEmpleado().getEmpresa().getNombre());
            parametros.put("siglas", pago.getUsuario().getDetallesEmpleado().getEmpresa().getSiglas());
            parametros.put("correo", "Correo: " + pago.getUsuario().getDetallesEmpleado().getEmpresa().getEmail());
            parametros.put("detalles", 
                         "Ruc: " + pago.getUsuario().getDetallesEmpleado().getEmpresa().getNumeracion() 
                    + " - Direccion: " + pago.getUsuario().getDetallesEmpleado().getEmpresa().getDireccion() 
                    + " - Tel: " + pago.getUsuario().getDetallesEmpleado().getEmpresa().getTelefono1());
            parametros.put("empleado", pago.getEmpleado());
            parametros.put("cedula", pago.getCedula());
            parametros.put("cargo", pago.getUsuario().getDetallesEmpleado().getCargo().getNombre());
            parametros.put("fecha", Fechas.getFechaConMes(pago.getInicio()) + " al " + Fechas.getFechaConMes(pago.getFinalizo()));
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "rol_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo de rol de pago individual del empleado " 
                    + pago.getUsuario().getNombre() + " " 
                    + pago.getUsuario().getApellido();
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
        dialogStage.setTitle("Imprimir Rol de Pago");
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
        dialogStage.setTitle("Imprimir Rol de Pago");
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
    
    public void setRolInfo(Pago pago) {
        this.pago = pago;
        
        empleadoText.setText(pago.getUsuario().getNombre() + " " 
                + pago.getUsuario().getApellido());
        cedulaText.setText(pago.getUsuario().getCedula());
        cargoText.setText(pago.getUsuario().getDetallesEmpleado()
                .getCargo().getNombre());
        if (pago.getCliente() == null) {
            clienteText.setText("Personal Administrativo");
            numeracionText.setVisible(false);
        } else {
            clienteText.setText("Cliente: " + pago.getClienteNombre());
            numeracionText.setText("Ruc: " + pago.getCliente().getRuc().toString());
        }
        lapsoText.setText(Fechas.getFechaConMes(pago.getInicio()) + " a " 
                + Fechas.getFechaConMes(pago.getFinalizo()));
       
        totalDias.setText(pago.getDias().toString());
        totalHorasN.setText(pago.getHorasNormales().toString());
        totalHorasRC.setText(pago.getHorasSuplementarias().toString());
        totalHorasST.setText(pago.getHorasSobreTiempo().toString());
        totalHorasExtras.setText(String.valueOf(pago.getHorasSuplementarias() 
                + pago.getHorasSobreTiempo()));
        totalSalario.setText(String.format( "%.2f", pago.getSalario()));
        totalSobreTiempo.setText(String.format( "%.2f", pago.getMontoHorasSobreTiempo()));
        totalRecargo.setText(String.format( "%.2f", pago.getMontoHorasSuplementarias()));
        totalBono.setText(String.format( "%.2f", pago.getBono()));
        totalTransporte.setText(String.format( "%.2f", pago.getTransporte()));
        totalBonos.setText(String.format( "%.2f", pago.getTotalBonos()));
        totalVacaciones.setText(String.format( "%.2f", pago.getVacaciones()));
        subTotal.setText(String.format( "%.2f", pago.getSubtotal()));
        totalDecimo3.setText(String.format( "%.2f", pago.getDecimoTercero()));
        totalDecimo4.setText(String.format( "%.2f", pago.getDecimoCuarto()));
        totalReserva.setText(String.format( "%.2f", pago.getDecimoTercero()));
        totalJubilacion.setText(String.format( "%.2f", pago.getJubilacionPatronal()));
        totalAporte.setText(String.format( "%.2f", pago.getAportePatronal()));
        totalSeguros.setText(String.format( "%.2f", pago.getSeguros()));
        totalUniformes.setText(String.format( "%.2f", pago.getUniformes()));
        totalIngresos.setText("$" + String.format( "%.2f", pago.getTotalIngreso()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        // Nada por aqui
    }  
}
