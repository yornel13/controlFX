/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolDePagoDecimo;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fechas;
import hibernate.HibernateSessionFactory;
import hibernate.dao.PagoDecimoDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.PagoDecimo;
import hibernate.model.RolIndividual;
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
public class PagoDecimoCuartoController implements Initializable {
    
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
    private TableColumn montoColumna;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonPagar;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<PagoDecimo> pagosDecimo;
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Stage dialogLoading;
    
    Dialog<Void> dialog;
    
    Label loader;
    
    ArrayList<List<RolIndividual>> rolesEmpleados;
    ArrayList<List<RolIndividual>> rolesParaImprimir;
    
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
        setTableInfo();
    }
    
    @FXML
    private void pagarATodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxPagarTodos.isSelected()) {
                if (empleadoTable.getDecimo4().equals(0d)) {
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
        dialogStage.setTitle("Pagar decimo cuarto acumulado");
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
        children(new Text("¿Seguro que desea pagar el decimo cuarto acumulado "
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
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PagoDecimoCuartoController.DataBaseThread(1, file, enviarCorreo);
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
        Runnable worker = new PagoDecimoCuartoController.DataBaseThread(0);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    public void dialogoPagoDecimosCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de decimo cuarto");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los pagos de decimo cuarto con exito, \n"
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
        dialogStage.setTitle("Pago de decimo Cuarto");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Pagos de decimos cuarto acumulado hecho con exito."), buttonOk).
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
        
        checkBoxPagarTodos.setSelected(false);
        
        setTableInfo();
    }
    
    public void setTableInfo() {
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivo(empresa.getId()));
        
        rolesEmpleados = new ArrayList<>();
        
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
            
            Double monto = 0d;
            
            List<RolIndividual> rolesIndividual = new RolIndividualDAO()
                    .findAllByEmpleadoId(empleado.getId());
            
            List<RolIndividual> rolesDelParaPagar = new ArrayList<>();
          
            for (RolIndividual rolIndividual: rolesIndividual) {
                if (!rolIndividual.getDecimosPagado() 
                        && rolIndividual.getPagoDecimoCuarto() == null) {
                    monto += rolIndividual.getDecimoCuarto();
                    rolesDelParaPagar.add(rolIndividual);
                }
            }

            rolesEmpleados.add(rolesDelParaPagar);
            
            empleado.setDecimo4(monto);
            empleado.setPagar(false);
            
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
        
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("decimo4"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
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
                    return;
                }
                
                setGraphic(checkBoxpagar);
                if (checkBoxpagar != null) {
                    checkBoxpagar.setSelected(empleadoTable.getPagar());
                }
                checkBoxpagar.setOnAction(event -> {
                    if (empleadoTable.getDecimo4().equals(0d)) {
                       empleadoTable.setPagar(false);
                       checkBoxpagar.setSelected(false);
                    } else {
                        empleadoTable.setPagar(checkBoxpagar.isSelected());
                    }
                });
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
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            closeDialogMode();
                        }
                    }
             }, 1000, 1000);
            
        }
        
        public void hacerPago() {

            pagosDecimo = new ArrayList<>();
            rolesParaImprimir = new ArrayList<>();

            for (Usuario user: usuarios) {
                if (data.get(usuarios.indexOf(user)).getPagar()) {
                    PagoDecimo pagoDecimo = new PagoDecimo();
                    pagoDecimo.setUsuario(user);
                    pagoDecimo.setFecha(new Timestamp(new Date().getTime()));
                    pagoDecimo.setMonto(data.get(usuarios.indexOf(user))
                            .getDecimo4());
                    pagoDecimo.setDecimo(Const.DECIMO_CUARTO);
                    new PagoDecimoDAO().save(pagoDecimo);
                    pagosDecimo.add(pagoDecimo);

                    List<RolIndividual> roles = rolesEmpleados
                            .get(usuarios.indexOf(user));

                    for (RolIndividual rolIndividual: roles) {
                        RolIndividual rolSearch = new RolIndividualDAO().findById(rolIndividual.getId());
                        rolSearch.setPagoDecimoCuarto(pagoDecimo);
                    }
                    rolesParaImprimir.add(roles);
                    HibernateSessionFactory.getSession().flush();

                    // Registro para auditar
                    String detalles = "hizo el pago de decimos acumulados nro: " 
                            + pagoDecimo.getId() 
                            + " del lapso " + getFechaConMes(roles.get(0).getInicio())+ " a " 
                            + getFechaConMes(roles.get(roles.size() - 1).getFinalizo()) + " para el empleado " 
                            + user.getApellido()+ " " + user.getNombre();
                    aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                }
            }
            setTableInfo();

            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoPagoDecimosCompletado();
                }
            });
        }
        
        public void imprimir(File file, Boolean enviarCorreo) {
        
            for (PagoDecimo pagoDecimo: pagosDecimo) {
                Usuario user = pagoDecimo.getUsuario();

                List<RolIndividual> roles = rolesParaImprimir
                            .get(pagosDecimo.indexOf(pagoDecimo));

                ReporteRolDePagoDecimo datasource = new ReporteRolDePagoDecimo();
                datasource.add(pagoDecimo);

                try {
                    InputStream inputStream = new FileInputStream(Const.REPORTE_PAGO_DECIMO_CUARTO);

                    Map<String, String> parametros = new HashMap();
                    parametros.put("empleado", user.getApellido()+ " " + user.getNombre());
                    parametros.put("cedula", user.getCedula());
                    parametros.put("cargo", user.getDetallesEmpleado().getCargo().getNombre());
                    parametros.put("empresa", user.getDetallesEmpleado().getEmpresa().getNombre());
                    parametros.put("numero", pagoDecimo.getId().toString()); 
                    parametros.put("lapso", getFechaConMes(roles.get(0).getInicio()) 
                            + " al " + getFechaConMes(roles.get(roles.size() - 1).getFinalizo()));
                    parametros.put("total", round(pagoDecimo.getMonto()).toString());
                    parametros.put("fecha_recibo", Fechas.getFechaConMes(pagoDecimo.getFecha()));

                    JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                    String filename = "pago_decimo_cuarto_" + pagoDecimo.getId();

                    if (file != null) {
                        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                    } 
                    if (enviarCorreo) {
                        File pdf = File.createTempFile(filename, ".pdf");
                        JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                        CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                                user.getEmail(), Const.ASUNTO_PAGO_DECIMO_CUARTO, 
                                "Recibo del pago del decimo cuarto acumulado desde el " 
                                        + getFechaConMes(roles.get(0).getInicio()) 
                                        + " hasta el "
                                        + getFechaConMes(roles.get(roles.size() - 1).getFinalizo()), 
                                pdf.getPath(), filename + ".pdf");
                    }

                } catch (JRException | IOException ex) {
                    Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            pagosDecimo.clear();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                    dialogoCompletado();
                }
            });
        }
        
    }
    
}
