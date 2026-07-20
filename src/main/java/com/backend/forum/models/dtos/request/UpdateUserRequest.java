package com.backend.forum.models.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @Size(min = 4, max = 32)
    private String firstName;

    @Size(min = 4, max = 32)
    private String lastName;

    @Email
    private String email;

    @Size(max = 20)
    private String phoneNumber;
}
