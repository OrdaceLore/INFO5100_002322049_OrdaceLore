public class App1 {


    public static void main(String[] args) {
        // Create a new session with 20 students
        Session s=new Session(20);

        // Get the average quiz score of student 2
        s.getAverageQuizScore(2);
        // Print the quiz scores of student 3 in ascending order
        s.printQuizScore(3);
        // Print the names of part time students
        s.printPartTimeStudent();
        // Print the exam scores of full time students
        s.printFullTimeExamScores();

    }


}
