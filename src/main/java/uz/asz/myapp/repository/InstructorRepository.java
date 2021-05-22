package uz.asz.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.asz.myapp.domain.Instructor;

/**
 * Spring Data SQL repository for the Instructor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {}
