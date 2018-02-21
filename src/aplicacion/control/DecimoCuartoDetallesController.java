/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.reports.ReporteRolDecimoCuarto;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import aplicacion.control.util.MaterialDesignButton;
import aplicacion.control.util.Numeros;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.PagoDecimoDAO;
import hibernate.model.Empresa;
import hibernate.model.PagoDecimo;
import hibernate.model.RolIndividual;
import hibernate.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
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
public class DecimoCuartoDetallesController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Empresa empresa;
    
    private EmpleadoTable empleadoTable;
    
    private String periodoLiquidacion;
    
    // Columnas
    @FXML
    private TableColumn mesesColumna;
    
    @FXML
    private TableColumn cuartoColumna;
 
    @FXML
    private TableView rolesTableView;
    
    // Labels
    @FXML
    private Label empleadoLabel;
    
    @FXML
    private Label sueldoLabel;
    
    @FXML
    private Label inicioLabel;
    
    @FXML
    private Label periodoLabel;
    
    @FXML
    private Label valorLabel;
   
    @FXML
    private Label cobrarLabel;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Button buttonImprimir;
    
    @FXML
    private Button buttonBorrar;
    
    @FXML
    private Label textPago;
    
    public ObservableList<RolIndividual> data;
    
    ArrayList<RolIndividual> rolIndividuales;
    
    Date inicio;
    Date fin;
    Double devengado;
    Double valor;
    Double deduccion;
    Double aCobrar;
    Integer dias;
    Double sueldo;
    String year;
    
    Stage dialogLoading;
    
    public PagoDecimo pagoDecimo;
    
    public PagoDecimoCuartoAnualController pagoDecimoCuartoAnualController;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setPrograma(PagoDecimoCuartoAnualController pagoDecimoCuartoAnualController) {
        this.pagoDecimoCuartoAnualController = pagoDecimoCuartoAnualController;
    }
    
    @FXML
    public void pagarDecimos(ActionEvent event) {
        dialogConfirm();
    }
    
    public void dialogConfirm() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pagar Decimo Cuarto");
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
        children(new Text("¿Seguro que desea hacer el pago de decimos cuarto?"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            hacerPago();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.show();
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
    
    public void imprimir(File file, Boolean enviarCorreo, EmpleadoTable empleado) {
        PagoDecimo pagoDecimo = empleado.getPagoDecimo();
        Usuario user = pagoDecimo.getUsuario();

        ReporteRolDecimoCuarto datasource = new ReporteRolDecimoCuarto();
        datasource.addAll(empleado.getRolesInds());

        try {
            InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_DECIMO_CUARTO);

            Map<String, Object> parametros = new HashMap();
            parametros.put("numero", pagoDecimo.getId().toString()); 
            parametros.put("fecha_recibo", Fechas.getFechaConMes(Fechas.getFechaActual()));
            parametros.put("empleado", empleado.getApellido()+ " " + empleado.getNombre());
            parametros.put("cedula", empleado.getCedula());
            parametros.put("cargo", empleado.getUsuario().getDetallesEmpleado().getCargo().getNombre());
            parametros.put("empresa", empresa.getNombre());
            parametros.put("siglas", empresa.getSiglas());
            parametros.put("correo", "Correo: " + empresa.getEmail());
            parametros.put("detalles", 
                         "Ruc: " + empresa.getNumeracion() 
                    + " - Direccion: " + empresa.getDireccion() 
                    + " - Tel: " + empresa.getTelefono1());
            parametros.put("devengado", Numeros.round(pagoDecimo.getMonto()).toString());
            parametros.put("cobrar", Numeros.round(pagoDecimo.getMonto()).toString());
            parametros.put("lapso", "del 1 de Ene "+String.valueOf(Integer.valueOf(year)-1)
                         +" al 31 de Dic "+String.valueOf(Integer.valueOf(year)-1));

            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

            String filename = "pago_decimo_cuarto_" + pagoDecimo.getId();

            if (file != null) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
            } 
            if (enviarCorreo) {
                File pdf = File.createTempFile(filename, ".pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                        user.getEmail(), Const.ASUNTO_PAGO_DECIMO_CUARTO, 
                        "Recibo del pago del decimo cuarto acumulado desde el " 
                                + "1 de Ene "+String.valueOf(Integer.valueOf(year)-1) 
                                + " hasta el "
                                + "31 de Dic "+String.valueOf(Integer.valueOf(year)-1), 
                        pdf.getPath(), filename + ".pdf");
            }

        } catch (JRException | IOException ex) {
            Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        dialogoCompletado();
    }
    
    @FXML
    public void reimprimirRecibo(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Rol individua");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Ya se hizo el pago de vacaciones del empleado, \n"
                + " ¿Desea guardar el documento de pago nuevamente?."), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimir(file, enviarCorreo.isSelected(), empleadoTable);
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                imprimir(null, enviarCorreo.isSelected(), empleadoTable);
            } 
        });
        enviarCorreo.setSelected(true);
        dialogStage.showAndWait();
    }
    
    @FXML
    public void borrarPago(ActionEvent event) {
        
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
                
                confirmacionBorrado();
     
            } else {
                aplicacionControl.noPermitido();
            }
        }
    }
    
    public void confirmacionBorrado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Precaución");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
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
        children(new Text("¿Seguro que desea Borrar este Pago?"),
                 new Text("No podra recuperarlo!"), hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            dialogWait();
            hacerBorrado();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    void hacerBorrado() {
        
        if (pagoDecimo.getId() != null) {
            new PagoDecimoDAO().delete(pagoDecimo);
            HibernateSessionFactory.getSession().flush();
            String detalles = "elemino el pago de decimo cuarto numero " + pagoDecimo.getId()
                    + ", del empleado " + pagoDecimo.getUsuario().getNombre() 
                    + " " + pagoDecimo.getUsuario().getApellido();
            aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
            dialogLoading.close();
            stagePrincipal.close();
            
            empleadoTable.setPagoDecimo(null);
            empleadoTable.setPagado("No");
            empleadoTable.setPagar(false);
            pagoDecimoCuartoAnualController.updateTable();

            HibernateSessionFactory.getSession().clear();
            dialogoBorradoCompletado();
            
        } else {
            dialogLoading.close();
            dialogoBorradoError();  
        } 
         
    }
    
    public void hacerPago() {
        pagoDecimo = new PagoDecimo();
        pagoDecimo.setUsuario(empleadoTable.getUsuario());
        pagoDecimo.setFecha(new Timestamp(Integer.valueOf(year)-1-1899, 0, 1, 0, 0, 0, 0));
        pagoDecimo.setMonto(empleadoTable.getDecimo4());
        pagoDecimo.setDecimo(Const.DECIMO_CUARTO);
        new PagoDecimoDAO().save(pagoDecimo);

        empleadoTable.setPagoDecimo(pagoDecimo);
        empleadoTable.setPagado("Si");
        empleadoTable.setPagar(false);
        
        buttonPagar.setVisible(false);
        buttonBorrar.setVisible(true);
        buttonImprimir.setVisible(true);
        
        dialogoImprimir(null);
        
    }
    
    public void dialogoBorradoError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de Vacaciones");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("No se puede borrar este pago."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        buttonOk.prefWidth(80);
        dialogStage.showAndWait();
    }
    
    public void dialogoBorradoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de Vacaciones");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se borro el pago con exito."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        buttonOk.prefWidth(80);
        dialogStage.showAndWait();
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de Vacaciones");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Impresion de pago completada!."), buttonOk).
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
        dialogStage.setTitle("Imprimir pago");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar documento al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Pago de Decimo hecho con exito \n"
                + "¿Que imprimir el documento de pago?"), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                imprimir(file, enviarCorreo.isSelected(), empleadoTable);
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                imprimir(null, enviarCorreo.isSelected(), empleadoTable);
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
    
    public void setEmpleado(EmpleadoTable empleadoTable, String periodo, String year) {
        this.periodoLiquidacion = periodo;
        this.empleadoTable = empleadoTable;
        this.empresa = empleadoTable.getUsuario().getDetallesEmpleado().getEmpresa();
        this.year = year;
        
        if (empleadoTable.getPagoDecimo() == null) {
        
            devengado = 0d;
            valor = 0d;
            aCobrar = 0d;
            sueldo = empleadoTable.getUsuario().getDetallesEmpleado().getSueldo();
            for (RolIndividual rol: empleadoTable.getRolesInds()) {
                valor += rol.getDecimoCuarto();
            }

            aCobrar = valor ;

            sueldoLabel.setText("Sueldo: $"+sueldo.toString());
            valorLabel.setText("$"+Numeros.round(valor).toString());
            cobrarLabel.setText("$"+Numeros.round(aCobrar).toString());
            
            buttonPagar.setVisible(true);
            buttonBorrar.setVisible(false);
            buttonImprimir.setVisible(false);
            textPago.setVisible(false);
        
        } else {
            pagoDecimo = empleadoTable.getPagoDecimo();
            
            sueldoLabel.setText("Sueldo: $"+empleadoTable.getUsuario().getDetallesEmpleado().getSueldo().toString());
            valorLabel.setText("$"+Numeros.round(pagoDecimo.getMonto()).toString());
            cobrarLabel.setText("$"+Numeros.round(pagoDecimo.getMonto()).toString());
            
            buttonPagar.setVisible(false);
            buttonBorrar.setVisible(true);
            buttonImprimir.setVisible(true);
            textPago.setVisible(true);
            
        }

        periodoLabel.setText(periodoLiquidacion);
        inicioLabel.setText("A pagar en "+year);
        empleadoLabel.setText(empleadoTable.getApellido()+" "+empleadoTable.getNombre());
        
        rolIndividuales = new ArrayList<>();
        rolIndividuales.addAll(empleadoTable.getRolesInds());
        data = FXCollections.observableArrayList();
        data.addAll(rolIndividuales);
        
        rolesTableView.setItems(data);
        buttonPagar.setVisible(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        mesesColumna.setCellValueFactory(new Callback<TableColumn
                .CellDataFeatures<RolIndividual, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn
                    .CellDataFeatures<RolIndividual, String> data) {
                
                Fecha fec = new Fecha(data.getValue().getInicio());
                String fechaToTable = fec.getMonthName()+" "+fec.getAno();
                
                return new ReadOnlyStringWrapper(fechaToTable);
            }
        });
        cuartoColumna.setCellValueFactory(new PropertyValueFactory<>("decimoCuarto"));
        
        rolesTableView.setEditable(Boolean.FALSE);
        
        buttonPagar.setTooltip(
            new Tooltip("Pagar Vacaciones")
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
                    + "-fx-background-color: rgba(0,0,0,0.1);");
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
                    + "-fx-background-color: rgba(0,0,0,0.1);");
        });
        
        buttonBorrar.setTooltip(
            new Tooltip("Borrar Pago")
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
                    + "-fx-background-color: rgba(0,0,0,0.1);");
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
