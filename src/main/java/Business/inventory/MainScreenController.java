package Business.inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.Optional;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class MainScreenController implements Initializable {

    @FXML private TableView<Part> mainPartTable;
    @FXML private  TableColumn<Part, Integer> partIDCol;
    @FXML private  TableColumn<Part, String> partNameCol;
    @FXML private TableColumn<Part, Integer> partInventCol;
    @FXML private  TableColumn<Part, Double> partPriceCol;

    @FXML private TableView<Product> mainProductTable;
    @FXML private  TableColumn<Product, Integer> productIDCol;
    @FXML private  TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Integer> productInventCol;
    @FXML private  TableColumn<Product, Double> productPriceCol;

    @FXML private TextField productSearch;
    @FXML private TextField partSearch;



    @FXML
    public void onExitButtonClick(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void onAddPartClick(ActionEvent actionEvent) throws IOException {
        Parent addParts = FXMLLoader.load(getClass().getResource("addPart.fxml"));
        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    /**
     * On button press, save the part.
     * RUNTIME ERROR: Caused by: java.lang.NullPointerException: because this.mainPartTable was null
     * I corrected this runtime error by assigning the mainPartTable
     * to the appropriate fx:id in the addPart fxml file.
     *
     * @param actionEvent
     */

    public void onModifyPartClick(ActionEvent actionEvent) throws IOException {

        //try{

            Part part=mainPartTable.getSelectionModel().getSelectedItem();
            int index = mainPartTable.getSelectionModel().getSelectedIndex();

                if(part != null) {


                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("modifyPart.fxml"));
                    Parent scene = loader.load();


                    ModifyPartController MPControl = loader.getController();
                    MPControl.setPart(index, part);

                    Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Select a part first");
                    alert.show();
                }
    }

    public void onDeletePartClick(ActionEvent actionEvent) {
        Part selectedPart = mainPartTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }

    public void onAddProductClick(ActionEvent actionEvent) throws IOException{
        Parent addProducts = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        Scene scene = new Scene(addProducts);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void onModifyProductClick(ActionEvent actionEvent) throws IOException{

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyProduct.fxml"));
            //loader.load();
            Parent scene = loader.load();

            ModifyProductController MPControl = loader.getController();
            MPControl.sendProduct(mainProductTable.getSelectionModel().getSelectedIndex(),
                    mainProductTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select a product to modify");
            alert.show();
        }

    }

    public void onDeleteProductClick(ActionEvent actionEvent) {
        Product selectedProduct = mainProductTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Product selectedDeleteProduct = mainProductTable.getSelectionModel().getSelectedItem();
            if (selectedDeleteProduct.getAllAssociatedParts().size() > 0) {
                Alert cantDelete = new Alert(Alert.AlertType.ERROR);
                cantDelete.setTitle("Error Message");
                cantDelete.setContentText("Remove associated parts before you delete the product.");
                cantDelete.showAndWait();
                return;
            }
            Inventory.deleteProduct(selectedProduct);
        }
    }

    /**
     * Initializing and populating tables
     *
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPartTable.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainProductTable.setItems(Inventory.getAllProducts());
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    //Search the Part Table
    Part foundParts;
    ObservableList parts;
    ObservableList products;
    Product foundProducts;

    @FXML
    public void mainPartSearch(){
        String searchedPart = partSearch.getText();


        if(searchedPart.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Part Search Warning");
            alert.setHeaderText("There were no parts found! Case Sensitive!");
            alert.setContentText("You did not enter a part ID or name to search for!");
            alert.showAndWait();
            mainPartTable.setItems(Inventory.getAllParts());
        } else {
            boolean found = false;
            try {
                foundParts = Inventory.lookupPart(Integer.parseInt(searchedPart));
                if (foundParts != null) {
                    ObservableList<Part> parts = FXCollections.observableArrayList();
                    parts.add(foundParts);
                    mainPartTable.setItems(parts);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found! Case Sensitive!");
                    alert.setContentText("The search term entered does not match any part ID!");
                    alert.showAndWait();
                    mainPartTable.setItems(Inventory.getAllParts());
                }
            } catch (NumberFormatException e) {
                ObservableList<Part> allParts = Inventory.getAllParts();
                if(allParts.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found! Case Sensitive!");
                    alert.setContentText("There are no parts in the parts list to search\nAdd parts first.");
                    alert.showAndWait();
                    mainPartTable.setItems(Inventory.getAllParts());

                } else {
                    for (int i = 0, allPartsSize = allParts.size(); i < allPartsSize; i++) {
                        Part p = allParts.get(i);
                        if (p.getName().equals(searchedPart)) {
                            found = true;
                            parts = Inventory.lookupPart(searchedPart);
                            mainPartTable.setItems(parts);
                        }
                    }   if (found == false) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Part Search Warning");
                        alert.setHeaderText("There were no parts found! Case Sensitive!");
                        alert.setContentText("The search term entered does not match any part name! Case Sensitive!");
                        alert.showAndWait();
                        mainPartTable.setItems(Inventory.getAllParts());
                    }
                }
            }
        }



        partSearch.setText("");

    }

    //Search the Product Table
    @FXML
    public void mainProductSearch() {

        String searchedProduct = productSearch.getText();

        if (searchedProduct.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Product Search Warning");
            alert.setHeaderText("There were no products found! Case Sensitive!");
            alert.setContentText("You did not enter a Product ID or Name to search for!");
            alert.showAndWait();
            mainProductTable.setItems(Inventory.getAllProducts());
        } else {
            boolean found = false;
            try {
                foundProducts = Inventory.lookupProduct(Integer.parseInt(searchedProduct));
                if (foundProducts != null) {
                    ObservableList<Product> products = FXCollections.observableArrayList();
                    products.add(foundProducts);
                    mainProductTable.setItems(products);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no Products found! Case Sensitive!");
                    alert.setContentText("The search term entered does not match any Product ID!");
                    alert.showAndWait();
                    mainProductTable.setItems(Inventory.getAllProducts());
                }
            } catch (NumberFormatException e) {
                ObservableList<Product> allProducts = Inventory.getAllProducts();
                if (allProducts.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no Products found! Case Sensitive!");
                    alert.setContentText("There are no Products in the list of Products to search\nAdd Products first.");
                    alert.showAndWait();
                    mainProductTable.setItems(Inventory.getAllProducts());

                } else {
                    for (int i = 0, allProductsSize = allProducts.size(); i < allProductsSize; i++) {
                        Product p = allProducts.get(i);
                        if (p.getName().equals(searchedProduct)) {
                            found = true;
                            products = Inventory.lookupProduct(searchedProduct);
                            mainProductTable.setItems(products);
                        }
                    }
                    if (found == false) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Part Search Warning");
                        alert.setHeaderText("There were no Products found! Case Sensitive!");
                        alert.setContentText("The search term entered does not match any Product name! Case Sensitive!");
                        alert.showAndWait();
                        mainProductTable.setItems(Inventory.getAllProducts());
                    }

                }
            }
        }
        productSearch.setText("");
    }
}