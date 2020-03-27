package com.ijrobotics.ijschoolnotificationservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UseridMapperTest {

    private UseridMapper useridMapper;

    @BeforeEach
    public void setUp() {
        useridMapper = new UseridMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(useridMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(useridMapper.fromId(null)).isNull();
    }
}
