/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButtonBlue;
import aplicacion.control.util.Numeros;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UniformeDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Actuariales;
import hibernate.model.Cliente;
import hibernate.model.Constante;
import hibernate.model.Empresa;
import hibernate.model.RolCliente;
import hibernate.model.RolIndividual;
import hibernate.model.Seguro;
import hibernate.model.Uniforme;
import hibernate.model.Usuario;
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
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import hibernate.dao.BonosDAO;
import hibernate.dao.DiasVacacionesDAO;
import hibernate.model.Bonos;
import hibernate.model.DiasVacaciones;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.dao.ControlDiarioDAO;
import hibernate.dao.ControlExtrasDAO;
import hibernate.model.ControlDiario;
import hibernate.model.ControlExtras;
import javafx.scene.control.ChoiceBox;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;
import static aplicacion.control.util.Numeros.round;

/**
 *
 * @author Yornel
 */
public class HorasEmpleadosClienteController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    private Cliente cliente;
    
    @FXML
    private TableColumn cedulaColumna;
    
    @FXML
    private TableColumn empleadoColumna;
    
    @FXML
    private TableColumn cargoColumna;
    
    @FXML
    private TableColumn diasColumna;
    
    @FXML
    private TableColumn normalesColumna;
    
    @FXML
    private TableColumn sobretiempoColumna;
    
    @FXML
    private TableColumn recargoColumna;
    
    @FXML
    private TableColumn<EmpleadoTable, EmpleadoTable> marcarColumna;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML
    private Button buttonAtras;
    
    @FXML 
    private Label contador;
    
    @FXML
    private CheckBox checkBoxTodos;
    
    @FXML
    private Button buttonBorrar;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Button buttonCancelar;
    
    @FXML
    private Button buttonGuardar;
    
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
    
    private Fecha inicio;
    private Fecha fin;
    
    @FXML
    private Label fechaLabel;
    
    private List<EmpleadoTable> empleadosRol;
    
    private TableColumn bonoColumna;
    
    private TableColumn transporteColumna;
    
    private TableColumn totalColumna;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    List<RolCliente> rolesCliente;
    private Stage stagePrincipal;
    
    Dialog<Void> dialog;
    
    private Uniforme uniforme;
    private Seguro seguro;
    private Constante constDecimoCuarto;
    private Boolean rolesMultiples = false;
    
    private ArrayList<Bonos> bonos;
    private ArrayList<Actuariales> actuariales;
    private ArrayList<DiasVacaciones> diasVacaciones;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    public void returnClientes(ActionEvent event) {
        this.aplicacionControl.mostrarClientesEmpresa(empresa, stagePrincipal);
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        
        inicio = inicio.plusMonths(1);
        fin = fin.plusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);  
        fechaLabel.setText(inicio.getMonthName());
        
        setTableInfo(empresa, inicio, fin);
        
        checkBoxTodos.setSelected(false);
        contador.setText("");
        
        if (rolesMultiples) {
            buttonCancelar.setVisible(false);
            buttonGuardar.setVisible(false);
            buttonBorrar.setVisible(true);
            buttonPagar.setVisible(true);
            empleadosTableView.getColumns().removeAll(bonoColumna, transporteColumna, 
                    totalColumna, marcarColumna, diasColumna, 
                    normalesColumna, sobretiempoColumna, recargoColumna);
            empleadosTableView.getColumns().addAll(cargoColumna, diasColumna, 
                    normalesColumna, sobretiempoColumna, recargoColumna, marcarColumna);

            rolesMultiples = false;
            callFilter();
        }
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        
        inicio = inicio.minusMonths(1);
        fin = fin.minusMonths(1);
        
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);  
        fechaLabel.setText(inicio.getMonthName());
        
        setTableInfo(empresa, inicio, fin);
        
        checkBoxTodos.setSelected(false);
        contador.setText("");
        
        if (rolesMultiples) {
            buttonCancelar.setVisible(false);
            buttonGuardar.setVisible(false);
            buttonBorrar.setVisible(true);
            buttonPagar.setVisible(true);
            empleadosTableView.getColumns().removeAll(bonoColumna, transporteColumna, 
                    totalColumna, marcarColumna, diasColumna, 
                    normalesColumna, sobretiempoColumna, recargoColumna);
            empleadosTableView.getColumns().addAll(cargoColumna, diasColumna, 
                    normalesColumna, sobretiempoColumna, recargoColumna, marcarColumna);

            rolesMultiples = false;
            callFilter();
        }
    }
    
    @FXML
    private void marcarTodos(ActionEvent event) throws ParseException {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxTodos.isSelected()) {
                empleadoTable.setAgregar(true);
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
        contarSelecciones();
    }
    
    @FXML
    public void dialogoCancelar(ActionEvent event) {
        setTableInfo(empresa, inicio, fin);
        buttonCancelar.setVisible(false);
        buttonGuardar.setVisible(false);
        buttonBorrar.setVisible(true);
        buttonPagar.setVisible(true);
        empleadosTableView.getColumns().removeAll(bonoColumna, transporteColumna, 
                totalColumna, marcarColumna, diasColumna, 
                normalesColumna, sobretiempoColumna, recargoColumna);
        empleadosTableView.getColumns().addAll(cargoColumna, diasColumna, 
                normalesColumna, sobretiempoColumna, recargoColumna, marcarColumna);
        
        rolesMultiples = false;
        checkBoxTodos.setSelected(false);
        contador.setText("");
        callFilter();
    }
    
    @FXML
    private void guardarRoles(ActionEvent event) {
        
        if (!contador.getText().equals("")) {
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
            children(new Text("¿Seguro que desea guardar los roles a estos empleados?"), hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                guardarRoles();
                dialogStage.close();
            });
            buttonNo.setOnAction((ActionEvent e) -> {
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
    
    public void guardarRoles() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new HorasEmpleadosClienteController.DataBaseThread(0);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
     public void guardarRolesCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new MaterialDesignButtonBlue("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se guardaron los roles cliente con exito."), buttonOk).
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
    
    @FXML
    public void dialogoBorrar(ActionEvent event) {
        if (!contador.getText().equals("")) {
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
            children(new Text("¿Seguro que desea borrar estas roles cliente?"),hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                borrarRoles();
                dialogStage.close();
            });
            buttonNo.setOnAction((ActionEvent e) -> {
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
    
    private void borrarRoles() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new HorasEmpleadosClienteController.DataBaseThread(1);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    @FXML
    private void crearRoles(ActionEvent event) {
        
        if (!contador.getText().equals("")) {
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
            children(new Text("¿Desea empezar la creacion de roles a estos empleados?"),
                    new Text("Los empleados con los siguiente casos seran excluidos:"),
                    new Text("1.- Ya tiene Rol con esta fecha y cliente"),
                    new Text("2.- Ya tiene pago mensual hecho"),
                    new Text("3.- No tiene ningun dia de trabajo registrado"), hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                comenzarCrearRoles();
                dialogStage.close();
            });
            buttonNo.setOnAction((ActionEvent e) -> {
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
    
    public void comenzarCrearRoles() {
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new HorasEmpleadosClienteController.DataBaseThread(2);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    public RolCliente calcularPago(Double dias, Double diasDecimo4to, Double diasJubilacion, Double normales, Double sobreTiempo, 
            Double suplementarias, Usuario empleado) {
        
        RolCliente pago = null;
        
        Double sueldoDia = empleado.getDetallesEmpleado().getSueldo() / 30d;
        Double sueldoHoras = empleado.getDetallesEmpleado().getSueldo() / 240d;
        
        normales = round(normales);
        sobreTiempo = round(sobreTiempo);
        suplementarias = round(suplementarias);
        
        // Salario
        Double totalSalarioDouble = round(sueldoHoras * normales);
        Double totalSobreTiempoDouble = round(sueldoHoras * sobreTiempo);
        Double totalRecargoDouble = round(sueldoHoras * suplementarias);
        Double totalBonoDouble = getBono(empleado);
        Double totalTransporteDouble = getTransporte(empleado);
        Double totalBonosDouble = round(totalBonoDouble + totalTransporteDouble);
        Double sueldoSinVacaciones = totalSalarioDouble + totalSobreTiempoDouble + totalRecargoDouble + totalBonoDouble + totalTransporteDouble;
        Double totalVacacionesDouble = round(getVacaciones(empleado, sueldoSinVacaciones));
        Double subTotalDouble = round(totalSalarioDouble 
                + totalSobreTiempoDouble + totalRecargoDouble 
                + totalBonosDouble);
        ////////////////////////////////////////////////////
        Double decimoTercero = round(subTotalDouble / 12d);
        Double decimoCuarto = round(getDecimoCuarto()/30d * diasDecimo4to);
        Double jubilacionPatronal = round((getActuariales(empleado)/ 360d) * diasJubilacion);
        Double aportePatronal = round(subTotalDouble * 12.15d / 100d);
        Double segurosDecimal = round(getSeguro() * dias);
        Double uniformeDecimal = round(getUniforme() * dias);
        Double ingresoTotal = round(subTotalDouble + decimoTercero 
                + decimoCuarto + decimoTercero + jubilacionPatronal 
                + aportePatronal + segurosDecimal + uniformeDecimal);
        
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
            pago.setClienteNombre(cliente.getNombre());
        pago.setCasoEspecial(false);
        pago.setDetalles("");
        
        return pago;
    }
    
    public RolCliente reCalcularPago(RolCliente rolCliente, Double bono, Double transporte) {
        
        Usuario empleado = rolCliente.getUsuario();
        
        RolCliente pago = null;
        
        Double sueldoDia = empleado.getDetallesEmpleado().getSueldo() / 30d;
        Double sueldoHoras = empleado.getDetallesEmpleado().getSueldo() / 240d;
        
        Double dias = rolCliente.getDias();
        Double normales = rolCliente.getHorasNormales();
        Double sobreTiempo = rolCliente.getHorasSobreTiempo();
        Double suplementarias = rolCliente.getHorasSuplementarias();
        
        // Salario
        Double totalSalarioDouble = round(sueldoHoras * normales);
        Double totalSobreTiempoDouble = round(sueldoHoras * sobreTiempo);
        Double totalRecargoDouble = round(sueldoHoras * suplementarias);
        Double totalBonoDouble = round(bono);
        Double totalTransporteDouble = round(transporte);
        Double totalBonosDouble = round(totalBonoDouble + totalTransporteDouble);
        Double sueldoSinVacaciones = totalSalarioDouble + totalSobreTiempoDouble + totalRecargoDouble + totalBonoDouble + totalTransporteDouble;
        Double totalVacacionesDouble = round(getVacaciones(empleado, sueldoSinVacaciones));
        Double subTotalDouble = round(totalSalarioDouble 
                + totalSobreTiempoDouble + totalRecargoDouble 
                + totalBonosDouble + totalVacacionesDouble);
        ////////////////////////////////////////////////////
        Double decimoTercero = round(subTotalDouble / 12d);
        Double decimoCuarto = rolCliente.getDecimoCuarto();
        Double jubilacionPatronal = rolCliente.getJubilacionPatronal();
        Double aportePatronal = round(subTotalDouble * 12.15d / 100d);
        Double segurosDecimal = rolCliente.getSeguros();
        Double uniformeDecimal = rolCliente.getUniformes();
        
        Double ingresoTotal = round(subTotalDouble + decimoTercero 
                + decimoCuarto + decimoTercero + jubilacionPatronal 
                + aportePatronal + segurosDecimal + uniformeDecimal);
        
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
        if (cliente!= null)
            pago.setClienteNombre(cliente.getNombre());
        pago.setCasoEspecial(false);
        pago.setDetalles("");
        
        return pago;
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
    
    public Double getDecimoCuarto() {
        if (constDecimoCuarto == null)
            constDecimoCuarto = (Constante) new ConstanteDAO().findUniqueResultByNombre(Const.DECIMO_CUARTO);
        if (constDecimoCuarto == null) {
            return 30.5d;
        } else {
            return Double.valueOf(constDecimoCuarto.getValor());
        }
    }
    
    public Double getActuariales(Usuario empleado) {
        if (actuariales == null) {
            return 0d;
        } else {
            for (Actuariales act: actuariales) {
                if (act.getUsuario().getId().equals(empleado.getId())) {
                    return act.getPrimario() + act.getSecundario();
                }
            }
        }
        return 0d;
    }
    
    public Double getBono(Usuario empleado) {
        if (bonos == null) {
            return 0d;
        } else {
            for (Bonos bon: bonos) {
                if (bon.getUsuario().getId().equals(empleado.getId())) {
                    return bon.getBono();
                }
            }
        }
        return 0d;
    }
    
    public Double getTransporte(Usuario empleado) {
        if (bonos == null) {
            return 0d;
        } else {
            for (Bonos bon: bonos) {
                if (bon.getUsuario().getId().equals(empleado.getId())) {
                    return bon.getTransporte();
                }
            }
        }
        return 0d;
    }
    
    public Double getSeguro() {
        if (seguro == null) {
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
    
    public Double getUniforme() {
        if (uniforme == null) {
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
    
    public Double getVacaciones(Usuario empleado, Double sueldoSinVacaciones) {
        if (diasVacaciones == null) {
            return 0d;
        } else {
            for (DiasVacaciones vaca: diasVacaciones) {
                if (vaca.getUsuario().getId().equals(empleado.getId())) {
                    Integer diasDerecho = vaca.getDias();
                    Double sueldoNeto = sueldoSinVacaciones;
                    Double vacaciones = (sueldoNeto / 360d) * diasDerecho.doubleValue();
                    return vacaciones;
                }
            }
        }
        return 0d;
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
        
        if (rolesMultiples)
            dialogoCancelar(null);
    }
    
    public void updateWindows() {
        closeDialogMode();
        checkBoxTodos.setSelected(false);
        contador.setText("");
    }
    
    public void updateWindowsRevisar() {
        
        empleadosTableView.setEditable(true);
        empleadosTableView.getColumns().removeAll(cargoColumna, marcarColumna);

        bonoColumna = new TableColumn("Bono");
        bonoColumna.setCellValueFactory(new PropertyValueFactory<>("bono"));
        bonoColumna.setPrefWidth(60);

        transporteColumna = new TableColumn("Transporte");
        transporteColumna.setCellValueFactory(new PropertyValueFactory<>("transporte"));
        transporteColumna.setPrefWidth(60);

        totalColumna = new TableColumn("Ingreso");
        totalColumna.setPrefWidth(60);
        totalColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {

                String monto = data.getValue().getRolCliente()
                        .getTotalIngreso().toString();

                return new ReadOnlyStringWrapper(monto); 
            }
        });

        empleadosTableView.getColumns().addAll(bonoColumna, transporteColumna, totalColumna, marcarColumna);

        data = FXCollections.observableArrayList();
        data.addAll(empleadosRol);
        empleadosTableView.setItems(data);
        
        buttonCancelar.setVisible(true);
        buttonGuardar.setVisible(true);
        buttonBorrar.setVisible(false);
        buttonPagar.setVisible(false);
        
        rolesMultiples = true;
        
        closeDialogMode();
        contarSelecciones();
        callFilter();
    }
    
    public void updateWindowsGuardado() {
        closeDialogMode();
        guardarRolesCompletado();
        dialogoCancelar(null);
    }
    
    public void updateWindowsBorrado() {
        closeDialogMode();
        borradoCompletado();
        checkBoxTodos.setSelected(false);
        contador.setText("");
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
        children(new Text("Se borraron los roles cliente con exito."), buttonOk).
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
    
    public void setCliente(Empresa empresa, Cliente cliente) throws ParseException {
        this.empresa = empresa;
        this.cliente = cliente;
        
        inicio = Fechas.getFechaActual();
        inicio.setDia("01");
        fin = inicio.plusMonths(1).minusDays(1);
           
        inicio.setToSpinner(selectorAnoDe, selectorMesDe, selectorDiaDe);
        fin.setToSpinner(selectorAnoHa, selectorMesHa, selectorDiaHa);
        fechaLabel.setText(inicio.getMonthName());
        
        setTableInfo(empresa, inicio, fin);
        
    }
    
    private void rolDePagoPorCliente(EmpleadoTable empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                
                for (Usuario usuario: usuarios) {
                    if (empleado.getId().equals(usuario.getId())) {
                        aplicacionControl.mostrarRolDePagoCliente(usuario, 
                                cliente, inicio, fin);
                    }
                }
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void setTableInfo(Empresa empresa, Fecha inicio, Fecha fin) {
        this.inicio = inicio;
        this.fin = fin;
        
        UsuarioDAO usuariosDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuariosDAO.findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), inicio));
        
        data = FXCollections.observableArrayList();
        
        Fecha fechaInicial = new Fecha(inicio.getFecha()); // dias anteriores de horas extras 
        Fecha fechaFinal = new Fecha(fin.getFecha());
        
        java.sql.Date fechaInicialExtra = fechaInicial.minusDays(7).getDate(); // dias anteriores de horas extras 
        java.sql.Date fechaFinalExtra = fechaFinal.minusDays(7).getDate();
        
        if (!usuarios.isEmpty()) {
            
            ControlDiarioDAO controlDAO = new ControlDiarioDAO();
            ControlExtrasDAO extrasDAO = new ControlExtrasDAO();
            
            List<ControlDiario> controlesDiarios;
            List<ControlExtras> controlesExtrases;
            if (cliente != null) {
                controlesDiarios = controlDAO
                        .findAllByClienteIdInDeterminateTime(cliente.getId(), fechaInicial.getFecha(), fechaFinal.getFecha());
                controlesExtrases = extrasDAO
                        .findAllByClienteIdInDeterminateTime(cliente.getId(), fechaInicialExtra, fechaFinalExtra);
                rolesCliente = new RolClienteDAO()
                   .findAllByFechaAndClienteIdAndEmpresaId(inicio.getFecha(), cliente.getId(), empresa.getId());
            } else {
                controlesDiarios = controlDAO
                        .findAllBySinClienteInDeterminateTime(fechaInicial.getFecha(), fechaFinal.getFecha());
                controlesExtrases = extrasDAO
                        .findAllBySinClienteInDeterminateTime(fechaInicialExtra, fechaFinalExtra);
                rolesCliente = new RolClienteDAO()
                   .findAllByFechaAndEmpresaIdSinCliente(inicio.getFecha(), empresa.getId());
            }
            
            ///////////////////////////////////////////////////////////////
            usuarios.stream().map((user) -> {
                Double dias = 0d;
                Double normales = 0d;
                Double sobreTiempo = 0d;
                Double suplementarias = 0d;
                Integer medioDias = 0;
                
                int descansosMedicos = 0;
                for (ControlDiario control: controlesDiarios) {
                    if (Objects.equals(user.getId(), control.getUsuario().getId())) {
                        
                        if (new Fecha(control.getFecha()).afterEquals(inicio)) {
                        
                            if (control.getCaso().equalsIgnoreCase(Const.DM)) {
                                descansosMedicos++;
                            } else {
                                descansosMedicos = 0;
                            }
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
                        }
                    }
                }
                for (ControlExtras controlExtra: controlesExtrases) {
            
                    if (Objects.equals(user.getId(), controlExtra.getUsuario().getId())) {

                        if (controlExtra.getCaso().equalsIgnoreCase(Const.TRABAJO)
                                || controlExtra.getCaso().equalsIgnoreCase(Const.LIBRE)
                                || controlExtra.getCaso().equalsIgnoreCase(Const.CM)) {

                            sobreTiempo += controlExtra.getSobretiempo();
                            suplementarias += controlExtra.getRecargo();
                        } 
                    }
                }
                
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
                empleado.setDias(round(dias));
                empleado.setDiasInt(Numeros.roundInt(dias));
                empleado.setHoras(round(normales));
                empleado.setSobreTiempo(round(sobreTiempo));
                empleado.setSuplementarias(round(suplementarias));
                for (RolCliente rol: rolesCliente) {
                    if (Objects.equals(user.getId(), rol.getUsuario().getId())) {
                        empleado.setHaveRolCliente(true);
                    }
                }
                return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
            empleadosTableView.setItems(data);
        }
        
        callFilter();
    }
    
    private void callFilter() {
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        buttonCancelar.setVisible(false);
        buttonGuardar.setVisible(false);
        buttonBorrar.setVisible(true);
        buttonPagar.setVisible(true);
        
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
        buttonBorrar.setTooltip(
            new Tooltip("Borrar")
        );
        buttonBorrar.setOnMouseEntered((MouseEvent t) -> {
            buttonBorrar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/borrar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonBorrar.setOnMouseExited((MouseEvent t) -> {
            buttonBorrar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/borrar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonPagar.setTooltip(
            new Tooltip("Generar roles")
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
        buttonCancelar.setTooltip(
            new Tooltip("Cancelar")
        );
        buttonCancelar.setOnMouseEntered((MouseEvent t) -> {
            buttonCancelar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cancelar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonCancelar.setOnMouseExited((MouseEvent t) -> {
            buttonCancelar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cancelar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        buttonGuardar.setTooltip(
            new Tooltip("Guardar roles")
        );
        buttonGuardar.setOnMouseEntered((MouseEvent t) -> {
            buttonGuardar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/guardar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonGuardar.setOnMouseExited((MouseEvent t) -> {
            buttonGuardar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/guardar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        empleadoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getApellido() 
                        + " " + data.getValue().getNombre());
            }
        });
        
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxMarcar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                setGraphic(checkBoxMarcar);
                if (checkBoxMarcar != null) {
                    checkBoxMarcar.setSelected(empleadoTable.getAgregar());
                }
                
                checkBoxMarcar.setOnAction(event -> {
                    empleadoTable.setAgregar(checkBoxMarcar.isSelected());
                    contarSelecciones();
                });
                
                if (empleadoTable.getHaveRolCliente()) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
            }
        });
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        diasColumna.setCellValueFactory(new PropertyValueFactory<>("diasInt"));
        
        normalesColumna.setCellValueFactory(new PropertyValueFactory<>("horas"));
        
        recargoColumna.setCellValueFactory(new PropertyValueFactory<>("suplementarias"));
        
        sobretiempoColumna.setCellValueFactory(new PropertyValueFactory<>("sobreTiempo"));
        
        empleadosTableView.setRowFactory((Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    if (!rolesMultiples) {
                        rolDePagoPorCliente(rowData);
                    }
                }
            });
            return row ;
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
    
    public void contarSelecciones() {
        List<EmpleadoTable> empleadosTable = new ArrayList<>();
        for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
            if (empleado.getAgregar())
                empleadosTable.add(empleado);
        }
        int count = empleadosTable.size();
        switch (count) {
            case 0:
                contador.setText("");
                break;
            case 1:
                contador.setText("1 empleado seleccionado");
                break;
            default:
                contador.setText(count + " empleados seleccionados");
                break;
        }
        if (((List<EmpleadoTable>) data).size() == count) {
            checkBoxTodos.setSelected(true);
        } else {
            checkBoxTodos.setSelected(false);
        }
    }
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
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
        public final Integer BORRAR = 1;
        public final Integer REVISAR = 2;
        
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
                        if (Objects.equals(opcion, GUARDAR)) {
                            guardar();
                        } else if (Objects.equals(opcion, BORRAR)) {
                            borrar();
                        } else if (Objects.equals(opcion, REVISAR)) {
                            revisar();
                        } 
                    }

             }, 1000, 1000);
            
        }
        
        private void guardar() {
            try {
                
                for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) data) {
                    if (empleadoTable.getAgregar()) {
                        new RolClienteDAO().save(empleadoTable.getRolCliente());

                        // Registro para auditar
                        String detalles = "genero un rol al empleado " 
                            + empleadoTable.getNombre() + " " + empleadoTable.getApellido() + " para el cliente " 
                                + empleadoTable.getRolCliente().getClienteNombre();
                        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        updateWindowsGuardado();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        error();
                    }
                });
            }
 
        }
        
        private void borrar() {
            try {
                ///////////////////Buscando empleado marcados///////////////////////////
                List<EmpleadoTable> empleadosRol = new ArrayList<>();
                List<EmpleadoTable> empleadosParaRemover = new ArrayList<>();
                for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) data) {
                    if (empleadoTable.getAgregar()) {
                        empleadosRol.add(empleadoTable);
                    }
                }
                System.out.println(empleadosRol.size() + " empleados marcados");
                ///////////// Removiendo empleado que tenga rol individual//////////////
                List<RolIndividual> rolIndividuales = new RolIndividualDAO()
                        .findAllByFechaAndEmpresaId(inicio.getFecha(), empresa.getId());
                for (EmpleadoTable empleadoTable: empleadosRol) {
                    for (RolIndividual rolIndividual: rolIndividuales) {
                        if (empleadoTable.getId().equals(rolIndividual.getUsuario().getId())) {
                            empleadosParaRemover.add(empleadoTable);
                        }
                    }
                }
                System.out.println(empleadosParaRemover.size() + " empleados para remover de los marcados");
                empleadosRol.removeAll(empleadosParaRemover);
                empleadosParaRemover = new ArrayList<>();

                //////////////////Agregando roles que se pueden borrar//////////////////
                List<RolCliente> rolesCliente;
                if (cliente != null)
                    rolesCliente = new RolClienteDAO()
                        .findAllByFechaAndClienteId(inicio.getFecha(), cliente.getId());
                else 
                    rolesCliente = new RolClienteDAO()
                        .findAllByFechaSinCliente(inicio.getFecha());

                System.out.println(rolesCliente.size()+" encontrados en db");
                
                List<RolCliente> rolesClienteDelete = new ArrayList<>();
                for (EmpleadoTable empleadoTable: empleadosRol) {
                    for (RolCliente rolCliente: rolesCliente) {
                        if (empleadoTable.getId().equals(rolCliente.getUsuario().getId())) {
                            rolesClienteDelete.add(rolCliente);
                        }
                    }
                }
                for (RolCliente rolCliente: rolesClienteDelete) {
                    new RolClienteDAO().delete(rolCliente);
                    HibernateSessionFactory.getSession().flush();
                }
                System.out.println(rolesClienteDelete.size()+" para borrar");
                setTableInfo(empresa, inicio, fin);
                
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        updateWindowsBorrado();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        error();
                    }
                });
            }
        }
        
        private void revisar() {
            
            try {
            
                empleadosRol = new ArrayList<>();
                List<EmpleadoTable> empleadosParaRemover = new ArrayList<>();

                for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) data) {
                    if (empleadoTable.getAgregar()) {
                        empleadosRol.add(empleadoTable);
                    }
                }
                System.out.println(empleadosRol.size() + " empleados marcados");
                List<RolIndividual> rolIndividuales = new RolIndividualDAO()
                        .findAllByFechaAndEmpresaId(inicio.getFecha(), empresa.getId());

                for (EmpleadoTable empleadoTable: empleadosRol) {
                    for (RolIndividual rolIndividual: rolIndividuales) {
                        if (empleadoTable.getId().equals(rolIndividual.getUsuario().getId())) {
                            empleadosParaRemover.add(empleadoTable);
                        }
                    }
                }
                System.out.println(empleadosParaRemover.size() + " empleados para remover de los marcados");
                empleadosRol.removeAll(empleadosParaRemover);
                empleadosParaRemover = new ArrayList<>();

                List<RolCliente> rolesCliente;
                if (cliente != null)
                    rolesCliente = new RolClienteDAO()
                        .findAllByFechaAndClienteId(inicio.getFecha(), cliente.getId());
                else 
                    rolesCliente = new RolClienteDAO()
                        .findAllByFechaSinCliente(inicio.getFecha());

                for (EmpleadoTable empleadoTable: empleadosRol) {
                    for (RolCliente rolCliente: rolesCliente) {
                        if (empleadoTable.getId().equals(rolCliente.getUsuario().getId())) {
                            empleadosParaRemover.add(empleadoTable);
                        }
                    }
                }
                empleadosRol.removeAll(empleadosParaRemover);
                empleadosParaRemover = new ArrayList<>();
                
                Fecha fechaInicial = new Fecha(inicio.getFecha()); 
                Fecha fechaFinal = new Fecha(fin.getFecha());
                
                java.sql.Date fechaInicialExtra = fechaInicial.minusDays(7).getDate(); // dias anteriores de horas extras 
                java.sql.Date fechaFinalExtra = fechaFinal.minusDays(7).getDate();
        

                List<ControlDiario> controlesEmpleados;
                List<ControlExtras> controlesExtras;
                if (cliente != null) {
                    controlesEmpleados = new ControlDiarioDAO()
                            .findAllByClienteIdInDeterminateTime(cliente.getId(), fechaInicial.getFecha(), fechaFinal.getFecha()); 
                    controlesExtras = new ControlExtrasDAO()
                        .findAllByClienteIdInDeterminateTime(cliente.getId(), fechaInicialExtra, fechaFinalExtra);
                }
                else {
                    controlesEmpleados = new ControlDiarioDAO()
                            .findAllBySinClienteInDeterminateTime(fechaInicial.getFecha(), fechaFinal.getFecha());
                    controlesExtras = new ControlExtrasDAO()
                        .findAllBySinClienteInDeterminateTime(fechaInicialExtra, fechaFinalExtra);
                }
                List<Usuario> usuariosRol = new ArrayList<>();
                for (ControlDiario controlEmpleado: controlesEmpleados) {
                    Boolean agregar = true;
                    for (Usuario usuario: usuariosRol) {
                        if (usuario.getId().equals(controlEmpleado.getUsuario().getId())) {
                            agregar = false;
                        }
                    }
                    if (agregar)
                        usuariosRol.add(controlEmpleado.getUsuario());
                }

                for (EmpleadoTable empleadoTable: empleadosRol) {
                    Boolean agregar = true;
                    for (Usuario usuario: usuariosRol) {
                        if (empleadoTable.getId().equals(usuario.getId())) {
                            agregar = false;
                        }
                    }
                    if (agregar)
                        empleadosParaRemover.add(empleadoTable);
                }
                empleadosRol.removeAll(empleadosParaRemover);
                empleadosParaRemover = new ArrayList<>();

                ///////////////////////// Obteniendo dias de vacaciones de todos los empleados
                diasVacaciones = new ArrayList<>();
                diasVacaciones.addAll((ArrayList<DiasVacaciones>) 
                    new DiasVacacionesDAO().findAllByEmpresaId(empresa.getId()));
                ///////////////////////// Obteniendo bono y transporte de todos los empleados
                bonos = new ArrayList<>();
                if (cliente != null) {
                    bonos.addAll((ArrayList<Bonos>) new BonosDAO()
                            .findAllByClienteIdAndEmpresaId(cliente.getId(), empresa.getId(), inicio.getFecha()));
                } else {
                    bonos.addAll((ArrayList<Bonos>) new BonosDAO()
                            .findAllByClienteNullAndEmpresaId(empresa.getId(), inicio.getFecha()));
                }
                ///////////////////////// Obteniendo los actuariales de todos los empleados
                actuariales = new ArrayList<>();
                actuariales.addAll((ArrayList<Actuariales>) 
                    new ActuarialesDAO().findAllByEmpresaId(empresa.getId()));
                
                for (EmpleadoTable empleadoTable: empleadosRol) {
                    Double dias = 0d;
                    Double diasDecimo4to = 0d;
                    Double diasJubilacion = 0d;
                    Double normales = 0d;
                    Double sobreTiempo = 0d;
                    Double suplementarias = 0d;
                    Integer medioDias = 0;
                    Usuario usuario = null;

                    int descansosMedicos = 0;
                    for (ControlDiario control: controlesEmpleados) {
                        if (Objects.equals(empleadoTable.getId(), control.getUsuario().getId())) {
                            usuario = control.getUsuario();

                            if (control.getCaso().equalsIgnoreCase(Const.DM)) {
                                descansosMedicos++;
                            } else {
                                descansosMedicos = 0;
                            }

                            if (control.getMedioDia()) {
                                medioDias++;
                            }

                            if (control.getCaso().equalsIgnoreCase(Const.TRABAJO)
                                    || control.getCaso().equalsIgnoreCase(Const.LIBRE)
                                    || control.getCaso().equalsIgnoreCase(Const.CM)) {

                                dias += 1;
                                normales = control.getMedioDia()? normales+4 : normales+8;
                                //sobreTiempo += control.getSobretiempo();
                                //suplementarias += control.getRecargo();

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
                        }
                    }
                    
                    for (ControlExtras control: controlesExtras) {
                        if (Objects.equals(empleadoTable.getId(), control.getUsuario().getId())) {

                            if (control.getCaso().equalsIgnoreCase(Const.TRABAJO)
                                    || control.getCaso().equalsIgnoreCase(Const.LIBRE)
                                    || control.getCaso().equalsIgnoreCase(Const.CM)) {

                                sobreTiempo += control.getSobretiempo();
                                suplementarias += control.getRecargo();
                            } 
                            
                        }
                    }
                    
                    Actuariales actuariales = new ActuarialesDAO().findByEmpleadoId(empleadoTable.getId());
                    empleadoTable.setActuariales(actuariales);
                    RolCliente rolCliente = calcularPago(dias, diasDecimo4to, diasJubilacion, normales, sobreTiempo, suplementarias, usuario);
                    empleadoTable.setRolCliente(rolCliente);
                    empleadoTable.setBono(rolCliente.getBono().toString());
                    empleadoTable.setTransporte(rolCliente.getTransporte().toString());
                    for (Bonos bon: bonos) {
                        if (bon.getUsuario().getId().equals(usuario.getId())) {
                            empleadoTable.setBonos(bon);
                        }
                    }
                }

                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        updateWindowsRevisar();
                    }
                });
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
