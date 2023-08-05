package az.azercell.smallbanking.service;

import az.azercell.smallbanking.model.dto.TransactionDto;

public interface TransactionService {

    void createDeposit(TransactionDto transactionDto);

    void createPurchase(TransactionDto transactionDto);

    void refundTransaction(TransactionDto transactionDto);

}
