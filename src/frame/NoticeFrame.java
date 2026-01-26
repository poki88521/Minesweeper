package frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import tool.*;


//所有提示窗口组成的类
public class NoticeFrame extends JFrame{

    public NoticeFrame(String content){
        super("提示");
        this.setSize(300,150);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        initText(content);
        this.setVisible(true);
    }

    private void initText(String content){
        //设置文字
        String showWord = "";
        switch(content){
            case "illegal format":
                showWord = "确定输入的是数字？";
                break;
            case "x or y too small":
                showWord = "横向或纵向的长度太短！";
                break;
            case "x or y too big":
                showWord = "横向或纵向的长度太长！";
                break;
            case "mine number too small":
                showWord = "雷数太少！";
                break;
            case "mine number too big":
                showWord = "雷数不可超过总格数的60%！";
                break;
            default:
                break;
        }
        JLabel text = new JLabel(showWord);
        text.setFont(FontClass.SONG20);
        text.setHorizontalAlignment(JLabel.CENTER);
        this.add(text, BorderLayout.CENTER);

    }
}
