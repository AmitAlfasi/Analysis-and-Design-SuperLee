package DomainLayer;

public class Supplier extends Site{
    private final int supplierID;


    public Supplier(String address, Area areaCode, String contactName, String contactNumber, int supplierID) {
        super(address, areaCode, contactName, contactNumber);
        this.supplierID = supplierID;

    }
    public int getSupplierId() {
        return supplierID;
    }
    public void printSupplier(){
        System.out.print("Supplier id: " + supplierID);
    }
    public Area getSupplierArea(){
        return this.areaCode;
    }
}
