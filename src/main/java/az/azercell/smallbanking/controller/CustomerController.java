package az.azercell.smallbanking.controller;

import az.azercell.smallbanking.model.dto.CustomerDto;
import az.azercell.smallbanking.model.dto.TransactionDto;
import az.azercell.smallbanking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.create(customerDto));
    }

}
