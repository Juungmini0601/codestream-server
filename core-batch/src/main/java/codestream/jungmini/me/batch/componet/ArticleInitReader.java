package codestream.jungmini.me.batch.componet;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.model.Article;

@Slf4j
@Component
@StepScope // JobParam은 StepScope에서 사용 가능하다.
@RequiredArgsConstructor
public class ArticleInitReader implements ItemReader<Article> {

    private final RandomArticleGenerator articleGenerator;

    @Value("#{jobParameters['totalCount'] ?: 10000}")
    public int totalCount = 10000;

    private int generatedCount = 0;

    @Override
    public Article read() {
        if (generatedCount < totalCount) {
            generatedCount++;

            double progressPercent = (double) generatedCount / totalCount * 100;

            if (generatedCount != 0 && generatedCount % 10000 == 0) {
                log.info(
                        "Article generation progress: {}/{} ({}%)",
                        generatedCount, totalCount, String.format("%.1f", progressPercent));
            }

            return articleGenerator.generateRandomArticle();
        }

        return null;
    }
}
