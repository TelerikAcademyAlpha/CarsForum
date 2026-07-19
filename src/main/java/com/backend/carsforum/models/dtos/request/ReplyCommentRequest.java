package com.backend.carsforum.models.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentRequest {

    @NotBlank(message = "Reply cannot be empty.")

    @Size(
            min = 32,
            max = 8192,
            message = "Reply must be between 32 and 8192 characters.")
    private String content;
}
