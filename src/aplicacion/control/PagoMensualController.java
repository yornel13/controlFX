/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.PagosTotalEmpleadoController.getToday;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.tableModel.PagosTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import static aplicacion.control.util.Numeros.round;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.DeudaDAO;
import hibernate.dao.PagoDAO;
import hibernate.dao.PagoQuincenaDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Constante;
import hibernate.model.Deuda;
import hibernate.model.Empresa;
import hibernate.model.Pago;
import hibernate.model.PagoMesItem;
import hibernate.model.PagoQuincena;
import hibernate.model.RolIndividual;
import hibernate.model.Usuario;
import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class PagoMensualController implements Initializable {
    
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
    private TableColumn sueldoColumna;
    
    @FXML 
    private TableColumn pagadoColumna;
    
    @FXML
    private CheckBox checkBoxPagarTodos;
    
    @FXML 
    private TableColumn<EmpleadoTable, EmpleadoTable> pagarColumna;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    public Timestamp inicio;
    public Timestamp fin;
    
    private ObservableList<EmpleadoTable> data;
    
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
    private void returnConfiguracion(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    @FXML
    public void onClickMore(ActionEvent event) {
        pickerDe.setValue(pickerDe.getValue().plusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().plusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());  
        
        //setTableInfo(inicio, fin, empleado.getId());
        
    }
    
    @FXML
    public void onClickLess(ActionEvent event)  {
        pickerDe.setValue(pickerDe.getValue().minusMonths(1));
        pickerHasta.setValue(pickerHasta.getValue().minusMonths(1));
        inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
        fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
        
        //setTableInfo(inicio, fin, empleado.getId());
        
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
            empleado.setId(user.getId());
            empleado.setNombre(user.getNombre());
            empleado.setApellido(user.getApellido());
            empleado.setCedula(user.getCedula());
            empleado.setEmpresa(user.getDetallesEmpleado().getEmpresa().getNombre());
            empleado.setTelefono(user.getTelefono());
            empleado.setDepartamento(user.getDetallesEmpleado().getDepartamento().getNombre());
            empleado.setCargo(user.getDetallesEmpleado().getCargo().getNombre());
            calcularPago(empleado, user);

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
            hacerPago();

        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        dialogStage.showAndWait();
    }
    
    public void dialogoOpciones() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pagar Adelanto Quincenal");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonSiDocumento = new Button("Guardar Documento");
        Button buttonNoDocumento = new Button("No Guardar");
        CheckBox enviarCorreo = new CheckBox("Enviar correo a los empleados");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Desea guardar una copia de los adelantos quincenal?."), 
                buttonSiDocumento, buttonNoDocumento, enviarCorreo).
        alignment(Pos.CENTER).padding(new Insets(10)).build()));
        buttonSiDocumento.setOnAction((ActionEvent e) -> {
            File file = seleccionarDirectorio();
            if (file != null) {
                dialogStage.close();
                //imprimir(file, enviarCorreo.isSelected());
            }
        });
        buttonNoDocumento.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            if (enviarCorreo.isSelected()) {
                //imprimir(null, enviarCorreo.isSelected());
            } else {
                dialogoCompletado();
            }
        });
        enviarCorreo.setSelected(true);
        dialogStage.showAndWait();
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
    
    public void hacerPago() {
        dialogWait();
        for (Usuario user: usuarios) {
            if (data.get(usuarios.indexOf(user)).getPagar()) {
                PagoQuincena pagoQuincena = new PagoQuincena();
                pagoQuincena.setUsuario(user);
                pagoQuincena.setFecha(new Timestamp(new Date().getTime()));
                pagoQuincena.setMonto(data.get(usuarios.indexOf(user))
                        .getQuincenal());
                new PagoQuincenaDAO().save(pagoQuincena);
            }
        }
        try {
            setEmpresa(empresa);
        } catch (ParseException ex) {
            Logger.getLogger(PagoMensualController.class.getName()).log(Level.SEVERE, null, ex);
        }
        dialogLoading.close();
        dialogoCompletado();
    }
    
    public void dialogoCompletado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Pago de adelanto Quincenal");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(20).
        children(new Text("Pago de adelanto quincenal hecho con exito."), buttonOk).
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
        
        DateTime dateTime = new DateTime();
        fin = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).getMillis());
        inicio = new Timestamp(dateTime.withDayOfMonth(empresa.getDiaCortePago()).minusMonths(1).plusDays(1).getMillis());

        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        iess = (Constante) new ConstanteDAO().findUniqueResultByNombre(Const.IESS);
        if (iess != null) 
            iessText = Const.IP_IESS + " (" + iess.getValor() + "%)";
        
        pagosQuincena = new ArrayList<>();
        pagosQuincena.addAll(new PagoQuincenaDAO()
                .findAllInDeterminateTime(inicio, fin));
        
        usuarios = new ArrayList<>();
        usuarios.addAll(new UsuarioDAO().findByEmpresaIdActivo(empresa.getId()));
        
        pagosQuincena = new ArrayList<>();
        pagosQuincena.addAll(new PagoQuincenaDAO()
                .findAllInDeterminateTime(inicio, fin));
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
           
            return empleado;
        }).forEach((empleado) -> {
            data.add(empleado);
        });
        empleadosTableView.setItems(data);
        checkBoxPagarTodos.setSelected(true);

        filtro();
    }
    
    public void calcularPago(EmpleadoTable empleadoTable, Usuario empleado) {
        PagoDAO pagoDAO = new PagoDAO();
        ArrayList<Pago> pagos = new ArrayList<>();
        ArrayList<PagosTable> pagosTable = new ArrayList<>();
        ArrayList<PagoMesItem> pagoMesItems = new ArrayList<>();
        ArrayList<Deuda> deudasAPagar = new ArrayList<>();
        pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdConCliente(fin, 
                empleadoTable.getId()));
        if (pagos.isEmpty())
            pagos.addAll(pagoDAO.findAllByFechaAndEmpleadoIdSinCliente(fin, 
                    empleadoTable.getId()));
        
        Integer diasTextValor = 0;
        Integer normalesTextValor = 0;
        Integer suplementariasTextValor = 0;
        Integer sobreTiempoTextValor = 0;
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
        
        for (Pago pago: pagos) {
            PagosTable pagoTable = new PagosTable();
            pagoTable.setId(pago.getId());
            if (pago.getClienteNombre() == null)
                pagoTable.setCliente("*Administrativo*");
            else
                pagoTable.setCliente(pago.getClienteNombre());
            pagoTable.setDias(pago.getDias());
            pagoTable.setNormales(pago.getHorasNormales());
            pagoTable.setSuplementarias(pago.getHorasSuplementarias());
            pagoTable.setSobreTiempo(pago.getHorasSobreTiempo());
            pagoTable.setSueldo(pago.getSalario());
            pagoTable.setExtra(pago.getMontoHorasSuplementarias() + pago.getMontoHorasSobreTiempo());
            pagoTable.setBonos(pago.getTotalBonos());
            pagoTable.setVacaciones(pago.getVacaciones());
            pagoTable.setSubtotal(pago.getSubtotal());
            pagoTable.setDecimos(pago.getDecimoCuarto() + pago.getDecimoTercero());
            pagoTable.setTercero(pago.getDecimoTercero());
            pagoTable.setCuarto(pago.getDecimoCuarto());
            pagoTable.setTotal(pago.getTotalIngreso());
            pagosTable.add(pagoTable);
            
            diasTextValor += pagoTable.getDias();
            normalesTextValor += pagoTable.getNormales();
            suplementariasTextValor += pagoTable.getSuplementarias();
            sobreTiempoTextValor += pagoTable.getSobreTiempo();
            sueldoTotalTextValor += pagoTable.getSueldo();
            extraTextValor += pagoTable.getExtra();
            bonosTextValor += pagoTable.getBonos();
            vacacionesTextValor += pagoTable.getVacaciones();
            subTotalTextValor += pagoTable.getSubtotal();
            decimosTotalTextValor += pagoTable.getDecimos();
            decimoTerceroTotalTextValor += pagoTable.getTercero();
            decimoCuartoTotalTextValor += pagoTable.getCuarto();
            totalTextValor += pagoTable.getTotal();
            
            montoSumplementariasTextValor += pago.getMontoHorasSuplementarias(); // No Visible en ventana
            montoSobreTiempoTextValor += pago.getMontoHorasSobreTiempo(); // No Visible en ventana
            montoBonoTextValor += pago.getBono(); // No Visible en ventana
            montoTransporteTextValor += pago.getTransporte(); // No Visible en ventana
            montoJubilacionTextValor += pago.getJubilacionPatronal(); // No Visible en ventana
            montoAportePatronalTextValor += pago.getAportePatronal(); // No Visible en ventana
            montoSegurosTextValor += pago.getSeguros(); // No Visible en ventana
            montoUniformasTextValor += pago.getUniformes(); // No Visible en ventana
        }
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion("Sueldo");
            rol.setIngreso(round(sueldoTotalTextValor, 2));
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
                rol.setIngreso(round(decimoTerceroTotalTextValor, 2));
                rol.setClave(Const.IP_DECIMO_TERCERO);
                pagoMesItems.add(rol);
            }
            {
                PagoMesItem rol = new PagoMesItem();
                rol.setDescripcion("Decimo Cuarto");
                rol.setIngreso(round(decimoCuartoTotalTextValor, 2));
                rol.setClave(Const.IP_DECIMO_CUARTO);
                pagoMesItems.add(rol);
            }
        }
        ieesValor = (ingresoValor/100d) * getIess(); 
        {
            PagoMesItem rol = new PagoMesItem();
            rol.setDescripcion(iessText);
            rol.setDeduccion(round(ieesValor, 2));
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
        empleadoTable.setSueldo(aPercibirValor);
        
        RolIndividual pagoRol = new RolIndividual();
        pagoRol.setDetalles(Const.ROL_PAGO_INDIVIDUAL);
        pagoRol.setFecha(new Timestamp(new Date().getTime()));
        pagoRol.setInicio(inicio);
        pagoRol.setFinalizo(fin);
        pagoRol.setDias(diasTextValor);
        pagoRol.setHorasNormales(Double.valueOf(normalesTextValor));
        pagoRol.setHorasSuplementarias(Double.valueOf(suplementariasTextValor));  // RC
        pagoRol.setHorasSobreTiempo(Double.valueOf(sobreTiempoTextValor));         // ST
        pagoRol.setTotalHorasExtras(Double.valueOf(sobreTiempoTextValor + suplementariasTextValor));
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
        
        if (new RolIndividualDAO().findByFechaAndEmpleadoIdAndDetalles(fin, empleado.getId(), Const.ROL_PAGO_INDIVIDUAL) != null) {
            empleadoTable.setPagado("Si");
            empleadoTable.setPagar(false);
        } else {
            empleadoTable.setPagado("No");
            empleadoTable.setPagar(true);
        }
        
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
        
        sueldoColumna.setCellValueFactory(new PropertyValueFactory<>("sueldo"));
        
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
                     empleadoTable.setPagar(checkBoxpagar.isSelected());
                     //guardar();
                });
            }

            
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
        ArrayList<Deuda> deudas = new ArrayList<>();
        deudasAPagar.addAll(new DeudaDAO()
                .findAllByUsuarioIdNoPagadaSinAplazar(empleadoTable.getId()));
        for (Deuda deuda: deudas) {
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
