package uz.asz.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.asz.myapp.domain.enumeration.Category;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "age")
    private Integer age;

    @Column(name = "language")
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @JsonIgnoreProperties(value = { "student" }, allowSetters = true)
    @OneToOne(mappedBy = "student")
    private Car car;

    @ManyToOne
    @JsonIgnoreProperties(value = { "students" }, allowSetters = true)
    private Instructor instructor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "students", "teacher" }, allowSetters = true)
    private Group group;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Student firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Student lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Student phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getAge() {
        return this.age;
    }

    public Student age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLanguage() {
        return this.language;
    }

    public Student language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Category getCategory() {
        return this.category;
    }

    public Student category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Car getCar() {
        return this.car;
    }

    public Student car(Car car) {
        this.setCar(car);
        return this;
    }

    public void setCar(Car car) {
        if (this.car != null) {
            this.car.setStudent(null);
        }
        if (car != null) {
            car.setStudent(this);
        }
        this.car = car;
    }

    public Instructor getInstructor() {
        return this.instructor;
    }

    public Student instructor(Instructor instructor) {
        this.setInstructor(instructor);
        return this;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Group getGroup() {
        return this.group;
    }

    public Student group(Group group) {
        this.setGroup(group);
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", age=" + getAge() +
            ", language='" + getLanguage() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
