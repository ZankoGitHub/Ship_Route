package Algoritmes;

import java.util.*;

public class BFS {
    static final int WIDTH = 7; // عرض رودخانه
    static final int LENGTH = 50; // طول رودخانه
    static final int MAX_ENERGY = 100; // انرژی اولیه قایق
    private int counter ; // نشان‌دهنده تعداد نقاط طی شده
    private int[][] river;
    public State bestState;
    public BFS(int[][] river){
        this.river = river;
        this.counter = 0;
        runBFS();
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

    public void runBFS() {
        // اجرای الگوریتم BFS
        bestState = bfs(0, 0, LENGTH - 1, WIDTH - 1);

        if (bestState != null) {
            System.out.println("BFS total node search: " + counter);
            System.out.println("BFS Best Path: " + bestState.path);
            System.out.println("Energy Used: " + bestState.energy);
        } else {
            System.out.println("No valid path found for BFS.");
            bestState = new State(0,0,0,null);
        }
        System.out.println();
    }

    // الگوریتم BFS برای پیدا کردن بهترین مسیر
    public State bfs(int startX, int startY, int endX, int endY) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // حالت اولیه
        List<String> initialPath = new ArrayList<>();
        queue.add(new State(startX, startY, 0, initialPath));

        // حرکت‌های مجاز
        int[] dx = {1, -1, 0, 0}; // پایین، بالا، چپ، راست
        int[] dy = {0, 0, -1, 1};
        String[] moves = {"DOWN", "UP", "LEFT", "RIGHT"};

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // اگر به نقطه پایان رسیدیم، مسیر را برگردانیم
            if (current.x == endX && current.y == endY) {
                return current;
            }

            // بازدید از گره‌های مجاور
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];

                // بررسی محدوده‌ها و برخورد با موانع
                if (newX >= 0 && newX < LENGTH && newY >= 0 && newY < WIDTH && river[newX][newY] != 1) {
                    int newEnergy = current.energy + 1; // حرکت به جلو انرژی مصرف می‌کند
                    if (newEnergy <= MAX_ENERGY) {
                        List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(moves[i]);

                        // وضعیت جدید
                        State nextState = new State(newX, newY, newEnergy, newPath);

                        String stateKey = newX + "," + newY;
                        if (!visited.contains(stateKey)) {
                            visited.add(stateKey);
                            queue.add(nextState);
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

    public int getCounter() {
        return counter;
    }
}
