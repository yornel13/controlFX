/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.ControlEmpleadoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlEmpleado;
import hibernate.model.Empresa;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class InEmpresaController implements Initializable {

    Empresa empresa;

    private ArrayList<Usuario> empleados;

    private Stage stagePrincipal;

    private AplicacionControl aplicacionControl;

    @FXML
    private Button administradoresButton;

    @FXML
    private Button empresaButton;

    @FXML
    private Pane contentPane;

    @FXML
    private Button homeButton;

    @FXML
    private Label labelTest;

    @FXML
    private Label numeracionLabel;

    @FXML
    private Label empresaLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField cedulaField;

    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }

    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }

    @FXML
    private void goHome(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarVentanaPrincipal();
    }
    
    @FXML
    private void controlPagos(ActionEvent event) {
        aplicacionControl.mostrarTotalPagosEmpleados(empresa, stagePrincipal);
    }

    @FXML
    private void quincenaPagos(ActionEvent event) {
        aplicacionControl.mostrarPagoQuincenal(empresa, stagePrincipal);
    }

    @FXML
    private void verEmpleados(ActionEvent event) {
        aplicacionControl.mostrarEmpleados(empresa, stagePrincipal);
    }

    @FXML
    private void verConfiguracion(ActionEvent event) {
        aplicacionControl.mostrarConfiguracionEmpresa(empresa, stagePrincipal);
    }
    
    @FXML
    private void verActuarialesEmpleados(ActionEvent event) {
        aplicacionControl.mostrarActuarialesEmpleados(empresa, stagePrincipal);
    }
    
    @FXML
    private void verDecimosEmpleados(ActionEvent event) {
        aplicacionControl.mostrarDecimosEmpleados(empresa, stagePrincipal);
    }
    
    @FXML
    private void verQuincenalEmpleados(ActionEvent event) {
        aplicacionControl.mostrarQuincenalEmpleados(empresa, stagePrincipal);
    }
    
    @FXML
    private void verDeudasEmpleados(ActionEvent event) {
        aplicacionControl.mostrarDeudasEmpleados(empresa, stagePrincipal);
    }
    
    @FXML
    private void verIessEmpleados(ActionEvent event) {
        aplicacionControl.mostrarIessEmpleados(empresa, stagePrincipal);
        
    }
    
    @FXML
    private void verSueldoEmpleados(ActionEvent event) {
        aplicacionControl.mostrarSueldoEmpleados(empresa, stagePrincipal);
    }

    @FXML
    private void rolCliente(ActionEvent event) {
        aplicacionControl.mostrarClientesEmpresa(empresa, stagePrincipal);
    }
    
    @FXML
    private void mostrarRolCliente(ActionEvent event) {
        aplicacionControl.mostrarClientesParaRol(empresa, stagePrincipal);
    }
    
    @FXML
    private void mostrarRolIndividual(ActionEvent event) {
        aplicacionControl.mostrarEmpleadosParaRol(empresa, stagePrincipal);
    }

    @FXML
    public void rolEmpleado(ActionEvent event) {
        aplicacionControl.mostrarHorasEmpleados(empresa, stagePrincipal);
    }

    @FXML
    private void verPagos(ActionEvent event) {
        aplicacionControl.mostrarPagos(empresa, stagePrincipal);
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        empresaLabel.setText(empresa.getNombre());
        numeracionLabel.setText("RUC: " + empresa.getNumeracion().toString());

        UsuarioDAO usuariosDAO = new UsuarioDAO();
        empleados = new ArrayList<>();
        empleados.addAll(usuariosDAO.findByEmpresaIdActivo(empresa.getId()));
        totalLabel.setText("Total de empleados: " + empleados.size());
    }

    public void guardarRegistro(Usuario empleado, Integer suplementarias, Integer sobreTiempo, Cliente cliente) throws ParseException {
        ControlEmpleadoDAO controlEmpleadoDAO = new ControlEmpleadoDAO();
        ControlEmpleado controlEmpleado = new ControlEmpleado();
        controlEmpleado = new ControlEmpleado();
        controlEmpleado.setFecha(getToday());
        controlEmpleado.setUsuario(empleado);
        controlEmpleado.setHorasExtras(sobreTiempo);
        controlEmpleado.setHorasSuplementarias(suplementarias);
        controlEmpleado.setCliente(cliente);
        controlEmpleadoDAO.save(controlEmpleado);
        labelTest.setText("Registro Completado!!!");
        cedulaField.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nada aqui por ahora
    }

    // obtener dia sin horas
    public Timestamp getToday() throws ParseException {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));

        return new Timestamp(todayWithZeroTime.getTime());
    }

    public Timestamp getTodayWithHora() {
        return new Timestamp(new Date().getTime());
    }

    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();

            }
        };
        return aux;
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
