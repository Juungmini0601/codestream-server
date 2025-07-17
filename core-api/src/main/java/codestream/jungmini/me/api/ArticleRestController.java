package codestream.jungmini.me.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.ArticleResponse;
import codestream.jungmini.me.api.dto.CreateArticleRequest;
import codestream.jungmini.me.api.dto.UpdateArticleRequest;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.model.ArticleWithDetails;
import codestream.jungmini.me.service.ArticleService;
import codestream.jungmini.me.support.aop.Admin;
import codestream.jungmini.me.support.response.ApiResponse;
import codestream.jungmini.me.support.response.CursorResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    @GetMapping("/api/v1/articles")
    public ApiResponse<CursorResponse<ArticleResponse, Long>> getArticles(
            @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "20") int size) {
        CursorResponse<ArticleWithDetails, Long> articlesWithDetails =
                articleService.getArticlesWithDetails(cursor, size);

        List<ArticleResponse> articleResponses = articlesWithDetails.getData().stream()
                .map(ArticleResponse::from)
                .toList();

        CursorResponse<ArticleResponse, Long> response = CursorResponse.of(
                articleResponses, articlesWithDetails.getNextCursor(), articlesWithDetails.isHasNext());

        return ApiResponse.success(response);
    }

    @GetMapping("/api/v1/articles/{id}")
    public ApiResponse<ArticleResponse> getArticle(@PathVariable Long id) {
        ArticleWithDetails articlesWithDetail = articleService.getArticlesWithDetail(id);

        return ApiResponse.success(ArticleResponse.from(articlesWithDetail));
    }

    @Admin
    @PostMapping("/api/v1/admin/articles")
    public ApiResponse<Article> create(@RequestBody @Valid CreateArticleRequest request) {
        Article res = articleService.createArticle(request.toArticle(), request.getTagNames());
        return ApiResponse.success(res);
    }

    @Admin
    @PutMapping("/api/v1/admin/articles/{id}")
    public ApiResponse<Article> update(@PathVariable Long id, @RequestBody @Valid UpdateArticleRequest request) {
        Article updatedArticle = request.toArticle(id);
        Article result = articleService.updateArticle(id, updatedArticle, request.getTagNames());

        return ApiResponse.success(result);
    }

    @Admin
    @DeleteMapping("/api/v1/admin/articles/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ApiResponse.success(null);
    }
}
