package codestream.jungmini.me.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.api.dto.ImageUploadResponse;
import codestream.jungmini.me.service.ImageService;
import codestream.jungmini.me.support.aop.Admin;
import codestream.jungmini.me.support.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @Admin
    @GetMapping("/api/v1/images/upload-url")
    public ApiResponse<ImageUploadResponse> getPresignedUrl(@RequestParam String filename) {
        ImageUploadResponse uploadInfo = imageService.generatePresignedUploadUrl(filename);
        return ApiResponse.success(uploadInfo);
    }
}
