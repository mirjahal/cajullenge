package br.com.caju;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;

@MicronautTest
class AuthorizeTransactionE2ETest {

    private static final String ACCOUNT_ID = "1c589934-d43e-4f60-a4af-067c6feb7394";

    @Test
    @DisplayName("Should return code 00 when transaction approved")
    public void transactionApproved(RequestSpecification requestSpecification) {
        // JEP 430: String Templates (Preview) JEP https://openjdk.org/jeps/430
        String requestBody = """
        {
            "id": "%s",
            "accountId": "%s",
            "amount": 1,
            "merchant": "PADARIA DO ZE             SAO PAULO BR",
            "mcc": 5411
        }
        """.formatted(randomUUID(), ACCOUNT_ID);

        String responseBody = "{\"code\":\"00\"}";

        requestSpecification
            .when()
                .body(requestBody)
                .contentType(ContentType.JSON.toString())
                .post("/authorize")
            .then()
                .statusCode(200)
                .body(is(responseBody));
    }

    @Test
    @DisplayName("Should return code 00 when transaction approved extracting mcc from merchant name")
    public void transactionApprovedMccFromMerchantName(RequestSpecification requestSpecification) {
        String requestBody = """
        {
            "id": "%s",
            "accountId": "%s",
            "amount": 1,
            "merchant": "PADARIA DO ZE EATS            SAO PAULO BR",
            "mcc": 1001
        }
        """.formatted(randomUUID(), ACCOUNT_ID);

        String responseBody = "{\"code\":\"00\"}";

        requestSpecification
                .when()
                .body(requestBody)
                .contentType(ContentType.JSON.toString())
                .post("/authorize")
                .then()
                .statusCode(200)
                .body(is(responseBody));
    }

    @Test
    @DisplayName("Should return code 00 when transaction approved using fallback considering balances MEAL equals 400 and CASH 3000")
    public void transactionApprovedUsingFallback(RequestSpecification requestSpecification) {
        String requestBody = """
        {
            "id": "%s",
            "accountId": "%s",
            "amount": 1000,
            "merchant": "PADARIA DO ZE            SAO PAULO BR",
            "mcc": 5812
        }
        """.formatted(randomUUID(), ACCOUNT_ID);

        String responseBody = "{\"code\":\"00\"}";

        requestSpecification
                .when()
                .body(requestBody)
                .contentType(ContentType.JSON.toString())
                .post("/authorize")
                .then()
                .statusCode(200)
                .body(is(responseBody));
    }

    @Test
    @DisplayName("Should return code 00 when transaction approved using fallback considering unable mapping MCC")
    public void transactionApprovedUsingFallbackUnableMappingMcc(RequestSpecification requestSpecification) {
        String requestBody = """
        {
            "id": "%s",
            "accountId": "%s",
            "amount": 1,
            "merchant": "PADARIA DO ZE            SAO PAULO BR",
            "mcc": 987654
        }
        """.formatted(randomUUID(), ACCOUNT_ID);

        String responseBody = "{\"code\":\"00\"}";

        requestSpecification
                .when()
                .body(requestBody)
                .contentType(ContentType.JSON.toString())
                .post("/authorize")
                .then()
                .statusCode(200)
                .body(is(responseBody));
    }

    @Test
    @DisplayName("Should return code 51 when insufficient balance")
    public void transactionInsufficientBalance(RequestSpecification requestSpecification) {
        String requestBody = """
        {
            "id": "%s",
            "accountId": "%s",
            "amount": 5000,
            "merchant": "PADARIA DO ZE             SAO PAULO BR",
            "mcc": 5411
        }
        """.formatted(randomUUID(), ACCOUNT_ID);

        String responseBody = "{\"code\":\"51\"}";

        requestSpecification
                .when()
                .body(requestBody)
                .contentType(ContentType.JSON.toString())
                .post("/authorize")
                .then()
                .statusCode(200)
                .body(is(responseBody));
    }

    @Test
    @DisplayName("Should return code 07 when merchant is blank")
    public void transactionRejectedWhenMerchantIsBlank(RequestSpecification requestSpecification) {
        String requestBody = """
        {
            "id": "%s",
            "accountId": "%s",
            "amount": 1,
            "merchant": "",
            "mcc": 5411
        }
        """.formatted(randomUUID(), ACCOUNT_ID);

        String responseBody = "{\"code\":\"07\"}";

        requestSpecification
                .when()
                .body(requestBody)
                .contentType(ContentType.JSON.toString())
                .post("/authorize")
                .then()
                .statusCode(200)
                .body(is(responseBody));
    }
}
