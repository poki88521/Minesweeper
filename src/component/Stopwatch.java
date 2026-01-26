package component;

import tool.FontClass;

import java.awt.*;
import javax.swing.*;

public class Stopwatch extends Thread{

    private JLabel label;
    private long time;//ms
    private String timestr;
    private boolean ifContinue;

    public Stopwatch(){
        ifContinue = true;
        time = 0;
        timestr = "";
        label = new JLabel();
        label.setBounds(90, 20, 100, 30);
        label.setFont(FontClass.SONG20);
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        while(ifContinue){
            time = System.currentTimeMillis() - start;
            String timestrNow = getTimeString();
            if(!timestrNow.equals(timestr)){
                label.setText(timestrNow);
                timestr = timestrNow;
            }
        }
    }

    private String getTimeString(){
        String millis = "";
        String sec = "";
        String min = "";
        if(time >= (1000 * 60 * 60) - 1)return "Time Out";//超时
        millis = Long.toString((time % 1000) / 10);
        sec = Long.toString((time / 1000) % 60);
        min = Long.toString((time / 1000) / 60);
        if(millis.length() != 2){
            millis = "0" + millis;
        }
        if(sec.length() != 2){
            sec = "0" + sec;
        }
        if(min.length() != 2){
            min = "0" + min;
        }
        return min + ':' +sec +  ':' + millis;
    }


    public JLabel getLabel() {
        return this.label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimestr() {
        return this.timestr;
    }

    public void setTimestr(String timestr) {
        this.timestr = timestr;
    }

    public boolean getIfContinue(){
        return this.ifContinue;
    }

    public void setIfContinue(boolean ifContinue){
        this.ifContinue = ifContinue;
    }

}
