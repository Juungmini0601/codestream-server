package codestream.jungmini.me.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.repository.ArticleRepository;
import codestream.jungmini.me.database.repository.TagRepository;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.Tag;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Article createArticle(Article article, Set<String> tagNames) {
        List<Tag> tags = tagRepository.findOrCreateTags(tagNames);

        articleRepository.save(article);
        tagRepository.saveArticleTags(article.getArticleId(), tags);

        return article;
    }
}
