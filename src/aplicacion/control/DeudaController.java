/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDeudaEmpleadoIndividual;
import aplicacion.control.tableModel.CuotaDeudaTable;
import aplicacion.control.tableModel.DeudaTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.HibernateSessionFactory;
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
import hibernate.dao.CuotaDeudaDAO;
import hibernate.model.CuotaDeuda;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import static aplicacion.control.util.Numeros.round;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;

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
    private TableColumn observacionColumna;
     
    @FXML
    private TableColumn<CuotaDeudaTable, CuotaDeudaTable> marcarColumna;
   
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
    private Label pagadoText;
    
    @FXML
    private Label labelSugerencia;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonCambiar;
    
    @FXML
    private TableView deudasTableView;
    
    private ObservableList<CuotaDeudaTable> data;
    
    Deuda deuda;
    
    ArrayList<CuotaDeuda> cuotaDeudas;
    
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
        //datasource.addAll(cuotaDeudas);
        
        try {
            InputStream inputStream;
            if (cuotaDeudas == null || cuotaDeudas.isEmpty()) {
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
            if (cuotaDeudas == null || cuotaDeudas.isEmpty()) {
                System.out.println("abono deudas empty" + cuotaDeudas.isEmpty());
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
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("Modificar");
            TextField fieldCuotas = new TextField();
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Ingrese la cantidad de cuotas que restaran ahora. \n"
                    + "tenga en cuenta que todas las cuotas anteriores que no esten pagas \n"
                    + "seran elimandas para hacer el reajuste."), fieldCuotas, buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            fieldCuotas.setMaxWidth(50);
            fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
            fieldCuotas.setText(deuda.getCuotas().toString());
            buttonOk.setOnAction((ActionEvent e) -> {
                
                if (fieldCuotas.getText() != null && Integer.parseInt(fieldCuotas.getText()) > 0) {
                    
                    int numeroC = Integer.valueOf(fieldCuotas.getText());
                
                    Double monto = deuda.getRestante() / numeroC;
                    monto = round(monto);
                
                    Fecha fecha = Fechas.getFechaActual();
                    fecha.setDia("30");
                    
                     for (int number = 0; numeroC > number; number++) {
                    
                        CuotaDeuda cuota = new CuotaDeuda();
                        cuota.setFecha(fecha.plusMonths(number).getFecha());
                        cuota.setMonto(monto);
                        cuota.setPagoMes(null);
                        cuota.setCreado(Fechas.getToday());
                        cuota.setEditado(Fechas.getToday());
                        cuota.setAplazado(Boolean.FALSE);
                        cuota.setDeuda(deuda);

                        new CuotaDeudaDAO().save(cuota);

                    }
                    
                    deuda.setCuotas(numeroC);
                    for (CuotaDeuda cuotaDeudaToDelete: cuotaDeudas) {
                        if (cuotaDeudaToDelete.getPagoMes() == null) {
                            new CuotaDeudaDAO().delete(cuotaDeudaToDelete);
                            HibernateSessionFactory.getSession().flush();
                        } else {
                            numeroC++;
                        }
                    }
                    
                    deuda.setCuotasTotal(numeroC);
                    deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                    HibernateSessionFactory.getSession().flush();
                    deudaTable.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                    deudasController.setEmpleado(empleado);
                    dialogStage.close();
                    if (deudasController.deudasEmpleadosController != null)
                        deudasController.deudasEmpleadosController.empleadoEditado(empleado);
                    else
                        deudasController.pagoMensualDetallesController.setInfoEditada(deudasController.pagoMensualDetallesController.inicio, 
                                deudasController.pagoMensualDetallesController.fin, 
                                deudasController.pagoMensualDetallesController.empleado.getId());
                        
                    // Registro para auditar
                    String detalles = "edito las cuotas de la deuda '" + deuda.getDetalles() + "' del empleado " 
                            +deuda.getUsuario().getApellido()+" " 
                            +deuda.getUsuario().getNombre();
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    
                    setDeuda(deuda, deudaTable);
                    
                    deudasController.edicionCompletada();
                }
            });
            dialogStage.show();
        }
    }
    
    public void cambiarFecha(Deuda deuda, CuotaDeudaTable cuotaDeudaTable) {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Modificar");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("Modificar");
        
        ChoiceBox selectorMes = new ChoiceBox();
        ChoiceBox selectorAno = new ChoiceBox();
        
        selectorMes.setItems(Fechas.arraySpinnerMes());
        selectorAno.setItems(Fechas.arraySpinnerAno());
        
        selectorMes.getSelectionModel().select(cuotaDeudaTable.getFecha().getMes());
        selectorAno.getSelectionModel().select(cuotaDeudaTable.getFecha().getAno());
        
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(0, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(selectorMes, selectorAno)
                .build();
        hBox.maxWidth(120);
        
        HBox hBoxText = HBoxBuilder.create()
                .spacing(20.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 0, 5))
                .alignment(Pos.CENTER)
                .children(new Text("Mes   "), new Text("Año"))
                .build();
        hBox.maxWidth(120);

        TextField fieldDetalles = new TextField();
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Selecione la nueva fecha para pago de la cuota"), 
                hBoxText, hBox, new Text("Observación"), fieldDetalles, buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        fieldDetalles.setMaxWidth(200);
        fieldDetalles.setText(cuotaDeudaTable.getCuotaDeuda().getDetalles());
        buttonOk.setOnAction((ActionEvent e) -> {

            CuotaDeuda cuotaDeuda = cuotaDeudaTable.getCuotaDeuda();
            cuotaDeuda.setDetalles(fieldDetalles.getText());
            cuotaDeuda.setFecha(new Fecha(selectorAno, selectorMes, "30").getFecha());
            deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
            HibernateSessionFactory.getSession().flush();

            // Registro para auditar
            String detalles = "modifico una cuota de la deuda '"+deuda.getDetalles()+ "' del empleado " 
                    + deuda.getUsuario().getApellido()+ " " 
                    + deuda.getUsuario().getNombre();
            aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogStage.close();
            setDeuda(deuda, deudaTable);
            
        });
        dialogStage.show();

    }
    
    public void setDeuda(Deuda deuda, DeudaTable deudaTable) {
        
        this.deuda = deuda;
        this.deudaTable = deudaTable;
        this.empleado = deuda.getUsuario();
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        
        tipoText.setText("Tipo de deuda: " + deuda.getTipo());
        detallesText.setText("Detalles: " + deuda.getDetalles());
        montoText.setText("Monto: $" + deuda.getMonto());
        fechaText.setText("Creada el " + Fechas.getFechaConMes(deuda.getCreacion()));
        if (deuda.getPagada()) {
            estadoText.setText("Deuda Pagada");
            labelSugerencia.setVisible(false);
            buttonCambiar.setVisible(false);
        } else {
            cuotasText.setText("Cuotas restantes: " + deuda.getCuotas());
            estadoText.setText("Deuda Pendiente");
        }
        debeText.setText("$"+deuda.getRestante().toString());
        
        Double pagado = 0d;
        CuotaDeudaDAO cuotaDeudaDAO = new CuotaDeudaDAO();
        cuotaDeudas = new ArrayList<>();
        cuotaDeudas.addAll(cuotaDeudaDAO.findAllByDeudaId(deuda.getId()));
        data = FXCollections.observableArrayList(); 
        for (CuotaDeuda cuotaDeuda: cuotaDeudas) {
            CuotaDeudaTable cuotaTable = new CuotaDeudaTable();
            cuotaTable.setId(cuotaDeuda.getId());
            cuotaTable.setMonto(cuotaDeuda.getMonto());
            cuotaTable.setFechaString(Fechas.getFechaConMes(cuotaDeuda.getFecha()));
            cuotaTable.setFecha(new Fecha(cuotaDeuda.getFecha()));
            cuotaTable.setPagoMes(cuotaDeuda.getPagoMes());
            cuotaTable.setDetalles(cuotaDeuda.getDetalles());
            cuotaTable.setCuotaDeuda(cuotaDeuda);
            if (cuotaDeuda.getPagoMes() != null) {
                cuotaTable.setNumeroPago("N-" + cuotaDeuda.getPagoMes().getId());
                pagado += cuotaDeuda.getMonto();
            } else {
                cuotaTable.setNumeroPago("-----");
            }
            cuotaTable.setDeuda(deuda);
            data.add(cuotaTable);
        }
        pagadoText.setText("$"+round(pagado).toString());
        deudasTableView.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fechaString"));
        numeroColumna.setCellValueFactory(new PropertyValueFactory<>("numeroPago"));
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        observacionColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<CuotaDeudaTable, CuotaDeudaTable>() {

            @Override
            protected void updateItem(CuotaDeudaTable abonoDeudaTable, boolean empty) {
                super.updateItem(abonoDeudaTable, empty);

                if (abonoDeudaTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                if (abonoDeudaTable.getPagoMes() != null) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
            } 
        });
  
        deudasTableView.setEditable(Boolean.FALSE);
        
        deudasTableView.setRowFactory((Object tv) -> {
            TableRow<CuotaDeudaTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    CuotaDeudaTable rowData = row.getItem();
                    if (rowData.getPagoMes() == null) {
                        cambiarFecha(deuda, rowData);
                    }
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
