/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.HorasExtrasController.getDateFromTimestamp;
import hibernate.dao.ControlEmpleadoDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlEmpleado;
import hibernate.model.Usuario;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class HorasExtrasClienteController implements Initializable {
    
    private RolDePagoClienteController rolDePagoClienteController;
    
    private Stage stagePrincipal;
    
    private Usuario empleado;
    
    @FXML
    private TextField suplementarias;
    
    @FXML
    private TextField sobreTiempo;
    
    @FXML
    private Label textError;
    
    @FXML
    private DatePicker datePickerFecha;
    
    @FXML 
    private CheckBox checkBoxLibre;
    
    @FXML 
    private GridPane panelHoras;
    
    @FXML
    private Label textEmpleadoLibre;
    
    @FXML
    private Label textCliente;
    
    private Cliente cliente;
    
    private ControlEmpleado lastControl;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(RolDePagoClienteController rolDePagoClienteController) {
        this.rolDePagoClienteController = rolDePagoClienteController;
        if (!rolDePagoClienteController.controlEmpleado.isEmpty()) {
          lastControl = rolDePagoClienteController.controlEmpleado.get(rolDePagoClienteController.controlEmpleado.size() - 1); 
          DateTime dateTime = new DateTime(lastControl.getFecha().getTime());
          datePickerFecha.setValue(getDateFromTimestamp(new Timestamp(dateTime.plusDays(1).getMillis())));
          
        } else {
           datePickerFecha.setValue(getDateFromTimestamp(rolDePagoClienteController.inicio));
        }
    }
    
    @FXML
    private void salirVentana(ActionEvent event) {
        stagePrincipal.close();
    }
    
    @FXML
    private void checkLibre(ActionEvent event) {
        System.out.println("aplicacion.control.HorasExtrasController.checkLibre()");
        if (checkBoxLibre.isSelected()) {
            panelHoras.setVisible(false); 
            textEmpleadoLibre.setVisible(true);
        } else {
            panelHoras.setVisible(true);
            textEmpleadoLibre.setVisible(false);
        }
    }
    
    @FXML
    private void guardar(ActionEvent event) throws NoSuchAlgorithmException, UnsupportedEncodingException, ParseException {
        
        if (datePickerFecha.getValue() == null) {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setTitle("Dialogo");
            String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
            dialogStage.getIcons().add(new Image(stageIcon));
            Button buttonOk = new Button("ok");
            dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
            children(new Text("Fecha no valida"), buttonOk).
            alignment(Pos.CENTER).padding(new Insets(10)).build()));
            dialogStage.show();
            buttonOk.setOnAction((ActionEvent e) -> {
                dialogStage.close();
            });
        } else {
            Timestamp timestamp = Timestamp.valueOf(datePickerFecha.getValue().atStartOfDay());
            
            ControlEmpleadoDAO controlEmpleadoDAO = new ControlEmpleadoDAO();
            if (controlEmpleadoDAO.findByFecha(timestamp, empleado.getId()) == null) {
                stagePrincipal.close();

                if (checkBoxLibre.isSelected()) {
                    rolDePagoClienteController.guardarRegistro(empleado, 0, 0, cliente, timestamp, true);
                } else {
                    if (suplementarias.getText().isEmpty() && sobreTiempo.getText().isEmpty()) {
                       rolDePagoClienteController.guardarRegistro(empleado, 0, 0, cliente, timestamp, false); 
                    } else if (suplementarias.getText().isEmpty()) {
                        rolDePagoClienteController.guardarRegistro(empleado, 0, 
                            Integer.parseInt(sobreTiempo.getText()), cliente, timestamp, false);
                    } else if (sobreTiempo.getText().isEmpty()) {
                        rolDePagoClienteController.guardarRegistro(empleado, 
                            Integer.parseInt(suplementarias.getText()), 0, cliente, timestamp, false);
                    } else {
                        rolDePagoClienteController.guardarRegistro(empleado, 
                                Integer.parseInt(suplementarias.getText()), 
                                Integer.parseInt(sobreTiempo.getText()), cliente, timestamp, false);
                    }
                }
            } else {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Dialogo");
                String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button buttonOk = new Button("ok");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("Ya el empleado tiene un registro con esta fecha"), buttonOk).
                alignment(Pos.CENTER).padding(new Insets(10)).build()));
                dialogStage.show();
                buttonOk.setOnAction((ActionEvent e) -> {
                    dialogStage.close();
                }); 
            }
        }
    }
    
    public void setEmpleado(Usuario empleado, Cliente cliente) {
        this.empleado = empleado;
        this.cliente = cliente;
    }

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        suplementarias.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        sobreTiempo.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
    }    
    
    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        };
        return aux;
    }
    
    public static Timestamp getToday() throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = new Date();

        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        
        return new Timestamp(todayWithZeroTime.getTime());
    }
    
    public static LocalDate getDateFromTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            DateTime dateTime = new DateTime(timestamp.getTime());
            return LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
        }
    }
}
