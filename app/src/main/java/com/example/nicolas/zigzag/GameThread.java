package com.example.nicolas.zigzag;

/**
 * Created by Nicolas on 25/10/2016.
 */

public class GameThread extends Thread {
    // on définit arbitrairement le nombre d'images par secondes à 30
    private final static int FRAMES_PER_SECOND = 60;

    // si on veut X images en 1 seconde, soit en 1000 ms,
    // on doit en afficher une toutes les (1000 / X) ms.
    private final static int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
    private final ZigZagView view;
    private boolean running = false; // état du thread, en cours ou non
    public GameThread(ZigZagView view){
        this.view = view;
    }
    public void setRunning(boolean run) {
        running = run;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        while(running){
            long startTime;
            long sleepTime;
            startTime = System.currentTimeMillis();
            view.moveZigzagsToBottom();
            view.moveBall();
            view.verifierBall();
            view.postInvalidate();
            sleepTime = SKIP_TICKS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0) {sleep(sleepTime);}
            }
            catch (Exception e) {}
        }
    }
}

