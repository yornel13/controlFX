/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Fechas.getToday;
import hibernate.dao.ClienteDAO;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.HorarioDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlEmpleado;
import hibernate.model.Empresa;
import hibernate.model.Horario;
import hibernate.model.Usuario;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.joda.time.DateTime;
import java.sql.Time;
import java.sql.Date;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import org.joda.time.DateTimeConstants;

/**
 *
 * @author Yornel
 */
public class HorarioSemanalEmpleadosController implements Initializable {
    
    public AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    @FXML
    private TableColumn empleadoColumna;
    
    @FXML
    private TableColumn lunesColumna;
    
    @FXML
    private TableColumn martesColumna;
     
    @FXML
    private TableColumn miercolesColumna;
    
    @FXML
    private TableColumn juevesColumna;
     
    @FXML
    private TableColumn viernesColumna;
      
    @FXML
    private TableColumn sabadoColumna;
       
    @FXML
    private TableColumn domingoColumna;
    
    @FXML
    private Button buttonAtras;
    
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
    
    private Date fecha;
    private Date fechaTope;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    
    private Stage stagePrincipal;
    
    List<ControlEmpleado> controlEmpleados;
    
    ArrayList<ControlEmpleado> ultimosRegistros;
    
    List<Horario> horarios;
    
    List<Cliente> clientes;
    
