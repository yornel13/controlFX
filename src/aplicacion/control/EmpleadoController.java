/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.asincronas.PrintFondoReservaReport;
import aplicacion.control.eventos.DialogResponse;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.DialogUtil;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.ActuarialesDAO;
import hibernate.dao.DetallesEmpleadoDAO;
import hibernate.dao.DeudaTipoDAO;
import hibernate.dao.DiasVacacionesDAO;
import hibernate.dao.EmpresaDAO;
import hibernate.dao.FotoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Actuariales;
import hibernate.model.DetallesEmpleado;
import hibernate.model.DeudaTipo;
import hibernate.model.DiasVacaciones;
import hibernate.model.Empresa;
import hibernate.model.Foto;
import hibernate.model.Usuario;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class EmpleadoController extends BaseController implements 
        Initializable, 
        DialogResponse {
    
    private Usuario empleado;
    
    private Foto foto;
    
    private Empresa empresaSelected;
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private Button administradoresButton;
    
    @FXML
    private Button empresaButton;
    
    @FXML
    private Pane profileImage;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Label cedula;
    
    @FXML
    private Label telefono;
    
    @FXML
    private Label direccion;
    
    @FXML
    private Label email;
    
    @FXML
    private Label estadoCivil;
    
    @FXML
    private Label empresa;
    
    @FXML
    private Label departamento;
    
    @FXML
    private Label cargo;
    
    @FXML
    private Label cuenta;
    
    @FXML
    private Label sueldo;
    
    @FXML
    private Label cumpleano;
    
    @FXML
    private Label observacion;
    
    @FXML
    private Label nombre;
    
    @FXML
    private Label fechaInicio;
    
    @FXML
    private Label fechaContrato;
    
    @FXML
    private Button editarButton;
    
    @FXML
    private Button textCambiarEmpresa;
    
    @FXML
    private Button buttonCambiarEmpresa;
    
    
    @FXML
    public void changeImage(ActionEvent event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona la foto");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));                 
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("GIF", "*.gif"),
            new FileChooser.ExtensionFilter("BMP", "*.bmp"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        
        File file = fileChooser.showOpenDialog(stagePrincipal);
        
        if (file != null) {
            System.out.println(file);
            String profile = file.toURI().toString();

            Image image = new Image(profile);
            setProfileImage(image);
            
            byte[] bFile = new byte[(int) file.length()];
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                //convert file into array of bytes
                fileInputStream.read(bFile);
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Foto foto = new FotoDAO().findByEmpleadoId(empleado.getId());
            if (foto != null) {
                foto.setFoto(bFile);
                HibernateSessionFactory.getSession().flush();
            } else {
                foto = new Foto();
                foto.setUsuario(empleado);
                foto.setFoto(bFile);
                new FotoDAO().save(foto);
            }
        }
    }
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    
   public void showChangeEmpresa() {
        textCambiarEmpresa.setVisible(true);
        buttonCambiarEmpresa.setVisible(true);
   }
    
    @FXML
    public void editarEmpresa(ActionEvent event) throws FileNotFoundException {
        showDialogChangeEmpresa();
    }
    
     private void showDialogChangeEmpresa(){
        EmpresaDAO empresaDao = new EmpresaDAO();
        ArrayList<Empresa> empresas = new ArrayList<>();
        empresas.addAll(empresaDao.findAll());
        String[] items = new String[empresas.size()];
        empresas.stream().forEach((emp) -> {
            items[empresas.indexOf(emp)] = emp.getNombre();
        });
         
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Cambiar Empresa");
        String stageIcon = AplicacionControl.class
                .getResource("imagenes/settings_36dp.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button buttonOk = new Button("Cambiar");
        Button buttonNo = new Button("Cancelar");
        ChoiceBox choiceBoxEmpresas = new ChoiceBox();
        choiceBoxEmpresas.setItems(FXCollections.observableArrayList(items));
        HBox hBox = HBoxBuilder.create()
                .spacing(10.0) //In case you are using HBoxBuilder
                .padding(new Insets(5, 5, 5, 5))
                .alignment(Pos.CENTER)
                .children(buttonOk, buttonNo)
                .build();
        hBox.maxWidth(120);
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Seleccione la empresa a la cual desea cambiar el empleado"),
                choiceBoxEmpresas,
                hBox).
        alignment(Pos.CENTER).padding(new Insets(20)).build()));
        buttonOk.setMinWidth(50);
        buttonNo.setMinWidth(50);
        buttonOk.setOnAction((ActionEvent e) -> {
            empresaSelected = empresas.get(choiceBoxEmpresas
                    .getSelectionModel().getSelectedIndex());
            changeEmpresa();
            dialogStage.close();
        });
        buttonNo.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
        buttonOk.setDisable(true);
        choiceBoxEmpresas.getSelectionModel().selectedIndexProperty()
                .addListener((ObservableValue<? extends Number> 
                        observableValue, Number number, Number number2) -> {
            buttonOk.setDisable(false);
        });
        dialogStage.show();
    }
     
    public void changeEmpresa() {
        DialogUtil.confirm("Cambiar Empresa", 
                "¿Seguro que desea cambiar el empleado a este empresa?\n"
                        + "Toda su informacion se clonara a la nueva empresa,\n"
                        + "excepto deudas, horarios, y pagos realizados.", this);
    }
    @Override
    public void onDialogOK() {
        UsuarioDAO empleadoDAO = new UsuarioDAO();
        DetallesEmpleadoDAO detallesDAO = new DetallesEmpleadoDAO();
        Usuario empleadoS = empleadoDAO.findByCedulaAndEmpresaId(empleado
                .getCedula(), empresaSelected.getId());
        if (empleadoS != null) {
            DialogUtil.error("Cambiar Empresa", 
                    "El empleado ya se encuentra registrado en la empresa seleccionada.");
        } else {
            DetallesEmpleado deN = new DetallesEmpleado();
            deN.setEmpresa(empresaSelected);
            deN.setDepartamento(empleado.getDetallesEmpleado().getDepartamento());
            deN.setCargo(empleado.getDetallesEmpleado().getCargo());
            deN.setFechaInicio(empleado.getDetallesEmpleado().getFechaInicio());
            deN.setFechaContrato(empleado.getDetallesEmpleado().getFechaContrato());
            deN.setObservacion(empleado.getDetallesEmpleado().getObservacion());
            deN.setNroCuenta(empleado.getDetallesEmpleado().getNroCuenta());
            deN.setSueldo(empleado.getDetallesEmpleado().getSueldo());
            deN.setQuincena(empleado.getDetallesEmpleado().getQuincena());
            deN.setAcumulaDecimos(empleado.getDetallesEmpleado().getAcumulaDecimos());
            detallesDAO.save(deN);
            
            Usuario usuN = new Usuario();
            usuN.setNombre(empleado.getNombre());
            usuN.setApellido(empleado.getApellido());
            usuN.setCedula(empleado.getCedula());
            usuN.setSexo(empleado.getSexo());
            usuN.setEmail(empleado.getEmail());
            usuN.setNacimiento(empleado.getNacimiento());
            usuN.setDireccion(empleado.getDireccion());
            usuN.setTelefono(empleado.getTelefono());
            usuN.setDetallesEmpleado(deN);
            usuN.setEstadoCivil(empleado.getEstadoCivil());
            usuN.setActivo(Boolean.TRUE);
            usuN.setCreacion(new Timestamp(new Date().getTime()));
            usuN.setUltimaModificacion(new Timestamp(new Date().getTime()));
            empleadoDAO.save(usuN);
            
            if (foto != null && foto.getFoto() != null) {
                Foto fN = new Foto();
                fN.setUsuario(usuN);
                fN.setFoto(foto.getFoto());
                new FotoDAO().save(fN);
            }
            
            ActuarialesDAO adao = new ActuarialesDAO();
            Actuariales actuariales = adao.findByEmpleadoId(empleado.getId());
            if (actuariales != null) {
                Actuariales actN = new Actuariales();
                actN.setActivo(true);
                actN.setPrimario(actuariales.getPrimario());
                actN.setSecundario(actuariales.getSecundario());
                actN.setFecha(new Timestamp(new Date().getTime()));
                actN.setUsuario(usuN);
                new ActuarialesDAO().save(actN);
            }
            DiasVacacionesDAO dvdao = new DiasVacacionesDAO();
            DiasVacaciones diasVacaciones = dvdao.findByEmpleadoId(empleado.getId());
            if (diasVacaciones != null) {
                DiasVacaciones diasN = new DiasVacaciones();
                diasN.setUsuario(usuN);
                diasN.setAumentado(Boolean.TRUE);
                diasN.setActivo(Boolean.TRUE);
                diasN.setDias(diasVacaciones.getDias());
                diasN.setFecha(new Timestamp(new Date().getTime()));
                new DiasVacacionesDAO().save(diasN);
            }
            
            DialogUtil.completed("Cambiar Empresa", 
                "Emplado cambiar a la nueva empresa!");
        }
    }
    
    @Override
    public void onDialogCancel() {
        showDialogChangeEmpresa();
    }
    
    public void editarEmpleado(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.EMPLEADOS, Permisos.Nivel.EDITAR)) {
               
                aplicacionControl.mostrarEditarEmpleado(empleado);
                stagePrincipal.close();
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void setEmpleado(Usuario empleado   ) throws IOException, SQLException {
        this.empleado = empleado;
        nombre.setText(empleado.getNombre() + " " + empleado.getApellido());
        cedula.setText(empleado.getCedula());
        telefono.setText(empleado.getTelefono());
        direccion.setText(empleado.getDireccion());
        email.setText(empleado.getEmail());
        estadoCivil.setText(empleado.getEstadoCivil().getNombre());
        empresa.setText(empleado.getDetallesEmpleado().getEmpresa().getNombre());
        departamento.setText(empleado.getDetallesEmpleado().getDepartamento().getNombre());
        cargo.setText(empleado.getDetallesEmpleado().getCargo().getNombre());
        cuenta.setText(empleado.getDetallesEmpleado().getNroCuenta());
        sueldo.setText("$"+empleado.getDetallesEmpleado().getSueldo());
        observacion.setText(empleado.getDetallesEmpleado().getObservacion());
        DateTime inicio = new DateTime(empleado.getDetallesEmpleado().getFechaInicio().getTime());
        fechaInicio.setText(inicio.getDayOfMonth() + " de " + getMonthName(inicio.getMonthOfYear()) + " " + inicio.getYear());
        DateTime contrato = new DateTime(empleado.getDetallesEmpleado().getFechaContrato().getTime());
        fechaContrato.setText(contrato.getDayOfMonth() + " de " + getMonthName(contrato.getMonthOfYear()) + " " + contrato.getYear());
        DateTime nacimiento = new DateTime(empleado.getNacimiento().getTime());
        cumpleano.setText(nacimiento.getDayOfMonth() + " de " + getMonthName(nacimiento.getMonthOfYear()) + " " + nacimiento.getYear());
        
        foto = new FotoDAO().findByEmpleadoId(empleado.getId());
        
        if (foto != null && foto.getFoto() != null) {
            final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(foto.getFoto()));
            Image image  =  SwingFXUtils.toFXImage(bufferedImage, null);
            setProfileImage(image);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String profile = AplicacionControl.class.getResource("imagenes/empty_profile.jpg").toExternalForm();
        Image image = new Image(profile);
        setProfileImage(image);
    }  
    
    public static String getMonthName(int month){
        Calendar cal = Calendar.getInstance();
        // Calendar numbers months from 0
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
    
    public void setProfileImage(Image image) {
        Rectangle rekt = new Rectangle(40, 40); 
        ImagePattern imagePattern = new ImagePattern(image);
        rekt.setFill(imagePattern);
        

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setFitHeight(160);
        //imageView.setPreserveRatio(true);  // para mantener la escala
        imageView.setSmooth(true);
        imageView.setCache(true);
        
        profileImage.getChildren().clear();
        profileImage.getChildren().add(imageView);
    }
}
