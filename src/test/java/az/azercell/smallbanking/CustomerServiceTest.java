package az.azercell.smallbanking;

import static org.mockito.Mockito.*;

import az.azercell.smallbanking.model.dto.CustomerDto;
import az.azercell.smallbanking.model.entity.Customer;
import az.azercell.smallbanking.repository.CustomerRepository;
import az.azercell.smallbanking.service.CustomerService;
import az.azercell.smallbanking.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void testCreate() {
        // Arrange
        CustomerDto customerDto = new CustomerDto();
        Customer customer = new Customer();
        Customer savedCustomer = new Customer();

        when(modelMapper.map(customerDto, Customer.class)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(modelMapper.map(savedCustomer, CustomerDto.class)).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.create(customerDto);

        // Assert
        assertEquals(customerDto, result);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(modelMapper, times(1)).map(customerDto, Customer.class);
        verify(modelMapper, times(1)).map(savedCustomer, CustomerDto.class);
    }
}
