package Business.inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/***
 * This Class contains the methods that adds parts data to table
 *
 * @author Ayomide Adedeji
 */

public class AddPartController {



    @FXML private RadioButton InHousePart;
    @FXML private  RadioButton OutSourcedPart;
    @FXML private  Button saveAddPartBtn;
    @FXML private Button cancelAddPartBtn;
    @FXML private  TextField addPartID;
    @FXML private  TextField addPartName;
    @FXML private  TextField addPartInv;
    @FXML private  TextField addPartPrice;
    @FXML private  TextField addPartMax;
    @FXML private  TextField addPartMin;
    @FXML private  TextField addPartMachineID;
    @FXML private  Label MachineOrComp;


    /**
     * On button press, set the text to Machine ID.
     * @param actionEvent
     */

    public void onInHouseClick (ActionEvent actionEvent){
        MachineOrComp.setText("Machine ID");
    }

    /**
     * On button press, set the text to Company Name.
     * @param actionEvent
     */
    public void onOutSourcedClick (ActionEvent actionEvent){
        MachineOrComp.setText("Company Name");
    }

    /**
     * Cancel Part Add
     * On button press, takes users back to main screen.
     * @param actionEvent
     */

    public void cancelAddPart(ActionEvent actionEvent) throws IOException{
        Parent cancelAdd = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(cancelAdd);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This function generates ID number, allow data input to textfields
     *
     * On button press, save the part.
     * RUNTIME ERROR: Caused by: java.lang.NumberFormatException: For input string
     * I corrected this runtime error using the try and catch method
     * to ensure that each integer values can be entered in the textfield
     *
     * @param actionEvent
     */

    public void saveAddPart(ActionEvent actionEvent) throws IOException{

        try{
            int ID = 0;
            for (Part part : Inventory.getAllParts()) {

                if (part.getId() > ID)

                    ID = part.getId();

            }

            addPartID.setText(String.valueOf(++ID));
            String name = addPartName.getText();
            int invt = Integer.parseInt(addPartInv.getText());
            double price = Double.parseDouble(addPartPrice.getText());
            int max = Integer.parseInt(addPartMax.getText());
            int min = Integer.parseInt(addPartMin.getText());




            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min value cannot be greater than Max value.");
                alert.showAndWait();
            } else if (invt > max || invt < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory amount must be between minimum and maximum values.");
                alert.showAndWait();
            }

            else {
                if (InHousePart.isSelected()) {
                    try{

                        int machineID = Integer.parseInt(addPartMachineID.getText());
                    InHouse addPart = new InHouse(ID, name, price, invt, min, max, machineID);

                    Inventory.addPart(addPart);


                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Object scene = FXMLLoader.load(getClass().getResource("main.fxml"));
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                    }
                    catch(NumberFormatException e){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setContentText("Enter number for machine ID");
                        alert.showAndWait();

                    }
                }


                else if (OutSourcedPart.isSelected()) {

                    String companyName = addPartMachineID.getText();

                        OutSourced addName = new OutSourced(ID, name, price, invt, min, max, companyName);

                        Inventory.addPart(addName);


                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        Object scene = FXMLLoader.load(getClass().getResource("main.fxml"));
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();

                    }

                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Select in-house or outsourced");
                    alert.showAndWait();
                }
            }
        }

        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();

        }
    }
}
