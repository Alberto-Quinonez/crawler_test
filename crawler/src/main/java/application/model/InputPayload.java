package application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.util.List;

@Builder
@Value
@RequiredArgsConstructor
@NonNull
@JsonDeserialize(builder = InputPayload.InputPayloadBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class InputPayload {
    private final List<String> urls;

    @Builder.Default
    private final int threadCount = 1;

    @JsonPOJOBuilder(withPrefix = "")
    static final class InputPayloadBuilder {}
}
