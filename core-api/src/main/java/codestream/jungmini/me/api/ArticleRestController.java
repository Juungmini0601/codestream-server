package codestream.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import codestream.jungmini.me.api.dto.CreateArticleRequest;
import codestream.jungmini.me.model.Article;
import codestream.jungmini.me.service.ArticleService;
import codestream.jungmini.me.support.aop.Admin;
import codestream.jungmini.me.support.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    @Admin
    @PostMapping("/api/v1/articles")
    public ApiResponse<Article> create(@RequestBody @Valid CreateArticleRequest request) {
        Article res = articleService.createArticle(request.toArticle(), request.getTagNames());

        return ApiResponse.success(res);
    }
}
