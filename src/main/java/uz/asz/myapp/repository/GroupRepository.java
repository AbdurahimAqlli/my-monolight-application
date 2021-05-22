package uz.asz.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.asz.myapp.domain.Group;

/**
 * Spring Data SQL repository for the Group entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {}
