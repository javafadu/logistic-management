package com.logistic.dto.mapper;

import com.logistic.domain.User;
import com.logistic.dto.UserDTO;
import com.logistic.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    UserDTO userToUserDTO(User user);

    List<UserDTO> userListToUserDTOList(List<User> user);

}
