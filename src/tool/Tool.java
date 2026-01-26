package tool;

import component.*;
import frame.*;
import java.util.*;

public class Tool {

    public static Block[][] setMine(Block[][] arr, int mineNum, int x, int y){
        //初始化
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j].setCondition(Block.EMPTY);
            }
        }

        //填充雷
        int count = 0;
            for(int i = 0; i < arr.length; i++){
                for(int j = 0; j < arr[i].length; j++){
                    if(count == mineNum) break;
                    //防止本格有雷：
                    if(i == x && j == y) continue;
                    //防止周围有雷：
                    if(Math.abs(i - x) == 1 || Math.abs(j - y) == 1)continue;
                    arr[i][j].setCondition(Block.MINE);
                    count++;
                }
                if(count == mineNum) break;
            }
        
        Random r = new Random();
        //打乱
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(i == x && j == y) continue;//当遍历至x y处跳过
                if(Math.abs(i - x) == 1 || Math.abs(j - y) == 1)continue;//周围
                int i2 = r.nextInt(10);
                int j2 = r.nextInt(10);
                if(i2 == x && j2 == y) continue;//x y处禁止所有的交换以避免雷
                if(Math.abs(i2 - x) == 1 || Math.abs(j2 - y) == 1)continue;//周围
                int temp = arr[i][j].getCondition();
                arr[i][j].setCondition(arr[i2][j2].getCondition());
                arr[i2][j2].setCondition(temp);
            }
        }

        show(arr);
        return arr;
    }

    //设置数字
    public static Block[][] setNumber(Block[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(arr[i][j].getCondition() == 0){
                    arr[i][j].setCondition(getMine(arr, i, j));
                }
            }
        }
        return arr;
    }

    //边界判断并计算雷数
    //可以确保i,j在数组内,而m,n不可以
    private static int getMine(Block[][] arr, int i, int j){
        int count = 0;
        for(int m = i - 1; m <= i + 1; m++){
            for(int n = j - 1; n <= j + 1; n++){
                if(m >= arr.length 
                || n >= arr[j].length
                || m < 0
                || n < 0) continue;//检测边界
                if(arr[m][n].getCondition() == Block.MINE) count++;
            }
        }
        return count;
    }

    //点开Block的方法，主要构建点开空Block时的情况
    public static Block[][] uncover(Block[][] arr, int i, int j) {
        //已揭开直接返回
        if(arr[i][j].getIfCover() == false) return arr;
        //如果点击有标记的要先去除标记
        if(arr[i][j].getMark() != Block.COVER){
            arr[i][j].setMark(Block.COVER);
            arr[i][j].changeImage(Block.COVER);
            return arr;
        }
        //揭开
        arr[i][j].setIfCover(false);
        arr[i][j].changeImage(arr[i][j].getCondition());
        //如果为空则递归
        if(arr[i][j].getCondition() == Block.EMPTY){
            for(int m = i - 1; m <= i + 1; m++){
                for(int n = j - 1; n <= j + 1; n++){
                    if(m >= arr.length || n >= arr[j].length
                    || m < 0 || n < 0) continue;//边界检测

                    //空和数字都可以调用递归
                    if(arr[m][n].getCondition() < Block.MINE){
                        arr = uncover(arr, m, n);
                    }
                }
            }
        }else if(arr[i][j].getCondition() == Block.MINE){
            arr[i][j].setCondition(Block.BOOMMINE);
            arr[i][j].changeImage(arr[i][j].getCondition());
        }
            
        
        return arr;
    }

    //移除数字周围的cover
    public static Block[][] removeCover(Block[][] arr, int i, int j){
        //只有uncover方块才可以进行此操作
        if(arr[i][j].getIfCover() == true) return arr;
        //先判断旗子数量是否等于数字
        int flagCount = 0;
        for (int m = i - 1; m <= i + 1; m++) {
            for (int n = j - 1; n <= j + 1; n++) {
                if(m >= arr.length 
                || n >= arr[j].length
                || m < 0
                || n < 0) continue;//检测边界

                if(arr[m][n].getIfCover() == false) continue;//只判断cover的Block
                if(arr[m][n].getMark() == Block.FLAG) flagCount++;
                if(arr[m][n].getCondition() == Block.EMPTY) uncover(arr, m, n);
            }
        }
        if(flagCount != arr[i][j].getCondition()) return arr;
        //移除开始
        for(int m = i - 1; m <= i + 1; m++){
            for(int n = j - 1; n <= j + 1; n++){
                if(m >= arr.length 
                || n >= arr[j].length
                || m < 0
                || n < 0) continue;//检测边界
                if(arr[m][n].getIfCover() == false) continue;//只判断cover的Block
                if(arr[m][n].getMark() != Block.FLAG){
                    arr[m][n].setIfCover(false);
                    arr[m][n].changeImage(arr[m][n].getCondition());
                    
                    if(arr[m][n].getCondition() == Block.MINE){
                        arr[m][n].setCondition(Block.BOOMMINE);
                        arr[m][n].changeImage(Block.BOOMMINE);
                    }
                    
                }
                //遇到空白要递归展开
                if(arr[m][n].getCondition() == Block.EMPTY){
                    arr = uncover(arr, m, n);
                }
            }
        }
        return arr;
    }

    //检测游戏是否结束
    public static int checkOver(Block[][] arr){
        boolean ifContinue = false;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                //检测是否已经爆炸
                if(arr[i][j].getCondition() == Block.BOOMMINE) {
                    return EasyFrame.FAIL;
                }
                //检测游戏是否还在继续
                if(arr[i][j].getCondition() != Block.MINE
                && arr[i][j].getIfCover() == true) ifContinue = true;
            }
        }
        if(ifContinue) return EasyFrame.CONTINUE;
        return EasyFrame.CLEAR;
    }

    //根据线程名字获取线程
    public static Thread getThreadByName(String name){
        for(Thread t : Thread.getAllStackTraces().keySet()){
            if(t.getName().equals(name)) return t;
        }
        return null;
    }

    //测试用
    public static void show(Block[][] arr){
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[i].length; j++){
                System.out.print(arr[i][j].getCondition() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
