package uz.asz.myapp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.asz.myapp.domain.Car;

/**
 * Spring Data SQL repository for the Car entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {}
