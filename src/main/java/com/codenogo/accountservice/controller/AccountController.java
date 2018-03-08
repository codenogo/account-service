package com.codenogo.accountservice.controller;

import com.codenogo.accountservice.model.AccountData;
import com.codenogo.accountservice.model.FetchQuery;
import com.codenogo.accountservice.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;

/**
 * @Author: Arnold Kizzitoh
 */

@RestController
public class AccountController extends AbstractRestHandler {

    @Autowired
    IAccountService accountService;

    /**
     * Deposit
     *
     * @param accountData
     * @return
     */

    @PostMapping(value = "/api/deposit")
    public ResponseEntity<?> creditAccount(@RequestBody @Valid AccountData accountData) throws ParseException {

        return response(accountService.creditAccount(accountData)) ;

    }

    /**
     * Check Balance endpoint
     *
     * @param fetchQuery
     * @return
     */

    @PostMapping(value = "/api/checkBalance")
    public ResponseEntity<?> queryBalance(@RequestBody FetchQuery fetchQuery) {

        return response(accountService.checkBalance(fetchQuery));
    }

    /**
     * Debit account endpoint
     *
     * @param accountData
     * @return
     */

    @PostMapping("/api/debit")
    public ResponseEntity<?> debitAccount(@RequestBody AccountData accountData) {

        return response(accountService.debitAccount(accountData));
    }


}
