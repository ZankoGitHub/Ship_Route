package Main;

import Algoritmes.AStar;
import Algoritmes.BFS;
import Algoritmes.DFS;
import Algoritmes.GeneticAlgorithm;
import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Graphic {

    private static int algorithms ;
    private static boolean fillRiverType = false ;
    private static List<String> bestRoad;
    private static int[][] riverData;
    private static RiverSimulation simulation;

    //Algorithms
    private static DFS dfs;
    private static BFS bfs;
    private static AStar aStar;
    private static GeneticAlgorithm algorithm;

        public static void main(String[] args) throws IOException {
            JFrame frame = new JFrame("River Simulation");
            frame.setSize(500,700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            JLabel label = new JLabel("     Algorithm");


            // Buttons
            JButton randomFillBtn = new JButton("Random fill");
            JButton button = new JButton("Change Algorithm");


//            frame.getContentPane().add(panel);
//            frame.setVisible(true);
            Random random = new Random();

            bestRoad = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            algorithms = 0;




            //رسم عادی رودخانه
//            for (int i = 0; i < test.length; i++) {
//                for (int j = 0; j < test[i].length; j++) {
//                    System.out.print(test[i][j] + " ");
//                }
//                System.out.println();
//            }
//


            // اجرای الگوریتم‌ها و بروزرسانی موقعیت قایق در اینجا انجام شود.
            riverData = objectMapper.readValue(new File("src/sample.json"), int[][].class);
            bfs = new BFS(riverData);
            dfs = new DFS(riverData);
            aStar = new AStar(riverData);
            //algorithm = new GeneticAlgorithm(riverData);
            //test = new GeneticAlgorithmTest(riverData);

            simulation = new RiverSimulation(riverData);

            panel.add(randomFillBtn);
            panel.add(label);
            panel.add(button);
            panel.add(simulation);
            JScrollPane scrollPane = new JScrollPane(panel);
            scrollPane.setPreferredSize(new Dimension(750,850));
            panel.setPreferredSize(new Dimension(450,450));
            frame.getContentPane().add(panel);
            frame.setVisible(true);

            // اکشن دکمه ها
            button.addActionListener(e -> {
                if (simulation != null){
                    panel.remove(simulation);
                }
                algorithms = (algorithms + 1) % 3;
                if (algorithms == 0){
                    bestRoad = aStar.getBestState().path;
                    label.setText("     A Star ");
                } else if (algorithms == 1) {
                    bestRoad = dfs.getBestState().path;
                    label.setText("     DFS    ");
                } else if (algorithms == 2) {
                    bestRoad = bfs.getBestState().path;
                    label.setText("     BFS    ");
                }
                if (bestRoad == null){
                    simulation = new RiverSimulation(riverData);
                    label.setText(label.getText()+"not found.");
                }else {
                    simulation = new RiverSimulation(riverData,bestRoad);
                }
                panel.add(simulation);
                frame.getContentPane().add(panel);
                frame.setVisible(true);
            });

            randomFillBtn.addActionListener(e -> {
                if (fillRiverType){
                    //پر کردن بر اساس اطلاعات اولیه موانع
                    try {
                        riverData = objectMapper.readValue(new File("src/sample.json"), int[][].class);
                        fillRiverType = false;
                        randomFillBtn.setText("Random fill");

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    // ابتدا رودخانه را از موانع خالی می کنیم
                    for (int i=0 ; i<50 ; i++){
                        for (int j=0 ; j<7 ; j++){
                            riverData[i][j] = 0;
                        }
                    }
                    for (int i=0 ; i<81 ; i++){
                        //پر کردن تصادفی موانع
                        int x = random.nextInt(0,100) % 50;
                        int y = random.nextInt(0,100) % 7;
                        if ((x==0 && y==0) || (x==49 && y==6) || riverData[x][y]==1){
                            i--;
                        }else {
                            riverData[x][y] = 1;
                        }
                    }
                    fillRiverType = true;
                    randomFillBtn.setText("Normal fill");
                }
                panel.remove(simulation);
                simulation = new RiverSimulation(riverData);
                bfs = new BFS(riverData);
                dfs = new DFS(riverData);
                aStar = new AStar(riverData);
                //algorithm = new GeneticAlgorithm(riverData);
                //test = new GeneticAlgorithmTest(riverData);

                //button.doClick();
                panel.add(simulation);
            });
        }

}