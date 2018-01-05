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
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ClienteDAO;
import hibernate.dao.HorarioDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cliente;
import hibernate.model.Empresa;
import hibernate.model.Horario;
import hibernate.model.RolCliente;
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
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.joda.time.DateTime;
import java.sql.Time;
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.ControlDiarioDAO;
import hibernate.model.ControlDiario;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Yornel
 */
public class HorasEmpleadosPorDiaController implements Initializable {
    
    public AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    @FXML
    private TableColumn cedulaColumna;
    
    @FXML
    private TableColumn empleadoColumna;
    
    @FXML
    private TableColumn cargoColumna;
    
    @FXML
    private TableColumn normalesColumna;
    
    @FXML
    private TableColumn sobretiempoColumna;
    
    @FXML
    private TableColumn recargoColumna;
    
     @FXML
    private TableColumn horarioColumna;
    
    @FXML
    private TableColumn clienteColumna;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> marcarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonHorario;
    
    @FXML
    private Button buttonBorrar;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
   
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private TextField filterField;
    
    @FXML
    private Label fechaLabel;
    
    @FXML
    private CheckBox checkBoxMarcarTodos;
    
    @FXML
    private Label contador;
    
    @FXML
    private ChoiceBox selectorDia;
    
    @FXML
    private ChoiceBox selectorMes;
    
    @FXML
    private ChoiceBox selectorAno;
    
    private Fecha fecha;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    private Stage stagePrincipal;
    
    List<ControlDiario> controlEmpleados;
    
    ArrayList<ControlDiario> ultimosRegistros;
    
    List<Horario> horarios;
    
    List<Cliente> clientes;
    
    List<RolCliente> rolClientes;
    
    Dialog<Void> dialog;
    
    @FXML
    private Button leyenda1;
    
    @FXML
    private Button leyenda2;
    
    @FXML
    private Button leyenda3;
    
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
    public void dialogoImprimir(ActionEvent event) {
        
    }
    
    @FXML
    public void cambiarHorarioVarios(ActionEvent event) throws IOException {
        if (!contador.getText().equals(""))
            abrirCambioMultiple();
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
    
    public void abrirCambioMultiple() throws IOException {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                
                ArrayList<Usuario> usuariosMarcados = new ArrayList<>();
                ArrayList<ControlDiario> controlsMarcados = new ArrayList<>();
                for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
                    if (empleado.getAgregar()) {
                        usuariosMarcados.add(getUsuario(empleado.getId()));
                        ControlDiario ce = getControlEmpleado(empleado.getId());
                        if (ce != null)
                            controlsMarcados.add(ce);
                    }
                }
                
                FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorarioEmpleado.fxml"));
                AnchorPane ventanaRolDePago = (AnchorPane) loader.load();
                Stage ventana = new Stage();
                ventana.setTitle(contador.getText());
                String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
                ventana.getIcons().add(new Image(stageIcon));
                ventana.setResizable(false);
                ventana.initOwner(stagePrincipal);
                Scene scene = new Scene(ventanaRolDePago);
                ventana.setScene(scene);
                HorarioEmpleadoController controller = loader.getController();
                controller.setStagePrincipal(ventana);
                controller.setHorasEmpleadosPorDia(this);
                controller.setEmpleados(usuariosMarcados, controlsMarcados, fecha);
                ventana.show();
  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public ControlDiario getControlEmpleado(Integer userId) {
       for (ControlDiario controlEmpleado: controlEmpleados) {
            if (controlEmpleado.getUsuario().getId().equals(userId)) 
                return controlEmpleado;
        }
        return null; 
    }
    
    public Usuario getUsuario(Integer id) {
        for (Usuario usuario: usuarios) {
            if (usuario.getId().equals(id)) 
                return usuario;
        }
        return null;
    }
    
    @FXML
    public void mostrarFecha(ActionEvent event) {
        fecha = new Fecha(selectorAno, selectorMes, selectorDia);
        setTableInfo(fecha);
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        fecha = fecha.plusDays(1);
        fecha.setToSpinner(selectorAno, selectorMes, selectorDia);
        setTableInfo(fecha);
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        fecha = fecha.minusDays(1);
        fecha.setToSpinner(selectorAno, selectorMes, selectorDia);
        setTableInfo(fecha);
    }
    
    @FXML
    public void marcarTodos(ActionEvent event) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxMarcarTodos.isSelected()) {
                if (!empleadoTable.getHaveRolCliente())
                    empleadoTable.setAgregar(true);
            } else {
                empleadoTable.setAgregar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
        contarSelecciones();
    }
    
