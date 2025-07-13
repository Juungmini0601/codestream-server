package codestream.jungmini.me.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import codestream.jungmini.me.api.dto.ImageUploadResponse;
import codestream.jungmini.me.s3.service.S3Service;
import codestream.jungmini.me.support.error.CustomException;
import codestream.jungmini.me.support.error.ErrorType;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Service s3Service;

    public ImageUploadResponse generatePresignedUploadUrl(String filename) {
        String key = generateImageKey(filename);
        String uploadUrl = s3Service.generatePresignedUploadUrl(key, Duration.ofMinutes(5));
        String accessUrl = s3Service.getPublicUrl(key);

        return new ImageUploadResponse(uploadUrl, key, accessUrl);
    }

    public String getImageUrl(String key) {
        return s3Service.getPublicUrl(key);
    }

    private String generateImageKey(String filename) {
        String extension = getFileExtension(filename);
        return "images/" + UUID.randomUUID() + "." + extension;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "파일 확장자가 없습니다.");
        }
        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        validateImageExtension(extension);
        return extension;
    }

    private void validateImageExtension(String extension) {
        if (!isValidImageExtension(extension)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "지원하지 않는 이미지 형식입니다. (jpg, jpeg, png, webp)");
        }
    }

    private boolean isValidImageExtension(String extension) {
        return extension.equals("jpg")
                || extension.equals("jpeg")
                || extension.equals("png")
                || extension.equals("webp");
    }
}
