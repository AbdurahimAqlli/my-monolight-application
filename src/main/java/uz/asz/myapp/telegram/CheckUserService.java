package uz.asz.myapp.telegram;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.asz.myapp.domain.Instructor;
import uz.asz.myapp.domain.Student;
import uz.asz.myapp.domain.Teacher;
import uz.asz.myapp.repository.InstructorRepository;
import uz.asz.myapp.repository.StudentRepository;
import uz.asz.myapp.repository.TeacherRepository;

@Service
public class CheckUserService {

    private final Logger log = LoggerFactory.getLogger(CheckUserService.class);

    private final InstructorRepository instructorRepository;

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    public CheckUserService(
        InstructorRepository instructorRepository,
        StudentRepository studentRepository,
        TeacherRepository teacherRepository
    ) {
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public boolean checkInstructorInDb(long chatId) {
        Optional<Instructor> optionalInstructor = instructorRepository.findByChatId(String.valueOf(chatId));
        log.info("optionalInstructor {}", optionalInstructor);
        return optionalInstructor.isPresent();
    }

    public boolean checkStudentInDb(long chatId) {
        Optional<Student> optionalStudent = studentRepository.findFirstByChatId(String.valueOf(chatId));
        log.info("optionalStudent {}", optionalStudent);
        return optionalStudent.isPresent();
    }

    public boolean checkTeacherInDb(long chatId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findByChatId(String.valueOf(chatId));
        log.info("optionalTeacher {}", optionalTeacher);
        return optionalTeacher.isPresent();
    }
}
