package br.com.caju.infrastructure.web.controller;

import br.com.caju.application.usecase.TransactionAuthorizer;
import br.com.caju.infrastructure.web.dto.TransactionRequest;
import br.com.caju.infrastructure.web.dto.TransactionResponse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;

@Validated
@Controller("/authorize")
public class TransactionAuthorizerController {

    private final TransactionAuthorizer transactionAuthorizer;

    public TransactionAuthorizerController(TransactionAuthorizer transactionAuthorizer) {
        this.transactionAuthorizer = transactionAuthorizer;
    }

    @Post
    public HttpResponse<TransactionResponse> authorizeTransaction(@Body @Valid TransactionRequest transactionRequest) {
        var transaction = transactionRequest.toTransaction();
        var accountId = transactionRequest.accountId();

        var transactionStatus = transactionAuthorizer.authorize(transaction, accountId);

        return HttpResponse.ok(new TransactionResponse(transactionStatus.getCode()));
    }
}
