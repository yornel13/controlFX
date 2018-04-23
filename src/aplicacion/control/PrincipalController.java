/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Roboto;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ConstanteDAO;
import hibernate.dao.DiasVacacionesDAO;
import hibernate.dao.EmpresaDAO;
import hibernate.dao.RolIndividualDAO;
import hibernate.model.Constante;
import hibernate.model.DiasVacaciones;
import hibernate.model.Empresa;
import hibernate.model.RolIndividual;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.joda.time.DateTime;

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
    private Button buttonGlobal;
    
    @FXML
    private Button buttonEntrar;
    
    @FXML
    private Label labelEmpresa;
    
    @FXML
    private Label versionLabel;
    
    @FXML
    private ProgressBar progressBar;
    
    ArrayList<Empresa> empresas;
    
    private Stage stagePrincipal;
    private Dialog<Object> dialog;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (!empresas.isEmpty() && !selector.getSelectionModel().isEmpty()) {
            aplicacionControl.mostrarInEmpresa(empresas.get(selector.getSelectionModel().getSelectedIndex()));
        }  else {
            label.setText("Selecciona una empresa primero.");
        }
    }
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    @FXML
    private void onSelectMenuButtonItem(ActionEvent event) {
        System.out.println("You clicked me menu!");
    }
    
    @FXML
    private void onCLickConfiguration(ActionEvent event) throws IOException, Exception {
        aplicacionControl.mostrarConfiguracion();
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
        
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new DataBaseThread();
        executor.execute(worker);
        executor.shutdown();

    }
    
    public void aumentarVacaciones() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable worker = new PrincipalController.VacacionesThread();
        executor.execute(worker);
        executor.shutdown();

        label.setText("Aumentado +1 los dias de derecho a vacaciones de los empleados.");
        label.setTextFill(Color.WHITE);
        dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stagePrincipal);//stage here is the stage of your webview
        dialog.initStyle(StageStyle.TRANSPARENT);
        Label loader = new Label("   Cargando, por favor espere...");
        loader.setContentDisplay(ContentDisplay.LEFT);
        loader.setGraphic(new ProgressIndicator());
        dialog.getDialogPane().setGraphic(loader);
        dialog.getDialogPane().setStyle("-fx-background-color: #E0E0E0;");
        dialog.getDialogPane().setPrefSize(250, 75);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3); 
        ds.setOffsetY(1.3); 
        ds.setColor(Color.DARKGRAY);
        dialog.getDialogPane().setEffect(ds);
        dialog.show();
    
    }
    
    public void closeDialogMode() {
        if (dialog != null) {
           Stage toClose = (Stage) dialog.getDialogPane()
                   .getScene().getWindow();
           toClose.close();
           dialog.close();
           dialog = null;
        }
        label.setText("");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        buttonGlobal.setTooltip(
            new Tooltip("Configuración Global")
        );
        buttonGlobal.setOnMouseEntered((MouseEvent t) -> {
            buttonGlobal.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/global.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: #29B6F6;");
        });
        buttonGlobal.setOnMouseExited((MouseEvent t) -> {
            buttonGlobal.setStyle("-fx-background-image: "
                    + "url('aplicacion/control/imagenes/global.png'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; "
                    + "-fx-background-color: transparent;");
        });
        
        buttonEntrar.setFont(Roboto.MEDIUM(12));
        buttonEntrar.setOnMouseEntered((MouseEvent t) -> {
            buttonEntrar.setStyle("-fx-background-color: #E0E0E0;");
        });
        buttonEntrar.setOnMouseExited((MouseEvent t) -> {
            buttonEntrar.setStyle("-fx-background-color: #039BE5;");
        });
        
        labelEmpresa.setFont(Roboto.MEDIUM(14));
        label.setFont(Roboto.MEDIUM(14));
        
        selector.setVisible(false);
        label.setVisible(false);
        labelEmpresa.setVisible(false);
        buttonEntrar.setVisible(false);
        buttonGlobal.setVisible(false);
        login.setVisible(false);
        usuarioLogin.setVisible(false);
        
        versionLabel.setText("Version 1.10");
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
    
    public class DataBaseThread implements Runnable {

        boolean aumentar = false;
        
        public DataBaseThread(){
        }

        @Override
        public void run() {

            try {
                EmpresaDAO empresaDao = new EmpresaDAO();
                empresas = new ArrayList<>();
                empresas.addAll(empresaDao.findAll());

                String[] items = new String[empresas.size()];

                empresas.stream().forEach((emp) -> {
                    items[empresas.indexOf(emp)] = emp.getNombre();
                });

                selector.setItems(FXCollections.observableArrayList(items));
                
                
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ConstanteDAO cdao = new ConstanteDAO();                          // verificando si este ano se aumento +1 los dias de derecho a vacaciones
                Constante vacaciones = cdao.findUniqueResultByNombre("vacaciones");
                if (vacaciones == null) {
                    vacaciones = new Constante();
                    vacaciones.setActivo(Boolean.TRUE);
                    vacaciones.setNombre("vacaciones");
                    vacaciones.setValor(String.valueOf(new DateTime().getYear()));
                    cdao.save(vacaciones);
                } else {
                    if (Integer.valueOf(vacaciones.getValor()) < new DateTime().getYear()) {
                        System.out.println("Se debe aumentar el valor de los dias de derecho a vacaciones");
                        aumentar = true; 
                    } else {
                        System.out.println("No se requiere aumentar los dias de derecho a vacaciones");
                    }
                    System.out.println("ultimo cambio "+vacaciones.getValor());
                }
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                new RolIndividualDAO().clearDecimosPagados();
                for (RolIndividual rolIndividual: 
                        (List<RolIndividual>) new RolIndividualDAO().findAll()) {
                    Double devengado = 0d;
                    try {
                        devengado = Double.valueOf(rolIndividual.getEmpresa());
                    } catch (NumberFormatException ex) {
                        devengado = -1d;
                    }
                    rolIndividual.setEmpresa(devengado.toString());
                }
                HibernateSessionFactory.getSession().flush();
                
                
            } catch (Exception e) {
                Platform.runLater(new Runnable() {
                @Override public void run() {
                    label.setText("ERROR DE CONEXION A BASE DE DATOS.");
                    }
                });
            }
            selector.setVisible(true);
            label.setVisible(true);
            labelEmpresa.setVisible(true);
            buttonEntrar.setVisible(true);
            buttonGlobal.setVisible(true);
            login.setVisible(true);
            usuarioLogin.setVisible(true);
            progressBar.setVisible(false);
            
            if (aumentar) {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        aumentarVacaciones();
                    }
                });
            }
        }
    }
    
    public class VacacionesThread implements Runnable {

        public VacacionesThread(){
        }

        @Override
        public void run() {
            new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        cancel();
                        guardar(); 
                    }
             }, 1000, 1000);
        }
        
        private void guardar() {
            ArrayList<DiasVacaciones> diasVacacioneses = new ArrayList<>();
            diasVacacioneses.addAll(new DiasVacacionesDAO().findAll());
            for (DiasVacaciones diasVaca: diasVacacioneses) {
                if (diasVaca.getActivo()) {
                    diasVaca.setDias(diasVaca.getDias()+1);
                }
            }
            ConstanteDAO cdao = new ConstanteDAO(); 
            Constante vacaciones = cdao.findUniqueResultByNombre("vacaciones");
            vacaciones.setValor(String.valueOf(new DateTime().getYear()));
            HibernateSessionFactory.getSession().flush();
            
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    closeDialogMode();
                }
            });
        }
    }
}
