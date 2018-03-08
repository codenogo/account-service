package com.codenogo.accountservice.controller;

import com.codenogo.accountservice.exception.HTTP400Exception;
import com.codenogo.accountservice.exception.HTTP404Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @Author: Arnold Kizzitoh
 * Date : 3/7/2018, 11:04 AM
 */
public class AbstractRestHandler {

    ResponseEntity<?> response(Object object) {
        if (object instanceof HTTP400Exception) {

            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);

        }else if (object instanceof HTTP404Exception){

            return new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(object, HttpStatus.OK);
    }
}
