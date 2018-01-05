/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.EmailValidator;
import aplicacion.control.util.Fechas;
import hibernate.HibernateSessionFactory;
import hibernate.dao.CargoDAO;
import hibernate.dao.DepartamentoDAO;
import hibernate.dao.EstadoCivilDAO;
import hibernate.dao.UsuarioDAO;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    private CheckBox checkBoxDecimos;
    
    @FXML
    private Label errorText1;
    
    @FXML
    private Label errorText2;
    
    @FXML
    private RadioButton sexoM;
    
    @FXML
    private RadioButton sexoF;
    
    @FXML 
    private DatePicker datePicker;
    
    @FXML 
    private DatePicker datePickerInicio;
    
    @FXML 
    private DatePicker datePickerContratacion;
    
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
    public void sexoMasculinoClick(ActionEvent event) {
        sexoM.setSelected(true);
        sexoF.setSelected(false);
    }
    
    @FXML
    public void sexoFemeninoClick(ActionEvent event) {
        sexoM.setSelected(false);
        sexoF.setSelected(true);
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
        } else if (!sexoF.isSelected() && !sexoM.isSelected()) {
            errorText1.setText("Debe seleccionar el sexo");
            errorText2.setText("Debe seleccionar el sexo");
        } else if (telefonoField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar un numero telefonico");
            errorText2.setText("Debe ingresar un numero telefonico");
        } else if (direccionField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar la direccion");
            errorText2.setText("Debe ingresar la direccion");
        } else if (!new EmailValidator().validate(emailField.getText())) {
            errorText1.setText("Debes introducir un correo valido");
            errorText2.setText("Debes introducir un correo valido");
        } else if (estadoCivilChoiceBox.getSelectionModel().isEmpty()) {
            errorText1.setText("Debe seleccionar el estado civil");
            errorText2.setText("Debe seleccionar el estado civil");
        } else if (datePicker.getValue() == null) {
            errorText1.setText("Debe seleccionar la fecha de nacimiento");
            errorText2.setText("Debe seleccionar la fecha de nacimiento");
        } else if (departamentoChoiceBox.getSelectionModel().isEmpty()) {
            errorText1.setText("Seleccione el departamento al cual pertenece el empleado");
            errorText2.setText("Seleccione el departamento al cual pertenece el empleado");
        } else if (cargoChoiceBox.getSelectionModel().isEmpty()) {
            errorText1.setText("Seleccione el cargo del empleado");
            errorText2.setText("Seleccione el cargo del empleado");
        } else if (datePickerInicio.getValue() == null) {
            errorText1.setText("Debe seleccionar la fecha de inicio del empleado");
            errorText2.setText("Debe seleccionar la fecha de inicio del empleado");
        } else if (datePickerContratacion.getValue() == null) {
            errorText1.setText("Debe seleccionar la fecha de contratacion del empleado");
            errorText2.setText("Debe seleccionar la fecha de contratacion del empleado");
        } else if (sueldoField.getText().isEmpty()) {
            errorText1.setText("Debe ingresar el sueldo del empleado");
            errorText2.setText("Debe ingresar el sueldo del empleado");
        } else {
            empleado = new UsuarioDAO().findById(empleado.getId());
            
            empleado.getDetallesEmpleado().setDepartamento(departamentos.get(departamentoChoiceBox.getSelectionModel().getSelectedIndex()));
            empleado.getDetallesEmpleado().setCargo(cargos.get(cargoChoiceBox.getSelectionModel().getSelectedIndex()));
            empleado.getDetallesEmpleado().setFechaInicio(Timestamp.valueOf(datePickerInicio.getValue().atStartOfDay()));
            empleado.getDetallesEmpleado().setFechaContrato(Timestamp.valueOf(datePickerContratacion.getValue().atStartOfDay()));
            //empleado.getDetallesEmpleado().setExtra(extraField.getText()); TODO, extra elimando
            empleado.getDetallesEmpleado().setNroCuenta(cuentaField.getText());
            empleado.getDetallesEmpleado().setSueldo(Double.parseDouble(sueldoField.getText()));
            empleado.getDetallesEmpleado().setAcumulaDecimos(checkBoxDecimos.isSelected());
            
            empleado.setNombre(nombreField.getText());
            empleado.setApellido(apellidoField.getText());
            empleado.setCedula(cedulaField.getText());
            if (sexoM.isSelected()) {
                empleado.setSexo("M");
            } else {
                empleado.setSexo("F");
            }
            empleado.setEmail(emailField.getText());
            System.out.println(datePicker.getValue().atStartOfDay());
            empleado.setNacimiento(Timestamp.valueOf(datePicker.getValue().atStartOfDay()));
            empleado.setDireccion(direccionField.getText());
            empleado.setTelefono(telefonoField.getText());
            empleado.setEstadoCivil(estadosCivil.get(estadoCivilChoiceBox.getSelectionModel().getSelectedIndex()));
            empleado.setUltimaModificacion(new Timestamp(new Date().getTime()));
            
            confirmarGuardado();
        }
    }
    
    public void confirmacionPositiva() {
        stagePrincipal.close();

        // Registro para auditar
        String detalles = "edito el empleado " 
                + empleado.getNombre() + " " + empleado.getApellido();
        aplicacionControl.au.saveEdito(detalles, aplicacionControl.permisos.getUsuario());

        completado();

        EmpleadosController ec = aplicacionControl.changeEmpleadosController;
        EmpleadosTodosController etc = aplicacionControl.changeEmpleadosTodosController;
        if (ec != null) {
            ec.empleadoEditado(empleado);
        }
        if (etc != null) {
            etc.empleadoEditado(empleado);
        }
    }
    
    public void completado() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Completado");
        String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Empleado editado satisfactoriamente."), buttonOk).
        alignment(Pos.CENTER).padding(new Insets(15)).build()));
        buttonOk.setMaxWidth(40);
        dialogStage.show();
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setOnKeyPressed((KeyEvent event1) -> {
            dialogStage.close();
        });
    }
    
    public void confirmarGuardado() {
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Confirmar modificación");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_editar.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("Si, modificar");
        Button buttonCancelar = new Button("No, no estoy seguro");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("¿Seguro que desea modificar este empleado?"), buttonOk, buttonCancelar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        buttonOk.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            HibernateSessionFactory.getSession().flush();
            confirmacionPositiva();
        });
        buttonOk.setOnKeyPressed((KeyEvent event) -> {
            dialogStage.close();
            HibernateSessionFactory.getSession().flush();
            confirmacionPositiva();
        });
        buttonCancelar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
            HibernateSessionFactory.getSession().clear();
        });
        dialogStage.showAndWait();
        
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
        checkBoxDecimos.setSelected(empleado.getDetallesEmpleado().getAcumulaDecimos());
        //extraField.setText(empleado.getDetallesEmpleado().getExtra());  TODO, extra elimando
        estadoCivilChoiceBox.getSelectionModel().select(empleado.getEstadoCivil().getNombre());
        departamentoChoiceBox.getSelectionModel().select(empleado.getDetallesEmpleado().getDepartamento().getNombre());
        cargoChoiceBox.getSelectionModel().select(empleado.getDetallesEmpleado().getCargo().getNombre()); 
        if (empleado.getSexo() == null) {
            sexoMasculinoClick(null);
        } else if (empleado.getSexo().equalsIgnoreCase("F")) {
            sexoFemeninoClick(null);
        } else {
            sexoMasculinoClick(null);
        }
        datePicker.setValue(Fechas.getLocalFromTimestamp(empleado.getNacimiento()));
        datePickerInicio.setValue(Fechas.getLocalFromTimestamp(empleado.getDetallesEmpleado().getFechaInicio()));
        datePickerContratacion.setValue(Fechas.getLocalFromTimestamp(empleado.getDetallesEmpleado().getFechaContrato()));
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
        
        estadosCivil.stream().forEach((obj) -> {
            itemsEstadosCivil[estadosCivil.indexOf(obj)] = obj.getNombre();
        });
        departamentos.stream().forEach((obj) -> {
            itemsDepartamentos[departamentos.indexOf(obj)] = obj.getNombre();
        });
        cargos.stream().forEach((obj) -> {
            itemsCargos[cargos.indexOf(obj)] = obj.getNombre();
        });
        
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
