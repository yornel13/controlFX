/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.ControlTable;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButtonBlue;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ControlDiarioDAO;
import hibernate.dao.ControlExtrasDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlDiario;
import hibernate.model.ControlExtras;
import hibernate.model.Empresa;
import hibernate.model.Horario;
import hibernate.model.RolCliente;
import hibernate.model.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
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
    
    @FXML
    private Label sobrecargoLabel;
    
    @FXML
    private Label sobretiempoLabel;
    
    @FXML
    private Label recargoLabel;
    
    @FXML
    private Label normalesLabel;
    
    private Boolean editable;
    
    public Boolean multiple;
    
    ControlDiario controlEmpleadoToReturn;
    
    ControlExtras controlExtraToReturn;
    
    Stage stage;
    
    Dialog<Void> dialog;
    
    boolean medioDia;
    Time entrada;
    Time salida;
    
    private ControlDiario controlDiario;
    private Fecha fecha;
    private DateTime fechaExtras;
    private RolDePagoClienteController rolDePagoClienteController;
    private ArrayList<ControlTable> controlsTables;
    private Fecha fechaInicio;
    private Fecha fechaFin;
    private AsignarHorariosController asignarHorariosController;
    private String dia;
    private EmpleadoTable empleadoTable;
    private ControlExtras controlExtras;
    private AsignarHorasExtrasController asignarHorasExtrasController;
    private boolean changeExtras;
    
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
            if (asignarHorariosController == null && asignarHorasExtrasController == null) {
            
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
                ControlDiario controlDiarioNew = new ControlDiario();
                if (marcarTrabajo.isSelected()) {
                    controlDiarioNew.setNormales(Double.valueOf(normales.getText()));
                    controlDiarioNew.setSobretiempo(Double.valueOf(sobreTiempo.getText()));
                    controlDiarioNew.setRecargo(Double.valueOf(suplementarias.getText()));
                    controlDiarioNew.setCaso(Const.TRABAJO);
                } else if (marcarLibre.isSelected()) {
                    controlDiarioNew.setNormales(8d);
                    controlDiarioNew.setSobretiempo(0d);
                    controlDiarioNew.setRecargo(0d);
                    controlDiarioNew.setCaso(Const.LIBRE);
                } else if (marcarFalta.isSelected()) {
                    controlDiarioNew.setNormales(0d);
                    controlDiarioNew.setSobretiempo(0d);
                    controlDiarioNew.setRecargo(0d);
                    controlDiarioNew.setCaso(Const.FALTA);
                } else if (marcarPermiso.isSelected()) {
                    controlDiarioNew.setNormales(0d);
                    controlDiarioNew.setSobretiempo(0d);
                    controlDiarioNew.setRecargo(0d);
                    controlDiarioNew.setCaso(Const.PERMISO);
                } else if (marcarVacaciones.isSelected()) {
                    controlDiarioNew.setNormales(0d);
                    controlDiarioNew.setSobretiempo(0d);
                    controlDiarioNew.setRecargo(0d);
                    controlDiarioNew.setCaso(Const.VACACIONES);
                } else if (marcarCM.isSelected()) {
                    controlDiarioNew.setNormales(8d);
                    controlDiarioNew.setSobretiempo(0d);
                    controlDiarioNew.setRecargo(0d);
                    controlDiarioNew.setCaso(Const.CM);
                } else if (marcarDM.isSelected()) {
                    controlDiarioNew.setNormales(8d);
                    controlDiarioNew.setSobretiempo(0d);
                    controlDiarioNew.setRecargo(0d);
                    controlDiarioNew.setCaso(Const.DM);
                } 
                controlDiarioNew.setEntrada(entrada);
                controlDiarioNew.setSalida(salida);
                controlDiarioNew.setMedioDia(medioDia);
                controlDiarioNew.setCliente(cliente);
                stage.close();
                if (asignarHorasExtrasController != null)
                    asignarHorasExtrasController.setHorarioToTurnos(new ControlExtras(controlDiarioNew));
                else
                    asignarHorariosController.setHorarioToTurnos(controlDiarioNew);
            }
        }
    }
    
    /*public void buscarRoles(Fecha fecha, int dias) {
        Fecha tiempo = new Fecha(fecha.getFecha());
        if (dias > 1) {
           int comienzoMes = empresa.getComienzoMes();
           tiempo.plusDays(1);
        } 
    }*/
    
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
    
    public void setEmpleado(Usuario empleado, ControlDiario controlDiario, 
            Fecha fecha, Boolean editable, Fecha fechaInicio) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin; ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        multiple = false;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        this.empleado = empleado;
        this.controlDiario = controlDiario;
        this.fecha = fecha;
        this.editable = editable;
        
        setControlInfo();
        
        this.changeExtras = false;
        
        fechaLabel.setText(Fechas.getFechaConMes(fecha));
        
        verificarDia();
        
        sobreTiempo.setVisible(false);
        suplementarias.setVisible(false);
        sobretiempoLabel.setVisible(false);
        recargoLabel.setVisible(false);
    }
    
    public void setEmpleado(Usuario empleado, ControlExtras controlExtras, 
            DateTime fecha, Boolean editable, Fecha fechaInicio) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin; ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        multiple = false;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        this.empleado = empleado;
        this.controlExtras = controlExtras;
        this.fechaExtras = fecha;
        this.editable = editable;
        
        setControlInfo();
        
        this.changeExtras = true;
        
        fechaLabel.setText(Fechas.getFechaConMes(fecha));
        
        verificarDia();
        
        normales.setVisible(false);
        normalesLabel.setVisible(false);
    }
    
    void setControlInfo() {
        if (controlDiario != null) {
            
            entrada = controlDiario.getEntrada();
            salida = controlDiario.getSalida();
            medioDia = controlDiario.getMedioDia();
            
            normales.setText(controlDiario.getNormales().toString());
            suplementarias.setText(controlDiario.getRecargo().toString());
            sobreTiempo.setText(controlDiario.getSobretiempo().toString());
            
            if (controlDiario.getCaso().equals(Const.LIBRE)) {
                marcarLibre.setSelected(true);
                checkLibre(null);
            } else if (controlDiario.getCaso().equals(Const.FALTA)) {
                marcarFalta.setSelected(true);
                checkFalta(null);
            } else if (controlDiario.getCaso().equals(Const.PERMISO)) {
                marcarPermiso.setSelected(true);
                checkPermiso(null);
            } else if (controlDiario.getCaso().equals(Const.VACACIONES)) {
                marcarVacaciones.setSelected(true);
                checkVacaciones(null);
            } else if (controlDiario.getCaso().equals(Const.CM)) {
                marcarCM.setSelected(true);
                checkCM(null);
            } else if (controlDiario.getCaso().equals(Const.DM)) {
                marcarDM.setSelected(true);
                checkDM(null);
            } else {
                horarioButton.setText(getLapso(controlDiario.getEntrada(), controlDiario.getSalida()));
            }
            
            if (controlDiario.getCliente() != null) {
                cliente = controlDiario.getCliente();
                clienteButton.setText(cliente.getNombre());
            }
        } else if (controlExtras != null) {
            entrada = controlExtras.getEntrada();
            salida = controlExtras.getSalida();
            
            suplementarias.setText(controlExtras.getRecargo().toString());
            sobreTiempo.setText(controlExtras.getSobretiempo().toString());
            
            if (controlExtras.getCaso().equals(Const.LIBRE)) {
                marcarLibre.setSelected(true);
                checkLibre(null);
            } else if (controlExtras.getCaso().equals(Const.FALTA)) {
                marcarFalta.setSelected(true);
                checkFalta(null);
            } else if (controlExtras.getCaso().equals(Const.PERMISO)) {
                marcarPermiso.setSelected(true);
                checkPermiso(null);
            } else if (controlExtras.getCaso().equals(Const.VACACIONES)) {
                marcarVacaciones.setSelected(true);
                checkVacaciones(null);
            } else if (controlExtras.getCaso().equals(Const.CM)) {
                marcarCM.setSelected(true);
                checkCM(null);
            } else if (controlExtras.getCaso().equals(Const.DM)) {
                marcarDM.setSelected(true);
                checkDM(null);
            } else {
                horarioButton.setText(getLapso(controlExtras.getEntrada(), controlExtras.getSalida()));
            }
            
            if (controlExtras.getCliente() != null) {
                cliente = controlExtras.getCliente();
                clienteButton.setText(cliente.getNombre());
            }
        }
    }
    
    public void setEmpleadoMultiplesDias(Usuario empleado, ArrayList<ControlTable> controls, Fecha fechaInicio, Fecha fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        editable = true;
        multiple = true;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        this.empleado = empleado;
        this.controlDiario = controlDiario;     ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.controlsTables = controls;
        this.fecha = fecha;
        fechaLabel.setText("Multiples Dias");
        
        this.changeExtras = false;
        
        sobreTiempo.setVisible(false);
        suplementarias.setVisible(false);
        sobretiempoLabel.setVisible(false);
        recargoLabel.setVisible(false);
    }
    
    public void setEmpleadoMultiplesDiasExtras(Usuario empleado, ArrayList<ControlTable> controls, Fecha fechaInicio, Fecha fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        editable = true;
        multiple = true;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        this.empleado = empleado;
        this.controlDiario = controlDiario;     ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.controlsTables = controls;
        this.fecha = fecha;
        fechaLabel.setText("Multiples Dias");
        
        this.changeExtras = true;
        
        normales.setVisible(false);
        normalesLabel.setVisible(false);
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
    
    public void updateWindowsExtras() {
        closeDialogMode();
        rolDePagoClienteController.cambiarControlExtras(controlExtraToReturn);
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
            List<Cliente> clientes, String dia, ControlDiario controlDiario) {
        this.dia = dia;
        this.empleado = empleado.getUsuario();
        this.empleadoTable = empleado;
        this.clientes = clientes;
        this.horarios = horarios;
        fechaLabel.setText("");
        this.controlDiario = controlDiario;
        setControlInfo();
        verificarDia();
    }
    
    void setEmpleado(List<Horario> horarios, 
            List<Cliente> clientes, ControlDiario controlDiario) {
        this.clientes = clientes;
        this.horarios = horarios;
        fechaLabel.setText("");
        this.controlDiario = controlDiario;
        setControlInfo();
        verificarDia();
        
        sobreTiempo.setVisible(false);
        sobretiempoLabel.setVisible(false);
        suplementarias.setVisible(false);
        recargoLabel.setVisible(false);
        
    }
    
    void setEmpleado(List<Horario> horarios, 
            List<Cliente> clientes, ControlExtras controlExtras) {
        this.clientes = clientes;
        this.horarios = horarios;
        fechaLabel.setText("");
        this.controlExtras = controlExtras;
        setControlInfo();
        verificarDia();
        
        normales.setVisible(false);
        normalesLabel.setVisible(false);
    }

    void setAsignarHorarioController(AsignarHorariosController asignarHorariosController) {
        this.asignarHorariosController = asignarHorariosController;
    }
    
    void setAsignarHorarioController(AsignarHorasExtrasController asignarHorasExtrasController) {
        this.asignarHorasExtrasController = asignarHorasExtrasController;
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
                            if (changeExtras)
                                saveMultipleExtras();
                            else
                                saveMultiple();
                        else 
                            if (changeExtras)
                                saveSingleExtras();
                            else
                                saveSingle();
                    }
             }, 1000, 1000);
            
        }
        
        public void saveMultiple() {
            try {
                Boolean cancelar = false;
                List<RolCliente> rolClientes = new RolClienteDAO()
                        .findAllByFechaAndEmpleadoId(fechaInicio.getFecha(), empleado.getId());
                for (ControlTable controlTable: controlsTables) {
                    ControlDiario controlEmpleado = controlTable.getControlDiario();
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
                        ControlDiario controlEmpleadoToDelete = controlTable.getControlDiario();
                        Fecha fechaParaGuardar = new Fecha(controlTable
                                .getFecha().getFecha());
                        ControlDiario controlEmpleadoNew = new ControlDiario();
                        controlEmpleadoNew.setFecha(fechaParaGuardar.getFecha());
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
                        new ControlDiarioDAO().save(controlEmpleadoNew);
                        if (controlEmpleadoToDelete != null) {
                            new ControlDiarioDAO().delete(controlEmpleadoToDelete);
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
        
        public void saveMultipleExtras() {
            try {
                Boolean cancelar = false;
                List<RolCliente> rolClientes = new RolClienteDAO()
                        .findAllByFechaAndEmpleadoId(fechaInicio.getFecha(), empleado.getId());
                for (ControlTable controlTable: controlsTables) {
                    ControlExtras controlExtras = controlTable.getControlExtras();
                    if (controlExtras != null) {
                        for (RolCliente rolCliente: rolClientes) {
                            if (rolCliente.getCliente() != null && controlExtras.getCliente() != null) {
                                if (controlExtras.getCliente().getId().equals(rolCliente.getCliente().getId())) {
                                    cancelar = true;  
                                }
                            } else if (rolCliente.getCliente() == null && controlExtras.getCliente() == null) {
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
                        ControlExtras controlEmpleadoToDelete = controlTable.getControlExtras();
                        DateTime fechaParaGuardar = controlTable
                                .getFechaExtra();
                        ControlExtras controlEmpleadoNew = new ControlExtras();
                        controlEmpleadoNew.setFecha(new Date(fechaParaGuardar.getMillis()));
                        if (marcarTrabajo.isSelected()) {
                            controlEmpleadoNew.setSobretiempo(Double.valueOf(sobreTiempo.getText()));
                            controlEmpleadoNew.setRecargo(Double.valueOf(suplementarias.getText()));
                            controlEmpleadoNew.setCaso(Const.TRABAJO);
                        } else if (marcarLibre.isSelected()) {
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.LIBRE);
                        } else if (marcarFalta.isSelected()) {
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.FALTA);
                        } else if (marcarPermiso.isSelected()) {
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.PERMISO);
                        } else if (marcarVacaciones.isSelected()) {
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.VACACIONES);
                        } else if (marcarCM.isSelected()) {
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.CM);
                        } else if (marcarDM.isSelected()) {
                            controlEmpleadoNew.setSobretiempo(0d);
                            controlEmpleadoNew.setRecargo(0d);
                            controlEmpleadoNew.setCaso(Const.DM);
                        } 
                        controlEmpleadoNew.setEntrada(entrada);
                        controlEmpleadoNew.setSalida(salida);
                        controlEmpleadoNew.setUsuario(empleado);
                        controlEmpleadoNew.setCliente(cliente);
                        new ControlExtrasDAO().save(controlEmpleadoNew);
                        if (controlEmpleadoToDelete != null) {
                            new ControlExtrasDAO().delete(controlEmpleadoToDelete);
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
                rolesCliente = new RolClienteDAO().findAllByFechaAndEmpleadoId(fechaInicio.getFecha(), empleado.getId());
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
                    
                    if (controlDiario != null) {
                        if (controlDiario.getFecha().equals(fecha)) {
                            
                            if (controlDiario.getCliente() == null) {
                                if (rolCliente.getCliente() == null) {
                                    cancelar = true;
                                } 
                            } else {
                                if (rolCliente.getCliente() != null) {
                                    if (rolCliente.getCliente().getId()
                                            .equals(controlDiario.getCliente().getId())) {
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
                    ControlDiario controlEmpleadoNew = new ControlDiario();
                    controlEmpleadoNew.setFecha(fecha.getFecha());
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
                    new ControlDiarioDAO().save(controlEmpleadoNew);
                    if (controlDiario != null && controlDiario.getFecha()
                            .equals(controlEmpleadoNew.getFecha())) {
                        new ControlDiarioDAO().delete(controlDiario);
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
        
        public void saveSingleExtras() {
            try {
                Boolean cancelar = false;
                List<RolCliente> rolesCliente;
                rolesCliente = new RolClienteDAO().findAllByFechaAndEmpleadoId(fechaInicio.getFecha(), empleado.getId());
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
                    
                    if (controlExtras != null) {
                        if (controlExtras.getFecha().getTime() == fechaExtras.getMillis()) {
                            
                            if (controlExtras.getCliente() == null) {
                                if (rolCliente.getCliente() == null) {
                                    cancelar = true;
                                } 
                            } else {
                                if (rolCliente.getCliente() != null) {
                                    if (rolCliente.getCliente().getId()
                                            .equals(controlExtras.getCliente().getId())) {
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
                    ControlExtras controlEmpleadoNew = new ControlExtras();
                    controlEmpleadoNew.setFecha(new Date(fechaExtras.getMillis()));
                    if (marcarTrabajo.isSelected()) {
                        controlEmpleadoNew.setSobretiempo(Double.valueOf(sobreTiempo.getText()));
                        controlEmpleadoNew.setRecargo(Double.valueOf(suplementarias.getText()));
                        controlEmpleadoNew.setCaso(Const.TRABAJO);
                    } else if (marcarLibre.isSelected()) {
                        controlEmpleadoNew.setSobretiempo(0d);
                        controlEmpleadoNew.setRecargo(0d);
                        controlEmpleadoNew.setCaso(Const.LIBRE);
                    } else if (marcarFalta.isSelected()) {
                        controlEmpleadoNew.setSobretiempo(0d);
                        controlEmpleadoNew.setRecargo(0d);
                        controlEmpleadoNew.setCaso(Const.FALTA);
                    } else if (marcarPermiso.isSelected()) {
                        controlEmpleadoNew.setSobretiempo(0d);
                        controlEmpleadoNew.setRecargo(0d);
                        controlEmpleadoNew.setCaso(Const.PERMISO);
                    } else if (marcarVacaciones.isSelected()) {
                        controlEmpleadoNew.setSobretiempo(0d);
                        controlEmpleadoNew.setRecargo(0d);
                        controlEmpleadoNew.setCaso(Const.VACACIONES);
                    } else if (marcarCM.isSelected()) {
                        controlEmpleadoNew.setSobretiempo(0d);
                        controlEmpleadoNew.setRecargo(0d);
                        controlEmpleadoNew.setCaso(Const.CM);
                    } else if (marcarDM.isSelected()) {
                        controlEmpleadoNew.setSobretiempo(0d);
                        controlEmpleadoNew.setRecargo(0d);
                        controlEmpleadoNew.setCaso(Const.DM);
                    } 
                    controlEmpleadoNew.setEntrada(entrada);
                    controlEmpleadoNew.setSalida(salida);
                    controlEmpleadoNew.setUsuario(empleado);
                    controlEmpleadoNew.setCliente(cliente);
                    new ControlExtrasDAO().save(controlEmpleadoNew);
                    if (controlExtras != null && controlExtras.getFecha().getTime() ==
                            controlEmpleadoNew.getFecha().getTime()) {
                        new ControlExtrasDAO().delete(controlExtras);
                    }
                    HibernateSessionFactory.getSession().flush();
                    controlExtraToReturn= controlEmpleadoNew;

                    // Registro para auditar
                    String detalles = "registro horas extras para el empleado " 
                            + empleado.getApellido() + " " + empleado.getNombre()
                            + " del dia " + Fechas.getFechaConMes(fechaExtras);
                    rolDePagoClienteController.aplicacionControl.au
                            .saveAgrego(detalles, rolDePagoClienteController
                                    .aplicacionControl.permisos.getUsuario());

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            updateWindowsExtras();
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