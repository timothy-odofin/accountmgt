package iobank.org.accountmgt.mapper;

import iobank.org.accountmgt.model.request.CustomerRequest;
import iobank.org.accountmgt.model.response.CustomerResponse;

public class ModelMapper {
    public static CustomerResponse mapToCustomer(CustomerRequest mapFrom){
        if(mapFrom==null)
            return null;
        CustomerResponse mapTo = new CustomerResponse();
        mapTo.setName(mapFrom.getName());
        mapTo.setEmail(mapFrom.getEmail());
        mapTo.setPhone(mapTo.getPhone());
        mapTo.setContactAddress(mapTo.getContactAddress());
        return mapTo;
    }
}
