/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteAcumulacionDecimosVarios;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Permisos;
import aplicacion.control.util.Roboto;
import hibernate.HibernateSessionFactory;
import hibernate.dao.UsuarioDAO;
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
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
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
public class DecimosEmpleadosController implements Initializable {
    
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
    private TableColumn<EmpleadoTable, EmpleadoTable>  decimosColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonCambiarVentana;
    
    private ObservableList<EmpleadoTable> data;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    private Stage dialogLoading;
    
    Integer si;
    Integer no;
    
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
    public void cambiarVentana(ActionEvent event) {
        aplicacionControl.mostrarDecimosAcumuladoEmpleados(empresa, stagePrincipal);
    }
    
    public void mostrarNoAcumulaDecimos(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
                try {
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonOk = new Button("Si, no acumula");
                    Button buttonCancelar = new Button("No, cancelar.");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("¿Seguro que desea marcar que NO acumula decimos el empleado?"), buttonOk, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    buttonOk.setPrefWidth(100);
                    buttonCancelar.setPrefWidth(100);
                    buttonOk.setOnAction((ActionEvent e) -> {
                        
                        empleado.getDetallesEmpleado().setAcumulaDecimos(false);
                        HibernateSessionFactory.getSession().flush();
                        
                        dialogStage.close();
                        
                        empleadoEditado(empleado);
                        
                        completado();

                        // Registro para auditar
                        String detalles = "marco que el empleado " 
                                + empleado.getNombre() + " " + empleado.getApellido() + " no acumula decimos";
                        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    }); 
                    buttonCancelar.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                        empleadoEditado(empleado);
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
    
    public void mostrarAcumulaDecimos(Usuario empleado) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.EDITAR)) {
                try {
                    
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setResizable(false);
                    dialogStage.setTitle(empleado.getNombre() + " " + empleado.getApellido());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                    dialogStage.getIcons().add(new Image(stageIcon));
                    Button buttonOk = new Button("Si, si acumula");
                    Button buttonCancelar = new Button("No, cancelar.");
                    dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                    children(new Text("¿Seguro que desea marcar que SI acumula decimos el empleado?"), buttonOk, buttonCancelar).
                    alignment(Pos.CENTER).padding(new Insets(25)).build()));
                    buttonOk.setPrefWidth(100);
                    buttonCancelar.setPrefWidth(100);
                    buttonOk.setOnAction((ActionEvent e) -> {
                        
                        empleado.getDetallesEmpleado().setAcumulaDecimos(true);
                        HibernateSessionFactory.getSession().flush();
                        
                        dialogStage.close();
                        
                        empleadoEditado(empleado);
                        
                        completado();

                        // Registro para auditar
                        String detalles = "marco que el empleado " 
                                + empleado.getNombre() + " " + empleado.getApellido() + " si acumula decimos";
                        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    }); 
                    buttonCancelar.setOnAction((ActionEvent e) -> {
                        dialogStage.close();
                        empleadoEditado(empleado);
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
    
    public void completado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Acumulación de decimos modificado con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
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
    
    public void imprimir(File file, Boolean noAcumulan, Boolean siAcumulan) {
        
        dialogWait();
        
        List<EmpleadoTable> empleadosParaImprimir = new ArrayList<>();
        
        for (EmpleadoTable empleadoTable: (List<EmpleadoTable>) 
                empleadosTableView.getItems()) {
            if (empleadoTable.getAcumulaDecimos()) {
                if (siAcumulan) {
                    empleadosParaImprimir.add(empleadoTable);
                }
            } else {
                if (noAcumulan) {
                    empleadosParaImprimir.add(empleadoTable);
                }
            }
        }
        
        ReporteAcumulacionDecimosVarios datasource = new ReporteAcumulacionDecimosVarios();
        datasource.addAll(empleadosParaImprimir);
        
        si = 0;
        no = 0;
        
        for (EmpleadoTable empleadoTable: empleadosParaImprimir) {
            if (empleadoTable.getAcumulaDecimos()) {
                 si ++;
             } else {
                 no ++;
             }
        }
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ACUMULACION_DECIMOS_EMPLEADOS);
        
            Map<String, Object> parametros = new HashMap();
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("si", String.valueOf(si));
            parametros.put("no", String.valueOf(no));
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint;
            if (empleadosParaImprimir.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "acumulado_decimos_" + System.currentTimeMillis();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            
            // Registro para auditar
            String detalles = "genero el recibo general de acumulacion de decimos de todos los empleado";
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
        dialogStage.setTitle("Decimos");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se completo la impresión."), buttonOk).
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
    
    public void dialogoImprimirTodos() {
        if (!empleadosTableView.getItems().isEmpty()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonSiDocumento = new MaterialDesignButton("Seleccionar ruta");
            Button buttonNoDocumento = new MaterialDesignButton("Salir");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Se imprimira el informe completo sobre decimos,"), 
                    new Text("Seleccione la ruta de guardado"), 
                    buttonSiDocumento, buttonNoDocumento).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonSiDocumento.setOnAction((ActionEvent e) -> {
                File file = seleccionarDirectorio();
                if (file != null) {
                    dialogStage.close();
                    imprimir(file, true, true);
                }
            });
            buttonNoDocumento.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.show();
        }
    }
    
    public void dialogoImprimirSiAcumulan() {
        if (!empleadosTableView.getItems().isEmpty()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonSiDocumento = new MaterialDesignButton("Seleccionar ruta");
            Button buttonNoDocumento = new MaterialDesignButton("Salir");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Se imprimira la informe de empleados que si acumulan decimos,"), 
                    new Text("Seleccione la ruta de guardado"), 
                    buttonSiDocumento, buttonNoDocumento).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonSiDocumento.setOnAction((ActionEvent e) -> {
                File file = seleccionarDirectorio();
                if (file != null) {
                    dialogStage.close();
                    imprimir(file, false, true);
                }
            });
            buttonNoDocumento.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.show();
        }
    }
    
    public void dialogoImprimirNoAcumulan() {
        if (!empleadosTableView.getItems().isEmpty()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonSiDocumento = new MaterialDesignButton("Seleccionar ruta");
            Button buttonNoDocumento = new MaterialDesignButton("Salir");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("Se imprimira el informe de empleados que no acumulan decimos,"), 
                    new Text("Seleccione la ruta de guardado"), 
                    buttonSiDocumento, buttonNoDocumento).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonSiDocumento.setOnAction((ActionEvent e) -> {
                File file = seleccionarDirectorio();
                if (file != null) {
                    dialogStage.close();
                    imprimir(file, true, false);
                }
            });
            buttonNoDocumento.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.show();
        }
    }
    
