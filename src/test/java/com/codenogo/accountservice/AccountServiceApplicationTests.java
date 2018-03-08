package com.codenogo.accountservice;

import com.codenogo.accountservice.model.AccountData;
import com.codenogo.accountservice.model.FetchQuery;
import com.codenogo.accountservice.utils.JsonUtil;
import com.codenogo.accountservice.utils.RRNGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceApplicationTests extends BaseTest{

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenExceededMaximumDepositPerTransaction_returnBadRequest_onDeposit(){

		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(50000));
		accountData.setRefNo(rrn.getRRN());
		accountData.setAccountId("321654987");

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/deposit").then()
				.statusCode(400);
	}

	@Test
	public void returnOk_givenAccountData_onDeposit(){
		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(40000));
		accountData.setAccountId("123456");
		accountData.setRefNo(rrn.getRRN());

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/deposit").then()
				.statusCode(200);


	}

	@Test
	public void whenExceedsDepositCount4_returnBadRequest_onDeposit(){
		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(10000));
		accountData.setAccountId(rrn.getRandomCharacterRRN("AC"));
		accountData.setRefNo(rrn.getRRN());

		IntStream.range(0, 4).forEach(i -> given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/deposit").then()
				.statusCode(200));

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/deposit").then()
				.statusCode(400);


	}

	@Test
	public void whenExceedsDepositDailyMax150_returnBadRequest_onDeposit(){
		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(40000));
		accountData.setAccountId(rrn.getRandomCharacterRRN("AC"));
		accountData.setRefNo(rrn.getRRN());

		IntStream.range(0, 3).forEach(i -> given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/deposit").then()
				.statusCode(200));
		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/deposit").then()
				.statusCode(400);

	}

	/**
	 * Testing Withdrawal
	 */

	/**
	 * Withdrawal Test when account is not available
	 */

	@Test
	public void whenAccountDoesNotExist_onDebit(){
		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(10000));
		accountData.setAccountId(rrn.getRandomCharacterRRN("AC"));
		accountData.setRefNo(rrn.getRRN());

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(404);
	}

	/**
	 * When trying to withdraw more than available balance
	 */

	@Test
	public void whenTryingToWithDrawMoreThanAvailableBalance_onDebit(){
		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(20000));
		accountData.setAccountId("123456");
		accountData.setRefNo(rrn.getRRN());

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(400);

	}

	/**
	 * When attempting to withdraw less than zero
	 */

	@Test
	public void whenWithdrawingLessThanZero_onDebit(){
		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(-20000));
		accountData.setAccountId("123456");
		accountData.setRefNo(rrn.getRRN());

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(400);

	}

	/**
	 * Withdrawing more than the limit per transaction
	 */

	@Test
	public void whenExceededDebitLimitPerTransaction_onDebit(){

		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(30000));
		accountData.setAccountId("123456");
		accountData.setRefNo(rrn.getRRN());

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(400);

	}
	/**
	 * Withdrawing more than 3 times per day
	 */

	@Test
	public void whenExceededDebitCountPerDay_onDebit(){

		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(500));
		accountData.setAccountId("123456");
		accountData.setRefNo(rrn.getRRN());

		IntStream.range(0, 3).forEach(i -> given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(200));

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(400);

	}

	/**
	 * Withdrawing more that daily maximum limit
	 */
	@Test
	public void whenExceededMaximumDailyLimit_onDebit(){

		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(20000));
		accountData.setAccountId("601");
		accountData.setRefNo(rrn.getRRN());

		IntStream.range(0, 2).forEach(i -> given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(200));

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(400);

	}

	/**
	 * On Successful withdrawal
	 */

	@Test
	public void onSuccessfulDebit_returnsOk_onDebit(){

		RRNGenerator rrn = new RRNGenerator("RE");

		AccountData accountData = new AccountData();
		accountData.setAmount(new BigDecimal(500));
		accountData.setAccountId("601");
		accountData.setRefNo(rrn.getRRN());

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(accountData))
				.when().post("/debit").then()
				.statusCode(200);


	}

	/**
	 * Testing CheckBalance when account does not exist
	 */

	@Test
	public void checkingbalancecwhenaccountiddoesnotexist_onCheckBalance(){

		FetchQuery fetchQuery = new FetchQuery();
		fetchQuery.setAccountId("343UIR");

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(fetchQuery))
				.when().post("/checkBalance").then()
				.statusCode(404);



	}

	@Test
	public void onsuccessfulCheckofBalance_onChackBalance(){

		FetchQuery fetchQuery = new FetchQuery();
		//use an existing account
		fetchQuery.setAccountId("123456");

		given()
				.contentType("application/json")
				.body(JsonUtil.createJsonFromObject(fetchQuery))
				.when().post("/checkBalance").then()
				.statusCode(200);

	}


}
