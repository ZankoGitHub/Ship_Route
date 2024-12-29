package Algoritmes;

import java.util.*;

public class AStar {
    static final int WIDTH = 7; // عرض رودخانه
    static final int LENGTH = 50; // طول رودخانه
    static final int MAX_ENERGY = 100; // انرژی اولیه قایق
    private int counter ; // نشان‌دهنده تعداد نقاط طی شده

    private int[][] river;
    private State bestState;

    public AStar(int[][] river){
        this.river = river;
        this.counter = 0;
        runAStar();
    }
    // ساختار موقعیت قایق
    public class State {
        public int x, y; // مختصات قایق
        public int energy; // انرژی مصرف شده
        public int gCost; // هزینه واقعی تا حالا
        public int hCost; // تخمین هزینه باقی‌مانده
        public List<String> path; // مسیر طی شده

        public State(int x, int y, int energy, int gCost, int hCost, List<String> path) {
            this.x = x;
            this.y = y;
            this.energy = energy;
            this.gCost = gCost;
            this.hCost = hCost;
            this.path = path;
        }

        // محاسبه f(n) = g(n) + h(n)
        public int fCost() {
            return gCost + hCost;
        }
    }

    public void runAStar() {

        // اجرای الگوریتم A*
        bestState = aStar(0, 0, LENGTH - 1, WIDTH - 1);

        if (bestState != null) {
            System.out.println("AStar total node search: " + counter);
            System.out.println("AStar Best Path: " + bestState.path);
            System.out.println("Energy Used: " + bestState.energy);
        } else {
            System.out.println("No valid path found for AStar.");
            bestState = new State(0,0,0,0,0,null);
        }
        System.out.println();
    }

    // تخمین فاصله به هدف با استفاده از Manhattan Distance
    public int heuristic(int x, int y, int endX, int endY) {
        return Math.abs(x - endX) + Math.abs(y - endY);
    }

    // الگوریتم A* برای پیدا کردن بهترین مسیر
    public State aStar(int startX, int startY, int endX, int endY) {
        PriorityQueue<State> openList = new PriorityQueue<>(Comparator.comparingInt(State::fCost));
        Set<String> closedList = new HashSet<>();

        // حالت اولیه
        List<String> initialPath = new ArrayList<>();
        openList.add(new State(startX, startY, 0, 0, heuristic(startX, startY, endX, endY), initialPath));

        // حرکت‌های مجاز
        int[] dx = {1, -1, 0, 0}; // پایین، بالا، چپ، راست
        int[] dy = {0, 0, -1, 1};
        String[] moves = {"DOWN", "UP", "LEFT", "RIGHT"};

        while (!openList.isEmpty()) {
            State current = openList.poll();

            // اگر به نقطه پایان رسیدیم، مسیر را برگردانیم
            if (current.x == endX && current.y == endY) {
                return current;
            }

            // بازدید از گره‌های مجاور
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dx[i];
                int newY = current.y + dy[i];

                // بررسی محدوده‌ها و برخورد با موانع
                if (newX >= 0 && newX < LENGTH && newY >= 0 && newY < WIDTH && this.river[newX][newY] != 1) {
                    int moveCost = 1;
                    int newEnergy = current.energy + moveCost;

                    if (newEnergy <= MAX_ENERGY) {
                        List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(moves[i]);

                        // وضعیت جدید
                        State nextState = new State(newX, newY, newEnergy,
                                current.gCost + moveCost, heuristic(newX, newY, endX, endY), newPath);

                        String stateKey = newX + "," + newY;
                        if (!closedList.contains(stateKey)) {
                            openList.add(nextState);
                            counter++;
                        }
                    }
                }
            }
            closedList.add(current.x + "," + current.y);
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
