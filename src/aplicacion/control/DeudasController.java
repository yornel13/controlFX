/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteDeudas;
import aplicacion.control.tableModel.DeudaTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import static aplicacion.control.util.Numeros.round;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.CuotaDeudaDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.DeudaTipoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.CuotaDeuda;
import hibernate.model.Deuda;
import hibernate.model.DeudaTipo;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ChoiceBox;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
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
public class DeudasController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    private Usuario empleado;
    
    // Columnas
    @FXML
    private TableColumn fechaColumna;
    
    @FXML
    private TableColumn detallesColumna;
    
    @FXML
    private TableColumn montoColumna;
    
    @FXML
    private TableColumn restanteColumna;
    
    @FXML
    private TableColumn cuotasColumna;
    
    @FXML
    private TableColumn tipoColumna;
    
    @FXML
    private TableColumn<DeudaTable, DeudaTable> borrarColumna;
    
    @FXML
    private Button buttonAgregar;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private ChoiceBox choiceBoxCuenta;
    
    @FXML
    private TableView deudasTableView;
    
    public ObservableList<DeudaTable> data;
    
    ArrayList<Deuda> deudas;
    
    Stage dialogLoading;
    
    ArrayList<DeudaTipo> tiposDeudas;
    
    private Fecha fecha;
    
    public DeudasEmpleadosController deudasEmpleadosController;
    public PagoMensualDetallesController pagoMensualDetallesController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setProgramaDeudas(DeudasEmpleadosController deudasEmpleadosController) {
        this.deudasEmpleadosController = deudasEmpleadosController;
        this.fecha = Fechas.getFechaActual();
    }
    
    public void setProgramaDeudas(PagoMensualDetallesController pagoMensualDetallesController, Fecha fecha) {
        this.pagoMensualDetallesController = pagoMensualDetallesController;
        this.fecha = fecha;
    }
    
    @FXML
    private void agregarDeuda(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.CREAR)) {
               
               nuevaDeuda();
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void nuevaDeuda() {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.CREAR)) {
               
                String[] itemsTipos = new String[tiposDeudas.size() + 1];
                tiposDeudas.stream().forEach((deudaTipo) -> {
                    itemsTipos[tiposDeudas.indexOf(deudaTipo)] = deudaTipo.getNombre();
                });
                itemsTipos[tiposDeudas.size()] = "+ CREAR";
                
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Nueva Deuda");
                String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button buttonConfirmar = new Button("Crear");
                ChoiceBox choiceBoxTipos = new ChoiceBox();
                TextField fieldDetalles = new TextField();
                TextField fieldMonto = new TextField();
                TextField fieldCuotas = new TextField();
                ChoiceBox selectorMes = new ChoiceBox();
                ChoiceBox selectorAno = new ChoiceBox();

                selectorMes.setItems(Fechas.arraySpinnerMes());
                selectorAno.setItems(Fechas.arraySpinnerAno());

                selectorMes.getSelectionModel().select(fecha.getMes());
                selectorAno.getSelectionModel().select(fecha.getAno());

                HBox hBox = HBoxBuilder.create()
                        .spacing(10.0) //In case you are using HBoxBuilder
                        .padding(new Insets(0, 5, 5, 5))
                        .alignment(Pos.CENTER)
                        .children(selectorMes, selectorAno)
                        .build();
                hBox.maxWidth(120);
                
                Text textTipo = new Text("Tipo");
                Text textDetalles = new Text("Detalles");
                Text textMonto = new Text("Monto");
                Text textCuotas = new Text("Cuotas");
                Text textFecha = new Text("Fecha de pago (a partir de)");
                
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(textTipo, choiceBoxTipos, textDetalles, fieldDetalles, textMonto, 
                        fieldMonto, textCuotas, fieldCuotas, textFecha, hBox, buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                choiceBoxTipos.setItems(FXCollections.observableArrayList(itemsTipos));
                fieldMonto.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
                fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
                choiceBoxTipos.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue.intValue() == (tiposDeudas.size())) {
                            agregarDeuda();
                            dialogStage.close();
                        } else
                            fieldCuotas.setText(tiposDeudas.get(newValue.intValue()).getCuotas().toString());
                    }
                });
                buttonConfirmar.setOnAction((ActionEvent e) -> {
                    
                    if (choiceBoxTipos.getSelectionModel().isEmpty()) {
                        
                    } else if (fieldMonto.getText().isEmpty()) {
                        
                    } else if (fieldCuotas.getText().isEmpty()) {
                        
                    } else {
                        
                        String tipo = choiceBoxTipos.getSelectionModel().getSelectedItem().toString();
                        String detalles = fieldDetalles.getText();
                        String monto = fieldMonto.getText();
                        String cuotas = fieldCuotas.getText();
                        
                        Deuda newDeuda = new Deuda();
                        newDeuda.setTipo(tipo);
                        newDeuda.setTipoDeuda(tiposDeudas.get(choiceBoxTipos
                                .getSelectionModel().getSelectedIndex()));
                        newDeuda.setDetalles(detalles);
                        newDeuda.setMonto(Double.valueOf(monto));
                        newDeuda.setCuotas(Integer.parseInt(cuotas));
                        newDeuda.setCuotasTotal(Integer.parseInt(cuotas));
                        newDeuda.setPagada(Boolean.FALSE);
                        newDeuda.setAplazar(Boolean.FALSE);
                        newDeuda.setRestante(Double.valueOf(monto));
                        newDeuda.setCreacion(new Timestamp(new Date().getTime()));
                        newDeuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                        newDeuda.setUsuario(empleado);
                        
                        new DeudaDAO().save(newDeuda);
                        new UsuarioDAO().getSession().refresh(newDeuda.getUsuario());
                        
                        int numeroC = newDeuda.getCuotas();
                
                        Double montoCuotas = newDeuda.getRestante() / numeroC;
                        montoCuotas = round(montoCuotas);

                        Fecha fecha = new Fecha(selectorAno, selectorMes, "30");

                        for (int number = 0; numeroC > number; number++) {

                            CuotaDeuda cuota = new CuotaDeuda();
                            cuota.setFecha(fecha.plusMonths(number).getFecha());
                            cuota.setMonto(montoCuotas);
                            cuota.setPagoMes(null);
                            cuota.setCreado(Fechas.getToday());
                            cuota.setEditado(Fechas.getToday());
                            cuota.setAplazado(Boolean.FALSE);
                            cuota.setDeuda(newDeuda);

                            new CuotaDeudaDAO().save(cuota);

                        }
                        
                        dialogStage.close();
                        
                        String detalle = "agrego una deudo al empleado " 
                            + empleado.getApellido()+ " " + empleado.getNombre()
                                + " del tipo " +tipo
                                + " por $" +Double.valueOf(monto);
                        aplicacionControl.au.saveElimino(detalle, aplicacionControl.permisos.getUsuario());
                        
                        setEmpleado(empleado);
                        if (deudasEmpleadosController != null)
                            deudasEmpleadosController.empleadoEditado(empleado);
                        else 
                            pagoMensualDetallesController.setInfoEditada(pagoMensualDetallesController.inicio, 
                                        pagoMensualDetallesController.fin, 
                                        pagoMensualDetallesController.empleado.getId());
                        
                        
                        generacionCompletada();
                    }
                });  
                dialogStage.showAndWait();
                
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    private void agregarDeuda() {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Nuevo tipo de deuda");
                String stageIcon = AplicacionControl.class.getResource("imagenes/icon_crear.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button cambiarValor = new Button("Agregar");
                TextField field = new TextField();
                TextField fieldCuotas = new TextField();
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("Escriba el nombre del tipo de deuda"), field, 
                        new Text("Cuotas por defecto"), fieldCuotas, cambiarValor).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                field.setPrefWidth(150);
                fieldCuotas.setPrefWidth(50);
                fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
                cambiarValor.setPrefWidth(100);
                dialogStage.show();
                cambiarValor.setOnAction((ActionEvent e) -> {
                    if (field.getText().isEmpty()) {

                    } else if (fieldCuotas.getText().isEmpty()) {

                    } else {
                        DeudaTipo deudaTipo = new DeudaTipo();
                        deudaTipo.setActivo(Boolean.TRUE);
                        deudaTipo.setNombre(field.getText());
                        deudaTipo.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                        new DeudaTipoDAO().save(deudaTipo);
                        dialogStage.close();
                        nuevaDeuda();

                        // Registro para auditar
                        String detalles = "agrego el tipo de deuda " 
                                + deudaTipo.getNombre();
                        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    }
                }); 
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void generacionCompletada() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda creada satisfactoriamente"), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void deleteDeuda(Deuda deuda, DeudaTable deudaTable) {
        if (Objects.equals(deuda.getMonto(), deuda.getRestante())) {
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Confirmacion");
            String stageIcon = AplicacionControl.class
                    .getResource("imagenes/icon_editar.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            MaterialDesignButton buttonOk = new MaterialDesignButton("Si");
            MaterialDesignButton buttonNo = new MaterialDesignButton("no");
            HBox hBox = HBoxBuilder.create()
                    .spacing(10.0) //In case you are using HBoxBuilder
                    .padding(new Insets(5, 5, 5, 5))
                    .alignment(Pos.CENTER)
                    .children(buttonOk, buttonNo)
                    .build();
            hBox.maxWidth(120);
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("¿Seguro que desea eliminar esta deuda?"), hBox).
            alignment(Pos.CENTER).padding(new Insets(20)).build()));
            buttonOk.setMinWidth(50);
            buttonNo.setMinWidth(50);
            buttonOk.setOnAction((ActionEvent e) -> {
                borrarDeuda(deuda, deudaTable);
                dialogStage.close();
            });
            buttonNo.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
            
        } else {
            borradoFallido();
        }
        
    }
    
    void borrarDeuda(Deuda deuda, DeudaTable deudaTable) {
        new CuotaDeudaDAO().deleteByDeudaId(deuda.getId());
        HibernateSessionFactory.getSession().flush();
        new DeudaDAO().delete(deuda);
        new UsuarioDAO().getSession().refresh(deuda.getUsuario());
        HibernateSessionFactory.getSession().flush();
        data.remove(deudaTable);
        deudas.remove(deuda);
        if (deudasEmpleadosController != null)
            deudasEmpleadosController.empleadoEditado(empleado);   
        else
            pagoMensualDetallesController.setInfoEditada(pagoMensualDetallesController.inicio, 
                        pagoMensualDetallesController.fin, 
                        pagoMensualDetallesController.empleado.getId());
        
        // Registro para auditar
        String detalles = "elimino la deuda del empleado " 
                + deuda.getUsuario().getApellido()+ " " 
                + deuda.getUsuario().getNombre()
                + " del tipo " + deuda.getTipo()
                + " y monto $" + deuda.getMonto();
        aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());

        borradoCompleto();
        
    }
    
    public void cambiarCuotasDeuda(Deuda deuda, DeudaTable deudaTable) {
        if (Objects.equals(deuda.getMonto(), 0d)) {
            
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Modificar Cuotas");
            String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("Modificar");
            TextField fieldCuotas = new TextField();
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Ingrese la cantidad de cuotas:"), fieldCuotas, buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            fieldCuotas.setMaxWidth(50);
            fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
            fieldCuotas.setText(deuda.getCuotas().toString());
            buttonOk.setOnAction((ActionEvent e) -> {
                
                if (fieldCuotas.getText() != null) {
                    deuda.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                    deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                    HibernateSessionFactory.getSession().flush();
                    new UsuarioDAO().getSession().refresh(deuda.getUsuario());
                    data.set(data.indexOf(deudaTable), deudaTable);
                    dialogStage.close();
                    if (deudasEmpleadosController != null)
                        deudasEmpleadosController.empleadoEditado(empleado);
                        
                    // Registro para auditar
                    String detalles = "edito las cuotas de la deuda '" + deuda.getDetalles() + "' del empleado " 
                            + deuda.getUsuario().getNombre() + " " 
                            + deuda.getUsuario().getApellido();
                    aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());
                    
                    edicionCompletada();
                }
            });
            dialogStage.showAndWait();
        }
    }
    
    public void edicionCompletada() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda editada satisfactoriamente."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void borradoFallido() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Error");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Esta deuda ya no se puede eliminar."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        ReporteDeudas datasource = new ReporteDeudas();
        List<Deuda> deudasImprimir = (List<Deuda>) new DeudaDAO().findAllByEmpleadoId(empleado.getId());
        datasource.addAll(deudasImprimir);
        
        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_DEUDAS_EMPLEADO);
        
            Map<String, String> parametros = new HashMap();
            parametros.put("empleado", empleado.getApellido()+ " " + empleado.getNombre());
            parametros.put("cedula", empleado.getCedula());
            parametros.put("cargo", empleado.getDetallesEmpleado().getCargo().getNombre());
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            
            JasperPrint jasperPrint;
            if (deudasImprimir.isEmpty())
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);
            
            String filename = "deudas_" + empleado.getNombre();
            
            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            if (enviarCorreo) {
                File pdf = File.createTempFile(filename, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                CorreoUtil.mandarCorreo(empleado.getDetallesEmpleado().getEmpresa().getNombre(), 
                        empleado.getEmail(), Const.ASUNTO_DEUDAS, 
                        "Reportes de deudas", 
                        pdf.getPath(), filename + ".pdf");
            }
            
            // Registro para auditar
            String detalles = "genero el recibo de deudas del empleado "
                    + empleado.getApellido()+ " " + empleado.getNombre();
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
        dialogStage.setTitle("Imprimir deudas");
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
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Imprimir Deudas");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar documento al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("¿Que desea hacer?"), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimir(file, enviarCorreo.isSelected());
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                imprimir(null, enviarCorreo.isSelected());
            } 
        });
        enviarCorreo.setSelected(true);
        dialogStage.showAndWait();
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
    
    public void borradoCompleto() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Deuda eliminada con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
        this.empresa = empleado.getDetallesEmpleado().getEmpresa();
        DeudaDAO deudaDao = new DeudaDAO();
        deudas = new ArrayList<>();
        deudas.addAll(deudaDao.findAllByEmpleadoId(empleado.getId()));
        filterCuenta();
    }
    
    
    void filterCuenta() {
        
        data = FXCollections.observableArrayList(); 
        
        boolean all = choiceBoxCuenta.getSelectionModel().getSelectedIndex() == 0;
        
        deudas.stream().forEach((deuda) -> {
            DeudaTable deudaTable = new DeudaTable();
            deudaTable.setId(deuda.getId());
            deudaTable.setCreacion(deuda.getCreacion());
            deudaTable.setUltimaModificacion(deuda.getUltimaModificacion());
            deudaTable.setMonto(deuda.getMonto());
            deudaTable.setRestante(deuda.getRestante());
            deudaTable.setAplazar(deuda.getAplazar());
            deudaTable.setPagada(deuda.getPagada());
            deudaTable.setCuotas(deuda.getCuotasTotal());
            deudaTable.setTipo(deuda.getTipo());
            deudaTable.setDetalles(deuda.getDetalles());
            deudaTable.setFecha(Fechas.getFechaConMes(deuda.getCreacion()));
            
            if (all)
                data.add(deudaTable); 
            else {
                DeudaTipo deudaTipo = tiposDeudas.get(choiceBoxCuenta
                        .getSelectionModel().getSelectedIndex()-1);
                if (deudaTipo.getId().equals(deuda.getTipoDeuda().getId())) {
                    data.add(deudaTable); 
                }
            }
        });
        deudasTableView.setItems(data);
    }
    
    public void mostrarDeuda(Deuda deuda, DeudaTable deudaTable) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.GESTION, Permisos.Nivel.VER)) {
                try {
                    FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaDeuda.fxml"));
                    AnchorPane ventanaDeudas = (AnchorPane) loader.load();
                    Stage ventana = new Stage();
                    ventana.setTitle("Deuda de " + empleado.getApellido()+ " " + empleado.getNombre());
                    String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
                    ventana.getIcons().add(new Image(stageIcon));
                    ventana.setResizable(false);
                    ventana.initOwner(stagePrincipal);
                    Scene scene = new Scene(ventanaDeudas);
                    ventana.setScene(scene);
                    DeudaController controller = loader.getController();
                    controller.setStagePrincipal(ventana);
                    controller.setProgramaPrincipal(aplicacionControl);
                    controller.setProgramaDeudas(this);
                    controller.setDeuda(deuda, deudaTable);
                    ventana.show();
 
                } catch (Exception e) {
                    e.printStackTrace();
                    //tratar la excepción
                }
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        detallesColumna.setCellValueFactory(new PropertyValueFactory<>("detalles"));
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("monto"));
        restanteColumna.setCellValueFactory(new PropertyValueFactory<>("restante"));
        cuotasColumna.setCellValueFactory(new PropertyValueFactory<>("cuotas"));
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        borrarColumna.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        borrarColumna.setCellFactory(param -> new TableCell<DeudaTable, DeudaTable>() {
            private final Button borrarButton = new Button("Borrar");

            @Override
            protected void updateItem(DeudaTable deuda, boolean empty) {
                super.updateItem(deuda, empty);

                if (deuda == null) {
                    setGraphic(null);
                    getTableRow().setStyle("");
                    return;
                }

                setGraphic(borrarButton);
                borrarButton.setOnAction(event -> {
                    deleteDeuda(new DeudaDAO().findById(deuda.getId()), deuda);
                });
                
                if (deuda.getPagada()) {
                    getTableRow().setStyle("-fx-background-color:#A5D6A7");
                } else {
                    getTableRow().setStyle("");
                }
            }
        });
        
        deudasTableView.setEditable(Boolean.FALSE);
        
        deudasTableView.setRowFactory( (Object tv) -> {
            TableRow<DeudaTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    DeudaTable rowData = row.getItem();
                    mostrarDeuda(new DeudaDAO().findById(rowData.getId()), rowData);
                }
            });
            return row ;
        });
        buttonAgregar.setTooltip(
            new Tooltip("Agregar deuda")
        );
        buttonAgregar.setOnMouseEntered((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonAgregar.setOnMouseExited((MouseEvent t) -> {
            buttonAgregar.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/agregar.png'); "
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
        
        tiposDeudas = (ArrayList<DeudaTipo>) new DeudaTipoDAO().findAll();
        String[] itemsTposDeuda = new String[tiposDeudas.size()+1];
        itemsTposDeuda[0] = "-> TODOS <-";
        tiposDeudas.stream().forEach((obj) -> {
            itemsTposDeuda[tiposDeudas.indexOf(obj)+1] = obj.getNombre();
        });
        choiceBoxCuenta.setItems(FXCollections.observableArrayList(itemsTposDeuda)); 
        choiceBoxCuenta.getSelectionModel().selectFirst();
        choiceBoxCuenta.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, 
                    Number oldValue, Number newNalue) {
                filterCuenta();
            }
        });
    } 
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
    
    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
    }
}