    @FXML
    public void borrarHorarios(ActionEvent event) {
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
            children(new Text("¿Seguro que desea borrar estos horarios?"), hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                borrarHorarios();
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
            children(new Text("No has seleccionado ningun horario para eliminar."), 
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
    
    public void borrarHorarios() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new HorasEmpleadosPorDiaController.DataBaseThread(1);
        executor.execute(worker);
        executor.shutdown();

        loadingMode();
    }
    
    public void updateWindows() {
        closeDialogMode();
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
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        horarios = (List<Horario>) new HorarioDAO().findAll();
        clientes = new ClienteDAO().findAllActivo();
           
        fecha = Fechas.getFechaActual();
        
        fecha.setToSpinner(selectorAno, selectorMes, selectorDia);
        
        setTableInfo(fecha);
        
    }
    
    private void horarioEmpleado(Usuario empleado, Boolean editable) throws IOException {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                
                ControlDiario controlEmpleado = null;
                for (ControlDiario control: controlEmpleados) {
                    if (Objects.equals(empleado.getId(), control.getUsuario().getId())) {
                        controlEmpleado = control;
                    }
                }
                if (controlEmpleado == null) {
                    for (ControlDiario control: ultimosRegistros) {
                        if (Objects.equals(empleado.getId(), control.getUsuario().getId())) {
                            controlEmpleado = control;
                        }
                    }
                }

                FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorarioEmpleado.fxml"));
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
                HorarioEmpleadoController controller = loader.getController();
                controller.setStagePrincipal(ventana);
                controller.setHorasEmpleadosPorDia(this);
                controller.setStage(ventana);
                controller.setEmpleado(empleado, controlEmpleado, fecha, editable);
                ventana.show();
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void cambiarControlEmpleado(ControlDiario controlDiario) {
        if (fecha.getFecha().equals(controlDiario.getFecha())) {
            Usuario user = controlDiario.getUsuario();
            for (EmpleadoTable empleadoTable: data) {
                if(Objects.equals(empleadoTable.getId(), user.getId())) {
                    EmpleadoTable empleado = new EmpleadoTable();
                    Double dias = 0d;
                    Double normales = 0d;
                    Double sobreTiempo = 0d;
                    Double suplementarias = 0d;
                    dias += 1;
                    normales += controlDiario.getNormales();
                    sobreTiempo += controlDiario.getSobretiempo();
                    suplementarias += controlDiario.getRecargo();
                    
                    if (controlDiario.getCliente() != null)
                        empleado.setCliente(controlDiario.getCliente().getNombre());
                    
                    empleado.setHorario("No guardado");
                    if (controlDiario.getCaso().equals(Const.LIBRE)) {
                        empleado.setHorario("Libre");
                    } else if (controlDiario.getCaso().equals(Const.FALTA)) {
                        empleado.setHorario("Falta");
                    } else if (controlDiario.getCaso().equals(Const.VACACIONES)) {
                        empleado.setHorario("Vacaciones");
                    } else if (controlDiario.getCaso().equals(Const.PERMISO)) {
                        empleado.setHorario("Permiso");
                    } else if (controlDiario.getCaso().equals(Const.DM)) {
                        empleado.setHorario("D. Medico");
                    } else if (controlDiario.getCaso().equals(Const.CM)) {
                        empleado.setHorario("C. Medica");
                    } else {
                       empleado.setHorario(getLapso(controlDiario.getEntrada(), controlDiario.getSalida()));
                    }
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
                   empleado.setDias(dias);
                   empleado.setHoras(round(normales));
                   empleado.setSobreTiempo(round(sobreTiempo));
                   empleado.setSuplementarias(round(suplementarias));
                   data.set(data.indexOf(empleadoTable), empleado);
                   break;
                }
            }
            
            Boolean agregar = true;
            for (ControlDiario control: controlEmpleados) {
                if (control.getUsuario().getId()
                        .equals(controlDiario.getUsuario().getId())) {
                    controlEmpleados.set(controlEmpleados
                            .indexOf(control), controlDiario);
                    agregar = false;
                    break;
                }
            }
            if (agregar)
                controlEmpleados.add(controlDiario);
        }
    }
    
    public void setTableInfo(Fecha fecha) {
        this.fecha = fecha;
        contador.setText("");
        checkBoxMarcarTodos.setSelected(false);
        
        fechaLabel.setText(Fechas.getFechaConMes(fecha));
        
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO().findAllByEmpresaIdActivoIFVISIBLE(empresa.getId(), fecha));
        
        data = FXCollections.observableArrayList();
        
        if (!usuarios.isEmpty()) {
            
            ControlDiarioDAO controlDAO = new ControlDiarioDAO();
            
            controlEmpleados = controlDAO
                    .findAllByFechaAndEmpresaId(fecha.getFecha(), empresa.getId());
            
            RolClienteDAO rolClienteDAO = new RolClienteDAO();
            rolClientes = rolClienteDAO
                    .findAllByEntreFechaAndEmpresaId(fecha.getFecha(), empresa.getId());
            
            usuarios.stream().map((user) -> {
                EmpleadoTable empleado = new EmpleadoTable();
                Double normales = 0d;
                Double sobreTiempo = 0d;
                Double suplementarias = 0d;
                for (ControlDiario control: controlEmpleados) {
                    if (Objects.equals(user.getId(), control.getUsuario().getId())) {
                        normales += control.getNormales();
                        sobreTiempo += control.getSobretiempo();
                        suplementarias += control.getRecargo();
                        if (control.getCliente() != null)
                            empleado.setCliente(control.getCliente().getNombre());
                        
                        empleado.setHoras(round(normales));
                        empleado.setSobreTiempo(round(sobreTiempo));
                        empleado.setSuplementarias(round(suplementarias));
                        
                        empleado.setHorario("No guardado");
                        if (control.getCaso().equals(Const.LIBRE)) {
                            empleado.setHorario("Libre");
                        } else if (control.getCaso().equals(Const.FALTA)) {
                            empleado.setHorario("Falta");
                        } else if (control.getCaso().equals(Const.VACACIONES)) {
                            empleado.setHorario("Vacaciones");
                        } else if (control.getCaso().equals(Const.PERMISO)) {
                            empleado.setHorario("Permiso");
                        } else if (control.getCaso().equals(Const.DM)) {
                            empleado.setHorario("D. Medico");
                        } else if (control.getCaso().equals(Const.CM)) {
                            empleado.setHorario("C. Medica");
                        } else {
                            empleado.setHorario(getLapso(control.getEntrada(), 
                                    control.getSalida()));
                        }
                    }
                }
                for (RolCliente rolCliente: rolClientes) {
                    if (rolCliente.getUsuario().getId().equals(user.getId())) {
                        empleado.setHaveRolCliente(true);
                        break;
                    }
                }
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
                return empleado;
            }).forEach((empleado) -> {
                data.add(empleado);
            });
           empleadosTableView.setItems(data);
        }
        
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
                } else if (empleado.getCliente().toLowerCase().contains(lowerCaseFilter)) {
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
            } else if (empleado.getCliente().toLowerCase().contains(lowerCaseFilter)) {
                return true; // Filter matches last name.
            } 
            return false; // Does not match.
        });
    }
    
    public String getLapso(Time entrada, Time salida) {
        
        String lapso = Fechas.getHora(entrada)
                        +" - "+Fechas.getHora(salida);
        
        return lapso;
    }
    
    public void cambiarUsuarioUltimoRegistro(ControlDiario controlDiario) {
        Boolean agregar = true;
        for (ControlDiario control: ultimosRegistros) {
            if (controlDiario.getUsuario().getId()
                    .equals(control.getUsuario().getId())) {
                ultimosRegistros
                        .set(ultimosRegistros.indexOf(control), controlDiario);
                agregar = false;
            }
        }
        if (agregar)
            ultimosRegistros.add(controlDiario);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
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
        buttonImprimir.setTooltip(
            new Tooltip("Imprimir")
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
        buttonImprimir.setVisible(false);  // ocultado porque no se ha hecho el reporte
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
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        normalesColumna.setCellValueFactory(new PropertyValueFactory<>("horas"));
        
        recargoColumna.setCellValueFactory(new PropertyValueFactory<>("suplementarias"));
        
        sobretiempoColumna.setCellValueFactory(new PropertyValueFactory<>("sobreTiempo"));
        
        horarioColumna.setCellValueFactory(new PropertyValueFactory<>("horario"));
        
        clienteColumna.setCellValueFactory(new PropertyValueFactory<>("cliente"));
       
        marcarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        marcarColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxSeleccionar = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleadoTable, boolean empty) {
                super.updateItem(empleadoTable, empty);

                if (empleadoTable == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }
                
                setGraphic(checkBoxSeleccionar);
                if (checkBoxSeleccionar != null) {
                    checkBoxSeleccionar.setSelected(empleadoTable.getAgregar());
                }
                checkBoxSeleccionar.setOnAction(event -> {
                    if (!empleadoTable.getHaveRolCliente())
                        empleadoTable.setAgregar(checkBoxSeleccionar.isSelected());
                    else {
                        checkBoxSeleccionar.setSelected(false);
                    }
                    contarSelecciones();
                });
                
                if (empleadoTable.getHaveRolCliente()) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else if (empleadoTable.getProblem()) {
                    getTableRow().setStyle("-fx-background-color:lightcoral"); 
                } else if (empleadoTable.getHoras() == null) {
                    getTableRow().setStyle("-fx-background-color:#dddddd");
                } else {
                    getTableRow().setStyle("");
                }
            }
        });
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    try {
                        EmpleadoTable rowData = row.getItem();
                        horarioEmpleado(usuarios.get(data.indexOf(rowData))
                                , !rowData.getHaveRolCliente());
                    } catch (IOException ex) {
                        Logger.getLogger(HorasEmpleadosPorDiaController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return row ;
        });
        
        ultimosRegistros = new ArrayList<>();
        ultimosRegistros.addAll(new ControlDiarioDAO().findAllByUltimosRegistros());
        
        leyenda1.setTooltip(new Tooltip("Ya se creo el rol cliente"));
        leyenda2.setTooltip(new Tooltip("Dia sin horario"));
        leyenda3.setTooltip(new Tooltip("Dia con horario"));
        
        selectorDia.setItems(Fechas.arraySpinnerDia());
        selectorMes.setItems(Fechas.arraySpinnerMes());
        selectorAno.setItems(Fechas.arraySpinnerAno());
        /*
        selectorAno.getSelectionModel().select(String.valueOf(new DateTime().getYear()));
        selectorMes.getSelectionModel().select(String.valueOf(new DateTime().getMonthOfYear()));
        if (new DateTime().getDayOfMonth() > 30) {
            selectorDia.getSelectionModel().select("30");
        } else {
            selectorDia.getSelectionModel().select(String.valueOf(new DateTime().getDayOfMonth()));
        }*/
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
    }
    
    public void marcarUsuariosConRol(ArrayList<Usuario> usuariosToChange) {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            for (Usuario usuario: usuariosToChange) {
                if (usuario.getId().equals(empleadoTable.getId())) {
                    empleadoTable.setProblem(true);
                    data.set(data.indexOf(empleadoTable), empleadoTable);
                }
            }
        }
    }
                
    public Integer verificarHorario(EmpleadoTable emp) {
        if (emp.getHoras() == 8d && emp.getSobreTiempo() == 0d && emp.getSuplementarias() == 0d) {
            return 0;
        } else if (emp.getHoras() == 8d && emp.getSobreTiempo() == 0d && emp.getSuplementarias() == 0.75d) {
            return 1;
        } else if (emp.getHoras() == 8d && emp.getSobreTiempo() == 0d && emp.getSuplementarias() == 2d) {
            return 2;
        } else if (emp.getHoras() == 8d && emp.getSobreTiempo() == 4d && emp.getSuplementarias() == 2d) {
            return 3;
        } else if (emp.getHoras() == 8d && emp.getSobreTiempo() == 4d && emp.getSuplementarias() == 5.7d) {
            return 4;
        }
        
        return 999;
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
                for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
                    if (empleado.getAgregar()) {
                        ControlDiario controlEmpleadoDelete = 
                                getControlEmpleado(empleado.getId());
                        if (controlEmpleadoDelete != null) {
                            new ControlDiarioDAO().delete(controlEmpleadoDelete);
                            controlEmpleados.remove(controlEmpleadoDelete);
                            empleado.setHoras(null);
                            empleado.setSobreTiempo(null);
                            empleado.setSuplementarias(null);
                            empleado.setHorario("");
                            empleado.setCliente("");
                            data.set(data.indexOf(empleado), empleado);
                            HibernateSessionFactory.getSession().flush();

                            // Registro para auditar
                            String detalles = "elimino el horario del empleado " 
                                    + empleado.getApellido() + " " + empleado.getNombre()
                                    + " del dia " + Fechas.getFechaConMes(fecha);
                            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
                        }
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        updateWindows();
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
