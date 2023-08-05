package az.azercell.smallbanking;

import az.azercell.smallbanking.model.dto.TransactionDto;
import az.azercell.smallbanking.model.entity.Customer;
import az.azercell.smallbanking.model.entity.Transaction;
import az.azercell.smallbanking.repository.TransactionRepository;
import az.azercell.smallbanking.service.CustomerService;
import az.azercell.smallbanking.service.TransactionService;
import az.azercell.smallbanking.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomerService customerService;

    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        transactionService = new TransactionServiceImpl(modelMapper, customerService,transactionRepository);
    }

    @Test
    void testCreateDeposit() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        Customer customer = new Customer();
        Transaction transaction = new Transaction();

        when(customerService.depositBalance(transactionDto)).thenReturn(customer);
        when(modelMapper.map(transactionDto, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        transactionService.createDeposit(transactionDto);

        // Assert
        verify(customerService, times(1)).depositBalance(transactionDto);
        verify(modelMapper, times(1)).map(transactionDto, Transaction.class);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testCreatePurchase() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        Customer customer = new Customer();
        Transaction transaction = new Transaction();

        when(customerService.purchaseBalance(transactionDto)).thenReturn(customer);
        when(modelMapper.map(transactionDto, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        // Act
        transactionService.createPurchase(transactionDto);

        // Assert
        verify(customerService, times(1)).purchaseBalance(transactionDto);
        verify(modelMapper, times(1)).map(transactionDto, Transaction.class);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testRefundTransaction() {
        // Arrange
        Long customerId = 1L;
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setCustomerId(customerId);
        transactionDto.setAmount(10.0);

        TransactionDto lastPurchaseTran = new TransactionDto();
        lastPurchaseTran.setAmount(20.0);

        Page<TransactionDto> page = new PageImpl<>(Collections.singletonList(lastPurchaseTran));

        Customer customer = new Customer();
        Transaction transaction = new Transaction();
        Transaction cloneTransaction = new Transaction();

        when(transactionRepository.findLastPurchaseTransaction(customerId, PageRequest.of(0, 1))).thenReturn(page);
        when(customerService.depositBalance(transactionDto)).thenReturn(customer);
        when(modelMapper.map(transactionDto, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(cloneTransaction);
        when(transactionRepository.save(cloneTransaction)).thenReturn(cloneTransaction);

        // Act
        transactionService.refundTransaction(transactionDto);

        // Assert
        verify(transactionRepository, times(1)).findLastPurchaseTransaction(customerId, PageRequest.of(0, 1));
        verify(customerService, times(1)).depositBalance(transactionDto);
        verify(modelMapper, times(1)).map(transactionDto, Transaction.class);
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

}
