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

    private static final String ACCOUNT_ENDPOINT = "/accounts/";

    String accountName = "Happy Account Holder";
    BigDecimal balance = new BigDecimal(0);

    @Test
    public void test1CreateAccount() {
        Account account = new Account(accountName, balance);

        given().contentType(ContentType.JSON).body(account)
                .when().post(ACCOUNT_ENDPOINT).then()
                .assertThat().statusCode(equalTo(HttpStatus.OK.value()));
    }

    @Test
    public void test2ListAccounts() {
        when().get(ACCOUNT_ENDPOINT).then()
                .body("[0].name", equalTo(accountName))
                .body("[0].balance", equalTo(0.0f));
    }
}
