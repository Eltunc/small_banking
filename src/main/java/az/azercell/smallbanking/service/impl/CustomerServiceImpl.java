package az.azercell.smallbanking.service.impl;

import az.azercell.smallbanking.exception.InsufficientBalanceException;
import az.azercell.smallbanking.model.dto.CustomerDto;
import az.azercell.smallbanking.model.dto.TransactionDto;
import az.azercell.smallbanking.model.entity.Customer;
import az.azercell.smallbanking.model.enums.Type;
import az.azercell.smallbanking.repository.CustomerRepository;
import az.azercell.smallbanking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto create(CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }

    @Override
    public Customer depositBalance(TransactionDto tranDto) {
        Customer customer = getCustomerById(tranDto.getCustomerId());
        Double balance = customer.getBalance() + tranDto.getAmount();
        return updateCustomerBalance(customer, balance);
    }

    @Override
    public Customer purchaseBalance(TransactionDto tranDto) {
        Customer customer = getCustomerById(tranDto.getCustomerId());
        Double balance = balanceProcedure(customer.getBalance(), tranDto.getAmount());
        return updateCustomerBalance(customer, balance);
    }

    private Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find Customer with id: " + id));
    }

    private Customer updateCustomerBalance(Customer customer, Double newBalance) {
        customer.setBalance(newBalance);
        customerRepository.updateBalance(customer.getId(), newBalance);
        return customer;
    }

    private Double balanceProcedure(Double balance, Double amount) {
        if (balance < amount) {
            throw new InsufficientBalanceException();
        }
        return  balance - amount;
    }

}