    @FXML
    public void dialogoImprimir(ActionEvent event) {
        if (!empleadosTableView.getItems().isEmpty()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_select.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonTodos = new MaterialDesignButton("TODOS");
            Button buttonNo = new MaterialDesignButton("NO ACUMULAN");
            Button buttonSi = new MaterialDesignButton("SI ACUMULAN");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("¿Que desea imprimir?"), 
                    buttonTodos, buttonNo, buttonSi).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            ///////////////////////// Boton 1
            buttonTodos.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
            buttonTodos.setMinHeight(25);
            buttonTodos.setMaxWidth(90);
            buttonTodos.setOnAction((ActionEvent e) -> {
                dialogoImprimirTodos();
                dialogStage.close();
            });
            buttonTodos.setOnMouseEntered((MouseEvent t) -> {
                buttonTodos.setStyle("-fx-background-color: #E0E0E0; "
                        + "-fx-text-fill: white;");
            });
            buttonTodos.setOnMouseExited((MouseEvent t) -> {
                buttonTodos.setStyle("-fx-background-color: #039BE5; "
                        + "-fx-text-fill: white;");
            });
            buttonTodos.setFont(Roboto.MEDIUM(9));
            ///////////////////////// Boton 2
            buttonNo.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
            buttonNo.setMinHeight(25);
            buttonNo.setMaxWidth(90);
            buttonNo.setOnAction((ActionEvent e) -> {
                dialogoImprimirNoAcumulan();
                dialogStage.close();
            });
            buttonNo.setOnMouseEntered((MouseEvent t) -> {
                buttonNo.setStyle("-fx-background-color: #E0E0E0; "
                        + "-fx-text-fill: white;");
            });
            buttonNo.setOnMouseExited((MouseEvent t) -> {
                buttonNo.setStyle("-fx-background-color: #039BE5; "
                        + "-fx-text-fill: white;");
            });
            buttonNo.setFont(Roboto.MEDIUM(9));
            ///////////////////////// Boton 3
            buttonSi.setStyle("-fx-background-color: #039BE5; "
                + "-fx-text-fill: white;");
            buttonSi.setMinHeight(25);
            buttonSi.setMaxWidth(90);
            buttonSi.setOnAction((ActionEvent e) -> {
                dialogoImprimirSiAcumulan();
                dialogStage.close();
            });
            buttonSi.setOnMouseEntered((MouseEvent t) -> {
                buttonSi.setStyle("-fx-background-color: #E0E0E0; "
                        + "-fx-text-fill: white;");
            });
            buttonSi.setOnMouseExited((MouseEvent t) -> {
                buttonSi.setStyle("-fx-background-color: #039BE5; "
                        + "-fx-text-fill: white;");
            });
            buttonSi.setFont(Roboto.MEDIUM(9));
            
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
                empleado.setAcumulaDecimos(user.getDetallesEmpleado()
                        .getAcumulaDecimos());
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
             empleado.setAcumulaDecimos(user.getDetallesEmpleado()
                     .getAcumulaDecimos());
             
            return empleado;
         }).forEach((empleado) -> {
             data.add(empleado);
         });
        empleadosTableView.setItems(data);

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
        
        decimosColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        decimosColumna.setCellFactory(param -> new TableCell<EmpleadoTable, EmpleadoTable>() {
            private final CheckBox checkBoxDeuda = new CheckBox();

            @Override
            protected void updateItem(EmpleadoTable empleado, boolean empty) {
                super.updateItem(empleado, empty);

                if (empleado == null) {
                    setGraphic(null);
                    return;
                }
                
                setGraphic(checkBoxDeuda);
                if (checkBoxDeuda != null)
                    checkBoxDeuda.setSelected(empleado.getAcumulaDecimos());
                checkBoxDeuda.setOnAction(event -> {
                     if (empleado.getAcumulaDecimos()) {
                         mostrarNoAcumulaDecimos(new UsuarioDAO().findById(empleado.getId()));
                     } else {
                         mostrarAcumulaDecimos(new UsuarioDAO().findById(empleado.getId()));
                     }
                });
            }
        });
        
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
        buttonCambiarVentana.setTooltip(
            new Tooltip("Ver acumulado")
        );
        buttonCambiarVentana.setOnMouseEntered((MouseEvent t) -> {
            buttonCambiarVentana.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cambiar_ventana.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonCambiarVentana.setOnMouseExited((MouseEvent t) -> {
            buttonCambiarVentana.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/cambiar_ventana.png'); "
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
