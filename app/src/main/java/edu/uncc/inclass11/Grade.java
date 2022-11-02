package edu.uncc.inclass11;

public class Grade {
    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_number() {
        return course_number;
    }

    public String getCourse_grade() {
        return course_grade;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "course_name='" + course_name + '\'' +
                ", course_number='" + course_number + '\'' +
                ", credit_hours='" + credit_hours + '\'' +
                ", course_grade='" + course_grade + '\'' +
                ", grade_id='" + grade_id + '\'' +
                ", student_name='" + student_id + '\'' +
                '}';
    }

    String course_name, course_number, course_grade, grade_id, student_id;
    double credit_hours;

    public double getCredit_hours() {
        return credit_hours;
    }


}
