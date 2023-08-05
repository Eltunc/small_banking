package az.azercell.smallbanking.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDto {

    Long id;

    String name;
    String surname;
    LocalDate birthDate;
    String gsmNumber;
    Double balance = 100.0;

}
