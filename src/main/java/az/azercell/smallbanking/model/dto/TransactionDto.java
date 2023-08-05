package az.azercell.smallbanking.model.dto;


import az.azercell.smallbanking.model.enums.Status;
import az.azercell.smallbanking.model.enums.Type;
import az.azercell.smallbanking.util.TypeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDto {

    Long id;

    Double amount;

    Status status;

    @JsonDeserialize(using = TypeDeserializer.class)
    Type type;

    Long relatedTransactionId;

    Long customerId;

    public TransactionDto(Long relatedTransactionId, Double amount) {
        this.relatedTransactionId = relatedTransactionId;
        this.amount = amount;
    }
}
