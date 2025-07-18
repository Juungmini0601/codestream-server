package codestream.jungmini.me.batch.componet;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.repository.CategoryRepository;
import codestream.jungmini.me.model.Category;

@Component
@RequiredArgsConstructor
public class CategoryInitTasklet implements Tasklet {

    private final CategoryRepository categoryRepository;
    private final List<String> categories =
            List.of("Java", "Spring", "Database", "Linux", "Docker", "Kubernetes", "AWS");

    @Override
    public RepeatStatus execute(@NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) {
        categories.stream().map(Category::from).forEach(categoryRepository::save);

        return RepeatStatus.FINISHED;
    }
}
