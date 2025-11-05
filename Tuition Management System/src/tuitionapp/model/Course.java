package tuitionapp.model;
/**
 * Course class represents a course/subject offered in the tuition center.
 * This class holds all the data related to a course.
 */
public class Course {
    // Private fields (encapsulation)
    private String courseId;
    private String courseName;
    private String description;
    private String instructor;
    private double courseFee;
    private int duration; // in months
    private String schedule; // e.g., "Mon, Wed, Fri - 3:00 PM to 5:00 PM"
    private int maxStudents;
    private int enrolledStudents;

    // Constructor with all fields
    public Course(String courseId, String courseName, String description, String instructor,
                  double courseFee, int duration, String schedule, int maxStudents, int enrolledStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
        this.courseFee = courseFee;
        this.duration = duration;
        this.schedule = schedule;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
    }

    // Constructor without enrolled students count (for new courses)
    public Course(String courseId, String courseName, String description, String instructor,
                  double courseFee, int duration, String schedule, int maxStudents) {
        this(courseId, courseName, description, instructor, courseFee, duration, schedule, maxStudents, 0);
    }

    // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructor() {
        return instructor;
    }

    public double getCourseFee() {
        return courseFee;
    }

    public int getDuration() {
        return duration;
    }

    public String getSchedule() {
        return schedule;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public int getEnrolledStudents() {
        return enrolledStudents;
    }

    // Setters
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    // Business methods
    public int getAvailableSeats() {
        return maxStudents - enrolledStudents;
    }

    public boolean hasAvailableSeats() {
        return enrolledStudents < maxStudents;
    }

    public boolean enrollStudent() {
        if (hasAvailableSeats()) {
            enrolledStudents++;
            return true;
        }
        return false;
    }

    public boolean unenrollStudent() {
        if (enrolledStudents > 0) {
            enrolledStudents--;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return enrolledStudents >= maxStudents;
    }

    public double getOccupancyRate() {
        if (maxStudents == 0) return 0.0;
        return (enrolledStudents * 100.0) / maxStudents;
    }

    // toString method for easy display
    @Override
    public String toString() {
        return String.format(
                "Course ID: %s\n" +
                        "Course Name: %s\n" +
                        "Description: %s\n" +
                        "Instructor: %s\n" +
                        "Course Fee: Rs. %.2f\n" +
                        "Duration: %d months\n" +
                        "Schedule: %s\n" +
                        "Capacity: %d/%d students (%.1f%% full)\n" +
                        "Available Seats: %d",
                courseId, courseName, description, instructor, courseFee, duration,
                schedule, enrolledStudents, maxStudents, getOccupancyRate(), getAvailableSeats()
        );
    }

    // Method to convert course to a file-friendly format (CSV)
    public String toFileString() {
        return String.format("%s,%s,%s,%s,%.2f,%d,%s,%d,%d",
                courseId,
                courseName.replace(",", ";"),  // Replace commas to avoid CSV issues
                description.replace(",", ";"),
                instructor.replace(",", ";"),
                courseFee,
                duration,
                schedule.replace(",", ";"),
                maxStudents,
                enrolledStudents);
    }

    // Static method to create Course from file string (CSV)
    public static Course fromFileString(String fileString) {
        String[] parts = fileString.split(",");
        if (parts.length != 9) {
            throw new IllegalArgumentException("Invalid file format");
        }

        return new Course(
                parts[0].trim(),                      // courseId
                parts[1].trim().replace(";", ","),    // courseName
                parts[2].trim().replace(";", ","),    // description
                parts[3].trim().replace(";", ","),    // instructor
                Double.parseDouble(parts[4].trim()),  // courseFee
                Integer.parseInt(parts[5].trim()),    // duration
                parts[6].trim().replace(";", ","),    // schedule
                Integer.parseInt(parts[7].trim()),    // maxStudents
                Integer.parseInt(parts[8].trim())     // enrolledStudents
        );
    }

    // Method for displaying course in a compact list format
    public String toListString() {
        return String.format("%-10s | %-25s | Rs. %8.2f | %d/%d students",
                courseId, courseName, courseFee, enrolledStudents, maxStudents);
    }
}
