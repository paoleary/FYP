package com.example.pa.project2.model;

/**
 * Created by Pa on 14/03/2018.
 */

public class Quiz {
 // private int quizID;
    private String quizName;
    private Question question;

    public Quiz(){
    }

    public Quiz(/*int quizID, */String quizName){
  //    this.quizID = quizID;
        this.quizName = quizName;
    }

   public String getQuizName() {
       return quizName;
   }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
