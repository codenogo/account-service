package com.codenogo.accountservice.service;

import com.codenogo.accountservice.exception.HTTP400Exception;
import com.codenogo.accountservice.exception.HTTP404Exception;
import com.codenogo.accountservice.model.*;
import com.codenogo.accountservice.repository.AccountDataRepository;
import com.codenogo.accountservice.utils.Messages;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: Arnold Kizzitoh
 */
@Service
public class AccountService implements IAccountService {

    @Autowired
    private Messages messages;

    @Autowired
    EntityManager entityManager;

    @Autowired
    private AccountDataRepository accountDataRepository;


    LocalDateTime dt = LocalDateTime.now().with(LocalTime.MIDNIGHT);
    LocalDateTime plusOneDay = dt.plusDays(1);

    Date from = Date.from(Instant.from(dt.atZone(ZoneId.systemDefault())));
    Date to = Date.from(Instant.from(plusOneDay.atZone(ZoneId.systemDefault())));


    QAccountData qAccountData = QAccountData.accountData;


    /**
     * Deposit limits
     */
    @Value("${daily_limit}")
    BigDecimal daily_limit;

    @Value("${tnx_limit}")
    BigDecimal tnx_limit;

    @Value("${daily_count_limit}")
    long daily_count_limit;

    /**
     * Withdrawal Limits
     */
    @Value("${w_daily_limit}")
    BigDecimal w_daily_limit;

    @Value("${w_tnx_limit}")
    BigDecimal w_tnx_limit;

    @Value("${w_daily_count_limit}")
    long w_daily_count_limit;


    /**
     * Persists deposit information to database
     *
     * @param accountData
     * @return
     */
    public AccountData saveAccount(AccountData accountData) {
        accountData.setTnx_time(new Date());
        return accountDataRepository.save(accountData);
    }

    /**
     * Returns all unique users in accounts table
     *
     * @param id
     * @return
     */

    public List<AccountData> findUniqueUsers(String id) {
        return accountDataRepository.findAllByAccountId(id);
    }

    /**
     * Returns user's initial balance in the account
     *
     * @param id
     * @return
     */

    public List<BigDecimal> fetchInitialBalance(String id) {


        JPQLQuery<?> query = new JPAQuery<>(entityManager);

        return query.select(qAccountData.amount.sum())
                .from(qAccountData)
                .where(qAccountData.accountId.eq(id))
                .fetch();
    }

    /**
     * Returns the map of account balance of a given user
     *
     * @param id
     * @return
     */

    public Balance queryAccountBalance(String id) {

        JPQLQuery<?> query = new JPAQuery<>(entityManager);

        AccountData accountData = query.select(qAccountData).from(qAccountData)
                .where(qAccountData.accountId.eq(id)
                        .and(qAccountData.id.eq(JPAExpressions.select(qAccountData.id.max()).from(qAccountData).where(qAccountData.accountId.eq(id)))))
                .fetchOne();

        Balance balanceResponse = new Balance(accountData.getAccountId(), accountData.getBalance());

        return balanceResponse;

    }

    /**
     * credit account
     *
     * @param accountData
     * @return
     * @throws ParseException
     */

    public Object creditAccount(AccountData accountData) {

        if (accountData.getAmount().doubleValue() <= tnx_limit.doubleValue()) {

            if (getCount(accountData.getAccountId()) < daily_count_limit) {

                List<AccountData> uniqueUsers = findUniqueUsers(accountData.getAccountId());

                if (uniqueUsers.isEmpty()) {
                    // Checking if user has an account, if not, set the incoming amount as new balance
                    accountData.setBalance(accountData.getAmount());
                    saveAccount(accountData);
                    return ResponseEntity.status(HttpStatus.OK).body(accountData);
                } else {

                    BigDecimal total = getDailySum(accountData.getAccountId()).add(accountData.getAmount());

                    if (total.doubleValue() <= daily_limit.doubleValue()) {

                        //Compute new balance
                        List<BigDecimal> initialBalance = fetchInitialBalance(accountData.getAccountId());
                        BigDecimal currentBalance = initialBalance.get(0).add(accountData.getAmount());
                        accountData.setBalance(currentBalance);
                        saveAccount(accountData);
                        return accountData;

                    } else {

                        throw new HTTP400Exception(messages.get("daily_total_deposit_exceeded"));

                    }

                }

            } else {

                throw new HTTP400Exception(messages.get("daily_count_deposit_exceeded"));

            }


        } else {

            throw new HTTP400Exception(messages.get("deposit_tnx_limit_exceeded"));


        }

    }

    /**
     * get the frequency of deposit entry into database per day
     *
     * @param accountId
     * @return
     */

