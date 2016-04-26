/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.HorasEmpleadosController.getToday;
import aplicacion.control.tableModel.ControlTable;
import aplicacion.control.util.Const;
import aplicacion.control.util.Fechas;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UniformeDAO;
import hibernate.model.Actuariales;
import hibernate.model.Cliente;
import hibernate.model.Constante;
import hibernate.model.ControlEmpleado;
import hibernate.model.Seguro;
import hibernate.model.Uniforme;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class RolDePagoController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    private Usuario empleado;
    
    public ArrayList<ControlEmpleado> controlEmpleado;
    private ArrayList<ControlTable> controlEmpleadoTable;
   
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button agregarButton;
    
    @FXML
    private Pane imagenLabel;
    
    @FXML
    private Button ver;
    
    @FXML
    private TableView empleadosTableView;
    
    @FXML
    private Label totalDias;
           
    @FXML
    private Label totalHorasN;
    
    @FXML
    private Label totalHorasST;
    
    @FXML
    private Label totalHorasRC;
    
    @FXML
    private Label totalHorasExtras;
    
    @FXML
    private Label totalSalario;
    
    @FXML
    private Label totalSobreTiempo;
    
    @FXML
    private Label totalRecargo;
    
    @FXML
    private Label totalBono;
    
    @FXML
    private Label totalTransporte;
    
    @FXML
    private Label totalBonos;
    
    @FXML
    private Label totalVacaciones;
    
    @FXML
    private Label subTotal;
    
    @FXML
    private Label totalDecimo3;
    
    @FXML
    private Label totalDecimo4;
    
    @FXML
    private Label totalReserva;
    
    @FXML
    private Label totalJubilacion;
    
    @FXML
    private Label totalAporte;
    
    @FXML
    private Label totalSeguros;
    
    @FXML
    private Label totalUniformes;
    
    @FXML
    private Label totalIngresos;  
    
    @FXML
    private Label textEmpleado;
    
    @FXML
    private DatePicker pickerDe;
    
    @FXML 
    private DatePicker pickerHasta;
    
    
    
    
    private ObservableList<ControlTable> data;
    
    ArrayList<Usuario> usuarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void goHome(ActionEvent event) {
        aplicacionControl.mostrarVentanaPrincipal();
        stagePrincipal.close();
    }
    
    @FXML
    private void agregarEmpleado(ActionEvent event) {
        //aplicacionControl.mostrarRegistrarEmpleado(empresa);
        LocalDate jose = pickerDe.getValue();
    }
    
    @FXML
    private void mostrarRegistro(ActionEvent event) {
        if (pickerDe.getValue() == null) {
            // error
        } else if (pickerHasta.getValue() == null) {
            // error 
        } else if (pickerDe.getValue().isAfter(pickerHasta.getValue())){
            // error
        } else {       
            Timestamp fin = Timestamp.valueOf(pickerHasta.getValue().atStartOfDay());
            Timestamp inicio = Timestamp.valueOf(pickerDe.getValue().atStartOfDay());
            setControlEmpleadoInfo(empleado, inicio, fin);
        }
    } 
    
    @FXML
    public void mostrarHoras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasExtras.fxml"));
            AnchorPane ventanaHoras = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaHoras);
            ventana.setScene(scene);
            HorasExtrasController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setEmpleado(empleado);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    @FXML
    public void mostrarEditarHoras(ControlEmpleado controlEmpleado) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaEditarHorasExtras.fxml"));
            AnchorPane ventanaHoras = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaHoras);
            ventana.setScene(scene);
            EditarHorasExtrasController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setProgramaPrincipal(this);
            controller.setControlEmpleado(controlEmpleado);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
    }
    
    public void guardarRegistro(Usuario empleado, Integer suplementarias, 
            Integer sobreTiempo, Cliente cliente, Timestamp fecha, Boolean libre) throws ParseException {
        ControlEmpleadoDAO controlEmpleadoDAO = new ControlEmpleadoDAO();
        ControlEmpleado controlEmpleado = new ControlEmpleado();
        controlEmpleado = new ControlEmpleado();
        controlEmpleado.setFecha(fecha);
        controlEmpleado.setLibre(libre);
        controlEmpleado.setUsuario(empleado);
        controlEmpleado.setHorasExtras(sobreTiempo);
        controlEmpleado.setHorasSuplementarias(suplementarias);
        controlEmpleado.setCliente(cliente);
        controlEmpleadoDAO.save(controlEmpleado);
        setEmpleado(empleado);
    }
    
    public void guardarRegistroEditado(ControlEmpleado controlEmpleado, Integer suplementarias, 
            Integer sobreTiempo, Cliente cliente, Timestamp fecha, Boolean libre) throws ParseException {
        
        controlEmpleado.setFecha(fecha);
        controlEmpleado.setLibre(libre);
        if (libre) {
            controlEmpleado.setHorasExtras(0);
            controlEmpleado.setHorasSuplementarias(0);
            controlEmpleado.setCliente(null);
        } else {
            controlEmpleado.setHorasExtras(sobreTiempo);
            controlEmpleado.setHorasSuplementarias(suplementarias);
            controlEmpleado.setCliente(cliente);
        }
        HibernateSessionFactory.getSession().flush();
        setEmpleado(empleado);
    }
    
    public void setEmpleado(Usuario empleado) throws ParseException {
        this.empleado = empleado;
        
        DateTime dateTime = new DateTime();
           
        Timestamp fin = new Timestamp(dateTime.getMillis());
        Timestamp inicio = new Timestamp(dateTime.minusMonths(1).plusDays(1).getMillis());
        
        pickerDe.setValue(Fechas.getDateFromTimestamp(inicio));
        pickerHasta.setValue(Fechas.getDateFromTimestamp(fin));
        
        setControlEmpleadoInfo(empleado, inicio, fin);
    }
    
    public void setControlEmpleadoInfo(Usuario empleado, Timestamp inicio, Timestamp fin) {
        
        empleadosTableView.getColumns().clear(); 
        
        ControlEmpleadoDAO controlDAO = new ControlEmpleadoDAO();
        
        Integer dias = 0;
        Integer normales = 0;
        Integer sobreTiempo = 0;
        Integer suplementarias = 0;
        
        controlEmpleado = new ArrayList<>();
        controlEmpleadoTable = new ArrayList<>();
        controlEmpleado.addAll(controlDAO.findAllByEmpleadoIdInDeterminateTime(empleado.getId(), inicio, fin));
        
        for (ControlEmpleado control: controlEmpleado) {
            dias = dias + 1;
            normales = normales + 8;
            sobreTiempo = sobreTiempo + control.getHorasExtras();
            suplementarias = suplementarias + control.getHorasSuplementarias();
            
            ControlTable controlTable = new ControlTable();
            
            controlTable.setId(control.getId());
            if (control.getCliente() != null) {
                controlTable.setCliente(control.getCliente().getNombre());
            }
            controlTable.setFecha(new DateTime(control.getFecha().getTime()).toString("dd-MM-yyyy"));
            controlTable.setHorasExtras(control.getHorasExtras());
            controlTable.setHorasSuplementarias(control.getHorasSuplementarias());
            controlTable.setUsuarios(empleado);
            
            controlEmpleadoTable.add(controlTable);
        }
       
        if (!controlEmpleadoTable.isEmpty()) {
           data = FXCollections.observableArrayList(); 
           data.addAll(controlEmpleadoTable);
           empleadosTableView.setItems(data);
        }
        
        TableColumn fecha = new TableColumn("Fecha");
        fecha.setMinWidth(100);
        fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
       
        TableColumn cliente = new TableColumn("Cliente");
        cliente.setMinWidth(100);
        cliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        TableColumn horasExtras = new TableColumn("HORAS (ST)");
        horasExtras.setMinWidth(100);
        horasExtras.setCellValueFactory(new PropertyValueFactory<>("horasExtras"));
        
        TableColumn horasSuplementarias = new TableColumn("HORAS (RC)");
        horasSuplementarias.setMinWidth(100);
        horasSuplementarias.setCellValueFactory(new PropertyValueFactory<>("horasSuplementarias"));
        
        TableColumn<ControlTable, ControlTable> delete = new TableColumn<>("Delete");
        delete.setMinWidth(40);
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<ControlTable, ControlTable>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(ControlTable controlTable, boolean empty) {
                super.updateItem(controlTable, empty);

                if (controlTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    controlDAO.delete(controlDAO.findById(controlTable.getId()));
                    HibernateSessionFactory.getSession().flush();
                    data.remove(controlTable);
                });
            }
        });
        
        empleadosTableView.getColumns().addAll(fecha, cliente, horasExtras, 
                horasSuplementarias, delete);
        
        empleadosTableView.setRowFactory( (Object tv) -> {
            TableRow<ControlTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ControlTable rowData = row.getItem();
                    mostrarEditarHoras(controlDAO.findById(rowData.getId()));
                }
            });
            return row ;
        });
        
        Double sueldoDia = empleado.getDetallesEmpleado().getSueldo() / 30d;
        Double sueldoHoras = empleado.getDetallesEmpleado().getSueldo() / 240d;
        
        totalDias.setText(dias.toString());
        totalHorasN.setText(normales.toString());
        totalHorasRC.setText(suplementarias.toString());
        totalHorasST.setText(sobreTiempo.toString());
        totalHorasExtras.setText(String.valueOf(sobreTiempo + suplementarias));
        
        // Salario
        Double totalSalarioDouble = sueldoDia * Double.valueOf(dias);
        totalSalario.setText(String.format( "%.2f", totalSalarioDouble));
        Double totalSobreTiempoDouble = sueldoHoras * Double.valueOf(sobreTiempo);
        totalSobreTiempo.setText(String.format( "%.2f", totalSobreTiempoDouble));
        Double totalRecargoDouble = sueldoHoras * Double.valueOf(suplementarias);
        totalRecargo.setText(String.format( "%.2f", totalRecargoDouble));
        Double subTotalDouble = totalSalarioDouble + totalSobreTiempoDouble + totalRecargoDouble;
        subTotal.setText(String.format( "%.2f", subTotalDouble));
        Double decimoTercero = subTotalDouble / 12d;
        totalDecimo3.setText(String.format( "%.2f", decimoTercero));
        Double decimoCuarto = (getDecimoCuarto()/30d) * Double.valueOf(dias);
        totalDecimo4.setText(String.format( "%.2f", decimoCuarto));
        totalReserva.setText(String.format( "%.2f", decimoTercero));
        Double jubilacionPatronal = getActuariales(empleado.getId())/ 360d * Double.valueOf(dias);
        totalJubilacion.setText(String.format( "%.2f", jubilacionPatronal));
        Double aportePatronal = subTotalDouble * 12.5d / 100d;
        totalAporte.setText(String.format( "%.2f", aportePatronal));
        Double segurosDecimal = getSeguro(empleado.getDetallesEmpleado().getEmpresa().getId()) * Double.valueOf(dias);
        totalSeguros.setText(String.format( "%.2f", segurosDecimal));
        Double uniformeDecimal = getUniforme(empleado.getDetallesEmpleado().getEmpresa().getId()) * Double.valueOf(dias);
        totalUniformes.setText(String.format( "%.2f", uniformeDecimal));
        
        Double ingresoTotal = subTotalDouble + decimoTercero + decimoCuarto + decimoTercero 
                + jubilacionPatronal + aportePatronal + segurosDecimal + uniformeDecimal;
        totalIngresos.setText(String.format( "%.2f", ingresoTotal));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        empleadosTableView.setEditable(Boolean.TRUE);
        empleadosTableView.getColumns().clear(); 
    }  
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public double getDecimoCuarto() {
        ConstanteDAO constanteDao = new ConstanteDAO();
        Constante constante;
        constante = (Constante) constanteDao.findUniqueResultByNombre(Const.DECIMO_TERCERO);
        if (constante == null) {
            return 30.5;
        } else {
            return Double.valueOf(constante.getValor());
        }
    }
    
    public double getActuariales(Integer empleadoId) {
        ActuarialesDAO actuarialesDAO = new ActuarialesDAO();
        Actuariales actuariales;
        actuariales = actuarialesDAO.findByEmpleadoId(empleadoId);
        if (actuariales == null) {
            return 0;
        } else {
            return actuariales.getPrimario() + actuariales.getSecundario();
        }
    }
    
    public double getSeguro(Integer empresaId) {
        SeguroDAO seguroDAO = new SeguroDAO();
        Seguro seguro;
        seguro = seguroDAO.findByEmpresaId(empresaId);
        if (seguro == null) {
          return 0;  
        } else {
            return seguro.getValor();
        }  
    }
    
    public double getUniforme(Integer empresaId) {
        UniformeDAO uniformeDAO = new UniformeDAO();
        Uniforme uniforme;
        uniforme = uniformeDAO.findByEmpresaId(empresaId);
        if (uniforme == null) {
          return 0;  
        } else {
            return uniforme.getValor();
        }  
    }
    
}
