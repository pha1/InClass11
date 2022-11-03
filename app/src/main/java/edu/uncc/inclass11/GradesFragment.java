/**
 * In Class 11
 * GradesFragment.java
 * Phi Ha
 * Srinath Dittakavi
 */

package edu.uncc.inclass11;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.inclass11.databinding.FragmentGradesBinding;
import edu.uncc.inclass11.databinding.GradeRowItemBinding;

public class GradesFragment extends Fragment {

    final String TAG = "test";
    private FirebaseAuth mAuth;

    public GradesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    FragmentGradesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGradesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.grades);

        // Get Data and populate the Array
        getData();

        binding.recyclerViewGrades.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GradesAdapter();
        binding.recyclerViewGrades.setAdapter(adapter);
    }

    /**
     * Get the database and update the UI of Grades Fragment if any changes occurs in the database
     */
    private void getData() {

        // Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Grades Collection
        db.collection("grades")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        // Get the documents and create Grade Objects
                        mGrades.clear();
                        for (QueryDocumentSnapshot document: value) {
                            mAuth = FirebaseAuth.getInstance();
                            if (document.getString("student_id").equals(mAuth.getCurrentUser().getUid())) {
                                Grade grade = document.toObject(Grade.class);
                                mGrades.add(grade);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        double gpa;
                        double hours;
                        double total_grade_points = 0.0;
                        double credit_points;
                        double total_hours = 0.0;

                        for (int i = 0; i < mGrades.size(); i++) {
                            // Hours
                            hours = mGrades.get(i).credit_hours;

                            // Check which letter grade/point worth
                            switch (mGrades.get(i).course_grade) {
                                case "A":
                                    credit_points = 4.0;
                                    break;
                                case "B":
                                    credit_points = 3.0;
                                    break;
                                case "C":
                                    credit_points = 2.0;
                                    break;
                                case "D":
                                    credit_points = 1.0;
                                    break;
                                default:
                                    credit_points = 0.0;
                                    break;
                            }
                            // Get the total grade points (credit hours * Letter Grade)
                            total_grade_points += hours * credit_points;
                            total_hours += hours;
                        }
                        Log.d(TAG, "onEvent: " + mGrades.size());
                        if (total_hours > 0.0){
                            gpa = total_grade_points/total_hours;
                        } else {
                            gpa = 4.0;
                        }
                        // GPA = Total Grade Points/Total Credit Hours

                        binding.textViewCreditHours.setText("Hours: " + total_hours);
                        binding.textViewGPA.setText("GPA: " + String.format("%.2f", gpa));
                    }
                });
    }
    
    GradesAdapter adapter;
    ArrayList<Grade> mGrades = new ArrayList<>();
    
    class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradesViewHolder> {

        @NonNull
        @Override
        public GradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GradeRowItemBinding binding = GradeRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new GradesViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull GradesViewHolder holder, int position) {
            Grade grade = mGrades.get(position);
            holder.setupUI(grade);
        }

        @Override
        public int getItemCount() {
            return mGrades.size();
        }

        class GradesViewHolder extends RecyclerView.ViewHolder {

            GradeRowItemBinding mBinding;
            Grade mGrade;
            
            public GradesViewHolder(GradeRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            /**
             * Set up the UI of each item
             * @param grade the Grade Object to be displayed
             */
            public void setupUI(Grade grade) {
                mGrade = grade;
                mBinding.textViewCourseHours.setText(String.format("%.0f", grade.credit_hours) + " Credit Hours");
                mBinding.textViewCourseLetterGrade.setText(grade.course_grade);
                mBinding.textViewCourseName.setText(grade.course_name);
                mBinding.textViewCourseNumber.setText(grade.course_number);

                // Delete Button
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "onClick: Delete Success");
                        delete(grade.grade_id);
                    }
                });
            }
        }
    }

    /**
     * This method deletes a course from the database
     * @param grade_id The id of the document of the selected course
     */
    private void delete(String grade_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("grades").document(grade_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Delete Successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    /**
     * This determines which action to take when clicking the buttons in the action bar
     * @param item Which item was clicked
     * @return boolean value
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:  {
                // navigate to Add Course screen
                mListener.addNewCourse();
                return true;
            }
            case R.id.logout:   {
                // Logout
                mListener.logout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GradesFragmentListener) {
            mListener = (GradesFragmentListener) context;
        }
    }

    GradesFragmentListener mListener;

    public interface GradesFragmentListener {
        void addNewCourse();
        void logout();
    }
}