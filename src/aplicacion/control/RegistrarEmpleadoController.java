/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.HibernateSessionFactory;
import hibernate.dao.CargoDAO;
import hibernate.dao.DepartamentoDAO;
import hibernate.dao.DetallesEmpleadoDAO;
import hibernate.dao.EstadoCivilDAO;
import hibernate.dao.RolesDAO;
import hibernate.dao.UsuariosDAO;
import hibernate.model.Cargo;
import hibernate.model.Departamento;
import hibernate.model.DetallesEmpleado;
import hibernate.model.Empresa;
import hibernate.model.EstadoCivil;
import hibernate.model.Roles;
import hibernate.model.Usuarios;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class RegistrarEmpleadoController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Stage stagePrincipal;
    
    private Empresa empresa;
   
    ArrayList<EstadoCivil> estadosCivil;
    ArrayList<Departamento> departamentos;
    ArrayList<Cargo> cargos;
    ArrayList<Roles> roles;
    
    @FXML
    private ChoiceBox estadoCivilChoiceBox;
    
    @FXML
    private ChoiceBox departamentoChoiceBox;
    
    @FXML
    private ChoiceBox cargoChoiceBox;
    
    @FXML
    private TextField nombreField;
    
    @FXML
    private TextField apellidoField;
    
    @FXML
    private TextField cedulaField;
    
    @FXML
    private TextField telefonoField;
    
    @FXML
    private TextField direccionField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField cuentaField;
    
    @FXML
    private TextField extraField;
    
    @FXML
    private TextField sueldoField;
    
    @FXML
    private Label errorText1;
    
    @FXML
    private Label errorText2;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
   
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me menu!");
        //label.setText(menuButton.getCursor().toString());
    }
    
    @FXML
    public void changeDetected(ActionEvent event) {
        System.out.println("aplicacion.control.RegistrarEmpleadoController.changeDetected()");
    }
    
    @FXML
    private void onCLickGuardar(ActionEvent event) throws IOException, Exception {
        System.out.println("aplicacion.control.RegistrarEmpleadoController.onCLickGuardar()");
        if (nombreField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar un nombre");
            errorText2.setText("Debe ingresar un nombre");
        } else if (apellidoField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar un apellido");
            errorText2.setText("Debe ingresar un apellido");
        } else if (cedulaField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar la cedula");
            errorText2.setText("Debe ingresar la cedula");
        } else if (telefonoField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar un numero telefonico");
            errorText2.setText("Debe ingresar un numero telefonico");
        } else if (direccionField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar la direccion");
            errorText2.setText("Debe ingresar la direccion");
        } else if (emailField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar una direccion");
            errorText2.setText("Debe ingresar una direccion");
        } else if (estadoCivilChoiceBox.getSelectionModel().isEmpty()) {
            errorText1.setText("Debe seleccionar el estado civil");
            errorText2.setText("Debe seleccionar el estado civil");
        } else if (departamentoChoiceBox.getSelectionModel().isEmpty()) {
            errorText1.setText("Seleccione el departamento al cual pertenece el empleado");
            errorText2.setText("Seleccione el departamento al cual pertenece el empleado");
        } else if (cargoChoiceBox.getSelectionModel().isEmpty()) {
            errorText1.setText("Seleccione el cargo del empleado");
            errorText2.setText("Seleccione el cargo del empleado");
        } else if (sueldoField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar el sueldo del empleado");
            errorText2.setText("Debe ingresar el sueldo del empleado");
        } else {
            UsuariosDAO usuariosDAO = new UsuariosDAO();
            DetallesEmpleadoDAO detallesEmpleadoDAO = new DetallesEmpleadoDAO();
            
            if (usuariosDAO.findByCedula(cedulaField.getText()) == null) {
                
                DetallesEmpleado detallesEmpleado = new DetallesEmpleado();
                detallesEmpleado.setEmpresa(empresa);
                detallesEmpleado.setDepartamento(departamentos.get(departamentoChoiceBox.getSelectionModel().getSelectedIndex()));
                detallesEmpleado.setCargo(cargos.get(cargoChoiceBox.getSelectionModel().getSelectedIndex()));
                detallesEmpleado.setExtra(extraField.getText());
                detallesEmpleado.setNroCuenta(cuentaField.getText());
                detallesEmpleado.setSueldo(Double.parseDouble(sueldoField.getText()));

                detallesEmpleadoDAO.save(detallesEmpleado);

                Usuarios usuario = new Usuarios();
                usuario.setNombre(nombreField.getText());
                usuario.setApellido(apellidoField.getText());
                usuario.setCedula(cedulaField.getText());
                usuario.setEmail(emailField.getText());
                usuario.setDireccion(direccionField.getText());
                usuario.setTelefono(telefonoField.getText());
                usuario.setDetallesEmpleado(detallesEmpleado);
                usuario.setEstadoCivil(estadosCivil.get(estadoCivilChoiceBox.getSelectionModel().getSelectedIndex()));
                usuario.setActivo(Boolean.TRUE);
                usuario.setCreacion(new Timestamp(new Date().getTime()));
                usuario.setUltimaModificacion(new Timestamp(new Date().getTime()));
                usuario.setRoles(roles.get(1));

                usuariosDAO.save(usuario);

                stagePrincipal.close();

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Dialogo");
                Button buttonOk = new Button("ok");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("Empleado creado satisfactoriamente"), buttonOk).
                alignment(Pos.CENTER).padding(new Insets(10)).build()));
                dialogStage.show();
                buttonOk.setOnAction((ActionEvent e) -> {
                    dialogStage.close();
                });
            } else {
                errorText2.setText("Ya hay un empleado con esta cedula");
            }
            
        }
    }
    
    @FXML
    public void onClickCancelar(ActionEvent event) {
        stagePrincipal.close();
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EstadoCivilDAO estadoCivilDAO = new EstadoCivilDAO();
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        CargoDAO cargoDAO = new CargoDAO();
        
        estadosCivil = (ArrayList<EstadoCivil>) estadoCivilDAO.findAll();
        departamentos = (ArrayList<Departamento>) departamentoDAO.findAll();
        cargos = (ArrayList<Cargo>) cargoDAO.findAll();
        
        String[] itemsEstadosCivil = new String[estadosCivil.size()];
        String[] itemsDepartamentos = new String[departamentos.size()];
        String[] itemsCargos = new String[cargos.size()];
        
        for (EstadoCivil obj: estadosCivil) {
            itemsEstadosCivil[estadosCivil.indexOf(obj)] = obj.getNombre();
        }
        for (Departamento obj: departamentos) {
            itemsDepartamentos[departamentos.indexOf(obj)] = obj.getNombre();
        }
        for (Cargo obj: cargos) {
            itemsCargos[cargos.indexOf(obj)] = obj.getNombre();
        }
        
        estadoCivilChoiceBox.setItems(FXCollections.observableArrayList(itemsEstadosCivil)); 
        departamentoChoiceBox.setItems(FXCollections.observableArrayList(itemsDepartamentos)); 
        cargoChoiceBox.setItems(FXCollections.observableArrayList(itemsCargos)); 
        
        RolesDAO rolesDAO = new RolesDAO();
        roles = (ArrayList<Roles>) rolesDAO.findAll();
        
        sueldoField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
        
        cedulaField.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
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
