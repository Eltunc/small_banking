package az.azercell.smallbanking.model.entity;

import az.azercell.smallbanking.validation.ValidGsmNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "customers", schema = "small_banking")
public class Customer {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Please enter Name to User")
    String name;

    @NotBlank(message = "Please enter Surname to User")
    String surname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;

    @ValidGsmNumber
    @Column(name = "GSM_NUMBER", unique = true)
    String gsmNumber;

    Double balance = 100.0;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    LocalDateTime creationDate;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer")
    List<Transaction> transactionList = new ArrayList<>();

    public Double getBalance() {
        if (Objects.equals(balance, null)) {
            return 0.0;
        }
        return balance;
    }

}