    public Long getCount(String accountId) {

        JPQLQuery<?> query = new JPAQuery<>(entityManager);

        Long frequency = query.select(qAccountData.accountId.count())
                .from(qAccountData)
                .where(qAccountData.tnx_time.between(from, to).and(qAccountData.accountId.eq(accountId)).and(qAccountData.amount.gt(0)))
                .fetchCount();

        return frequency;

    }

    /**
     * @param accountId
     * @return
     * @throws ParseException
     */

    public Long getDebitCount(String accountId) {


        JPQLQuery<?> query = new JPAQuery<>(entityManager);

        Long frequency = query.select(qAccountData.accountId.count())
                .from(qAccountData)
                .where(qAccountData.tnx_time.between(from, to).and(qAccountData.accountId.eq(accountId)).and(qAccountData.amount.lt(0)))
                .fetchCount();

        return frequency;
    }


    /**
     * Returns the sum deposits transacted already in a day
     *
     * @param accountId
     * @return
     */

    public BigDecimal getDailySum(String accountId) {


        JPQLQuery<?> query = new JPAQuery<>(entityManager);

        BigDecimal sum = query.select(qAccountData.amount.sum().coalesce(BigDecimal.ZERO))
                .from(qAccountData)
                .where(qAccountData.accountId.eq(accountId).and(qAccountData.tnx_time.between(from, to)).and(qAccountData.amount.gt(0)))
                .fetchOne();

        return sum;

    }

    /**
     * Fetch Sum of daily withdrawals
     *
     * @param accountId
     * @return
     */

    public BigDecimal getDebitDailySum(String accountId) {

        JPQLQuery<?> query = new JPAQuery<>(entityManager);

        BigDecimal sum = query.select(qAccountData.amount.sum().coalesce(BigDecimal.ZERO))
                .from(qAccountData)
                .where(qAccountData.accountId.eq(accountId).and(qAccountData.tnx_time.between(from, to)).and(qAccountData.amount.lt(0)))
                .fetchOne();

        return sum.negate();

    }


    /**
     * Debit account
     *
     * @param accountData
     * @return
     */

    public Object debitAccount(AccountData accountData) {

        //1.Check limits of amount to be withdrawn

        if (accountData.getAmount().doubleValue() <= w_tnx_limit.doubleValue()) {

            //2.Check frequency
            if (getDebitCount(accountData.getAccountId()) < w_daily_count_limit) {
                //1. Find unique users
                List<AccountData> uniqueUsers = findUniqueUsers(accountData.getAccountId());

                //2. Check if the user exists, if not throw Exception
                if (uniqueUsers.isEmpty()) {

                    throw new HTTP404Exception(messages.get("account_non-existent"));


                }

                if (accountData.getAmount().doubleValue() > 0.00) {

                    BigDecimal w_total = getDebitDailySum(accountData.getAccountId()).add(accountData.getAmount());

                    if (w_total.doubleValue() <= w_daily_limit.doubleValue()) {

                        List<BigDecimal> initialBalance = fetchInitialBalance(accountData.getAccountId());

                        if (initialBalance.get(0).doubleValue() <= 0.00 || initialBalance.get(0).doubleValue() < accountData.getAmount().doubleValue()) {

                            throw new HTTP400Exception(messages.get("not_enough_funds"));

                        } else {

                            BigDecimal debitAmount = accountData.getAmount().negate();
                            BigDecimal currentBalance = initialBalance.get(0).add(debitAmount);
                            accountData.setAmount(debitAmount);
                            accountData.setBalance(currentBalance);
                            accountData.setTnx_time(new Date());
                            saveAccount(accountData);
                        }
                        return accountData;

                    } else {

                        throw new HTTP400Exception(messages.get("daily_total_withdrawals_exceeded"));

                    }

                } else {

                    throw new HTTP400Exception(messages.get("lt_zero"));

                }

            } else {

                throw new HTTP400Exception(messages.get("daily_count_withdrawal_exceeded"));

            }

        } else {

            throw new HTTP400Exception(messages.get("withdrawal_tnx_limit_exceeded"));

        }
    }

    /**
     * Fetches account balance
     *
     * @param fetchQuery
     * @return
     */

    public Object checkBalance(FetchQuery fetchQuery) {

        List<AccountData> existingUser = findUniqueUsers(fetchQuery.getAccountId());

        if (existingUser.isEmpty()) {

            throw new HTTP404Exception(messages.get("account_non-existent"));
        }

        Balance accountBalanceRes = queryAccountBalance(fetchQuery.getAccountId());

        return accountBalanceRes;


    }


}
