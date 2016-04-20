/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import hibernate.dao.ClienteDAO;
import hibernate.model.Cliente;
import hibernate.model.Usuarios;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class HorasExtrasController implements Initializable {
    
    private InEmpresaController inEmpresaController;
    
    private Stage stagePrincipal;
    
    private Usuarios empleado;
    
    @FXML
    private TextField suplementarias;
    
    @FXML
    private TextField sobreTiempo;
    
    @FXML
    private Label textError;
    
    @FXML
    private ChoiceBox selector;
    
    private ArrayList<Cliente> clientes;
    
    private Cliente cliente;
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(InEmpresaController inEmpresaController) {
        this.inEmpresaController = inEmpresaController;
    }
    
    @FXML
    private void salirVentana(ActionEvent event) {
        stagePrincipal.close();
    }
    
    @FXML
    private void guardar(ActionEvent event) throws NoSuchAlgorithmException, UnsupportedEncodingException, ParseException {
        stagePrincipal.close();
        if (!clientes.isEmpty() && !selector.getSelectionModel().isEmpty()) {
            cliente = clientes.get(selector.getSelectionModel().getSelectedIndex());
        }
        if (suplementarias.getText().isEmpty() && sobreTiempo.getText().isEmpty()) {
           inEmpresaController.guardarRegistro(empleado, 0, 0, cliente); 
        } else if (suplementarias.getText().isEmpty()) {
            inEmpresaController.guardarRegistro(empleado, 0, 
                Integer.parseInt(sobreTiempo.getText()), cliente);
        } else if (sobreTiempo.getText().isEmpty()) {
            inEmpresaController.guardarRegistro(empleado, 
                Integer.parseInt(suplementarias.getText()), 0, cliente);
        } else {
            inEmpresaController.guardarRegistro(empleado, 
                    Integer.parseInt(suplementarias.getText()), 
                    Integer.parseInt(sobreTiempo.getText()), cliente);
        }
    }
    
    public void setEmpleado(Usuarios empleado) {
        this.empleado = empleado;
    }

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        suplementarias.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        sobreTiempo.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
        
        ClienteDAO clienteDAO = new ClienteDAO();
        clientes = new ArrayList<>();
        clientes.addAll(clienteDAO.findAll());
        
        String[] items = new String[clientes.size()];
        
        for (Cliente cli: clientes) {
            items[clientes.indexOf(cli)] = cli.getNombre();
        }
        
        selector.setItems(FXCollections.observableArrayList(items));
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
