package com.backend.carsforum.models.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    @NotNull
    private Long postId;

    @NotBlank(message = "Comment cannot be empty.")
    @Size(
            min = 32,
            max = 8192,
            message = "Comment must be between 32 and 8192 characters.")
    private String content;
}
