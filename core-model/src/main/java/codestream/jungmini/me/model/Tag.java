package codestream.jungmini.me.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = "tagId")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private Long tagId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Tag from(final String name) {
        return builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
