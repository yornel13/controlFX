/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Const;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
        aplicacionControl.mostrarVentanaPrincipal();
        stagePrincipal.close();
    }
    
    @FXML
    private void verEmpleados(ActionEvent event) {
        aplicacionControl.mostrarEmpleados(empresa);
        stagePrincipal.close();
    }
    
    @FXML
    public void onClickPagos(ActionEvent event) {
        aplicacionControl.mostrarHorasEmpleados(empresa);
        stagePrincipal.close();
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
        empresaLabel.setText(empresa.getNombre());
        numeracionLabel.setText("RUC: " + empresa.getNumeracion().toString());
        
        UsuarioDAO usuariosDAO = new UsuarioDAO();
        empleados = new ArrayList<>();
        empleados.addAll(usuariosDAO.findByEmpresaId(empresa.getId()));
        totalLabel.setText("Total de empleados: " + empleados.size());
    }
    
    @FXML
    public void marcar(ActionEvent event) throws ParseException {
        UsuarioDAO usuariosDAO = new UsuarioDAO();
        Usuario empleado = new Usuario();
        empleado = usuariosDAO.findByAllCedulaAndEmpresaId(cedulaField.getText(), empresa.getId());
        if (empleado != null) {
            ControlEmpleadoDAO controlEmpleadoDAO = new ControlEmpleadoDAO();
            ControlEmpleado controlEmpleado = new ControlEmpleado();
            
            controlEmpleado = controlEmpleadoDAO.findByFecha(getToday(), empleado.getId());
            
            // si no hubo registro hoy
            if (controlEmpleado == null) {
                mostrarHoras(empleado);
            } else {
                labelTest.setText("Ya el usuario fue \nregistrado hoy");
            }
        }  else {
            labelTest.setText("El usuario no existe");
        }      
    }
    
    public void mostrarHoras(Usuario empleado) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaHorasExtras.fxml"));
            AnchorPane ventanaHoras = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle(empleado.getNombre() + " " + empleado.getApellido());
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
        
        homeButton.setStyle(Const.BACKGROUND_COLOR_SEMI_TRANSPARENT);
        String image2 = AplicacionControl.class.getResource("imagenes/home_32dp.png").toExternalForm();
        Image homeImage = new Image(image2);
        homeButton.setGraphic(new ImageView(homeImage));
        
        contentPane.setStyle(Const.BACKGROUND_COLOR_SEMI_TRANSPARENT);
        
        labelTest.setText("Ingrese la cedula del empleado \n para hacer el registro de hoy");
        
        cedulaField.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        
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
}
