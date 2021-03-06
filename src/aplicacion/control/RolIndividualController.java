/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolCliente;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.dao.RolClienteDAO;
import hibernate.model.Empresa;
import hibernate.model.RolCliente;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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

/**
 *
 * @author Yornel
 */
public class RolIndividualController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private TableView controlIndividualTableView;
    
    /// Columnas
    @FXML
    private TableColumn columnaCliente;
    
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
    private TableColumn columnaJubilacion;
    
    @FXML 
    private TableColumn columnaAporte;
    
    @FXML 
    private TableColumn columnaSeguros;
    
    @FXML 
    private TableColumn columnaUniforme;
    
    @FXML
    private TableColumn columnaTotal;
    
    //////////////////////////////////////
    
    // Totales de el cliente

    private Double sueldoTotalTextValor;

    private Double extraTextValor;

    private Double bonosTextValor;

    private Double vacacionesTextValor;

    private Double subTotalTextValor;

    private Double decimosTotalTextValor;
    
    private Double decimoTerceroTotalTextValor;
    
    private Double decimoCuartoTotalTextValor;
    
    private Double montoSumplementariasTextValor; 
    
    private Double montoSobreTiempoTextValor; 
    
    private Double montoBonoTextValor; 
    
    private Double montoTransporteTextValor; 
    
    private Double montoJubilacionTextValor; 
    
    private Double montoAportePatronalTextValor; 
    
    private Double montoSegurosTextValor;
    
    private Double montoUniformesTextValor;
    
    private Double totalTextValor;
    
    // Totales Text
    
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
    
    @FXML
    private Label reservaText;
    
    @FXML
    private Label jubilacionText;
    
    @FXML
    private Label aporteText;
    
    @FXML
    private Label segurosText;
    
    @FXML
    private Label uniformeText;
    
    ///////////////////////////////
    
    @FXML
    private Button buttonAtras;
    
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
    
    private ObservableList<RolCliente> data;
    
    ArrayList<RolCliente> pagos;
    
    private Empresa empresa;
    
    private Usuario empleado;
    
    private Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnEmpleados(ActionEvent event) {
        aplicacionControl.mostrarEmpleadosParaRol(empresa, stagePrincipal);
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
    public void onClickMore(ActionEvent event) {
        inicio = inicio.plusMonths(1);
        fin = fin.plusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
        setTableInfo(inicio, fin);
        
    }
    
    @FXML
    public void onClickLess(ActionEvent event)  {
        inicio = inicio.minusMonths(1);
        fin = fin.minusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa); 
        setTableInfo(inicio, fin);
        
    }
    
    public void imprimir(File file) {
        
        dialogWait();
        
        ReporteRolCliente datasource = new ReporteRolCliente();
        datasource.addAll((List<RolCliente>) controlIndividualTableView.getItems());
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_INDIVIDUAL);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("empleado", empleado.getApellido()+ " " + empleado.getNombre());
            parametros.put("cedula", empleado.getCedula());
            parametros.put("cargo", empleado.getDetallesEmpleado().getCargo().getNombre());
            parametros.put("fecha", Fechas.getFechaConMes(inicio) + " al " + Fechas.getFechaConMes(fin));
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (pagos.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "rol_individual_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo del rol individual del empleado " 
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
        dialogStage.setTitle("Imprimir Rol Individual");
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
        dialogStage.show();
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
        dialogStage.setTitle("Imprimir Rol Individual");
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
    
    public void setEmpleado(Usuario empleado, Empresa empresa) throws ParseException {
        this.empresa = empresa;
        this.empleado = empleado;
        
         inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
        
        setTableInfo(inicio, fin);
    }
    
    public void setTableInfo(Fecha inicio, Fecha fin) {
        this.inicio = inicio;
        this.fin = fin;
        
        RolClienteDAO pagoDAO = new RolClienteDAO();
        pagos = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoId(inicio.getFecha(), empleado.getId()));
        
        sueldoTotalTextValor = 0d;
        extraTextValor = 0d;
        bonosTextValor = 0d;
        vacacionesTextValor = 0d;
        subTotalTextValor = 0d;
        decimosTotalTextValor = 0d;
        decimoTerceroTotalTextValor = 0d;
        decimoCuartoTotalTextValor = 0d;
        totalTextValor = 0d;
        montoSumplementariasTextValor = 0d; 
        montoSobreTiempoTextValor = 0d;
        montoBonoTextValor = 0d; 
        montoTransporteTextValor = 0d;
        montoJubilacionTextValor = 0d; 
        montoAportePatronalTextValor = 0d; 
        montoSegurosTextValor = 0d; 
        montoUniformesTextValor = 0d;
        
        for (RolCliente pago: pagos){
            sueldoTotalTextValor += pago.getSueldo();
            extraTextValor += pago.getMontoHorasSobreTiempo() + pago.getMontoHorasSuplementarias();
            pago.setMontoHorasExtras(extraTextValor);
            bonosTextValor += pago.getTotalBonos();
            vacacionesTextValor += pago.getVacaciones();
            subTotalTextValor += pago.getSubtotal();
            decimosTotalTextValor += pago.getDecimoTercero()+ pago.getDecimoCuarto();
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
            montoUniformesTextValor += pago.getUniformes(); 
        }
        
        data = FXCollections.observableArrayList(); 
        data.addAll(pagos);
        controlIndividualTableView.setItems(data);
      
        sueldoTotalText.setText(String.format( "%.2f", sueldoTotalTextValor));
        extraText.setText(String.format( "%.2f", extraTextValor));
        bonosText.setText(String.format( "%.2f", bonosTextValor));
        vacacionesText.setText(String.format( "%.2f", vacacionesTextValor));
        subTotalText.setText(String.format( "%.2f", subTotalTextValor));
        decimosTotalText.setText(String.format( "%.2f", decimoTerceroTotalTextValor) + "|" + String.format( "%.2f", decimoCuartoTotalTextValor));
        totalText.setText(String.format( "%.2f", totalTextValor));
        jubilacionText.setText(String.format( "%.2f", montoJubilacionTextValor));
        aporteText.setText(String.format( "%.2f", montoAportePatronalTextValor));
        segurosText.setText(String.format( "%.2f", montoSegurosTextValor));
        uniformeText.setText(String.format( "%.2f", montoUniformesTextValor));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        
        controlIndividualTableView.setEditable(Boolean.FALSE);
        
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNombre"));
        
        columnaNormales.setCellValueFactory(new PropertyValueFactory<>("horasNormales"));
        
        columnaSumplementarias.setCellValueFactory(new PropertyValueFactory<>("horasSuplementarias"));
        
        columnaSobreTiempo.setCellValueFactory(new PropertyValueFactory<>("horasSobreTiempo"));
        
        columnaHoras.getColumns().clear();
        columnaHoras.getColumns().addAll(columnaNormales, columnaSumplementarias, columnaSobreTiempo);
        
        columnaSueldo.setCellValueFactory(new PropertyValueFactory<>("salario"));
        
        columnaExtra.setCellValueFactory(new PropertyValueFactory<>("montoHorasExtras"));
        
        columnaBonos.setCellValueFactory(new PropertyValueFactory<>("totalBonos"));
        
        columnaIngresos.getColumns().clear();
        columnaIngresos.getColumns().addAll(columnaSueldo, columnaExtra, columnaBonos);
        
        columnaVacaciones.setCellValueFactory(new PropertyValueFactory<>("vacaciones"));
        
        columnaSubTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        columnaTercero.setCellValueFactory(new PropertyValueFactory<>("decimoTercero"));
        columnaCuarto.setCellValueFactory(new PropertyValueFactory<>("decimoCuarto"));
        
        columnaDecimos.getColumns().clear();
        columnaDecimos.getColumns().addAll(columnaTercero, columnaCuarto);
        
        columnaJubilacion.setCellValueFactory(new PropertyValueFactory<>("jubilacionPatronal"));
        
        columnaAporte.setCellValueFactory(new PropertyValueFactory<>("aportePatronal"));
        
        columnaSeguros.setCellValueFactory(new PropertyValueFactory<>("seguros"));
        
        columnaUniforme.setCellValueFactory(new PropertyValueFactory<>("uniformes"));
        
        columnaTotal.setCellValueFactory(new PropertyValueFactory<>("totalIngreso"));
        
        controlIndividualTableView.setRowFactory((Object tv) -> {
            TableRow<RolCliente> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    RolCliente rowData = row.getItem();
                    aplicacionControl.mostrarRolClienteEmpleado(rowData);
                }
            });
            return row ;
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
        buttonImprimir.setTooltip(
            new Tooltip("Pagar a seleccionados")
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
