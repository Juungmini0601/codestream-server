package codestream.jungmini.me.batch.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.batch.componet.ArticleInitReader;
import codestream.jungmini.me.database.repository.ArticleRepository;
import codestream.jungmini.me.model.Article;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ArticleInitBatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ArticleInitReader articleInitReader;
    private final ArticleRepository articleRepository;

    @Bean
    public Job initArticlesJob() {
        return new JobBuilder("initArticlesJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // 작업을 여러번 실행 시키기 위함
                .start(initArticlesStep())
                .build();
    }

    @Bean
    public Step initArticlesStep() {
        return new StepBuilder("initArticlesStep", jobRepository)
                .<Article, Article>chunk(10000, new ResourcelessTransactionManager())
                .reader(articleInitReader)
                .writer(articleRepositoryWriter())
                .build();
    }

    @Bean
    public ItemWriter<Article> articleRepositoryWriter() {
        return chunk -> {
            List<Article> articles =
                    chunk.getItems().stream().map(Article.class::cast).toList();

            articleRepository.saveAll(articles);
        };
    }
}
