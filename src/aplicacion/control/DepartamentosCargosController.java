/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

import aplicacion.control.util.Permisos;
import hibernate.HibernateSessionFactory;
import hibernate.dao.CargoDAO;
import hibernate.dao.DepartamentoDAO;
import hibernate.dao.DetallesEmpleadoDAO;
import hibernate.dao.DeudaTipoDAO;
import hibernate.model.Cargo;
import hibernate.model.Departamento;
import hibernate.model.DeudaTipo;
import hibernate.model.Usuario;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class DepartamentosCargosController implements Initializable {
    
    private Stage stagePrincipal;
    
    private AplicacionControl aplicacionControl;
    
    @FXML
    private TableView departamentosTableView;
    
    @FXML
    private TableView cargosTableView;
    
    @FXML
    private TableView deudasTableView;
    
    private ObservableList<Departamento> dataDepartamentos;
    
    private ObservableList<Cargo> dataCargos;
    
    private ObservableList<DeudaTipo> dataDeudas;
    
    ArrayList<Usuario> usuarios;
    
    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    public void setProgramaPrincipal(AplicacionControl aplicacionControl) {
        this.aplicacionControl = aplicacionControl;
    }
    
    @FXML
    private void agregarCargo(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
            aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
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

                        // Registro para auditar
                        String detalles = "agrego el cargo " 
                                + cargo.getNombre();
                        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    }
                }); 
             } else {
                aplicacionControl.noPermitido();
            }
        }     
    }
    
    @FXML
    private void agregarDepartamento(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
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

                        // Registro para auditar
                        String detalles = "agrego el departamento " 
                                + departamento.getNombre();
                        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    }
                }); 
            } else {
                aplicacionControl.noPermitido();
            }
        } 
    }
    
     @FXML
    private void agregarDeuda(ActionEvent event) {
        if (aplicacionControl.permisos == null) {
           aplicacionControl.noLogeado();
        } else {
            if (aplicacionControl.permisos.getPermiso(Permisos.TOTAL, Permisos.Nivel.CREAR)) {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.setTitle("tipo de Deuda");
                String stageIcon = AplicacionControl.class.getResource("imagenes/admin.png").toExternalForm();
                dialogStage.getIcons().add(new Image(stageIcon));
                Button cambiarValor = new Button("Agregar");
                TextField field = new TextField();
                TextField fieldCuotas = new TextField();
                dialogStage.setScene(new Scene(VBoxBuilder.create().spacing(15).
                children(new Text("Escriba el nombre del tipo de deuda"), field, 
                        new Text("Cuotas por defecto"), fieldCuotas, cambiarValor).
                alignment(Pos.CENTER).padding(new Insets(20)).build()));
                field.setPrefWidth(150);
                fieldCuotas.setPrefWidth(50);
                fieldCuotas.addEventFilter(KeyEvent.KEY_TYPED, numFilter());
                cambiarValor.setPrefWidth(100);
                dialogStage.show();
                cambiarValor.setOnAction((ActionEvent e) -> {
                    if (field.getText().isEmpty()) {

                    } else if (fieldCuotas.getText().isEmpty()) {

                    } else {
                        DeudaTipo deudaTipo = new DeudaTipo();
                        deudaTipo.setActivo(Boolean.TRUE);
                        deudaTipo.setNombre(field.getText());
                        deudaTipo.setCuotas(Integer.parseInt(fieldCuotas.getText()));
                        new DeudaTipoDAO().save(deudaTipo);
                        dialogStage.close();
                        setDeudaTable();

                        // Registro para auditar
                        String detalles = "agrego el tipo de deuda " 
                                + deudaTipo.getNombre();
                        aplicacionControl.au.saveAgrego(detalles, aplicacionControl.permisos.getUsuario());
                    }
                }); 
            } else {
               aplicacionControl.noPermitido();
            }
        } 
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
                        
                        // Registro para auditar
                        String detalles = "elimino el cargo " 
                                + cargo.getNombre();
                        aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
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
                        
                        // Registro para auditar
                        String detalles = "elimino el departamento " 
                                + departamento.getNombre();
                        aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());
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
    
    public void deleteDeuda(DeudaTipo deudaTipo) {
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
                children(new Text("¿Borrar este tipo de deuda"), buttonConfirmar).
                alignment(Pos.CENTER).padding(new Insets(25)).build()));
                dialogStage.show();
                buttonConfirmar.setOnAction((ActionEvent e) -> {

                    new DeudaTipoDAO().delete(deudaTipo);
                    HibernateSessionFactory.getSession().flush();
                    dialogStage.close();
                    dataDeudas.remove(deudaTipo);

                    // Registro para auditar
                    String detalles = "elimino el tipo de deuda " 
                            + deudaTipo.getNombre();
                    aplicacionControl.au.saveElimino(detalles, aplicacionControl.permisos.getUsuario());

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
        
        createDepartamentoTable();
        createCargoTable();
        createDeudaTable();
        
        setDepartamentoTable();
        setCargoTable();
        setDeudaTable();
    }
    
    public void createDepartamentoTable () {
        
        departamentosTableView.setEditable(Boolean.FALSE);
        departamentosTableView.getColumns().clear();
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(200);
        nombre.setMaxWidth(200);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Departamento, Departamento> delete = new TableColumn<>("Borrar");
        delete.setMinWidth(55);
        delete.setMaxWidth(55);
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
    
    public void setDepartamentoTable() {
        
        DepartamentoDAO departamentoDAO = new DepartamentoDAO();
        dataDepartamentos = FXCollections.observableArrayList(); 
        dataDepartamentos.addAll(departamentoDAO.findAll());
        departamentosTableView.setItems(dataDepartamentos);
        
    }
    
    public void createCargoTable () {
        
        cargosTableView.setEditable(Boolean.FALSE);
        cargosTableView.getColumns().clear();
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(200);
        nombre.setMaxWidth(200);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn<Cargo, Cargo> delete = new TableColumn<>("Borrar");
        delete.setMinWidth(55);
        delete.setMaxWidth(55);
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
    
    public void setCargoTable() {
        
        CargoDAO cargoDAO = new CargoDAO();
        dataCargos = FXCollections.observableArrayList();
        dataCargos.addAll(cargoDAO.findAll());
        cargosTableView.setItems(dataCargos);
        
    }
    
    public void createDeudaTable () {
        
        deudasTableView.setEditable(Boolean.FALSE);
        deudasTableView.getColumns().clear();
        
        TableColumn nombre = new TableColumn("Nombre");
        nombre.setMinWidth(150);
        nombre.setMaxWidth(150);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        TableColumn cuotas = new TableColumn("Cuotas");
        cuotas.setMinWidth(50);
        cuotas.setMaxWidth(50);
        cuotas.setCellValueFactory(new PropertyValueFactory<>("cuotas"));
        
        TableColumn<DeudaTipo, DeudaTipo> delete = new TableColumn<>("Borrar");
        delete.setMinWidth(55);
        delete.setMaxWidth(55);
        delete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        delete.setCellFactory(param -> new TableCell<DeudaTipo, DeudaTipo>() {
            private final Button deleteButton = new Button("Borrar");

            @Override
            protected void updateItem(DeudaTipo deuda, boolean empty) {
                super.updateItem(deuda, empty);

                if (deuda == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    deleteDeuda(deuda);
                });
            }
        });
        
        deudasTableView.getColumns().addAll(nombre, cuotas, delete); 
    }
    
    public void setDeudaTable() {
        
        DeudaTipoDAO deudaTipoDAO = new DeudaTipoDAO();
        dataDeudas = FXCollections.observableArrayList();
        dataDeudas.addAll(deudaTipoDAO.findAll());
        deudasTableView.setItems(dataDeudas);
        
    }
    
    public static EventHandler<KeyEvent> numFilter() {

        EventHandler<KeyEvent> aux = (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
                
            }
        };
        return aux;
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
