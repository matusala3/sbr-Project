package com.matikif.sbrproject.service;

import com.matikif.sbrproject.exception.StudentAlreadyExistException;
import com.matikif.sbrproject.exception.StudentNotFoundException;
import com.matikif.sbrproject.model.Student;
import com.matikif.sbrproject.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService{

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Student addStudent(Student student) {
        if(studentAlreadyExists(student.getEmail())){
            throw new StudentAlreadyExistException(student.getEmail() + "student already exists");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student, Long id) {
        return studentRepository.findById(id).map(st ->{
            st.setFirstName(student.getFirstName());
            st.setLastName(student.getLastName());
            st.setEmail(student.getEmail());
            st.setDepartment(student.getDepartment());
            return studentRepository.save(st);
        }).orElseThrow(() -> new StudentNotFoundException("Sorry, This student couldn't be found"));
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Sorry, Student with the id of " + id + " couldn't be found"));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)){
            throw new StudentNotFoundException("Sorry, Student with the id of " + id + " couldn't be found");
        }
        studentRepository.deleteById(id);
    }

    private boolean studentAlreadyExists(String email){
        return studentRepository.findByEmail(email).isPresent();
    }
}
