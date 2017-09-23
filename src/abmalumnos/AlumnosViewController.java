package abmalumnos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author Pablo Acevedo
 */
public class AlumnosViewController implements Initializable {
    @FXML
    private TableView<Alumno> tablaAlumnos;
    @FXML
    private TableColumn<Alumno, Integer> colId;
    @FXML
    private TableColumn<Alumno, String> colNombre;
    @FXML
    private TableColumn<Alumno, Integer> colDni;
    @FXML
    private TableColumn<Alumno, String> colMail;
    @FXML
    private Label labelId;
    @FXML
    private TextField textId;
    @FXML
    private TextField textNombre;
    @FXML
    private TextField textDni;
    @FXML
    private TextField textMail;
    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonCancelar;
    @FXML
    private Label mensaje;
    @FXML
    private Button botonCrear;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelDni;
    @FXML
    private Label labelMail;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tablaAlumnos.setItems(Alumno.getAlumnos());
        colNombre.setCellValueFactory(p-> p.getValue().getNombre());
        colMail.setCellValueFactory(p-> p.getValue().getMail());
        colDni.setCellValueFactory(p -> p.getValue().getDni().asObject());
        colId.setCellValueFactory(p -> p.getValue().getId().asObject());

        tablaAlumnos.getSelectionModel().selectedIndexProperty()
                .addListener((obs, orig, nuevo)-> 
                    mostrarDetallesAlumno(tablaAlumnos.getSelectionModel().getSelectedItem()));
        mostrar(false);
        
        MenuItem mi1 = new MenuItem("Eliminar alumno");
        mi1.setOnAction(e -> {
            tablaAlumnos.getSelectionModel().getSelectedItem().eliminar();
            mostrar(false);
            tablaAlumnos.setItems(Alumno.getAlumnos()); 
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        tablaAlumnos.setContextMenu(menu);

    }
    @FXML
    private void guardar(ActionEvent event) {
        try{
            Alumno alumno = new Alumno(
                Integer.valueOf(textId.getText()),
                textNombre.getText(),
                Integer.valueOf(textDni.getText()),
                textMail.getText()
            );
            if(alumno.guardar()){
                cancelar(event); //limpio formulario y borro selección
                mensaje.setText("Datos guardados correctamente!");
                mensaje.setStyle("-fx-text-fill:green");
            }else{
                mensaje.setText("Error al grabar. Vuelva a intentarlo.");
                mensaje.setStyle("-fx-text-fill:red");
            }
            tablaAlumnos.setItems(Alumno.getAlumnos());
        }catch(NumberFormatException e){
            mensaje.setText("Error en los datos, revise el formulario.");
            mensaje.setStyle("-fx-text-fill:red");        
        }finally{
            mensaje.setVisible(true);
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        tablaAlumnos.getSelectionModel().clearSelection();
        mostrar(false);
    }
    @FXML
    private void nuevo(ActionEvent event) {
        tablaAlumnos.getSelectionModel().clearSelection();
        labelId.setText("Nuevo alumno");
        mostrar(true);
    }
    private void mostrar(boolean valor){
        mensaje.setVisible(false);
        textNombre.setText("");
        textDni.setText("");
        textMail.setText("");
        textId.setText("0");
        textNombre.setVisible(valor);
        textDni.setVisible(valor);
        textMail.setVisible(valor);
        labelNombre.setVisible(valor);
        labelDni.setVisible(valor);
        labelMail.setVisible(valor);
        labelId.setVisible(valor);
        botonCrear.setDisable(valor);
        botonGuardar.setDisable(!valor);
        botonCancelar.setDisable(!valor);
    }

    private void mostrarDetallesAlumno(Alumno alumno) {
        if(alumno != null){
            mostrar(true);
            labelId.setText("Alumno "+alumno.getId().get());
            textId.setText(String.valueOf(alumno.getId().get()));
            textNombre.setText(alumno.getNombre().get());
            textDni.setText(String.valueOf(alumno.getDni().get()));
            textMail.setText(alumno.getMail().get());
        }
    }
}
