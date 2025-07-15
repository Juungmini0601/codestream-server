package codestream.jungmini.me.database.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.database.mapper.TagMapper;
import codestream.jungmini.me.model.ArticleTag;
import codestream.jungmini.me.model.Tag;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final TagMapper tagMapper;

    @Transactional
    public List<Tag> findOrCreateTags(Set<String> tagNames) {
        if (tagNames.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> tagNameList = new ArrayList<>(tagNames);

        List<Tag> existingTags = tagMapper.findByNames(tagNameList);

        List<String> existingTagNames = existingTags.stream().map(Tag::getName).toList();

        List<Tag> newTags = tagNameList.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(Tag::from)
                .toList();

        if (!newTags.isEmpty()) {
            tagMapper.saveAll(newTags);
        }

        List<Tag> allTags = new ArrayList<>(existingTags);
        allTags.addAll(newTags);

        return allTags;
    }

    @Transactional
    public void saveArticleTags(Long articleId, List<Tag> tags) {
        List<ArticleTag> articleTags = tags.stream()
                .map(tag -> ArticleTag.from(articleId, tag.getTagId()))
                .toList();

        if (!articleTags.isEmpty()) {
            tagMapper.saveArticleTags(articleTags);
        }
    }

    @Transactional
    public void deleteArticleTags(Long articleId) {
        tagMapper.deleteArticleTags(articleId);
    }
}
