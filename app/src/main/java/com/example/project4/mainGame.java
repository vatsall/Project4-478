package com.example.project4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class mainGame extends AppCompatActivity {
    ArrayAdapter adapter,adapter2;
    String[] p1info = new String[]{

    };
    int buttonCounter = 0;
    String[] p2info = new String[]{
    };
    final List<String> p1infoList = new ArrayList<String>
            (Arrays.asList(p1info));
    final List<String> p2infoList = new ArrayList<String>
            (Arrays.asList(p2info));
    public static final int P1_ID = 101;
    public static final int P2_ID = 102;
    LooperClass myLooperClass = new LooperClass();
    ListView p1_lv;
    ListView p2_lv;
    String p1_num=" nothing " ,p2_num = " nothing ";
    int data;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            int guessNumber = msg.arg1;
            if(msg.what == 101){
                p1infoList.add("Player 1 guess " + String.valueOf(guessNumber));
                gameLogic(guessNumber,p2_num);
                refreshView();
            }
            else{
                p2infoList.add("Player 2 guess " + String.valueOf(guessNumber));
                refreshView();
            }
        }
    };

    private void gameLogic(int g,String actual){
        String guess = Integer.toString(g);
        if(guess == actual){
            // winner
        }
        else{
            for(int i=0;i<guess.length();i++){
                for(int t=0;t<actual.length();t++){
                    if(guess.charAt(i)==actual.charAt(t)){
                        if(i==t){
                            p1infoList.add(guess.charAt(i)+" in correct Position");
                            SystemClock.sleep(1000);
                            refreshView();

                        }else{
                            p1infoList.add(guess.charAt(i)+" in wrong Postion");
                            SystemClock.sleep(1000);
                            refreshView();
                        }
                    }
                }
            }
        }

    }

    private void refreshView(){
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,p1infoList);
        p1_lv.setAdapter(adapter);
        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,p2infoList);
        p2_lv.setAdapter(adapter2);
    }

    public class LooperClass extends Thread{
        public Looper mLooper;
        public Handler handler;
        @Override
        public void run() {
            Looper.prepare();
            mLooper = Looper.myLooper();
            handler = mHandler;
            Looper.loop();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button threadStarter = (Button)findViewById(R.id.button2);
        p1_lv = (ListView)findViewById(R.id.p1list);
        p2_lv = (ListView)findViewById(R.id.p2list);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,p1infoList);
        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,p2infoList);
        p1_lv.setAdapter(adapter);
        p2_lv.setAdapter(adapter2);
        myLooperClass.start();
        int p1Num = getRandom();
        int p2Num = getRandom();
        p1_num = Integer.toString(p1Num);
        p2_num = Integer.toString(p2Num);
        setText();

        threadStarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonCounter == 0){
                    startThreads();
                    buttonCounter++;
                }else{
                    stopAllThreads();
                }

            }
        });
    }

    private void stopAllThreads(){
        System.exit(0);
    }

    public void startThreads(){
        Handler newHandle = new Handler(myLooperClass.mLooper);
        newHandle.post(new Runnable() {
            @Override
            public void run() {
                //for(int i=0;i<10;i++){
                    p1Thread();
                    p2Thread();
               // }
            }
        });
    }

    private void setText(){
        ((TextView)findViewById(R.id.g1)).setText(p1_num);
        ((TextView)findViewById(R.id.g2)).setText(p2_num);
    }

    private int getRandom(){
        Random rand = new Random();
        int tempp1 = (int)(Math.random()*(10000-1000+1)+1000);
        Log.i("p1Num",String.valueOf(tempp1));
        return tempp1;
    }

    public class PlayerOne implements Runnable {
        @Override
        public void run() {
            Message msgP1 = Message.obtain();
            msgP1.what = 101;
            msgP1.arg1 = guessRandomly();

            data = msgP1.arg1;
            Log.i("p1 guess ",String.valueOf(msgP1.arg1));
            myLooperClass.handler.sendMessage(msgP1);
            try{
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private int guessRandomly(){
        int randomGuess1 = (int)(Math.random()*(10000-1000+1)+1000);
        return randomGuess1;
    }

    public class playerTwo implements Runnable {
        @Override
        public void run() {
            Message msgP2 = Message.obtain();
            msgP2.what = 202;
            msgP2.arg1 = guessRandomly();
            data = msgP2.arg1;
            myLooperClass.handler.sendMessage(msgP2);
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void p2Thread(){
        Handler p2Handler = new Handler();
        p2Handler.post(new Runnable() {
            @Override
            public void run() {
                Thread threadTwo = new Thread(new playerTwo());
                threadTwo.start();
                try{
                    threadTwo.sleep(3500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void p1Thread(){
        Handler p1Handler = new Handler();
        p1Handler.post(new Runnable() {
            @Override
            public void run() {
                Thread threadOne = new Thread(new PlayerOne());
                    threadOne.start();
                    try{
                    threadOne.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}