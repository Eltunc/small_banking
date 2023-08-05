package az.azercell.smallbanking.service;

import az.azercell.smallbanking.model.dto.CustomerDto;
import az.azercell.smallbanking.model.dto.TransactionDto;
import az.azercell.smallbanking.model.entity.Customer;

public interface CustomerService {
    CustomerDto create(CustomerDto customerDto);

    Customer depositBalance(TransactionDto transactionDto);

    Customer purchaseBalance(TransactionDto transactionDto);


}
