package com.epam.esm;

import com.epam.esm.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ResourceBundle;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final ResourceBundle labels = ResourceBundle.getBundle("messages");

    private static Logger log = LogManager.getLogger(ControllerExceptionHandler.class);

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

    @ExceptionHandler(RoleDuplicateException.class)
    public ResponseEntity<Object> handleRoleDuplicateException(RoleDuplicateException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.DATA_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CertificateDuplicateException.class)
    public ResponseEntity<Object> handleCertificateDuplicateException(CertificateDuplicateException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.CERTIFICATE_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<Object> handleUserDuplicateException(UserDuplicateException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.USER_ERROR_CODE.getErrorCode(),
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.USER_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException exception) {
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        String message = exception.getMessage();
        String localeMessage = labels.getString("message.wrong_data");
        ExceptionResponse error = new ExceptionResponse(localeMessage, errorCode);
        log.error(message, exception);
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.FORBIDDEN.value(), ErrorCode.AUTH_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        String message = exception.getMessage();
        String localeMessage = labels.getString("message.forbidden_access");
        ExceptionResponse error = new ExceptionResponse(localeMessage, errorCode);
        log.error(message, exception);
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongEnteredDataException.class)
    public ResponseEntity<Object> handleWrongEnteredDataException(WrongEnteredDataException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.NOT_FOUND.value(), ErrorCode.DATA_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.VALIDATE_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntityForStandardException(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.BAD_REQUEST.value(), ErrorCode.VALIDATE_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntityForStandardException(exception, errorCode, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OpenFileException.class)
    public ResponseEntity<Object> handleOpenFileException(OpenFileException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATA_ERROR_CODE.getErrorCode(),
                ErrorCode.DAO_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Object> handleJwtAuthenticationException(JwtAuthenticationException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.FORBIDDEN.value(), ErrorCode.AUTH_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationDataException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationDataException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.UNAUTHORIZED.value(), ErrorCode.AUTH_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntity(exception, errorCode, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Object> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException exception) {
        String errorCode = String.format("%s%s%s", HttpStatus.UNAUTHORIZED.value(), ErrorCode.VALIDATE_ERROR_CODE.getErrorCode(),
                ErrorCode.DATA_ERROR_CODE.getErrorCode());
        return getResponseEntityForStandardException(exception, errorCode, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Object> getResponseEntity(Exception exception, String errorCode, HttpStatus httpStatus) {
        String message = labels.getString(exception.getMessage());
        log.error(message, exception);
        ExceptionResponse error = new ExceptionResponse(message, errorCode);
        return new ResponseEntity<>(error, new HttpHeaders(), httpStatus);
    }

    private ResponseEntity<Object> getResponseEntityForStandardException(Exception exception, String errorCode, HttpStatus httpStatus) {
        String message = exception.getMessage();
        String localeMessage = labels.getString("message.wrong_data");
        ExceptionResponse error = new ExceptionResponse(localeMessage, errorCode);
        log.error(message, exception);
        return new ResponseEntity<>(error, new HttpHeaders(), httpStatus);
    }
}
