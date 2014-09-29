package erogenousbeef.bigreactors.simulator;

public class ReactorParser {

  public static final char CONTROL_ROD = 'X';
  private FakeReactorWorld world;

  public ReactorParser(int height, String[] lines) {
    String first = lines[0].replaceAll(" ","");
    this.world = new FakeReactorWorld(lines.length+1, height, first.length()+1);
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i].replaceAll(" ","");
      for (int j = 0; j < line.length(); j++) {
        char c = line.charAt(j);
        parse(i, j, c, world);
      }

    }
  }

  public static void parse(int i, int j, char c, FakeReactorWorld world) {
    if(c == CONTROL_ROD) {
      world.makeControlRod(i+1, j+1);
    }else if(c =='O'){
      //Air column
    }else{
      String coolant = getCoolant(c);
      world.makeCoolantColumn(i+1,j+1, coolant);
    }
  }

  public FakeReactorWorld getWorld() {
    return world;
  }

  private static String getCoolant(char c) {
    switch (c){
      case 'E':
        return "fluid:ender";
      case 'C':
        return "fluid:cryotheum";
      case 'D':
        return "block:blockDiamond";
      case 'P':
        return "fluid:pyrotheum";
      case 'G':
        return "block:blockGraphite";
    }
    return null;
  }
}
