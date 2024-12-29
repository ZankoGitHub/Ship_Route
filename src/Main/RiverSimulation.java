package Main;

import Enums.Move;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RiverSimulation extends JPanel {
    static final int WIDTH = 7; // عرض رودخانه
    static final int LENGTH = 50; // طول رودخانه
    private int[][] river;// ماتریس رودخانه
    private List<String> path;//لیست حرکت قایق در طول کوتاه ترین مسیر
    private Graphics graphic; //گرافیک ماتریس

    public RiverSimulation(int[][] river,List<String> path) {
        this.river = river;
        this.path = path;
    }
    public RiverSimulation(int[][] river) {
        this.river = river;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawRiver(g);
    }

    private void drawRiver(Graphics g) {
        for (int i = 0; i < river.length; i++) {
            for (int j = 0; j < river[i].length; j++) {
                if (river[i][j] == 1) { // مانع
                    g.setColor(Color.RED);
                    g.fillRect(j*16 , i*16,15, 15); // اندازه مستطیل به دلخواه تغییر یابد
                } else { // فضای خالی
                    g.setColor(Color.BLUE);
                    g.fillRect(j*16 , i*16,15, 15);
                }
                if(j == 6 && i == 49){
                    g.setColor(Color.black);
                    g.fillRect(6*16 , 49*16,15, 15);
                }
            }
        }
        boatInRoad(0,0,g, Move.RIGHT);
        this.graphic = g;
        boatPath(0,0,g,path);
    }
    //موقعیت لحظه ای قایق
    private void boatInRoad (int i , int j , Graphics g,Move move){
        // رسم قایق در لحظه
        g.setColor(Color.white);
        if (move == Move.UP || move == Move.DAWN){
            g.fillRect((j*16)+4, (i*16)+2, 5, 10);
            g.fillRect((j*16)+1, (i*16)+6, 3, 3);
        }else if (move == Move.RIGHT || move == Move.LEFT){
            g.fillRect((j*16)+2, (i*16)+3, 10, 5);
            g.fillRect((j*16)+6, (i*16)+1, 3, 3);
        }
    }
    //حرکت قایق
    public void boatPath(int i , int j, Graphics graphic, List<String> path) {

        //نمایش مسیر فایق
        if (path != null) {
            for (String string : path){

                switch (string){
                    case "UP":
                        if (i > 0){
                            i--;
                            boatPathDraw(i,j,graphic,Move.UP);
                        }
                        break;
                    case "DOWN":
                        if (i < LENGTH - 1){
                            i++;
                            boatPathDraw(i,j,graphic,Move.DAWN);
                        }
                        break;
                    case "LEFT":
                        if (j > 0){
                            j--;
                            boatPathDraw(i,j,graphic,Move.LEFT);
                        }
                        break;
                    case "RIGHT":
                        if (j < WIDTH - 1){
                            j++;
                            boatPathDraw(i,j,graphic,Move.RIGHT);
                        }
                        break;
                    default:

                        break;
                }
            }
        }
    }
    //مسیر حرکت قایق
    private void boatPathDraw (int i , int j , Graphics g,Move move){
        g.setColor(Color.GREEN);
        if (move == Move.UP || move == Move.DAWN){
            g.fillRect((j*16)+5, i*16, 5, 16);
        }else if (move == Move.RIGHT || move == Move.LEFT){
            g.fillRect(j*16, (i*16)+5, 16, 5);
        }
    }

}
