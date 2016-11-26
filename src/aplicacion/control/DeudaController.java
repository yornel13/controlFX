/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDeudaEmpleadoIndividual;
import aplicacion.control.tableModel.AbonoDeudaTable;
import aplicacion.control.tableModel.DeudaTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Numeros.round;
import hibernate.HibernateSessionFactory;
import hibernate.dao.AbonoDeudaDAO;
import hibernate.model.AbonoDeuda;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
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
import javafx.event.EventHandler;
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
import static aplicacion.control.util.Numeros.round;

/**
 *
 * @author Yornel
 */
public class DeudaController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    private Usuario empleado;
    
    // Columnas
    @FXML
    private TableColumn fechaColumna;
    
    @FXML
    private TableColumn numeroColumna;
    
    @FXML
    private TableColumn montoColumna;
    
    @FXML
    private TableColumn restanteColumna;
    
    @FXML
    private Label tipoText;
    
    @FXML
    private Label detallesText;
    
    @FXML
    private Label montoText;
    
    @FXML
    private Label fechaText;
    
    @FXML
    private Label estadoText;
    
    @FXML
    private Label cuotasText;
  
    @FXML
    private Label debeText;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonCambiar;
    
    @FXML
    private TableView deudasTableView;
    
    private ObservableList<AbonoDeudaTable> data;
    
    Deuda deuda;
    
    ArrayList<AbonoDeuda> abonoDeudas;
    
    Stage dialogLoading;
    
    private DeudasController deudasController;
    private DeudaTable deudaTable;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setProgramaDeudas(DeudasController deudasController) {
        this.deudasController = deudasController;
    }
   
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        ReporteDeudaEmpleadoIndividual datasource = new ReporteDeudaEmpleadoIndividual();
        datasource.addAll(abonoDeudas);
        
        try {
            InputStream inputStream;
            if (abonoDeudas == null || abonoDeudas.isEmpty()) {
                inputStream = new FileInputStream(Const.REPORTE_DEUDA_EMPLEADO_INDIVIDUAL_SIN_ABONO);
            } else {
                inputStream = new FileInputStream(Const.REPORTE_DEUDA_EMPLEADO_INDIVIDUAL);
            }
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empleado", empleado.getApellido()+ " " + empleado.getNombre());
            parametros.put("cedula", empleado.getCedula());
            parametros.put("cargo", empleado.getDetallesEmpleado().getCargo().getNombre());
            parametros.put("empresa", empleado.getDetallesEmpleado().getEmpresa().getNombre());
            parametros.put("tipo", deuda.getTipo());
            parametros.put("detalles", deuda.getDetalles());
            parametros.put("monto", deuda.getMonto().toString());
            parametros.put("cuotas", deuda.getCuotas().toString());
            parametros.put("fecha", Fechas.getFechaConMes(deuda.getCreacion()));
            
            if (deuda.getPagada()) {
                parametros.put("estado", "Pagada");
            } else {
                parametros.put("estado", "pendiente");
            }
            parametros.put("total", round(deuda.getRestante()).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (abonoDeudas == null || abonoDeudas.isEmpty()) {
                System.out.println("abono deudas empty" + abonoDeudas.isEmpty());
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            } else {
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            }
            
            String filename = "deuda_" + deuda.getId() + "_" + empleado.getApellido();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            if (enviarCorreo) {
                File pdf = File.createTempFile(filename, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                CorreoUtil.mandarCorreo(empleado.getDetallesEmpleado().getEmpresa().getNombre(), 
                        empleado.getEmail(), Const.ASUNTO_DEUDAS, 
                        "Reporte de deuda", 
                        pdf.getPath(), filename + ".pdf");
            }
            
            // Registro para auditar
            String detalles = "genero el recibo de deudas del empleado "
                    + empleado.getNombre() + " " + empleado.getApellido();
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
        dialogStage.setTitle("Imprimir deudas");
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
        dialogStage.setTitle("Imprimir Deudas");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar documento al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("¿Que desea hacer?"), 
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
    
    @FXML
    public void cambiarCuotas(ActionEvent event) {
        cambiarCuotasDeuda(deuda, deudaTable);
    }
    
    public void cambiarCuotasDeuda(Deuda deuda, DeudaTable deudaTable) {
        if (deuda.getPagada()) {
            
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Modificar Cuotas");
            String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("Modificar");
            TextField fieldCuotas = new TextField();
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Ingrese la cantidad de cuotas:"), fieldCuotas, buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            fieldCuotas.setMaxWidth(50);
            fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
            fieldCuotas.setText(deuda.getCuotas().toString());
            buttonOk.setOnAction((ActionEvent e) -> {
                
                if (fieldCuotas.getText() != null) {
                    deuda.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                    deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                    HibernateSessionFactory.getSession().flush();
                    deudaTable.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                    deudasController.setEmpleado(empleado);
                    dialogStage.close();
                    deudasController.deudasEmpleadosController.empleadoEditado(empleado);
                    cuotasText.setText("Cuotas restantes: " + deuda.getCuotas());
                        
                    // Registro para auditar
                    String detalles = "edito las cuotas de la deuda '" + deuda.getDetalles() + "' del empleado " 
                            + deuda.getUsuario().getApellido()+ " " 
                            + deuda.getUsuario().getNombre();
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    
                    deudasController.edicionCompletada();
                }
            });
            dialogStage.show();
        }
    }
    
    public void setDeuda(Deuda deuda, DeudaTable deudaTable) {
        
        this.deuda = deuda;
        this.deudaTable = deudaTable;
        this.empleado = deuda.getUsuario();
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        
        tipoText.setText("Tipo de deuda: " + deuda.getTipo());
        detallesText.setText("Detalles: " + deuda.getDetalles());
        montoText.setText("Monto: $" + deuda.getMonto());
        fechaText.setText("Creada: $" + Fechas.getFechaConMes(deuda.getCreacion()));
        cuotasText.setText("Cuotas restantes: " + deuda.getCuotas());
        if (deuda.getPagada()) {
            estadoText.setText("Deuda Pagada");
        } else {
            estadoText.setText("Deuda Pendiente");
        }
        debeText.setText(deuda.getRestante().toString());
        
        AbonoDeudaDAO abonoDeudaDAO = new AbonoDeudaDAO();
        abonoDeudas = new ArrayList<>();
        abonoDeudas.addAll(abonoDeudaDAO.findAllByDeudaId(deuda.getId()));
        data = FXCollections.observableArrayList(); 
        for (AbonoDeuda abonoDeuda: abonoDeudas) {
            AbonoDeudaTable abonoDeudaTable = new AbonoDeudaTable();
            abonoDeudaTable.setId(abonoDeuda.getId());
            abonoDeudaTable.setMonto(abonoDeuda.getMonto());
            abonoDeudaTable.setFecha(Fechas.getFechaConMes(abonoDeuda.getFecha()));
            abonoDeudaTable.setRestante(abonoDeuda.getRestante());
            abonoDeudaTable.setPagoMes(abonoDeuda.getPagoMes());
            abonoDeudaTable.setNumeroPago("N-" + abonoDeuda.getPagoMes().getId());
            abonoDeudaTable.setDeuda(deuda);
            data.add(abonoDeudaTable);
        }
        deudasTableView.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        numeroColumna.setCellValueFactory(new PropertyValueFactory<>("numeroPago"));
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        restanteColumna.setCellValueFactory(new PropertyValueFactory<>("restante"));
  
        deudasTableView.setEditable(Boolean.FALSE);
        
        deudasTableView.setRowFactory( (Object tv) -> {
            TableRow<AbonoDeudaTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    AbonoDeudaTable rowData = row.getItem();
                    // TODO nada aqui
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
        buttonCambiar.setTooltip(
            new Tooltip("Cambias cuotas restantes")
        );
        buttonCambiar.setOnMouseEntered((MouseEvent t) -> {
            buttonCambiar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cambiar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonCambiar.setOnMouseExited((MouseEvent t) -> {
            buttonCambiar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cambiar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
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
