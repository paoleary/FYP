package com.example.pa.project2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pa.project2.model.Question;
import com.example.pa.project2.model.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.time.Month;

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
    DatabaseReference questions;

    //for creating new quiz
    MaterialEditText edtNewQuizName;
    MaterialEditText edtNewQuestion;
    MaterialEditText edtNewAnswerA;
    MaterialEditText edtNewAnswerB;
    MaterialEditText edtNewAnswerC;
    MaterialEditText edtNewAnswerD;
    Spinner CorrectDropdown;
    String correctAnswer;
    Boolean quizCreated = false;
    int quizCreatedInt = 0;

    //for taking quiz
    MaterialEditText edtQuizName;
    TextView textQuestion;
    TextView textAnswerA;
    TextView textAnswerB;
    TextView textAnswerC;
    TextView textAnswerD;
    Spinner AnswerSelect;
    String answerSelect;



    //for taking a quiz


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
                    if(quizName.isEmpty()){
                        Toast.makeText(LoggedInHomeActivity.this, "Please enter a quiz name", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final Quiz quiz = new Quiz(quizName);
                        quizzes.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //if(){
                                //    Toast.makeText(LoggedInHomeActivity.this, "Quiz by that name does not exist", Toast.LENGTH_SHORT).show();
                                //}
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        final AlertDialog.Builder activeQuizDialog = new AlertDialog.Builder(LoggedInHomeActivity.this);
                        activeQuizDialog.setMessage("Please select which answer you think is correct from the dropdown");

                        final LayoutInflater takeQuizInflater = LayoutInflater.from(LoggedInHomeActivity.this);
                        final View active_quiz_layout = takeQuizInflater.inflate(R.layout.active_quiz_layout, null);
                        activeQuizDialog.setView(active_quiz_layout);

                        textQuestion = active_quiz_layout.findViewById(R.id.textQuestion);
                        textAnswerA = active_quiz_layout.findViewById(R.id.textAnswerA);
                        textAnswerB = active_quiz_layout.findViewById(R.id.textAnswerB);
                        textAnswerC = active_quiz_layout.findViewById(R.id.textAnswerC);
                        textAnswerD = active_quiz_layout.findViewById(R.id.textAnswerD);

                        //https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
                        AnswerSelect = active_quiz_layout.findViewById(R.id.AnswerSelect);
                        String[] items = new String[]{"A", "B", "C", "D"};
                        //https://stackoverflow.com/questions/28848735/cannot-resolve-constructor-arrayadapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoggedInHomeActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                        AnswerSelect.setAdapter(adapter);

                        questions = database.getReference(quizName.toString());
                        questions.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Question tempQuestion = singleSnapshot.getValue(Question.class);
                                    textQuestion.setText(tempQuestion.getQuestion());
                                    textAnswerA.setText(tempQuestion.getAnswerA());
                                    textAnswerB.setText(tempQuestion.getAnswerB());
                                    textAnswerC.setText(tempQuestion.getAnswerC());
                                    textAnswerD.setText(tempQuestion.getAnswerD());
                                    //((ViewGroup)active_quiz_layout.getParent()).removeView(active_quiz_layout);
                                    //activeQuizDialog.show();//Skips all questions but the last one

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        activeQuizDialog.setNegativeButton("Close", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        });

                        activeQuizDialog.setPositiveButton("Submit", new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                questions = database.getReference(quizName.toString());
                                questions.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                        //     Question tempQuestion = singleSnapshot.getValue(Question.class);
                                        //     textQuestion.setText(tempQuestion.getQuestion()); //not doing what I want right now
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                        activeQuizDialog.show();
                    }
                    }
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    //ADD QUIZ DIALOGUE
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
                        //Check if quiz name is already taken
                        if (dataSnapshot.child(quiz.getQuizName()).exists()) {
                            Toast.makeText(LoggedInHomeActivity.this, "Quiz name is taken", Toast.LENGTH_SHORT).show();
                        }
                        //Sets quizCreated and quizCreatedInt to required states to run addQuestionDialogue
                        else {
                            quizCreated = true;
                            quizCreatedInt = 1;
                            quizzes.child(quiz.getQuizName()).setValue(quiz);
                            displayAddQuestionDialogue();
                            Toast.makeText(LoggedInHomeActivity.this, "Quiz creation successful", Toast.LENGTH_SHORT).show();
                            //resets quizCreated and quizCreatedInt to allow creation of multiple quizzes subsequently
                            quizCreated = false;
                            quizCreatedInt = 0;
                        }
                        /*if (quizCreated = true){
                            AlertDialog.Builder alertdialog2 = new AlertDialog.Builder(LoggedInHomeActivity.this);
                            alertdialog2.setTitle("Add Question");
                            alertdialog2.setMessage("Please provide the necessary information");

                            Context context = null; //
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



        alertdialog2.setNegativeButton("Finish", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertdialog2.setPositiveButton("Submit another question", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                final Question question = new Question(
                    edtNewQuestion.getText().toString(),
                    edtNewAnswerA.getText().toString(),
                    edtNewAnswerB.getText().toString(),
                    edtNewAnswerC.getText().toString(),
                    edtNewAnswerD.getText().toString(),
                    correctAnswer = CorrectDropdown.getSelectedItem().toString()); //https://stackoverflow.com/questions/10331854/how-to-get-spinner-selected-item-value-to-string
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
