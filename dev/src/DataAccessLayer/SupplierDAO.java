package DataAccessLayer;

import DomainLayer.Supplier;

import java.util.Set;

public interface SupplierDAO {
    void saveSupplier(Supplier supplier);
    Set<Supplier> findAllSuppliers();
    Supplier findSupplierById(int supplierId);
}
