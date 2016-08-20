/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.PagosTotalEmpleadoController.getToday;
import aplicacion.control.reports.ReporteRolDePagoIndividual;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.CorreoUtil;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Numeros.round;
import hibernate.HibernateSessionFactory;
import hibernate.dao.AbonoDeudaDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.RolClienteDAO;
import hibernate.dao.PagoMesDAO;
import hibernate.dao.PagoMesItemDAO;
import hibernate.dao.PagoQuincenaDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.AbonoDeuda;
import hibernate.model.Constante;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.RolCliente;
import hibernate.model.PagoMes;
import hibernate.model.PagoMesItem;
import hibernate.model.PagoQuincena;
import hibernate.model.RolIndividual;
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
import static aplicacion.control.util.Fechas.getFechaConMes;
import static aplicacion.control.util.Fechas.getFechaConMes;

/**
 *
 * @author Yornel
 */
public class PagoMensualController implements Initializable {
    
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
    private TableColumn sueldoColumna;
    
    @FXML 
    private TableColumn pagadoColumna;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    @FXML
    private Button buttonAtras;
    
    @FXML
    private Button buttonPagar;
    
    @FXML
    private Button buttonAnterior;
    
    @FXML
    private Button buttonSiguiente;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    public Timestamp inicio;
    public Timestamp fin;
    
    private ObservableList<EmpleadoTable> data;
    
    private ArrayList<EmpleadoTable> empleadosParaImprimir;
    private ArrayList<Usuario> usuariosParaImprimir;
    
    ArrayList<Usuario> usuarios;
    private Empresa empresa;
    
    Stage dialogLoading;
    
    Constante iess;
    
    String iessText = "IESS (0.0%)";
    
