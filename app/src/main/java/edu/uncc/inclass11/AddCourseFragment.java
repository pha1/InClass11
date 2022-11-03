/**
 * In Class 11
 * AddCourseFragment.java
 * Phi Ha
 * Srinath Dittakavi
 */

package edu.uncc.inclass11;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import edu.uncc.inclass11.databinding.FragmentAddCourseBinding;

public class AddCourseFragment extends Fragment {

    final String TAG = "test";
    private FirebaseAuth mAuth;

    public AddCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddCourseBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCourseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String courseNumber = binding.editTextCourseNumber.getText().toString();
                String courseName = binding.editTextCourseName.getText().toString();
                double courseHours = Double.parseDouble(binding.editTextCourseHours.getText().toString());
                int selectedId = binding.radioGroupGrades.getCheckedRadioButtonId();

                if (courseName.isEmpty() || courseNumber.isEmpty() || binding.editTextCourseHours.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }else if (courseHours < 0.0){
                    Toast.makeText(getContext(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
                }else if(selectedId == -1){
                    Toast.makeText(getContext(), "Please select a letter grade !!", Toast.LENGTH_SHORT).show();
                } else {
                    String courseLetterGrade;
                    if(selectedId == R.id.radioButtonA) {
                        courseLetterGrade = "A";
                    } else if(selectedId == R.id.radioButtonB) {
                        courseLetterGrade = "B";
                    } else if(selectedId == R.id.radioButtonC) {
                        courseLetterGrade = "C";
                    } else if(selectedId == R.id.radioButtonD) {
                        courseLetterGrade = "D";
                    } else {
                        courseLetterGrade = "F";
                    }
                    // FirebaseAuth Instance
                    mAuth = FirebaseAuth.getInstance();
                    // Get student id to add into document
                    String student_id = mAuth.getCurrentUser().getUid();

                    // Add the Course
                    addCourse(courseNumber, courseName, courseHours, courseLetterGrade, student_id);
                }
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancel();
            }
        });

    }

    /**
     * This creates a new course given the data by the user
     * @param courseNumber
     * @param courseName
     * @param courseHours
     * @param courseLetterGrade
     * @param student_id
     */
    private void addCourse(String courseNumber, String courseName, double courseHours,
                           String courseLetterGrade, String student_id) {

        // Create the course object
        HashMap<String, Object> course = new HashMap<>();
        course.put("course_grade", courseLetterGrade);
        course.put("course_name", courseName);
        course.put("course_number", courseNumber);
        course.put("credit_hours", courseHours);
        course.put("student_id", student_id);

        // Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Collection Reference
        CollectionReference collRef = db.collection("grades");

        // Create a new document and store it's id
        String id = collRef.document().getId();

        // Add the id to course being created
        course.put("grade_id", id);

        // Set the data to the created document
        db.collection("grades").document(id)
                .set(course)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        mListener.goToGrades();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCourseFragmentListener) {
            mListener = (AddCourseFragmentListener) context;
        }
    }

    AddCourseFragmentListener mListener;

    public interface AddCourseFragmentListener {
        void cancel();
        void goToGrades();
    }
}