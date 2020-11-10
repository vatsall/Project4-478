package com.example.project4;

import java.util.Random;

public class numberGenerator {

    String[] numbers = new String[]{
            "1","2","3","4","5","6","7","8","9","0"
    };


    private void checkDuplicates(int number){

    }

    private int getRandom(){
        return (int) (Math.random()*numbers.length);
    }
}
