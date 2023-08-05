package az.azercell.smallbanking.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AllServicesAOP {

    @Before(value = "execution(* az.azercell.smallbanking.service.*.*(..))")
    public void beforeAllMethodsInAllServices(JoinPoint joinPoint) {
        log.info("Method Location {}", joinPoint.getSignature());
        log.info("Method Parameters {}", Arrays.toString(joinPoint.getArgs()));
    }


    @AfterReturning(value = "execution(* az.azercell.smallbanking.service.*.*(..))", returning = "result")
    public void afterAllMethodsInAllServices(Object result) {
        log.info("Result is {}", result);
    }

}
