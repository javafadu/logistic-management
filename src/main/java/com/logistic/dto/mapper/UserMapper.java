package com.logistic.dto.mapper;

import com.logistic.domain.User;
import com.logistic.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

}
