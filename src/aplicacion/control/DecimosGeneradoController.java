/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDecimosGenerado;
import aplicacion.control.reports.ReporteDecimosTotalGenerado;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
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
import aplicacion.control.util.Roboto;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.RolCliente;
import hibernate.model.RolIndividual;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import net.sf.jasperreports.engine.JREmptyDataSource;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;

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
    private Label totalD3AcumuladoText;
     
    @FXML
    private Label totalD3PagadoText;
    
    @FXML
    private Label totalD4AcumuladoText;
     
    @FXML
    private Label totalD4PagadoText;
     
    @FXML
    private Label totalAcumuladoText;
     
    @FXML
    private Label totalPagadoText;
     
    @FXML
    private Label estadoD3Text;
    
    @FXML
    private Label estadoD4Text;
    
    
    public Fecha inicio;
    public Fecha fin;
    
    public Usuario usuario;
    
    Stage dialogLoading;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonImprimirTotal;
    
    List<RolIndividual> rolIndividuales;
    
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
        
        Double dias = 0d;
        Double horas = 0d;
        Double decimo3 = 0d;
        Double decimo4 = 0d;
        Double suma;
        
        RolClienteDAO pagoDAO = new RolClienteDAO();
        ArrayList<RolCliente> pagos = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdConCliente(inicio.getFecha(), usuario.getId()));
        if (pagos.isEmpty())
            pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdSinCliente(inicio.getFecha(), usuario.getId()));
        
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

                Map<String, Object> parametros = new HashMap();
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
                parametros.put("estado", "Decimo 3ro " + estadoD3Text.getText() 
                         + " , Decimo 4to " + estadoD4Text.getText());
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
                            usuario.getEmail(), Const.ASUNTO_DECIMOS_GENERADO, 
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
    
    public void imprimirTotalDecimoTercero(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        Double retenidoD3 = 0d;
        Double pagadoD3 = 0d;
        Double decimo3;
        
        for (RolIndividual rolIndividual: rolIndividuales) {
            if (rolIndividual.getDecimosPagado()) {
                pagadoD3 += rolIndividual.getDecimoTercero();
            } else {
                if (rolIndividual.getPagoDecimoTercero() != null ) {
                    pagadoD3 += rolIndividual.getDecimoTercero();
                } else {
                    retenidoD3 += rolIndividual.getDecimoTercero();
                }
            }
        }
        
        decimo3 = pagadoD3 + retenidoD3;
      
        ReporteDecimosTotalGenerado datasource = new ReporteDecimosTotalGenerado();
        datasource.addAll(rolIndividuales);

            try {
                InputStream inputStream = new FileInputStream(Const.REPORTE_DECIMO_TERCERO_TOTAL_EMPLEADO);

                Map<String, Object> parametros = new HashMap();
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
                String lapso = "No tiene";
                if (!rolIndividuales.isEmpty()) {
                    lapso = getFechaConMes(rolIndividuales.get(0).getInicio())  
                        + " al " + getFechaConMes(rolIndividuales.get(rolIndividuales.size() - 1)
                                .getFinalizo());
                }
                parametros.put("lapso", lapso);
                
                parametros.put("decimo3", round(decimo3).toString());
                parametros.put("retenidoD3", round(retenidoD3).toString());
                parametros.put("pagadoD3", round(pagadoD3).toString());

                JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                
                JasperPrint jasperPrint;
                if (rolIndividuales.isEmpty())
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
                else
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                String filename = "total_decimos_tercero_generados_" + System.currentTimeMillis();

                if (file != null) {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                } 
                if (enviarCorreo) {
                    File pdf = File.createTempFile(filename, ".pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                    CorreoUtil.mandarCorreo(usuario.getDetallesEmpleado().getEmpresa().getNombre(), 
                            usuario.getEmail(), Const.ASUNTO_DECIMO_TERCERO_GENERADO, 
                            "Recibo de decimos tercero generados desde " 
                                    + lapso, 
                            pdf.getPath(), filename + ".pdf");
                }
                dialogoCompletado();

            } catch (JRException | IOException ex) {
                Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dialogLoading.close();
            }
    }
    
    public void imprimirTotalDecimoCuarto(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        Double retenidoD4 = 0d;
        Double pagadoD4 = 0d;
        Double decimo4;
        
        for (RolIndividual rolIndividual: rolIndividuales) {
            if (rolIndividual.getDecimosPagado()) {
                pagadoD4 += rolIndividual.getDecimoCuarto();
            } else {
                if (rolIndividual.getPagoDecimoCuarto()!= null ) {
                    pagadoD4 += rolIndividual.getDecimoCuarto();
                } else {
                    retenidoD4 += rolIndividual.getDecimoCuarto();
                }
            }
        }
        
        decimo4 = pagadoD4 + retenidoD4;
      
        ReporteDecimosTotalGenerado datasource = new ReporteDecimosTotalGenerado();
        datasource.addAll(rolIndividuales);

            try {
                InputStream inputStream = new FileInputStream(Const.REPORTE_DECIMO_CUARTO_TOTAL_EMPLEADO);

                Map<String, Object> parametros = new HashMap();
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
                String lapso = "No tiene";
                if (!rolIndividuales.isEmpty()) {
                    lapso = getFechaConMes(rolIndividuales.get(0).getInicio())  
                        + " al " + getFechaConMes(rolIndividuales.get(rolIndividuales.size() - 1)
                                .getFinalizo());
                }
                parametros.put("lapso", lapso);
                
                parametros.put("decimo4", round(decimo4).toString());
                parametros.put("retenidoD4", round(retenidoD4).toString());
                parametros.put("pagadoD4", round(pagadoD4).toString());

                JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                
                JasperPrint jasperPrint;
                if (rolIndividuales.isEmpty())
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
                else
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                String filename = "total_decimos_cuarto_generados_" + System.currentTimeMillis();

                if (file != null) {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                } 
                if (enviarCorreo) {
                    File pdf = File.createTempFile(filename, ".pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                    CorreoUtil.mandarCorreo(usuario.getDetallesEmpleado().getEmpresa().getNombre(), 
                            usuario.getEmail(), Const.ASUNTO_DECIMO_CUARTO_GENERADO, 
                            "Recibo de decimos cuarto generados desde " 
                                    + lapso, 
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
    public void dialogoImprimirTotal(ActionEvent event) {
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
            rutaDecimoTercero();
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
            rutaDecimoCuarto();
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
    
    @FXML
    public void reimprimirRecibo(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta para guardar, el recibo de "
                + "\ndecimos generados de el mes seleccionado."), 
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
    
    public void rutaDecimoTercero() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta para guardar, el recibo total de "
                + "\ndecimos tercero generados del empleado seleccionado."), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimirTotalDecimoTercero(file, enviarCorreo.isSelected());
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                imprimirTotalDecimoTercero(null, enviarCorreo.isSelected());
            } else {
                dialogoCompletado();
            }
        });
        enviarCorreo.setSelected(true);
        dialogStage.showAndWait();
    }
    
    public void rutaDecimoCuarto() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta para guardar, el recibo total de "
                + "\ndecimos cuarto generados del empleado seleccionado."), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimirTotalDecimoCuarto(file, enviarCorreo.isSelected());
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                imprimirTotalDecimoCuarto(null, enviarCorreo.isSelected());
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
    
    public void setEmpleado(Usuario empleado, Fecha inicio, Fecha fin, 
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
        rolIndividuales = rolDao.findAllByEmpleadoId(usuario.getId());
        
        Double acumulado;
        Double pagado;
        Double acumuladoD3 = 0d;
        Double pagadoD3 = 0d;
        Double acumuladoD4 = 0d;
        Double pagadoD4 = 0d;
        for (RolIndividual rolIndividual: rolIndividuales) {

            if (rolIndividual.getDecimosPagado()) {
                pagadoD3 += rolIndividual.getDecimoTercero();
                pagadoD4 += rolIndividual.getDecimoCuarto();
            } else {
                if (rolIndividual.getPagoDecimoTercero() != null ) {
                    pagadoD3 += rolIndividual.getDecimoTercero();
                } else {
                    acumuladoD3 += rolIndividual.getDecimoTercero();
                }
                if (rolIndividual.getPagoDecimoCuarto() != null ) {
                    pagadoD4 += rolIndividual.getDecimoCuarto();
                } else {
                    acumuladoD4 += rolIndividual.getDecimoCuarto();
                }
            }
        }
        acumulado = acumuladoD3 + acumuladoD4;
        pagado = pagadoD3 + pagadoD4;
        
        totalD3AcumuladoText.setText("$"+round(acumuladoD3).toString());
        totalD4AcumuladoText.setText("$"+round(acumuladoD4).toString());
        totalD3PagadoText.setText("$"+round(pagadoD3).toString());
        totalD4PagadoText.setText("$"+round(pagadoD4).toString());
        
        totalAcumuladoText.setText("$"+round(acumulado).toString());
        totalPagadoText.setText("$"+round(pagado).toString());
        estadoD3Text.setText(empleadoTable.getDetallesD3());
        estadoD4Text.setText(empleadoTable.getDetallesD4());
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
        buttonImprimirTotal.setTooltip(
            new Tooltip("Imprimir informe completo del empleado")
        );
        buttonImprimirTotal.setOnMouseEntered((MouseEvent t) -> {
            buttonImprimirTotal.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/imprimir.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonImprimirTotal.setOnMouseExited((MouseEvent t) -> {
            buttonImprimirTotal.setStyle("-fx-background-image: "
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
