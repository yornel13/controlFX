/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.HibernateSessionFactory;
import hibernate.dao.CargoDAO;
import hibernate.dao.DepartamentoDAO;
import hibernate.dao.EstadoCivilDAO;
import hibernate.model.Cargo;
import hibernate.model.Departamento;
import hibernate.model.EstadoCivil;
import hibernate.model.Usuario;
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
public class EditarEmpleadoController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Stage stagePrincipal;
    
    private Usuario empleado;
   
    ArrayList<EstadoCivil> estadosCivil;
    ArrayList<Departamento> departamentos;
    ArrayList<Cargo> cargos;
    
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
            
            empleado.getDetallesEmpleado().setDepartamento(departamentos.get(departamentoChoiceBox.getSelectionModel().getSelectedIndex()));
            empleado.getDetallesEmpleado().setCargo(cargos.get(cargoChoiceBox.getSelectionModel().getSelectedIndex()));
            empleado.getDetallesEmpleado().setExtra(extraField.getText());
            empleado.getDetallesEmpleado().setNroCuenta(cuentaField.getText());
            empleado.getDetallesEmpleado().setSueldo(Double.parseDouble(sueldoField.getText()));
            
            empleado.setNombre(nombreField.getText());
            empleado.setApellido(apellidoField.getText());
            empleado.setCedula(cedulaField.getText());
            empleado.setEmail(emailField.getText());
            empleado.setDireccion(direccionField.getText());
            empleado.setTelefono(telefonoField.getText());
            empleado.setEstadoCivil(estadosCivil.get(estadoCivilChoiceBox.getSelectionModel().getSelectedIndex()));
            empleado.setUltimaModificacion(new Timestamp(new Date().getTime()));
            HibernateSessionFactory.getSession().flush();
            
            stagePrincipal.close();
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Dialogo");
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Empleado editado satisfactoriamente"), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            dialogStage.show();
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
        }
    }
    
    @FXML
    public void onClickCancelar(ActionEvent event) {
        stagePrincipal.close();
    }
    
    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
        
        nombreField.setText(empleado.getNombre());
        apellidoField.setText(empleado.getApellido());
        cedulaField.setText(empleado.getCedula());
        telefonoField.setText(empleado.getTelefono());
        direccionField.setText(empleado.getDireccion());
        emailField.setText(empleado.getEmail());
        cuentaField.setText(empleado.getDetallesEmpleado().getNroCuenta());
        sueldoField.setText(empleado.getDetallesEmpleado().getSueldo().toString());
        extraField.setText(empleado.getDetallesEmpleado().getExtra());
        estadoCivilChoiceBox.getSelectionModel().select(empleado.getEstadoCivil().getNombre());
        departamentoChoiceBox.getSelectionModel().select(empleado.getDetallesEmpleado().getDepartamento().getNombre());
        cargoChoiceBox.getSelectionModel().select(empleado.getDetallesEmpleado().getCargo().getNombre());  
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
       
        sueldoField.addEventFilter(KeyEvent.KEY_TYPED, numDecimalFilter());
    }    
    
    public static EventHandler<KeyEvent> numDecimalFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        };
        return aux;
    }
}