    ArrayList<PagoQuincena> pagosQuincena;
    
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
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        setTableInfo();
    }
    
    @FXML
    public void onClickLess(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerDe.getValue().plusMonths(1).minusDays(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        setTableInfo();
    }
    
    @FXML
    private void pagarATodos(ActionEvent event) throws ParseException {
        for (EmpleadoTable empleadoTable: 
                (List<EmpleadoTable>) empleadosTableView.getItems()) {
            if (checkBoxPagarTodos.isSelected()) {
                if (empleadoTable.getPagado().equalsIgnoreCase("Si")
                            || empleadoTable.getSinRoles()
                        || empleadoTable.getSueldo() < 0d) {
                    empleadoTable.setPagar(false);
                } else {
                    empleadoTable.setPagar(true);
                }
            } else {
                empleadoTable.setPagar(false);
            }
            data.set(data.indexOf(empleadoTable), empleadoTable);
        }
    }
    
    @FXML
    public void pagarAdelanto(ActionEvent event) {
        if (hayParaPagar()) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Pagar Rol Individual");
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
            children(new Text("¿Seguro que desea pagar el Rol Individual "
                    + "a estos empleado?"), hBox).
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
            dialogStage.showAndWait();
        } else {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Roles Individuales");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
            children(new Text("No hay empleados seleccionados para hacer el pago."), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            buttonOk.setPrefWidth(60);
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
            buttonOk.setOnKeyPressed((KeyEvent event1) -> {
                dialogStage.close();
            });
            dialogStage.showAndWait();
        }
    }
    
    public File seleccionarDirectorio() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Selecciona un directorio para guardar el recibo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
        return fileChooser.showDialog(stagePrincipal);
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
    
    public void imprimir(File file, Boolean enviarCorreo) {
        
        dialogWait();
        
        for (Usuario user: usuariosParaImprimir) {
            EmpleadoTable empleadoTable = empleadosParaImprimir
                    .get(usuariosParaImprimir.indexOf(user));
            if (empleadoTable.getPagar()) {
        
                ReporteRolDePagoIndividual datasource = new ReporteRolDePagoIndividual();
                datasource.addAll(empleadoTable.getPagoMesItems());

                try {
                    InputStream inputStream = new FileInputStream(Const.REPORTE_ROL_PAGO_INDIVIDUAL);

                    Map<String, String> parametros = new HashMap();
                    parametros.put("empleado", user.getNombre() + " " + user.getApellido());
                    parametros.put("cedula", user.getCedula());
                    parametros.put("cargo", user.getDetallesEmpleado().getCargo().getNombre());
                    parametros.put("empresa", user.getDetallesEmpleado().getEmpresa().getNombre());
                    parametros.put("numero", empleadoTable.getRolIndividual().getId().toString()); 
                    parametros.put("lapso", getFechaConMes(inicio) + " al " + getFechaConMes(fin));
                    parametros.put("total", round(empleadoTable.getSueldo()).toString());
                    parametros.put("fecha_recibo", Fechas.getFechaConMes(empleadoTable.getRolIndividual().getFecha()));

                    JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, datasource);

                    String filename = "rol_individual_" + empleadoTable.getRolIndividual().getId();

                    if (file != null) {
                        JasperExportManager.exportReportToPdfFile(jasperPrint, file.getPath() + "\\" + filename +".pdf"); 
                    } 
                    if (enviarCorreo) {
                        File pdf = File.createTempFile(filename, ".pdf");
                        JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));  
                        CorreoUtil.mandarCorreo(user.getDetallesEmpleado().getEmpresa().getNombre(), 
                                user.getEmail(), Const.ASUNTO_ROL_INDIVIDUAL, 
                                "Recibo de rol de pago del mes que empieza el " 
                                        + getFechaConMes(inicio) 
                                        + " y termina el " 
                                        + getFechaConMes(fin), 
                                pdf.getPath(), filename + ".pdf");
                    }

                } catch (JRException | IOException ex) {
                    Logger.getLogger(PagoMensualDetallesController.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        empleadosParaImprimir.clear();
        usuariosParaImprimir.clear();
        dialogLoading.close();
        dialogoCompletado();
    }
    
    public void hacerPago() {
        dialogWait();
        
        empleadosParaImprimir = new ArrayList<>();
        usuariosParaImprimir = new ArrayList<>();
        
        for (Usuario user: usuarios) {
            EmpleadoTable empleadoTable = data.get(usuarios.indexOf(user));
            empleadosParaImprimir.add(empleadoTable);
            usuariosParaImprimir.add(user);
            if (empleadoTable.getPagar() && empleadoTable.getSueldo() > 0d) {
                new RolIndividualDAO().save(empleadoTable.getRolIndividual());
        
                PagoMes pagoMes = new PagoMes();
                pagoMes.setFecha(new Timestamp(new Date().getTime()));
                pagoMes.setInicioMes(inicio);
                pagoMes.setFinMes(fin);
                pagoMes.setMonto(round(empleadoTable.getSueldo()));
                pagoMes.setUsuario(user);
                pagoMes.setRolIndividual(empleadoTable.getRolIndividual());
                new PagoMesDAO().save(pagoMes);
                
                for (PagoMesItem pago: empleadoTable.getPagoMesItems()) {
                    pago.setPagoMes(pagoMes);
                    new PagoMesItemDAO().save(pago);
                }
                
                for (Deuda deuda: empleadoTable.getDeudas()) {
                    Double montoAPagar = round(deuda.getRestante() / (double) deuda.getCuotas());
                    Integer newCuotas = deuda.getCuotas() - 1;
                    if (newCuotas == 0) {
                        deuda.setPagada(Boolean.TRUE);
                        deuda.setRestante(0d);  
                        deuda.setCuotas(newCuotas);
                    } else {
                        deuda.setPagada(Boolean.FALSE);
                        deuda.setRestante(round(deuda.getRestante() - montoAPagar));
                        deuda.setCuotas(newCuotas);
                    }
                    deuda.setUltimaModificacion(new Timestamp(new Date().getTime()));
                    HibernateSessionFactory.getSession().flush();

                    AbonoDeuda abonoDeuda = new AbonoDeuda();
                    abonoDeuda.setDeuda(deuda);
                    abonoDeuda.setFecha(new Timestamp(new Date().getTime()));
                    abonoDeuda.setMonto(montoAPagar);
                    abonoDeuda.setRestante(deuda.getRestante());
                    abonoDeuda.setPagoMes(pagoMes);
                    new AbonoDeudaDAO().save(abonoDeuda);  
                }
                
                // Registro para auditar
                String detalles = "genero el rol individual nro: " 
                        + empleadoTable.getRolIndividual().getId() 
                        + " del lapso " + getFechaConMes(inicio)+ " a " 
                        + getFechaConMes(fin) + " para el empleado " 
                        + empleadoTable.getNombre() + " " + empleadoTable.getApellido();
                aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
            }
        }
        setTableInfo();
        
        dialogLoading.close();
        dialogoGenerarRolCompletado();
    }
    
    public void dialogoGenerarRolCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Roles Individuales");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo al empleado");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Se generaron los roles de pago individual con exito, \n"
                + " ¿Desea guardar los documento de pago?."), 
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
            } else {
                dialogoCompletado();
            }
        });
        enviarCorreo.setSelected(true);
        dialogStage.show();
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Roles Individuales");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Completado."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonOk.setPrefWidth(60);
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void setEmpresa(Empresa empresa) throws ParseException {
        this.empresa = empresa;
        DateTime dateTime = new DateTime(getToday().getTime());
        inicio = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                .getMillis());
        fin = new Timestamp(dateTime.withDayOfMonth(empresa.getComienzoMes())
                .plusMonths(1).minusDays(1).getMillis());
        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        setTableInfo();
    }
    
    public void setTableInfo() {
        checkBoxPagarTodos.setSelected(false);
        
        iess = (Constante) new ConstanteDAO().findUniqueResultByNombre(Const.IESS);
        if (iess != null) 
            iessText = Const.IP_IESS + " (" + iess.getValor() + "%)";
        
        pagosQuincena = new ArrayList<>();
        pagosQuincena.addAll(new PagoQuincenaDAO()
                .findAllInDeterminateTime(fin));
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO().findAllByEmpresaIdActivo(empresa.getId()));
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
            empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
            calcularPago(empleado, user);
            if (!checkBoxPagarTodos.isSelected()) {
                empleado.setPagar(false);
            }
           
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);
        
        filtro();
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
                calcularPago(empleado, user);
                if (!checkBoxPagarTodos.isSelected()) {
                    empleado.setPagar(false);
                }
                data.set(data.indexOf(empleadoTable), empleado);
                return;
            }
        }
    }
    
    public Boolean hayParaPagar() {
        for (EmpleadoTable empleadoTable: data) {
            if (empleadoTable.getPagar()) {
                return true;
            }
        }
        return false;
    }
    
    public void calcularPago(EmpleadoTable empleadoTable, Usuario empleado) {
        RolClienteDAO pagoDAO = new RolClienteDAO();
        ArrayList<RolCliente> pagos = new ArrayList<>();
        ArrayList<PagoMesItem> pagoMesItems = new ArrayList<>();
        ArrayList<Deuda> deudasAPagar = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdConCliente(fin, 
                empleadoTable.getId()));
        if (pagos.isEmpty())
            pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdSinCliente(fin, 
                    empleadoTable.getId()));
        if (pagos.isEmpty()) 
            empleadoTable.setSinRoles(Boolean.TRUE);
        Integer diasTextValor = 0;
        Double normalesTextValor = 0d;
        Double suplementariasTextValor = 0d;
        Double sobreTiempoTextValor = 0d;
        Double sueldoTotalTextValor = 0d;
        Double extraTextValor = 0d;
        Double bonosTextValor = 0d;
        Double vacacionesTextValor = 0d;
        Double subTotalTextValor = 0d;
        Double decimosTotalTextValor = 0d;
        Double decimoTerceroTotalTextValor = 0d;
        Double decimoCuartoTotalTextValor = 0d;
        Double totalTextValor = 0d;
        Double montoSumplementariasTextValor = 0d;
        Double montoSobreTiempoTextValor = 0d;
        Double montoBonoTextValor = 0d;
        Double montoTransporteTextValor = 0d;
        Double montoJubilacionTextValor = 0d;
        Double montoAportePatronalTextValor = 0d;
        Double montoSegurosTextValor = 0d; 
        Double montoUniformasTextValor = 0d;
        Double ingresoValor = 0d;
        Double ieesValor = 0d;
        Double quincenaValor = 0d;
        Double deudasValor = 0d;
        Double deduccionesValor = 0d;
        Double aPercibirValor = 0d;
        
        for (RolCliente pago: pagos) {
            
            diasTextValor += pago.getDias();
            normalesTextValor += pago.getHorasNormales();
            suplementariasTextValor += pago.getHorasSuplementarias();
            sobreTiempoTextValor += pago.getHorasSobreTiempo();
            sueldoTotalTextValor += pago.getSalario();
            extraTextValor += (pago.getMontoHorasSuplementarias() 
                    + pago.getMontoHorasSobreTiempo());
            bonosTextValor += pago.getTotalBonos();
            vacacionesTextValor += pago.getVacaciones();
            subTotalTextValor += pago.getSubtotal();
            decimosTotalTextValor += (pago.getDecimoCuarto() 
                    + pago.getDecimoTercero());
            decimoTerceroTotalTextValor += pago.getDecimoTercero();
            decimoCuartoTotalTextValor += pago.getDecimoCuarto();
            totalTextValor += pago.getTotalIngreso();
            
            montoSumplementariasTextValor += pago.getMontoHorasSuplementarias();
            montoSobreTiempoTextValor += pago.getMontoHorasSobreTiempo();
            montoBonoTextValor += pago.getBono();
            montoTransporteTextValor += pago.getTransporte();
            montoJubilacionTextValor += pago.getJubilacionPatronal(); 
            montoAportePatronalTextValor += pago.getAportePatronal(); 
            montoSegurosTextValor += pago.getSeguros(); 
            montoUniformasTextValor += pago.getUniformes(); 
        }
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Sueldo");
            rol.setIngreso(round(sueldoTotalTextValor));
            rol.setDias(diasTextValor);
            rol.setHoras(normalesTextValor);
            rol.setClave(Const.IP_SUELDO);
            pagoMesItems.add(rol);
        }
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Horas Extras");
            rol.setIngreso(extraTextValor);
            rol.setHoras(suplementariasTextValor + sobreTiempoTextValor);
            rol.setClave(Const.IP_HORAS_EXTRAS);
            pagoMesItems.add(rol);
        }
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Bonos");
            rol.setIngreso(bonosTextValor);
            rol.setClave(Const.IP_BONOS);
            pagoMesItems.add(rol);
        }
        if (empleado.getDetallesEmpleado().getAcumulaDecimos()) {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor;
        } else {
            ingresoValor = sueldoTotalTextValor + extraTextValor + bonosTextValor 
                    + decimosTotalTextValor;
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Decimo Tercero");
                rol.setIngreso(round(decimoTerceroTotalTextValor));
                rol.setClave(Const.IP_DECIMO_TERCERO);
                pagoMesItems.add(rol);
            }
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Decimo Cuarto");
                rol.setIngreso(round(decimoCuartoTotalTextValor));
                rol.setClave(Const.IP_DECIMO_CUARTO);
                pagoMesItems.add(rol);
            }
        }
        ieesValor = (ingresoValor/100d) * getIess(); 
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion(iessText);
            rol.setDeduccion(round(ieesValor));
            rol.setClave(Const.IP_IESS);
            pagoMesItems.add(rol);
        }
        for (PagoQuincena pagoQuincena: pagosQuincena) {
            if (pagoQuincena.getUsuario().getId().equals(empleado.getId())) {
                quincenaValor = pagoQuincena.getMonto();
                {
                    PagoMesItem rol = new PagoMesItem();
                    rol.setDescripcion("Adelanto Quincenal");
                    rol.setDeduccion(quincenaValor);
                    rol.setClave(Const.IP_ADELANTO_QUINCENAL);
                    pagoMesItems.add(rol);
                }
                break;
            }
        }
        deudasValor = getDeudas(deudasAPagar, empleadoTable, pagoMesItems);
        deduccionesValor = ieesValor + quincenaValor + deudasValor;
        aPercibirValor = ingresoValor - deduccionesValor;
        
        RolIndividual pagoRol;
        pagoRol = new RolIndividualDAO().findByFechaAndEmpleadoIdAndDetalles(fin, 
                empleado.getId(), Const.ROL_PAGO_INDIVIDUAL);
        
        if (pagoRol == null) {
            pagoRol = new RolIndividual();
            pagoRol.setDetalles(Const.ROL_PAGO_INDIVIDUAL);
            pagoRol.setFecha(new Timestamp(new Date().getTime()));
            pagoRol.setInicio(inicio);
            pagoRol.setFinalizo(fin);
            pagoRol.setDias(diasTextValor);
            pagoRol.setHorasNormales(normalesTextValor);
            pagoRol.setHorasSuplementarias(suplementariasTextValor);  // RC
            pagoRol.setHorasSobreTiempo(sobreTiempoTextValor);        // ST
            pagoRol.setTotalHorasExtras(sobreTiempoTextValor 
                    + suplementariasTextValor);
            pagoRol.setSalario(sueldoTotalTextValor);
            pagoRol.setMontoHorasSuplementarias(montoSumplementariasTextValor);
            pagoRol.setMontoHorasSobreTiempo(montoSobreTiempoTextValor);
            pagoRol.setBono(montoBonoTextValor);
            pagoRol.setTransporte(montoTransporteTextValor);
            pagoRol.setTotalBonos(bonosTextValor);
            pagoRol.setVacaciones(vacacionesTextValor);
            pagoRol.setSubtotal(subTotalTextValor);
            pagoRol.setDecimoTercero(decimoTerceroTotalTextValor);
            pagoRol.setDecimoCuarto(decimoCuartoTotalTextValor);
            pagoRol.setJubilacionPatronal(montoJubilacionTextValor);
            pagoRol.setAportePatronal(montoAportePatronalTextValor);
            pagoRol.setSeguros(montoSegurosTextValor);
            pagoRol.setUniformes(montoUniformasTextValor);
            pagoRol.setTotalIngreso(totalTextValor);
            pagoRol.setEmpleado(empleado.getNombre() + " " + empleado.getApellido());
            pagoRol.setCedula(empleado.getCedula());
            pagoRol.setEmpresa(empleado.getDetallesEmpleado().getEmpresa().getNombre());
            pagoRol.setSueldo(empleado.getDetallesEmpleado().getSueldo());
            pagoRol.setUsuario(empleado);
            if (empleado.getDetallesEmpleado().getAcumulaDecimos()) 
                pagoRol.setDecimosPagado(Boolean.FALSE);
            else 
                pagoRol.setDecimosPagado(Boolean.TRUE);
            
            empleadoTable.setPagado("No");
            empleadoTable.setPagar(true);
        } else {
           empleadoTable.setPagado("Si");
           empleadoTable.setPagar(false);
           PagoMes pagoMes = new PagoMesDAO()
                   .findInDeterminateTimeByUsuarioId(fin, empleado.getId());
           aPercibirValor = pagoMes.getMonto();
        }
        
        empleadoTable.setSueldo(round(aPercibirValor));
        empleadoTable.setDeudas(deudasAPagar);
        empleadoTable.setPagoMesItems(pagoMesItems);
        empleadoTable.setRolIndividual(pagoRol);
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
    
    public void mostrarPagoMensualDetalles(Usuario empleado) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoMensualDetalles.fxml"));
            AnchorPane ventanaAuditar = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Pago para el empleado " + empleado.getNombre() + " " 
                    + empleado.getApellido());
            String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaAuditar);
            ventana.setScene(scene);
            PagoMensualDetallesController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(aplicacionControl);
            controller.setPagoMensualController(this);
            controller.setEmpleado(empleado, inicio, fin);
            ventana.show();

        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        } 
    }
    
    public void mostrarPagado(RolIndividual rolIndividual) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaPagoMensualPagado.fxml"));
            AnchorPane ventanaAuditar = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Pago para el empleado " + rolIndividual.getUsuario().getNombre() + " " 
                    + rolIndividual.getUsuario().getApellido());
            String stageIcon = AplicacionControl.class.getResource("imagenes/security_dialog.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaAuditar);
            ventana.setScene(scene);
            PagoMensualPagadoController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(aplicacionControl);
            controller.setPagoMensualController(this);
            controller.setPago(rolIndividual, rolIndividual.getInicio(), rolIndividual.getFinalizo());
            ventana.show();

        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        } 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.FALSE);
        
        cedulaColumna.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
       
        apellidoColumna.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        
        departamentoColumna.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        sueldoColumna.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        
        pagadoColumna.setCellValueFactory(new PropertyValueFactory<>("pagado"));
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<EmpleadoTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EmpleadoTable rowData = row.getItem();
                    if (rowData.getPagado().equalsIgnoreCase("Si")) {
                        mostrarPagado(rowData.getRolIndividual());
                    } else {
                        mostrarPagoMensualDetalles(new UsuarioDAO()
                                .findById(rowData.getId()));
                    }
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
                    if (empleadoTable.getPagado().equalsIgnoreCase("Si")
                            || empleadoTable.getSinRoles() 
                            || empleadoTable.getSueldo() < 0d) {
                        checkBoxpagar.setDisable(true);
                        empleadoTable.setPagar(false);
                    } else {
                        checkBoxpagar.setDisable(false);
                    }
                    checkBoxpagar.setSelected(empleadoTable.getPagar());
                }
                checkBoxpagar.setOnAction(event -> {
                     empleadoTable.setPagar(checkBoxpagar.isSelected());
                     //guardar();
                });
            } 
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
        buttonPagar.setTooltip(
            new Tooltip("Pagar a seleccionados")
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
    } 
    
    public double getIess() {
        if (iess == null) {
            return 0.0;
        } else {
            return Double.valueOf(iess.getValor());
        }
    }
    
    public Double getDeudas(ArrayList<Deuda> deudasAPagar, 
            EmpleadoTable empleadoTable, ArrayList<PagoMesItem> pagoMesItems) {
        Double monto = 0d;
        deudasAPagar.addAll(new DeudaDAO()
                .findAllByUsuarioIdNoPagadaSinAplazar(empleadoTable.getId()));
        for (Deuda deuda: deudasAPagar) {
            monto += (deuda.getRestante() / deuda.getCuotas());
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Deuda - " + deuda.getTipo());
                rol.setDeduccion(deuda.getRestante() / deuda.getCuotas());
                rol.setClave(Const.IP_DEUDA);
                pagoMesItems.add(rol);
            }
        }
        return monto;
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
