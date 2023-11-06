package com.example.game;

public class MyThread {

    private Thread thread;
    private Runnable runnable;

    public MyThread( Runnable runnable ){
        thread = new Thread(runnable);
        this.runnable = runnable;
    }

    public void start(){
        if (thread != null){
            thread.start();
        }
    }

    public void restart() {
        thread = new Thread(runnable);
        thread.start();
    }

}
