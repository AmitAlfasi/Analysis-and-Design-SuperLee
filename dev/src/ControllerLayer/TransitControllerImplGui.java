package ControllerLayer;

import BussinesLogic.TransitCoordinator;
import DataAccessLayer.TransitDAO;

public class TransitControllerImplGui extends TransitControllerImpl{

    public TransitControllerImplGui(TransitDAO transitDAO, TruckController truckController,
                                    TransitCoordinator transitCoordinator,
                                    OrderDocumentController orderDocController,
                                    TransitRecordController transitRecordController) {
        super(transitDAO, truckController, transitCoordinator, orderDocController, transitRecordController);
    }


}
