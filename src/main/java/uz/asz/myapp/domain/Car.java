package uz.asz.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.asz.myapp.domain.enumeration.Category;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "car_owner_name")
    private String carOwnerName;

    @Column(name = "year_of_car")
    private Instant yearOfCar;

    @Column(name = "color")
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @JsonIgnoreProperties(value = { "car", "instructor", "group" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car id(Long id) {
        this.id = id;
        return this;
    }

    public String getModel() {
        return this.model;
    }

    public Car model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCarOwnerName() {
        return this.carOwnerName;
    }

    public Car carOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
        return this;
    }

    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }

    public Instant getYearOfCar() {
        return this.yearOfCar;
    }

    public Car yearOfCar(Instant yearOfCar) {
        this.yearOfCar = yearOfCar;
        return this;
    }

    public void setYearOfCar(Instant yearOfCar) {
        this.yearOfCar = yearOfCar;
    }

    public String getColor() {
        return this.color;
    }

    public Car color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Category getCategory() {
        return this.category;
    }

    public Car category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Student getStudent() {
        return this.student;
    }

    public Car student(Student student) {
        this.setStudent(student);
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", carOwnerName='" + getCarOwnerName() + "'" +
            ", yearOfCar='" + getYearOfCar() + "'" +
            ", color='" + getColor() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
