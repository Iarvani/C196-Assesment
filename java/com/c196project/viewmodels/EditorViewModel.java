package com.c196project.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.c196project.databases.ApplicationRepository;
import com.c196project.models.Assessment;
import com.c196project.models.AssessmentType;
import com.c196project.models.Course;
import com.c196project.models.CourseStatus;
import com.c196project.models.Instructor;
import com.c196project.models.Term;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {
    public MutableLiveData<Term> activeTerm = new MutableLiveData<>();
    public MutableLiveData<Course> activeCourse = new MutableLiveData<>();
    public MutableLiveData<Assessment> activeAssessment = new MutableLiveData<>();
    public MutableLiveData<Instructor> activeInstructor = new MutableLiveData<>();
    public LiveData<List<Term>> terms;
    public LiveData<List<Course>> courses;
    public LiveData<List<Assessment>> assessments;
    public LiveData<List<Instructor>> instructors;
    private ApplicationRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        repository = ApplicationRepository.getInstance(getApplication());
        terms = repository.terms;
        courses = repository.courses;
        assessments = repository.assessments;
        instructors = repository.instructors;
    }

    public void loadTermData(final int termId) {
        executor.execute(() -> {
            Term term = repository.getTermById(termId);
            activeTerm.postValue(term);
        });
    }

    public void loadCourseData(final int courseId) {
        executor.execute(() -> {
            Course course = repository.getCourseById(courseId);
            activeCourse.postValue(course);
        });
    }

    public void loadAssessmentData(final int assessmentId) {
        executor.execute(() -> {
            Assessment assessment = repository.getAssessmentById(assessmentId);
            activeAssessment.postValue(assessment);
        });
    }

    public void loadInstructorData(final int mentorId) {
        executor.execute(() -> {
            Instructor instructor = repository.getInstructorById(mentorId);
            activeInstructor.postValue(instructor);
        });
    }

    public void saveTerm(String termTitle, Date startDate, Date endDate) {
        Term term = activeTerm.getValue();

        if (term == null) {
            if (TextUtils.isEmpty(termTitle.trim())) {
                return;
            }
            term = new Term(termTitle.trim(), startDate, endDate);
        } else {
            term.setTitle(termTitle.trim());
            term.setStartDate(startDate);
            term.setEndDate(endDate);
        }
        repository.insertTerm(term);
    }

    public void saveCourse(String courseTitle, Date startDate, Date endDate, CourseStatus courseStatus, int termId, String note) {
        Course course = activeCourse.getValue();

        if(course == null) {
            if (TextUtils.isEmpty(courseTitle.trim())) {
                return;
            }
            course = new Course(courseTitle.trim(), startDate, endDate, courseStatus, termId, note);
        } else {
            course.setTitle(courseTitle.trim());
            course.setStartDate(startDate);
            course.setAnticipatedEndDate(endDate);
            course.setCourseStatus(courseStatus);
            course.setTermId(termId);
            course.setNote(note);
        }
        repository.insertCourse(course);
    }

    public void overwriteCourse(Course course, int termId) {
        course.setTermId(termId);
        repository.insertCourse(course);
    }

    public void overwriteAssessment(Assessment assessment, int courseId) {
        assessment.setCourseId(courseId);
        repository.insertAssessment(assessment);
    }

    public void overwriteInstructor(Instructor instructor, int courseId) {
        instructor.setCourseId(courseId);
        repository.insertInstructor(instructor);
    }

    public void saveAssessment(String assessmentTitle, Date date, AssessmentType assessmentType, int courseId) {
        Assessment assessment = activeAssessment.getValue();

        if(assessment == null) {
            if(TextUtils.isEmpty(assessmentTitle.trim())) {
                return;
            }
            assessment = new Assessment(assessmentTitle.trim(), date, assessmentType, courseId);
        } else {
            assessment.setTitle(assessmentTitle.trim());
            assessment.setDate(date);
            assessment.setAssessmentType(assessmentType);
            assessment.setCourseId(courseId);
        }
        repository.insertAssessment(assessment);
    }

    public void saveInstructor(String name, String email, String phone, int courseId) {
        Instructor instructor = activeInstructor.getValue();

        if(instructor == null) {
            if(TextUtils.isEmpty(name.trim())) {
                return;
            }
            instructor = new Instructor(name, email, phone, courseId);
        } else {
            instructor.setName(name);
            instructor.setEmail(email);
            instructor.setPhone(phone);
            instructor.setCourseId(courseId);
        }
        repository.insertInstructor(instructor);
    }

    public void deleteTerm() {
        repository.deleteTerm(activeTerm.getValue());
    }

    public void deleteCourse() {
        repository.deleteCourse(activeCourse.getValue());
    }

    public void deleteAssessment() {
        repository.deleteAssessment(activeAssessment.getValue());
    }

    public void deleteInstructor(){
        repository.deleteInstructor(activeInstructor.getValue());
    }

    public LiveData<List<Course>> getCoursesInTerm(int termId) {
        return (repository.getCoursesByTerm(termId));
    }

    public LiveData<List<Assessment>> getAssessmentsInCourse(int courseId) {
        return (repository.getAssessmentsByCourse(courseId));
    }

    public LiveData<List<Instructor>> getInstructorsInCourse(int courseId) {
        return (repository.getInstructorsByCourse(courseId));
    }

    public LiveData<List<Course>> getUnassignedCourses() {
        return (repository.getCoursesByTerm(-1));
    }

    public LiveData<List<Assessment>> getUnassignedAssessments() {
        return (repository.getAssessmentsByCourse(-1));
    }

    public LiveData<List<Instructor>> getUnassignedInstructors() {
        return (repository.getInstructorsByCourse(-1));
    }
}