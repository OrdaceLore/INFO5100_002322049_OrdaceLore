import java.lang.reflect.Array;
import java.util.*;
public class Session {

    int numStudents; 
    Student [] students;
// create a session with n students
    public Session(int n){
        numStudents=n;
        students=new Student[n];
        Random r=new Random();
        for (int i=0;i<n;i++){
            if (r.nextBoolean())
            {students[i]=new FullTimeStudent("Student"+i);
            FullTimeStudent s=(FullTimeStudent)students[i];
            s.examScores[0]=r.nextDouble()*100;
            s.examScores[1]=r.nextDouble()*100;
            for (int j=0;j<15;j++)
            {s.quizScores[j]=Math.sqrt(r.nextDouble())*100;}}

  
            else
            {students[i]=new PartTimeStudent("Student"+i);
            for (int j=0;j<15;j++)
            {students[i].quizScores[j]=Math.sqrt(r.nextDouble())*100;}}
        }
        
        
    }
// get the average quiz score of student n
    public void getAverageQuizScore(int n){
        double sum=0;
        for(int j=0;j<15;j++)
        {sum+=students[n].quizScores[j];}
        System.out.println("the average quiz score is");
        System.out.println(sum/15);
    }
// print the quiz scores of student n
    public void printQuizScore(int n){
        double [] s=students[n].quizScores;
        Arrays.sort(s);
        System.out.println("the quiz scores are");  
       for(int j=0;j<15;j++){System.out.println(s[j]);}

    }
// print the names of part time students
public void printPartTimeStudent(){
    System.out.println("the part time students are");
    for (int i=0;i<numStudents;i++){
        if (students[i] instanceof PartTimeStudent){
            PartTimeStudent s=(PartTimeStudent)students[i];
            System.out.println(s.name);


        }
    }
}
// print the exam scores of full time students
public void printFullTimeExamScores(){
    System.out.println("the full time students exam scores are");
    for (int i=0;i<numStudents;i++){
        if (students[i] instanceof FullTimeStudent){
            FullTimeStudent s=(FullTimeStudent)students[i];
            System.out.println(s.name);
            System.out.println(s.examScores[0]);
            System.out.println(s.examScores[1]);

        }

    }
}
}
