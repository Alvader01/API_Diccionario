package com.github.alvader01.api_diccionario.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//esta excepcion se va a lanzar y automáticamente se va a generar una respuesta en el protocolo http
//de valor  NOT_FOUND
@ResponseStatus (value = HttpStatus.NOT_FOUND)  //
public class RecordNotFoundException extends RuntimeException {

    private String exceptionDetail;
    private Object fieldValue;

    public RecordNotFoundException(String exceptionDetail) {
        super(exceptionDetail);
    }

    public RecordNotFoundException(String exceptionDetail, Object fieldValue) {
        super(exceptionDetail+" - "+fieldValue);
        this.exceptionDetail = exceptionDetail;
        this.fieldValue = fieldValue;
    }

    public String getExceptionDetail() {
        return exceptionDetail;
    }
    public Object getFieldValue() {
        return fieldValue;
    }
}
