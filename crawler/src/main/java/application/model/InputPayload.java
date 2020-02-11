package application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//@Builder
//@Value
//@RequiredArgsConstructor
//@NonNull
//@JsonDeserialize(builder = InputPayload.InputPayloadBuilder.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InputPayload {
    private List<String> urls;

//    @Builder.Default
    private int threadCount = 1;
//
//    @JsonPOJOBuilder(withPrefix = "")
//    static final class InputPayloadBuilder {}
}
