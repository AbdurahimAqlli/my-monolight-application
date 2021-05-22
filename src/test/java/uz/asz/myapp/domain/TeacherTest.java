package uz.asz.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.asz.myapp.web.rest.TestUtil;

class TeacherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Teacher.class);
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(teacher1.getId());
        assertThat(teacher1).isEqualTo(teacher2);
        teacher2.setId(2L);
        assertThat(teacher1).isNotEqualTo(teacher2);
        teacher1.setId(null);
        assertThat(teacher1).isNotEqualTo(teacher2);
    }
}
