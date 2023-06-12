package ControllerLayer;

import DataAccessLayer.SupplierDAO;
import DomainLayer.Supplier;

public class SupplierControllerImpl implements SupplierController {
    private final SupplierDAO supplierDAO;


    public SupplierControllerImpl(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }
    @Override
    public Supplier findSupplierById(int supplierId) {
        return supplierDAO.findSupplierById(supplierId);
    }
}
