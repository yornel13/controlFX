/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.ControlTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButtonBlue;
import static aplicacion.control.util.Numeros.roundInt;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.ClienteDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.HorarioDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UniformeDAO;
import hibernate.model.Actuariales;
import hibernate.model.Cliente;
import hibernate.model.Constante;
import hibernate.model.Horario;
import hibernate.model.RolCliente;
import hibernate.model.RolIndividual;
import hibernate.model.Seguro;
import hibernate.model.Uniforme;
import hibernate.model.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import hibernate.dao.BonosDAO;
import hibernate.dao.DiasVacacionesDAO;
import hibernate.model.Bonos;
import hibernate.model.DiasVacaciones;
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.ControlDiarioDAO;
import hibernate.model.ControlDiario;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Yornel
 */
public class RolDePagoClienteController implements Initializable {
    
    private Stage stagePrincipal;
    
    public AplicacionControl aplicacionControl;
    
    private Usuario empleado;
    
    public ArrayList<ControlDiario> controlesEmpleado;
    private ArrayList<ControlTable> controlesDiarios;
    
    @FXML
    private TableColumn fechaColumna;
    
    @FXML
    private TableColumn diaColumna;
    
    @FXML
    private TableColumn clienteColumna;
    
    @FXML
    private TableColumn normalesColumna;
    
    @FXML
    private TableColumn sobreTiempoColumna;
    
    @FXML
    private TableColumn recargoColumna;
    
    @FXML
    private TableColumn observacionColumna;
    
    @FXML
    private  TableColumn<ControlTable, ControlTable> marcarColumna;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private Label totalDias;
           
    @FXML
    private Label totalHorasN;
    
    @FXML
    private Label totalHorasST;
    
    @FXML
    private Label totalHorasRC;
    
    @FXML
    private Label totalHorasExtras;
    
    @FXML
    private Label totalSalario;
    
    @FXML
    private Label totalSobreTiempo;
    
    @FXML
    private Label totalRecargo;
    
    @FXML
    private Label totalBonos;
    
    @FXML
    private Label subTotal;
    
    @FXML
    private Label totalReserva;
    
    @FXML
    private Label totalIngresos;  
    
    @FXML
    private Button leyenda1;
    
    @FXML
    private Button leyenda2;
    
    @FXML
    private Button leyenda3;
    
    @FXML 
    private Button expandirButton;
    
    @FXML
    private GridPane gridPaneTotal;
    
     @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private Button buttonHorario;
    
    @FXML
    private Button buttonBorrar;
    
    @FXML 
    private Button buttonGenerarRol;
    
    @FXML
    private CheckBox checkBoxMarcarTodos;
    
    @FXML
    private TextField bonoField;
    
    @FXML
    private TextField transporteField;
    
    @FXML
    private TextField vacacionesField;
    
    @FXML
    private TextField decimo3Field;
    
    @FXML
    private TextField decimo4Field;
    
    @FXML
    private TextField jubilacionField;
    
    @FXML
    private TextField aporteField;
    
    @FXML
    private TextField segurosField;
    
    @FXML
    private TextField uniformeField;
    
    @FXML
    private CheckBox checkBoxCasoEspecial;
    
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
    private Label indicacion1;
    
    public Cliente cliente;
    
    private ObservableList<ControlTable> data;
    
    ArrayList<Usuario> usuarios;
    
    private RolCliente pago;
    
    private Boolean editable = true;
    
    private Button buttonchangeBono;
    
    List<Horario> horarios;
    List<Cliente> clientes;
    
    Fecha fin;
    Fecha inicio;
    
    Dialog<Void> dialog;
    private Uniforme uniforme;
    private Seguro seguro;
    private Bonos bonos;
    private Actuariales actuariales;
    private Constante constDecimoCuarto;
    private DiasVacaciones diasVacaciones;
    
    private Boolean casoEspecial = false;
    private Boolean variableVacaciones = false;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    public void cambiarBono(ActionEvent event) {
        
    }
    
