package game.entities;

/*
Class representing the university courses
 */
public class Course {
    public final String name;
    public final int educationalValue;//Value to be added to player's Education goal
    public final int enrollmentFee; //Fee to be paid when enrolling in the course
    public final Course courseRequired; //Previous course needed

    public Course(String name, int educationalValue, int enrollmentFee, Course courseRequired) {
        this.name = name;
        this.educationalValue = educationalValue;
        this.enrollmentFee = enrollmentFee;
        this.courseRequired = courseRequired;
    }

    @Override
    public String toString() {
        return name;
    }
}
