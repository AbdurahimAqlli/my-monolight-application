package uz.asz.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.asz.myapp.domain.Student;

/**
 * Spring Data SQL repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {}
