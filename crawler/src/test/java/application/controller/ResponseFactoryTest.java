package application.controller;

import application.job.CrawlJob;
import application.job.Job;
import application.job.Status;
import application.model.ProgressResponse;
import application.model.ResponseFactory;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Response Factory Test")
class ResponseFactoryTest {
    private ResponseFactory responseFactory;
    private Job job;
    private CrawlJob crawlJob1;
    private CrawlJob crawlJob2;
    private CrawlJob crawlJob3;
    private CrawlJob crawlJob4;
    private ProgressResponse response;

    @BeforeEach
    void setup() {
        crawlJob1 = new CrawlJob("non-important");
        crawlJob2 = new CrawlJob("non-important");
        crawlJob3 = new CrawlJob("non-important");
        crawlJob4 = new CrawlJob("non-important");
        responseFactory = new ResponseFactory();
    }

    @Nested
    class CreateProgress {
        @Nested
        class GivenNoneStarted {
            @BeforeEach
            void setup() {
                crawlJob1.setStatus(Status.NOT_STARTED);
                crawlJob2.setStatus(Status.NOT_STARTED);
                crawlJob3.setStatus(Status.NOT_STARTED);
                crawlJob4.setStatus(Status.NOT_STARTED);

                Job job = new Job(UUID.randomUUID(), Sets.newHashSet(crawlJob1, crawlJob2, crawlJob3, crawlJob4));

                response = responseFactory.createProgress(job);
            }

            @Test
            void thenAllStatusNotStarted() {
                assertThat(response.getNotStarted()).isEqualTo(4);
            }
        }

        @Nested
        class GivenOneError {
            @BeforeEach
            void setup() {
                crawlJob1.setStatus(Status.NOT_STARTED);
                crawlJob2.setStatus(Status.NOT_STARTED);
                crawlJob3.setStatus(Status.NOT_STARTED);
                crawlJob4.setStatus(Status.ERROR);

                Job job = new Job(UUID.randomUUID(), Sets.newHashSet(crawlJob1, crawlJob2, crawlJob3, crawlJob4));

                response = responseFactory.createProgress(job);
            }

            @Test
            void thenOneStatusError() {
                assertThat(response.getNotStarted()).isEqualTo(3);
                assertThat(response.getError()).isEqualTo(1);
            }
        }

        @Nested
        class GivenOneInProgress {
            @BeforeEach
            void setup() {
                crawlJob1.setStatus(Status.IN_PROGRESS);
                crawlJob2.setStatus(Status.NOT_STARTED);
                crawlJob3.setStatus(Status.NOT_STARTED);
                crawlJob4.setStatus(Status.NOT_STARTED);

                Job job = new Job(UUID.randomUUID(), Sets.newHashSet(crawlJob1, crawlJob2, crawlJob3, crawlJob4));

                response = responseFactory.createProgress(job);
            }

            @Test
            void thenOneStatusInProgress() {
                assertThat(response.getNotStarted()).isEqualTo(3);
                assertThat(response.getInProgress()).isEqualTo(1);
            }
        }

        @Nested
        class GivenOneComplete {
            @BeforeEach
            void setup() {
                crawlJob1.setStatus(Status.COMPLETE);
                crawlJob2.setStatus(Status.NOT_STARTED);
                crawlJob3.setStatus(Status.NOT_STARTED);
                crawlJob4.setStatus(Status.NOT_STARTED);

                Job job = new Job(UUID.randomUUID(), Sets.newHashSet(crawlJob1, crawlJob2, crawlJob3, crawlJob4));

                response = responseFactory.createProgress(job);
            }

            @Test
            void thenResultIsIdentical() {
                assertThat(response.getNotStarted()).isEqualTo(3);
                assertThat(response.getCompleted()).isEqualTo(1);
            }
        }

        @Nested
        class GivenOneOfEach {
            @BeforeEach
            void setup() {
                crawlJob1.setStatus(Status.NOT_STARTED);
                crawlJob2.setStatus(Status.IN_PROGRESS);
                crawlJob3.setStatus(Status.COMPLETE);
                crawlJob4.setStatus(Status.ERROR);

                Job job = new Job(UUID.randomUUID(), Sets.newHashSet(crawlJob1, crawlJob2, crawlJob3, crawlJob4));

                response = responseFactory.createProgress(job);
            }

            @Test
            void thenResultIsIdentical() {
                assertThat(response.getNotStarted()).isEqualTo(1);
                assertThat(response.getInProgress()).isEqualTo(1);
                assertThat(response.getCompleted()).isEqualTo(1);
                assertThat(response.getError()).isEqualTo(1);
            }
        }
    }
}
