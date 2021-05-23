package uz.asz.myapp.repository;

import java.util.Optional;
import javax.swing.text.html.Option;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.asz.myapp.domain.Student;

/**
 * Spring Data SQL repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findFirstByChatId(@NotNull String chatId);
}
