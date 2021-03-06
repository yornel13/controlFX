/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.ConstantesController.numDecimalFilter;
import aplicacion.control.reports.ReporteAdelantoQuincenalVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Numeros;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.CargoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cargo;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ChoiceBox;
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
public class QuincenalEmpleadosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
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
    private TableColumn quincenalColumna;
    
    @FXML
    private Button buttonAumento;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonGuardar;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Stage dialogLoading;
   
    private Boolean editable = true;
    
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
    public void aumentoAvanzado(ActionEvent event) {
        empezarAumento();  
    }
    
    @FXML
    public void guardarAumento(ActionEvent event) {
        ArrayList<EmpleadoTable> empleadosIndex = new ArrayList<>();
        empleadosIndex.addAll((List<EmpleadoTable>) data);
        empleadosIndex.stream().forEach((empleado) -> {
            Usuario user = usuarios.get(empleadosIndex.indexOf(empleado));
            if (user.getId().equals(empleado.getId())) {
                user.getDetallesEmpleado()
                    .setQuincena(empleado.getNuevoQuincenal());
                // Registro para auditar
                String detalles = "aumento de $" + empleado.getQuincenal() 
                        + " a $" + empleado.getNuevoQuincenal() 
                        + " el adelanto quincenal del empleado " 
                        + user.getNombre() + " " + user.getApellido();
                aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
            }
        });
        HibernateSessionFactory.getSession().flush();
        completado();
        quincenalColumna = new TableColumn("Adelanto Quincenal");
        quincenalColumna.setMinWidth(150);
        quincenalColumna.setCellValueFactory(new PropertyValueFactory<>("quincenal"));
        quincenalColumna.setStyle("-fx-alignment: center;");
        empleadosTableView.getColumns().clear();
        empleadosTableView.getColumns().addAll(cedulaColumna, nombreColumna, 
                apellidoColumna, departamentoColumna, cargoColumna, quincenalColumna);
        
        editable = true;
        buttonAumento.setVisible(true);
        buttonGuardar.setVisible(false);
        filterField.clear();
        setEmpresa(empresa);
    }
    
    public void empezarAumento() {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
                try {
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Aumento de Adelanto Quincenal");
                    String stageIcon = AplicacionControl.class
                            .getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonCargo = new Button("Por cargo");
                    Button buttonLista = new Button("Empleados seleccionados");
                    Button buttonCancelar = new Button("Cancelar");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("Elija el modo de gestion de el aumento a los empleados."), 
                            buttonCargo, buttonLista, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    buttonCargo.setPrefWidth(170);
                    buttonLista.setPrefWidth(170);
                    buttonCancelar.setPrefWidth(80); 
                    buttonCancelar.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                    });
                     buttonCargo.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                        aumentoPorCargo();
                    });
                    buttonLista.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                        aumentoPorSeleccion();
                    });
                    dialogStage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                aplicacionControl.noPermitido();
            }
        }   
    }
    
    public void aumentoPorCargo() {
        ArrayList<Cargo> cargos;
        ChoiceBox cargoChoiceBox = new ChoiceBox();
        CargoDAO cargoDAO = new CargoDAO();
        cargos = (ArrayList<Cargo>) cargoDAO.findAll();
        String[] itemsCargos = new String[cargos.size()];
        cargos.stream().forEach((obj) -> {
            itemsCargos[cargos.indexOf(obj)] = obj.getNombre();
        });
        cargoChoiceBox.setItems(FXCollections.observableArrayList(itemsCargos)); 
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por cargo");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonContinuar = new Button("continuar");
        Button buttonCancelar = new Button("Cancelar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Elija el cargo a aumentar el adelanto quincenal."), 
                cargoChoiceBox, buttonContinuar, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        cargoChoiceBox.setPrefWidth(150);
        buttonContinuar.setPrefWidth(80);
        buttonCancelar.setPrefWidth(80); 
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        }); 
        buttonContinuar.setOnAction((ActionEvent e) -> {
            if (!cargoChoiceBox.getSelectionModel().isEmpty()) {
                dialogStage.close();
                selecionarForma(cargos.get(cargoChoiceBox
                        .getSelectionModel().getSelectedIndex()));
            }
        }); 
        dialogStage.show();
    }
    
    public void aumentoPorSeleccion() {
        String texto = "Aumento a empleados seleccionados "
                + "en la tabla de empleados.";
  
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por selección.");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonPorcentaje = new Button("Por pocentaje");
        Button buttonMonto = new Button("Por valor fijo");
        Button buttonCancelar = new Button("Cancelar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text(texto), new Text("Ahora elija la forma de aumento."), 
                buttonPorcentaje, buttonMonto, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        buttonPorcentaje.setPrefWidth(150);
        buttonMonto.setPrefWidth(150);
        buttonCancelar.setPrefWidth(80); 
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonPorcentaje.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            porPocentajeSeleccion();
        });
        buttonMonto.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            porValorFijoSeleccion();
        });
        dialogStage.show();
        
        if (filterField.getText().isEmpty()) {
            tablaPrecausion();
        }
    }
    
    public void tablaPrecausion() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Información");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("No hay nada escrito en el filtro, entonces han sido "
                + "seleccionados todos \nlos empleados de la empresa para "
                + "aumento de adelanto quincenal."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void selecionarForma(Cargo cargo) {
        String texto = "Aumento a empleados con el cargo " + cargo.getNombre() + ".";
  
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por cargo");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonPorcentaje = new Button("Por pocentaje");
        Button buttonMonto = new Button("Por valor fijo");
        Button buttonCancelar = new Button("Cancelar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text(texto), new Text("Ahora elija la forma de aumento."), 
                buttonPorcentaje, buttonMonto, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        buttonPorcentaje.setPrefWidth(150);
        buttonMonto.setPrefWidth(150);
        buttonCancelar.setPrefWidth(80); 
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonPorcentaje.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            porPocentaje(cargo);
        });
        buttonMonto.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            porValorFijo(cargo);
        });
        dialogStage.show();
    }
    
    public void porPocentaje(Cargo cargo) {
        String texto = "Aumento a empleados con el cargo " + cargo.getNombre() + ".";
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por cargo");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        TextField fieldPorcentaje = new TextField();
        Button buttonContinuar = new Button("Continuar");
        Button buttonCancelar = new Button("Cancelar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text(texto), new Text("Por favor ingrese el porcentaje del aumento."), 
                fieldPorcentaje, new Text("Valor valido entre 0.0 y 1000.0"), 
                buttonContinuar, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        fieldPorcentaje.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        fieldPorcentaje.setPrefWidth(60);
        fieldPorcentaje.setMaxWidth(60);
        buttonContinuar.setPrefWidth(80); 
        buttonCancelar.setPrefWidth(80); 
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        }); 
        buttonContinuar.setOnAction((ActionEvent e) -> {
            if (!fieldPorcentaje.getText().isEmpty())
                if (Double.valueOf(fieldPorcentaje.getText()) < 1000.0d) {
                    dialogStage.close();
                    setEmpleadoNuevoSueldoPorcentaje(cargo, Double
                            .valueOf(fieldPorcentaje.getText()));
                }
        }); 
        dialogStage.show();
    }
    
    public void porPocentajeSeleccion() {
        String texto = "Aumento a empleados seleccionados "
                + "en la tabla de empleados.";
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por selección");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        TextField fieldPorcentaje = new TextField();
        Button buttonContinuar = new Button("Continuar");
        Button buttonCancelar = new Button("Cancelar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text(texto), new Text("Por favor ingrese el pocentaje del aumento."), 
                fieldPorcentaje, new Text("Valor valido entre 0.0 y 1000.0"), 
                buttonContinuar, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        fieldPorcentaje.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        fieldPorcentaje.setPrefWidth(60);
        fieldPorcentaje.setMaxWidth(60);
        buttonContinuar.setPrefWidth(80); 
        buttonCancelar.setPrefWidth(80); 
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        }); 
        buttonContinuar.setOnAction((ActionEvent e) -> {
            if (!fieldPorcentaje.getText().isEmpty())
                if (Double.valueOf(fieldPorcentaje.getText()) < 1000.0d) {
                    dialogStage.close();
                    setEmpleadoNuevoSueldoPorcentajeSeleccion(Double
                            .valueOf(fieldPorcentaje.getText()));
                }
        }); 
        dialogStage.show();
    }
    
    public void porValorFijo(Cargo cargo) {
        String texto = "Aumento a empleados con el cargo " + cargo.getNombre() + ".";
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por cargo");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        TextField fieldPorcentaje = new TextField();
        Button buttonContinuar = new Button("Continuar");
        Button buttonCancelar = new Button("Cancelar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text(texto), new Text("Por favor ingrese el valor fijo para "
                + "adicionar al \n adelanto quincenal de los empleados."), 
                fieldPorcentaje, buttonContinuar, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        fieldPorcentaje.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        fieldPorcentaje.setPrefWidth(60);
        fieldPorcentaje.setMaxWidth(60);
        buttonContinuar.setPrefWidth(80); 
        buttonCancelar.setPrefWidth(80); 
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        }); 
        buttonContinuar.setOnAction((ActionEvent e) -> {
            if (!fieldPorcentaje.getText().isEmpty())
                dialogStage.close();
                setEmpleadoNuevoSueldoMonto(cargo, Double
                    .valueOf(fieldPorcentaje.getText()));
            
        }); 
        dialogStage.show();
    }
    
    public void porValorFijoSeleccion() {
        String texto = "Aumento a empleados seleccionados "
                + "en la tabla de empleados.";
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por selección");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        TextField fieldPorcentaje = new TextField();
        Button buttonContinuar = new Button("Continuar");
        Button buttonCancelar = new Button("Cancelar");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text(texto), new Text("Por favor ingrese el valor fijo para "
                + "adicionar al \n adelanto quincenal de los empleados."), 
                fieldPorcentaje, buttonContinuar, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        fieldPorcentaje.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        fieldPorcentaje.setPrefWidth(60);
        fieldPorcentaje.setMaxWidth(60);
        buttonContinuar.setPrefWidth(80); 
        buttonCancelar.setPrefWidth(80); 
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        }); 
        buttonContinuar.setOnAction((ActionEvent e) -> {
            if (!fieldPorcentaje.getText().isEmpty()){
                dialogStage.close();
                setEmpleadoNuevoSueldoMontoSeleccion(Double
                    .valueOf(fieldPorcentaje.getText()));
            }  
             
        }); 
        dialogStage.show();
    }
    
    public void setEmpleadoNuevoSueldoPorcentaje(Cargo cargo, Double porcentaje) {
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO()
                .findAllByEmpresaYCargoActivo(empresa.getId(), cargo.getId()));
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
            empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
            Double nuevoQuincenal = user.getDetallesEmpleado().getQuincena()
                    + (user.getDetallesEmpleado().getQuincena()/100d) * porcentaje;
            empleado.setNuevoQuincenal(Numeros.round(nuevoQuincenal));
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);
        empleadosTableView.getColumns().clear();
        
        TableColumn sueldoViejo = new TableColumn("Viejo");
        sueldoViejo.setMinWidth(75);
        sueldoViejo.setCellValueFactory(new PropertyValueFactory<>("quincenal"));

        TableColumn sueldoNuevo = new TableColumn("Nuevo");
        sueldoNuevo.setMinWidth(75);
        sueldoNuevo.setCellValueFactory(new PropertyValueFactory<>("nuevoQuincenal"));
        
        quincenalColumna = new TableColumn("Adelanto Quincenal"); 
        quincenalColumna.getColumns().addAll(sueldoViejo, sueldoNuevo);
        
        empleadosTableView.getColumns().addAll(cedulaColumna, nombreColumna, 
                apellidoColumna, departamentoColumna, cargoColumna, quincenalColumna);
        
        editable = false;
        
        filtro();
        buttonAumento.setVisible(false);
        buttonGuardar.setVisible(true);
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por cargo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Adelantos quincenal aumentados, pero no guardados,"), 
                new Text("En la lista ahora se muestran los adelantos viejos y nuevos"),
                new Text("Verifiquelos y luego use el boton \"Guardar\""),
                new Text("Sí no guarda los cambios seran desechados."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(80);
        
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void setEmpleadoNuevoSueldoPorcentajeSeleccion(Double porcentaje) {
        ArrayList<Usuario> usuariosLista = new ArrayList<>();
        for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) 
                empleadosTableView.getItems()) {
            for (Usuario user: usuarios) {
                if (user.getId().equals(empleadoTable.getId())) {
                    usuariosLista.add(user);
                }
            }
        }
        usuarios.clear();
        usuarios.addAll(usuariosLista);
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
            empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
            Double nuevoQuincenal = user.getDetallesEmpleado().getQuincena()
                    + (user.getDetallesEmpleado().getQuincena()/100d) * porcentaje;
            empleado.setNuevoQuincenal(Numeros.round(nuevoQuincenal));
             return empleado;
         }).forEach((empleado) -> {
             data.add(empleado);
         });
        empleadosTableView.setItems(data);
        empleadosTableView.getColumns().clear();
        
        TableColumn sueldoViejo = new TableColumn("Viejo");
        sueldoViejo.setMinWidth(75);
        sueldoViejo.setCellValueFactory(new PropertyValueFactory<>("quincenal"));

        TableColumn sueldoNuevo = new TableColumn("Nuevo");
        sueldoNuevo.setMinWidth(75);
        sueldoNuevo.setCellValueFactory(new PropertyValueFactory<>("nuevoQuincenal"));
        
        quincenalColumna = new TableColumn("Adelanto Quincenal"); 
        quincenalColumna.getColumns().addAll(sueldoViejo, sueldoNuevo);
        
        empleadosTableView.getColumns().addAll(cedulaColumna, nombreColumna, 
                apellidoColumna, departamentoColumna, cargoColumna, quincenalColumna);
        
        editable = false;
        
        filtro();
        buttonAumento.setVisible(false);
        buttonGuardar.setVisible(true);
        
         Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por selección");
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Adelantos quincenal aumentados, pero no guardados,"), 
                new Text("En la lista ahora se muestran los adelantos viejos y nuevos"),
                new Text("Verifiquelos y luego use el boton \"Guardar\""),
                new Text("Sí no guarda los cambios seran desechados."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(80);
        
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void setEmpleadoNuevoSueldoMonto(Cargo cargo, Double monto) {
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO()
                .findAllByEmpresaYCargoActivo(empresa.getId(), cargo.getId()));
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
            empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
            Double nuevoQuincenal = user.getDetallesEmpleado().getQuincena() + monto;
            empleado.setNuevoQuincenal(Numeros.round(nuevoQuincenal));
            return empleado;
         }).forEach((empleado) -> {
             data.add(empleado);
         });
        empleadosTableView.setItems(data);
        empleadosTableView.getColumns().clear();
        
        TableColumn sueldoViejo = new TableColumn("Viejo");
        sueldoViejo.setMinWidth(75);
        sueldoViejo.setCellValueFactory(new PropertyValueFactory<>("quincenal"));

        TableColumn sueldoNuevo = new TableColumn("Nuevo");
        sueldoNuevo.setMinWidth(75);
        sueldoNuevo.setCellValueFactory(new PropertyValueFactory<>("nuevoQuincenal"));
        
        quincenalColumna = new TableColumn("Adelanto Quincenal"); 
        quincenalColumna.getColumns().addAll(sueldoViejo, sueldoNuevo);
        
        empleadosTableView.getColumns().addAll(cedulaColumna, nombreColumna, 
                apellidoColumna, departamentoColumna, cargoColumna, quincenalColumna);
        
        editable = false;
        
        filtro();
        buttonAumento.setVisible(false);
        buttonGuardar.setVisible(true);
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por cargo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Adelantos quincenal aumentados, pero no guardados,"), 
                new Text("En la lista ahora se muestran los adelantos viejos y nuevos"),
                new Text("Verifiquelos y luego use el boton \"Guardar\""),
                new Text("Sí no guarda los cambios seran desechados."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(80);
        
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
     public void setEmpleadoNuevoSueldoMontoSeleccion(Double monto) {
        ArrayList<Usuario> usuariosLista = new ArrayList<>();
        for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) 
                empleadosTableView.getItems()) {
            for (Usuario user: usuarios) {
                if (user.getId().equals(empleadoTable.getId())) {
                    usuariosLista.add(user);
                }
            }
        }
        usuarios.clear();
        usuarios.addAll(usuariosLista);
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
            empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
            Double nuevoQuincenal = user.getDetallesEmpleado().getQuincena() + monto;
            empleado.setNuevoQuincenal(Numeros.round(nuevoQuincenal));
             return empleado;
         }).forEach((empleado) -> {
             data.add(empleado);
         });
        empleadosTableView.setItems(data);
        empleadosTableView.getColumns().clear();
        
        TableColumn sueldoViejo = new TableColumn("Viejo");
        sueldoViejo.setMinWidth(75);
        sueldoViejo.setCellValueFactory(new PropertyValueFactory<>("quincenal"));

        TableColumn sueldoNuevo = new TableColumn("Nuevo");
        sueldoNuevo.setMinWidth(75);
        sueldoNuevo.setCellValueFactory(new PropertyValueFactory<>("nuevoQuincenal"));
        
        quincenalColumna = new TableColumn("Adelanto Quincenal"); 
        quincenalColumna.getColumns().addAll(sueldoViejo, sueldoNuevo);
        
        empleadosTableView.getColumns().addAll(cedulaColumna, nombreColumna, 
                apellidoColumna, departamentoColumna, cargoColumna, quincenalColumna);
        
        editable = false;
        
        filtro();
        buttonAumento.setVisible(false);
        buttonGuardar.setVisible(true);
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Aumento de Adelanto Quincenal por selección");
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Adelantos quincenal aumentados, pero no guardados,"), 
                new Text("En la lista ahora se muestran los adelantos viejos y nuevos"),
                new Text("Verifiquelos y luego use el boton \"Guardar\""),
                new Text("Sí no guarda los cambios seran desechados."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(80);
        
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void mostrarEditarQuincenal(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
                try {
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle("Adelanto Quincenal");
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonGuardar = new Button("Cambiar");
                    Button buttonCancelar = new Button("Cancelar");
                    TextField fieldAdelanto = new TextField();
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("Ingrese el nuevo valor del adelanto"), fieldAdelanto, buttonGuardar, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    fieldAdelanto.setPrefWidth(150);
                    buttonGuardar.setPrefWidth(100);
                    buttonCancelar.setPrefWidth(100);
                    fieldAdelanto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                    dialogStage.show();
                    buttonGuardar.setOnAction((ActionEvent e) -> {
                        Double newAdelantoValue;
                        if (fieldAdelanto.getText().isEmpty()) {
                            newAdelantoValue = 0d;
                        } else {
                            newAdelantoValue = Double.parseDouble(fieldAdelanto.getText());
                        }
                        
                        double oldMonto = 0;
                        if (empleado.getDetallesEmpleado().getQuincena() != null) {
                            oldMonto = empleado.getDetallesEmpleado().getQuincena();
                        }
                        
                        empleado.getDetallesEmpleado().setQuincena(newAdelantoValue);
                        HibernateSessionFactory.getSession().flush();
                        
                        dialogStage.close();
                        
                        empleadoEditado(empleado);
                        
                        completado();

                        // Registro para auditar
                        String detalles = "edito el adelanto quincenal de " 
                                + empleado.getNombre() + " " + empleado.getApellido()
                                + " de " + oldMonto + " a " + newAdelantoValue;
                        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    }); 
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    public void completado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Adelanto(s) quincenal modificado con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
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
    
    public void imprimir(File file) {
        
        dialogWait();
        
        ReporteAdelantoQuincenalVarios datasource = new ReporteAdelantoQuincenalVarios();
        datasource.addAll((List<EmpleadoTable>) empleadosTableView.getItems());
        
        Double total = 0d;
        
        for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) 
                empleadosTableView.getItems()) {
            total += empleadoTable.getQuincenal();
        }
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ADELANTO_QUINCENAL_EMPLEADOS);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("total", round(total).toString());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "adelanto_quincenal_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo general de adelantos quincenales de todos los empleado";
            aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            
            dialogoCompletado();
            
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(PagosTotalEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dialogLoading.close();
        }
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir Adelanto Quincenal");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Completado."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        if (!empleadosTableView.getItems().isEmpty()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Imprimir Adelanto Quincenal");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonSiDocumento = new Button("Seleccionar ruta");
            Button buttonNoDocumento = new Button("Salir");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Seleccione la ruta de guardado"), 
                    buttonSiDocumento, buttonNoDocumento).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonSiDocumento.setOnAction((ActionEvent e) -> {
                File file = seleccionarDirectorio();
                if (file != null) {
                    dialogStage.close();
                    imprimir(file);
                }
            });
            buttonNoDocumento.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
        }
    }
    
    public void empleadoEditado(Usuario user) {
        for (EmpleadoTable empleadoTable: data) {
            if(Objects.equals(empleadoTable.getId(), user.getId())) {
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
               if (user.getDetallesEmpleado().getQuincena() != null) {
                    empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
               } else {
                    empleado.setQuincenal(0d);
               }
               data.set(data.indexOf(empleadoTable), empleado);
               return;
            }
        }
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findAllByEmpresaIdActivo(empresa.getId()));
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
            if (user.getDetallesEmpleado().getQuincena() != null) {
                 empleado.setQuincenal(user.getDetallesEmpleado().getQuincena());
            } else {
                 empleado.setQuincenal(0d);
            }

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
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        quincenalColumna.setCellValueFactory(new PropertyValueFactory<>("quincenal"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    mostrarEditarQuincenal(new UsuarioDAO().findById(rowData.getId()));
                }
            });
            return row ;
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
        buttonAumento.setTooltip(
            new Tooltip("Aumento avanzado")
        );
        buttonAumento.setOnMouseEntered((MouseEvent t) -> {
            buttonAumento.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/aumento.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAumento.setOnMouseExited((MouseEvent t) -> {
            buttonAumento.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/aumento.png'); "
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
        buttonGuardar.setTooltip(
            new Tooltip("Guardar")
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
