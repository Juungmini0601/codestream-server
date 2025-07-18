package codestream.jungmini.me.batch.componet;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import codestream.jungmini.me.database.repository.CategoryRepository;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.Category;

@Component
public class RandomArticleGenerator {

    private final Random random = new Random();
    private final CategoryRepository categoryRepository;

    private final List<String> sampleTitles = List.of(
            "효율적인 개발을 위한 팁",
            "성능 최적화 가이드",
            "모던 개발 방법론",
            "클린 코드 작성하기",
            "디자인 패턴 활용법",
            "테스트 주도 개발",
            "마이크로서비스 아키텍처",
            "데이터베이스 설계 원칙",
            "보안 개발 가이드",
            "운영환경 배포 전략");

    private final List<String> sampleAuthors =
            List.of("김개발", "이개발", "박개발", "최개발", "정개발", "강백엔드", "송프론트", "윤데브옵스", "임아키텍트", "한테크리드");

    private final List<String> sampleDescriptions = List.of(
            "실무에서 바로 적용할 수 있는 개발 노하우를 공유합니다.",
            "효율적인 코드 작성을 위한 베스트 프랙티스를 알아보세요.",
            "최신 기술 트렌드와 실제 적용 사례를 소개합니다.",
            "개발 생산성 향상을 위한 도구와 방법론을 다룹니다.",
            "실제 프로젝트 경험을 바탕으로 한 인사이트를 제공합니다.");
    private List<Category> categories;

    public RandomArticleGenerator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void loadCategories() {
        this.categories = categoryRepository.findCategories(0L, 10);
    }

    public Article generateRandomArticle() {
        Category randomCategory = categories.get(random.nextInt(categories.size()));

        return Article.from(
                getRandomTitle(),
                getRandomAuthor(),
                getRandomDescription(),
                generateThumbnailUrl(),
                generateLink(),
                randomCategory.getCategoryId());
    }

    private String getRandomTitle() {
        return sampleTitles.get(random.nextInt(sampleTitles.size())) + " " + (random.nextInt(100) + 1);
    }

    private String getRandomAuthor() {
        return sampleAuthors.get(random.nextInt(sampleAuthors.size()));
    }

    private String getRandomDescription() {
        return sampleDescriptions.get(random.nextInt(sampleDescriptions.size()));
    }

    private String generateThumbnailUrl() {
        return "https://example.com/thumbnails/thumb_" + (random.nextInt(1000) + 1) + ".jpg";
    }

    private String generateLink() {
        return "https://example.com/articles/" + UUID.randomUUID();
    }
}
