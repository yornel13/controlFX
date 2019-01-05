/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.asincronas.PrintFondoReservaReport;
import aplicacion.control.eventos.DialogResponse;
import aplicacion.control.eventos.PrintResult;
import aplicacion.control.reports.ReporteFondoReserva;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.DialogUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Fechas.getFechaConMes;
import aplicacion.control.util.GuardarText;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import aplicacion.control.util.MaterialDesignButtonBlue;
import aplicacion.control.util.Numeros;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.RolIndividual;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.PagoVacacionesDAO;
import hibernate.model.PagoVacaciones;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.stage.DirectoryChooser;
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
public class FondoReservaEmpleadosController extends BaseController 
        implements Initializable, DialogResponse, PrintResult {
    
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
    private TableColumn valorColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> marcarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;

    @FXML
    private Button buttonImprimir;  
    
    @FXML
    private Button buttonBank;
    
    @FXML
    private Button buttonSend;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
      
    @FXML
    private CheckBox checkBoxPermitir;
    
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
    
    @FXML
    private CheckBox checkBoxTodos;
    
    private Fecha inicio;
    private Fecha fin;
    Stage dialogLoading;
    
    private ArrayList<String> textosDAT;
    private ArrayList<String> textosTXT;
    
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
    public void send(ActionEvent event) throws ParseException {
        DialogUtil.confirm("Fondo de Reserva", 
                "¿Seguro que desea enviar el recibo de fondo de reserva \n"
                        + "a estos empleados?", this);
    }
    @Override
    public void onDialogOK() {
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        
        if (empleadosTable.isEmpty()) {
            DialogUtil.error("Fondo de Reserva", "No has seleccionado ningun empleado.");
            return;
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PrintFondoReservaReport(empleadosTable, inicio, fin, this);
        executor.execute(worker);
        executor.shutdown();

        showPrintingDialog();
    }

    @Override
    public void onDialogCancel() {
        DialogUtil.error("Fondo de Reserva", "Error envio cancelado.");
    }
    
    @Override
    public void onPrintUpdate(int current, int total) {
        updatePrintingDialog(current, total);
    }

    @Override
    public void onPrintSuccessful() {
        closePrintingDialog();
        DialogUtil.completed("Fondo de Reserva", "Correos enviados exitosamente!");
    }

    @Override
    public void onPrintFailure(String error) {
        closePrintingDialog();
        DialogUtil.error("Fondo de Reserva", "Error al intentar enviar correos.");
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
        setTableInfo();
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
        setTableInfo();
    }
    
    @FXML
    public void permitirAmpliar(ActionEvent event) {
        if (!checkBoxPermitir.isSelected()) {
            fin = inicio.plusMonths(1).minusDays(1);
            fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);  
        }
    }
    
    @FXML
    private void marcarTodos(ActionEvent event) throws ParseException {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxTodos.isSelected() && empleadoTable.getMonto() != null) {
                empleadoTable.setAgregar(true);
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
    @FXML
    public void generarBank() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Bizbank");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_select.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new MaterialDesignButtonBlue("Seleccionar ruta");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione la ruta de guardado de los bizbank"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                generadorBank(file);
            }
        });
        dialogStage.show();
    }
    
    public void generadorBank(File file) {
        
        dialogWait();
    
        textosDAT = new ArrayList<>();
        textosTXT = new ArrayList<>();
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        
        for (EmpleadoTable table: 
                empleadosTable) {
            
            String reportName = "";
            Double reportMonto = 0d;
            
            reportMonto = Double.valueOf(table.getMonto());
            reportName = "FONDO RESERVA  ";
               
            
            textosDAT.add(crearLineaDAT(table.getUsuario(), reportMonto));
            textosTXT.add(crearLineaTXT(table.getUsuario(), reportMonto, reportName));
            
        }
        if (textosDAT.size() > 0 && textosTXT.size() > 0) {
            if (file != null) {
                new GuardarText().saveFile(textosDAT, getFileNameDat(file));
                new GuardarText().saveFile(textosTXT, getFileNameTXT(file));
                dialogoCompletado();
            } 
        } else {
            dialogoErrorBizBank();
        }
        dialogLoading.close();
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
        children(new Text("No se pueden generar los archivos porque\n"
                + "no hay pagos en la fecha seleccionada."), buttonOk).
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
    
    public void completado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Ajustes de fondos de reserva hechos con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public boolean isSelected() {
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        int count = empleadosTable.size();
        if (count > 0) {
            return true;
        }
        return false;
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        if (isSelected()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Ruta de guardado");
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
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new MaterialDesignButtonBlue("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
            children(new Text("No has seleccionado ningun empleado."), 
                    buttonOk).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            dialogStage.show();
            buttonOk.setMaxWidth(60);
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                dialogStage.close();
            });
        }
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
        
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        
        ReporteFondoReserva datasource = new ReporteFondoReserva();
        datasource.addAll(empleadosTable);
        
        try {
            InputStream inputStream = null;
            String reportName = "";
            inputStream = new FileInputStream(Const.REPORTE_SUMATORIA_RESERVA);
            reportName = "Fondo de Reserva";
            
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (data.isEmpty())
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
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa; 
        
        inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
       
        setTableInfo();
    }
    
    public void setTableInfo() {
        
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
            empleado.setEmpresa(user.getDetallesEmpleado()
                    .getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado()
                    .getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado()
                    .getCargo().getNombre());
            empleado.setUsuario(user);
            
            List<RolIndividual> rolIndividuals = new RolIndividualDAO()
                    .findAllByRangoFechaAndEmpleadoId(inicio.getFecha(), fin.getFecha(), empleado.getId());
            if (rolIndividuals != null && !rolIndividuals.isEmpty()) {
                Double monto = 0d;
                for (RolIndividual rolIndividual: rolIndividuals) {
                    Fecha inicioVac = new Fecha("01", "01", new Fecha(rolIndividual.getInicio()).getAno())
                        .minusYears(1);
                    PagoVacaciones pagoVacaciones = new PagoVacacionesDAO()
                        .findInDeterminateYearByUsuarioId(inicioVac.getAno(), empleado.getId());
                        
                    /*Double montoFR = rolIndividual.getSalario() 
                            + rolIndividual.getTotalMontoHorasExtras() 
                            + rolIndividual.getTotalBonos() 
                            + getVacacionesFromThisMonth(pagoVacaciones);
                    monto += montoFR/12d;*/
                    //Double vacaciones12 = getVacacionesFromThisMonth(pagoVacaciones) /12;
                    monto = rolIndividual.getReserva();
                }
                empleado.setMonto(round(monto).toString());
            }  else {
                empleado.setMonto(null);
            }
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
        chequearFiltro(filteredData);
    }
    
    private Double getVacacionesFromThisMonth(PagoVacaciones pagoVacaciones) {
        
        if (pagoVacaciones != null) {
           Calendar calIni = Calendar.getInstance();
            calIni.setTime(pagoVacaciones.getGoceInicio());

            Calendar calFin = Calendar.getInstance();
            calFin.setTime(pagoVacaciones.getGoceFin());


            Integer mes1int = calIni.get(Calendar.MONTH)+1;
            Integer mes2int = calFin.get(Calendar.MONTH)+1;
            Integer dias1int = 0;
            Integer dias2int = 0;
            Double montoMes1Dou = 0d;
            Double montoMes2Dou = 0d;
            if (mes1int != mes2int) {
                if (mes1int+1 == mes2int) {
                    dias2int = calFin.get(Calendar.DAY_OF_MONTH);
                    dias1int = pagoVacaciones.getDias() - dias2int;
                    Double valorDia = pagoVacaciones.getValor()
                            /pagoVacaciones.getDias().doubleValue();
                    montoMes1Dou = round(valorDia*dias1int);
                    montoMes2Dou = round(valorDia*dias2int);
                } 
            } else {
                if (inicio.getMesInt() == mes1int) {
                /******Entre 12 que es para el fondo de reserva*******/
                    return pagoVacaciones.getValor();
                }
            }
            if (inicio.getMesInt() == mes1int) {
                /******Entre 12 que es para el fondo de reserva*******/
                return montoMes1Dou;
            }
            if (inicio.getMesInt() == mes2int) {
                /******Entre 12 que es para el fondo de reserva*******/
                return montoMes2Dou;
            } 
        }
        return 0d;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        empleadosTableView.setEditable(Boolean.FALSE); // no se puede editar por ahora
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        valorColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        valorColumna.setCellFactory(TextFieldTableCell.forTableColumn());
        valorColumna.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<EmpleadoTable, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<EmpleadoTable, String> t) {
                    Double newValue;
                    try {
                        newValue = round(t.getNewValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace(); // TODO, quitar
                        newValue = Double.valueOf(t.getOldValue());
                        //dialogoErrorCuotas();
                    }
                    
                    EmpleadoTable empleadoTable = ((EmpleadoTable) t.getTableView().getItems()
                                .get(t.getTablePosition().getRow())); 
                    if (t.getOldValue() != null) {    
                        empleadoTable.setAgregar(true);
                        Double monto = newValue; 
                        empleadoTable.setMonto(monto.toString());
                    }
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                }
            }
        );
        
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxSeleccionar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                setGraphic(checkBoxSeleccionar);
                if (checkBoxSeleccionar != null) {
                    checkBoxSeleccionar.setSelected(empleadoTable.getAgregar());
                }
                checkBoxSeleccionar.setOnAction(event -> {
                    empleadoTable.setAgregar(checkBoxSeleccionar.isSelected());
                    if (empleadoTable.getMonto() == null) {
                        checkBoxSeleccionar.setSelected(false);
                        empleadoTable.setAgregar(false);
                    }
                });
            }
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    //mostrarEditarQuincenal(new UsuarioDAO().findById(rowData.getId()));
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
        buttonBank.setTooltip(
            new Tooltip("Generar .DAT y .TXT")
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
        buttonSend.setTooltip(
            new Tooltip("Enviar correos")
        );
        buttonSend.setOnMouseEntered((MouseEvent t) -> {
            buttonSend.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_send.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonSend.setOnMouseExited((MouseEvent t) -> {
            buttonSend.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_send.png'); "
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
    
    String crearLineaDAT(Usuario user, Double reportMonto) {
        String monto = Numeros.roundToString(reportMonto);
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
    
    String crearLineaTXT(Usuario user, Double reportMonto, String reportName) {
        String monto = Numeros.roundToString(reportMonto);
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
                +espacios+partEntera+partDecimal+reportName
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
