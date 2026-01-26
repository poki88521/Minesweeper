package frame;


import component.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import tool.*;

public class EasyFrame extends JFrame{
    //常量
    public static final int DEFAULT_MINE = 10;

    public static final int CLEAR = 1;
    public static final int CONTINUE = 2;
    public static final int FAIL = 3;
    //字体

    private int XNumber;
    private int YNumber;
    private int mine;
    private JFrame mainframe;
    private Block [][]easyBlocks;
    private boolean ifFirst;//是否为初始状态的变量
    private Stopwatch sw;

    public EasyFrame(int XNumber, int YNumber, int mine){
        easyBlocks = new Block[XNumber][YNumber];
        this.mine = mine;
        this.XNumber = XNumber;
        this.YNumber = YNumber;
        mainframe = initFrame(XNumber, YNumber);
        init();
        ifFirst = true;
    }

    private void initMine(int x, int y){
        easyBlocks = Tool.setMine(easyBlocks, mine, x ,y);
        easyBlocks = Tool.setNumber(easyBlocks);
        
    }

    private void init(){
        //界面初始化
        
        JMenuBar bar = initBar();
        mainframe.setJMenuBar(bar);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        //操作初始化(设置雷和数字)
        for(int i = 0; i < easyBlocks.length; i++){
            for(int j = 0; j < easyBlocks[i].length; j++){
                final int ii = i;
                final int jj = j;
                easyBlocks[i][j] = new Block(30 + 30 * i, 60 + 30 * j);
                sw = new Stopwatch();
                mainframe.add(sw.getLabel());

                //“时间”提示词
                JLabel timeWord = new JLabel("时间:");
                timeWord.setFont(FontClass.SONG20);
                timeWord.setBounds(30, 20, 70, 30);
                mainframe.add(timeWord);

                //绑定按键
                easyBlocks[i][j].getLabel().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent me) {
                        int b1b3 = (MouseEvent.BUTTON1_DOWN_MASK 
                            | MouseEvent.BUTTON3_DOWN_MASK);

                        //同时点击左右键
                        if((me.getModifiersEx() & b1b3) == b1b3){
                            easyBlocks = Tool.removeCover(easyBlocks,ii,jj);
                            try {
                                check();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        //点击左键
                        if(me.getButton() == MouseEvent.BUTTON1){
                            if(ifFirst == true){
                                ifFirst = false;
                                sw.start();
                                initMine(ii, jj);
                            }
                            easyBlocks = Tool.uncover(easyBlocks, ii, jj);
                            try {
                                check();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        //点击右键
                        if(me.getButton() == MouseEvent.BUTTON3){
                            easyBlocks[ii][jj].changeMark();
                        }
                    } 

                });
                panel.add(easyBlocks[i][j].getLabel());  
            }
        }

        //背景
        //ImageIcon bg = new ImageIcon(IMAGE_PATH + "background.png");
        //JLabel bgLabel = new JLabel(bg);
        //bgLabel.setBounds(27, 57, 304, 304);
        //panel.add(bgLabel);
        
        mainframe.add(panel);
        mainframe.setVisible(true);
    }

    //检测游戏状态
    private void check() throws InterruptedException{
        switch(Tool.checkOver(easyBlocks)){
            case EasyFrame.FAIL:
                //显示所有雷的位置（图标boommine）
                for (int i = 0; i < easyBlocks.length; i++) {
                    for (int j = 0; j < easyBlocks[i].length; j++) {
                        if(easyBlocks[i][j].getCondition() == Block.MINE){
                            easyBlocks[i][j].changeImage(Block.BOOMMINE);
                        }
                    }
                }
                //弹出窗口
                new ResultFrame(false);
                //秒表停止
                sw.setIfContinue(false);
                sw.interrupt();
                break;
            case EasyFrame.CLEAR:
                //全局uncover
                for (int i = 0; i < easyBlocks.length; i++) {
                    for (int j = 0; j < easyBlocks[i].length; j++) {
                        if(easyBlocks[i][j].getCondition() == Block.MINE){
                            easyBlocks[i][j].changeImage(Block.MINE);
                        }
                    }
                }
                //弹出窗口
                new ResultFrame(true);
                //秒表停止
                sw.setIfContinue(false);
                sw.interrupt();
                break;
            default:
                break;
        }
    }

    //初始化frame
    //界面长宽：边框宽（长）度*2 + block大小30（block本身大小28 + 边界空白2）*block数
    private JFrame initFrame(int xNumber, int yNumber){
        JFrame mainframe = new JFrame("MineSweeper");
        mainframe.setSize(70 + 30 * xNumber,150 + 30 * yNumber);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setLocationRelativeTo(null);
        mainframe.setResizable(false);
        mainframe.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent ke){
                switch(ke.getKeyCode()){
                    case KeyEvent.VK_SPACE:
                        showMine();
                        break;
                    case KeyEvent.VK_S:
                        Tool.show(easyBlocks);
                        break;
                    default:
                        break;
                }
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode() == KeyEvent.VK_SPACE){
                    hideMine();
                }
            }
            
        });
        return mainframe;
    }

    //作弊用，展示雷
    private void showMine(){
        //改变Block的图片
        for (int i = 0; i < easyBlocks.length; i++) {
            for (int j = 0; j < easyBlocks[i].length; j++) {
                easyBlocks[i][j].changeImage(easyBlocks[i][j].getCondition());
            }
        }
        //重新加载
        mainframe.repaint();
    }
    //作弊用，隐藏雷
    private void hideMine(){
        //改变Block的图片
        for (int i = 0; i < easyBlocks.length; i++) {
            for (int j = 0; j < easyBlocks[i].length; j++) {
                if(easyBlocks[i][j].getIfCover()){
                    easyBlocks[i][j].changeImage(easyBlocks[i][j].getMark());
                }else{
                    easyBlocks[i][j].changeImage(easyBlocks[i][j].getCondition());
                }
                
            }
        }
        //重新加载
        mainframe.repaint();
    }

    private JMenuBar initBar(){
        JMenuBar jmb = new JMenuBar();
        JMenu reset = new JMenu("重置");
        JMenu difficulty = new JMenu("难度");

        JMenuItem easy = new JMenuItem("简单");
        JMenuItem normal = new JMenuItem("普通");
        JMenuItem hard = new JMenuItem("困难");
        JMenuItem customize = new JMenuItem("自定义");

        //设置字体
        reset.setFont(FontClass.SONG16);
        difficulty.setFont(FontClass.SONG16);
        easy.setFont(FontClass.SONG16);
        normal.setFont(FontClass.SONG16);
        hard.setFont(FontClass.SONG16);
        customize.setFont(FontClass.SONG16);

        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me){
                //暂停
                sw.setIfContinue(false);
                sw.interrupt();
                //加载提示窗口
                /*
                new NoticeFrame(Thread.currentThread().getName());
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                 */
                //重置
                ifFirst = true;
                mainframe.dispose();
                new EasyFrame(XNumber, YNumber, mine);

            }
        });

        //绑定事件
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifFirst = true;
                mainframe.dispose();
                new EasyFrame(10, 10, 10);
            }
        });

        normal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifFirst = true;
                mainframe.dispose();
                new EasyFrame(15, 15, 40);
            }
        });

        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifFirst = true;
                mainframe.dispose();
                new EasyFrame(25, 15, 60);
            }
        });

        customize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifFirst = true;
                new CustomizeFrame();
                mainframe.dispose();
            }
        });
        
        //添加组件
        difficulty.add(easy);
        difficulty.add(normal);
        difficulty.add(hard);
        difficulty.add(customize);
        jmb.add(reset);
        jmb.add(difficulty);

        return jmb;
    }

    
}