    @FXML
    private void expandir(ActionEvent event) {
        if (expandirButton.getText().equalsIgnoreCase("Expandir")) {
            empleadosTableView.setPrefHeight(475);
            expandirButton.setText("Contraer");
            gridPaneTotal.setVisible(false);
        } else {
            empleadosTableView.setPrefHeight(240);
            expandirButton.setText("Expandir");
            gridPaneTotal.setVisible(true);
        }
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        inicio = inicio.plusMonths(1);
        fin = fin.plusMonths(1);
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa); 
        mostrarRegistro();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        inicio = inicio.minusMonths(1);
        fin = fin.minusMonths(1);
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa); 
        mostrarRegistro();
    }
    
    @FXML
    public void onClickPago(ActionEvent event) {
        if (indicacion1.getText().equals("")) {
            if (editable) {
                if (aplicacionControl.permisos == null) {
                   aplicacionControl.noLogeado();
                } else {
                    if (aplicacionControl.permisos.getPermiso(Permisos.ROLES, Permisos.Nivel.CREAR)) {

                        String faltantes = "";

                        if (pago.getBono().equals(0d)) {
                            faltantes += "Sin bono.";
                        }
                        if (pago.getTransporte().equals(0d)) {
                            faltantes += " Sin transporte.";
                        }

                        Stage dialogStage = new Stage();
                        dialogStage.initModality(Modality.APPLICATION_MODAL);
                        dialogStage.setResizable(false);
                        dialogStage.setTitle("Pago " + new Fecha(pago.getInicio())
                                .toStringInverse() + " a " + new Fecha(pago.getFinalizo()
                                        ).toStringInverse());
                        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                        dialogStage.getIcons().add(new Image(stageIcon));
                        Button buttonOk = new Button("ok");
                        Text falta = new Text(faltantes);
                        falta.setFill(Color.RED);
                        Text clienteText = new Text("para el cliente " + pago.getClienteNombre()+ "?");
                        if (cliente == null) 
                            clienteText.setText("Administrativo");
                        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                        children(new Text("¿Desea generar el rol de pago del empleado " + pago.getEmpleado()),
                                clienteText, falta, buttonOk).
                        alignment(Pos.CENTER).padding(new Insets(10)).build()));
                        buttonOk.setPrefWidth(50);
                        dialogStage.show();
                        buttonOk.setOnAction((ActionEvent e) -> {
                            generarPago();
                            dialogStage.close();

                        });  


                    } else {
                        aplicacionControl.noPermitido();
                    }
                }
            } else {
                dialogError();
            }
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Despues de escribir los montos presiona enter."), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            dialogStage.show();
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
        }
    }
    
    @FXML
    public void marcarCasoEspecial(ActionEvent event) {
        if (editable) {
            if (checkBoxCasoEspecial.isSelected()) {
                casoEspecial = true;
                enableCasoEspecial();
            } else {
                casoEspecial = false;
                disableCasoEspecial();
                setToTable(false);
            }
        }
    }
    
    public void generarPago() {
        
        pago.setCasoEspecial(casoEspecial);
        pago.setDetalles("");
        new RolClienteDAO().save(pago);

        // Registro para auditar
        String detalles;
        if (cliente != null)
            detalles = "genero un rol de pago al empleado " 
                + empleado.getNombre() + " " + empleado.getApellido() + " para el cliente " 
                    + pago.getClienteNombre();
        else 
            detalles = "genero un rol de pago al empleado administrativo " 
                + empleado.getNombre() + " " + empleado.getApellido(); 
        
        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());

        dialogCompletado();
        
    }
    
    public void dialogCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Rol de cliente guardado satisfactoriamente."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            stagePrincipal.close();
        });
    }
    
    public void dialogError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("No se puede editar este registro."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    private void mostrarRegistro() {
        try {
            setControlEmpleadoInfo(empleado, inicio, fin); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
    @FXML
    public void cambiarHorarioVarios(ActionEvent event) throws IOException {
        ArrayList<ControlTable> diasMarcados = new ArrayList<>();
        for (ControlTable diaMarcado: (List<ControlTable>) data) {
            if (diaMarcado.getMarcar()) {
                diasMarcados.add(diaMarcado);
            }
        }
        
        if (!diasMarcados.isEmpty())
            abrirCambioMultiple(diasMarcados);
        else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new MaterialDesignButtonBlue("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(18).
            children(new Text("No has seleccionado ningun dia."), 
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
    
    public void abrirCambioMultiple(ArrayList<ControlTable> diasMarcados) throws IOException {
        if (editable) {
            if (aplicacionControl.permisos == null) {
               aplicacionControl.noLogeado();
            } else {
                if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {

                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class
                            .getResource("ventanas/VentanaHorarioEmpleadoCliente.fxml"));
                    AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Multiples dias");
                    String stageIcon = AplicacionControl.class
                            .getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaRolDePago);
                    ventana.setScene(scene);
                    HorarioEmpleadoClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setRolDePagoCliente(this);
                    controller.setCliente(cliente);
                    controller.setEmpleadoMultiplesDias(empleado, diasMarcados, inicio, fin);
                    ventana.show();

                } else {
                   aplicacionControl.noPermitido();
                }
            } 
        } else {
            dialogError();
        }
    }
    
    @FXML
    public void borrarHorarios(ActionEvent event) {
        ArrayList<ControlTable> diasMarcados = new ArrayList<>();
        for (ControlTable diaMarcado: (List<ControlTable>) data) {
            if (diaMarcado.getMarcar()) {
                diasMarcados.add(diaMarcado);
            }
        }
        
        if (!diasMarcados.isEmpty()) {
            borrarHoras(diasMarcados);
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
            children(new Text("No has seleccionado ningun dia."), 
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
    
    public void closeDialogMode() {
        if (dialog != null) {
           Stage toClose = (Stage) dialog.getDialogPane()
                   .getScene().getWindow();
           toClose.close();
           dialog.close();
           dialog = null;
        }
    }
    
    @FXML
    public void marcarTodos(ActionEvent event) {
        for (ControlTable control: 
                (List<ControlTable>) empleadosTableView.getItems()) {
            if (checkBoxMarcarTodos.isSelected()) {
                control.setMarcar(true);
            } else {
                control.setMarcar(false);
            }
            data.set(data.indexOf(control), control);
        }
    }
    
    private void horarioEmpleado(ControlDiario controlEmpleado, 
            Fecha fecha) throws IOException {
        if (editable) {
            if (aplicacionControl.permisos == null) {
               aplicacionControl.noLogeado();
            } else {
                if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {

                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorarioEmpleadoCliente.fxml"));
                    AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Empleado: " + empleado.getApellido()+ " " 
                            + empleado.getNombre());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaRolDePago);
                    ventana.setScene(scene);
                    HorarioEmpleadoClienteController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setRolDePagoCliente(this);
                    controller.setStage(ventana);
                    controller.setCliente(cliente);
                    controller.setEmpleado(empleado, controlEmpleado, fecha, editable, inicio);
                    ventana.show();

                } else {
                   aplicacionControl.noPermitido();
                }
            } 
        } else {
            dialogError();
        }
    }
    
    private void borrarHoras(ArrayList<ControlTable> diasMarcados) {
        if (editable)
            if (aplicacionControl.permisos == null) {
                aplicacionControl.noLogeado();
            } else {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("");
                String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                MaterialDesignButtonBlue buttonOk = new MaterialDesignButtonBlue("Si");
                MaterialDesignButtonBlue buttonNo = new MaterialDesignButtonBlue("no");
                HBox hBox = HBoxBuilder.create()
                        .spacing(10.0) //In case you are using HBoxBuilder
                        .padding(new Insets(5, 5, 5, 5))
                        .alignment(Pos.CENTER)
                        .children(buttonOk, buttonNo)
                        .build();
                hBox.maxWidth(120);
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Seguro que desea borrar estos horarios?"), hBox).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                buttonOk.setMinWidth(50);
                buttonNo.setMinWidth(50);
                buttonOk.setOnAction((ActionEvent e) -> {
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    Runnable worker = new RolDePagoClienteController.DataBaseThread(1);
                    executor.execute(worker);
                    executor.shutdown();

                    loadingMode();
                    dialogStage.close();
                });
                buttonNo.setOnAction((ActionEvent e) -> {
                    dialogStage.close();
                });
                dialogStage.showAndWait();
                } 
        else {
            dialogError();
        }
    }
    
    public void guardarRegistro(Usuario empleado, Double suplementarias, 
            Double sobreTiempo, Cliente cliente, Fecha fecha, Boolean libre,
            Boolean falta) throws ParseException {
        ControlDiarioDAO controlEmpleadoDAO = new ControlDiarioDAO();
        ControlDiario controlEmpleado = new ControlDiario();
        controlEmpleado.setFecha(fecha.getFecha());
        controlEmpleado.setUsuario(empleado);
        controlEmpleado.setSobretiempo(sobreTiempo);
        controlEmpleado.setRecargo(suplementarias);
        controlEmpleado.setCliente(cliente);
        controlEmpleadoDAO.save(controlEmpleado);
        setControlEmpleadoInfo(this.empleado, 
                new Fecha(selectorAnoDe, selectorMesDe, selectorDiaDe), 
                new Fecha(selectorAnoHa, selectorMesHa, selectorDiaHa));
        
        // Registro para auditar
        String detalles = "agrego un registro diario al empleado " 
            + empleado.getNombre() + " " + empleado.getApellido() 
            + " con fecha " + fecha.toStringInverse();
        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
    }
    
    public void cambiarControlEmpleado(ControlDiario control) {
        Boolean agregar = true;
        for (ControlDiario controlEmpleado: controlesEmpleado) {
            if (controlEmpleado.getFecha().equals(control.getFecha())) {
                controlesEmpleado.set(controlesEmpleado.indexOf(controlEmpleado), control);
                agregar = false;
            }
        }
        if (agregar)
                controlesEmpleado.add(control);
        
        clearControlesDiarios();
        setToTable(false);
    }
    
    public void quitarControlEmpleado(ControlTable controlTableDelete) {
        System.out.println("borrando control "+controlTableDelete.getFechaString());
        ControlDiario control = controlTableDelete.getControlDiario();
        
        for (ControlTable controlTable: controlesDiarios) {
            if(controlTable.getFecha().getFecha().equals(control.getFecha())) {
                ControlTable controlDiarioEmpty = new ControlTable();
                controlDiarioEmpty.setMarcar(controlTableDelete.getMarcar());
                controlDiarioEmpty.setFecha(controlTableDelete.getFecha());
                controlDiarioEmpty.setFechaString(controlDiarioEmpty.getFecha().toStringInverse());
                controlDiarioEmpty.setDia(controlDiarioEmpty.getFecha().getMonthName());
                controlesDiarios.set(controlesDiarios.indexOf(controlTable), controlDiarioEmpty); 
            }
        }
        controlesEmpleado.remove(control);
    }
    
    @FXML
    public void calcularVacaciones(ActionEvent event) {
        if (casoEspecial) {
            variableVacaciones = true;
            casoEspecial = false;
            setToTable(false);
            variableVacaciones = false;
            casoEspecial = true;
        } else {
            setToTable(false);
        }
    }
    
    @FXML
    public void calcularHoras(ActionEvent event) {
        setToTable(false);
    }
    
    public void setEmpleado(Usuario empleado, Cliente cliente, Fecha inicio, Fecha fin) throws ParseException {
        this.empleado = empleado;
        this.cliente = cliente;
        this.inicio = inicio;
        this.fin = fin;
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
        
        horarios = (List<Horario>) new HorarioDAO().findAll();
        clientes = new ClienteDAO().findAllActivo();
        
        setControlEmpleadoInfo(empleado, inicio, fin);
    }
    
    public void setControlEmpleadoInfo(Usuario empleado, Fecha inicio, Fecha fin) {
        
        ControlDiarioDAO controlDAO = new ControlDiarioDAO();
        
        Fecha fechaInicial = new Fecha(inicio.getFecha()).minusDays(7); // dias anteriores de horas extras 
        Fecha fechaFinal = new Fecha(fin.getFecha());
        
        int days = (int) fechaInicial.daysDifference(fechaFinal);
        
        controlesDiarios = new ArrayList<>();
        
        for(int i = 0; i <= days; i++) {
            ControlTable controlTable = new ControlTable();
            controlTable.setFecha(fechaInicial.plusDays(i));
            controlTable.setFechaString(controlTable.getFecha().toStringInverse());
            controlTable.setDia(controlTable.getFecha().getMonthName());
            controlesDiarios.add(controlTable);
        }
        
        System.out.println("dias " + controlesDiarios.size());
        controlesEmpleado = new ArrayList<>();
        controlesEmpleado.addAll(controlDAO
                .findAllByEmpleadoIdInDeterminateTime(empleado.getId(), 
                        fechaInicial.getFecha(), fechaFinal.getFecha()));
        
        setToTable(true);
    }
    
    void setToTable(Boolean searchRol) {
        
        Double dias = 0d;
        Double diasDecimo4to = 0d;
        Double diasJubilacion = 0d;
        Double normales = 0d;
        Double sobreTiempo = 0d;
        Double suplementarias = 0d;
        Integer medioDias = 0;
        int descansosMedicos = 0;
        
        for (ControlTable controlDiario: controlesDiarios) {
            
            for (ControlDiario control: controlesEmpleado) {
                if (controlDiario.getFecha().getFecha().equals(control.getFecha())) {
                    
                    controlDiario.setControlEmpleado(control);
                    
                    controlDiario.setId(control.getId());
                    if (control.getCliente() != null) {
                        controlDiario.setCliente(control.getCliente().getNombre());
                    }
                    
                    controlDiario.setNormales(control.getNormales());
                    controlDiario.setSobreTiempo(control.getSobretiempo());
                    controlDiario.setRecargo(control.getRecargo());
                    
                    if (control.getCaso().equals(Const.LIBRE)) {
                       controlDiario.setObservacion("Libre"); 
                    } else if (control.getCaso().equals(Const.FALTA)) {
                       controlDiario.setObservacion("Falta"); 
                    } else if (control.getCaso().equals(Const.VACACIONES)) {
                       controlDiario.setObservacion("Vacaciones"); 
                    } else if (control.getCaso().equals(Const.PERMISO)) {
                       controlDiario.setObservacion("Permiso"); 
                    } else if (control.getCaso().equals(Const.DM)) {
                        controlDiario.setObservacion("D. Medico"); 
                        descansosMedicos++;
                       
                        if (descansosMedicos > 3) {
                            controlDiario.setNormales(0d);
                            controlDiario.setSobreTiempo(0d);
                            controlDiario.setRecargo(0d);
                        }
                    } else if (control.getCaso().equals(Const.CM)) {
                       controlDiario.setObservacion("C. Medica"); 
                    } else if (control.getMedioDia()) {
                        controlDiario.setObservacion("1/2 Dia"); 
                    }
                    if (cliente == null && control.getCliente() == null 
                            || cliente != null && control.getCliente() != null 
                            && cliente.getId().equals(control.getCliente().getId())) {
                        
                        if (controlesDiarios.indexOf(controlDiario) > 6) {
                            if (control.getMedioDia()) {
                                medioDias++;
                            }

                            if (control.getCaso().equalsIgnoreCase(Const.TRABAJO)
                                    || control.getCaso().equalsIgnoreCase(Const.LIBRE)
                                    || control.getCaso().equalsIgnoreCase(Const.CM)) {

                                dias += 1;
                                normales = control.getMedioDia()? normales+4 : normales+8;

                            } else if (control.getCaso().equalsIgnoreCase(Const.DM)) {
                                if (descansosMedicos <= 3) {
                                    dias += 1;
                                    normales = control.getMedioDia()? normales+4 : normales+8;
                                }
                            } 

                            if (control.getCaso().equalsIgnoreCase(Const.TRABAJO)
                                    || control.getCaso().equalsIgnoreCase(Const.VACACIONES)
                                    || control.getCaso().equalsIgnoreCase(Const.PERMISO)
                                    || control.getCaso().equalsIgnoreCase(Const.LIBRE)
                                    || control.getCaso().equalsIgnoreCase(Const.CM)
                                    || control.getCaso().equalsIgnoreCase(Const.DM)) {

                                diasJubilacion += 1;

                            } 

                            if (control.getCaso().equalsIgnoreCase(Const.TRABAJO)
                                    || control.getCaso().equalsIgnoreCase(Const.VACACIONES)
                                    || control.getCaso().equalsIgnoreCase(Const.LIBRE)
                                    || control.getCaso().equalsIgnoreCase(Const.CM)
                                    || control.getCaso().equalsIgnoreCase(Const.DM)) {

                                diasDecimo4to += 1;

                            } 

                        } else {
                            controlDiario.setNormales(null);
                        }  
                        if (controlesDiarios.indexOf(controlDiario) < 30) {
                            if (control.getCaso().equalsIgnoreCase(Const.TRABAJO)
                                    || control.getCaso().equalsIgnoreCase(Const.LIBRE)
                                    || control.getCaso().equalsIgnoreCase(Const.CM)) {

                                sobreTiempo += control.getSobretiempo();
                                suplementarias += control.getRecargo();
                            } 
                        } else {
                            controlDiario.setSobreTiempo(null);
                            controlDiario.setRecargo(null);
                        }
                        
                    } else {
                        controlDiario.setAjeno(true);
                    }
                }
            }
        }
       
        data = FXCollections.observableArrayList();
        data.addAll(controlesDiarios);
        empleadosTableView.setItems(data);
        
        indicacion1.setText("");
        calcularPago(dias, diasDecimo4to, diasJubilacion, normales, sobreTiempo, suplementarias, searchRol, medioDias); 
    }
    
    public void calcularPago(Double dias, Double diasDecimo4to, Double diasJubilacion, Double normales, Double sobreTiempo, 
            Double suplementarias, Boolean searchRol, Integer medioDias) {
        
        pago = null;
        
        Double sueldoDia = empleado.getDetallesEmpleado().getSueldo() / 30d;
        Double sueldoHoras = empleado.getDetallesEmpleado().getSueldo() / 240d;
        
        normales = round(normales);
        sobreTiempo = round(sobreTiempo);
        suplementarias = round(suplementarias);
        
        totalDias.setText(dias.intValue()+"");
        totalHorasN.setText(normales.toString());
        totalHorasRC.setText(suplementarias.toString());
        totalHorasST.setText(sobreTiempo.toString());
        totalHorasExtras.setText(round(sobreTiempo + suplementarias).toString());
        
        // Salario
        Double totalSalarioDouble = round(sueldoHoras * normales);
        totalSalario.setText(totalSalarioDouble.toString());
        Double totalSobreTiempoDouble = round(sueldoHoras * sobreTiempo);
        totalSobreTiempo.setText(totalSobreTiempoDouble.toString());
        Double totalRecargoDouble = round(sueldoHoras * suplementarias);
        totalRecargo.setText(totalRecargoDouble.toString());
        Double totalBonoDouble = round(getBono(searchRol));
        bonoField.setText(totalBonoDouble.toString());
        Double totalTransporteDouble = round(getTransporte(searchRol));
        transporteField.setText(totalTransporteDouble.toString());
        Double totalBonosDouble = round(totalBonoDouble + totalTransporteDouble);
        totalBonos.setText(totalBonosDouble.toString());
        Double totalVacacionesDouble;
        Double sueldoSinVacaciones = totalSalarioDouble + totalSobreTiempoDouble + totalRecargoDouble + totalBonoDouble + totalTransporteDouble;
        if (variableVacaciones)
            totalVacacionesDouble = round(vacacionesField.getText());
        else
            totalVacacionesDouble = casoEspecial ? round(vacacionesField.getText()) : round(getVacaciones(sueldoSinVacaciones, searchRol));
        vacacionesField.setText(totalVacacionesDouble.toString());
        Double subTotalDouble = round(totalSalarioDouble 
                + totalSobreTiempoDouble + totalRecargoDouble 
                + totalBonosDouble + totalVacacionesDouble);
        subTotal.setText(subTotalDouble.toString());
        ////////////////////////////////////////////////////
        Double decimoTercero = casoEspecial ? round(decimo3Field.getText()) : round(subTotalDouble / 12d);
        decimo3Field.setText(decimoTercero.toString());
        Double decimoCuarto = casoEspecial ? round(decimo4Field.getText()) : round(getDecimoCuarto(searchRol)/30d * diasDecimo4to);
        decimo4Field.setText(decimoCuarto.toString());
        totalReserva.setText(decimoTercero.toString());
        Double jubilacionPatronal = casoEspecial ? round(jubilacionField.getText()) : round((getActuariales(empleado.getId(), searchRol)/ 360d) * diasJubilacion);
        jubilacionField.setText(jubilacionPatronal.toString());
        Double aportePatronal = casoEspecial ? round(aporteField.getText()) : round(subTotalDouble * 12.15d / 100d);
        aporteField.setText(aportePatronal.toString());
        Double segurosDecimal = casoEspecial ? round(segurosField.getText()) : round(getSeguro(searchRol) * dias);
        segurosField.setText(segurosDecimal.toString());
        Double uniformeDecimal = casoEspecial ? round(uniformeField.getText()) : round(getUniforme(searchRol) * dias);
        uniformeField.setText(uniformeDecimal.toString());
        
        Double ingresoTotal = round(subTotalDouble + decimoTercero 
                + decimoCuarto + decimoTercero + jubilacionPatronal 
                + aportePatronal + segurosDecimal + uniformeDecimal);
        totalIngresos.setText(ingresoTotal.toString());
        
        if (searchRol) {
            if (cliente != null)
                pago = new RolClienteDAO().findByFechaAndEmpleadoIdAndClienteId(inicio.getFecha(), 
                        this.empleado.getId(), this.cliente.getId());
            else 
                pago = new RolClienteDAO().findByFechaAndEmpleadoIdSinCliente(inicio.getFecha(), 
                    this.empleado.getId());
        }
        
        if (pago == null) {
            
            RolIndividual rolIndividual = null;
            
            if (searchRol) {
                rolIndividual = new RolIndividualDAO().findByFechaAndEmpleadoIdAndDetalles(inicio.getFecha(), 
                        empleado.getId(), Const.ROL_PAGO_INDIVIDUAL);
            }
            
            if (rolIndividual == null) {
                pago = new RolCliente();
                pago.setFecha(new Timestamp(new Date().getTime()));
                pago.setInicio(inicio.getFecha());
                pago.setFinalizo(fin.getFecha());
                pago.setDias(dias);
                pago.setHorasNormales(normales);
                pago.setHorasSuplementarias(suplementarias);  // RC
                pago.setHorasSobreTiempo(sobreTiempo);         // ST
                pago.setTotalHorasExtras(sobreTiempo + suplementarias);
                pago.setSalario(totalSalarioDouble);
                pago.setMontoHorasSuplementarias(totalRecargoDouble);
                pago.setMontoHorasSobreTiempo(totalSobreTiempoDouble);
                pago.setBono(totalBonoDouble);
                pago.setTransporte(totalTransporteDouble);
                pago.setTotalBonos(totalBonosDouble);
                pago.setVacaciones(totalVacacionesDouble);
                pago.setSubtotal(subTotalDouble);
                pago.setDecimoTercero(decimoTercero);
                pago.setDecimoCuarto(decimoCuarto);
                pago.setJubilacionPatronal(jubilacionPatronal);
                pago.setAportePatronal(aportePatronal);
                pago.setSeguros(segurosDecimal);
                pago.setUniformes(uniformeDecimal);
                pago.setTotalIngreso(ingresoTotal);
                pago.setEmpleado(empleado.getNombre() + " " + empleado.getApellido());
                pago.setCedula(empleado.getCedula());
                pago.setEmpresa(empleado.getDetallesEmpleado().getEmpresa().getNombre());
                pago.setSueldo(empleado.getDetallesEmpleado().getSueldo());
                pago.setUsuario(empleado);
                pago.setCliente(cliente);
                if (cliente != null)
                    pago.setClienteNombre(this.cliente.getNombre());
                
                checkBoxCasoEspecial.setDisable(false);
                editable = true;
            } else {
                checkBoxCasoEspecial.setDisable(true);
                editable = false;
            }
        } else {
            editable = false;
            vacacionesField.setText(pago.getVacaciones().toString());
            bonoField.setText(pago.getBono().toString());
            transporteField.setText(pago.getTransporte().toString());
            totalBonos.setText(pago.getTotalBonos().toString());
            subTotal.setText(pago.getSubtotal().toString());
            decimo3Field.setText(pago.getDecimoTercero().toString());
            decimo4Field.setText(pago.getDecimoCuarto().toString());
            totalReserva.setText(pago.getDecimoTercero().toString());
            jubilacionField.setText(pago.getJubilacionPatronal().toString());
            aporteField.setText(pago.getAportePatronal().toString());
            segurosField.setText(pago.getSeguros().toString());
            uniformeField.setText(pago.getUniformes().toString());
            totalIngresos.setText(pago.getTotalIngreso().toString());
            if (pago.getCasoEspecial())
                checkBoxCasoEspecial.setSelected(true);
            else
                checkBoxCasoEspecial.setSelected(false);
            
            checkBoxCasoEspecial.setDisable(true);
        }
        
        buttonGenerarRol.setVisible(editable);
        if (editable) {
            bonoField.setEditable(false);
            transporteField.setEditable(false);
            bonoField.setDisable(true);
            transporteField.setDisable(true);
        } else {
            bonoField.setEditable(false);
            transporteField.setEditable(true);
            bonoField.setDisable(true);
            transporteField.setDisable(true);
        }
    }
    
    void clearControlesDiarios() {
        for (ControlTable controlTable: controlesDiarios) {
            ControlTable controlDiarioEmpty = new ControlTable();
            controlDiarioEmpty.setMarcar(controlTable.getMarcar());
            controlDiarioEmpty.setFecha(controlTable.getFecha());
            controlDiarioEmpty.setFechaString(controlDiarioEmpty.getFecha().toStringInverse());
            controlDiarioEmpty.setDia(controlDiarioEmpty.getFecha().getMonthName());
            controlesDiarios.set(controlesDiarios.indexOf(controlTable), controlDiarioEmpty); 
        }
    }
    
    public void updateWindows() {
        closeDialogMode();
        calcularHoras(null);
        borradoCompletado();
    }
    
    public void borradoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se borraron los horarios con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setMaxWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.show();
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
        children(new Text("No se puede eliminar los horarios por que hay un rol cliente generado."), 
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        bonoField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        transporteField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        vacacionesField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        decimo3Field.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        decimo4Field.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        jubilacionField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        aporteField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        segurosField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        uniformeField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilterAndUpdate());
        
        disableCasoEspecial();
     
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
        
        buttonHorario.setTooltip(
            new Tooltip("Cambiar horario a varios")
        );
        buttonHorario.setOnMouseEntered((MouseEvent t) -> {
            buttonHorario.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_reloj.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonHorario.setOnMouseExited((MouseEvent t) -> {
            buttonHorario.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_reloj.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonBorrar.setOnMouseEntered((MouseEvent t) -> {
            buttonBorrar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_borrar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonBorrar.setOnMouseExited((MouseEvent t) -> {
            buttonBorrar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/icon_borrar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fechaString"));
        
        diaColumna.setCellValueFactory(new PropertyValueFactory<>("dia"));
   
        clienteColumna.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        normalesColumna.setCellValueFactory(new PropertyValueFactory<>("normales"));
       
        sobreTiempoColumna.setCellValueFactory(new PropertyValueFactory<>("sobreTiempo"));
   
        recargoColumna.setCellValueFactory(new PropertyValueFactory<>("recargo"));
      
        observacionColumna.setCellValueFactory(new PropertyValueFactory<>("observacion"));
        
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<ControlTable, ControlTable>() {
            private final CheckBox checkBoxMarcar = new CheckBox();

            @Override
            protected void updateItem(ControlTable controlTable, boolean empty) {
                super.updateItem(controlTable, empty);

                if (controlTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }

                setGraphic(checkBoxMarcar);
                
                checkBoxMarcar.setOnAction(event -> {
                    controlTable.setMarcar(checkBoxMarcar.isSelected());
                });
                
                if (controlTable.getMarcar()) {
                    checkBoxMarcar.setSelected(true);
                } else {
                    checkBoxMarcar.setSelected(false);
                }
                
                if (controlTable.esAjeno()) {
                    getTableRow().setStyle("-fx-background-color:lightcoral");
                } else if (controlTable.getNormales() == null) {
                    getTableRow().setStyle("-fx-background-color:#dddddd");
                } else {
                    getTableRow().setStyle("");
                }
            }
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<ControlTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ControlTable rowData = row.getItem();
                    try {
                        horarioEmpleado(rowData.getControlDiario(), 
                                new Fecha(rowData.getFecha().getFecha()));
                    } catch (IOException ex) {
                        Logger.getLogger(RolDePagoClienteController
                                .class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return row ;
        });
        
        leyenda1.setTooltip(new Tooltip("Dia trabajado a otro cliente"));
        leyenda2.setTooltip(new Tooltip("Dia sin horario"));
        leyenda3.setTooltip(new Tooltip("Dia con horario creado"));
        
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
    
    private void enableCasoEspecial() {
        
        vacacionesField.setEditable(true);
        decimo3Field.setEditable(true);
        decimo4Field.setEditable(true);
        jubilacionField.setEditable(true);
        aporteField.setEditable(true);
        segurosField.setEditable(true);
        uniformeField.setEditable(true);
        
        vacacionesField.setDisable(false);
        decimo3Field.setDisable(false);
        decimo4Field.setDisable(false);
        jubilacionField.setDisable(false);
        aporteField.setDisable(false);
        segurosField.setDisable(false);
        uniformeField.setDisable(false);
    }
    
    private void disableCasoEspecial() {
        
        vacacionesField.setEditable(false);
        decimo3Field.setEditable(false);
        decimo4Field.setEditable(false);
        jubilacionField.setEditable(false);
        aporteField.setEditable(false);
        segurosField.setEditable(false);
        uniformeField.setEditable(false);
        
        vacacionesField.setDisable(true);
        decimo3Field.setDisable(true);
        decimo4Field.setDisable(true);
        jubilacionField.setDisable(true);
        aporteField.setDisable(true);
        segurosField.setDisable(true);
        uniformeField.setDisable(true);
    }
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public Double getDecimoCuarto(Boolean buscar) {
        if (buscar)
            constDecimoCuarto = (Constante) new ConstanteDAO().findUniqueResultByNombre(Const.DECIMO_CUARTO);
        if (constDecimoCuarto == null) {
            return 30.5d;
        } else {
            return Double.valueOf(constDecimoCuarto.getValor());
        }
    }
    
    public Double getActuariales(Integer empleadoId, Boolean buscar) {
        if (buscar)
            actuariales = new ActuarialesDAO().findByEmpleadoId(empleadoId);
        if (actuariales == null) {
            return 0d;
        } else {
            return actuariales.getPrimario() + actuariales.getSecundario();
        }
    }
    
    public Double getSeguro(Boolean buscar) {
        if (buscar) {
            if (cliente != null) {
                seguro = new SeguroDAO().findByClienteId(cliente.getId());
            } else {
                seguro = new SeguroDAO().findAdministrativo();
            }
        }
        if (seguro == null) {
          return 0d;  
        } else {
            return seguro.getValor();
        }  
    }
    
    public Double getUniforme(Boolean buscar) {
        if (buscar) {
            if (cliente != null) {
                uniforme = new UniformeDAO().findByClienteId(cliente.getId());
            } else {
                uniforme = new UniformeDAO().findAdministrativo();
            }
        }
        if (uniforme == null) {
          return 0d;  
        } else {
            return uniforme.getValor();
        }  
    }
    
    public Double getBono(Boolean buscar) {
        if (buscar) {
            if (cliente != null) {
                bonos = new BonosDAO().findByClienteIdAndEmpleadoId(cliente.getId(), empleado.getId(), inicio.getFecha());
            } else {
                bonos = new BonosDAO().findByClienteNullAndEmpleadoId(empleado.getId(), inicio.getFecha());
            }
        }
        if (bonos == null) {
          return 0d;  
        } else {
            return bonos.getBono();
        } 
    }
    
    public Double getTransporte(Boolean buscar) {
        if (buscar) {
            if (cliente != null) {
                bonos = new BonosDAO().findByClienteIdAndEmpleadoId(cliente.getId(), empleado.getId(), inicio.getFecha());
            } else {
                bonos = new BonosDAO().findByClienteNullAndEmpleadoId(empleado.getId(), inicio.getFecha());
            }
        }
        if (bonos == null) {
          return 0d;  
        } else {
            return bonos.getTransporte();
        } 
    }
    /*
    public Double getBono() {
        if (bonoField.getText().isEmpty()) {
            return 0d;
        } else {
            return round(bonoField.getText());
        }
    }
    
    public Double getTransporte() {
        if (transporteField.getText().isEmpty()) {
            return 0d;
        } else {
            return round(transporteField.getText());
        }
    }*/
    
    public Double getVacaciones(Double sueldoSinVacaciones, Boolean buscar) {
        if (buscar)
            diasVacaciones = new DiasVacacionesDAO().findByEmpleadoId(empleado.getId());
        if (diasVacaciones == null) {
            return 0d;
        } else {
            Integer diasDerecho = diasVacaciones.getDias();
            Double sueldoNeto = sueldoSinVacaciones;
            Double vacaciones = (sueldoNeto / 360d) * diasDerecho.doubleValue();
            return vacaciones;
        }
    }
    /*
    public Double getVacaciones(Double sueldoSinVacaciones) {
        
        try {
            DateTime fechaInicial = new DateTime(getToday().getTime());
            DateTime fechaFinal = new DateTime(empleado.getDetallesEmpleado().getFechaContrato().getTime());
            int years = Years.yearsBetween(fechaFinal.withTimeAtStartOfDay(), 
                    fechaInicial.withTimeAtStartOfDay()).getYears();
            System.out.println("inicio " + fechaInicial);
            System.out.println("Final " + fechaFinal);
            int diasExtras;
            if (years >= 5) {
                diasExtras = years - 5;
            } else {
                diasExtras = 0;
            }
            System.out.println("dias extras " + diasExtras);
            Integer diasDerecho = 16 + diasExtras;
            Double sueldoNeto = sueldoSinVacaciones;
            Double vacaciones = (sueldoNeto / 360d) * diasDerecho.doubleValue();

            return vacaciones;
        } catch (Exception e) {
            e.printStackTrace();
            return 0d;
        }
    }*/
    
     public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    
    public EventHandler<KeyEvent> numDecimalFilterAndUpdate() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
            String[] strArray = new String[] {keyEvent.getCharacter()};
            int letterCount = 0;
            for (String letter: strArray) {
                if (letter.equalsIgnoreCase("."))
                    letterCount++;
            }
            if (letterCount >= 2) {
                keyEvent.consume();
            }
            
            if (!keyEvent.getCharacter().equalsIgnoreCase("\r"))
                indicacion1.setText("Presione enter al terminar de escribir");
            else
                buttonGenerarRol.requestFocus();
        };
        return aux;
    }
    
    public class DataBaseThread implements Runnable {
        
        public final Integer BORRAR = 1;
        
        Integer opcion;

        public DataBaseThread(Integer opcion){
            this.opcion = opcion;
        }

        @Override
        public void run() {
    
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        if (Objects.equals(opcion, BORRAR)) {
                            borrar();
                        }
                    }
             }, 1000, 1000);
            
        }
        
        public void borrar() {
            System.out.println("Comenzado borrado");
            try {
                Boolean cancelar = false;
                List<RolCliente> rolClientes = new RolClienteDAO()
                        .findAllByFechaAndEmpleadoId(inicio.getFecha(), empleado.getId());
                for (ControlTable controlTable: (List<ControlTable>) data) {
                    if (controlTable.getMarcar()) {
                        ControlDiario controlEmpleadoDelete = 
                                controlTable.getControlDiario();
                        if (controlEmpleadoDelete != null) {
                            
                            for (RolCliente rolCliente: rolClientes) {
                                if (rolCliente.getCliente() != null 
                                        && controlEmpleadoDelete.getCliente() != null) {
                                    if (controlEmpleadoDelete.getCliente()
                                            .getId().equals(rolCliente.getCliente().getId())) {
                                        cancelar = true;  
                                    }
                                } else if (rolCliente.getCliente() == null 
                                        && controlEmpleadoDelete.getCliente() == null) {
                                    cancelar = true;
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
                    for (ControlTable controlTable: (List<ControlTable>) data) {
                        if (controlTable.getMarcar()) {
                            ControlDiario controlEmpleadoDelete = controlTable.getControlDiario();
                            if (controlEmpleadoDelete != null) {
                                new ControlDiarioDAO().delete(controlEmpleadoDelete);
                                HibernateSessionFactory.getSession().flush();
                                quitarControlEmpleado(controlTable);

                                // Registro para auditar
                                String detalles = "elimino el horario del empleado " 
                                        + empleado.getApellido() + " " + empleado.getNombre()
                                        + "del dia " + Fechas.getFechaConMes(new Fecha(controlEmpleadoDelete.getFecha()));
                                aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                            }
                        }
                    }
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
