package ru.hse.edu.azamatarifkhanov198;

import java.util.Scanner;

public class Main {

    private static final MyAlphabet alph = new MyAlphabet();

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); //считываю количество подаваемых имен

        String[] names = new String[n];//массив имен
        //массив активности имен. Нужен для игнорирования выбывших имен при обработке
        Boolean[] activeNames = new Boolean[n];

        int maxNameLen = 0;

        for (int i = 0; i < n; i++) {
            names[i] = in.next();
            activeNames[i] = true;
            maxNameLen = Math.max(names[i].length(), maxNameLen);
        }

        System.out.println(alphabetChecker(names));
    }

    public static String alphabetChecker(String[] names) {
        for (int i = 0; i < names.length-1; i++) {
            if(!myAlphabetComparison(names[i],names[i+1])){
                return "Impossible";
            }
        }
        return alph.toString();

    }

    //Фиговое решение
    /*
    private static boolean rowChecker(int rowNumm, String[] names, Boolean[] active) {

        int latestLetterPos = -1;
        char letter;
        for (int i = 0; i < names.length; i++) {
            if (active[i]) //мы все еще смотрим на это имя?
            {
                if (names[i].length() - 1 < rowNumm) // на этом ряду у него кончились буквы?
                {
                    if (i != 0 //не надо рассматривать первое имя
                            && active[i - 1] //а прошлое имя мы проверяем?
                            && lettersComparison(names[i].charAt(names[i].length() - 1),
                            names[i - 1].charAt(names[i].length() - 1)) >= 0) {
                        //последняя буква текущего слова не больше чем буква на том же месте от предыдущего?
                        //если идут  abc[d]e...abc[d] и номер ряда 4 (с нуля): проверяем, d
                        return false;
                    } else {
                        //все ок, ситуция adc[d]e...abc[f] или  abcd[]...abcd[] или это первое имя
                        active[i] = false; //слово короткое, в следующих итерациях проверять не надо
                    }
                } else if(i!=0){
                    if(rowNumm!= 0
                            && names[i].charAt(rowNumm-1) != names[i-1].charAt(rowNumm-1))
                    {
                        continue;
                    }
                    int res = lettersComparison(names[i-1].charAt(rowNumm),names[i].charAt(rowNumm));
                    if(res == -1)
                    {
                        return false;
                    } else if( res == 1){
                        active[i] = false; //это слово уже упорядоченно
                    }

                }
            }
        }
        return true; //ряд соответствует лексикографическому порядку
    }
     */

    private static boolean myAlphabetComparison(String a, String b){
        int commonLen = Math.min(a.length(),b.length());
        int res;

        for(int i = 0; i < commonLen; i++){
            res = lettersComparison(a.charAt(i),b.charAt(i));
            if(res == 1){
                return true;
            }
            if( res == -1){
                return false;
            }
        }
        return a.length() <= b.length();//если а длиннее b при том что общие commonLen букв одинаковые
        //значит а стоит лексикографически позже b
    }

    private static int lettersComparison(char a, char b){
        //на самом деле все не так просто. Ведь данные буквы могут сравниваться
        //с остальным алфавитом в словах далее и становится неясно, между каких именно букв вставить данные
        //но я без понятия как справиться с этой проблемой
        if(alph.hasLetter(a) && alph.hasLetter(b)) {
            return (Integer.compare(alph.getLetterPosition(b), alph.getLetterPosition(a)));
        }
        else if(!alph.hasLetter(a) && alph.hasLetter(b)){
            alph.addLetterBefore(a,b);
            return 1;
        }
        else if(alph.hasLetter(a) && !alph.hasLetter(b)){
            alph.addLetterAfter(b,a);
            return 1;
        }
        else{
            alph.addLetter(a);
            alph.addLetter(b);
            return a==b? 0 : 1;
        }
    }
}

/*
6
qwert
qwertg
qwertc
qwerg
qwerh
qwel

6
nikita
nazar
seriy
stas
igor
bahrom

7
igor
nazar
nikita
stas
seriy
sbor
sss

тут к примеру падает проверка sbor sss, т.к. по моему алгоритму s перед b стоит
а вот как определить, куда поставить b?
 */