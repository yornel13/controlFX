/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButtonBlue;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.dao.ClienteDAO;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.HorarioDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlEmpleado;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class HorasEmpleadosPorDiaController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
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
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML 
    private DatePicker pickerDia;
    
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
    
    private Timestamp fecha;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    private Stage stagePrincipal;
    
    List<ControlEmpleado> controlEmpleados;
    
    ArrayList<ControlEmpleado> ultimosRegistros;
    
    List<Horario> horarios;
    
    List<Cliente> clientes;
    
    List<RolCliente> rolClientes;
    
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
                ArrayList<ControlEmpleado> controlsMarcados = new ArrayList<>();
                for (EmpleadoTable empleado: (List<EmpleadoTable>) data) {
                    if (empleado.getAgregar()) {
                        usuariosMarcados.add(getUsuario(empleado.getId()));
                        ControlEmpleado ce = getControlEmpleado(empleado.getId());
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
    
    public ControlEmpleado getControlEmpleado(Integer userId) {
       for (ControlEmpleado controlEmpleado: controlEmpleados) {
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
        fecha = Timestamp.valueOf(pickerDia.getValue().atStartOfDay());
        setTableInfo(fecha);
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDia.setValue(pickerDia.getValue().plusDays(1));
        fecha = Timestamp.valueOf(pickerDia.getValue().atStartOfDay());
        setTableInfo(fecha);
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        pickerDia.setValue(pickerDia.getValue().minusDays(1));
        fecha = Timestamp.valueOf(pickerDia.getValue().atStartOfDay());
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
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        horarios = (List<Horario>) new HorarioDAO().findAll();
        clientes = new ClienteDAO().findAllActivo();
        
        DateTime dateTime = new DateTime(getToday().getTime());
           
        fecha = new Timestamp(dateTime.getMillis());
        
        pickerDia.setValue(Fechas.getDateFromTimestamp(fecha));
        
        setTableInfo(fecha);
        
    }
    
    private void horarioEmpleado(Usuario empleado, Boolean editable) throws IOException {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.HORAS, Permisos.Nivel.VER)) {
                
                ControlEmpleado controlEmpleado = null;
                for (ControlEmpleado control: controlEmpleados) {
                    if (Objects.equals(empleado.getId(), control.getUsuario().getId())) {
                        controlEmpleado = control;
                    }
                }
                if (controlEmpleado == null) {
                    for (ControlEmpleado control: ultimosRegistros) {
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
    
    public void cambiarControlEmpleado(ControlEmpleado controlEmpleado) {
        if (fecha.getTime() == controlEmpleado.getFecha().getTime()) {
            Usuario user = controlEmpleado.getUsuario();
            for (EmpleadoTable empleadoTable: data) {
                if(Objects.equals(empleadoTable.getId(), user.getId())) {
                    EmpleadoTable empleado = new EmpleadoTable();
                    Integer dias = 0;
                    Double normales = 0d;
                    Double sobreTiempo = 0d;
                    Double suplementarias = 0d;
                    dias += 1;
                    normales += controlEmpleado.getNormales();
                    sobreTiempo += controlEmpleado.getSobretiempo();
                    suplementarias += controlEmpleado.getRecargo();
                    
                    if (controlEmpleado.getCliente() != null)
                        empleado.setCliente(controlEmpleado.getCliente().getNombre());
                    
                    empleado.setHorario("No guardado");
                    if (controlEmpleado.getLibre()) {
                        empleado.setHorario("Libre");
                    } else if (controlEmpleado.getFalta()) {
                        empleado.setHorario("Falta");
                    } else {
                        for (Horario horario: horarios) {
                            if (horario.getNormales().equals(controlEmpleado.getNormales())
                                        && horario.getRecargo().equals(controlEmpleado.getRecargo())
                                        && horario.getSobretiempo().equals(controlEmpleado.getSobretiempo())) {
                                empleado.setHorario(getLapso(horario));
                                break;
                            }
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
                   empleado.setDias(dias);
                   empleado.setHoras(round(normales));
                   empleado.setSobreTiempo(round(sobreTiempo));
                   empleado.setSuplementarias(round(suplementarias));
                   data.set(data.indexOf(empleadoTable), empleado);
                   break;
                }
            }
            
            Boolean agregar = true;
            for (ControlEmpleado control: controlEmpleados) {
                if (control.getUsuario().getId()
                        .equals(controlEmpleado.getUsuario().getId())) {
                    controlEmpleados.set(controlEmpleados
                            .indexOf(control), controlEmpleado);
                    agregar = false;
                    break;
                }
            }
            if (agregar)
                controlEmpleados.add(controlEmpleado);
        }
    }
    
    public void setTableInfo(Timestamp fecha) {
        this.fecha = fecha;
        contador.setText("");
        checkBoxMarcarTodos.setSelected(false);
        
        DateTime dateTime = new DateTime(fecha);
        String dia = dateTime.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar
                                    .DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String output = dia.substring(0, 1).toUpperCase() + dia.substring(1);
        
        fechaLabel.setText(output + " " + Fechas.getFechaConMes(fecha));
        
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO().findAllByEmpresaIdActivo(empresa.getId()));
        
        data = FXCollections.observableArrayList();
        
        if (!usuarios.isEmpty()) {
            
            ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
            
            controlEmpleados = controlDAO
                    .findAllByFechaAndEmpresaId(fecha, empresa.getId());
            
            RolClienteDAO rolClienteDAO = new RolClienteDAO();
            rolClientes = rolClienteDAO
                    .findAllByEntreFechaAndEmpresaId(fecha, empresa.getId());
            
            usuarios.stream().map((user) -> {
                EmpleadoTable empleado = new EmpleadoTable();
                Integer dias = 0;
                Double normales = 0d;
                Double sobreTiempo = 0d;
                Double suplementarias = 0d;
                for (ControlEmpleado control: controlEmpleados) {
                    if (Objects.equals(user.getId(), control.getUsuario().getId())) {
                        dias += 1;
                        normales += control.getNormales();
                        sobreTiempo += control.getSobretiempo();
                        suplementarias += control.getRecargo();
                        if (control.getCliente() != null)
                            empleado.setCliente(control.getCliente().getNombre());
                        
                        empleado.setHorario("No guardado");
                        if (control.getLibre()) {
                            empleado.setHorario("Libre");
                        } else if (control.getFalta()) {
                            empleado.setHorario("Falta");
                        } else {
                            for (Horario horario: horarios) {
                                if (horario.getNormales().equals(control.getNormales())
                                            && horario.getRecargo().equals(control.getRecargo())
                                            && horario.getSobretiempo().equals(control.getSobretiempo())) {
                                    empleado.setHorario(getLapso(horario));
                                    break;
                                }
                            }
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
                empleado.setDias(dias);
                empleado.setHoras(round(normales));
                empleado.setSobreTiempo(round(sobreTiempo));
                empleado.setSuplementarias(round(suplementarias));
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
                } 
                return false; // Does not match.
            });
        });

        SortedList<EmpleadoTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(empleadosTableView.comparatorProperty());
        empleadosTableView.setItems(sortedData);
    }
    
    public String getLapso(Horario horario) {
        String lapso;
                
        if (horario.getHoraInicio() == 0) {
            lapso = "12am";
        } else if (horario.getHoraInicio() < 12) {
            lapso = horario.getHoraInicio() + "am";
        } else if (horario.getHoraInicio() == 12) {
            lapso = "12pm";
        } else {
            lapso = (horario.getHoraInicio() - 12) + "pm";
        }

        if (horario.getHoraFin()== 0) {
            lapso = lapso + "-" + "12am";
        } else if (horario.getHoraFin() < 12) {
            lapso = lapso + "-" + horario.getHoraFin() + "am";
        } else if (horario.getHoraFin() == 12) {
            lapso = lapso + "-" + "12pm";
        } else {
            lapso = lapso + "-" + (horario.getHoraFin() - 12) + "pm";
        }
        
        return lapso;
    }
    
    public void cambiarUsuarioUltimoRegistro(ControlEmpleado controlEmpleado) {
        Boolean agregar = true;
        for (ControlEmpleado control: ultimosRegistros) {
            if (controlEmpleado.getUsuario().getId()
                    .equals(control.getUsuario().getId())) {
                ultimosRegistros
                        .set(ultimosRegistros.indexOf(control), controlEmpleado);
                agregar = false;
            }
        }
        if (agregar)
            ultimosRegistros.add(controlEmpleado);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        pickerDia.setEditable(true);
        
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
        ultimosRegistros.addAll(new ControlEmpleadoDAO().findAllByUltimosRegistros());
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
}
