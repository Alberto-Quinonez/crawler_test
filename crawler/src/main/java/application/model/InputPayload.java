package application.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class InputPayload {
    private final List<String> urls;
    private final int threadCount;
}
