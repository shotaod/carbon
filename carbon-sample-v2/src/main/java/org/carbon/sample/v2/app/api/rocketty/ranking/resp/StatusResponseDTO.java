package org.carbon.sample.v2.app.api.rocketty.ranking.resp;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.carbon.web.core.validation.HandyValidationResult;
import org.carbon.web.translate.dto.Json;

/**
 * @author Shota.Oda 2018/02/08.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StatusResponseDTO implements Json {
    protected String message;
    protected List<?> payloads;

    public static StatusResponseDTO success() {
        return new StatusResponseDTO("success", null);
    }

    public static StatusResponseDTO success(String data) {
        return new StatusResponseDTO("success, published key", Collections.singletonList(data));
    }

    public static StatusResponseDTO badRequest(HandyValidationResult vr) {
        List<String> payloads = vr.getViolationResults().entrySet().stream()
                .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return new StatusResponseDTO("invalid parameter", payloads);
    }

    public static StatusResponseDTO badRequest(String message) {
        return new StatusResponseDTO(message, null);
    }
}
