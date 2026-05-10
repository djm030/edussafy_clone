package com.edussafy.clone.domain.user.dto.mapper;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserMeResponse toMeResponse(User user);
}
