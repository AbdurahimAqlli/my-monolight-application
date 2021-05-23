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

    @Column(name = "name")
    private String name;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "study_time")
    private String studyTime;

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

    public String getName() {
        return this.name;
    }

    public Student name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public Student contactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getChatId() {
        return this.chatId;
    }

    public Student chatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getStudyTime() {
        return this.studyTime;
    }

    public Student studyTime(String studyTime) {
        this.studyTime = studyTime;
        return this;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
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
            ", name='" + getName() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", chatId='" + getChatId() + "'" +
            ", studyTime='" + getStudyTime() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
