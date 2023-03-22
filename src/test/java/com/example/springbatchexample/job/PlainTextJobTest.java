package com.example.springbatchexample.job;

import com.example.springbatchexample.BatchTestConfig;
import com.example.springbatchexample.core.domain.PlainText;
import com.example.springbatchexample.core.repository.PlainTextRepository;
import com.example.springbatchexample.core.repository.ResultTextRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {PlainTextJobConfig.class, BatchTestConfig.class})
public class PlainTextJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private PlainTextRepository plainTextRepository;
    @Autowired
    private ResultTextRepository resultTextRepository;

    private final int COUNT = 10;

    @AfterEach
    public void truncate() {
        resultTextRepository.deleteAll();
    }

    /**
     * Given - PlainText 데이터가 없을 때
     * When - PlainTextJob을 실행하면
     * Then - ResultText 테이블에 데이터가 생성되지 않는다
     **/
    @Test
    public void noDataTest() throws Exception {
        // given

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
        Assertions.assertEquals(0, resultTextRepository.count());
    }

    /**
     * Given - PlainText 데이터가 있을 때
     * When - PlainTextJob을 실행하면
     * Then - ResultText 테이블에 데이터가 PlainText만큼 생성된다
     **/
    @Test
    public void existDataTest() throws Exception {
        // given
        givenPlainTexts(COUNT);

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
        Assertions.assertEquals(COUNT, resultTextRepository.count());
    }

    private void givenPlainTexts(Integer count) {
        IntStream.range(0, count)
                .forEach(
                        num -> plainTextRepository.save(new PlainText(null, "text - " + num))
                );
    }
}
