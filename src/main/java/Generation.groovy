/**
 * Created by malinovsky on 12/9/14.
 */
public class GAHelloWorld {

    public static final int POPULATION_SIZE = 1000;
    public static final double ELITE_RATE = 0.1;
    public static final double SURVIVE_RATE = 0.5;
    public static final double MUTATION_RATE = 0.2;
    public static final String TARGET = "Hello World!";
    private static final int MAX_ITER = 1000;

    void initializePopulation(List<Genome> population) {
        int tsize = TARGET.length();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < tsize; j++) {
                str.append((char) (Math.random() * 255));
            }
            Genome citizen = new Genome(str.toString());
            population.add(citizen);
        }

    }


    String mutate(String str) {
        int tsize = TARGET.length();
        int ipos = (int) (Math.random() * tsize);
        char delta = (char) (Math.random() * 255);

        return str.substring(0, ipos) + delta + str.substring(ipos + 1);

    }

    List<Genome> mate(List<Genome> population) {
        int esize = (int) (POPULATION_SIZE * ELITE_RATE);
        int tsize = TARGET.length();
        List<Genome> children = new ArrayList<Genome>();

        selectElite(population, children, esize);

        for (int i = esize; i < POPULATION_SIZE; i++) {
            int i1 = (int) (Math.random() * POPULATION_SIZE * SURVIVE_RATE);
            int i2 = (int) (Math.random() * POPULATION_SIZE * SURVIVE_RATE);
            int spos = (int) (Math.random() * tsize);

            String str = population.get(i1).str.substring(0, spos) +
                    population.get(i2).str.substring(spos);
            if (Math.random() < MUTATION_RATE) {
                str = mutate(str);
            }
            Genome child = new Genome(str);
            children.add(child);


        }
        return children;
    }

    private void selectElite(List<Genome> population, List<Genome> children, int esize) {
        for (int i = 0; i < esize; i++) {
            children.add(population.get(i));
        }
    }

    private void go() {
        List<Genome> population = new ArrayList<Genome>();
        initializePopulation(population);

        for (int i = 0; i < MAX_ITER; i++) {
            Collections.sort(population);
            System.out.println(i + " > " + population.get(0));

            if (population.get(0).fitness == 0) {
                break;
            }

            population = mate(population);
        }

    }

    public static void main(String[] args) {
        new GAHelloWorld().go();
    }

}

class Genome implements Comparable<Genome> {
    final String str;
    final int fitness;

    public Genome(String str) {
        this.str = str;
        int fitness = 0;
        for (int j = 0; j < str.length(); j++) {
            fitness += Math.abs(str.charAt(j) - GAHelloWorld.TARGET.charAt(j));
        }
        this.fitness = fitness;
    }

    public int compareTo(Genome o) {
        return fitness - o.fitness;
    }

    public String toString() {
        return fitness + " " + str;
    }

}

