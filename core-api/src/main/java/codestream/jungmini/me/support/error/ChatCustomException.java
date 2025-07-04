package codestream.jungmini.me.support.error;

import lombok.Getter;

@Getter
public class ChatCustomException extends RuntimeException {

    private final ErrorType errorType;

    private final Object data;

    public ChatCustomException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public ChatCustomException(ErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }
}
