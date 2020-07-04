package com.example.prane.braintrainer;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int totalScore=0;
    int correctAnswer=0;
    Boolean isTimeLeft=true;
    Boolean isInstanceCompleted=false;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeLayout();
    }

    void initializeLayout()
    {
        ConstraintLayout initialLayout = findViewById(R.id.initialLayout);
        initialLayout.setVisibility(View.VISIBLE);

        ConstraintLayout gameOverLayout = findViewById(R.id.gameOverLayout);
        gameOverLayout.setVisibility(View.GONE);

        ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.GONE);
    }

    void resetGameLayout()
    {
        totalScore = 0;
        ConstraintLayout initialLayout = findViewById(R.id.initialLayout);
        initialLayout.setVisibility(View.GONE);

        ConstraintLayout gameOverLayout = findViewById(R.id.gameOverLayout);
        gameOverLayout.setVisibility(View.GONE);

        ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.VISIBLE);

        TextView resultTextView = findViewById(R.id.result);
        resultTextView.setText("");

        resetScore();
        resetTimer();

        isInstanceCompleted=true;
        startTimer();
        generateNextInstance();

    }

    void generateNextInstance()
    {
        isInstanceCompleted = false;
        correctAnswer=0;
        isTimeLeft=true;
        isInstanceCompleted=false;

        int randomNumber1 = getRandomNumber(20);
        int randomNumber2 = getRandomNumber(20);
        String operators[] = {"+","-","*","/"};
        String operator = getRandomOperator(operators);
        String question = generateQuestion(randomNumber1,randomNumber2,operator);
        correctAnswer = computeResult(randomNumber1, randomNumber2, operator);
        displayQuestion(question);
        generateOptions();
        displayOptions();
        isInstanceCompleted = true;
    }

    int getRandomNumber(int maxNumber)
    {
        Random random = new Random();
        return random.nextInt(maxNumber);
    }

    String getRandomOperator(String operators[])
    {
        int randomIndex = getRandomNumber(operators.length);

        return operators[randomIndex];
    }

    String generateQuestion(int num1, int num2, String operator)
    {
        String question="";
        question = num1 + operator + num2;

        return  question;
    }

    void displayQuestion(String question)
    {
        TextView questionTextView = findViewById(R.id.question);
        questionTextView.setText(question);
    }

    void resetTimer()
    {
        try {
            timer.cancel();
        }
        catch (Exception e)
        {

        }
        TextView timerTextView = findViewById(R.id.timer);
        timerTextView.setText("30");
    }

    void startTimer()
    {
        final TextView timerTextView = findViewById(R.id.timer);
        timer = new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(Long.toString(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                isTimeLeft=false;
                TextView totalScoreTextView = findViewById(R.id.totalScore);
                totalScoreTextView.setText("You score is "+Integer.toString(totalScore));
                gameOver();
                //stop
                //display score
                //save high score
            }
        };
        timer.start();
    }

    void resetScore()
    {
        TextView scoreTextView = findViewById(R.id.score);
        scoreTextView.setText("0");
    }

    String getResultMessage(Boolean isCorrectAnswer)
    {
        if(isCorrectAnswer)
        {
            return "Correct Answer";
        }
        return "Wrong Answer";
    }


    int computeResult(int num1, int num2, String operator)
    {
        switch (operator)
        {
            case "+" : return num1 + num2;

            case "-" : return num1 - num2;

            case "*" : return num1 * num2;

            case "/" :
                while (num2 == 0)
                {
                    num2 = getRandomNumber(20);
                }
                return num1 / num2;

            default: return 0;
        }
    }
//need to work on option position
    void displayOptions()
    {
        int options[] = generateOptions();

        int optionPosition1 = getRandomNumber(options.length);
        TextView option1TextView = findViewById(R.id.buttonA);
        option1TextView.setText(Integer.toString(options[optionPosition1]));

        int optionPosition2;
        optionPosition2 = getRandomNumber(options.length);
        while (optionPosition2 == optionPosition1)
        {
            optionPosition2 = getRandomNumber(options.length);
        }
        TextView option2TextView = findViewById(R.id.buttonB);
        option2TextView.setText(Integer.toString(options[optionPosition2]));

        int optionPosition3;
        optionPosition3 = getRandomNumber(options.length);
        while (optionPosition3 == optionPosition1 || optionPosition3 == optionPosition2)
        {
            optionPosition3 = getRandomNumber(options.length);
        }
        TextView option3TextView = findViewById(R.id.buttonC);
        option3TextView.setText(Integer.toString(options[optionPosition3]));

        int optionPosition4;
        optionPosition4 = getRandomNumber(options.length);
        while (optionPosition4 == optionPosition1 || optionPosition4 == optionPosition2 || optionPosition4 == optionPosition3)
        {
            optionPosition4 = getRandomNumber(options.length);
        }
        TextView option4TextView = findViewById(R.id.buttonD);
        option4TextView.setText(Integer.toString(options[optionPosition4]));

    }

    int[] generateOptions()
    {
        String operators[] = {"+","-"};
        int options[] = {correctAnswer,0,0,0};

        options[1] = computeResult(correctAnswer,getRandomNumber(10),getRandomOperator(operators));
        options[2] = computeResult(correctAnswer,getRandomNumber(10),getRandomOperator(operators));
        options[3] = computeResult(correctAnswer,getRandomNumber(10),getRandomOperator(operators));

        return options;
    }

    public void onOptionClick(View v) {
        Boolean isCorrectAnswer;

        Button optionButton = (Button)v;
        String optionValue = optionButton.getText().toString();
        isCorrectAnswer = checkResultEquality(Integer.parseInt(optionValue));
        String resultMessage = getResultMessage(isCorrectAnswer);
        displayResultMessage(resultMessage);
        incrementScore(isCorrectAnswer);
        displayScore();
        if(isTimeLeft && isInstanceCompleted){
            generateNextInstance();
        }
    }

    Boolean checkResultEquality(int optionValue)
    {
        if(optionValue == correctAnswer)
        {
            return true;
        }
        return false;
    }

    void displayResultMessage(String resultMessage)
    {
        TextView resultTextView = findViewById(R.id.result);
        if(resultMessage=="Correct Answer")
        {
            resultTextView.setTextColor(Color.parseColor("#77ff77"));
        }
        else
        {
            resultTextView.setTextColor(Color.parseColor("#ff7777"));
        }
        resultTextView.setText(resultMessage);
    }

    void incrementScore(Boolean isCorrectAnswer)
    {
        if(isCorrectAnswer)
        {
            totalScore++;
        }
    }

    void displayScore()
    {
        TextView scoreTextView = findViewById(R.id.score);
        scoreTextView.setText(Integer.toString(totalScore));
    }

    int getTotalScore()
    {
        return totalScore;
    }

    public void reset(View view) {
        resetGameLayout();
    }

    public void gameOver()
    {
        ConstraintLayout initialLayout = findViewById(R.id.initialLayout);
        initialLayout.setVisibility(View.GONE);

        ConstraintLayout gameOverLayout = findViewById(R.id.gameOverLayout);
        gameOverLayout.setVisibility(View.VISIBLE);

        ConstraintLayout gameLayout = findViewById(R.id.gameLayout);
        gameLayout.setVisibility(View.GONE);
    }
}
