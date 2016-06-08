/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.ConfiguracionEmpresaController.numDecimalFilter;
import static aplicacion.control.HorasEmpleadosController.getToday;
import aplicacion.control.reports.ReporteAdelantoQuincenalVarios;
import aplicacion.control.reports.ReporteDeudasVarios;
import aplicacion.control.reports.ReporteRolDePagoIndividual;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import static aplicacion.control.util.Fechas.getFechaConMes;
import aplicacion.control.util.Numeros;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.CargoDAO;
import hibernate.dao.PagoQuincenaDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cargo;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.PagoQuincena;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
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
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class PagoQuincenalController implements Initializable {
    
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
    private TableColumn quincenalColumna;
    
    @FXML 
    private TableColumn pagadoColumna;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    @FXML
    private void pagarATodos(ActionEvent event) {
        changeSelect(checkBoxPagarTodos.isSelected());
    }
    
    public void changeSelect(Boolean todos) {
        data.clear();
        empleadosTableView.getItems().clear();
        ArrayList<PagoQuincena> pagosQuincena = new ArrayList<>();
        DateTime dateTime = new DateTime();
        Timestamp inicio = new Timestamp(dateTime.withDayOfMonth(1).getMillis());
        Timestamp fin = new Timestamp(dateTime.withDayOfMonth(28).getMillis());
        pagosQuincena.addAll(new PagoQuincenaDAO().findAllInDeterminateTime(inicio, fin));
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.id.set(user.getId());
            empleado.nombre.set(user.getNombre());
            empleado.apellido.set(user.getApellido());
            empleado.cedula.set(user.getCedula());
            empleado.empresa.set(user.getDetallesEmpleado().getEmpresa().getNombre());
            empleado.telefono.set(user.getTelefono());
            empleado.departamento.set(user.getDetallesEmpleado().getDepartamento().getNombre());
            empleado.cargo.set(user.getDetallesEmpleado().getCargo().getNombre());
            if (user.getDetallesEmpleado().getQuincena() != null) {
                 empleado.quincenal.set(user.getDetallesEmpleado().getQuincena());
            } else {
                 empleado.quincenal.set(0d);
            }
            empleado.pagado.set("No");
            empleado.pagar.set(todos);
            for (PagoQuincena pagoQuincena: pagosQuincena) {
                if (pagoQuincena.getUsuario().getId().equals(user.getId())) {
                    empleado.pagado.set("Si");
                    empleado.pagar.set(false);
                    break;
                }
            }

             return empleado;
         }).forEach((empleado) -> {
             data.add(empleado);
         });
         empleadosTableView.setItems(data);

        filtro(); 
    }
    
    @FXML
    public void pagarAdelanto(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pagar Adelanto Quincenal");
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
        children(new Text("¿Seguro que desea pagar el adelanto Quincenal "
                + "a estos empleado?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            //generarRolIndividual();

        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarios = new ArrayList<>();
        usuarios.addAll(usuarioDAO.findByEmpresaIdActivo(empresa.getId()));
        
        ArrayList<PagoQuincena> pagosQuincena = new ArrayList<>();
        DateTime dateTime = new DateTime();
        Timestamp inicio = new Timestamp(dateTime.withDayOfMonth(1).getMillis());
        Timestamp fin = new Timestamp(dateTime.withDayOfMonth(28).getMillis());
        pagosQuincena.addAll(new PagoQuincenaDAO().findAllInDeterminateTime(inicio, fin));
        data = FXCollections.observableArrayList(); 
        usuarios.stream().map((user) -> {
            EmpleadoTable empleado = new EmpleadoTable();
            empleado.id.set(user.getId());
            empleado.nombre.set(user.getNombre());
            empleado.apellido.set(user.getApellido());
            empleado.cedula.set(user.getCedula());
            empleado.empresa.set(user.getDetallesEmpleado().getEmpresa().getNombre());
            empleado.telefono.set(user.getTelefono());
            empleado.departamento.set(user.getDetallesEmpleado().getDepartamento().getNombre());
            empleado.cargo.set(user.getDetallesEmpleado().getCargo().getNombre());
            if (user.getDetallesEmpleado().getQuincena() != null) {
                 empleado.quincenal.set(user.getDetallesEmpleado().getQuincena());
            } else {
                 empleado.quincenal.set(0d);
            }
            empleado.pagado.set("No");
            empleado.pagar.set(true);
            for (PagoQuincena pagoQuincena: pagosQuincena) {
                if (pagoQuincena.getUsuario().getId().equals(user.getId())) {
                    empleado.pagado.set("Si");
                    empleado.pagar.set(false);
                    break;
                }
            }

             return empleado;
         }).forEach((empleado) -> {
             data.add(empleado);
         });
         empleadosTableView.setItems(data);
         checkBoxPagarTodos.setSelected(true);

        
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
        
        quincenalColumna.setCellValueFactory(new PropertyValueFactory<>("quincenal"));
        
        pagadoColumna.setCellValueFactory(new PropertyValueFactory<>("pagado"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    //mostrarEditarQuincenal(new UsuarioDAO().findById(rowData.getId()));
                }
            });
            return row ;
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
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")) {
                        checkBoxpagar.setDisable(true);
                    }
                }
                checkBoxpagar.setOnAction(event -> {
                     empleadoTable.pagar.set(checkBoxpagar.isSelected());
                     //guardar();
                });
            }

            
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
