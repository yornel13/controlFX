/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import static aplicacion.control.ConfiguracionEmpresaController.numDecimalFilter;
import aplicacion.control.tableModel.EmpleadoTable;
import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.CargoDAO;
import hibernate.dao.DepartamentoDAO;
import hibernate.dao.DetallesEmpleadoDAO;
import hibernate.dao.SeguroDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Cargo;
import hibernate.model.Departamento;
import hibernate.model.Empresa;
import hibernate.model.Seguro;
import hibernate.model.Usuario;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Yornel
 */
public class DepartamentosCargosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private TableView departamentosTableView;
    
    @FXML
    private TableView cargosTableView;
    
    private ObservableList<Departamento> dataDepartamentos;
    
    private ObservableList<Cargo> dataCargos;
    
    ArrayList<Usuario> usuarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void agregarCargo(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Cargo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button cambiarValor = new Button("Agregar");
        TextField field = new TextField();
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Escriba el nombre del cargo"), field, cambiarValor).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        field.setPrefWidth(150);
        cambiarValor.setPrefWidth(100);
        dialogStage.show();
        cambiarValor.setOnAction((ActionEvent e) -> {
            if (field.getText().isEmpty()) {
            } else {
                Cargo cargo = new Cargo();
                cargo.setActivo(Boolean.TRUE);
                cargo.setNombre(field.getText());
                new CargoDAO().save(cargo);
                dialogStage.close();
                setCargoTable();
            }
        }); 
    }
    
    @FXML
    private void agregarDepartamento(ActionEvent event) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Departamento");
        String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));
        Button cambiarValor = new Button("Agregar");
        TextField field = new TextField();
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("Escriba el nombre del departamento"), field, cambiarValor).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        field.setPrefWidth(150);
        cambiarValor.setPrefWidth(100);
        dialogStage.show();
        cambiarValor.setOnAction((ActionEvent e) -> {
            if (field.getText().isEmpty()) {
            } else {
                Departamento departamento = new Departamento();
                departamento.setActivo(Boolean.TRUE);
                departamento.setNombre(field.getText());
                new DepartamentoDAO().save(departamento);
                dialogStage.close();
                setDepartamentoTable();
            }
        }); 
    }
    
    @FXML
    private void returnPrevius(ActionEvent event) {
        stagePrincipal.close();
        aplicacionControl.mostrarConfiguracion();
    } 
    
    public void deleteCargo(Cargo cargo) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
               
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Confirmación de borrado");
                String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));;
                Button buttonConfirmar = new Button("Si Borrar");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Borrar este cargo?"), buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                dialogStage.show();
                buttonConfirmar.setOnAction((ActionEvent e) -> {

                    if (new DetallesEmpleadoDAO().findByCargoId(cargo.getId()).isEmpty()) {
                        new CargoDAO().delete(cargo);
                        HibernateSessionFactory.getSession().flush();
                        dialogStage.close();
                        dataCargos.remove(cargo);
                    } else {
                        dialogStage.close();
                        dialogCargoError();
                    }
                });
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void deleteDepartamento(Departamento departamento) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.ELIMINAR)) {
               
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("Confirmación de borrado");
                String stageIcon = AplicacionControl.class.getResource("imagenes/completado.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));;
                Button buttonConfirmar = new Button("Si Borrar");
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("¿Borrar este Departamento"), buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                dialogStage.show();
                buttonConfirmar.setOnAction((ActionEvent e) -> {

                    if (new DetallesEmpleadoDAO().findByDepartamentoId(departamento.getId()).isEmpty()) {
                        new DepartamentoDAO().delete(departamento);
                        HibernateSessionFactory.getSession().flush();
                        dialogStage.close();
                        dataDepartamentos.remove(departamento);
                    } else {
                        dialogStage.close();
                        dialogDepartamentoError();
                    }
                });
                  
            } else {
               aplicacionControl.noPermitido();
            }
        } 
    }
    
    public void dialogCargoError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Dialogo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));;
        Button buttonConfirmar = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("El cargo esta en uso"), buttonConfirmar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        dialogStage.show();
        buttonConfirmar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    public void dialogDepartamentoError() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Dialogo");
        String stageIcon = AplicacionControl.class.getResource("imagenes/icon_error.png").toExternalForm();
        dialogStage.getIcons().add(new Image(stageIcon));;
        Button buttonConfirmar = new Button("ok");
        dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
        children(new Text("El departamento esta en uso"), buttonConfirmar).
        alignment(Pos.CENTER).padding(new Insets(25)).build()));
        dialogStage.show();
        buttonConfirmar.setOnAction((ActionEvent e) -> {
            dialogStage.close();
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        setDepartamentoTable();
        setCargoTable();
    }
    
    public void setDepartamentoTable() {
        departamentosTableView.setEditable(Boolean.FALSE);
        departamentosTableView.getColumns().clear();
        
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        dataDepartamentos = FXCollections.observableArrayList(); 
        dataDepartamentos.addAll(departamentoDAO.findAll());
        departamentosTableView.setItems(dataDepartamentos);
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Departamento, Departamento> delete = new TableColumn<>("Borrar");
        delete.setMinWidth(40);
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(Departamento departamento, boolean empty) {
                super.updateItem(departamento, empty);

                if (departamento == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteDepartamento(departamento);
                });
            }
        });
        
        departamentosTableView.getColumns().addAll(nombre, delete); 
    }
    
    public void setCargoTable() {
        cargosTableView.setEditable(Boolean.FALSE);
        cargosTableView.getColumns().clear();
        
        CargoDAO cargoDAO = new CargoDAO();
        dataCargos = FXCollections.observableArrayList();
        dataCargos.addAll(cargoDAO.findAll());
        cargosTableView.setItems(dataCargos);
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(100);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Cargo, Cargo> delete = new TableColumn<>("Borrar");
        delete.setMinWidth(40);
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<Cargo, Cargo>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(Cargo cargo, boolean empty) {
                super.updateItem(cargo, empty);

                if (cargo == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteCargo(cargo);
                });
            }
        });
        
        cargosTableView.getColumns().addAll(nombre, delete); 
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
