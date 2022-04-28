package ru.hse.edu.azamatarifkhanov192;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Singer extends Thread{

    private final String name;
    private final List<String> songText = new ArrayList<>();
    private final Semaphore localSem;
    private final Semaphore globalSem;

    public Singer(String name, Semaphore local, Semaphore global){
        this.name = name;
        localSem = local;
        globalSem = global;
    }

    public void addSongLine(String line){
        songText.add(line);
    }


    @Override
    public void run() {
        try
        {
            localSem.acquire();
            for(String line: songText){
                localSem.acquire();
                System.out.println(name + ": " + line + "\n");
                globalSem.release();
            }
        }
        catch(InterruptedException e) {
            System.out.println ("Что-то пошло не так!");
        }
    }
}
