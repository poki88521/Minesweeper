package frame;

import tool.FontClass;

import java.awt.*;
import javax.swing.*;

public class ResultFrame {



    public ResultFrame(boolean ifWin){
        JFrame frame = new JFrame("结果");
        frame.setSize(300,100);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JLabel label = new JLabel();
        if(ifWin){
            label.setText("你找到了所有的雷！");
        }else{
            label.setText("爆炸了！");
        }

        label.setFont(FontClass.SONG20);
        label.setBounds(50, 20, 100, 50);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(label);

        frame.setVisible(true);
    }
}
