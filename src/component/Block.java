package component;

import tool.PathClass;

import java.awt.event.*;
import javax.swing.*;


public class Block extends MouseAdapter{
    //常量
    public static final int EMPTY = 0;
    public static final int MINE = 9;
    public static final int BOOMMINE = 10;
    public static final int COVER = 11;
    public static final int FLAG = 12;
    public static final int QUESTION = 13;

    //变量
    private boolean ifCover;
    private int condition;
    private int mark;
    private ImageIcon image;
    private JLabel label;
    private int x;
    private int y;

    //构造
    public Block(int x, int y){
        this.ifCover = true;
        this.condition = EMPTY;
        this.image = new ImageIcon(PathClass.IMAGE_PATH + "cover.png");
        this.label = new JLabel(image);
        label.setBounds(x, y, 28, 28);//此处x y为在窗口中的坐标
        this.x = x;
        this.y = y;
        this.mark = Block.COVER;
    }

    //Block改变图片外观，并重置此JLabel
    public void changeImage(int condition){
        String newPath = PathClass.IMAGE_PATH;
        switch(condition){
            case Block.BOOMMINE:
                newPath += "boommine";
                break;
            case Block.COVER:
                newPath += "cover";
                break;
            case Block.FLAG:
                newPath += "flag";
                break;
            case Block.QUESTION:
                newPath += "question";
                break;
            default:
                newPath += condition;
                break;    
        }
        newPath += ".png";
        this.image = new ImageIcon(newPath);
        this.label.setIcon(image); 
    }

    //changeMark方法，主要切换flag，cover和question的标记
    public void changeMark(){
        if(this.getIfCover() == false) return;
        if(this.mark == Block.QUESTION){
            this.mark = Block.COVER;
            changeImage(mark);
        }else{
            this.mark++;
            changeImage(mark);
        }
    }


    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getIfCover() {
        return this.ifCover;
    }

    public void setIfCover(boolean ifCover) {
        this.ifCover = ifCover;
    }

    public int getCondition() {
        return this.condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public JLabel getLabel(){
        return this.label;
    }

    public void setLabel(JLabel label){
        this.label = label;
    }

    public JLabel getImage(){
        return this.label;
    }

    public void setImage(ImageIcon image){
        this.image = image;
    }

    public int getMark(){
        return mark;
    }

    public void setMark(int mark){
        this.mark = mark;
    }
    
}

//mode: cover, uncover
//condition: empty, 1~8, mine