package az.azercell.smallbanking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Pattern(regexp = "(\\+99450|\\+99451|\\+99410)[0-9]{7}", message = "GSM number is not valid")
@Size(min = 13, max = 13, message = "GSM number should be 13 characters long")
@NotNull(message = "GSM number cannot be null")
@ReportAsSingleViolation
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGsmNumber {
    String message() default "{GSM number is not valid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}