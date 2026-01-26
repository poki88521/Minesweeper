package frame;

import exceptions.IllegalNumberException;
import tool.FontClass;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;

public class CustomizeFrame extends JFrame{
    private int xNumber;
    private int yNumber;
    private int mine;

    public CustomizeFrame(){
        super("自定义");
        this.setSize(300,180);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        //添加文本框
        JTextField xText = new JTextField("横向方格数",25);
        JTextField yText = new JTextField("纵向方格数",25);
        JTextField mineText = new JTextField("雷数",25);
        xText.setFont(FontClass.SONG16);
        yText.setFont(FontClass.SONG16);
        mineText.setFont(FontClass.SONG16);
        xText.setForeground(Color.GRAY);
        yText.setForeground(Color.GRAY);
        mineText.setForeground(Color.GRAY);

        xText.addFocusListener(new FocusAdapter() {
            private String text;
            @Override
            public void focusLost(FocusEvent e) {
                if(!xText.getText().isEmpty()) {
                    text = xText.getText();
                    xText.setText(text);
                }else{
                    xText.setText("横向方格数");
                    xText.setForeground(Color.GRAY);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(text == null || text.isEmpty()){
                    xText.setForeground(Color.BLACK);
                    xText.setText("");
                }
            }
        });

        yText.addFocusListener(new FocusAdapter() {
            private String text;
            @Override
            public void focusLost(FocusEvent e) {
                if(!yText.getText().isEmpty()) {
                    text = yText.getText();
                    yText.setText(text);
                }else{
                    yText.setText("纵向方格数");
                    yText.setForeground(Color.GRAY);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(text == null || text.isEmpty()){
                    yText.setForeground(Color.BLACK);
                    yText.setText("");
                }
            }
        });

        mineText.addFocusListener(new FocusAdapter() {
            private String text;
            @Override
            public void focusLost(FocusEvent e) {
                if(!mineText.getText().isEmpty()) {
                    text = mineText.getText();
                    mineText.setText(text);
                }else{
                    mineText.setText("雷数");
                    mineText.setForeground(Color.GRAY);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if(text == null || text.isEmpty()){
                    mineText.setForeground(Color.BLACK);
                    mineText.setText("");
                }
            }
        });

        //添加按钮
        JButton sure = new JButton("确定");
        sure.setSize(50,30);
        sure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //获取数字
                    xNumber = Integer.parseInt(xText.getText());
                    yNumber = Integer.parseInt(yText.getText());
                    mine = Integer.parseInt(mineText.getText());
                    //检查
                    checkNumber();
                    //检查成功后关闭
                    new EasyFrame(xNumber, yNumber, mine);
                    CustomizeFrame.this.dispose();
                } catch (Exception ex) {
                    if(ex instanceof NumberFormatException){
                        new NoticeFrame("illegal format");
                    }
                    if(ex instanceof IllegalNumberException){
                        new NoticeFrame(ex.getMessage());
                    }
                }
            }
        });

        JPanel p1 = new JPanel();
        p1.add(xText);
        p1.add(yText);
        p1.add(mineText);
        p1.add(sure);
        JPanel card = new JPanel(new CardLayout());
        card.add(p1,"p1");
        CardLayout cl = (CardLayout)(card.getLayout());
        cl.show(card, "p1");

        this.add(card);
        this.setFocusable(true);
        this.setVisible(true);
    }

    private void checkNumber() throws IllegalNumberException{
        if(xNumber < 10 || yNumber < 10){
            throw new IllegalNumberException("x or y too small");
        }else if(xNumber > 30 || yNumber > 30){
            throw new IllegalNumberException("x or y too big");
        }else if(mine < 10){
            throw new IllegalNumberException("mine number too small");
        }else if(mine > (xNumber * yNumber * 0.6)){
            throw new IllegalNumberException("mine number too big");
        }
    }

    public int getXNumber() {
        return xNumber;
    }

    public void setXNumber(int xNumber) {
        this.xNumber = xNumber;
    }

    public int getyNumber() {
        return yNumber;
    }

    public void setyNumber(int yNumber) {
        this.yNumber = yNumber;
    }

    public int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }
}
