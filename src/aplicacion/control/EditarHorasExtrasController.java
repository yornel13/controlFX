/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.util.Numeros.round;
import hibernate.dao.ClienteDAO;
import hibernate.model.Cliente;
import hibernate.model.ControlEmpleado;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
public class EditarHorasExtrasController implements Initializable {
    
    private RolDePagoController rolDePagoController;
    
    private Stage stagePrincipal;
    
    private ControlEmpleado controlEmpleado;
    
    @FXML
    private TextField suplementarias;
    
    @FXML
    private TextField sobreTiempo;
    
    @FXML
    private ChoiceBox selector;
    
    @FXML
    private DatePicker datePickerFecha;
    
    @FXML 
    private GridPane panelHoras;
    
    @FXML
    private Label textEmpleadoLibre;
    
    @FXML 
    private RadioButton marcarTrabajo;
    
    @FXML 
    private RadioButton marcarLibre;
    
    @FXML 
    private RadioButton marcarFalta;
    
    private ArrayList<Cliente> clientes;
    
    private Cliente cliente;
    
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(RolDePagoController rolDePagoController) {
        this.rolDePagoController = rolDePagoController;
    }
    
    @FXML
    private void salirVentana(ActionEvent event) {
        stagePrincipal.close();
    }
    
    @FXML
    private void checkTrabajo(ActionEvent event) {
        panelHoras.setVisible(true);
        textEmpleadoLibre.setText("");
    }
    
    @FXML
    private void checkLibre(ActionEvent event) {
        panelHoras.setVisible(false);
        textEmpleadoLibre.setText("Libre");
    }
    
    @FXML
    private void checkFalta(ActionEvent event) {
        panelHoras.setVisible(false);
        textEmpleadoLibre.setText("Falta");
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
            stagePrincipal.close();
            
            if (!clientes.isEmpty() && !selector.getSelectionModel().isEmpty() 
                        && selector.getSelectionModel().getSelectedItem() != null) {
                    cliente = clientes.get(selector.getSelectionModel().getSelectedIndex());
            }
            
            if (marcarLibre.isSelected()) {
                rolDePagoController.guardarRegistroEditado(controlEmpleado, 0d, 0d, cliente, timestamp, true, false);
            }else if (marcarFalta.isSelected()) {
                rolDePagoController.guardarRegistroEditado(controlEmpleado, 0d, 0d, cliente, timestamp, false, true);
            } else {
                if (suplementarias.getText().isEmpty() && sobreTiempo.getText().isEmpty()) {
                   rolDePagoController.guardarRegistroEditado(controlEmpleado, 0d, 0d, cliente, timestamp, false, false); 
                } else if (suplementarias.getText().isEmpty()) {
                    rolDePagoController.guardarRegistroEditado(controlEmpleado, 0d, 
                        round(Double.valueOf(sobreTiempo.getText())), cliente, timestamp, false, false);
                } else if (sobreTiempo.getText().isEmpty()) {
                    rolDePagoController.guardarRegistroEditado(controlEmpleado, 
                        round(Double.valueOf(suplementarias.getText())), 0d, cliente, timestamp, false, false);
                } else {
                    rolDePagoController.guardarRegistroEditado(controlEmpleado, 
                            round(Double.valueOf(suplementarias.getText())), 
                            round(Double.valueOf(sobreTiempo.getText())), cliente, timestamp, false, false);
                }
            }
        }
    }
    
    public void setControlEmpleado(ControlEmpleado controlEmpleado) {
        this.controlEmpleado = controlEmpleado;
        
        sobreTiempo.setText(this.controlEmpleado.getSobretiempo().toString());
        suplementarias.setText(this.controlEmpleado.getRecargo().toString());
        if (controlEmpleado.getCliente() != null) {
            selector.getSelectionModel().select(controlEmpleado.getCliente().getNombre());
        }
        datePickerFecha.setValue(getDateFromTimestamp(this.controlEmpleado.getFecha()));
        /*if (controlEmpleado.getLibre()) {
            marcarLibre.setSelected(true);
            checkLibre(null);
        } else if (controlEmpleado.getFalta()) {
            marcarFalta.setSelected(true);
            checkFalta(null);
        }*/
    }

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        suplementarias.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        sobreTiempo.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        datePickerFecha.setDisable(true);
        
        ClienteDAO clienteDAO = new ClienteDAO();
        clientes = new ArrayList<>();
        clientes.addAll(clienteDAO.findAllActivo());
        
        String[] items = new String[clientes.size() + 1];
        for (Cliente cli: clientes) {
            items[clientes.indexOf(cli)] = cli.getNombre();
        }
        
        selector.setItems(FXCollections.observableArrayList(items));
    }    
    
    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789.".contains(keyEvent.getCharacter())) {
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
