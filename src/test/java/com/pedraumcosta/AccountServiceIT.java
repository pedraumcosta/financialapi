package com.pedraumcosta;

import com.jayway.restassured.http.ContentType;
import com.pedraumcosta.model.Account;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountServiceIT {

    private static final String ACCOUNTS_ENDPOINT = "/accounts/";
    private static final String ACCOUNT_ENDPOINT = "/account/";

    String accountName0 = "HappyAccountHolder";
    String accountName1 = "UnHappyAccountHolder";
    String accountName2 = "NoFeelingsAccountHolder";
    BigDecimal balanceAccount0 = new BigDecimal(1000);
    BigDecimal balanceAccount1 = new BigDecimal(2000);
    BigDecimal balanceAccount2 = new BigDecimal(3000);

    @Test
    public void test1CreateAccounts() {
        Account account = new Account(accountName0, balanceAccount0);
        given().contentType(ContentType.JSON).body(account)
                .when().post(ACCOUNT_ENDPOINT).then()
                .assertThat().statusCode(equalTo(HttpStatus.OK.value()));

        account = new Account(accountName1, balanceAccount1);
        given().contentType(ContentType.JSON).body(account)
                .when().post(ACCOUNT_ENDPOINT).then()
                .assertThat().statusCode(equalTo(HttpStatus.OK.value()));

        account = new Account(accountName2, balanceAccount2);
        given().contentType(ContentType.JSON).body(account)
                .when().post(ACCOUNT_ENDPOINT).then()
                .assertThat().statusCode(equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void test2ListAccounts() {
        when().get(ACCOUNTS_ENDPOINT).then()
                .body("[0].name", equalTo(accountName0))
                .body("[0].balance", equalTo(1000f))
                .body("[1].name", equalTo(accountName1))
                .body("[1].balance", equalTo(2000f));
    }

    @Test
    public void test3ListExistingAccount() {
        String appPath = ACCOUNT_ENDPOINT + accountName0;
        when().get(appPath).then()
                .body("name", equalTo(accountName0))
                .body("balance", equalTo(1000f));
    }

    @Test
    public void test4CreateAnEmptyAccount() {
        Account account = new Account();
        given().contentType(ContentType.JSON).body(account)
                .when().post(ACCOUNT_ENDPOINT).then()
                .assertThat()
                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("error", equalTo("Not enough information to create an account"));
    }

    @Test
    public void test5CreateAccountWithNegativeBalance() {
        String accountName = "DummyAccountName";
        BigDecimal neagtiveValue = new BigDecimal(-1000);
        Account account = new Account(accountName, neagtiveValue);
        given().contentType(ContentType.JSON).body(account)
                .when().post(ACCOUNT_ENDPOINT).then()
                .assertThat().statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("error", equalTo("Can't create an account with negative balance"));;


    }


    //@todo Create account with same name
}
