package DataAccessLayer;

import DomainLayer.Area;
import DomainLayer.Supplier;

import java.util.HashSet;
import java.util.Set;

public class SupplierDAOImpl implements SupplierDAO {
    private final Set<Supplier> suppliersSet = new HashSet<>();

    public SupplierDAOImpl() {
        Supplier sup1 = new Supplier("Jerusalem", Area.Center, "David", "0523333333", 1);
        Supplier sup2 = new Supplier("Hiafa", Area.North, "Shlomi", "0524444444", 2);
        Supplier sup3 = new Supplier("TelAviv", Area.Center, "Tomer", "0525555555", 3);
        Supplier parkSup = new Supplier("Logistical Warehouse", Area.Center, "Amitai", "031000778", 0);
        suppliersSet.add(sup1);
        suppliersSet.add(sup2);
        suppliersSet.add(sup3);
        suppliersSet.add(parkSup);
    }

    @Override
    public void saveSupplier(Supplier supplier){
        suppliersSet.add(supplier);}
    @Override
    public Set<Supplier> findAllSuppliers(){return suppliersSet;}
    @Override
    public Supplier findSupplierById(int supplierId){
        for (Supplier supplier : this.findAllSuppliers()) {
            if (supplier.getSupplierId() == supplierId) {
                return supplier;
            }
        }
        return null;
    }
}