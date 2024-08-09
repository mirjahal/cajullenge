package br.com.caju.infrastructure.web.handler;

import br.com.caju.infrastructure.web.dto.TransactionResponse;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import io.micronaut.http.server.exceptions.response.HateoasErrorResponseProcessor;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static br.com.caju.domain.TransactionStatus.REJECTED;

@Singleton
@Replaces(HateoasErrorResponseProcessor.class)
public class GenericErrorResponseProcessor implements ErrorResponseProcessor<TransactionResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GenericErrorResponseProcessor.class);

    @Override
    public @NonNull MutableHttpResponse<TransactionResponse> processResponse(@NonNull ErrorContext errorContext, @NonNull MutableHttpResponse<?> baseResponse) {
        Throwable throwable = (errorContext.getRootCause().isPresent()) ? errorContext.getRootCause().get() : null;
        logger.info("Root Cause -> ", throwable);

        return HttpResponse.ok(new TransactionResponse(REJECTED.getCode()));
    }
}
