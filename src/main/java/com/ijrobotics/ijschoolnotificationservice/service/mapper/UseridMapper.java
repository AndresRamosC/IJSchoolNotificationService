package com.ijrobotics.ijschoolnotificationservice.service.mapper;


import com.ijrobotics.ijschoolnotificationservice.domain.*;
import com.ijrobotics.ijschoolnotificationservice.service.dto.UseridDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Userid} and its DTO {@link UseridDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UseridMapper extends EntityMapper<UseridDTO, Userid> {


    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "removeNotification", ignore = true)
    @Mapping(target = "notificationSettings", ignore = true)
    Userid toEntity(UseridDTO useridDTO);

    default Userid fromId(Long id) {
        if (id == null) {
            return null;
        }
        Userid userid = new Userid();
        userid.setId(id);
        return userid;
    }
}
