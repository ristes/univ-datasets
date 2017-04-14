package mk.ukim.finki.univds.model;

/**
 * @author Riste Stojanov
 */
public class Measurement {

  private int warmUp;
  private QueryDefinition definition;
  private long duration = 0;
  private long start = 0;
  private long lastIteration;

  public Measurement(int warmUp, QueryDefinition definition) {
    this.warmUp = warmUp;
    this.definition = definition;
  }

  public void start(long iteration) {
    if (iteration >= warmUp) {
      start = System.currentTimeMillis();
    }
  }

  public void pause(long iteration) {
    lastIteration = iteration;
    if (iteration >= warmUp) {
      duration += (System.currentTimeMillis() - start);
    }
  }

  public void reset() {
    lastIteration = 0;
    start = 0;
    duration = 0;
  }

  public void print(String name, String dataset) {
    System.out.printf("%s\t%s\t%s\t%d\n", name, dataset, definition.getId(), duration);
    reset();
  }

}
