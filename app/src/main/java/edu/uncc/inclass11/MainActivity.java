package edu.uncc.inclass11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
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
}