    Dialog<Void> dialog;
    
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
        fecha = Date.valueOf(pickerDia.getValue());
        setTableInfo(fecha);
    }
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDia.setValue(pickerDia.getValue().plusDays(7));
        fecha = Date.valueOf(pickerDia.getValue());
        setTableInfo(fecha);
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        pickerDia.setValue(pickerDia.getValue().minusDays(7));
        fecha = Date.valueOf(pickerDia.getValue());
        setTableInfo(fecha);
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        
        horarios = (List<Horario>) new HorarioDAO().findAll();
        clientes = new ClienteDAO().findAllActivo();
        
        DateTime dateTime = new DateTime(getToday().getTime());
        dateTime = dateTime.minusDays(dateTime.getDayOfWeek());
        dateTime = dateTime.plusDays(1);
           
        fecha = new Date(dateTime.getMillis());
        
        pickerDia.setValue(fecha.toLocalDate());
        
        setTableInfo(fecha);
        
    }
    
    String rangoText(DateTime dateTime) {
        String dia = dateTime.toCalendar(Locale.getDefault())
                            .getDisplayName(Calendar
                                    .DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String output = dia.substring(0, 1).toUpperCase() + dia.substring(1);
        
        return output + " " + Fechas.getFechaConMes(dateTime);
    }
  
    public void setTableInfo(Date fecha) {
        this.fecha = fecha;
        
        DateTime dateTimeLunes = new DateTime(fecha.getTime());
        DateTime dateTimeDomingo = dateTimeLunes.plusDays(6);
        
        fechaLabel.setText(rangoText(dateTimeLunes)+" - "+rangoText(dateTimeDomingo));
        
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO().findAllByEmpresaIdActivo(empresa.getId()));
        
        data = FXCollections.observableArrayList();
        
        if (!usuarios.isEmpty()) {
            
            ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
            
            controlEmpleados = controlDAO
                    .findAllByFechaAndEmpresaId(fecha, empresa.getId());
            
            usuarios.stream().map((user) -> {
                EmpleadoTable empleado = new EmpleadoTable();
                empleado.setId(user.getId());
                empleado.setUsuario(user);
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
                
                List<ControlEmpleado> controls = new ArrayList<>();
                controls.addAll(new ControlEmpleadoDAO()
                            .findAllByEmpleadoIdInDeterminateTime(user.getId()
                                    ,fecha , new Date(dateTimeDomingo.getMillis())));
                for (ControlEmpleado ce: controls) {
                    switch (new DateTime(ce.getFecha().getTime()).getDayOfWeek()) {
                        case DateTimeConstants.MONDAY:
                            empleado.setLunes(ce);
                            break;
                        case DateTimeConstants.TUESDAY:
                            empleado.setMartes(ce);
                            break;
                        case DateTimeConstants.WEDNESDAY:
                            empleado.setMiercoles(ce);
                            break;
                        case DateTimeConstants.THURSDAY:
                            empleado.setJueves(ce);
                            break;
                        case DateTimeConstants.FRIDAY:
                            empleado.setViernes(ce);
                            break;
                        case DateTimeConstants.SATURDAY:
                            empleado.setSabado(ce);
                            break;
                        case DateTimeConstants.SUNDAY:
                            empleado.setDomingo(ce);
                            break;
                    }
                }
                
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
        buttonAnterior.setTooltip(
            new Tooltip("Semana Anterior")
        );
        buttonAnterior.setOnMouseEntered((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonAnterior.setOnMouseExited((MouseEvent t) -> {
            buttonAnterior.setStyle("-fx-background-color: #039BE5;");
        });
        buttonSiguiente.setTooltip(
            new Tooltip("Semana Siguiente")
        );
        buttonSiguiente.setOnMouseEntered((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #29B6F6;");
        });
        buttonSiguiente.setOnMouseExited((MouseEvent t) -> {
            buttonSiguiente.setStyle("-fx-background-color: #039BE5;");
        });
        
        empleadoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                return new ReadOnlyStringWrapper(data.getValue().getApellido() 
                        + " " + data.getValue().getNombre());
            }
        });
        
        lunesColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getLunes() == null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getLunes()));
            }
        });
       
        martesColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getMartes()== null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getMartes()));
            }
        });
        
        miercolesColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getMiercoles()== null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getMiercoles()));
            }
        });
        
        juevesColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getJueves()== null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getJueves()));
            }
        });
        
        viernesColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getViernes()== null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getViernes()));
            }
        });
         
        sabadoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getSabado()== null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getSabado()));
            }
        });
          
        domingoColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<EmpleadoTable,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<EmpleadoTable, String> data) {
                
                if (data.getValue().getDomingo()== null) 
                    return null;
                
                return new ReadOnlyStringWrapper(getTextHorario(data.getValue()
                        .getDomingo()));
            }
        });
        
         lunesColumna.setCellFactory(new Callback<TableColumn<EmpleadoTable, 
                String>, TableCell<EmpleadoTable, String>>() {
            @Override
            public TableCell<EmpleadoTable, String> call(TableColumn<EmpleadoTable, 
                    String> col) {
                final TableCell<EmpleadoTable, String> cell = 
                        new TableCell<EmpleadoTable, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(firstName);
                        }
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            selecionarHorario(((EmpleadoTable) cell.getTableRow()
                                    .getItem()), Const.LUNES, ((EmpleadoTable)
                                            cell.getTableRow().getItem()).getLunes());
                        } else if (event.getButton() == MouseButton.SECONDARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            setUltimoControl(((EmpleadoTable) cell.getTableRow()
                                        .getItem()), Const.LUNES);
                        }
                    }
   
                });
                return cell ;
            }
        });
        
        martesColumna.setCellFactory(new Callback<TableColumn<EmpleadoTable, 
                String>, TableCell<EmpleadoTable, String>>() {
            @Override
            public TableCell<EmpleadoTable, String> call(TableColumn<EmpleadoTable, 
                    String> col) {
                final TableCell<EmpleadoTable, String> cell = 
                        new TableCell<EmpleadoTable, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(firstName);
                        }
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            selecionarHorario(((EmpleadoTable) cell.getTableRow()
                                    .getItem()), Const.MARTES, ((EmpleadoTable)
                                            cell.getTableRow().getItem()).getMartes());
                        } else if (event.getButton() == MouseButton.SECONDARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            setUltimoControl(((EmpleadoTable) cell.getTableRow()
                                        .getItem()), Const.MARTES);
                        }
                    }
   
                });
                return cell ;
            }
        });
        
        miercolesColumna.setCellFactory(new Callback<TableColumn<EmpleadoTable, 
                String>, TableCell<EmpleadoTable, String>>() {
            @Override
            public TableCell<EmpleadoTable, String> call(TableColumn<EmpleadoTable, 
                    String> col) {
                final TableCell<EmpleadoTable, String> cell = 
                        new TableCell<EmpleadoTable, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(firstName);
                        }
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            selecionarHorario(((EmpleadoTable) cell.getTableRow()
                                    .getItem()), Const.MIERCOLES, ((EmpleadoTable)
                                            cell.getTableRow().getItem()).getMiercoles());
                        } else if (event.getButton() == MouseButton.SECONDARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            setUltimoControl(((EmpleadoTable) cell.getTableRow()
                                        .getItem()), Const.MIERCOLES);
                        }
                    }
   
                });
                return cell ;
            }
        });
        
        juevesColumna.setCellFactory(new Callback<TableColumn<EmpleadoTable, 
                String>, TableCell<EmpleadoTable, String>>() {
            @Override
            public TableCell<EmpleadoTable, String> call(TableColumn<EmpleadoTable, 
                    String> col) {
                final TableCell<EmpleadoTable, String> cell = 
                        new TableCell<EmpleadoTable, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(firstName);
                        }
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            selecionarHorario(((EmpleadoTable) cell.getTableRow()
                                    .getItem()), Const.JUEVES, ((EmpleadoTable)
                                            cell.getTableRow().getItem()).getJueves());
                        } else if (event.getButton() == MouseButton.SECONDARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            setUltimoControl(((EmpleadoTable) cell.getTableRow()
                                        .getItem()), Const.JUEVES);
                        }
                    }
   
                });
                return cell ;
            }
        });
        
        viernesColumna.setCellFactory(new Callback<TableColumn<EmpleadoTable, 
                String>, TableCell<EmpleadoTable, String>>() {
            @Override
            public TableCell<EmpleadoTable, String> call(TableColumn<EmpleadoTable, 
                    String> col) {
                final TableCell<EmpleadoTable, String> cell = 
                        new TableCell<EmpleadoTable, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(firstName);
                        }
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            selecionarHorario(((EmpleadoTable) cell.getTableRow()
                                    .getItem()), Const.VIERNES, ((EmpleadoTable)
                                            cell.getTableRow().getItem()).getViernes());
                        } else if (event.getButton() == MouseButton.SECONDARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            setUltimoControl(((EmpleadoTable) cell.getTableRow()
                                        .getItem()), Const.VIERNES);
                        }
                    }
   
                });
                return cell ;
            }
        });
        
        sabadoColumna.setCellFactory(new Callback<TableColumn<EmpleadoTable, 
                String>, TableCell<EmpleadoTable, String>>() {
            @Override
            public TableCell<EmpleadoTable, String> call(TableColumn<EmpleadoTable, 
                    String> col) {
                final TableCell<EmpleadoTable, String> cell = 
                        new TableCell<EmpleadoTable, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(firstName);
                        }
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            selecionarHorario(((EmpleadoTable) cell.getTableRow()
                                    .getItem()), Const.SABADO, ((EmpleadoTable)
                                            cell.getTableRow().getItem()).getSabado());
                        } else if (event.getButton() == MouseButton.SECONDARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            setUltimoControl(((EmpleadoTable) cell.getTableRow()
                                        .getItem()), Const.SABADO);
                        }
                    }
   
                });
                return cell ;
            }
        });
        
        domingoColumna.setCellFactory(new Callback<TableColumn<EmpleadoTable, 
                String>, TableCell<EmpleadoTable, String>>() {
            @Override
            public TableCell<EmpleadoTable, String> call(TableColumn<EmpleadoTable, 
                    String> col) {
                final TableCell<EmpleadoTable, String> cell = 
                        new TableCell<EmpleadoTable, String>() {
                    @Override
                    public void updateItem(String firstName, boolean empty) {
                        super.updateItem(firstName, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(firstName);
                        }
                    }
                };
                cell.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            selecionarHorario(((EmpleadoTable) cell.getTableRow()
                                    .getItem()), Const.DOMINGO, ((EmpleadoTable)
                                            cell.getTableRow().getItem()).getDomingo());
                        } else if (event.getButton() == MouseButton.SECONDARY 
                                && event.getClickCount() == 1 
                                && cell.getTableRow().getItem() != null) {
                            setUltimoControl(((EmpleadoTable) cell.getTableRow()
                                        .getItem()), Const.DOMINGO);
                        }
                    }
   
                });
                return cell ;
            }
        });
        
    }
    
    private void selecionarHorario(EmpleadoTable empleadoTable, String dia, ControlEmpleado control) {
        
    }
    
    private void setUltimoControl(EmpleadoTable empleadoTable, String dia) {

    }
    
    public String getTextHorario(ControlEmpleado controlEmpleado) {
        
        if (controlEmpleado.getCaso().equals(Const.LIBRE)) {
            return "Libre";
        } else if (controlEmpleado.getCaso().equals(Const.FALTA)) {
            return "Falta";
        } else if (controlEmpleado.getCaso().equals(Const.VACACIONES)) {
            return "Vacaciones";
        } else if (controlEmpleado.getCaso().equals(Const.PERMISO)) {
            return "Permiso";
        } else if (controlEmpleado.getCaso().equals(Const.DM)) {
            return "D. Medico";
        } else if (controlEmpleado.getCaso().equals(Const.CM)) {
            return "C. Medica";
        } else {
           return getLapso(controlEmpleado.getEntrada(), controlEmpleado.getSalida());
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
