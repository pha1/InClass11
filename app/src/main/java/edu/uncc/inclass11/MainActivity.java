/**
 * In Class 11
 * MainActivity.java
 * Phi Ha
 * Srinath Dittakavi
 */

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

        // Get instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Check if there is a user logged in
        // If there is not go to Login Fragment
        if (mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        }
        // If there is a user logged in, go to Grades Fragment
        else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new GradesFragment())
                    .commit();
        }
    }

    /**
     * Go to Create New Account from Login Page
     */
    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment(), "Sign Up")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Go to Grades Fragment
     */
    @Override
    public void goToGrades() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new GradesFragment(), "Grades")
                .commit();
    }

    /**
     * Go back to Login page from Sign up
     */
    @Override
    public void login() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * Go to Add New Course Page
     */
    @Override
    public void addNewCourse() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new AddCourseFragment(), "Add Course")
                .addToBackStack(null)
                .commit();
    }

    /**
     * Logout
     */
    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment(), "Login")
                .commit();
    }

    /**
     * Cancel action
     * Go back to previous page
     */
    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }
}