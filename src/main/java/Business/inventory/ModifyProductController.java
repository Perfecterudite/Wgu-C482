package Business.inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.IOException;

import static java.lang.Integer.parseInt;

/***
 * This Class contains the methods that modifies product data existing in the table
 * changes added associated parts related to product to table
 * also makes changes in removing associated parts related with product
 *
 * @author Ayomide Adedeji
 */


public class ModifyProductController implements Initializable{

    // This observablelist holds the associatedPartsList.

    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();

    @FXML private TextField modProdSearch;
    @FXML private TextField ProdPartID;
    @FXML private TextField ProdPartName;
    @FXML private TextField ProdPartInv;
    @FXML private TextField ProdPrice;
    @FXML private TextField ProdPartMax;
    @FXML private TextField ProdPartMin;
    @FXML private TableView<Part> mainProdTable;
    @FXML private TableColumn<?, ?> modProdPartID;
    @FXML private TableColumn<?, ?> modProdPartName;
    @FXML private TableColumn<?, ?> modProdPartInv;
    @FXML private TableColumn<?, ?> modProdPartPrice;
    @FXML private TableView<Part> assoModTable;
    @FXML private TableColumn<?, ?> modPartIDAsso;
    @FXML private TableColumn<?, ?> modPartNameAsso;
    @FXML private TableColumn<?, ?> modPartInvAsso;
    @FXML private TableColumn<?, ?> modPartPriceAsso;
    @FXML private Button cancelMod;
    @FXML private Button saveProd;
    @FXML private Button removeAssoBtn;
    @FXML private Button modProdAdd;

    private int currIndex = 0;

    /**
     * Cancel Product Add
     * On button press, takes users back to main screen.
     * @param actionEvent
     */

    @FXML
    public void modProdCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);
        Stage MainScreenReturn = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        MainScreenReturn.setScene(scene);
        MainScreenReturn.show();
    }

    /**
     * Receive information from the main screen parts table.
     * @param selectedIndex
     * @param product
     */

    @FXML
    public void sendProduct(int selectedIndex, Product product){

        currIndex = selectedIndex;
        ProdPartID.setText(String.valueOf(product.getId()));
        ProdPartName.setText(String.valueOf(product.getName()));
        ProdPartInv.setText(String.valueOf(product.getStock()));
        ProdPrice.setText(String.valueOf(product.getPrice()));
        ProdPartMax.setText(String.valueOf(product.getMax()));
        ProdPartMin.setText(String.valueOf(product.getMin()));

        for (Part part: product.getAllAssociatedParts()) {
            associatedPartsList.add(part);
        }
    }

    /**
     *  Initialize and populate the table with products and associated parts.
     * @param resourceBundle
     * @param url
     */

    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainProdTable.setItems(Inventory.getAllParts());
        modProdPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //add parts to associated table (bottom)

        assoModTable.setItems(associatedPartsList);
        modPartIDAsso.setCellValueFactory(new PropertyValueFactory<>("id"));
        modPartNameAsso.setCellValueFactory(new PropertyValueFactory<>("name"));
        modPartInvAsso.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modPartPriceAsso.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Modifies added selected part to the associated parts table.
     * @param actionEvent
     */
    @FXML
    public void addProdClick(ActionEvent actionEvent) {
        Part selectedPart = mainProdTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Select part from list");
            alert.showAndWait();
            return;
        }
        else if (!associatedPartsList.contains(selectedPart))
        {
            associatedPartsList.add(selectedPart);
            assoModTable.setItems(associatedPartsList);
        }
    }

    /**
     * Remove the part to the observablelist.
     * Gives a prompt if user still wants to proceed with removing associated part
     * @param actionEvent
     */
    @FXML
    public void removeAssoClick(ActionEvent actionEvent) {
        Part selectedPart = assoModTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Select part from list");
            alert.showAndWait();
            return;
        }
        else if (associatedPartsList.contains(selectedPart))
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to remove this part?");
            Optional<ButtonType> result = alert.showAndWait();


            Product.deleteAssociatedPart(selectedPart);
            associatedPartsList.remove(selectedPart);
            assoModTable.setItems(associatedPartsList);
        }
    }

    /**
     * Save new changes made on product and takes user back to main screen
     * @param actionEvent
     */

    @FXML
    public void modProdSave(ActionEvent actionEvent) throws IOException {
        try {
            int id = Integer.parseInt(ProdPartID.getText());
            String name = ProdPartName.getText();
            int stock = Integer.parseInt(ProdPartInv.getText());
            double price = Double.parseDouble(ProdPrice.getText());
            int max = Integer.parseInt(ProdPartMax.getText());
            int min = Integer.parseInt(ProdPartMin.getText());

            if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory requirements: Inventory must be within min and max.");
                alert.showAndWait();
                return;
            } else if (min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory requirements: maximum must be greater than minimum");
                alert.showAndWait();
                return;
            }
            Product updatedProduct = new Product(id, name, price, stock, min, max);
            if (updatedProduct != associatedPartsList) {
                Inventory.updateProduct(currIndex, updatedProduct);
            }


            for (Part part: associatedPartsList) {
                if (part != associatedPartsList)
                    updatedProduct.addAssociatedParts(part);
            }

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Incorrect value, enter valid value");
            alert.showAndWait();
        }
    }

    /**
     * After user presses enter
     * Search for parts to add to the associated parts table.
     * @param actionEvent
     */
    @FXML
    public void modProdSearchClick(ActionEvent actionEvent) {

        String searchedPart = modProdSearch.getText();


        if(searchedPart.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Search Warning");
            alert.setHeaderText("There were no parts found! Case Sensitive!");
            alert.setContentText("You did not enter a part ID or name to search for!");
            alert.showAndWait();
            mainProdTable.setItems(Inventory.getAllParts());
        } else {
            boolean found = false;
            try {
                Part foundParts = Inventory.lookupPart(Integer.parseInt(searchedPart));
                if (foundParts != null) {
                    ObservableList<Part> parts = FXCollections.observableArrayList();
                    parts.add(foundParts);
                    mainProdTable.setItems(parts);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found!");
                    alert.setContentText("The search term entered does not match any part ID! Case Sensitive!");
                    alert.showAndWait();
                    mainProdTable.setItems(Inventory.getAllParts());
                }
            } catch (NumberFormatException e) {
                ObservableList<Part> allParts = Inventory.getAllParts();
                if(allParts.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found! Case Sensitive!");
                    alert.setContentText("There are no parts in the parts list to search\nAdd parts first.");
                    alert.showAndWait();
                    mainProdTable.setItems(Inventory.getAllParts());

                } else {
                    for (int i = 0, allPartsSize = allParts.size(); i < allPartsSize; i++) {
                        Part p = allParts.get(i);
                        if (p.getName().equals(searchedPart)) {
                            found = true;
                            ObservableList parts = Inventory.lookupPart(searchedPart);
                            mainProdTable.setItems(parts);
                        }
                    }   if (found == false) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Part Search Warning");
                        alert.setHeaderText("There were no parts found! Case Sensitive!");
                        alert.setContentText("The search term entered does not match any part name!");
                        alert.showAndWait();
                        mainProdTable.setItems(Inventory.getAllParts());
                    }
                }
            }
        }

        modProdSearch.setText("");
    }

}
