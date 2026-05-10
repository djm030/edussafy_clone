package com.edussafy.clone.domain.user.api;

import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentors")
public class MentorController {

    private final UserQueryService userQueryService;

    @GetMapping
    public ApiResponse<PageResponse<UserMeResponse>> getMentors(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(userQueryService.getMentors(keyword, region, page, size));
    }

    @GetMapping("/{mentorId}")
    public ApiResponse<UserMeResponse> getMentor(@PathVariable Long mentorId) {
        return ApiResponse.ok(userQueryService.getMentor(mentorId));
    }
}
