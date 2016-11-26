/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Const;
import aplicacion.control.util.DiasSpinner;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButtonBlue;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlEmpleado;
import hibernate.model.Empresa;
import hibernate.model.Horario;
import hibernate.model.RolCliente;
import hibernate.model.Usuario;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class HorarioEmpleadoController implements Initializable {
    
    private Stage stagePrincipal;
    
    private Usuario empleado;
    
    private ArrayList<Usuario> empleados;
    
    private ArrayList<ControlEmpleado> controls;
    
    private Empresa empresa;
    
    @FXML
    private TextField normales;
    
    @FXML
    private TextField suplementarias;
    
    @FXML
    private TextField sobreTiempo;
    
    @FXML
    private Button horarioButton;
    
    @FXML
    private Button clienteButton;
    
    @FXML 
    private RadioButton marcarTrabajo;
    
    @FXML 
    private RadioButton marcarLibre;
    
    @FXML 
    private RadioButton marcarFalta;
    
    @FXML 
    private RadioButton marcarEspecial;
    
    @FXML 
    private RadioButton marcarPermiso;
    
    @FXML 
    private RadioButton marcarVacaciones;
    
    @FXML 
    private RadioButton marcarCM;
    
    @FXML 
    private RadioButton marcarDM;
    
    @FXML 
    private GridPane panelHoras;
    
    @FXML 
    private GridPane panelEspecial;
    
    @FXML 
    private GridPane panelHorario;
    
    @FXML
    private Label fechaLabel;
    
    @FXML
    private Label diaLabel;
    
    @FXML 
    private Pane panelDias;
    
    private List<Cliente> clientes;
    
    private List<Horario> horarios;
    
    private Cliente cliente;
    
    @FXML
    private Button buttonBorrarHorario;
    
    @FXML
    private Button buttonBorrarCliente;
    
    @FXML
    private Button buttonInformacion;
    
    private Boolean editable;
    
    public Boolean multiple;
    
    DiasSpinner spinnder;
    
    ControlEmpleado controlEmpleadoToReturn;
    
    Stage stage;
    
    Dialog<Void> dialog;
    
    boolean medioDia;
    Time entrada = new Time(6, 0, 0);
    Time salida = new Time(14, 0, 0);
    
    private List<ControlEmpleado> ultimosRegistros;
    private HorasEmpleadosPorDiaController horasEmpleadosPorDiaController;
    private ControlEmpleado controlEmpleado;
    private Timestamp fecha;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setHorasEmpleadosPorDia(HorasEmpleadosPorDiaController
            horasEmpleadosPorDiaController) {
        this.horasEmpleadosPorDiaController = horasEmpleadosPorDiaController;
        this.clientes = horasEmpleadosPorDiaController.clientes;
        this.horarios = horasEmpleadosPorDiaController.horarios;
    }
    
    @FXML
    private void checkTrabajo(ActionEvent event) {
        panelHoras.setVisible(true);
        panelHorario.setVisible(true);
        panelEspecial.setVisible(false);
        verificarDia();
    }
    
    @FXML
    private void checkLibre(ActionEvent event) {
        panelHoras.setVisible(false);
        panelHorario.setVisible(false);
        panelEspecial.setVisible(false);
        medioDia = false;
    }
    
    @FXML
    private void checkFalta(ActionEvent event) {
        panelHoras.setVisible(false);
        panelHorario.setVisible(false);
        panelEspecial.setVisible(false);
        medioDia = false;
        verificarDia();
    }
    
    @FXML
    private void checkEspecial(ActionEvent event) {
        panelHoras.setVisible(false);
        panelHorario.setVisible(false);
        panelEspecial.setVisible(true);
        medioDia = false;
        verificarDia();
    }
    
    @FXML
    private void checkPermiso(ActionEvent event) {
        marcarEspecial.setSelected(true);
        panelHoras.setVisible(false);
        panelHorario.setVisible(false);
        panelEspecial.setVisible(true);
        medioDia = false;
        verificarDia();
    }
    
    @FXML
    private void checkVacaciones(ActionEvent event) {
        marcarEspecial.setSelected(true);
        panelHoras.setVisible(false);
        panelHorario.setVisible(false);
        panelEspecial.setVisible(true);
        medioDia = false;
        verificarDia();
    }
    
    @FXML
    private void checkCM(ActionEvent event) {
        marcarEspecial.setSelected(true);
        panelHoras.setVisible(false);
        panelHorario.setVisible(false);
        panelEspecial.setVisible(true);
        medioDia = false;
        verificarDia();
    }
    
    @FXML
    private void checkDM(ActionEvent event) {
        marcarEspecial.setSelected(true);
        panelHoras.setVisible(false);
        panelHorario.setVisible(false);
        panelEspecial.setVisible(true);
        medioDia = false;
        verificarDia();
    }
    
    private void verificarDia() {
        if (medioDia) {
            diaLabel.setText("medio dia");
        } else {
            diaLabel.setText("");
        }
    }
    
    private void loadingMode(){
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);//stage here is the stage of your webview
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
    
    public void closeDialogMode() {
        if (dialog != null) {
           Stage toClose = (Stage) dialog.getDialogPane()
                   .getScene().getWindow();
           toClose.close();
           dialog.close();
           dialog = null;
        }
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private void guardar(ActionEvent event)  {
        if (horarioButton.getText().isEmpty() && marcarTrabajo.isSelected()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Debes seleccionar un horario."), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                dialogStage.close();
            });
            dialogStage.show();
        } else {
            if (!marcarTrabajo.isSelected()) {
                entrada = new Time(0, 0, 0);
                salida = new Time(0, 0, 0);
            }
            saveHorario();
        }
    }
    
    private void saveHorario()  {
        if (marcarEspecial.isSelected() 
                && !marcarPermiso.isSelected() 
                && !marcarVacaciones.isSelected()
                && !marcarCM.isSelected()
                && !marcarDM.isSelected()) {
            // Nothing to do
        } else {
            if (multiple) {
                ExecutorService executor = Executors.newFixedThreadPool(1);
                Runnable worker = new HorarioEmpleadoController.DataBaseThread();
                executor.execute(worker);
                executor.shutdown();

                loadingMode();
            } else {
                if (editable) {

                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    Runnable worker = new HorarioEmpleadoController.DataBaseThread();
                    executor.execute(worker);
                    executor.shutdown();

                    loadingMode();

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
                    children(new Text("No se puede cambiar porque ya se creo el rol."), 
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
        }
    }
    
    public void buscarRoles(Timestamp fecha, int dias) {
        DateTime tiempo = new DateTime(fecha.getTime());
        if (dias > 1) {
           int comienzoMes = empresa.getComienzoMes();
           tiempo.plusDays(1);
        } 
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Horario guardado con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
    }
    
    @FXML
    private void informacion(ActionEvent event)  {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Información");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("1 dia para que se guarde el horario para la fecha seleccionada unicamente."), 
                new Text("2 dias para que se guarde para la fecha seleccionada y el siguiente dia."),
                new Text("3 dias para que se guarde para la fecha seleccionada y 2 dias siguientes."),
                new Text("asi sucesivamente."),
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
    
    @FXML
    private void borrarHorario(ActionEvent event)  {
        horarioButton.setText("");
        suplementarias.setText("0");
        sobreTiempo.setText("0"); 
    }
    
    @FXML
    private void borrarCliente(ActionEvent event)  {
        this.cliente = null;
        clienteButton.setText("");
    }
    
    @FXML
    private void cambiarHorario(ActionEvent event) throws IOException  {
        FXMLLoader loader = new FXMLLoader(AplicacionControl.class
                .getResource("ventanas/VentanaSeleccionarHorario.fxml"));
        AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
        Stage ventana = new Stage();
        ventana.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/security_dialog.png").toExternalForm();
        ventana.getIcons().add(new Image(stageIcon));
        ventana.setResizable(false);
        ventana.initOwner(stagePrincipal);
        Scene scene = new Scene(ventanaRolDePago);
        ventana.setScene(scene);
        SeleccionarHorarioController controller = loader.getController();
        controller.setStagePrincipal(ventana);
        controller.setHorarioEmpleadoController(this);
        controller.setHorarios(horarios);
        ventana.show();
    }
    
    @FXML
    private void cambiarCliente(ActionEvent event) throws IOException  {
        FXMLLoader loader = new FXMLLoader(AplicacionControl.class
                .getResource("ventanas/VentanaSeleccionarCliente.fxml"));
        AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
        Stage ventana = new Stage();
        ventana.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/security_dialog.png").toExternalForm();
        ventana.getIcons().add(new Image(stageIcon));
        ventana.setResizable(false);
        ventana.initOwner(stagePrincipal);
        Scene scene = new Scene(ventanaRolDePago);
        ventana.setScene(scene);
        SeleccionarClienteController controller = loader.getController();
        controller.setStagePrincipal(ventana);
        controller.setHorarioEmpleadoController(this);
        controller.setClientes(clientes);
        ventana.show();
    }
    
    public void setHorario(Horario horario) {
        horarioButton.setText(getLapso(horario.getEntrada(), horario.getSalida()));
        normales.setText(horario.getNormales().toString());
        suplementarias.setText(horario.getRecargo().toString());
        sobreTiempo.setText(horario.getSobretiempo().toString());
        entrada = horario.getEntrada();
        salida = horario.getSalida();
        medioDia = horario.getMedioDia();
        verificarDia();
    }
    
    public String getLapso(Time entrada, Time salida) {
        
        String lapso = Fechas.getHora(entrada)
                        +" - "+Fechas.getHora(salida);
        
        return lapso;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        clienteButton.setText(cliente.getNombre());
    }
    
    public void setEmpleado(Usuario empleado, ControlEmpleado controlEmpleado, 
            Timestamp fecha, Boolean editable) {
        multiple = false;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        this.empleado = empleado;
        this.controlEmpleado = controlEmpleado;
        this.fecha = fecha;
        this.editable = editable;
        if (controlEmpleado != null) {
            
            entrada = controlEmpleado.getEntrada();
            salida = controlEmpleado.getSalida();
            medioDia = controlEmpleado.getMedioDia();
            
            normales.setText(controlEmpleado.getNormales().toString());
            suplementarias.setText(controlEmpleado.getRecargo().toString());
            sobreTiempo.setText(controlEmpleado.getSobretiempo().toString());
            
            if (controlEmpleado.getCaso().equals(Const.LIBRE)) {
                marcarLibre.setSelected(true);
                checkLibre(null);
            } else if (controlEmpleado.getCaso().equals(Const.FALTA)) {
                marcarFalta.setSelected(true);
                checkFalta(null);
            } else if (controlEmpleado.getCaso().equals(Const.PERMISO)) {
                marcarPermiso.setSelected(true);
                checkPermiso(null);
            } else if (controlEmpleado.getCaso().equals(Const.VACACIONES)) {
                marcarVacaciones.setSelected(true);
                checkVacaciones(null);
            } else if (controlEmpleado.getCaso().equals(Const.CM)) {
                marcarCM.setSelected(true);
                checkCM(null);
            } else if (controlEmpleado.getCaso().equals(Const.DM)) {
                marcarDM.setSelected(true);
                checkDM(null);
            } else {
                horarioButton.setText(getLapso(controlEmpleado.getEntrada(), controlEmpleado.getSalida()));
            }
            
            if (controlEmpleado.getCliente() != null) {
                cliente = controlEmpleado.getCliente();
                clienteButton.setText(cliente.getNombre());
            }
        }
        
        DateTime dateTime = new DateTime(fecha);
        String dia = dateTime.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar
                                    .DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String output = dia.substring(0, 1).toUpperCase() + dia.substring(1);
        fechaLabel.setText(output + " " + Fechas.getFechaConMes(fecha));
        
        verificarDia();
    }
    
    public void setEmpleados(ArrayList<Usuario> empleados, ArrayList<ControlEmpleado> controls, Timestamp fecha) {
        editable = true;
        multiple = true;
        empresa = empleados.get(0).getDetallesEmpleado().getEmpresa();
        this.empleados = new ArrayList<>();
        this.controls = new ArrayList<>();
        this.empleados.addAll(empleados);
        this.controls.addAll(controls);
        this.fecha = fecha;
        DateTime dateTime = new DateTime(fecha);
        String dia = dateTime.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar
                                    .DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String output = dia.substring(0, 1).toUpperCase() + dia.substring(1);
        fechaLabel.setText(output + " " + Fechas.getFechaConMes(fecha));
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        normales.setText("0");
        suplementarias.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        sobreTiempo.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        normales.setEditable(false);
        normales.setDisable(true);
        marcarTrabajo.setSelected(true);
        spinnder = new DiasSpinner();
        spinnder.setNumber(new BigDecimal(1));
        panelDias.getChildren().add(spinnder);
        buttonBorrarHorario.setTooltip(
            new Tooltip("Limpiar")
        );
        buttonBorrarHorario.setOnMouseEntered((MouseEvent t) -> {
            buttonBorrarHorario.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_borrar_mini.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonBorrarHorario.setOnMouseExited((MouseEvent t) -> {
            buttonBorrarHorario.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_borrar_mini.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonBorrarCliente.setTooltip(
            new Tooltip("Limpiar")
        );
        buttonBorrarCliente.setOnMouseEntered((MouseEvent t) -> {
            buttonBorrarCliente.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_borrar_mini.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonBorrarCliente.setOnMouseExited((MouseEvent t) -> {
            buttonBorrarCliente.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_borrar_mini.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonInformacion.setTooltip(
            new Tooltip("Información")
        );
        buttonInformacion.setOnMouseEntered((MouseEvent t) -> {
            buttonInformacion.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_informacion.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonInformacion.setOnMouseExited((MouseEvent t) -> {
            buttonInformacion.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_informacion.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        buttonBorrarHorario.setVisible(false);
    } 
    
    public void updateWindows() {
        closeDialogMode();
        horasEmpleadosPorDiaController.cambiarUsuarioUltimoRegistro(controlEmpleadoToReturn);
        horasEmpleadosPorDiaController.cambiarControlEmpleado(controlEmpleadoToReturn);
        stagePrincipal.close();
        dialogoCompletado();
    }
    
    public void updateWindowsMultiple() {
        closeDialogMode();
        horasEmpleadosPorDiaController.setTableInfo(fecha);
        stagePrincipal.close();
        dialogoCompletado();
    }
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        };
        return aux;
    }
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public static LocalDate getDateFromTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            DateTime dateTime = new DateTime(timestamp.getTime());
            return LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        }
    }
    
    public void hayRolCliente() {
        closeDialogMode();
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("No se puede crear los horarios por que hay un algun rol cliente generado."), 
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
    
    public void hayRolCliente(ArrayList<Usuario> usuarios) {
        closeDialogMode();
        stagePrincipal.close();
        horasEmpleadosPorDiaController.marcarUsuariosConRol(usuarios);
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("No se puede hacer el guardado de horarios,"),
                new Text("Los siguiente empleados marcados en rojo tienen un rol cliente creado ya."), 
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
    
    public void error() {
        closeDialogMode();
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
        children(new Text("Hubo un error en el proceso."), 
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
    
    public class DataBaseThread implements Runnable {

        public DataBaseThread(){
        }

        @Override
        public void run() {
    
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        if (multiple) 
                           saveMultiple();
                        else 
                           saveSingle();
                    }
             }, 1000, 1000);
            
        }
        
        public void saveMultiple() {
            System.out.println("Empezando guardado multiple");
            try {
                if (spinnder.getDias() == 1) {
                    
                    for (Usuario usuario: empleados) {
                        ControlEmpleado controlEmpleadoNew = new ControlEmpleado();
                        controlEmpleadoNew.setFecha(fecha);
                        if (marcarTrabajo.isSelected()) {
                            controlEmpleadoNew.setNormales(Double.valueOf(normales.getText()));
                            controlEmpleadoNew.setSobretiempo(Double.valueOf(sobreTiempo.getText()));
                            controlEmpleadoNew.setRecargo(Double.valueOf(suplementarias.getText()));
                            controlEmpleadoNew.setCaso(Const.TRABAJO);
                        } else if (marcarLibre.isSelected()) {
                            controlEmpleadoNew.setNormales(8d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.LIBRE);
                        } else if (marcarFalta.isSelected()) {
                            controlEmpleadoNew.setNormales(0d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.FALTA);
                        } else if (marcarPermiso.isSelected()) {
                            controlEmpleadoNew.setNormales(0d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.PERMISO);
                        } else if (marcarVacaciones.isSelected()) {
                            controlEmpleadoNew.setNormales(0d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.VACACIONES);
                        } else if (marcarCM.isSelected()) {
                            controlEmpleadoNew.setNormales(8d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.CM);
                        } else if (marcarDM.isSelected()) {
                            controlEmpleadoNew.setNormales(8d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.DM);
                        } 
                        controlEmpleadoNew.setEntrada(entrada);
                        controlEmpleadoNew.setSalida(salida);
                        controlEmpleadoNew.setMedioDia(medioDia);
                        controlEmpleadoNew.setUsuario(usuario);
                        controlEmpleadoNew.setCliente(cliente);
                        System.out.println("guardando control de: " 
                                + controlEmpleadoNew.getUsuario().getApellido());
                        new ControlEmpleadoDAO().save(controlEmpleadoNew);
                        
                        // Registro para auditar
                        String detalles = "registro un horario para el empleado " 
                                + usuario.getApellido() + " " + usuario.getNombre()
                                + " del dia " + Fechas.getFechaConMes(fecha);
                        horasEmpleadosPorDiaController.aplicacionControl.au
                                .saveAgrego(detalles, horasEmpleadosPorDiaController
                                        .aplicacionControl.permisos.getUsuario());
                    }
                    for (ControlEmpleado controlDelete: controls) {
                        System.out.println("Borrando control de: " 
                                + controlDelete.getUsuario().getApellido());
                        new ControlEmpleadoDAO().delete(controlDelete);
                    }
                    HibernateSessionFactory.getSession().flush();
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            updateWindowsMultiple();
                        }
                    });
                } else {
                    System.out.println("Guardado para " + spinnder.getDias() + " dias");
                    Boolean cancelar = false;
                    ArrayList<Usuario> usuariosConRol = new ArrayList<>();
                    for (int i=0; i < (spinnder.getDias() - 1); i++) {
                        Timestamp fechaAConsultar = new Timestamp((new DateTime(fecha
                                .getTime()).plusDays(i+1)).getMillis());
                        RolClienteDAO rolClienteDAO = new RolClienteDAO();
                        
                        List<RolCliente> rolClientes;
                        rolClientes = rolClienteDAO.findAllByEntreFechaAndEmpresaId(fechaAConsultar, empresa.getId());
                        if (!rolClientes.isEmpty()) {
                            for (RolCliente rol: rolClientes) {
                                for (Usuario usuario: empleados) {
                                    if (rol.getUsuario().getId() 
                                            .equals(usuario.getId())) {
                                        usuariosConRol.add(usuario);
                                        cancelar = true;
                                    }
                                }
                            }
                        }
                    }
                    if (cancelar) {
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                hayRolCliente(usuariosConRol);
                            }
                        });
                    } else {
                        ArrayList<ControlEmpleado> controlsEncontrados = new ArrayList<>();
                        DateTime fechaLimite = new DateTime(fecha.getTime());
                        Timestamp fechaFin = new Timestamp(fechaLimite
                                .plusDays((spinnder.getDias() - 1)).getMillis());
                        for (Usuario usuario: empleados) {
                            controlsEncontrados.addAll(new ControlEmpleadoDAO()
                                    .findAllByEmpleadoIdInDeterminateTime(usuario.getId()
                                            ,fecha , fechaFin));
                        }
                        for (int i=0; i<spinnder.getDias(); i++) {
                            for (Usuario usuario: empleados) {
                                ControlEmpleado controlEmpleadoNew = new ControlEmpleado();
                                controlEmpleadoNew.setFecha(new Timestamp((new DateTime(fecha.getTime()).plusDays(i)).getMillis()));
                                if (marcarTrabajo.isSelected()) {
                                    controlEmpleadoNew.setNormales(Double.valueOf(normales.getText()));
                                    controlEmpleadoNew.setSobretiempo(Double.valueOf(sobreTiempo.getText()));
                                    controlEmpleadoNew.setRecargo(Double.valueOf(suplementarias.getText()));
                                    controlEmpleadoNew.setCaso(Const.TRABAJO);
                                    } else if (marcarLibre.isSelected()) {
                                        controlEmpleadoNew.setNormales(8d);
                                        controlEmpleadoNew.setSobretiempo(0d);
                                        controlEmpleadoNew.setRecargo(0d);
                                        controlEmpleadoNew.setCaso(Const.LIBRE);
                                    } else if (marcarFalta.isSelected()) {
                                        controlEmpleadoNew.setNormales(0d);
                                        controlEmpleadoNew.setSobretiempo(0d);
                                        controlEmpleadoNew.setRecargo(0d);
                                        controlEmpleadoNew.setCaso(Const.FALTA);
                                    } else if (marcarPermiso.isSelected()) {
                                        controlEmpleadoNew.setNormales(0d);
                                        controlEmpleadoNew.setSobretiempo(0d);
                                        controlEmpleadoNew.setRecargo(0d);
                                        controlEmpleadoNew.setCaso(Const.PERMISO);
                                    } else if (marcarVacaciones.isSelected()) {
                                        controlEmpleadoNew.setNormales(0d);
                                        controlEmpleadoNew.setSobretiempo(0d);
                                        controlEmpleadoNew.setRecargo(0d);
                                        controlEmpleadoNew.setCaso(Const.VACACIONES);
                                    } else if (marcarCM.isSelected()) {
                                        controlEmpleadoNew.setNormales(8d);
                                        controlEmpleadoNew.setSobretiempo(0d);
                                        controlEmpleadoNew.setRecargo(0d);
                                        controlEmpleadoNew.setCaso(Const.CM);
                                    } else if (marcarDM.isSelected()) {
                                        controlEmpleadoNew.setNormales(8d);
                                        controlEmpleadoNew.setSobretiempo(0d);
                                        controlEmpleadoNew.setRecargo(0d);
                                        controlEmpleadoNew.setCaso(Const.DM);
                                    } 
                                controlEmpleadoNew.setEntrada(entrada);
                                controlEmpleadoNew.setSalida(salida);
                                controlEmpleadoNew.setMedioDia(medioDia);
                                controlEmpleadoNew.setUsuario(usuario);
                                controlEmpleadoNew.setCliente(cliente);
                                new ControlEmpleadoDAO().save(controlEmpleadoNew);
                                
                                // Registro para auditar
                                String detalles = "registro horarios para el empleado " 
                                        + usuario.getApellido() + " " + usuario.getNombre()
                                        + " desde el dia " + Fechas.getFechaConMes(fecha)
                                        + " hasta el dia " + Fechas.getFechaConMes(fechaFin);
                                horasEmpleadosPorDiaController.aplicacionControl.au
                                        .saveAgrego(detalles, horasEmpleadosPorDiaController
                                                .aplicacionControl.permisos.getUsuario());
                            }
                        }
                        for (ControlEmpleado controlEmpleadoDelete: controlsEncontrados) {
                            new ControlEmpleadoDAO().delete(controlEmpleadoDelete);
                        }
                        HibernateSessionFactory.getSession().flush();
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                updateWindowsMultiple();
                            }
                        });
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        error();
                    }
                });
            }
        }
        
        public void saveSingle() {
            System.out.println("Empezando guardado simple");
            try {
                if (spinnder.getDias() == 1) {
                    ControlEmpleado controlEmpleadoNew = new ControlEmpleado();
                    controlEmpleadoNew.setFecha(fecha);
                    if (marcarTrabajo.isSelected()) {
                        controlEmpleadoNew.setNormales(Double.valueOf(normales.getText()));
                        controlEmpleadoNew.setSobretiempo(Double.valueOf(sobreTiempo.getText()));
                        controlEmpleadoNew.setRecargo(Double.valueOf(suplementarias.getText()));
                        controlEmpleadoNew.setCaso(Const.TRABAJO);
                        } else if (marcarLibre.isSelected()) {
                            controlEmpleadoNew.setNormales(8d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.LIBRE);
                        } else if (marcarFalta.isSelected()) {
                            controlEmpleadoNew.setNormales(0d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.FALTA);
                        } else if (marcarPermiso.isSelected()) {
                            controlEmpleadoNew.setNormales(0d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.PERMISO);
                        } else if (marcarVacaciones.isSelected()) {
                            controlEmpleadoNew.setNormales(8d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.VACACIONES);
                        } else if (marcarCM.isSelected()) {
                            controlEmpleadoNew.setNormales(8d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.CM);
                        } else if (marcarDM.isSelected()) {
                            controlEmpleadoNew.setNormales(8d);
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.DM);
                        } 
                    controlEmpleadoNew.setEntrada(entrada);
                    controlEmpleadoNew.setSalida(salida);
                    controlEmpleadoNew.setMedioDia(medioDia);
                    controlEmpleadoNew.setUsuario(empleado);
                    controlEmpleadoNew.setCliente(cliente);
                    new ControlEmpleadoDAO().save(controlEmpleadoNew);
                    if (controlEmpleado != null && controlEmpleado.getFecha()
                            .equals(controlEmpleadoNew.getFecha())) {
                        new ControlEmpleadoDAO().delete(controlEmpleado);
                    }
                    HibernateSessionFactory.getSession().flush();
                    controlEmpleadoToReturn = controlEmpleadoNew;
                    
                    // Registro para auditar
                    String detalles = "registro un horario para el empleado " 
                            + empleado.getApellido() + " " + empleado.getNombre()
                            + " del dia " + Fechas.getFechaConMes(fecha);
                    horasEmpleadosPorDiaController.aplicacionControl.au
                            .saveAgrego(detalles, horasEmpleadosPorDiaController
                                    .aplicacionControl.permisos.getUsuario());
                    
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            updateWindows();
                        }
                    });
                } else {
                    Boolean cancelar = false;
                    for (int i=0; i < (spinnder.getDias() - 1); i++) {
                        Timestamp fechaAConsultar = new Timestamp((new DateTime(fecha
                                .getTime()).plusDays(i+1)).getMillis());
                        RolClienteDAO rolClienteDAO = new RolClienteDAO();
                        
                        List<RolCliente> rolClientes = rolClienteDAO
                            .findAllByEntreFechaAndEmpresaIdAndEmpleadoId(fechaAConsultar, 
                                    empresa.getId(), empleado.getId());
                        if (!rolClientes.isEmpty()) {
                            cancelar = true;
                        }
                    }
                    
                    if (cancelar) {
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                hayRolCliente();
                            }
                        });
                    } else {
                        controlEmpleadoToReturn = new ControlEmpleado();
                        DateTime fechaLimite = new DateTime(fecha.getTime());
                        Timestamp fechaFin = new Timestamp(fechaLimite
                                .plusDays((spinnder.getDias() - 1)).getMillis());
                        ArrayList<ControlEmpleado> controlsEncontrados = new ArrayList<>();
                        controlsEncontrados.addAll(new ControlEmpleadoDAO()
                                .findAllByEmpleadoIdInDeterminateTime(empleado.getId()
                                        ,fecha , fechaFin));
                        for (int i=0; i<spinnder.getDias(); i++) {
                            ControlEmpleado controlEmpleadoNew = new ControlEmpleado();
                            controlEmpleadoNew.setFecha(new Timestamp((new DateTime(fecha.getTime()).plusDays(i)).getMillis()));
                            if (marcarTrabajo.isSelected()) {
                                controlEmpleadoNew.setNormales(Double.valueOf(normales.getText()));
                                controlEmpleadoNew.setSobretiempo(Double.valueOf(sobreTiempo.getText()));
                                controlEmpleadoNew.setRecargo(Double.valueOf(suplementarias.getText()));
                                controlEmpleadoNew.setCaso(Const.TRABAJO);
                            } else if (marcarLibre.isSelected()) {
                                controlEmpleadoNew.setNormales(8d);
                                controlEmpleadoNew.setSobretiempo(0d);
                                controlEmpleadoNew.setRecargo(0d);
                                controlEmpleadoNew.setCaso(Const.LIBRE);
                            } else if (marcarFalta.isSelected()) {
                                controlEmpleadoNew.setNormales(0d);
                                controlEmpleadoNew.setSobretiempo(0d);
                                controlEmpleadoNew.setRecargo(0d);
                                controlEmpleadoNew.setCaso(Const.FALTA);
                            } else if (marcarPermiso.isSelected()) {
                                controlEmpleadoNew.setNormales(0d);
                                controlEmpleadoNew.setSobretiempo(0d);
                                controlEmpleadoNew.setRecargo(0d);
                                controlEmpleadoNew.setCaso(Const.PERMISO);
                            } else if (marcarVacaciones.isSelected()) {
                                controlEmpleadoNew.setNormales(0d);
                                controlEmpleadoNew.setSobretiempo(0d);
                                controlEmpleadoNew.setRecargo(0d);
                                controlEmpleadoNew.setCaso(Const.VACACIONES);
                            } else if (marcarCM.isSelected()) {
                                controlEmpleadoNew.setNormales(8d);
                                controlEmpleadoNew.setSobretiempo(0d);
                                controlEmpleadoNew.setRecargo(0d);
                                controlEmpleadoNew.setCaso(Const.CM);
                            } else if (marcarDM.isSelected()) {
                                controlEmpleadoNew.setNormales(8d);
                                controlEmpleadoNew.setSobretiempo(0d);
                                controlEmpleadoNew.setRecargo(0d);
                                controlEmpleadoNew.setCaso(Const.DM);
                            } 
                            controlEmpleadoNew.setEntrada(entrada);
                            controlEmpleadoNew.setSalida(salida);
                            controlEmpleadoNew.setMedioDia(medioDia);
                            controlEmpleadoNew.setUsuario(empleado);
                            controlEmpleadoNew.setCliente(cliente);
                            new ControlEmpleadoDAO().save(controlEmpleadoNew);
                            if (i == 0) {
                                controlEmpleadoToReturn = controlEmpleadoNew;
                            }
                        }
                        for (ControlEmpleado controlEmpleadoDelete: controlsEncontrados) {
                            new ControlEmpleadoDAO().delete(controlEmpleadoDelete);
                        }

                        HibernateSessionFactory.getSession().flush();
                        
                        // Registro para auditar
                        String detalles = "registro horarios para el empleado " 
                                + empleado.getApellido() + " " + empleado.getNombre()
                                + " desde el dia " + Fechas.getFechaConMes(fecha)
                                + " hasta el dia " + Fechas.getFechaConMes(fechaFin);
                        horasEmpleadosPorDiaController.aplicacionControl.au
                                .saveAgrego(detalles, horasEmpleadosPorDiaController
                                        .aplicacionControl.permisos.getUsuario());

                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                updateWindows();
                            }
                        });
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        error();
                    }
                });
            }
        }
    }
}
