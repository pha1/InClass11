package edu.uncc.inclass11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, GradesFragment.GradesFragmentListener, AddCourseFragment.AddCourseFragmentListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new GradesFragment())
                    .commit();
        }
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment(), "Sign Up")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToGrades() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new GradesFragment(), "Grades")
                .commit();
    }

    @Override
    public void login() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void addNewCourse() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AddCourseFragment(), "Add Course")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment(), "Login")
                .commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }
}