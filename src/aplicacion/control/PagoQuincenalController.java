/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.PagosTotalEmpleadoController.getToday;
import aplicacion.control.reports.ReporteRolDePagoQuincenal;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
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
import javafx.fxml.FXMLLoader;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Fechas.getFechaConMes;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

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
    private Button buttonAtras;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
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
    Dialog<Void> dialog;
    
    Stage dialogLoading;
    
    Integer count;
    
    Label loader;
    
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
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
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
        if (hayParaPagar()) {
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
            dialogStage.show();
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Adelanto Quincenal");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("No hay empleados seleccionados para hacer el pago."), buttonOk).
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
    }
    
    public Boolean hayParaPagar() {
        for (EmpleadoTable empleadoTable: data) {
            if (empleadoTable.getPagar()) {
                return true;
            }
        }
        return false;
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoQuincenalController.DataBaseThread(1, file, enviarCorreo);
        executor.execute(worker);
        executor.shutdown();

        loadingModeImprimir();
        
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
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoQuincenalController.DataBaseThread(0);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
        
        
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
        dialogStage.show();
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
        if (dateTime.getDayOfMonth() >= empresa.getComienzoMes() ) {
            inicio = new Timestamp(dateTime.withDayOfMonth(empresa
                    .getComienzoMes()).getMillis());
            fin = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                    .plusMonths(1).minusDays(1).getMillis());
        } else {
            fin = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                    .minusDays(1).getMillis());
            inicio = new Timestamp(dateTime.withDayOfMonth(empresa
                    .getComienzoMes()).minusMonths(1).getMillis());
        }
        pickerDe.setValue(Fechas.getLocalFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getLocalFromTimestamp(fin));
        
        setTableInfo();
    }
    
    public void setTableInfo() {
        checkBoxPagarTodos.setSelected(false);
        count = 0;
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivo(empresa.getId()));
        
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
                    .findInDeterminateTimeByUsuarioId(inicio, empleado.getId());
            if (pagoQuincena != null) {
                empleado.setQuincenal(pagoQuincena.getMonto());
                empleado.setPagado("Si");
                empleado.setPagar(false);
                empleado.setPagoQuincena(pagoQuincena);
            } else {
                if (user.getDetallesEmpleado().getQuincena() != null) { 
                    if (new PagoMesDAO().findInDeterminateTimeByUsuarioId(inicio, 
                        empleado.getId()) == null) {
                        empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
                    } else {
                        empleado.setProblem(Boolean.TRUE);
                        count ++;
                        empleado.setQuincenal(0d);
                        empleado.setPagar(false);
                    }
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
        
        if (count > 0) {
            dialogAdvertencia();
        }
         
        filtro();
        
        pagarATodos(null);
    }
    
     public void dialogAdvertencia() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Precaución");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        MaterialDesignButton buttonOk = new MaterialDesignButton("Si");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonOk)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Hay empleados con pago mensual ya generado y sin adelanto quincenal,"),
                 new Text("Si desea hacer el pago quincenal a estos empleados"), 
                 new Text("por favor borre el pago mensual primero."), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
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
        chequearFiltro(filteredData);
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
    
    public void dialogoBorrarPago(int usuarioId) {
        
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
                    borrarPago(usuarioId);
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
    
    void borrarPago(int usuarioId) {
        if (new PagoMesDAO().findInDeterminateTimeByUsuarioId(inicio, 
                        usuarioId) == null) {
           PagoQuincena pagoQuincena = new PagoQuincenaDAO()
                    .findInDeterminateTimeByUsuarioId(inicio, usuarioId);
            new PagoQuincenaDAO().delete(pagoQuincena);
            HibernateSessionFactory.getSession().flush();
            String detalles = "elemino el pago quincenal numero " + pagoQuincena.getId()
                    + ", del empleado " + pagoQuincena.getUsuario().getNombre() 
                    + " " + pagoQuincena.getUsuario().getApellido();
            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
            setTableInfo();
            dialogLoading.close();
            dialogoBorradoCompletado();    
        } else {
            dialogLoading.close();
            dialogoBorradoError();  
        }
        
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
        children(new Text("Se borro el adelanto quincenal con exito."), buttonOk).
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
                    if (rowData.getPagado().equalsIgnoreCase("Si"))
                        mostrarPagoQuincenalPagado(rowData
                                .getPagoQuincena());
                }
            });
            return row;
        });
        
        pagarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        pagarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxpagar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                setGraphic(checkBoxpagar);
                if (checkBoxpagar != null) {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si") 
                            || empleadoTable.getQuincenal().equals(0d)) {
                        checkBoxpagar.setDisable(true);
                        empleadoTable.setPagar(false);
                    } else {
                        checkBoxpagar.setDisable(false);
                    }
                    checkBoxpagar.setSelected(empleadoTable.getPagar());
                }
                checkBoxpagar.setOnAction(event -> {
                     empleadoTable.setPagar(checkBoxpagar.isSelected());
                });
                
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else if (empleadoTable.getProblem()) {
                    getTableRow().setStyle("-fx-background-color:lightcoral");
                } else {
                    getTableRow().setStyle("");
                }
            }
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
        buttonPagar.setTooltip(
            new Tooltip("Pagar a seleccionados")
        );
        buttonPagar.setOnMouseEntered((MouseEvent t) -> {
            buttonPagar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/pagar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonPagar.setOnMouseExited((MouseEvent t) -> {
            buttonPagar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/pagar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
    } 
    
    public void mostrarPagoQuincenalPagado(PagoQuincena pagoQuincena) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoQuincenalPagado.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Pago Quincenal");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    PagoQuincenalPagadoController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(aplicacionControl);
                    controller.setPagoQuincenalController(this);
                    controller.setPago(pagoQuincena);
                    ventana.show();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
              aplicacionControl.noPermitido();
            }
        }
    }
    
    private void loadingMode(){
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stagePrincipal);//stage here is the stage of your webview
        dialog.initStyle(StageStyle.TRANSPARENT);
        Label loader = new Label("   Cargando, por favor espere...");
        loader.setContentDisplay(ContentDisplay.LEFT);
        loader.setGraphic(new ProgressIndicator());
        dialog.getDialogPane().setGraphic(loader);
        dialog.getDialogPane().setStyle("-fx-background-color: #E0E0E0;");
        dialog.getDialogPane().setPrefSize(250, 75);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3); 
        ds.setOffsetY(1.3); 
        ds.setColor(Color.DARKGRAY);
        dialog.getDialogPane().setEffect(ds);
        dialog.show();
    }
    
    private void loadingModeImprimir(){
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stagePrincipal);//stage here is the stage of your webview
        dialog.initStyle(StageStyle.TRANSPARENT);
        loader = new Label("   Imprimiendo, por favor espere...");
        loader.setContentDisplay(ContentDisplay.LEFT);
        loader.setGraphic(new ProgressIndicator());
        dialog.getDialogPane().setGraphic(loader);
        dialog.getDialogPane().setStyle("-fx-background-color: #E0E0E0;");
        dialog.getDialogPane().setPrefSize(250, 75);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3); 
        ds.setOffsetY(1.3); 
        ds.setColor(Color.DARKGRAY);
        dialog.getDialogPane().setEffect(ds);
        dialog.show();
    }
    
    private void loadingModeUpdate(int current, int total){
        loader.setText("   Imprimiendo "+current+"/"+total+", espere...");
    }
    
    public void closeDialogMode() {
        if (dialog != null) {
           Stage toClose = (Stage) dialog.getDialogPane()
                   .getScene().getWindow();
           toClose.close();
           dialog.close();
           dialog = null;
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
    
    public class DataBaseThread implements Runnable {
        
        public final Integer GUARDAR = 0;
        public final Integer IMPRIMIR = 1;
        public final Integer BORRAR = 2;
        
        Integer opcion;
        File file;
        Boolean enviarCorreo;
        
        public DataBaseThread(Integer opcion){
            this.opcion = opcion;
        }
        
        public DataBaseThread(Integer opcion, File file, Boolean enviarCorreo){
            this.opcion = opcion;
            this.file = file;
            this.enviarCorreo = enviarCorreo;
        }

        @Override
        public void run() {
    
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        try {
                            if (Objects.equals(opcion, GUARDAR)) {
                                hacerPago();
                            } else if (Objects.equals(opcion, IMPRIMIR)) {
                                imprimir(file, Boolean.TRUE);
                            } 
                        } catch (ParseException ex) {
                            closeDialogMode();
                        }
                    }
             }, 1000, 1000);
            
        }
        
        private void hacerPago() throws ParseException {
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
            
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoGenerarAdelantoCompletado();
                }
            });
        }
        
        public void imprimir(File file, Boolean enviarCorreo) {
        
            for (PagoQuincena pagoQuincena: pagosQuincenal) {
                
                Usuario user = pagoQuincena.getUsuario();

                ReporteRolDePagoQuincenal datasource = new ReporteRolDePagoQuincenal();
                datasource.add(pagoQuincena);
                
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        loadingModeUpdate(pagosQuincenal.indexOf(pagoQuincena)
                                +1,pagosQuincenal.size());
                    }
                });

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
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoCompletado();
                }
            });
        }
    }
}
