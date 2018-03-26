package com.example.pa.project2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pa.project2.model.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoggedInHomeActivity extends AppCompatActivity {

    //private BottomNavigationView bottomNavigationView;

    //initialise buttons
    Button btnAddQuiz;
    Button btnTakeQuiz;

    //firebase stuff
    FirebaseDatabase database;
    DatabaseReference quizzes;

    //for creating new quiz
    MaterialEditText edtNewQuizName;
    MaterialEditText edtNewQuestion;
    MaterialEditText edtNewAnswerA;
    MaterialEditText edtNewAnswerB;
    MaterialEditText edtNewAnswerC;
    MaterialEditText edtNewAnswerD;


    //for taking a quiz
    MaterialEditText edtQuizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_home);

        //Firebase stuff
        database = FirebaseDatabase.getInstance();
        quizzes = database.getReference("Quizzes");

        btnAddQuiz = (Button)findViewById(R.id.btnAddQuiz);
        btnTakeQuiz = (Button) findViewById(R.id.btnTakeQuiz);

        edtQuizName = findViewById(R.id.edtQuizName);

        btnTakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQuiz(edtQuizName.getText().toString());  //CREATE TAKEQUIZ METHOD - LOOK AT signIn METHOD IN MainActivity
            }
        });

        btnAddQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                displayAddQuizDialogue(); //CREATE METHOD
            }
        });
    }

    private void takeQuiz(final String quizName){
        quizzes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check quizName field isn't empty
                if(dataSnapshot.child(quizName).exists()){
                    if(!quizName.isEmpty()){

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayAddQuizDialogue(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(LoggedInHomeActivity.this);
        alertdialog.setTitle("Add Quiz");
        alertdialog.setMessage("Please provide the necessary information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_quiz_layout = inflater.inflate(R.layout.add_quiz_layout,null);
        alertdialog.setView(add_quiz_layout);
        alertdialog.setIcon(R.drawable.ic_question_answer_black_24dp);

        edtNewQuizName = add_quiz_layout.findViewById(R.id.edtNewQuizName);
        edtNewQuestion = add_quiz_layout.findViewById(R.id.edtNewQuestion);
        edtNewAnswerA = add_quiz_layout.findViewById(R.id.edtNewAnswerA);
        edtNewAnswerB = add_quiz_layout.findViewById(R.id.edtNewAnswerB);
        edtNewAnswerC = add_quiz_layout.findViewById(R.id.edtNewAnswerC);
        edtNewAnswerD = add_quiz_layout.findViewById(R.id.edtNewAnswerD);

        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Quiz quiz = new Quiz(
                    edtNewQuizName.getText().toString(),
                    edtNewQuestion.getText().toString(),
                    edtNewAnswerA.getText().toString(),
                    edtNewAnswerB.getText().toString(),
                    edtNewAnswerC.getText().toString(),
                    edtNewAnswerD.getText().toString());
                quizzes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(quiz.getQuizName()).exists()){
                            Toast.makeText(LoggedInHomeActivity.this, "Quiz name is taken", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            quizzes.child(quiz.getQuizName()).setValue(quiz);
                            Toast.makeText(LoggedInHomeActivity.this, "Quiz creation successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });

        alertdialog.show();

    }


}
