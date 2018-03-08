package com.codenogo.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: Arnold Kizzitoh
 * Date : 3/7/2018, 11:07 AM
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HTTP400Exception extends RuntimeException {

    public HTTP400Exception(){
        super();
    }

    public HTTP400Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTP400Exception(String message){
        super(message);
    }

    public HTTP400Exception(Throwable cause){
        super(cause);
    }
}
