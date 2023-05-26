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
 * This Class contains the methods that adds products data to table
 * adds associated parts related to product to table
 * also removes associated parts related with product
 *
 * @author Ayomide Adedeji
 */

public class AddProductController implements Initializable {

    @FXML
    private TextField addProductID;
    @FXML
    private TextField addProductName;
    @FXML
    private TextField addProductInv;
    @FXML
    private TextField addProductPrice;
    @FXML
    private TextField addProductMax;
    @FXML
    private TextField addProductMin;

    @FXML
    private TextField addProdSearchField;
    @FXML
    private TableView<Part> addProductPartTable;
    @FXML
    private TableColumn<Part, Integer> partIDCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInventCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TableView<Part> addProductAssocPartTable;
    @FXML
    private TableColumn<Part, Integer> partID1;
    @FXML
    private TableColumn<Part, String> partName1;
    @FXML
    private TableColumn<Part, Integer> partInvent1;
    @FXML
    private TableColumn<Part, Double> price1;
    @FXML
    private Button addProduct;

    @FXML
    private Button saveAddProduct;

    @FXML
    private Button cancelProduct;

    @FXML
    private Button removeAssocPart;



    // This observablelist holds the associatedPartsList.
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();


    /**
     *  Initialize and populate the table with products and associated parts.
     * @param resourceBundle
     * @param url
     */
    //@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Setup partTableView and columns
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductPartTable.setItems(Inventory.getAllParts());

        //Setup associatedPartTableView colums
        partID1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvent1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        price1.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Receive information from the main screen parts table.
     * Add the part to the observablelist.
     * Thereby creating an associated part with product
     * @param actionEvent
     */
    public void onAddProductClick(ActionEvent actionEvent) {
        Part selectedPart = addProductPartTable.getSelectionModel().getSelectedItem();

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
            addProductAssocPartTable.setItems(associatedPartsList);
        }

    }

    /**
     * Remove the part to the observablelist.
     * Gives a prompt if user still wants to proceed with removing associated part
     * @param actionEvent
     */
    public void onRemoveClick(ActionEvent actionEvent) {
        Part selectedPart = addProductAssocPartTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Part not selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to remove this part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
        }

    }


    /**
     * Cancel Product Add
     * On button press, takes users back to main screen.
     * @param actionEvent
     */
    public void onCancelAddProductClick(ActionEvent actionEvent) throws IOException {
        Parent cancelAddProduct = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(cancelAddProduct);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    /**
     * Save new product and takes user back to main screen
     * @param actionEvent
     */
    public void onSaveProductClick(ActionEvent actionEvent) throws IOException {

        int Id = 0;
        for(Product prod : Inventory.getAllProducts()) {

            if(prod.getId() > Id)

                Id = prod.getId();

        }

        addProductID.setText(String.valueOf(++Id));
        String name = addProductName.getText();
        int invt = parseInt(addProductInv.getText());
        double price = Double.parseDouble(addProductPrice.getText());
        int max = parseInt(addProductMax.getText());
        int min = parseInt(addProductMin.getText());

        try {

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min value cannot be greater than Max value.");
                alert.showAndWait();
            } else if (invt > max || invt < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory amount must be between minimum and maximum values.");
                alert.showAndWait();
            } else {

            }
            Product product = new Product(Id, name, price, invt, min, max);

            for (Part part : associatedPartsList) {
                if (part != associatedPartsList)
                    product.addAssociatedParts(part);
            }

            Inventory.getAllProducts().add(product);

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();

        }
    }

    /**
     * After user presses enter
     * Search for parts to add to the associated parts table.
     * @param actionEvent
     */
    @FXML
    public void prodSearchField(ActionEvent actionEvent) {

        String searchedPart = addProdSearchField.getText();


        if(searchedPart.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Search Warning");
            alert.setHeaderText("There were no parts found! Case Sensitive!");
            alert.setContentText("You did not enter a part ID or name to search for!");
            alert.showAndWait();
            addProductPartTable.setItems(Inventory.getAllParts());
        } else {
            boolean found = false;
            try {
                Part foundParts = Inventory.lookupPart(Integer.parseInt(searchedPart));
                if (foundParts != null) {
                    ObservableList<Part> parts = FXCollections.observableArrayList();
                    parts.add(foundParts);
                    addProductPartTable.setItems(parts);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found!");
                    alert.setContentText("The search term entered does not match any part ID!");
                    alert.showAndWait();
                    addProductPartTable.setItems(Inventory.getAllParts());
                }
            } catch (NumberFormatException e) {
                ObservableList<Part> allParts = Inventory.getAllParts();
                if(allParts.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found! Case Sensitive!");
                    alert.setContentText("There are no parts in the parts list to search\nAdd parts first.");
                    alert.showAndWait();
                    addProductPartTable.setItems(Inventory.getAllParts());

                } else {
                    for (int i = 0, allPartsSize = allParts.size(); i < allPartsSize; i++) {
                        Part p = allParts.get(i);
                        if (p.getName().equals(searchedPart)) {
                            found = true;
                            ObservableList parts = Inventory.lookupPart(searchedPart);
                            addProductPartTable.setItems(parts);
                        }
                    }   if (found == false) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Part Search Warning");
                        alert.setHeaderText("There were no parts found! Case Sensitive!");
                        alert.setContentText("The search term entered does not match any part name!");
                        alert.showAndWait();
                        addProductPartTable.setItems(Inventory.getAllParts());
                    }
                }
            }
        }

        addProdSearchField.setText("");

    }
}
