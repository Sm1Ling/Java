package ru.hse.edu.azamatarifkhanov192;

import java.util.*;
import java.util.concurrent.Semaphore;


public class SongSinger {

    private final Map<String, Pair<Semaphore,Singer>> singers = new HashMap<>();
    private final ArrayList<String[]> schedule = new ArrayList<>();
    private final Semaphore globalSemaphore;

    public SongSinger(String[][] songText){

        if(songText.length == 0){
            throw new IllegalArgumentException("There are no sentences in song");
        }

        Set<String> uniqueNames = new HashSet<>();

        for(String[]line: songText){
            schedule.add(line[0].split(", "));
            uniqueNames.addAll(List.of(schedule.get(schedule.size() - 1)));
        }

        globalSemaphore = new Semaphore(uniqueNames.size());

        for(int i = 0; i < songText.length; i++){
            for(String name: schedule.get(i)){
                if(!singers.containsKey(name)){
                    Semaphore sem = new Semaphore(1);
                    singers.put(name,new Pair<>(sem, new Singer(name,sem, globalSemaphore)));
                }
                singers.get(name).value2.addSongLine(songText[i][1]);
            }
        }

        if(singers.isEmpty()){
            throw new IllegalArgumentException("There are no singers for the song");
        }
    }

    public void singSong() throws InterruptedException {

        singers.forEach((k,v) -> v.value2.start());

        globalSemaphore.acquire(singers.size());

        for(String[] line: schedule){

            for(String name: line){
                singers.get(name).value1.release();
            }
            globalSemaphore.acquire(line.length);
        }
    }

}

