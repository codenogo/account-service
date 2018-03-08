package com.codenogo.accountservice.repository;

import com.codenogo.accountservice.model.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @Author: Arnold Kizzitoh
 */
public interface AccountDataRepository extends JpaRepository<AccountData, Long> {

    List<AccountData> findAllByAccountId(String accountId);
}
