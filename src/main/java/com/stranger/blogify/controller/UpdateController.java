package com.stranger.blogify.controller;

import com.stranger.blogify.common.ApiResponse;
import com.stranger.blogify.model.Post;
import com.stranger.blogify.service.UpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/newPosts")
public class UpdateController {

    private final UpdateService updateService;


    public UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    @PutMapping("/update-post")
    public ResponseEntity<ApiResponse> updatePost (@RequestBody Post post){
        String newValue = post.getContent();

        if(newValue==null) {
            ApiResponse successResponse = new ApiResponse("Content found empty.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(successResponse);
        }
        boolean updated = updateService.updatePost(newValue, post.getId());

        ApiResponse successResponse = new ApiResponse("Post Updated Successfully.😊😊😊");
        return updated? ResponseEntity.ok(successResponse) :
                ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new ApiResponse("No rows updated.😒😒")) ;
    }
}
