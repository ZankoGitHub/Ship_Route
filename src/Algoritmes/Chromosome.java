package Algoritmes;

public class Chromosome {
    private String path; // مسیر
    private int energy; // انرژی مصرفی

    public Chromosome(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
