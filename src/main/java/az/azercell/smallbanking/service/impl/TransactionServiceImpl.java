package az.azercell.smallbanking.service.impl;

import az.azercell.smallbanking.exception.RefundExceedsPurchaseException;
import az.azercell.smallbanking.model.dto.TransactionDto;
import az.azercell.smallbanking.model.entity.Customer;
import az.azercell.smallbanking.model.entity.Transaction;
import az.azercell.smallbanking.model.enums.Status;
import az.azercell.smallbanking.model.enums.Type;
import az.azercell.smallbanking.repository.TransactionRepository;
import az.azercell.smallbanking.service.CustomerService;
import az.azercell.smallbanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final TransactionRepository transactionRepository;


    @Override
    public void createDeposit(TransactionDto tranDto) {
        createTransaction(tranDto, Type.DEPOSIT, customerService::depositBalance);
    }

    @Override
    public void createPurchase(TransactionDto tranDto) {
        createTransaction(tranDto, Type.PURCHASE, customerService::purchaseBalance);
    }

    private void createTransaction(TransactionDto tranDto, Type type, Function<TransactionDto, Customer> balanceUpdateFunction) {
        Customer customer = balanceUpdateFunction.apply(tranDto);

        Transaction transaction = modelMapper.map(tranDto, Transaction.class);
        transaction.setStatus(Status.COMPLETED);
        transaction.setType(type);
        transaction.setCustomer(customer);

        transactionRepository.save(transaction);
    }

    @Override
    public void refundTransaction(TransactionDto tranDto) {

        TransactionDto lastPurchaseTran = findLastPurchaseTransaction(tranDto.getCustomerId());

        checkRefundAmount(lastPurchaseTran.getAmount(), tranDto.getAmount());

        Customer customer = customerService.depositBalance(tranDto);
        Transaction transaction = modelMapper.map(tranDto, Transaction.class);

        transaction.setType(Type.REFUND);
        transaction.setStatus(Status.COMPLETED);
        transaction.setCustomer(customer);
        transaction.setRelatedTransactionId(lastPurchaseTran.getRelatedTransactionId());
        transactionRepository.save(transaction);


        Transaction clone = transaction.clone();
        clone.setType(Type.PURCHASE);
        clone.setStatus(Status.COMPLETED);
        clone.setAmount(lastPurchaseTran.getAmount() - tranDto.getAmount());
        transactionRepository.save(clone);

        transactionRepository.updateStatusById(lastPurchaseTran.getRelatedTransactionId(), Status.REJECT.name());

    }


    private TransactionDto findLastPurchaseTransaction(Long customerId) {
        Page<TransactionDto> lastPurchaseTransactionPage = transactionRepository
                .findLastPurchaseTransaction(customerId, PageRequest.of(0, 1));

        List<TransactionDto> transactions = lastPurchaseTransactionPage.getContent();

        checkTransactionExist(customerId, transactions);

        return transactions.get(0);
    }

    private static void checkTransactionExist(Long customerId, List<TransactionDto> transactions) {
        if (transactions.isEmpty()) {
            throw new EntityNotFoundException("No transactions found for customer with id: " + customerId);
        }
    }


    private void checkRefundAmount(Double lastPurchaseAmount, Double refundAmount) {
        if (refundAmount > lastPurchaseAmount) {
            throw new RefundExceedsPurchaseException("Refund amount of " + refundAmount + " exceeds the last purchase amount of " + lastPurchaseAmount);
        }
    }

}
