/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Password;
import hibernate.dao.EstadoCivilDAO;
import hibernate.dao.IdentidadDAO;
import hibernate.dao.RolesDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Empresa;
import hibernate.model.EstadoCivil;
import hibernate.model.Identidad;
import hibernate.model.Roles;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class RegistrarAdministradorController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    private Stage stagePrincipal;
    
    private Empresa empresa;
   
    ArrayList<EstadoCivil> estadosCivil;
    ArrayList<Usuario> empleados; 
            
    ArrayList<Roles> roles;
    
    @FXML
    private ChoiceBox estadoCivilChoiceBox;
    
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
    private TextField usuarioField;
    
    @FXML
    private TextField contrasenaField;
    
    @FXML
    private TextField contrasenaField2;
    
    @FXML
    private CheckBox checkBoxAsignar;
    
    @FXML
    private Pane panelUsuario;
    
    @FXML 
    private ChoiceBox choiceBoxEmpleado;
    
    @FXML
    private Label errorText;
    
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
    
    IdentidadDAO identidadDAO;
    
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
    public void onClickAsignar(ActionEvent event) {
        if (checkBoxAsignar.isSelected()) {
            panelUsuario.setVisible(false);
            choiceBoxEmpleado.setVisible(true);
        } else {
            panelUsuario.setVisible(true);
            choiceBoxEmpleado.setVisible(false);
        }
    }
    
    @FXML
    private void onCLickGuardar(ActionEvent event) throws IOException, Exception {
        System.out.println("aplicacion.control.RegistrarEmpleadoController.onCLickGuardar()");
        
        identidadDAO = new IdentidadDAO();
        
        if (usuarioField.getText().isEmpty() || usuarioField.getText().length() < 4) {
            errorText.setText("Debe ingresar un usuario valido");
        } else if (contrasenaField.getText().isEmpty()) {
            errorText.setText("Debe ingresar una contraseña");
        } else if (contrasenaField.getText().length() < 4) {
            errorText.setText("Debe ingresar una contraseña de minimo 4 caracteres"); 
        } else if (contrasenaField2.getText().isEmpty()) {
            errorText.setText("Debe repetir la contraseña"); 
        } else if (!contrasenaField.getText().equals(contrasenaField2.getText())) {
            errorText.setText("Las contraseñas no son las mismas"); 
        } else {
        
            if (checkBoxAsignar.isSelected()) {
                
                if (choiceBoxEmpleado.getSelectionModel().isEmpty()) {
                   errorText.setText("Seleccione el empleado");  
                } else {
                    if (identidadDAO.findByNombreUsuarioActivo(usuarioField.getText()) == null) {

                        Identidad identidad = new Identidad();

                        Integer usuarioId = empleados.get(choiceBoxEmpleado.getSelectionModel().getSelectedIndex()).getId();

                        if (identidadDAO.findByUsuarioIdActivo(usuarioId) == null) {

                            guardarIdentidad(empleados.get(choiceBoxEmpleado.getSelectionModel().getSelectedIndex()));
                            
                        } else {
                            errorText.setText("El empleado ya tiene un usario creado");  
                        }
                    } else {
                        errorText.setText("Ya esta en uso esta cedula");
                    }
                }

            } else {

                if (nombreField.getText().isEmpty()) {
                    errorText.setText("Debe ingresar un nombre");
                } else if (apellidoField.getText().isEmpty()) {
                    errorText.setText("Debe ingresar un apellido");
                } else if (cedulaField.getText().isEmpty()) {
                    errorText.setText("Debe ingresar la cedula");
                } else if (!sexoF.isSelected() && !sexoM.isSelected()) {
                    errorText.setText("Debe seleccionar el sexo");
                } else if (telefonoField.getText().isEmpty()) {
                    errorText.setText("Debe ingresar un numero telefonico");
                } else if (direccionField.getText().isEmpty()) {
                    errorText.setText("Debe ingresar la direccion");
                } else if (emailField.getText().isEmpty()) {
                    errorText.setText("Debe ingresar una direccion");
                } else if (estadoCivilChoiceBox.getSelectionModel().isEmpty()) {
                    errorText.setText("Debe seleccionar el estado civil");
                } else if (datePicker.getValue() == null) {
                    errorText.setText("Debe seleccionar la fecha de nacimiento");
                } else {
                    UsuarioDAO usuariosDAO = new UsuarioDAO();

                    if (usuariosDAO.findByCedulaActivo(cedulaField.getText()) == null) {
                        
                        if (identidadDAO.findByNombreUsuarioActivo(usuarioField.getText()) == null) {

                            Usuario usuario = new Usuario();
                            usuario.setNombre(nombreField.getText());
                            usuario.setApellido(apellidoField.getText());
                            usuario.setCedula(cedulaField.getText());
                            if (sexoM.isSelected()) {
                                usuario.setSexo("M");
                            } else {
                                usuario.setSexo("F");
                            }
                            usuario.setEmail(emailField.getText());
                            usuario.setNacimiento(Timestamp.valueOf(datePicker.getValue().atStartOfDay()));
                            usuario.setDireccion(direccionField.getText());
                            usuario.setTelefono(telefonoField.getText());
                            usuario.setEstadoCivil(estadosCivil.get(estadoCivilChoiceBox.getSelectionModel().getSelectedIndex()));
                            usuario.setActivo(Boolean.TRUE);
                            usuario.setCreacion(new Timestamp(new Date().getTime()));
                            usuario.setUltimaModificacion(new Timestamp(new Date().getTime()));

                            usuariosDAO.save(usuario);

                            guardarIdentidad(usuario);
                            
                        } else {
                           errorText.setText("Ya esta en uso este usuario"); 
                        }
                    } else {
                        errorText.setText("Ya esta en uso esta cedula");
                    }
                }
            } 
        }
    }
    
    public void guardarIdentidad(Usuario usuario) {
        
        Identidad identidad = new Identidad();

        identidad.setNombreUsuario(usuarioField.getText());
        identidad.setContrasena(Password.MD5(contrasenaField.getText()));
        identidad.setActivo(Boolean.TRUE);
        identidad.setUsuario(usuario);

        identidadDAO.save(identidad);
        stagePrincipal.close();
        
        seleccionarPermisos(usuario);
    }
    
    public void seleccionarPermisos(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(AplicacionControl.class.getResource("ventanas/VentanaAsignarRol.fxml"));
            AnchorPane ventanaPermisos = (AnchorPane) loader.load();
            Stage ventana = new Stage();
            ventana.setTitle(usuarioField.getText());
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_registro.png").toExternalForm();
            ventana.getIcons().add(new Image(stageIcon));
            ventana.setResizable(false);
            ventana.initOwner(stagePrincipal);
            Scene scene = new Scene(ventanaPermisos);
            ventana.setScene(scene);
            AsignarRolController controller = loader.getController();
            controller.setStagePrincipal(ventana);
            controller.setUsuario(usuario);
            controller.setProgramaPrincipal(this);
            ventana.show();
 
        } catch (Exception e) {
            e.printStackTrace();
            //tratar la excepción
        }
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
    public void onClickCancelar(ActionEvent event) {
        stagePrincipal.close();
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EstadoCivilDAO estadoCivilDAO = new EstadoCivilDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        estadosCivil = (ArrayList<EstadoCivil>) estadoCivilDAO.findAll();
        empleados = (ArrayList<Usuario>) usuarioDAO.findAllEmpleadosActivos();
        
        String[] itemsEstadosCivil = new String[estadosCivil.size()];
        String[] itemsEmpleados = new String[empleados.size()];
        
        for (EstadoCivil obj: estadosCivil) {
            itemsEstadosCivil[estadosCivil.indexOf(obj)] = obj.getNombre();
        }
        
        for (Usuario obj: empleados) {
            itemsEmpleados[empleados.indexOf(obj)] = obj.getNombre() + " " + obj.getApellido();
        }
        
        estadoCivilChoiceBox.setItems(FXCollections.observableArrayList(itemsEstadosCivil)); 
        choiceBoxEmpleado.setItems(FXCollections.observableArrayList(itemsEmpleados));
        
        RolesDAO rolesDAO = new RolesDAO();
        roles = (ArrayList<Roles>) rolesDAO.findAll();
        
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
