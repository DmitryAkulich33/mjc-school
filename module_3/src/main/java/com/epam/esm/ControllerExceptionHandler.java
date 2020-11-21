package com.epam.esm;

import com.epam.esm.exceptions.*;
import com.epam.esm.service.impl.CertificateServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ResourceBundle;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final ResourceBundle labels = ResourceBundle.getBundle("messages");

    private static Logger log = LogManager.getLogger(CertificateServiceImpl.class);

    @ExceptionHandler(TagValidatorException.class)
    public ResponseEntity<Object> handleTagValidatorException(TagValidatorException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.TAG_ERROR_CODE.getErrorCode(),
                ErrorCode.VALIDATE_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CertificateValidatorException.class)
    public ResponseEntity<Object> handleTagValidatorException(CertificateValidatorException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.CERTIFICATE_ERROR_CODE.getErrorCode(),
                ErrorCode.VALIDATE_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<Object> handleTagNotFoundException(TagNotFoundException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.TAG_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<Object> handleCertificateNotFoundException(CertificateNotFoundException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.CERTIFICATE_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CertificateDaoException.class)
    public ResponseEntity<Object> handleCertificateDaoException(CertificateDaoException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.CERTIFICATE_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TagDaoException.class)
    public ResponseEntity<Object> handleTagDaoException(TagDaoException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.TAG_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TagDuplicateException.class)
    public ResponseEntity<Object> handleTagDuplicateException(TagDuplicateException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.TAG_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CertificateDuplicateException.class)
    public ResponseEntity<Object> handleCertificateDuplicateException(CertificateDuplicateException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.CERTIFICATE_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.ORDER_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderDaoException.class)
    public ResponseEntity<Object> handleOrderDaoException(OrderDaoException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.ORDER_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OrderValidatorException.class)
    public ResponseEntity<Object> handleOrderValidatorException(OrderValidatorException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.ORDER_ERROR_CODE.getErrorCode(),
                ErrorCode.VALIDATE_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.USER_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserDaoException.class)
    public ResponseEntity<Object> handleUserDaoException(UserDaoException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.USER_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserValidatorException.class)
    public ResponseEntity<Object> handleUserValidatorException(UserValidatorException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.USER_ERROR_CODE.getErrorCode(),
                ErrorCode.VALIDATE_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaginationValidatorException.class)
    public ResponseEntity<Object> handlePaginationValidatorException(PaginationValidatorException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.PAGINATION_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        String message = exception.getMessage();
        ExceptionResponse error = new ExceptionResponse(message, errorCode);
        log.error(message, exception);
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WrongEnteredDataException.class)
    public ResponseEntity<Object> handleWrongEnteredDataException(WrongEnteredDataException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.DATA_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(Exception exception, String errorCode, HttpStatus httpStatus) {
        String message = labels.getString(exception.getMessage());
        log.error(message, exception);
        ExceptionResponse error = new ExceptionResponse(message, errorCode);
        return new ResponseEntity<>(error, new HttpHeaders(), httpStatus);
    }
}
