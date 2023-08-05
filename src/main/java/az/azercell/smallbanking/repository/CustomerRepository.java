package az.azercell.smallbanking.repository;

import az.azercell.smallbanking.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE small_banking.customers " +
            "SET balance = :amount + COALESCE((SELECT t.balance FROM small_banking.customers t WHERE t.id = :id), 0) " +
            "WHERE id = :id", nativeQuery = true)
    void topUpMoney(Long id, Double amount);


    @Modifying
    @Transactional
    @Query(value = "UPDATE small_banking.customers " +
            "SET balance = :amount " +
            "WHERE id = :id", nativeQuery = true)
    void updateBalance(Long id, Double amount);


}
