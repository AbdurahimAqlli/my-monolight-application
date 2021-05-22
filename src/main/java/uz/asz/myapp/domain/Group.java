package uz.asz.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uz.asz.myapp.domain.enumeration.Category;
import uz.asz.myapp.domain.enumeration.GroupStatus;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "begin_driving_date")
    private Instant beginDrivingDate;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "telegram_group_link")
    private String telegramGroupLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GroupStatus status;

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "car", "instructor", "group" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "groups" }, allowSetters = true)
    private Teacher teacher;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return this.category;
    }

    public Group category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Instant getBeginDrivingDate() {
        return this.beginDrivingDate;
    }

    public Group beginDrivingDate(Instant beginDrivingDate) {
        this.beginDrivingDate = beginDrivingDate;
        return this;
    }

    public void setBeginDrivingDate(Instant beginDrivingDate) {
        this.beginDrivingDate = beginDrivingDate;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Group startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Group endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getTelegramGroupLink() {
        return this.telegramGroupLink;
    }

    public Group telegramGroupLink(String telegramGroupLink) {
        this.telegramGroupLink = telegramGroupLink;
        return this;
    }

    public void setTelegramGroupLink(String telegramGroupLink) {
        this.telegramGroupLink = telegramGroupLink;
    }

    public GroupStatus getStatus() {
        return this.status;
    }

    public Group status(GroupStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(GroupStatus status) {
        this.status = status;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public Group students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Group addStudent(Student student) {
        this.students.add(student);
        student.setGroup(this);
        return this;
    }

    public Group removeStudent(Student student) {
        this.students.remove(student);
        student.setGroup(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.setGroup(null));
        }
        if (students != null) {
            students.forEach(i -> i.setGroup(this));
        }
        this.students = students;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public Group teacher(Teacher teacher) {
        this.setTeacher(teacher);
        return this;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return id != null && id.equals(((Group) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", category='" + getCategory() + "'" +
            ", beginDrivingDate='" + getBeginDrivingDate() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", telegramGroupLink='" + getTelegramGroupLink() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
