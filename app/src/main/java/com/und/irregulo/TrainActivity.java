package com.und.irregulo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TrainActivity extends AppCompatActivity {
    BufferedReader reader;
    HashMap<String, String[]> words;

    EditText present, preterit, pastParticiple;
    TextView score, infinitive;

    TextView presentAnswer, preteritAnswer, pastParticipleAnswer;

    int totalQuestion, passedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        words = new HashMap<>();

        totalQuestion = 0;
        passedQuestion = 0;

        presentAnswer = findViewById(R.id.present_answer);
        preteritAnswer = findViewById(R.id.preterit_answer);
        pastParticipleAnswer = findViewById(R.id.past_participle_answer);

        present = findViewById(R.id.present_edit);
        preterit = findViewById(R.id.preterit_edit);
        pastParticiple = findViewById(R.id.past_participle_edit);

        Button verify = findViewById(R.id.verify_btn);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TrainActivity", "Button pushed");
                AlertDialog.Builder builder = new AlertDialog.Builder(TrainActivity.this.getBaseContext());
                builder.setTitle("Oopsie woopsie...").setCancelable(false).setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                String[] correction = words.get(infinitive.getText().toString());
                boolean error = false;
                assert correction != null;
                if (!present.getText().toString().equalsIgnoreCase(correction[0])) {
                    error = true;
                    setError(present);
                }else{
                    setValid(present);
                }

                if(!preterit.getText().toString().equalsIgnoreCase(correction[1])){
                    error = true;
                    setError(preterit);
                } else {
                    setValid(preterit);
                }
                if(!pastParticiple.getText().toString().equalsIgnoreCase(correction[2])){
                    error = true;
                    setError(pastParticiple);
                } else {
                    setValid(pastParticiple);
                }

                totalQuestion++;
                if(!error){
                    passedQuestion++;
                    setNewWord();
                }
                updateScore();
            }
        });

        Button showAnswerBtn = findViewById(R.id.showanswer_btn);

        showAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentAnswer.setText(Objects.requireNonNull(words.get(infinitive.getText().toString()))[0]);
                preteritAnswer.setText(Objects.requireNonNull(words.get(infinitive.getText().toString()))[1]);
                pastParticipleAnswer.setText(Objects.requireNonNull(words.get(infinitive.getText().toString()))[2]);
                totalQuestion++;
                updateScore();
            }
        });

        score = findViewById(R.id.score_lbl);
        infinitive = findViewById(R.id.infinitive_lbl);

        try{
            final InputStream file = getAssets().open("verbs.txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            line = reader.readLine();
            while(line != null){
                String[] cwords = line.split(";");
                words.put(cwords[0], new String[]{cwords[1], cwords[2], cwords[3]});
                line = reader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        Log.d("TrainActivity", "KeySetSize:" + words.keySet().size());


        setNewWord();
    }

    private void updateScore(){
        score.setText(new StringBuilder().append("Score:").append(passedQuestion).append("/").append(totalQuestion).toString());
    }

    private void setError(EditText edit){
        edit.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorError, null));
    }

    private void setValid(EditText edit){
        edit.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorValid, null));
    }


    private void setNewWord(){

        present.setText("");
        preterit.setText("");
        pastParticiple.setText("");

        presentAnswer.setText("");
        preteritAnswer.setText("");
        pastParticipleAnswer.setText("");

        present.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        preterit.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        pastParticiple.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));

        List<String> keys = new ArrayList<String>(words.keySet());
        infinitive.setText(keys.get(new Random().nextInt(keys.size())));
    }
}
