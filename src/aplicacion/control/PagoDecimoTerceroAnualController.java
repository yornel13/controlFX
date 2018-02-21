/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolDecimoTercero;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.GuardarText;
import aplicacion.control.util.Numeros;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.PagoQuincena;
import hibernate.model.Usuario;
import java.io.File;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import hibernate.dao.PagoDecimoDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.DiasVacaciones;
import hibernate.model.PagoDecimo;
import hibernate.model.RolIndividual;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
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
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Yornel
 */
public class PagoDecimoTerceroAnualController implements Initializable {
    
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
    private TableColumn montoColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> imprimirColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonBank;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Label yearLabel;
    
    @FXML
    private Label periodoLabel;
    
    private ObservableList<EmpleadoTable> data;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML
    private CheckBox checkBoxImpTodos;
    
    ArrayList<PagoQuincena> pagosQuincenal;
    private ArrayList<String> textosDAT;
    private ArrayList<String> textosTXT;
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    ArrayList<DiasVacaciones> diasVac;
    Dialog<Void> dialog;
    String periodoALiquidar;
    
    ArrayList<PagoDecimo> pagosDecimo;
    
    ArrayList<EmpleadoTable> empladosImprimir;
    
    Stage dialogLoading;
    
    Label loader;
    
    Integer count;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        yearLabel.setText(String.valueOf(Integer.valueOf(yearLabel.getText())+1));
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        yearLabel.setText(String.valueOf(Integer.valueOf(yearLabel.getText())-1));
        setTableInfo();
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
        setTableInfo();
    }
    
    public void updateTable() {
        empleadosTableView.refresh();
    }
    
    @FXML
    private void pagarATodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxPagarTodos.isSelected()) {
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
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
    private void impTodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxImpTodos.isSelected()) {
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    empleadoTable.setAgregar(true);
                } else {
                    empleadoTable.setAgregar(false);
                }
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
    @FXML
    public void pagarAdelanto(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pagar decimo tercero acumulado");
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
        children(new Text("¿Seguro que desea pagar el decimo tercero acumulado "
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
    }
    
    @FXML
    public void generarBank(ActionEvent event) {
        
        textosDAT = new ArrayList<>();
        textosTXT = new ArrayList<>();
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) data) {
            if (empleadoTable.getAgregar() && empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                textosDAT.add(crearLineaDAT(empleadoTable.getUsuario(), empleadoTable.getPagoDecimo()));
                textosTXT.add(crearLineaTXT(empleadoTable.getUsuario(), empleadoTable.getPagoDecimo()));
            }
        }
        if (textosDAT.size() > 0 && textosTXT.size() > 0) {
            dialogoGenerarDatTxt();
        } else {
            dialogoErrorBizBank();
        }
    }
    
    @FXML
    public void imprimirGeneral(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Reporte - Decimo Terceros");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonSiDocumento, buttonNoDocumento)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Desea imprimir el pago de decimos tercero"),
                new Text("de los empleados seleccionados?"),
                hBox, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonSiDocumento.setMinWidth(50);
        buttonNoDocumento.setMinWidth(50);
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                empladosImprimir = new ArrayList<>();
                for (EmpleadoTable empleado: data) {
                    if (empleado.getAgregar() && 
                            empleado.getPagoDecimo() != null) {
                        empladosImprimir.add(empleado);
                    }
                }
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
    
    public void dialogoErrorBizBank() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Generador de archivos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No se pueden generar los archivos porque no hay empleados\n"
                + "con pagos hechos, o sus pagos son menores a 0."), buttonOk).
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
    
    public void hacerPago() {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoDecimoTerceroAnualController.DataBaseThread(0);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoDecimoTerceroAnualController.DataBaseThread(1, file, enviarCorreo);
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
    
    public void dialogoGenerarDatTxt() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Roles Individuales");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Seleccionar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los pagos de decimo tercero con exito, \n"
                +"Seleccione ahora donde guardar los archivos .TXT y .DAT"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                new GuardarText().saveFile(textosDAT, getFileNameDat(file));
                new GuardarText().saveFile(textosTXT, getFileNameTXT(file));
                dialogoGenerarDatCompletado();
            }
        });
        dialogStage.show(); 
    }
    
    public void dialogoGenerarDatCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Decimo Tercero");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Generacion de archivos Dat y Txt completada."), buttonOk).
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
    
    public void dialogoPagoDecimosCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de decimo tercero");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los pagos de decimo tercero con exito, \n"
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
        dialogStage.setTitle("Pago de decimo Tercero");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Pagos de decimos tercero acumlado hecho con exito."), buttonOk).
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
        
        DateTime fechaActual = new DateTime();
        String anio = String.valueOf(fechaActual.getYear());
        
        yearLabel.setText(anio);
        
        checkBoxPagarTodos.setSelected(false);
        
         new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                loadingMode();
                                new Timer().schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            cancel();
                                            Platform.runLater(new Runnable() {
                                                @Override public void run() {
                                                    setTableInfo(); 
                                                    closeDialogMode();
                                                }
                                            });
                                        }
                                    }, 500, 500);
                            }
                        });
                    }
                }, 500, 500);
    }
    
    public void setTableInfo() {
        
        periodoALiquidar = "1 de Ene "+String.valueOf(Integer.valueOf(yearLabel.getText())-1)
                +" al 31 de Dic "+String.valueOf(Integer.valueOf(yearLabel.getText())-1);
        periodoLabel.setText("Periodo entre el "+periodoALiquidar);

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), 
                new Fecha("01","01",yearLabel.getText())));
        
        PagoDecimoDAO pagoDecimoDAO = new PagoDecimoDAO();
        Timestamp fecha = new Timestamp(Integer.valueOf(yearLabel.getText())-1-1899, 0, 1, 0, 0, 0, 0);
        pagosDecimo = new ArrayList<>();
        pagosDecimo.addAll(pagoDecimoDAO.findByDecimoAndFecha(Const.DECIMO_TERCERO, fecha));
        
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.setUsuario(user);
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
            
            Double monto = 0d;
            
            List<RolIndividual> rolesIndividual = new RolIndividualDAO()
                    .findAllByEmpleadoId(empleado.getId());
            
            ArrayList<RolIndividual> rolesDelParaPagar = new ArrayList<>();
          
            for (RolIndividual rolIndividual: rolesIndividual) {
                
                Fecha inicio = new Fecha(rolIndividual.getInicio());
                Fecha finalizo = new Fecha(rolIndividual.getFinalizo());
                if (inicio.getAno().equals(String.valueOf(Integer.valueOf(yearLabel.getText())-1))) {
                    monto += rolIndividual.getDecimoTercero();
                    rolesDelParaPagar.add(rolIndividual);
                }
            }

            empleado.setRolesInds(rolesDelParaPagar);
        
            empleado.setDecimo3(round(monto));
            empleado.setPagar(false);
            empleado.setAgregar(false);
            empleado.setPagado("No");
            
            for (PagoDecimo pagoDecimo: pagosDecimo) {
                if (pagoDecimo.getUsuario().getId().equals(user.getId())) {
                    empleado.setPagoDecimo(pagoDecimo);
                    empleado.setPagado("Si");
                    empleado.setDecimo3(round(pagoDecimo.getMonto()));
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
        
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("decimo3"));
        
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
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                        checkBoxpagar.setDisable(true);
                    } else {
                        checkBoxpagar.setDisable(false);
                    }
                    checkBoxpagar.setSelected(empleadoTable.getPagar());
                }
                checkBoxpagar.setOnAction(event -> {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                       empleadoTable.setPagar(false);
                       checkBoxpagar.setSelected(false);
                    } else {
                        empleadoTable.setPagar(checkBoxpagar.isSelected());
                    }
                });
                
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
            }
        });
        
        imprimirColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        imprimirColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxImp = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                setGraphic(checkBoxImp);
                if (checkBoxImp != null) {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                        checkBoxImp.setDisable(false);
                    } else {
                        checkBoxImp.setDisable(true);
                    }
                    checkBoxImp.setSelected(empleadoTable.getAgregar());
                }
                
                checkBoxImp.setOnAction(event -> {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                        empleadoTable.setAgregar(checkBoxImp.isSelected());
                    } else {
                        empleadoTable.setAgregar(false);
                        checkBoxImp.setSelected(false);
                    }
                     
                });
            } 
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    mostrarPagoDecimoTercero(rowData);
                }
            });
           return row;
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
        buttonBank.setTooltip(
            new Tooltip("Generar .DAT y .TXT \n(Selecionados en \"Imp.\")")
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
        buttonImprimir.setTooltip(
            new Tooltip("Imprimir \n(Selecionados en \"Imp.\")")
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
    } 
    
    public void mostrarPagoDecimoTercero(EmpleadoTable empleadoTable) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDecimoTerceroDetalles.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Decimo Tercero");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    DecimoTerceroDetallesController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(aplicacionControl);
                    controller.setPrograma(this);
                    controller.setEmpleado(empleadoTable, periodoALiquidar, yearLabel.getText());
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
                                imprimir(file, enviarCorreo, empladosImprimir);
                            } 
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            closeDialogMode();
                        }
                    }
             }, 1000, 1000);
            
        }
        
        public void hacerPago() {
        
            empladosImprimir = new ArrayList<>();
            textosDAT = new ArrayList<>();
            textosTXT = new ArrayList<>();

            for (EmpleadoTable empleadoTable: data) {
                if (empleadoTable.getPagar()) {
                    PagoDecimo pagoDecimo = new PagoDecimo();
                    pagoDecimo.setUsuario(empleadoTable.getUsuario());
                    pagoDecimo.setFecha(new Timestamp(Integer.valueOf(yearLabel.getText())-1-1899, 0, 1, 0, 0, 0, 0));
                    pagoDecimo.setMonto(empleadoTable.getDecimo3());
                    pagoDecimo.setDecimo(Const.DECIMO_TERCERO);
                    new PagoDecimoDAO().save(pagoDecimo);
                    
                    empleadoTable.setPagoDecimo(pagoDecimo);
                    empleadoTable.setPagado("Si");
                    empleadoTable.setPagar(false);
                    
                    empladosImprimir.add(empleadoTable);
                
                    // Registro para auditar
                    String detalles = "hizo el pago de decimo  acumulados nro: " + pagoDecimo.getId() 
                            + " del lapso 1 de Ene "+String.valueOf(Integer.valueOf(yearLabel.getText())-1)
                            + " al 31 de Dic "+String.valueOf(Integer.valueOf(yearLabel.getText())-1) + " para el empleado " 
                            + empleadoTable.getApellido()+ " " + empleadoTable.getNombre();
                    aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    
                    textosDAT.add(crearLineaDAT(empleadoTable.getUsuario(), pagoDecimo));
                    textosTXT.add(crearLineaTXT(empleadoTable.getUsuario(), pagoDecimo));
                }
            }
            HibernateSessionFactory.getSession().close();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    setTableInfo();
                    closeDialogMode();
                    dialogoPagoDecimosCompletado();
                }
            });
        }

        public void imprimir(File file, Boolean enviarCorreo, List<EmpleadoTable> empleados) {
        
            for (EmpleadoTable empleado: empleados) {
                PagoDecimo pagoDecimo = empleado.getPagoDecimo();
                Usuario user = pagoDecimo.getUsuario();

                ReporteRolDecimoTercero datasource = new ReporteRolDecimoTercero();
                datasource.addAll(empleado.getRolesInds());

                try {
                    InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_DECIMO_TERCERO);

                    Map<String, Object> parametros = new HashMap();
                    parametros.put("numero", pagoDecimo.getId().toString()); 
                    parametros.put("fecha_recibo", Fechas.getFechaConMes(Fechas.getFechaActual()));
                    parametros.put("empleado", empleado.getApellido()+ " " + empleado.getNombre());
                    parametros.put("cedula", empleado.getCedula());
                    parametros.put("cargo", empleado.getUsuario().getDetallesEmpleado().getCargo().getNombre());
                    parametros.put("empresa", empresa.getNombre());
                    parametros.put("siglas", empresa.getSiglas());
                    parametros.put("correo", "Correo: " + empresa.getEmail());
                    parametros.put("detalles", 
                                 "Ruc: " + empresa.getNumeracion() 
                            + " - Direccion: " + empresa.getDireccion() 
                            + " - Tel: " + empresa.getTelefono1());
                    parametros.put("devengado", Numeros.round(pagoDecimo.getMonto()).toString());
                    parametros.put("cobrar", Numeros.round(pagoDecimo.getMonto()).toString());
                    parametros.put("lapso", "del 1 de Ene "+String.valueOf(Integer.valueOf(yearLabel.getText())-1)
                                 +" al 31 de Dic "+String.valueOf(Integer.valueOf(yearLabel.getText())-1));

                    JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                    String filename = "pago_decimo_tercero_" + pagoDecimo.getId();

                    if (file != null) {
                        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                    } 
                    if (enviarCorreo) {
                        File pdf = File.createTempFile(filename, ".pdf");
                        JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                        CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                                user.getEmail(), Const.ASUNTO_PAGO_DECIMO_TERCERO, 
                                "Recibo del pago del decimo tercero acumulado desde el " 
                                        + "1 de Ene "+String.valueOf(Integer.valueOf(yearLabel.getText())-1) 
                                        + " hasta el "
                                        + "31 de Dic "+String.valueOf(Integer.valueOf(yearLabel.getText())-1), 
                                pdf.getPath(), filename + ".pdf");
                    }

                } catch (JRException | IOException ex) {
                    Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            empladosImprimir.clear();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoCompletado();
                }
            });
        }
    }
    
    String crearLineaDAT(Usuario user, PagoDecimo pagoDecimo) {
        String monto = Numeros.roundToString(pagoDecimo.getMonto());
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
                            .getEmpresa().getNumeracion()+";0001;"+String.valueOf(Integer.valueOf(yearLabel.getText())-1)
                            +";"+"12"+";INS;"+user.getCedula()
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
    
    String crearLineaTXT(Usuario user, PagoDecimo pagoDecimo) {
        String monto = Numeros.roundToString(pagoDecimo.getMonto());
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
                +espacios+partEntera+partDecimal+"DECIMO TERCERO "
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
    
}