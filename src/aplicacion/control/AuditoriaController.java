/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteActuarialesVarios;
import aplicacion.control.reports.ReporteAuditoria;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import hibernate.dao.AccionTipoDAO;
import hibernate.dao.RegistroAccionesDAO;
import hibernate.model.AccionTipo;
import hibernate.model.RegistroAcciones;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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

/**
 *
 * @author Yornel
 */
public class AuditoriaController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private ArrayList<Usuario> usuarios;
    
    private ArrayList<AccionTipo> acciones;
   
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private TableView accionesTableView;
    
    @FXML
    private ChoiceBox accionesSelector;
    
    @FXML 
    private TextField filterField;
    
    @FXML
    private TableColumn detalles;
    
    @FXML
    private TableColumn fecha;
    
    private ObservableList<RegistroAcciones> data;
    private Stage dialogLoading;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
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
        
        ReporteAuditoria datasource = new ReporteAuditoria();
        datasource.addAll((List<RegistroAcciones>) accionesTableView.getItems());
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_AUDITORIA);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("detalles", "Reporte impreso por: " + 
                    aplicacionControl.permisos.getUsuario().getNombre() + " " + 
                    aplicacionControl.permisos.getUsuario().getApellido());
            String detalles2 = "";
            if (aplicacionControl.permisos.getUsuario().getDetallesEmpleado() != null) {
                detalles2 = "Cargo: " + aplicacionControl.permisos.getUsuario()
                        .getDetallesEmpleado().getCargo().getNombre();
            }
            parametros.put("detalles2", detalles2);
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "auditoria_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero un reporte de acciones hechas por los usarios.";
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
        dialogStage.setTitle("Imprimir Auditoria");
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
        fileChooser.setTitle("Selecciona un directorio para guardar la auditoria");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir Auditoria");
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
    
    @FXML
    private void onVerUsuarios(ActionEvent event) {
        filterField.clear();
        if (!accionesSelector.getSelectionModel().isEmpty()
                && accionesSelector.getSelectionModel().getSelectedIndex() != acciones.size()) {
            setAccionesTipo();
        } else {
            setAcciones();
        }
    }
    
    @FXML
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarConfiguracion();
    }
    
    public void setAcciones() {
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(registroAccionesDAO.findAllDesc());
        accionesTableView.setItems(data); 
        setFiltro();
    }
    
    public void setAccionesTipo() {
        Integer accionId = acciones.get(accionesSelector.getSelectionModel().getSelectedIndex()).getId();
        
        RegistroAccionesDAO registroAccionesDAO = new RegistroAccionesDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(registroAccionesDAO.findAllByAccionIdDesc(accionId));
        accionesTableView.setItems(data); 
        setFiltro();
    }
    
    public void setFiltro() {
       FilteredList<RegistroAcciones> filteredData = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(acciones -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (acciones.getUsuario().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (acciones.getUsuario().getApellido().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (acciones.getDetalles().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        SortedList<RegistroAcciones> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(accionesTableView.comparatorProperty());
        accionesTableView.setItems(sortedData); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        accionesTableView.setEditable(Boolean.FALSE);
        
        detalles.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        detalles.setMinWidth(620);
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        
        setAcciones();
        
        AccionTipoDAO accionTipoDAO = new AccionTipoDAO();
        acciones = new ArrayList<>();
        acciones.addAll(accionTipoDAO.findAll());
        
        String[] itemsAccion = new String[acciones.size() + 1];
        acciones.stream().forEach((accion) -> {
            itemsAccion[acciones.indexOf(accion)] = accion.getNombre();
        });
        itemsAccion[acciones.size()] = "TODO";
        accionesSelector.setItems(FXCollections.observableArrayList(itemsAccion));
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