package com.ijrobotics.ijschoolnotificationservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ijrobotics.ijschoolnotificationservice.web.rest.TestUtil;

public class UseridTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Userid.class);
        Userid userid1 = new Userid();
        userid1.setId(1L);
        Userid userid2 = new Userid();
        userid2.setId(userid1.getId());
        assertThat(userid1).isEqualTo(userid2);
        userid2.setId(2L);
        assertThat(userid1).isNotEqualTo(userid2);
        userid1.setId(null);
        assertThat(userid1).isNotEqualTo(userid2);
    }
}
