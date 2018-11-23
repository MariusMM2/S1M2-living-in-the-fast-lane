package game.entities;

public final class CourseCollection {
    private CourseCollection() {
    }

    //All courses
    public static final Course
            tradeSchool, electronics, preEngineering, engineering,
            juniorCollege, businessAdmin, academic, graduateSchool,
            postDoctoral, research, publishing;

    //Static initializer block for the courses
    static {
        tradeSchool = new Course("Trade School", 15, 40, null);
        electronics = new Course("Electronics", 20, 50, tradeSchool);
        preEngineering = new Course("Pre-Engineering", 20, 60, tradeSchool);
        engineering = new Course("Engineering", 35, 70, preEngineering);
        juniorCollege = new Course("Junior College", 15, 45, null);
        businessAdmin = new Course("Business Administration", 25, 60, juniorCollege);
        academic = new Course("Academic", 30, 70, juniorCollege);
        graduateSchool = new Course("Graduate School", 45, 70, academic);
        postDoctoral = new Course("Post Doctoral", 50, 100, graduateSchool);
        research = new Course("Research", 65, 125, postDoctoral);
        publishing = new Course("Publishing", 80, 150, research);
    }

    private static final Course[] courseList =
            {
                    tradeSchool, electronics, preEngineering, engineering,
                    juniorCollege, businessAdmin, academic, graduateSchool,
                    postDoctoral, research, publishing
            };

    public static Course[] getCourseList() {
        return courseList;
    }
}
