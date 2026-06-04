package it.exprivia.mapper;


import it.exprivia.models.dtos.user.UserDTO;
import it.exprivia.models.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "jakarta",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> usersEnt);

    List<User> toEntityList(List<UserDTO> usersDTOs);
}
