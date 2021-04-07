package ru.hse.edu.azamatarifkhanov198;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Укажите имя файла с первым текстом из папки resources внутри проекта");
        String pathToOld = in.nextLine();
        System.out.println("Укажите имя файла со вторым текстом из папки resources внутри проекта");
        String pathToNew = in.nextLine();

        List<String> file1 = new ArrayList<>();
        List<String> file2 = new ArrayList<>();

        fileReader(file1, pathToOld);
        fileReader(file2, pathToNew);

        List<StringHolder> oldLines = new ArrayList<>(file1.size());
        List<StringHolder> newLines = new ArrayList<>(file2.size());

        linesHandler(oldLines, newLines, file1, file2);
        //теперь создать html файл

        fileWriter(htmlGenerator(oldLines, "Старый файл"), "oldtext.html");
        fileWriter(htmlGenerator(newLines, "Новый файл"),"newtext.html");
        System.out.println("Проверьте файл htmlfile.html в корневой папке проекта");

    }

    /***
     * Более ООП решение для html файла. , в которой построчно находится текст.
     * Ячейки подкрашены как надо
     * @param lines Список держателей строк
     * @param title Название первой строки в таблице
     * @return Строка с HTML разметкой страницы
     */
    private static String htmlGenerator(List<StringHolder> lines, String title){
        //Я что, похож на фронтендера?
        //Какие такие библиотеки для Верстки?
        StringBuilder file = new StringBuilder();
        file.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "tr:nth-child(1) {\n" +
                "background: #fffff;\n" +
                "border-bottom: 2px solid\n" +
                "text-align: center\n" +
                "height: 17px;\n" +
                "}\n" +
                "tr {\n" +
                "height: 18px;\n" +
                "border-bottom: none;\n" +
                "margin: 0px;\n" +
                "cellspacing: none;\n" +
                "} \n" +
                "td {\n" +
                "width: 100%;\n" +
                "height: 10px;\n" +
                "text-align: left;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table style=\"height: 37px; width: 100%; border-spacing: 0px; border-collapse: collapse; border-style: hidden;\" border=\"1\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td bgcolor=\"#e8d150\" colspan=\"2\">"+ title+"</td>\n" +
                "</tr>\n");
        int i = 0;
        for(var e: lines){
            String color = getColor(e.getType());
            file.append("<tr>\n<td style=\"width:5px;\" bgcolor=\"")
                    .append(color)
                    .append("\">")
                    .append(i)
                    .append("</td>\n<td bgcolor=\"")
                    .append(color)
                    .append("\" style=\"padding-left: 2%;\">")
                    .append(e.getString())
                    .append("</td>\n</tr>\n");
            i++;
        }

        file.append("</tbody>\n" +
                "</table>\n" +
                "  </body>\n" +
                "</html>");

        return file.toString();
    }

    /***
     * Метод для получения цвета в формате RGB по передаваемому типу
     * @param c тип ячейки 0 - Без изменений #ffffff. 1 - Удаленная #dfdfdf.  2 - Добавленная ##64e89c
     *          3 - Измененная #73bde8
     * @return Строка с цветом в формате RGB
     */
    private static String getColor(byte c) {
        switch (c) {
            case 1:
                return "#dfdfdf";
            case 2:
                return "#64e89c";
            case 3:
                return "#73bde8";
            default:
                return "#ffffff";
        }
    }

    /***
     * Метод для Обработки двух текстов на различия и схожесть
     * @param oldLines Список для заполнения старыми строками
     * @param newLines Список для заполнения новыми строками
     * @param file1 Список строк первого текста
     * @param file2 Список строк второго текста
     */
    private static void linesHandler(List<StringHolder> oldLines, List<StringHolder> newLines,
                                     List<String> file1, List<String> file2) {
        for (String s : file1) {
            oldLines.add(new StringHolder(s.trim(), (byte) 1)); //изначально все строки под удаление
        }

        for (String s : file2) {
            newLines.add(new StringHolder(s.trim(), (byte) 2)); //изначально все строки под добавление
        }

        List<Integer> identicalRowsIndexes1 = new ArrayList<>();
        List<Integer> identicalRowsIndexes2 = new ArrayList<>();

        //hashEnumeration(oldLines,newLines,identicalRowsIndexes1,identicalRowsIndexes2);
        hashSubSequence(oldLines,newLines,identicalRowsIndexes1,identicalRowsIndexes2);

        //размер списков индексов одинаковый
        //измененные строки и слева и справа будут покрашены синим

        int idx1 = -1;
        int idx2 = -1;
        boolean addedRows;
        boolean deletedRows;
        //identicalRowsIndexes1.sort(Integer::compareTo);
        //identicalRowsIndexes2.sort(Integer::compareTo);
        for (int i = 0; i < identicalRowsIndexes1.size(); i++) {
            deletedRows = identicalRowsIndexes1.get(i) - idx1 > 1;
            addedRows = identicalRowsIndexes2.get(i) - idx2 > 1;
            //значит между прошлой совпашей строкой и текущей есть несовпавшие
            if (deletedRows && addedRows) { //значит это измененные строки
                for (int k = idx1 + 1; k < identicalRowsIndexes1.get(i); k++) {
                    oldLines.get(k).setType((byte) 3);
                }
                for (int k = idx2 + 1; k < identicalRowsIndexes2.get(i); k++) {
                    newLines.get(k).setType((byte) 3);
                }
            } //все строки что не будут изменены и не явзяются совпавшими останутся добавленными и удаленными
            idx1 = identicalRowsIndexes1.get(i);
            idx2 = identicalRowsIndexes2.get(i);
        }
        //надо еще проверить что там в конце
        //последняя строка не является совпавшей?
        deletedRows = identicalRowsIndexes1.get(identicalRowsIndexes1.size() - 1) < oldLines.size() - 1;
        //последняя строка не является совпавшей?
        addedRows = identicalRowsIndexes2.get(identicalRowsIndexes2.size() - 1) < newLines.size() - 1;
        if (deletedRows && addedRows) { //конец изменен
            for (int k = identicalRowsIndexes1.get(identicalRowsIndexes1.size() - 1) + 1; k < oldLines.size(); k++) {
                oldLines.get(k).setType((byte) 3);
            }
            for (int k = identicalRowsIndexes2.get(identicalRowsIndexes2.size() - 1) + 1; k < newLines.size(); k++) {
                newLines.get(k).setType((byte) 3);
            }
        }
        //можно окраску проводить на этапе сравнения строк, но так более наглядно

    }

    /*
    Данный метод также работает, но у него много условностей и недостатков

    private static void hashEnumeration(List<StringHolder> oldLines, List<StringHolder> newLines,
                                        List<Integer> idRowsIdx1, List<Integer> idRowsIdx2){
        Map<Integer,Integer> duplicateLinesTimes = new HashMap<>(); //против повторяющихся строк
        //начинаем типизировать)))
        int size = -1;
        int counter;
        for (int i = 0; i < oldLines.size(); i++) {
            counter = 1;

            for (int j = 0; j < newLines.size(); j++) {
                if (oldLines.get(i).getHashcode() == newLines.get(j).getHashcode() &&
                        oldLines.get(i).getString().equals(newLines.get(j).getString())) {

                    //if(duplicateLinesTimes.containsKey(oldLines.get(i).getHashcode())
                    //&& counter <= duplicateLinesTimes.get(oldLines.get(i).getHashcode())){
                    //    counter++; //борьба чтобы одинаковые строки из старого файла не стопорились на одной строке
                    //    // из нового
                    //    continue;
                    //}

                    oldLines.get(i).setType((byte) 0); //строки которые не менялись
                    newLines.get(j).setType((byte) 0);
                    idRowsIdx1.add(i);
                    idRowsIdx2.add(j);
                    size = j; //Он прикольно отработает если у нас текст разделен Абзацами
                    //Но если мы рассматриваем абзац как полноценную строку и нам важна несмещенность
                    //строк, то такой вывод корректен
                    //Если мы будем игнорировать Пустые строки как строки в целом, то тогда результат
                    //алгоритма будет выглядеть Адекватно. Но в идеале еще бы весов добавить, чтобы алгоритм понимал,
                    //какие строки лучше принять за "совпавшие"

                    //Если нам совсем не важен порядок строк
                    //if(duplicateLinesTimes.containsKey(oldLines.get(i).getHashcode())){
                    //    duplicateLinesTimes.put(oldLines.get(i).getHashcode(),
                    //            counter); //неустойчиво к строкам с одинаковыми хешами
                    //} else {
                    //    duplicateLinesTimes.put(oldLines.get(i).getHashcode(), 1);
                    //}
                    break;
                }
            }
        }
    } */


    /***
     * Поиск наибольшей подпоследовательности двух строк. Помимо хешей можно и динамически определить типы строк.
     * Но это будет более трудоемко
     * @param s Список контейнеров строк первого файла
     * @param t Список контейнеров строк второго файла
     * @param idRowsIdx1 список индексов совпавших строк из первого файла
     * @param idRowsIdx2 список индексов совпавших строк из второго файла
     */
    private static void hashSubSequence(List<StringHolder> s, List<StringHolder> t,
                                    List<Integer> idRowsIdx1, List<Integer> idRowsIdx2) {
        int slen = s.size();
        int tlen = t.size();
        int[][] table = new int[slen + 1][tlen + 1];


        for (int i = 1; i <= slen; i++) {
            for (int j = 1; j <= tlen; j++) {
                if (s.get(i - 1).getHashcode() == t.get(j - 1).getHashcode() &&
                        s.get(i - 1).getString().equals(t.get(j - 1).getString())) {
                    table[i][j] = table[i - 1][j - 1] + 1;
                } else {
                    table[i][j] = Math.max(table[i - 1][j], table[i][j - 1]);
                }
            }
        }

        int index = table[slen][tlen];
        while (index != 0) {
            if (s.get(slen - 1).getHashcode() == t.get(tlen - 1).getHashcode() &&
                    s.get(slen - 1).getString().equals(t.get(tlen - 1).getString())){

                s.get(slen - 1).setType((byte)0);
                t.get(tlen - 1).setType((byte)0);
                idRowsIdx1.add(slen-1);
                idRowsIdx2.add(tlen-1);
                index--;
                slen--;
                tlen--;
            } else {
                if (table[slen - 1][tlen] < table[slen][tlen - 1]) {
                    tlen--;
                } else {
                    slen--;
                }
            }
        }

        Collections.reverse(idRowsIdx1);
        Collections.reverse(idRowsIdx2);
    }

    private static void fileReader(List<String> arr, String filename) {

        try (FileReader fr = new FileReader(new File(System.getProperty("user.dir") + "\\resources\\" + filename));
             BufferedReader reader = new BufferedReader(fr)) {

            String line = reader.readLine();
            while (line != null) {
                arr.add(line);
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fileWriter(String str, String filename){
        try(FileWriter fw = new FileWriter(new File(System.getProperty("user.dir") +"\\resources\\"+filename))){
            fw.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
