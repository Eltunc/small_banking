package az.azercell.smallbanking.repository;

import az.azercell.smallbanking.model.dto.TransactionDto;
import az.azercell.smallbanking.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * " +
            "FROM small_banking.transactions " +
            "WHERE customer_id = :customerId " +
            "AND transaction_type = 'PURCHASE' " +
            "ORDER BY creation_date DESC " +
            "LIMIT 1", nativeQuery = true)
    Double getAmountLastPurchaseTran(Long customerId);

    @Query("SELECT new az.azercell.smallbanking.model.dto.TransactionDto(t.id,t.amount) " +
            "FROM Transaction t " +
            "WHERE t.customer.id = :customerId " +
            "AND t.type = 'PURCHASE' " +
            "AND t.status = 'COMPLETED' " +
            "ORDER BY t.creationDate DESC")
    Page<TransactionDto> findLastPurchaseTransaction(Long customerId, Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = "UPDATE small_banking.transactions " +
            "SET status = :status " +
            "WHERE id = :id", nativeQuery = true)
    void updateStatusById(Long id, String status);

}
