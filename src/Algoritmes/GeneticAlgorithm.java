package Algoritmes;

import java.io.IOException;
import java.util.*;

public class GeneticAlgorithm {

    private static final int RIVER_WIDTH = 7;
    private static final int RIVER_LENGTH = 50;
    private static final int MAX_ENERGY = 100;
    private static final int NUM_GENERATIONS = 1000;
    private static final int POPULATION_SIZE = 50;
    private static final double CROSSOVER_RATE = 0.7;
    private static final double MUTATION_RATE = 0.1;

    private int[][] river; // نقشه رودخانه و موانع
    private Chromosome bestRoad ; // بهترین مسیر
    private List<Chromosome> bestInGeneration ;// لیست بهترین مسیر در هر جهش

    public GeneticAlgorithm(int[][] river){
        this.river = river;
        runAlgorithm();
    }

    public void runAlgorithm() {
        // ایجاد جمعیت اولیه
        List<Chromosome> population = generateInitialPopulation();
        bestInGeneration = new ArrayList<>();
        Chromosome start = new Chromosome("");
        population.add(start);population.add(start);

        // اجرای الگوریتم ژنتیک
        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            // ارزیابی جمعیت
            evaluatePopulation(population);

            // ایجاد نسل جدید
            population = createNewGeneration(population);

            // چاپ بهترین مسیر در هر نسل (برای بررسی)
            Chromosome best = getBestChromosome(population);
//            System.out.println("Generation " + generation + " Best Energy: " + best.getEnergy() +
//                    " Path: " + best.getPath());

            bestInGeneration.add(best);
        }
        bestRoad = getBestChromosome(bestInGeneration);
        System.out.println("Generation " +  " Best Energy: " + bestRoad.getEnergy() +
                " Path: " + bestRoad.getPath());

    }


    // ایجاد جمعیت اولیه
    public List<Chromosome> generateInitialPopulation() {
        List<Chromosome> population = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            // هر مسیر به صورت تصادفی ساخته می‌شود
            String path = generateRandomPath();
            population.add(new Chromosome(path));
        }

        return population;
    }

    // ایجاد مسیر تصادفی برای کروموزوم
    public String generateRandomPath() {
        StringBuilder path = new StringBuilder();
        Random random = new Random();
        String[] moves = {"UP", "DOWN", "LEFT", "RIGHT", "STAY"};

        for (int i = 0; i < 50; i++) { // طول مسیر
            path.append(moves[random.nextInt(moves.length)]).append(", ");
        }

        return path.toString();
    }

    // ارزیابی جمعیت
    public void evaluatePopulation(List<Chromosome> population) {
        for (Chromosome chromosome : population) {
            chromosome.setEnergy(calculateEnergy(chromosome.getPath()));
        }
    }

    // محاسبه انرژی مصرفی برای یک مسیر
    public int calculateEnergy(String path) {
        int energy = MAX_ENERGY;
        String[] moves = path.split(", ");

        for (String move : moves) {
            if (move.equals("UP") || move.equals("DOWN") || move.equals("LEFT") || move.equals("RIGHT")) {
                energy -= 1; // حرکت به جلو، چپ یا راست یک واحد انرژی مصرف می‌کند
            } else if (move.equals("STAY")) {
                energy -= 1; // ایستادن یک واحد انرژی مصرف می‌کند
            } else {
                energy -= 2; // حرکت به عقب دو واحد انرژی مصرف می‌کند
            }
        }

        return energy;
    }

    // ایجاد نسل جدید
    public List<Chromosome> createNewGeneration(List<Chromosome> population) {
        List<Chromosome> newGeneration = new ArrayList<>();
        Random random = new Random();

        // انتخاب والدین و اعمال ترکیب و جهش
        while (newGeneration.size() < POPULATION_SIZE) {
            Chromosome parent1 = selectParent(population);
            Chromosome parent2 = selectParent(population);

            if (!correctParent(parent1) ){
                population.remove(parent1);
                continue;
            }
            if (!correctParent(parent2) ){
                population.remove(parent2);
                continue;
            }

            Chromosome child;
            if (random.nextDouble() < CROSSOVER_RATE) {
                child = crossover(parent1, parent2);
            } else {
                child = parent1;
            }

            if (random.nextDouble() < MUTATION_RATE) {
                child = mutate(child);
            }

            newGeneration.add(child);
        }

        return newGeneration;
    }

    // انتخاب والد برای ترکیب
    public Chromosome selectParent(List<Chromosome> population) {
        Random random = new Random();
        return population.get(random.nextInt(population.size()));
    }

    //برای یافتن والد درست
    public boolean correctParent (Chromosome parent){
        int[] location = findWidthAndLength(parent.getPath());
        if (location[0] < 0 || location[0] > RIVER_WIDTH-1 || location[1] < 0 || location[1] > RIVER_LENGTH-1
                || river[location[0]][location[1]] == 1){
            return false;
        }
        return true;
    }

























    // ترکیب دو کروموزوم (تبادل تک نقطه‌ای)
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        String[] path1 = parent1.getPath().split(", ");
        String[] path2 = parent2.getPath().split(", ");

        int crossoverPoint = path1.length / 2;
        StringBuilder newPath = new StringBuilder();

        for (int i = 0; i < crossoverPoint; i++) {
            newPath.append(path1[i]).append(", ");
        }

        for (int i = crossoverPoint; i < path2.length; i++) {
            newPath.append(path2[i]).append(", ");
        }

        return new Chromosome(newPath.toString());
    }

    // جهش کروموزوم
    public Chromosome mutate(Chromosome chromosome) {
        String[] path = chromosome.getPath().split(", ");
        Random random = new Random();
        //int[] location = findWidthAndLength(chromosome.getPath());

        int mutationPoint = random.nextInt(path.length);
        String[] moves = {"UP", "DOWN", "LEFT", "RIGHT", "STAY"}; //randomMove(location[0],location[1]);
        path[mutationPoint] = moves[random.nextInt(moves.length)];

        StringBuilder newPath = new StringBuilder();
        for (String move : path) {
            newPath.append(move).append(", ");
        }

        return new Chromosome(newPath.toString());
    }

    // پیدا کردن بهترین کروموزوم
    public Chromosome getBestChromosome(List<Chromosome> population) {
        return population.stream().min(Comparator.comparingInt(Chromosome::getEnergy)).orElse(null);
    }
































    // انتخاب حرکت تصادفی

    public String[] randomMove(int i, int j) {
        if (river[i][j] == 1) return null;
        if (i == 0 && j == 0){
            String[] moves = {"DOWN", "RIGHT"};
            return moves;
        }
        if (i == RIVER_WIDTH-1 && j == 0){
            String[] moves = {"DOWN", "LEFT"};
            return moves;
        }
        if (i == 0 && j == RIVER_LENGTH-1){
            String[] moves = {"UP", "RIGHT"};
            return moves;
        }
        if (i == 0){
            String[] moves = {"DOWN", "LEFT", "RIGHT"};
            return moves;
        }
        if (i == RIVER_WIDTH-1){
            String[] moves = {"UP", "DOWN", "LEFT"};
            return moves;
        }
        if (j == 0){
            String[] moves = {"UP", "DOWN", "RIGHT"};
            return moves;
        }if (j == RIVER_LENGTH-1){
            String[] moves = {"UP", "LEFT", "RIGHT"};
            return moves;
        }
        return null;
    }

    //پیدا کردن x,y کشتی در رودخانه
    public int[] findWidthAndLength (String path){
        int x=0 , y=0 ;
        int[] temp = new int[2];
        String[] moves = path.split(", ");
        for (String move : moves) {
            switch (move) {
                case "UP":
                    x++;
                case "DOWN":
                    x--;
                case "LEFT":
                    y--;
                case "RIGHT":
                    y++;
                default:
            }
        }
        temp[0] = x;
        temp[1] = y;
        return temp;
    }



}

