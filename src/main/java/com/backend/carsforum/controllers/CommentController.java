package com.backend.carsforum.controllers;

import com.backend.carsforum.models.Comment;
import com.backend.carsforum.models.Post;
import com.backend.carsforum.models.dtos.request.CreateCommentRequest;
import com.backend.carsforum.services.CommentService;
import com.backend.carsforum.services.LikeService;
import com.backend.carsforum.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final LikeService likeService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, LikeService likeService) {
        this.commentService = commentService;
        this.postService = postService;
        this.likeService = likeService;
    }


    @PostMapping("/create/{postId}")
    public String create(
            @PathVariable Long postId,
            @Valid @ModelAttribute CreateCommentRequest request,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            Post post = postService.getById(postId);

            model.addAttribute("post", post);

            model.addAttribute("liked",
                    likeService.isLiked(postId));

            return "posts/details";
        }
        Comment comment = new Comment();

        comment.setContent(request.getContent());

        Post post = new Post();
        post.setId(postId);

        comment.setPost(post);

        commentService.create(comment);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/reply/{commentId}")
    public String reply(
            @PathVariable Long commentId,
            @RequestParam Long postId,
            @Valid @ModelAttribute CreateCommentRequest request,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            return "redirect:/posts/" + postId;
        }

        Comment reply = new Comment();

        reply.setContent(request.getContent());

        commentService.reply(commentId, reply);

        return "redirect:/posts/" + postId;
    }
}