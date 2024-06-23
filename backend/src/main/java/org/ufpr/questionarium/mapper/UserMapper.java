package org.ufpr.questionarium.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.ufpr.questionarium.dtos.UserData;
import org.ufpr.questionarium.dtos.UserPatch;
import org.ufpr.questionarium.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "creationDateAccount", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User userPatchToUser(UserPatch patch);

    UserData userToUserData(User user);

}
