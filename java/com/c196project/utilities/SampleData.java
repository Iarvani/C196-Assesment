package com.c196project.utilities;

import com.c196project.models.Assessment;
import com.c196project.models.AssessmentType;
import com.c196project.models.Course;
import com.c196project.models.CourseStatus;
import com.c196project.models.Instructor;
import com.c196project.models.Term;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleData {
    private static final String SAMPLE_TITLE = "Term";
    private static final String SAMPLE_COURSE_TITLE = "Course";
    private static final String SAMPLE_ASSESSMENT_TITLE = "Assessment";

    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND, diff);
        return cal.getTime();
    }

    public static List<Term> getTerms() {
        List<Term> terms = new ArrayList<>();
        terms.add(new Term(10000, SAMPLE_TITLE + " 1", getDate(0), (getDate(10))));
        terms.add(new Term(10001, SAMPLE_TITLE + " 2", getDate(-100), (getDate(10))));
        terms.add(new Term(10002, SAMPLE_TITLE + " 3", getDate(-1000), (getDate(10))));
        terms.add(new Term(10003, SAMPLE_TITLE + " 4", getDate(-10000), (getDate(10))));
        return terms;
    }

    public static List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(10000, SAMPLE_COURSE_TITLE + " 1", getDate(0), (getDate(10)), CourseStatus.IN_PROGRESS, 10000, "Test Note 1"));
        courses.add(new Course(10001, SAMPLE_COURSE_TITLE + " 2", getDate(-100), (getDate(10)), CourseStatus.IN_PROGRESS, 10001, "Test Note 2"));
        courses.add(new Course(10002, SAMPLE_COURSE_TITLE + " 3", getDate(-1000), (getDate(10)), CourseStatus.IN_PROGRESS, 10002, "Test Note 3"));
        courses.add(new Course(10003, SAMPLE_COURSE_TITLE + " 4", getDate(-10000), (getDate(10)), CourseStatus.IN_PROGRESS, 10002, "Test Note 4"));
        courses.add(new Course(10004, SAMPLE_COURSE_TITLE + " 5", getDate(-100000), (getDate(10)), CourseStatus.IN_PROGRESS, 10003, "Test Note 5"));
        return courses;
    }

    public static List<Assessment> getAssessments() {
        List<Assessment> assessments = new ArrayList<>();
        assessments.add(new Assessment(SAMPLE_ASSESSMENT_TITLE + " 1", getDate(0), AssessmentType.OA, 10000));
        assessments.add(new Assessment(SAMPLE_ASSESSMENT_TITLE + " 2", getDate(-100), AssessmentType.PA, 10000));
        assessments.add(new Assessment(SAMPLE_ASSESSMENT_TITLE + " 3", getDate(-1000), AssessmentType.OA, 10001));
        assessments.add(new Assessment(SAMPLE_ASSESSMENT_TITLE + " 4", getDate(-10000), AssessmentType.OA, 10002));
        assessments.add(new Assessment(SAMPLE_ASSESSMENT_TITLE + " 5", getDate(-100000), AssessmentType.PA, 10002));
        return assessments;
    }

    public static List<Instructor> getInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor("Jack Nicholes", "JNicholes@gmail.com", "801-335-2299", 10000));
        instructors.add(new Instructor("Ace Ventura", "PetDetective01@gmail.com", "555-343-2221", 10001));
        instructors.add(new Instructor("Able Cable", "Cableguy@gmail.com", "385-232-1232", 10002));
        instructors.add(new Instructor("Rid dick", "Dark3y3s@gmail.com", "909-423-3456", 10003));
        return instructors;
    }

}
