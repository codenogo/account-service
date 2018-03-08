package com.codenogo.accountservice.service;

import com.codenogo.accountservice.model.AccountData;
import com.codenogo.accountservice.model.Balance;
import com.codenogo.accountservice.model.FetchQuery;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Arnold Kizzitoh
 */
public interface IAccountService {


    AccountData saveAccount(AccountData accountData);

    List<AccountData> findUniqueUsers(String id);

    List<BigDecimal> fetchInitialBalance(String id);

    Balance queryAccountBalance(String id);

    Object creditAccount(AccountData accountData);

    Long getCount(String accountId);

    Long getDebitCount(String accountId);

    BigDecimal getDailySum(String accountId);

    BigDecimal getDebitDailySum(String accountId);

    Object debitAccount(AccountData accountData);

    Object checkBalance(FetchQuery fetchQuery);


}
