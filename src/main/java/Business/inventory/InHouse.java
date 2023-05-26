package Business.inventory;
/**
 *Contains Class, methods and data necessary to create an In House Part,
 * such as constructors, setters, and getters.
 *
 */
public class InHouse extends Part{


    /**
     * Constructor to create an In-House part
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineID
     */



    private int machineID;

    public InHouse(int partID, String name, double price, int stock, int min, int max, int machineID) {
        super(partID, name, price, stock, min, max);
        this.machineID = machineID;
    }


    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
