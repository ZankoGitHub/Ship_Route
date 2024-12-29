package Algoritmes;

import java.util.*;

public class GeneticAlgorithmTest {
    static final int WIDTH = 7; // عرض رودخانه
    static final int LENGTH = 50; // طول رودخانه
    static final int MAX_ENERGY = 100; // انرژی اولیه قایق
    static final int EMPTY = 0; // نشان‌دهنده مناطق خالی
    static int numObstacles = 81; // تعداد موانع
    private int[][] river;
    private Individual bestSolution;
    public GeneticAlgorithmTest(int[][] river){
        this.river = river;
        runAlgoritm();
    }
    // ساختار مسیر
    public class Individual {
        public List<String> path; // مسیر
        public int energy; // انرژی مصرف شده

        public Individual(List<String> path, int energy) {
            this.path = path;
            this.energy = energy;
        }
    }

    public void runAlgoritm() {
        // ایجاد جمعیت اولیه
        List<Individual> population = createInitialPopulation(100); // جمعیت 100 تایی

        // اجرای الگوریتم ژنتیک
        bestSolution = geneticAlgorithm(population, 1000); // تعداد تکرارها 1000

        System.out.println("Genetic Algorithm Best Path: " + bestSolution.path);
        System.out.println("Energy Used: " + bestSolution.energy);
    }

    // ایجاد جمعیت اولیه
    public List<Individual> createInitialPopulation(int populationSize) {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            List<String> path = generateRandomPath();
            int energy = calculateEnergy(path);
            population.add(new Individual(path, energy));
        }
        return population;
    }

    // تولید مسیر تصادفی
    public List<String> generateRandomPath() {
        List<String> path = new ArrayList<>();
        int x = 0, y = 0;
        while (x < LENGTH && y < WIDTH) {
            String move = randomMove(x,y);
            if (move == null) continue;
            path.add(move);
            switch (move) {
                case "UP":
                    if (x > 0) x--;
                    break;
                case "DOWN":
                    if (x < LENGTH - 1) x++;
                    break;
                case "LEFT":
                    if (y > 0) y--;
                    break;
                case "RIGHT":
                    if (y < WIDTH - 1) y++;
                    break;
                default:
                    break;
            }
        }
        return path;
    }

    // انتخاب حرکت تصادفی
    public String randomMove(int i, int j) {
        if (river[i][j] == 1) return null;
        if (i == 0 && j == 0){
            String[] moves = {"DOWN", "RIGHT"};
            return moves[(int)(Math.random() * moves.length)];
        }
        if (i == WIDTH-1 && j == 0){
            String[] moves = {"DOWN", "LEFT"};
            return moves[(int)(Math.random() * moves.length)];
        }
        if (i == 0 && j == LENGTH-1){
            String[] moves = {"UP", "RIGHT"};
            return moves[(int)(Math.random() * moves.length)];
        }
        if (i == 0){
            String[] moves = {"DOWN", "LEFT", "RIGHT"};
            return moves[(int)(Math.random() * moves.length)];
        }
        if (i == WIDTH-1){
            String[] moves = {"UP", "DOWN", "LEFT"};
            return moves[(int)(Math.random() * moves.length)];
        }
        if (j == 0){
            String[] moves = {"UP", "DOWN", "RIGHT"};
            return moves[(int)(Math.random() * moves.length)];
        }if (j == LENGTH-1){
            String[] moves = {"UP", "LEFT", "RIGHT"};
            return moves[(int)(Math.random() * moves.length)];
        }
        return null;
    }

    //
    public int[] findWidthAndLength (List<String> path){
        int x=0 , y=0 ;
        int[] temp = new int[2];
        for (String move : path) {
            switch (move) {
                case "UP":
                    x++;break;
                case "DOWN":
                    x--;break;
                case "LEFT":
                    y--;break;
                case "RIGHT":
                    y++;break;
                default:
                    break;
            }
        }
        temp[0] = x;
        temp[1] = y;
        return temp;
    }

    // محاسبه انرژی مصرف شده برای یک مسیر
    public int calculateEnergy(List<String> path) {
        int energy = 0;
        for (String move : path) {
            switch (move) {
                case "UP":
                case "DOWN":
                case "LEFT":
                case "RIGHT":
                case "STAY":
                    energy += 1; // حرکت به جلو، چپ یا راست 1 واحد انرژی می‌برد
                    break;
                default:
                    break;
            }
        }
        return energy;
    }

    // الگوریتم ژنتیک
    public Individual geneticAlgorithm(List<Individual> population, int generations) {
        for (int gen = 0; gen < generations; gen++) {
            // انتخاب والدین
            List<Individual> selected = selection(population);

            // تولید نسل بعد
            List<Individual> newGeneration = new ArrayList<>();
            for (int i = 0; i < population.size() / 2; i++) {
                Individual parent1 = selected.get(i);
                Individual parent2 = selected.get(i + 1);

// انجام عملگرهای crossover و mutation
                Individual child = crossover(parent1, parent2);
                mutate(child);

                newGeneration.add(child);
            }

            // جایگزینی نسل قدیم با نسل جدید
            population = newGeneration;
        }

        // بازگشت بهترین مسیر
        return getBestIndividual(population);
    }

    // انتخاب والدین از جمعیت
    public List<Individual> selection(List<Individual> population) {
        // انتخاب والدین بر اساس تناسب (fitness)
        population.sort(Comparator.comparingInt(i -> i.energy)); // ترتیب بر اساس کمترین انرژی
        return population.subList(0, population.size() / 2);
    }

    // عملگر crossover
    public Individual crossover(Individual parent1, Individual parent2) {
        List<String> childPath = new ArrayList<>();
        int crossoverPoint = (int) (Math.random() * Math.min(parent1.path.size(), parent2.path.size()));
        for (int i = 0; i < crossoverPoint; i++) {
            childPath.add(parent1.path.get(i));
        }
        for (int i = crossoverPoint; i < parent2.path.size(); i++) {
            childPath.add(parent2.path.get(i));
        }
        return new Individual(childPath, calculateEnergy(childPath));
    }

    // جهش (mutation)
    public void mutate(Individual individual) {
        if (Math.random() < 0.05) { // احتمال جهش 5%
            int mutationPoint = (int) (Math.random() * individual.path.size());
            int[] location = findWidthAndLength(individual.path);
            String newMove = randomMove(location[0],location[1]);
            individual.path.set(mutationPoint, newMove);
            individual.energy = calculateEnergy(individual.path); // به‌روزرسانی انرژی بعد از جهش
        }
    }

    // پیدا کردن بهترین فرد (مسیر با کمترین انرژی)
    public Individual getBestIndividual(List<Individual> population) {
        return population.stream().min(Comparator.comparingInt(i -> i.energy)).orElse(null);
    }

    public Individual getBestSolution() {
        return bestSolution;
    }
}