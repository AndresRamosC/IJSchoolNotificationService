package com.ijrobotics.ijschoolnotificationservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ijrobotics.ijschoolnotificationservice.web.rest.TestUtil;

public class UseridDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UseridDTO.class);
        UseridDTO useridDTO1 = new UseridDTO();
        useridDTO1.setId(1L);
        UseridDTO useridDTO2 = new UseridDTO();
        assertThat(useridDTO1).isNotEqualTo(useridDTO2);
        useridDTO2.setId(useridDTO1.getId());
        assertThat(useridDTO1).isEqualTo(useridDTO2);
        useridDTO2.setId(2L);
        assertThat(useridDTO1).isNotEqualTo(useridDTO2);
        useridDTO1.setId(null);
        assertThat(useridDTO1).isNotEqualTo(useridDTO2);
    }
}
