/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.DataSourceRolIndividual;
import aplicacion.control.reports.ReporteIessVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButtonBlue;
import hibernate.dao.PagoMesItemDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.PagoMesItem;
import hibernate.model.RolIndividual;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
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
import static aplicacion.control.util.Fechas.getFechaConMes;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Yornel
 */
public class ReportesEmpleadosController implements Initializable {
    
    public final static int REPORTE_HORAS = 1;
    public final static int REPORTE_JUBILACION = 2;
    public final static int REPORTE_APORTE = 3;
    public final static int REPORTE_RESERVA= 4;
    public final static int REPORTE_VACACIONES = 5;
    public final static int REPORTE_SEGUROS = 6;
    public final static int REPORTE_UNIFORMES = 7;
    public final static int REPORTE_BONOS = 8;
    public final static int REPORTE_TRANSPORTE = 9;
    public final static int REPORTE_DECIMO_TERCERO = 10;
    public final static int REPORTE_DECIMO_CUARTO = 11;
    public final static int REPORTE_IESS = 12;
    
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
    private TableColumn telefonoColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> imprimirColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private CheckBox checkBoxPermitir;
    
    @FXML
    private CheckBox checkBoxMarcarTodos;
    
    @FXML
    private Label contador;
    
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
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    private Stage dialogLoading;
    
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
    public void onClickMore(ActionEvent event) throws ParseException {
        if (checkBoxPermitir.isSelected()) {
            fin = fin.plusMonths(1);
        } else {
            fin = fin.plusMonths(1);
            inicio = fin.minusMonths(1).plusDays(1);
        }
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);  
    }
    
    @FXML
    public void onClickLess(ActionEvent event) throws ParseException  {
        if (checkBoxPermitir.isSelected()) {
            inicio = inicio.minusMonths(1);
        } else {
            inicio = inicio.minusMonths(1);
            fin = inicio.plusMonths(1).minusDays(1);
        }
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);  
    }
    
    @FXML
    public void permitirAmpliar(ActionEvent event) {
        if (!checkBoxPermitir.isSelected()) {
            fin = inicio.plusMonths(1).minusDays(1);
            fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);  
        }
    }
    
    @FXML
    public void marcarTodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxMarcarTodos.isSelected()) {
                empleadoTable.setAgregar(true);
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
        contarSelecciones();
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
    
    public ArrayList<RolIndividual> getRoles() {
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        
        ArrayList<RolIndividual> rolIndividualsParaImprimir = new ArrayList<>();
        
        for (EmpleadoTable empleadoTable: empleadosTable) {
            
            Usuario empleado = null;
            
            for (Usuario user: usuarios) {
                if (Objects.equals(empleadoTable.getId(), user.getId())) {
                    empleado = user;
                }
            }
            
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
            Double reservaTotalTextValor = 0d;
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
            
            System.out.println("fecha inicio "+inicio.getFecha());
            System.out.println("fecha fin "+fin.getFecha());
            
            List<RolIndividual> rolIndividuals = new RolIndividualDAO()
                    .findAllByRangoFechaAndEmpleadoId(inicio.getFecha(), fin.getFecha(), empleadoTable.getId());
            RolIndividual pagoRol = new RolIndividual();
            
            for (RolIndividual pago: rolIndividuals) {
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
                reservaTotalTextValor += pago.getReserva();
                totalTextValor += pago.getTotalIngreso();

                montoSumplementariasTextValor += pago.getMontoHorasSuplementarias();
                montoSobreTiempoTextValor += pago.getMontoHorasSobreTiempo();
                montoBonoTextValor += pago.getBono();
                montoTransporteTextValor += pago.getTransporte();
                montoJubilacionTextValor += pago.getJubilacionPatronal(); 
                montoAportePatronalTextValor += pago.getAportePatronal(); 
                montoSegurosTextValor += pago.getSeguros(); 
                montoUniformasTextValor += pago.getUniformes(); 
            }
            
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
            pagoRol.setReserva(reservaTotalTextValor);
            pagoRol.setJubilacionPatronal(montoJubilacionTextValor);
            pagoRol.setAportePatronal(montoAportePatronalTextValor);
            pagoRol.setSeguros(montoSegurosTextValor);
            pagoRol.setUniformes(montoUniformasTextValor);
            pagoRol.setTotalIngreso(totalTextValor);
            pagoRol.setEmpleado(empleado.getApellido()+ " " + empleado.getNombre());
            pagoRol.setCedula(empleado.getCedula());
            pagoRol.setEmpresa(empleado.getDetallesEmpleado().getEmpresa().getNombre());
            pagoRol.setSueldo(empleado.getDetallesEmpleado().getSueldo());
            pagoRol.setUsuario(empleado);
            if (empleado.getDetallesEmpleado().getAcumulaDecimos()) 
                pagoRol.setDecimosPagado(Boolean.FALSE);
            else 
                pagoRol.setDecimosPagado(Boolean.TRUE);
            
            rolIndividualsParaImprimir.add(pagoRol);
        }
        
        return rolIndividualsParaImprimir;
        
    }
    
    public void imprimir(File file, int reporte) {
        
        dialogWait();
        
        ArrayList<RolIndividual> roles = getRoles();
        System.out.println("roles: "+roles.size());
        DataSourceRolIndividual datasource = new DataSourceRolIndividual();
        datasource.addAll(roles);
        
        try {
            InputStream inputStream = null;
            String reportName = "";
            switch (reporte) {
                case REPORTE_HORAS:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_HORAS);
                    reportName = "horas extras";
                    break;
                case REPORTE_JUBILACION:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_JUBILACION);
                    reportName = "Jubilacion patronal";
                    break;
                case REPORTE_APORTE:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_APORTE);
                    reportName = "Aporte patronal";
                    break;
                case REPORTE_RESERVA:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_RESERVA);
                    reportName = "Fondo de Reserva";
                    break;
                case REPORTE_VACACIONES:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_VACACIONES);
                    reportName = "Vacaciones";
                    break;
                case REPORTE_SEGUROS:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_SEGUROS);
                    reportName = "Seguros";
                    break;
                case REPORTE_UNIFORMES:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_UNIFORMES);
                    reportName = "Uniforme";
                    break;
                case REPORTE_TRANSPORTE:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_TRANSPORTE);
                    reportName = "Transporte";
                    break;
                case REPORTE_BONOS:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_BONOS);
                    reportName = "Bonos";
                    break;
                case REPORTE_DECIMO_TERCERO:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_DECIMO_TERCERO);
                    reportName = "Decimo Tercero";
                    break;
                case REPORTE_DECIMO_CUARTO:
                    inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_DECIMO_CUARTO);
                    reportName = "Decimo Cuarto";
                    break;
            }
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            if (reporte == REPORTE_HORAS) {
                parametros.put("lapso", getFechaConMes(inicio.minusDays(7)) + " al " + getFechaConMes(fin.minusDays(7)));
            } else {
                parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            }
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (roles.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = reportName + " " + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el reporte de " + reportName + " de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    public void imprimirIESS(File file) {
        
        dialogWait();
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        
        for (EmpleadoTable empleadoTable: empleadosTable) {
            
            Double total = 0d;
            
            for (PagoMesItem pagoMesItem: new PagoMesItemDAO()
                    .findAllByEmpleadoIdAndClaveAndRangoFecha(empleadoTable.getId(), 
                            Const.IP_IESS, inicio.getFecha(), fin.getFecha())){
                total += pagoMesItem.getDeduccion();
            }
            empleadoTable.setTotalIess(total);
        }
        
        ReporteIessVarios datasource = new ReporteIessVarios();
        datasource.addAll(empleadosTable);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_IESS);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (empleadosTable.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else 
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "IESS " + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el reporte de IESS de todos los empleado";
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
        dialogStage.setTitle("Reporte");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("OK");
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
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Reportes");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button horasButton = new MaterialDesignButtonBlue("Horas Extras");
        Button iessButton = new MaterialDesignButtonBlue("IESS");
        Button decimo3Button = new MaterialDesignButtonBlue("Decimo Tercero");
        Button decimo4Button = new MaterialDesignButtonBlue("Decimo Cuarto");
        Button jubilacionButton = new MaterialDesignButtonBlue("Jubilacion Pat.");
        Button aporteButton = new MaterialDesignButtonBlue("Aporte Pat.");
        Button reservaButton = new MaterialDesignButtonBlue("Fondo de Res.");
        Button transporteButton = new MaterialDesignButtonBlue("Transporte");
        Button bonosButton = new MaterialDesignButtonBlue("Bonos");
        Button vacacionesButton = new MaterialDesignButtonBlue("Vacaciones");
        Button seguroButton = new MaterialDesignButtonBlue("Seguro");
        Button uniformeButton = new MaterialDesignButtonBlue("Uniforme");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(horasButton, iessButton, decimo3Button, decimo4Button, 
                jubilacionButton, aporteButton, reservaButton, bonosButton, transporteButton,
                vacacionesButton, seguroButton, uniformeButton).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        dialogStage.show();
        horasButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_HORAS);
            dialogStage.close();
        });
        jubilacionButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_JUBILACION);
            dialogStage.close();
        });
        aporteButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_APORTE);
            dialogStage.close();
        });
        reservaButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_RESERVA);
            dialogStage.close();
        });
        vacacionesButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_VACACIONES);
            dialogStage.close();
        });
        seguroButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_SEGUROS);
            dialogStage.close();
        });
        uniformeButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_UNIFORMES);
            dialogStage.close();
        });
        transporteButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_TRANSPORTE);
            dialogStage.close();
        });
        bonosButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_BONOS);
            dialogStage.close();
        });
        decimo3Button.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_DECIMO_TERCERO);
            dialogStage.close();
        });
        decimo4Button.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_DECIMO_CUARTO);
            dialogStage.close();
        });
        iessButton.setOnAction((ActionEvent e) -> {
            dialogoRuta(REPORTE_IESS);
            dialogStage.close();
        });
    }
    
    public void contarSelecciones() {
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        int count = empleadosTable.size();
        switch (count) {
            case 0:
                contador.setText("");
                break;
            case 1:
                contador.setText("1 empleado seleccionado");
                break;
            default:
                contador.setText(count + " empleados seleccionados");
                break;
        }
    }
    
    public void dialogoRuta(int reporte) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Reportes");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new MaterialDesignButtonBlue("Seleccionar ruta");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta de guardado para el documento"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                if (reporte == REPORTE_IESS)
                    imprimirIESS(file);
                else
                    imprimir(file, reporte);
            }
        });
        dialogStage.show();
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), inicio));
        
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.setId(user.getId());
            empleado.setNombre(user.getNombre());
            empleado.setApellido(user.getApellido());
            empleado.setCedula(user.getCedula());
            empleado.setEmpresa(user.getDetallesEmpleado().getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
            empleado.setAgregar(false);
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
       empleadosTableView.setItems(data);

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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        telefonoColumna.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        
        imprimirColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        imprimirColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxSeleccionar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxSeleccionar);
                if (checkBoxSeleccionar != null) {
                    checkBoxSeleccionar.setSelected(empleadoTable.getAgregar());
                }
                checkBoxSeleccionar.setOnAction(event -> {
                     empleadoTable.setAgregar(checkBoxSeleccionar.isSelected());
                     contarSelecciones();
                });
            }
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    //new UsuarioDAO().findById(rowData.getId());
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
