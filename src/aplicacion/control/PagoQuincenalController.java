/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.PagosTotalEmpleadoController.getToday;
import aplicacion.control.reports.ReporteRolDePagoIndividual;
import aplicacion.control.reports.ReporteRolDePagoQuincenal;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.PagoMesDAO;
import hibernate.dao.PagoQuincenaDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.PagoQuincena;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
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
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class PagoQuincenalController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
   
    @FXML
    private Button administradoresButton;
    
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
    private TableColumn quincenalColumna;
    
    @FXML 
    private TableColumn pagadoColumna;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    public Timestamp inicio;
    public Timestamp fin;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<PagoQuincena> pagosQuincenal;
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    @FXML
    public void onClickMore(ActionEvent event) throws ParseException {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().plusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) throws ParseException  {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().minusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        setTableInfo();
    }
    
    @FXML
    private void pagarATodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxPagarTodos.isSelected()) {
                if (empleadoTable.getPagado().equalsIgnoreCase("Si") 
                        || empleadoTable.getQuincenal().equals(0d)) {
                    empleadoTable.setPagar(false);
                } else {
                    empleadoTable.setPagar(true);
                }
            } else {
                empleadoTable.setPagar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
    @FXML
    public void pagarAdelanto(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pagar Adelanto Quincenal");
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
        children(new Text("¿Seguro que desea pagar el adelanto Quincenal "
                + "a estos empleado?"), hBox).
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
        dialogStage.showAndWait();
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        for (PagoQuincena pagoQuincena: pagosQuincenal) {
            Usuario user = pagoQuincena.getUsuario();
            
            ReporteRolDePagoQuincenal datasource = new ReporteRolDePagoQuincenal();
            datasource.add(pagoQuincena);

            try {
                InputStream inputStream = new FileInputStream(Const.REPORTE_PAGO_ADELANTO_QUINCENAL);

                Map<String, String> parametros = new HashMap();
                parametros.put("empleado", user.getNombre() + " " + user.getApellido());
                parametros.put("cedula", user.getCedula());
                parametros.put("cargo", user.getDetallesEmpleado().getCargo().getNombre());
                parametros.put("empresa", user.getDetallesEmpleado().getEmpresa().getNombre());
                parametros.put("numero", pagoQuincena.getId().toString()); 
                parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
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
                    CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                            user.getEmail(), Const.ASUNTO_ADELANTO_QUINCENAL, 
                            "Recibo de adelanto quincenal del mes que empieza el " 
                                    + getFechaConMes(inicio) 
                                    + " y termina el " 
                                    + getFechaConMes(fin), 
                            pdf.getPath(), filename + ".pdf");
                }

            } catch (JRException | IOException ex) {
                Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        pagosQuincenal.clear();
        dialogLoading.close();
        dialogoCompletado();
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
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
    
    public void hacerPago() {
        dialogWait();
        
        pagosQuincenal = new ArrayList<>();
        
        for (Usuario user: usuarios) {
            if (data.get(usuarios.indexOf(user)).getPagar()) {
                PagoQuincena pagoQuincena = new PagoQuincena();
                pagoQuincena.setUsuario(user);
                pagoQuincena.setFecha(new Timestamp(new Date().getTime()));
                pagoQuincena.setMonto(data.get(usuarios.indexOf(user))
                        .getQuincenal());
                pagoQuincena.setInicioMes(inicio);
                pagoQuincena.setFinMes(fin);
                new PagoQuincenaDAO().save(pagoQuincena);
                pagosQuincenal.add(pagoQuincena);
                
                // Registro para auditar
                String detalles = "hizo el adelanto quincenal nro: " 
                        + pagoQuincena.getId() 
                        + " del lapso " + getFechaConMes(inicio)+ " a " 
                        + getFechaConMes(fin) + " para el empleado " 
                        + user.getNombre() + " " + user.getApellido();
                aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            }
        }
        setTableInfo();
        
        dialogLoading.close();
        dialogoGenerarAdelantoCompletado();
    }
    
    public void dialogoGenerarAdelantoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de Adelantos Quincenal");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los pagos adelanto quincenal con exito, \n"
                + " ¿Desea guardar los documento de pago?."), 
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
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de Adelantos Quincenal");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Pagos de adelanto quincenal hecho con exito."), buttonOk).
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
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        DateTime dateTime = new DateTime(getToday().getTime());
        fin = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago())
                .getMillis());
        inicio = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago())
                .minusMonths(1).plusDays(1).getMillis());
        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        checkBoxPagarTodos.setSelected(true);
        setTableInfo();
    }
    
    public void setTableInfo() {
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findByEmpresaIdActivo(empresa.getId()));
        
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
            empleado.setPagado("No");
            empleado.setPagar(true);
            
            PagoQuincena pagoQuincena = new PagoQuincenaDAO()
                    .findInDeterminateTimeByUsuarioId(fin, empleado.getId());
            if (pagoQuincena != null) {
                empleado.setQuincenal(pagoQuincena.getMonto());
                empleado.setPagado("Si");
                empleado.setPagar(false);
            } else {
                if (new PagoMesDAO().findInDeterminateTimeByUsuarioId(fin, 
                        empleado.getId()) == null 
                        && user.getDetallesEmpleado().getQuincena() != null) {
                    empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
                } else {
                    empleado.setQuincenal(0d);
                    empleado.setPagar(false);
                }  
            }
            
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);
         
        filtro();
    }
    
    public void filtro() {
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
        
        quincenalColumna.setCellValueFactory(new PropertyValueFactory<>("quincenal"));
        
        pagadoColumna.setCellValueFactory(new PropertyValueFactory<>("pagado"));
        
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
        
        pagarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pagarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxpagar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxpagar);
                if (checkBoxpagar != null) {
                    checkBoxpagar.setSelected(empleadoTable.getPagar());
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si") 
                            || empleadoTable.getQuincenal().equals(0d)) {
                        checkBoxpagar.setDisable(true);
                    } else {
                        checkBoxpagar.setDisable(false);
                    }
                }
                checkBoxpagar.setOnAction(event -> {
                     empleadoTable.setPagar(checkBoxpagar.isSelected());
                });
            }

            
        });
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