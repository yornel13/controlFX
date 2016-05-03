/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ClienteDAO;
import hibernate.dao.DepartamentoDAO;
import hibernate.dao.DetallesEmpleadoDAO;
import hibernate.dao.PagoDAO;
import hibernate.model.Cliente;
import hibernate.model.Departamento;
import hibernate.model.Empresa;
import hibernate.model.Pago;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class PagosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
     private Empresa empresa;
    
    private ArrayList<Cliente> clientes;
    
    @FXML
    private TableView pagosTableView;
    
    @FXML
    private ChoiceBox clienteSelector;
    
    @FXML
    private CheckBox checkBoxCliente;
    
    private ObservableList<Pago> data;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    @FXML
    private void onCheckCliente(ActionEvent event) {
        if (checkBoxCliente.isSelected()) {
            clienteSelector.setDisable(false);
        } else {
            clienteSelector.setDisable(true);
        }
    }
    
    @FXML
    private void onVerCliente(ActionEvent event) {
        if (!clienteSelector.getSelectionModel().isEmpty() && checkBoxCliente.isSelected()) {
            setPagoClienteTabla();
        } else {
            setPagoTable();
        }
    }
    
    @FXML
    private void returnEmpresa(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarInEmpresa(empresa);
    } 
    
    public void deleteDepartamento(Departamento departamento) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
               
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Confirmación de borrado");
                String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));;
                Button buttonConfirmar = new Button("Si Borrar");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Borrar este Departamento"), buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                dialogStage.show();
                buttonConfirmar.setOnAction((ActionEvent e) -> {

                    if (new DetallesEmpleadoDAO().findByDepartamentoId(departamento.getId()).isEmpty()) {
                        new DepartamentoDAO().delete(departamento);
                        HibernateSessionFactory.getSession().flush();
                        dialogStage.close();
                        //dataDepartamentos.remove(departamento);
                    } else {
                        dialogStage.close();
                        dialogDepartamentoError();
                    }
                });
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void dialogCargoError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Dialogo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));;
        Button buttonConfirmar = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("El cargo esta en uso"), buttonConfirmar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        dialogStage.show();
        buttonConfirmar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void dialogDepartamentoError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Dialogo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));;
        Button buttonConfirmar = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("El departamento esta en uso"), buttonConfirmar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        dialogStage.show();
        buttonConfirmar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        pagosTableView.setEditable(Boolean.FALSE);
        pagosTableView.getColumns().clear();
        
        TableColumn cedula = new TableColumn("Cedula");
        cedula.setMinWidth(100);
        cedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        
        TableColumn empleado = new TableColumn("Empleado");
        empleado.setMinWidth(100);
        empleado.setCellValueFactory(new PropertyValueFactory<>("empleado"));
        
        TableColumn inicio = new TableColumn("Inicio");
        inicio.setMinWidth(100);
        inicio.setCellValueFactory(new PropertyValueFactory<>("inicio"));
        
        TableColumn finalizo = new TableColumn("Finalizo");
        finalizo.setMinWidth(100);
        finalizo.setCellValueFactory(new PropertyValueFactory<>("finalizo"));
        
        TableColumn dias = new TableColumn("Dias");
        dias.setMinWidth(100);
        dias.setCellValueFactory(new PropertyValueFactory<>("dias"));
        
        TableColumn horasNormales = new TableColumn("Horas");
        horasNormales.setMinWidth(100);
        horasNormales.setCellValueFactory(new PropertyValueFactory<>("horasNormales"));
        
        TableColumn horasSuplementarias = new TableColumn("Suplementarias");
        horasSuplementarias.setMinWidth(100);
        horasSuplementarias.setCellValueFactory(new PropertyValueFactory<>("horasSuplementarias"));
        
        
        TableColumn horasSobreTiempo = new TableColumn("Sobre Tiempo");
        horasSobreTiempo.setMinWidth(100);
        horasSobreTiempo.setCellValueFactory(new PropertyValueFactory<>("horasSobreTiempo"));
        
        TableColumn totalHorasExtras = new TableColumn("Horas Extras");
        totalHorasExtras.setMinWidth(100);
        totalHorasExtras.setCellValueFactory(new PropertyValueFactory<>("totalHorasExtras"));
        
        TableColumn salario = new TableColumn("Salario");
        salario.setMinWidth(100);
        salario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        
        TableColumn montoHorasSuplementarias = new TableColumn("Monto Suplementarias");
        montoHorasSuplementarias.setMinWidth(100);
        montoHorasSuplementarias.setCellValueFactory(new PropertyValueFactory<>("montoHorasSuplementarias"));
        
        TableColumn montoHorasSobreTiempo = new TableColumn("Monto SobreTiempo");
        montoHorasSobreTiempo.setMinWidth(100);
        montoHorasSobreTiempo.setCellValueFactory(new PropertyValueFactory<>("montoHorasSobreTiempo"));
        
        TableColumn bono = new TableColumn("Bono");
        bono.setMinWidth(100);
        bono.setCellValueFactory(new PropertyValueFactory<>("bono"));
        
        TableColumn transporte = new TableColumn("Transporte");
        transporte.setMinWidth(100);
        transporte.setCellValueFactory(new PropertyValueFactory<>("transporte"));
        
        TableColumn totalBonos = new TableColumn("Total Bonos");
        totalBonos.setMinWidth(100);
        totalBonos.setCellValueFactory(new PropertyValueFactory<>("totalBonos"));
        
        TableColumn vacaciones = new TableColumn("Vacaciones");
        vacaciones.setMinWidth(100);
        vacaciones.setCellValueFactory(new PropertyValueFactory<>("vacaciones"));
        
        TableColumn subtotal = new TableColumn("Subtotal");
        subtotal.setMinWidth(100);
        subtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        TableColumn decimoTercero = new TableColumn("Decimo Tercero");
        decimoTercero.setMinWidth(100);
        decimoTercero.setCellValueFactory(new PropertyValueFactory<>("decimoTercero"));
        
        TableColumn decimoCuarto = new TableColumn("Decimo Cuarto");
        decimoCuarto.setMinWidth(100);
        decimoCuarto.setCellValueFactory(new PropertyValueFactory<>("decimoCuarto"));
        
        TableColumn jubilacionPatronal = new TableColumn("Jubilacion Patronal");
        jubilacionPatronal.setMinWidth(100);
        jubilacionPatronal.setCellValueFactory(new PropertyValueFactory<>("jubilacionPatronal"));
        
        TableColumn aportePatronal = new TableColumn("Aporte Patronal");
        aportePatronal.setMinWidth(100);
        aportePatronal.setCellValueFactory(new PropertyValueFactory<>("aportePatronal"));
        
        TableColumn seguros = new TableColumn("Seguros");
        seguros.setMinWidth(100);
        seguros.setCellValueFactory(new PropertyValueFactory<>("seguros"));
        
        TableColumn uniformes = new TableColumn("Uniformes");
        uniformes.setMinWidth(100);
        uniformes.setCellValueFactory(new PropertyValueFactory<>("uniformes"));
        
        TableColumn totalIngreso = new TableColumn("Ingreso");
        totalIngreso.setMinWidth(100);
        totalIngreso.setCellValueFactory(new PropertyValueFactory<>("totalIngreso"));;
        
        pagosTableView.getColumns().addAll(cedula, empleado, inicio, finalizo, 
                dias, horasNormales, horasSuplementarias, horasSobreTiempo, 
                totalHorasExtras, salario, montoHorasSuplementarias, 
                montoHorasSobreTiempo, bono, transporte, totalBonos,
                vacaciones, subtotal, decimoTercero, decimoCuarto,
                jubilacionPatronal, aportePatronal, seguros, uniformes,
                totalIngreso); 
        setPagoTable();
        
        ClienteDAO clienteDAO = new ClienteDAO();
        clientes = new ArrayList<>();
        clientes.addAll(clienteDAO.findAll());
        
        String[] items = new String[clientes.size()];
        for (Cliente cli: clientes) {
            items[clientes.indexOf(cli)] = cli.getNombre();
        }
        
        clienteSelector.setItems(FXCollections.observableArrayList(items));
        clienteSelector.setDisable(true);
    }
    
    public void setPagoTable() {
        
        PagoDAO pagoDAO = new PagoDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(pagoDAO.findAll());
        pagosTableView.setItems(data);
    }
    
    public void setPagoClienteTabla() {
        Integer clienteId = clientes.get(clienteSelector.getSelectionModel().getSelectedIndex()).getId();
        
        PagoDAO pagoDAO = new PagoDAO();
        data = FXCollections.observableArrayList(); 
        data.addAll(pagoDAO.findByClienteId(clienteId));
        pagosTableView.setItems(data);
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
