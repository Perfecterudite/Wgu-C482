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

import static java.lang.Integer.parseInt;

/***
 * This Class contains the methods that modifies parts data existing in the table
 *
 *
 * @author Ayomide Adedeji
 */

public class ModifyPartController{


    @FXML private RadioButton InHouseModify;
    @FXML private  RadioButton OutSourcedModify;
    @FXML private  Button saveModifyPartBtn;
    @FXML private  Button cancelModifyPartBtn;
    @FXML private  TextField modifyPartName;
    @FXML private  TextField modifyPartInv;
    @FXML private TextField modifyPartPrice;
    @FXML private  TextField modifyPartMax;
    @FXML private  TextField modifyPartMin;
    @FXML private TextField modifyPartMachineID;
    @FXML private TextField IDModField;
    @FXML private  Label MachineOrComp;

    int selectedIndex;

    /**
     * On button press, set the text to Machine ID.
     * @param actionEvent
     */
    @FXML

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

        public void cancelModifyPart(ActionEvent actionEvent) throws IOException {
        Parent cancelModify= FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(cancelModify);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * on save button press
     * Save new changes made on existing parts data and takes user back to main screen
     * @param actionEvent
     */
   public void saveModifyPart(ActionEvent actionEvent) throws IOException {

        int id = Integer.parseInt(IDModField.getText());
        String name = modifyPartName.getText();
        int invt = parseInt(modifyPartInv.getText());
        double price = Double.parseDouble(modifyPartPrice.getText());
        int max = parseInt(modifyPartMax.getText());
        int min = parseInt(modifyPartMin.getText());


        try {

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min value cannot be greater than Max value.");
                alert.showAndWait();
            } else if (invt > max || invt < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory amount must be between minimum and maximum values.");
                alert.showAndWait();
            } else {

                if (InHouseModify.isSelected()) {

                    try{

                    int machineID = Integer.parseInt(modifyPartMachineID.getText());

                    InHouse InHousePart = new InHouse(id, name, price, invt, min, max, machineID);
                    Inventory.updatePart(selectedIndex, InHousePart);

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
                } else if (OutSourcedModify.isSelected()) {
                    try{
                        String companyName = modifyPartMachineID.getText();

                    OutSourced outsourcedPart = new OutSourced(id, name, price, invt, min, max, companyName);
                    Inventory.updatePart(selectedIndex, outsourcedPart);

                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        Object scene = FXMLLoader.load(getClass().getResource("main.fxml"));
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                    }
                    catch(NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setContentText("Enter number for Company Name");
                        alert.showAndWait();
                    }
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
    /**
     * I ran into a logical error which was updating the changes modified
     * only to the first index on the table thereby creating one modified data
     * and one unmodified date
     * I corrected that by making selectedIndex = Index
     * which made table update correctly on the selected part
     */
    public void setPart(int Index, Part part) {
        selectedIndex = Index;
        if (part instanceof InHouse) {

            InHouse newPart = (InHouse) part;
            InHouseModify.setSelected(true);
            OutSourcedModify.setSelected(false);
            MachineOrComp.setText("Machine ID");
            this.IDModField.setText(Integer.toString(newPart.getId()));
            this.modifyPartName.setText(newPart.getName());
            this.modifyPartInv.setText((Integer.toString(newPart.getStock())));
            this.modifyPartPrice.setText((Double.toString(newPart.getPrice())));
            this.modifyPartMin.setText((Integer.toString(newPart.getMin())));
            this.modifyPartMax.setText((Integer.toString(newPart.getMax())));
            this.modifyPartMachineID.setText((Integer.toString(newPart.getMachineID())));
            Inventory.updatePart(selectedIndex, newPart);

        }

        else if(part instanceof OutSourced){
            OutSourced newPartOs = (OutSourced) part;
            OutSourcedModify.setSelected(true);
            InHouseModify.setSelected(false);
            MachineOrComp.setText("Company Name");
            this.IDModField.setText(Integer.toString(newPartOs.getId()));
            this.modifyPartName.setText(newPartOs.getName());
            this.modifyPartInv.setText((Integer.toString(newPartOs.getStock())));
            this.modifyPartPrice.setText((Double.toString(newPartOs.getPrice())));
            this.modifyPartMin.setText((Integer.toString(newPartOs.getMin())));
            this.modifyPartMax.setText((Integer.toString(newPartOs.getMax())));
            this.modifyPartMachineID.setText(newPartOs.getCompanyName());
            Inventory.updatePart(selectedIndex, newPartOs);
        }
    }


}
