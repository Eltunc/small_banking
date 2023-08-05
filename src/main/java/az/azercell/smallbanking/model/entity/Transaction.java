package az.azercell.smallbanking.model.entity;


import az.azercell.smallbanking.model.enums.Status;
import az.azercell.smallbanking.model.enums.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Slf4j
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "transactions", schema = "small_banking")
public class Transaction implements Cloneable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Double amount;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Type type;

    Long relatedTransactionId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;

    @Override
    public Transaction clone() {

        Transaction clone = new Transaction();

        clone.setAmount(amount);
        clone.setType(type);
        clone.setRelatedTransactionId(relatedTransactionId);
        clone.setCustomer(customer);

        return clone;
    }
}
