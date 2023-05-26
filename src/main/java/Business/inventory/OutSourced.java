package Business.inventory;

/**
 *Contains Class, methods and data necessary to create an OutSourced Part,
 * such as constructors, setters, and getters.
 *
 */

public class OutSourced extends Part{


    /**
     * Constructor to create an In-House part
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    private String companyName;

    public OutSourced(int partID, String name, double price, int inStock, int min, int max, String companyName) {
        super(partID, name, price, inStock, min, max);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
