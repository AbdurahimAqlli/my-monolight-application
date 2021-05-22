package uz.asz.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.asz.myapp.domain.Teacher;

/**
 * Spring Data SQL repository for the Teacher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {}
