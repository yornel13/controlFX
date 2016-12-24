/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.ControlTable;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
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
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class HorarioEmpleadoClienteController implements Initializable {
    
    private Stage stagePrincipal;
    
    private Usuario empleado;
    
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
    
    private List<Cliente> clientes;
    
    private List<Horario> horarios;
    
    private Cliente cliente;
    
    @FXML
    private Button buttonBorrarHorario;
    
    @FXML
    private Button buttonBorrarCliente;
    
    private Boolean editable;
    
    public Boolean multiple;
    
    ControlEmpleado controlEmpleadoToReturn;
    
    Stage stage;
    
    Dialog<Void> dialog;
    
    boolean medioDia;
    Time entrada;
    Time salida;
    
    private ControlEmpleado controlEmpleado;
    private Timestamp fecha;
    private RolDePagoClienteController rolDePagoClienteController;
    private ArrayList<ControlTable> controlsTables;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private AsignarHorariosController asignarHorariosController;
    private String dia;
    private EmpleadoTable empleadoTable;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setRolDePagoCliente(RolDePagoClienteController
            rolDePagoClienteController) {
        this.rolDePagoClienteController = rolDePagoClienteController;
        this.clientes = rolDePagoClienteController.clientes;
        this.horarios = rolDePagoClienteController.horarios;
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
    
    public String getLapso(Time entrada, Time salida) {
        
        String lapso = Fechas.getHora(entrada)
                        +" - "+Fechas.getHora(salida);
        
        return lapso;
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
    
    @FXML
    private void saveHorario()  {
        if (marcarEspecial.isSelected() 
                && !marcarPermiso.isSelected() 
                && !marcarVacaciones.isSelected()
                && !marcarCM.isSelected()
                && !marcarDM.isSelected()) {
            // Nothing to do
        } else {
            if (asignarHorariosController == null) {
            
                if (multiple) {
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    Runnable worker = new HorarioEmpleadoClienteController.DataBaseThread();
                    executor.execute(worker);
                    executor.shutdown();

                    loadingMode();
                } else {
                    if (editable) {

                        ExecutorService executor = Executors.newFixedThreadPool(1);
                        Runnable worker = new HorarioEmpleadoClienteController.DataBaseThread();
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
            } else {
                ControlEmpleado controlEmpleadoNew = new ControlEmpleado();
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
                controlEmpleadoNew.setCliente(cliente);
                stage.close();
                asignarHorariosController.setHorarioToTurnos(controlEmpleadoNew);
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
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null)
            clienteButton.setText(cliente.getNombre());    
    }
    
    public void setEmpleado(Usuario empleado, ControlEmpleado controlEmpleado, 
            Timestamp fecha, Boolean editable, Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin; ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        multiple = false;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        this.empleado = empleado;
        this.controlEmpleado = controlEmpleado;
        this.fecha = fecha;
        this.editable = editable;
        
        setControlInfo();
        
        DateTime dateTime = new DateTime(fecha);
        String dia = dateTime.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar
                                    .DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String output = dia.substring(0, 1).toUpperCase() + dia.substring(1);
        fechaLabel.setText(output + " " + Fechas.getFechaConMes(fecha));
        
        verificarDia();
    }
    
    void setControlInfo() {
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
    }
    
    public void setEmpleadoMultiplesDias(Usuario empleado, ArrayList<ControlTable> controls, Timestamp fechaInicio, Timestamp fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        editable = true;
        multiple = true;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        this.empleado = empleado;
        this.controlEmpleado = controlEmpleado;     ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.controlsTables = controls;
        this.fecha = fecha;
        fechaLabel.setText("Multiples Dias");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        normales.setText("0");
        suplementarias.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        sobreTiempo.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        normales.setEditable(false);
        normales.setDisable(true);
        marcarTrabajo.setSelected(true);
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
        
        buttonBorrarHorario.setVisible(false);
    } 
    
    public void updateWindows() {
        closeDialogMode();
        rolDePagoClienteController.cambiarControlEmpleado(controlEmpleadoToReturn);
        stagePrincipal.close();
        dialogoCompletado();
    }
    
    public void updateWindowsMultiple() {
        closeDialogMode();
        try {
            rolDePagoClienteController.setEmpleado(empleado, rolDePagoClienteController.cliente, fechaInicio, fechaFin);
        } catch (Exception ex) {
            Logger.getLogger(HorarioEmpleadoClienteController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
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
        children(new Text("No se puede crear los horarios por que hay un rol cliente generado."), 
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
        //horasEmpleadosPorDiaController.marcarUsuariosConRol(usuarios);
        
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

    void setEmpleado(EmpleadoTable empleado, List<Horario> horarios, 
            List<Cliente> clientes, String dia, ControlEmpleado controlEmpleado) {
        this.dia = dia;
        this.empleado = empleado.getUsuario();
        this.empleadoTable = empleado;
        this.clientes = clientes;
        this.horarios = horarios;
        fechaLabel.setText("");
        this.controlEmpleado = controlEmpleado;
        setControlInfo();
        verificarDia();
    }
    
    void setEmpleado(List<Horario> horarios, 
            List<Cliente> clientes, ControlEmpleado controlEmpleado) {
        this.clientes = clientes;
        this.horarios = horarios;
        fechaLabel.setText("");
        this.controlEmpleado = controlEmpleado;
        setControlInfo();
        verificarDia();
    }

    void setAsignarHorarioController(AsignarHorariosController asignarHorariosController) {
        this.asignarHorariosController = asignarHorariosController;
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
            try {
                Boolean cancelar = false;
                List<RolCliente> rolClientes = new RolClienteDAO()
                        .findAllByFechaAndEmpleadoId(fechaInicio, empleado.getId());
                for (ControlTable controlTable: controlsTables) {
                    ControlEmpleado controlEmpleado = controlTable.getControlEmpleado();
                    if (controlEmpleado != null) {
                        for (RolCliente rolCliente: rolClientes) {
                            if (rolCliente.getCliente() != null && controlEmpleado.getCliente() != null) {
                                if (controlEmpleado.getCliente().getId().equals(rolCliente.getCliente().getId())) {
                                    cancelar = true;  
                                }
                            } else if (rolCliente.getCliente() == null && controlEmpleado.getCliente() == null) {
                                cancelar = true;
                            }
                        }
                    }
                    
                }
                
                for (RolCliente rolCliente: rolClientes) {
                    if (rolCliente.getCliente() != null && cliente != null) {
                        if (cliente.getId().equals(rolCliente.getCliente().getId())) {
                            cancelar = true;  
                        }
                    } else if (rolCliente.getCliente() == null && cliente == null) {
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
                    for (ControlTable controlTable: controlsTables) {
                        ControlEmpleado controlEmpleadoToDelete = controlTable.getControlEmpleado();
                        Timestamp fechaParaGuardar = new Timestamp(controlTable
                                .getFecha().getMillis());
                        ControlEmpleado controlEmpleadoNew = new ControlEmpleado();
                        controlEmpleadoNew.setFecha(new Date(fechaParaGuardar.getTime()));
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
                        if (controlEmpleadoToDelete != null) {
                            new ControlEmpleadoDAO().delete(controlEmpleadoToDelete);
                        }
                        HibernateSessionFactory.getSession().flush();
                    }
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            updateWindowsMultiple();
                        }
                    });
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
            try {
                Boolean cancelar = false;
                List<RolCliente> rolesCliente;
                rolesCliente = new RolClienteDAO().findAllByFechaAndEmpleadoId(fechaInicio, empleado.getId());
                System.out.println("roles " + rolesCliente.size());
                for (RolCliente rolCliente: rolesCliente) {
                    if (rolCliente.getCliente() == null && cliente == null) {
                        System.out.println("mismo cliente null");
                        cancelar = true;
                    } else if(rolCliente.getCliente() == null && cliente != null) {
                        // nothing to do
                    } else if(cliente == null && rolCliente.getCliente() != null) {
                        // nothing to do
                    } else if (rolCliente.getCliente().getId().equals(cliente.getId())) {
                        System.out.println("mismo id cliente");
                        cancelar = true;
                    } 
                    
                    if (controlEmpleado != null) {
                        if (controlEmpleado.getFecha().equals(fecha)) {
                            
                            if (controlEmpleado.getCliente() == null) {
                                if (rolCliente.getCliente() == null) {
                                    cancelar = true;
                                } 
                            } else {
                                if (rolCliente.getCliente() != null) {
                                    if (rolCliente.getCliente().getId()
                                            .equals(controlEmpleado.getId())) {
                                        cancelar = true;
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (cancelar) {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            hayRolCliente();
                        }
                    });
                } else {
                    ControlEmpleado controlEmpleadoNew = new ControlEmpleado();
                    controlEmpleadoNew.setFecha(new Date(fecha.getTime()));
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
                    rolDePagoClienteController.aplicacionControl.au
                            .saveAgrego(detalles, rolDePagoClienteController
                                    .aplicacionControl.permisos.getUsuario());

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            updateWindows();
                        }
                    });
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