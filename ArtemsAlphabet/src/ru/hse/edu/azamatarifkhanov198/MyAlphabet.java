package ru.hse.edu.azamatarifkhanov198;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAlphabet {

    private final Map<Character, Integer> alphabet; //таблица букв и их положений в моем алфавите
    private int alphabetSize; //текущий размер алфавита (сколько букв добавлено)
    private final List<Character> myalph = new ArrayList<>(26); //упорядоченный новый алфавит

    MyAlphabet() {
        alphabet = new HashMap<>(26);
        alphabetSize = 0;

        for (int i = 0; i < 26; i++) {
            alphabet.put((char) (i + 97), -1); // заполняю таблицу чистым алфавитом
        }
        //теперь могу к ней обращаться, чтобы узнать позицию моих букв за O(1)
    }

    boolean hasLetter(char l) {
        return alphabet.get(l) != -1;
    }

    boolean addLetter(char l) {
        if (!hasLetter(l)) {
            alphabet.put(l, alphabetSize);
            myalph.add(alphabetSize,l);
            alphabetSize += 1;
            return true;
        }
        return false;
    }

    void addLetterAfter(char letter, char afterWich){
        if(!hasLetter(letter) && hasLetter(afterWich)) {
            int index = getLetterPosition(afterWich);
            myalph.add(index+1,letter);
            alphabetSize+= 1;
            for(int i = index+1; i<alphabetSize; i++){
                alphabet.put(myalph.get(i),i); //смещение букв
            }
        }
    }

    void addLetterBefore(char letter, char beforeWich){
        if(!hasLetter(letter) && hasLetter(beforeWich)){
            int index = getLetterPosition(beforeWich);
            myalph.add(index,letter);
            alphabetSize+= 1;
            for(int i = index; i<alphabetSize; i++){
                alphabet.put(myalph.get(i),i); //смещение букв
            }
        }
    }

    final int getLetterPosition(char l){
        return alphabet.get(l);
    }



    final int getAlphabetSize(){
        return alphabetSize;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(26);
        for(int i = 0; i<alphabetSize; i++){
            sb.append(myalph.get(i));
        }
        for(int i = 0; i<26; i++){
            if(!hasLetter((char)(i+97))){
                sb.append((char)(i+97));
            }
        }
        return sb.toString();
    }

}
