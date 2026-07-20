package com.backend.forum.rest;

import com.backend.forum.mapper.UserMapper;
import com.backend.forum.models.dtos.request.LoginRequest;
import com.backend.forum.models.User;
import com.backend.forum.models.dtos.request.RegisterRequest;
import com.backend.forum.models.dtos.request.UpdateUserRequest;
import com.backend.forum.models.dtos.response.UserResponse;
import com.backend.forum.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public UserRestController(UserService userService,
                              UserMapper userMapper,
                              AuthenticationManager authenticationManager) {

        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userMapper.toEntity(request);

        return userMapper.toResponse(
                userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        ));

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        httpRequest.getSession(true)
                .setAttribute(
                        "SPRING_SECURITY_CONTEXT",
                        context);

        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {

        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);

        return ResponseEntity.ok("Logged out successfully.");
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {

        return userMapper.toResponse(
                userService.getById(id));
    }

    @PutMapping("/profile")
    public UserResponse update(
            @Valid @RequestBody UpdateUserRequest request) {

        return userMapper.toResponse(
                userService.updateProfile(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/block")
    public void block(@PathVariable Long id) {
        userService.block(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/unblock")
    public void unblock(@PathVariable Long id) {
        userService.unblock(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}