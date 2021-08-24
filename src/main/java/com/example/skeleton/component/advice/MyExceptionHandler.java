package com.example.skeleton.component.advice;


import com.example.skeleton.exception.*;
import com.example.skeleton.model.dto.StatusMsg;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

interface SmartMsgMarshaller {
    <E> ResponseEntity<StatusMsg> Marshal(E e);
}

@RestControllerAdvice
public class MyExceptionHandler {
    static private final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    private final SmartMsgMarshaller exceptionMarshaller;

    public MyExceptionHandler(SmartMsgMarshaller exceptionMarshaller) {
        this.exceptionMarshaller = exceptionMarshaller;
    }

    @ExceptionHandler(value = {AuthException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<StatusMsg> AuthException(AuthException e) {
        return this.exceptionMarshaller.Marshal(e);
    }

    @ExceptionHandler(value = {ClientSideException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StatusMsg> ClientSideException(WebAppException e) {
        return this.exceptionMarshaller.Marshal(e);
    }

    @ExceptionHandler(value = {ServerSideException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse
    public ResponseEntity<StatusMsg> ServerSideException(WebAppException e) {
        return this.exceptionMarshaller.Marshal(e);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<StatusMsg> AccessDeniedException(AccessDeniedException e) {
        return this.exceptionMarshaller.Marshal(ClientSideException.Forbidden(e.getMessage()).CausedBy(e));
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseEntity<StatusMsg> SpringWebException1(Exception e) {
        return this.exceptionMarshaller.Marshal(StatusMsg.builder().code(HttpStatus.BAD_REQUEST.value()).occurred_at(LocalDateTime.now()).msg(e.getMessage()).Details(e).build());
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> defaultExceptionHandler(Throwable e) {
        logger.error("exception", e);
        return this.exceptionMarshaller.Marshal(ServerSideException.InternalError(e.getMessage()).CausedBy(e));
    }


}

@Component
@Profile("prod")
class ProdMsgMarshaller implements SmartMsgMarshaller {

    static private final Logger logger = LoggerFactory.getLogger(ProdMsgMarshaller.class);

    @Override
    public <E> ResponseEntity<StatusMsg> Marshal(E e) {
        if (e instanceof CustomRuntimeException) {
            return Marshal(StatusMsg.builder().code(((CustomRuntimeException) e).getCode()).msg(((CustomRuntimeException) e).getMsg()).occurred_at(((CustomRuntimeException) e).getOccurredAt()).build());
        } else if (e instanceof StatusMsg) {
            return ResponseEntity.status(((StatusMsg) e).getCode()).body((StatusMsg) e);
        } else if (e instanceof Throwable) {
            return Marshal(StatusMsg.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(((Throwable) e).getMessage()).occurred_at(LocalDateTime.now()).build());
        } else {
            logger.warn("unknown msg prototype {}", e.getClass());
            return null;
        }
    }
}

@Component
@Profile("dev")
class DevMsgMarshaller implements SmartMsgMarshaller {

    static private final Logger logger = LoggerFactory.getLogger(DevMsgMarshaller.class);

    @Override
    public <E> ResponseEntity<StatusMsg> Marshal(E e) {
        if (e instanceof CustomRuntimeException) {
            return Marshal(StatusMsg.builder().Details(e).code(((CustomRuntimeException) e).getCode()).msg(((CustomRuntimeException) e).getMsg()).occurred_at(((CustomRuntimeException) e).getOccurredAt()).build());
        } else if (e instanceof StatusMsg) {
            return ResponseEntity.status(((StatusMsg) e).getCode()).body((StatusMsg) e);
        } else if (e instanceof Throwable) {
            return Marshal(StatusMsg.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(((Throwable) e).getMessage()).occurred_at(LocalDateTime.now()).Details(e).build());
        } else {
            logger.warn("unknown msg prototype {}", e.getClass());
            return null;
        }
    }
}

@Component
@Profile("prev")
class PrevMsgMarshaller implements SmartMsgMarshaller {

    static private final Logger logger = LoggerFactory.getLogger(DevMsgMarshaller.class);

    @Override
    public <E> ResponseEntity<StatusMsg> Marshal(E e) {
        if (e instanceof CustomRuntimeException) {
            return Marshal(StatusMsg.builder().Details(e).code(((CustomRuntimeException) e).getCode()).msg(((CustomRuntimeException) e).getMsg()).occurred_at(((CustomRuntimeException) e).getOccurredAt()).build());
        } else if (e instanceof StatusMsg) {
            return ResponseEntity.status(((StatusMsg) e).getCode()).body((StatusMsg) e);
        } else if (e instanceof Throwable) {
            return Marshal(StatusMsg.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(((Throwable) e).getMessage()).occurred_at(LocalDateTime.now()).Details(e).build());
        } else {
            logger.warn("unknown msg prototype {}", e.getClass());
            return null;
        }
    }
}