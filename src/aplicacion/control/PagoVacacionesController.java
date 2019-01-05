/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.DateUtil;
import aplicacion.control.util.DialogUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.GuardarText;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Numeros;
import aplicacion.control.util.Permisos;
import hibernate.dao.PagoMesDAO;
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
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import hibernate.dao.DiasVacacionesDAO;
import hibernate.dao.PagoMesItemDAO;
import hibernate.dao.PagoVacacionesDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.DiasVacaciones;
import hibernate.model.PagoMes;
import hibernate.model.PagoVacaciones;
import hibernate.model.RolIndividual;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
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
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class PagoVacacionesController implements Initializable {
    
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
    private TableColumn cargoColumna;
    
    @FXML 
    private TableColumn diasColumna;
    
    @FXML 
    private TableColumn montoColumna;
    
    @FXML 
    private TableColumn periodoColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> marcarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonBank;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private Label yearLabel;
    
    @FXML
    private Label periodoLabel;
    
    private ObservableList<EmpleadoTable> data;
    
    @FXML
    private CheckBox checkBoxMarcarTodos;
    
    ArrayList<PagoQuincena> pagosQuincenal;
    private ArrayList<String> textosDAT;
    private ArrayList<String> textosTXT;
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    ArrayList<DiasVacaciones> diasVac;
    Dialog<Void> dialog;
    String periodoALiquidar;
    
    
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
        yearLabel.setText(String.valueOf(Integer.valueOf(yearLabel.getText())+1));
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        yearLabel.setText(String.valueOf(Integer.valueOf(yearLabel.getText())-1));
        setTableInfo();
    }
    
    @FXML
    public void generarBank(ActionEvent event) {
        
        textosDAT = new ArrayList<>();
        textosTXT = new ArrayList<>();
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) data) {
            if (empleadoTable.getPagado().equalsIgnoreCase("Si") && empleadoTable.getAgregar()) {
                    textosDAT.add(crearLineaDAT(empleadoTable.getUsuario(), Double.valueOf(empleadoTable.getVacaciones())));
                    textosTXT.add(crearLineaTXT(empleadoTable.getUsuario(), Double.valueOf(empleadoTable.getVacaciones())));
            }
        }
        if (textosDAT.size() > 0 && textosTXT.size() > 0) {
            dialogoGenerarDatTxt();
        } else {
            dialogoErrorBizBank();
        }
    }
    
    @FXML
    private void marcarTodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxMarcarTodos.isSelected()) {
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
    
    public void dialogoGenerarDatTxt() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago Vacaciones");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Seleccionar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Seleccione donde guardar los archivos .TXT y .DAT"), 
                buttonSiDocumento).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                new GuardarText().saveFile(textosDAT, getFileNameDat(file));
                new GuardarText().saveFile(textosTXT, getFileNameTXT(file));
                dialogoCompletado();
            }
        });
        dialogStage.show();
        
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago Quincenal");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Completado."), buttonOk).
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
    
    public void empleadoEditado(EmpleadoTable empleado) {
        for (EmpleadoTable empleadoTable: data) {
            if(Objects.equals(empleadoTable.getId(), empleado.getId())) {
               data.set(data.indexOf(empleadoTable), empleado);
               return;
            }
        }
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        DateTime fechaActual = new DateTime();
        String anio = String.valueOf(fechaActual.getYear());
        
        yearLabel.setText(anio);
        
        
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
        periodoALiquidar = "el 1 de Ene "+String.valueOf(Integer.valueOf(yearLabel.getText())-1)
                +" al 31 de Dic "+String.valueOf(Integer.valueOf(yearLabel.getText())-1);
        periodoLabel.setText("Periodo entre "+periodoALiquidar);
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), 
                new Fecha("01","01",yearLabel.getText())));
        DiasVacacionesDAO diasDAO = new DiasVacacionesDAO();
        diasVac = new ArrayList<>();
        diasVac.addAll((ArrayList<DiasVacaciones>) diasDAO.findAllByEmpresaId(empresa.getId()));
        
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
            empleado.setPagado("No");
            empleado.setPagar(true);
            
            Timestamp timestamp = user.getDetallesEmpleado().getFechaInicio();
            DateTime contratoDate = new DateTime(timestamp.getTime());
            
            ////////////////////////////////////////////////////////////////////
            Fecha inicio = new Fecha("01", "01", yearLabel.getText())
                    .minusYears(1)
                    //.minusMonths(1)
                    //.plusMonths(contratoDate.getMonthOfYear());   De aqui toma el mes cuando inicio el empleado a trabajar
                    ; 
            Fecha fin = inicio.plusYears(1).minusMonths(1);
            ////////////////////////////////////////////////////////////////////
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.valueOf(yearLabel.getText())-1, contratoDate.getMonthOfYear()-1, 01);
            Date inicioDate = new Date(cal.getTime().getTime());
            Date finDate = DateUtil.addYears(inicioDate, 1);
            finDate = DateUtil.removeMonths(finDate, 1);
            Calendar calendar = Calendar.getInstance();  
            calendar.setTime(finDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            finDate = new Date(calendar.getTime().getTime());
            empleado.setSqlDateInicio(inicioDate);
            empleado.setSqlDateFin(finDate);
            empleado.setFechaInicio(inicio);
            empleado.setFechaFin(fin);
            ////////////////////////////////////////////////////////////////////
            fin.setDia("30");
            List<RolIndividual> rolIndividuals = new RolIndividualDAO()
                    .findAllByRangoFechaAndEmpleadoId(inicio.getFecha(), fin.getFecha(), empleado.getId());
            List<PagoMes> pagosMensuales = new PagoMesDAO()
                    .findAllByRangoFechaAndEmpleadoId(inicio.getFecha(), fin.getFecha(), empleado.getId());
            
            PagoVacaciones pagoVacaciones = new PagoVacacionesDAO()
                    .findInDeterminateYearByUsuarioId(inicio.getAno(), empleado.getId());
            
            if (rolIndividuals != null) {
                empleado.setRolesInds(new ArrayList<>(rolIndividuals));
                for (PagoMes pagoMes: pagosMensuales) {
                    pagoMes.setPagosItems(new PagoMesItemDAO().findByPagoMesId(pagoMes.getId()));
                }
                empleado.setPagosMensuales(new ArrayList<>(pagosMensuales));
            } 
            for (DiasVacaciones vacaciones: diasVac) {
                if (vacaciones.getUsuario().getId().equals(user.getId())) {
                    empleado.setObjectVacaciones(vacaciones);
                }
            }
            
            if (pagoVacaciones != null) {
                empleado.setVacaciones(Numeros.round(pagoVacaciones.getMonto()).toString());
                empleado.setDiasVacaciones(pagoVacaciones.getDias().toString());
                empleado.setPagado("Si");
                empleado.setPagoVacaciones(pagoVacaciones);
                empleado.setPeriodo(Fechas.getFechaCorta(pagoVacaciones.getGoceInicio())
                        +" al "+
                        Fechas.getFechaCorta(pagoVacaciones.getGoceFin()));
            }
            
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);
        
        filtro();
        closeDialogMode();
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
           //         borrarPago(usuarioId);
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
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        diasColumna.setCellValueFactory(new PropertyValueFactory<>("diasVacaciones"));
        
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("vacaciones"));
        
        periodoColumna.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            
            private final CheckBox checkBox = new CheckBox();
            
            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                setGraphic(checkBox);
                if (checkBox != null) {
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                        checkBox.setDisable(false);
                    } else {
                        checkBox.setDisable(true);
                    }
                    checkBox.setSelected(empleadoTable.getAgregar());
                }
                checkBox.setOnAction(event -> {
                     empleadoTable.setAgregar(checkBox.isSelected());
                });
                
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
            }
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    mostrarPagoVacaciones(rowData);
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
    } 
    
    public void mostrarPagoVacaciones(EmpleadoTable empleadoTable) {
        
        if (empleadoTable.getObjectVacaciones() == null) {
            DialogUtil.error("Error", "El empleado seleccionado "
                    + "no tiene configurado dias de vacaciones.");
            return;
        } 
        
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaVacacionesDetalles.fxml"));
                    AnchorPane ventanaAuditar = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Vacaciones");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaAuditar);
                    ventana.setScene(scene);
                    VacacionesDetallesController controller = loader.getController();
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
    
    String crearLineaDAT(Usuario user, Double cobrado) {
        String monto = Numeros.roundToString(cobrado);
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
                            .getEmpresa().getNumeracion()+";0001;"+Fechas.getFechaActual().getAno()
                            +";"+Fechas.getFechaActual().getMes()+";INS;"+user.getCedula()
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
    
    String crearLineaTXT(Usuario user, Double cobrado) {
        String monto = Numeros.roundToString(cobrado);
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
                +espacios+partEntera+partDecimal+"ROL VACACIONES "
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
