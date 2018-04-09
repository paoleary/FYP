package com.example.pa.project2;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pa.project2.model.Question;
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
    DatabaseReference quizQuestions;
    DatabaseReference questionAndAnswers;

    //for creating new quiz
    MaterialEditText edtNewQuizName;
    MaterialEditText edtNewQuestion;
    MaterialEditText edtNewAnswerA;
    MaterialEditText edtNewAnswerB;
    MaterialEditText edtNewAnswerC;
    MaterialEditText edtNewAnswerD;
    Boolean quizCreated = false;
    int quizCreatedInt = 0;

    Spinner CorrectDropdown;
    String correctAnswer;


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
                if(!quizCreated){
                    displayAddQuizDialogue();
                }
                if(quizCreatedInt == 1){
                    displayAddQuestionDialogue();;
                }
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

        final LayoutInflater inflater = this.getLayoutInflater();
        final View add_quiz_layout = inflater.inflate(R.layout.add_quiz_layout,null);
        alertdialog.setView(add_quiz_layout);
        alertdialog.setIcon(R.drawable.ic_question_answer_black_24dp);

        edtNewQuizName = add_quiz_layout.findViewById(R.id.edtNewQuizName);
        /*edtNewQuestion = add_quiz_layout.findViewById(R.id.edtNewQuestion);
        edtNewAnswerA = add_quiz_layout.findViewById(R.id.edtNewAnswerA);
        edtNewAnswerB = add_quiz_layout.findViewById(R.id.edtNewAnswerB);
        edtNewAnswerC = add_quiz_layout.findViewById(R.id.edtNewAnswerC);
        edtNewAnswerD = add_quiz_layout.findViewById(R.id.edtNewAnswerD); */

        alertdialog.setNegativeButton("Cancel", new AlertDialog.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        alertdialog.setPositiveButton("Submit", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                final Quiz quiz = new Quiz(
                    edtNewQuizName.getText().toString());
                quizzes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(quiz.getQuizName()).exists()) {
                            Toast.makeText(LoggedInHomeActivity.this, "Quiz name is taken", Toast.LENGTH_SHORT).show();
                        } else {
                            quizCreated = true;
                            quizCreatedInt = 1;
                            quizzes.child(quiz.getQuizName()).setValue(quiz);
                            displayAddQuestionDialogue();
                            Toast.makeText(LoggedInHomeActivity.this, "Quiz creation successful", Toast.LENGTH_SHORT).show();
                            quizCreated = false;
                            quizCreatedInt = 0;


                        }
                        /*if (quizCreated = true){
                            AlertDialog.Builder alertdialog2 = new AlertDialog.Builder(LoggedInHomeActivity.this);
                            alertdialog2.setTitle("Add Question");
                            alertdialog2.setMessage("Please provide the necessary information");

                            Context context = null; //FIX THIS NOOOOOOOOOOOOOOOOOOOOOOOOOOOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
                            LayoutInflater inflater2 = LayoutInflater.from(LoggedInHomeActivity.this);//(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //this.getLayourInflater();
                            View add_question_layout = inflater2.inflate(R.layout.add_question_layout, null);
                            alertdialog2.setView(add_question_layout);
                            alertdialog2.setIcon(R.drawable.ic_question_answer_black_24dp);

                            edtNewQuestion = add_question_layout.findViewById(R.id.edtNewQuestion);
                            edtNewAnswerA = add_question_layout.findViewById(R.id.edtNewAnswerA);
                            edtNewAnswerB = add_question_layout.findViewById(R.id.edtNewAnswerB);
                            edtNewAnswerC = add_question_layout.findViewById(R.id.edtNewAnswerC);
                            edtNewAnswerD = add_question_layout.findViewById(R.id.edtNewAnswerD);

                            alertdialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialogInterface.dismiss();
                                }
                            });
                            alertdialog2.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    final Question question = new Question(
                                            edtNewQuestion.getText().toString(),
                                            edtNewAnswerA.getText().toString(),
                                            edtNewAnswerB.getText().toString(),
                                            edtNewAnswerC.getText().toString(),
                                            edtNewAnswerD.getText().toString());
                                    questions = database.getReference(edtQuizName.getText().toString());
                                    questions.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.child(question.getQuestion()).exists()){
                                               Toast.makeText(LoggedInHomeActivity.this, "testing",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                questions.child(question.getQuestion()).setValue(edtNewQuestion);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    dialogInterface.dismiss();


                                }
                            });
                        }*/
                    }

                    //private LayoutInflater getLayourInflater() {
                    //    return LayoutInflater;
                    //}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();

            }


        });

        alertdialog.show();

    }

    private void displayAddQuestionDialogue() {
        AlertDialog.Builder alertdialog2 = new AlertDialog.Builder(LoggedInHomeActivity.this);
        alertdialog2.setTitle("Add Question");
        alertdialog2.setMessage("Please provide the necessary information");

        final LayoutInflater inflater2 = LayoutInflater.from(LoggedInHomeActivity.this);
        final View add_question_layout = inflater2.inflate(R.layout.add_question_layout,null);
        alertdialog2.setView(add_question_layout);
        alertdialog2.setIcon(R.drawable.ic_question_answer_black_24dp);


        edtNewQuestion = add_question_layout.findViewById(R.id.edtNewQuestion);
        edtNewAnswerA = add_question_layout.findViewById(R.id.edtNewAnswerA);
        edtNewAnswerB = add_question_layout.findViewById(R.id.edtNewAnswerB);
        edtNewAnswerC = add_question_layout.findViewById(R.id.edtNewAnswerC);
        edtNewAnswerD = add_question_layout.findViewById(R.id.edtNewAnswerD);

        //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        CorrectDropdown = add_question_layout.findViewById(R.id.CorrectDropdown);
        String[] items = new String[]{"A","B","C","D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        CorrectDropdown.setAdapter(adapter);



        alertdialog2.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog2.setPositiveButton("Submit", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                final Question question = new Question(
                    edtNewQuestion.getText().toString(),
                    edtNewAnswerA.getText().toString(),
                    edtNewAnswerB.getText().toString(),
                    edtNewAnswerC.getText().toString(),
                    edtNewAnswerD.getText().toString(),
                    correctAnswer = "Answer" + CorrectDropdown.getSelectedItem().toString()); //https://stackoverflow.com/questions/10331854/how-to-get-spinner-selected-item-value-to-string
                quizQuestions = database.getReference(edtNewQuizName.getText().toString());
                quizQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(question.getQuestion()).exists()){
                            Toast.makeText(LoggedInHomeActivity.this, "Question already exists", Toast.LENGTH_SHORT).show();
                            displayAddQuestionDialogue();
                        }
                        else{
                            quizQuestions.child(question.getQuestion()).setValue(question);

                            displayAddQuestionDialogue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        alertdialog2.show();
    }

}
