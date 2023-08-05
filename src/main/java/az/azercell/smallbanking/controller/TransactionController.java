package az.azercell.smallbanking.controller;

import az.azercell.smallbanking.model.dto.TransactionDto;
import az.azercell.smallbanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("transactions/balance")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("deposit")
    public void createDeposit(@RequestBody TransactionDto transactionDto){
        transactionService.createDeposit(transactionDto);
    }

    @PostMapping("purchase")
    public void createPurchase(@RequestBody TransactionDto transactionDto){
        transactionService.createPurchase(transactionDto);
    }

    @PostMapping("reversals")
    public void refundTransaction(@RequestBody TransactionDto transactionDto) {
        transactionService.refundTransaction(transactionDto);
    }

}
