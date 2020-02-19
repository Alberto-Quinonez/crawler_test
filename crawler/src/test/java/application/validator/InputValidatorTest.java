package application.validator;

import application.exception.InvalidThreadCountException;
import application.exception.InvalidUrlException;
import application.exception.MaxThreadCountException;
import application.model.InputPayload;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("Input Payload Validator Test")
class InputValidatorTest {
    private InputValidator validator;

    @BeforeEach
    void setup() {
        validator = new InputValidator();
    }

    @Nested
    class Validate {
        @Nested
        class GivenValidInput {
            InputPayload inputPayload = new InputPayload(Lists.newArrayList("https://www.google.com/"), 1);
            InputPayload result;

            @BeforeEach
            void setup() {
                result = validator.validate(inputPayload);
            }

            @Test
            void thenResultIsIdentical() {
                assertThat(result.getUrls().get(0)).isEqualTo(inputPayload.getUrls().get(0));
                assertThat(result.getThreadCount()).isEqualTo(inputPayload.getThreadCount());
            }
        }

        @Nested
        class GivenInvalidUrl {
            Throwable throwable;
            InputPayload inputPayload = new InputPayload(Lists.newArrayList("gle.com/"), 1);

            @BeforeEach
            void setup() {
                throwable = catchThrowable(() -> validator.validate(inputPayload));
            }


            @Test
            void thenThrowsInvalidUrlException() {
                assertThat(throwable).isInstanceOf(InvalidUrlException.class);
            }
        }

        @Nested
        class GivenInvalidThreadCount {
            @Nested
            class GivenNegativeThreadCount {
                Throwable throwable;
                InputPayload inputPayload = new InputPayload(Lists.newArrayList("https://www.google.com/"), -1);

                @BeforeEach
                void setup() {
                    throwable = catchThrowable(() -> validator.validate(inputPayload));
                }


                @Test
                void thenThrowsInvalidThreadCountException() {
                    assertThat(throwable).isInstanceOf(InvalidThreadCountException.class);
                }
            }
            @Nested
            class GivenAboveMaxThreadCount {
                Throwable throwable;
                InputPayload inputPayload = new InputPayload(Lists.newArrayList("https://www.google.com/"), 40);

                @BeforeEach
                void setup() {
                    throwable = catchThrowable(() -> validator.validate(inputPayload));
                }

                @Test
                void thenThrowsMaxThreadCountException() {
                    assertThat(throwable).isInstanceOf(MaxThreadCountException.class);
                }
            }
        }
    }
}
