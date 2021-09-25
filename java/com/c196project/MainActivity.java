package com.c196project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.c196project.guis.RecyclerContext;
import com.c196project.guis.TermAdapter;
import com.c196project.models.Assessment;
import com.c196project.models.Course;
import com.c196project.models.Instructor;
import com.c196project.models.Term;
import com.c196project.utilities.Alerts;
import com.c196project.viewmodels.MainViewModel;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;

    private List<Term> termData = new ArrayList<>();
    private List<Course> courseData = new ArrayList<>();
    private List<Assessment> assessmentData = new ArrayList<>();
    private TermAdapter adapter;
    private MainViewModel viewModel;
    private TextView termStatus, courseStatus, assessmentStatus, instructorStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        ButterKnife.bind(this);
        initViewModel();
        termStatus = findViewById(R.id.status_terms_count);
        courseStatus = findViewById(R.id.status_courses_count);
        assessmentStatus = findViewById(R.id.status_assessments_count);
        instructorStatus = findViewById(R.id.status_mentors_count);
    }

    private void handleAlerts() {
        Log.v("TBUGGER", "We are handling alerts");
        ArrayList<String> alerts = new ArrayList<>();
        Log.v("TBUGGER", "Courses: " + courseData.size() + "\nAssessments: " + assessmentData.size());
        for(Course course: courseData) {
            Log.v("TBUGGER", "We are looping through courses to find due dates");
            if(DateUtils.isToday(course.getStartDate().getTime())) {
                Log.v("DEBUG", "Start date is today.");
                alerts.add("Course " + course.getTitle() + " begins today!");
            } else if(DateUtils.isToday(course.getAnticipatedEndDate().getTime())) {
                Log.v("DEBUG", "End date is today!");
                alerts.add("Course" + course.getTitle() + " ends today!");
            }
        }

        for(Assessment assessment: assessmentData) {
            Log.v("TBUGGER", "We are looping through assessments to find due dates");
            if(DateUtils.isToday(assessment.getDate().getTime())) {
                Log.v("DEBUG", "Assessment due date is today");
                alerts.add("Assessment " + assessment.getTitle() + " is due today!");
            }
        }
        if(alerts.size() > 0) {
            for(String alert: alerts) {
                AlarmManager alarm = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
                Alerts alerting = new Alerts();
                IntentFilter filter = new IntentFilter("ALARM_ACTION");
                registerReceiver(alerting, filter);

                Intent intent = new Intent("ALARM_ACTION");
                intent.putExtra("param", alert);
                PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent, 0);
                alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ Toast.LENGTH_SHORT, operation);
            }
        }
    }

    private void setStatusNumbers(int count, TextView textView) {
        textView.setText(Integer.toString(count));
    }

    private void initViewModel() {
        final Observer<List<Term>> termObserver =
                termEntities -> {
                    termData.clear();
                    termData.addAll(termEntities);
                    setStatusNumbers(termEntities.size(), termStatus);
                    if (adapter == null) {
                        adapter = new TermAdapter(termData, MainActivity.this, RecyclerContext.MAIN);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                };
        final Observer<List<Course>> courseObserver =
                courseEntities -> {
                    courseData.clear();
                    courseData.addAll(courseEntities);
                    setStatusNumbers(courseEntities.size(), courseStatus);
                    handleAlerts();
                };

        final Observer<List<Assessment>> assessmentObserver =
                assessmentEntities -> {
                    assessmentData.clear();
                    assessmentData.addAll(assessmentEntities);
                    setStatusNumbers(assessmentEntities.size(), assessmentStatus);
                    handleAlerts();
                };

        final Observer<List<Instructor>> instructorObserver =
                instructorEntities -> {
                    setStatusNumbers(instructorEntities.size(), instructorStatus);
                };
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.terms.observe(this, termObserver);
        viewModel.courses.observe(this, courseObserver);
        viewModel.assessments.observe(this, assessmentObserver);
        viewModel.instructors.observe(this, instructorObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_sample_data) {
            addSampleData();
            return true;
        } else if (id == R.id.action_delete_all) {
            deleteAllData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        viewModel.deleteAllData();
    }

    private void addSampleData() {
        viewModel.addSampleData();
    }

    public void showTerms(View view) {
        Intent intent = new Intent(this, TermActivity.class);
        startActivity(intent);
    }

    public void showCourses(View view) {
        Intent intent = new Intent(this, CourseActivity.class);
        startActivity(intent);
    }

    public void showAssessments(View view) {
        Intent intent = new Intent(this, AssessmentActivity.class);
        startActivity(intent);
    }
//TODO - change the button name
    @OnClick(R.id.btn_mentors)
    public void showMentors() {
        Intent intent = new Intent(this, InstructorActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int duration = Toast.LENGTH_SHORT;
        Toast toast;

        switch (id) {
            case R.id.nav_terms:
                toast = Toast.makeText(this, "Terms Selected", duration);
                toast.show();
                Intent termIntent = new Intent(this, TermActivity.class);
                startActivity(termIntent);
                break;
            case R.id.nav_courses:
                toast = Toast.makeText(this, "Courses Selected", duration);
                toast.show();
                Intent coursesIntent = new Intent(this, CourseActivity.class);
                startActivity(coursesIntent);
                break;
            case R.id.nav_assessments:
                toast = Toast.makeText(this, "Assessments Selected", duration);
                toast.show();
                Intent AssessmentIntent = new Intent(this, AssessmentActivity.class);
                startActivity(AssessmentIntent);
                break;
                //TODO Change the string to instuctors
            case R.id.nav_instructors:
                toast = Toast.makeText(this, "Mentors Selected", duration);
                toast.show();
                Intent instructorsIntent = new Intent(this, InstructorActivity.class);
                startActivity(instructorsIntent);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}