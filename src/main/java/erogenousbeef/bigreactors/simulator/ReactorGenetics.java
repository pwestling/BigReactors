package erogenousbeef.bigreactors.simulator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

public class ReactorGenetics {


  private final int xSize;
  private final int zSize;
  private final int height;
  private double selectionFactor;

  private ExecutorService service = new ScheduledThreadPoolExecutor(10);

  public ReactorGenetics(int xSize, int zSize, int height) {
    this.xSize = xSize;
    this.zSize = zSize;
    this.height = height;
  }

  public static final List<Character> weightedGenes = Lists.newArrayList(
      'E',
      'X',
      'C'
  );

  public FakeReactorWorld makeReactor(String reactor) {
    FakeReactorWorld fakeReactorWorld = new FakeReactorWorld(xSize, height, zSize);
    reactor = reactor.replaceAll(" ", "");
    reactor = reactor.replaceAll("\n", "");
    for (int i = 0; i < reactor.length(); i++) {
      int x = i % (xSize - 2);
      int z = i / (xSize - 2);
      ReactorParser.parse(x, z, reactor.charAt(i), fakeReactorWorld);
    }
    return fakeReactorWorld;
  }

  public String makeCandidate() {
    Random r = new Random();
    String reactor = "";
    for (int i = 0; i < xSize * zSize; i++) {
      reactor += weightedGenes.get(r.nextInt(weightedGenes.size()));
    }
    return reactor;
  }

  public static class SimulationRun implements Runnable {

    private FakeReactorWorld reactor;
    private ReactorGenome genome;
    private BigReactorSimulator function;

    public SimulationRun(FakeReactorWorld reactor, ReactorGenome genome, BigReactorSimulator function) {
      this.reactor = reactor;
      this.genome = genome;
      this.function = function;
    }

    public void run() {
    BigReactorSimulator.ReactorResult result = function.simulate(reactor, 1000);
    genome.fitness = calcFitness(result, genome);
    genome.result = result;
  }

    public static double calcFitness(BigReactorSimulator.ReactorResult result, ReactorGenome genome) {
      return (result.output * result.efficiency);
    }

  }

  public List<ReactorGenome> select(List<ReactorGenome> genomes, BigReactorSimulator simulator){
    fitness(genomes, simulator);
    Collections.sort(genomes);
    ReactorGenome best = genomes.get(genomes.size() - 1);
    System.out.println("Best: "+ best.result);
    System.out.println("Fitness: "+ SimulationRun.calcFitness(best.result, best));

    selectionFactor = 0.20;
    int toTake = (int)(genomes.size() * selectionFactor);
    List<ReactorGenome> selected = Lists.newArrayList();
    for (int i = genomes.size()-1; i > genomes.size() - toTake; i--) {
      selected.add(genomes.get(i));
    }
    return selected;
  }

  public void fitness(List<ReactorGenome> genomes, BigReactorSimulator simulator) {
    long start  = System.currentTimeMillis();
    service = new ScheduledThreadPoolExecutor(50);
    for (ReactorGenome genome : genomes) {
      SimulationRun run = new SimulationRun(makeReactor(genome.reactor), genome, simulator);
      service.submit(run);
    }
    try {
      service.shutdown();
      service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    System.out.println("Fitness time: "+(System.currentTimeMillis() - start));
  }

  public List<ReactorGenome> generation(List<ReactorGenome> selected, int popSize){
    List<ReactorGenome> nextGeneration = Lists.newArrayList();

    for (int i = 0; i < Math.ceil(1/selectionFactor)+1; i++) {
      Collections.shuffle(selected);
      addGeneration(selected, nextGeneration);
    }
    nextGeneration = nextGeneration.subList(0, popSize);
    List<ReactorGenome> mutatedNextGeneration = Lists.newArrayList();
    for (ReactorGenome reactorGenome : nextGeneration) {
      mutatedNextGeneration.add(reactorGenome.mutate(popSize));
    }
    return mutatedNextGeneration;
  }

  public void addGeneration(List<ReactorGenome> selected, List<ReactorGenome> nextGeneration) {
    for (int i = 0; i < selected.size(); i++) {
      ReactorGenome primary = selected.get(i%selected.size());
      ReactorGenome secondary = selected.get((i + 1)%selected.size());
      ReactorGenome tertiary = selected.get((i + 2)%selected.size());
      nextGeneration.add(primary.breed(secondary, tertiary));
    }
  }

  public ReactorGenome breedGoodReactor(int startingPopulation, int generations){
    BigReactorSimulator simulator = new BigReactorSimulator();
    simulator.init(true);

    List<ReactorGenome> population = Lists.newArrayList();
    for (int i = 0; i < startingPopulation; i++) {
      population.add(new ReactorGenome(makeCandidate()));
    }

    for (int i = 0; i < generations; i++) {
      List<ReactorGenome> selected = select(population, simulator);
      population = generation(selected, startingPopulation);
      System.out.println("Gen: "+i);
    }

    fitness(population, simulator);
    Collections.sort(population);
    return population.get(population.size()-1);
  }


  public static void main(String[] args) {

    ReactorGenetics genetics = new ReactorGenetics(13, 13, 13);
    System.out.println("Starting simulation...");
    ReactorGenome reactorGenome = genetics.breedGoodReactor(2000, 20);
    genetics.display(reactorGenome.reactor);
    System.out.println(reactorGenome.result);

  }

  private void display(String s) {
    for (int i = 0; i < s.length(); i++) {
      if(i % xSize == 0){
        System.out.println();
      }
      System.out.print(s.charAt(i) + " ");
    }
    System.out.println();
  }

  private static class ReactorGenome implements Comparable<ReactorGenome>{

    private String reactor;
    private double fitness;
    private BigReactorSimulator.ReactorResult result;
    private static Random random = new Random();

    private ReactorGenome(String reactor) {
      this.reactor = reactor;
    }

    public ReactorGenome breed(ReactorGenome otherParent1, ReactorGenome otherParent2){
      String result = "";
      for (int i = 0; i < reactor.length(); i++) {
        if(otherParent1.reactor.charAt(i) == otherParent2.reactor.charAt(i)){
          result += otherParent1.reactor.charAt(i);
        }else{
          result += this.reactor.charAt(i);
        }
      }
      return new ReactorGenome(result);
    }

    public ReactorGenome mutate(int popSize){
      String result = "";
      double chanceOfTotalShuffle = 1.0/popSize;
      if(random.nextDouble() < chanceOfTotalShuffle){
        result = shuffleAll(result);
      }else {
        result = mutateSlightly(result);
      }
      return new ReactorGenome(result);

    }

    public String mutateSlightly(String result) {
      double chance = 1.0 / reactor.length();
      for (int i = 0; i < reactor.length(); i++) {
        if (random.nextDouble() < chance) {
          result += weightedGenes.get(random.nextInt(weightedGenes.size()));
        } else {
          result += reactor.charAt(i);
        }
      }
      return result;
    }

    public String shuffleAll(String result) {
      for (int i = 0; i < reactor.length(); i++) {
        result += weightedGenes.get((weightedGenes.indexOf(reactor.charAt(i))+1)%weightedGenes.size());
      }
      return result;
    }

    @Override
    public int compareTo(ReactorGenome reactorGenome) {
      return Double.compare(this.fitness, reactorGenome.fitness);
    }

    @Override
    public String toString() {
      return "ReactorGenome{" +
          "reactor='" + reactor + '\'' +
          ", fitness=" + fitness +
          '}';
    }
  }
}
