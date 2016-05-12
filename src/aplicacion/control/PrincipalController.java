/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Const;
import hibernate.dao.EmpresaDAO;
import hibernate.model.Empresa;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Yornel
 */
public class PrincipalController implements Initializable {
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Label label;
    
    @FXML
    private ChoiceBox selector;
    
    @FXML
    private Button configuracion;
    
    ArrayList<Empresa> empresas;
    
    public static final String REPORTE_IMPORTE = "/report/prueba_Mano.jrxml";
    
    private Stage stagePrincipal;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws JRException {
        if (!empresas.isEmpty() && !selector.getSelectionModel().isEmpty()) {
            aplicacionControl.mostrarInEmpresa(empresas.get(selector.getSelectionModel().getSelectedIndex()));
        }  else {
            label.setText("Selecciona una empresa primero.");
        }
        
        
        
        //File file = new File("dfsd");
        
        //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(REPORTE_IMPORTE);
        
        //Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        //JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, new Em);
       
    }
    
    
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me menu!");
        //label.setText(menuButton.getCursor().toString());
    }
    
    @FXML
    private void onCLickConfiguration(ActionEvent event) throws IOException, Exception {
        aplicacionControl.mostrarConfiguracion();
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EmpresaDAO empresaDao = new EmpresaDAO();
        empresas = new ArrayList<>();
        empresas.addAll(empresaDao.findAll());
        
        String[] items = new String[empresas.size()];
        
        empresas.stream().forEach((emp) -> {
            items[empresas.indexOf(emp)] = emp.getNombre();
        });
        
        selector.setItems(FXCollections.observableArrayList(items)); 
        
        String image = AplicacionControl.class.getResource("imagenes/config_admin.png").toExternalForm();
        
        Image adminImage = new Image(image);
        configuracion.setGraphic(new ImageView(adminImage));
        configuracion.setStyle(Const.BACKGROUND_COLOR_SEMI_TRANSPARENT);
        
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
