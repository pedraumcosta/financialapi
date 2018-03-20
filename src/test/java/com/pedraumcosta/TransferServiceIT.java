package com.pedraumcosta;

import com.jayway.restassured.http.ContentType;
import com.pedraumcosta.model.Account;
import com.pedraumcosta.model.TransferRequest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransferServiceIT {

    private static final String ACCOUNTS_ENDPOINT = "/accounts/";
    private static final String ACCOUNT_ENDPOINT = "/account/";
    private static final String TRANSFER_ENDPOINT = "/transfer/";

    String accountName0 = "accountToBeTransfered";
    String accountName1 = "accountToBeDebted";
    BigDecimal balanceAccount0 = new BigDecimal(1000);
    BigDecimal balanceAccount1 = new BigDecimal(2000);

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
    }

    @Test
    public void test2TryAnEmptyTransferRequest() {
        TransferRequest transferRequest = new TransferRequest();
        given().contentType(ContentType.JSON).body(transferRequest)
                .when().post(TRANSFER_ENDPOINT).then()
                .assertThat()
                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("error", equalTo("Not enough information to process transfer request"));
    }

    @Test
    public void test3IvalidSourceAccountTransferRequest() {
        TransferRequest transferRequest = new TransferRequest("InvalidAccount", accountName0,
                new BigDecimal(100));
        given().contentType(ContentType.JSON).body(transferRequest)
                .when().post(TRANSFER_ENDPOINT).then()
                .assertThat()
                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("error", equalTo("Source account do not exists"));
    }

    @Test
    public void test4IvalidDestinationAccountTransferRequest() {
        TransferRequest transferRequest = new TransferRequest(accountName0, "InvalidAccount",
                new BigDecimal(100));
        given().contentType(ContentType.JSON).body(transferRequest)
                .when().post(TRANSFER_ENDPOINT).then()
                .assertThat()
                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("error", equalTo("Destination account do not exists"));
    }

    @Test
    public void test5NotEnoughFundsOnSourceAccount() {
        TransferRequest transferRequest = new TransferRequest(accountName0, accountName1,
                new BigDecimal(1000000));
        given().contentType(ContentType.JSON).body(transferRequest)
                .when().post(TRANSFER_ENDPOINT).then()
                .assertThat()
                .statusCode(equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("error", equalTo("Not enough funds on source account"));
    }

    @Test
    public void test6SuccessFullTransfer() {
        TransferRequest transferRequest = new TransferRequest(accountName0, accountName1,
                new BigDecimal(500));

        given().contentType(ContentType.JSON).body(transferRequest)
                .when().post(TRANSFER_ENDPOINT).then()
                .assertThat()
                .statusCode(equalTo(HttpStatus.OK.value()))
                .body("status", equalTo("COMPLETED"));

        String appPath = ACCOUNT_ENDPOINT + accountName0;
        when().get(appPath).then()
                .body("name", equalTo(accountName0))
                .body("balance", equalTo(500f));

        appPath = ACCOUNT_ENDPOINT + accountName1;
        when().get(appPath).then()
                .body("name", equalTo(accountName1))
                .body("balance", equalTo(2500f));
    }

}
