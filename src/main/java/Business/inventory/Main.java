package Business.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 *
 * @author
 * Ayomide Adedeji
 * aaded14@wgu.edu
 * Student ID: 007440467
 *
 *
 * The Inventory Management program implements an application for managing
 * an inventory of parts and products associated with the parts for a
 * small manufacturing organization.
 *
 *
 * FUTURE ENHANCEMENT: A feature suitable for a future enhancement version would be that
 * only admin could delete parts.
 * Other users can only add and modify parts or products. It should also ensure there are no
 * duplicate machine ID
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 500);
        stage.setTitle("Inventory");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //This is to load part and product data on the table as the application launches

        Part brakes = new InHouse(1, "Brakes", 10.00, 10, 4, 32, 1);
        Inventory.addPart(brakes);

        Part wheel = new InHouse(2, "Wheel", 15.00, 16, 10, 30, 4);
        Inventory.addPart(wheel);

        Part seat = new InHouse(3, "Seat", 40.00, 11, 0, 120, 5);
        Inventory.addPart(seat);

        Part spoke = new OutSourced(4, "Spoke", 25.00, 10, 0, 120, "Bob's bikes");
        Inventory.addPart(spoke);

        Part gear = new OutSourced(5, "Gear", 14.00, 5, 0, 20, "Sam's Autos");
        Inventory.addPart(gear);

        Product GiantBike = new Product(1000, "Giant Bike", 1299.99, 3, 1, 50);
        Inventory.addProduct(GiantBike);

        Product Tricycle = new Product(1001, "Tricycle", 197.49, 5, 1, 50);
        Inventory.addProduct(Tricycle);

        Product ThirdWheel = new Product(1002, "ThirdWheel", 99.99, 8, 1, 100);
        Inventory.addProduct(ThirdWheel);

        Product FanBand = new Product(1003, "FanBand", 80.05, 2, 1, 100);
        Inventory.addProduct(FanBand);

        launch();
    }


}