/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.FotoDAO;
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
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.joda.time.DateTime;

/**
 *
 * @author Yornel
 */
public class EmpleadoController implements Initializable {
    
    private Usuario empleado;
    
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
    private Label extra;
    
    @FXML
    private Label nombre;
    
    @FXML
    private Label fechaInicio;
    
    @FXML
    private Label fechaContrato;
    
    @FXML
    private Button editarButton;
    
    
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
        //extra.setText(empleado.getDetallesEmpleado().getExtra()); TODO, extra elimando
        DateTime inicio = new DateTime(empleado.getDetallesEmpleado().getFechaInicio().getTime());
        fechaInicio.setText(inicio.getDayOfMonth() + " de " + getMonthName(inicio.getMonthOfYear()) + " " + inicio.getYear());
        DateTime contrato = new DateTime(empleado.getDetallesEmpleado().getFechaContrato().getTime());
        fechaContrato.setText(contrato.getDayOfMonth() + " de " + getMonthName(contrato.getMonthOfYear()) + " " + contrato.getYear());
        DateTime nacimiento = new DateTime(empleado.getNacimiento().getTime());
        cumpleano.setText(nacimiento.getDayOfMonth() + " de " + getMonthName(nacimiento.getMonthOfYear()) + " " + nacimiento.getYear());
        
        Foto foto = new FotoDAO().findByEmpleadoId(empleado.getId());
        
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
