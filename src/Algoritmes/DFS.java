package Algoritmes;

import java.util.*;

public class DFS {

    static final int WIDTH = 7; // عرض رودخانه
    static final int LENGTH = 50; // طول رودخانه
    static final int MAX_ENERGY = 100; // انرژی اولیه قایق
    static int counter = 0; // نشان‌دهنده تعداد نقاط طی شده

    // ماتریس رودخانه
    private int[][] river;
    private State bestState;
    public DFS(int[][] river){
        this.river = river;
        runDFS();
    }
    // ساختار موقعیت قایق
    public class State {
        public int x, y; // مختصات قایق
        public int energy; // انرژی مصرف شده
        public List<String> path; // مسیر طی شده

        public State(int x, int y, int energy, List<String> path) {
            this.x = x;
            this.y = y;
            this.energy = energy;
            this.path = path;
        }
    }

    public void runDFS() {

        // اجرای الگوریتم DFS
        bestState = dfs(0, 0, LENGTH - 1, WIDTH - 1);

        if (bestState != null) {
            System.out.println("BFS total node search: " + counter);
            System.out.println("DFS Best Path: " + bestState.path);
            System.out.println("Energy Used: " + bestState.energy);
        } else {
            System.out.println("No valid path found for DFS.");
            bestState = new State(0,0,0,null);
        }
        System.out.println();
    }

    // حرکت‌های مجاز: پایین، بالا، چپ، راست
    static int[] dx = {1, -1, 0, 0}; // جهت‌های عمودی (پایین، بالا)
    static int[] dy = {0, 0, -1, 1}; // جهت‌های افقی (چپ، راست)
    static String[] moves = {"DOWN", "UP", "LEFT", "RIGHT"};

    // الگوریتم DFS برای پیدا کردن بهترین مسیر
    public State dfs(int startX, int startY, int endX, int endY) {
        Stack<State> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // وضعیت اولیه
        List<String> initialPath = new ArrayList<>();
        stack.push(new State(startX, startY, 0, initialPath));

        while (!stack.isEmpty()) {
            State current = stack.pop();

            // اگر به نقطه پایان رسیدیم، مسیر را برگردانیم
            if (current.x == endX && current.y == endY) {
                return current;
            }

            // بررسی گره‌های مجاور
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];

                // بررسی محدوده‌ها و برخورد با موانع
                if (newX >= 0 && newX < LENGTH && newY >= 0 && newY < WIDTH && this.river[newX][newY] != 1) {
                    int moveCost = 1; // هزینه حرکت به جلو، چپ و راست 1 است
                    int newEnergy = current.energy + moveCost;

                    // اگر قایق توانایی حرکت را دارد (انرژی کافی)
                    if (newEnergy <= MAX_ENERGY) {
                        List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(moves[i]);

                        State nextState = new State(newX, newY, newEnergy, newPath);
                        String stateKey = newX + "," + newY;

                        // بررسی نشده بودن گره
                        if (!visited.contains(stateKey)) {
                            stack.push(nextState);
                            visited.add(stateKey);
                            counter++;
                        }
                    }
                }
            }
        }

        // اگر مسیری پیدا نشد
        return null;
    }

    public State getBestState() {
        return bestState;
    }
}
