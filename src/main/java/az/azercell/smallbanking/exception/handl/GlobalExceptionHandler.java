package az.azercell.smallbanking.exception.handl;

import az.azercell.smallbanking.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternal(final Exception ex) {

        List<String> errorList = getErrorList(ex);

        final GenericResponse bodyOfResponse = new GenericResponse("Server error", errorList);
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(
            final RuntimeException ex, final WebRequest request) {
        getErrorList(ex);
        final GenericResponse bodyOfResponse = new GenericResponse("Server error", "This GSM Number already exists");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        log.error("Validation errors --> {}", ex.getMessage());
        List<String> errors = new ArrayList<>();
        List<String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());
        List<String> globalErrors = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(error -> error.getObjectName() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());
        errors.addAll(fieldErrors);
        errors.addAll(globalErrors);
        GenericResponse genericResponse = new GenericResponse("Validation errors ", errors);
        return new ResponseEntity<>(genericResponse, headers, status);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        log.error("Mismatch Type --> ", ex);
        final GenericResponse apiError = new GenericResponse("Mismatch Type", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
        logger.error("Method Not Found", ex);
        final GenericResponse apiError = new GenericResponse("Method Not Found",
                String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        logger.error("Missing Parameters", ex);
        final GenericResponse bodyOfResponse = new GenericResponse("Missing Parameters", "Parameter " + "'" + ex.getParameterName() + "'" + " is missing");
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<Object> handleCustomException(CustomException ex, WebRequest request) {
        log.error(ex.getMessage());
        GenericResponse bodyOfResponse = new GenericResponse();
        bodyOfResponse.setMessage(ex.getMessage());
        bodyOfResponse.getErrors().add(ex.getCode());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private static List<String> getErrorList(Exception ex) {
        Throwable cause = ex.getCause();

        List<String> errorList = new ArrayList<>();
        while (cause != null) {
            errorList.add(cause.getMessage());
            cause = cause.getCause();
        }

        errorList.forEach(log::error);

        if (errorList.isEmpty()) {
            return Collections.singletonList(ex.getMessage());
        }
        return errorList;
    }
}
