package codestream.jungmini.me.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.batch.componet.CategoryInitTasklet;

@Configuration
@RequiredArgsConstructor
public class CategoryInitBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job initCategoryJob(Step initCategoryStep) {
        return new JobBuilder("initCategoryJob", jobRepository)
                .start(initCategoryStep)
                .build();
    }

    @Bean
    public Step initCategoryStep(CategoryInitTasklet categoryInitTasklet) {
        return new StepBuilder("initCategoryStep", jobRepository)
                .tasklet(categoryInitTasklet, transactionManager)
                .build();
    }
}
