package ru.hse.edu.azamatarifkhanov198;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String text = in.nextLine();
        String regex = in.nextLine();
        System.out.println(matches(text,regex));
    }

    public static boolean matches(String text, String regex) {
        if (text == null
                || regex == null
                || text.isEmpty()
                || regex.isEmpty()) {
            return false;
        }
        try {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Boolean> result = executor.submit(() -> {
                return atomicGroupRegexMatcher(text,regex);
            });

            return result.get(10000,TimeUnit.MILLISECONDS); //ожидаю ответа 10 сек или завершаю
        } catch (PatternSyntaxException e){
            System.out.println("Regex pattern contains Errors " + e.getMessage());
            return false;
        } catch (InterruptedException e){
            System.out.println("Regex thread was interrupted");
            return false;
        } catch (ExecutionException e){
            System.out.println("Exception occurred in computations");
            return false;
        } catch (TimeoutException e){
            System.out.println("TimeLimit exceeded");
            return false;
        }
    }

    private static boolean regexMatcherClear (String text, String regex) throws PatternSyntaxException{
            return Pattern.compile(regex).matcher(text).matches();
    }

    private static boolean atomicGroupRegexMatcher(String text, String regex) throws PatternSyntaxException{
            return Pattern.compile("(?>" + regex + ")").matcher(text).matches();
            //Атомарная группировка, но не уверен, что она будет давать верный результат в некоторых случаях и
            //комплексных выражениях
    }
}
