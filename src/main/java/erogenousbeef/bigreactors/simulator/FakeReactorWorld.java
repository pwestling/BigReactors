package erogenousbeef.bigreactors.simulator;

import java.util.List;

import com.google.common.collect.Lists;
import erogenousbeef.bigreactors.common.multiblock.tileentity.TileEntityReactorControlRod;
import erogenousbeef.bigreactors.common.multiblock.tileentity.TileEntityReactorFuelRod;
import erogenousbeef.bigreactors.simulator.nested_map.ThreeNestedMap;
import erogenousbeef.core.common.CoordTriplet;
import net.minecraft.tileentity.TileEntity;

public class FakeReactorWorld implements FakeWorld {

  private ThreeNestedMap<Integer, Integer, Integer, Value> worldValues = new ThreeNestedMap<Integer, Integer, Integer, Value>();
  private final CoordTriplet maxDims;
  private final List<TileEntity> parts = Lists.newArrayList();

  public FakeReactorWorld(int x, int y, int z) {
    this.maxDims = new CoordTriplet(x, y, z);
  }

  public static FakeReactorWorld test() {
    FakeReactorWorld world = new FakeReactorWorld(9, 9, 9);
    world.makeControlRod(5,5);
    return world;
  }

  public void makeControlRod(int x, int z) {
    int maxY = maxDims.y;
    List<TileEntityReactorFuelRodSimulator> fuel = Lists.newArrayList();
    for (int i = 1; i < maxY - 1; i++) { //From above reactor floor to below control rod
      TileEntityReactorFuelRodSimulator part = new TileEntityReactorFuelRodSimulator();
      setEntity(x, i, z, part);
      fuel.add(part);
    }
    TileEntityReactorControlRod controlRod = new TileEntityReactorControlRod();
    setEntity(x, maxY, z, controlRod);
  }

  private void setEntity(int x,int y, int z, TileEntity part) {
    parts.add(part);
    part.xCoord = x;
    part.yCoord = y;
    part.zCoord = z;
    worldValues.put(x, y, z, new Value(part));
  }

  public void makeCoolantColumn(int x, int z, String coolant) {
    int maxY = maxDims.y;
    for (int i = 1; i < maxY - 1; i++) { //From above reactor floor to below control rod
      worldValues.put(x, i, z, new Value(coolant));
    }
  }

  @Override
  public TileEntity getTileEntity(int x, int y, int z) {
    Value value = worldValues.get(x, y, z);
    return value == null ? null : value.getEntity();
  }

  @Override
  public boolean isAirBlock(int x, int y, int z) {
    return !worldValues.containsKey(x,y,z);
  }

  @Override
  public String getBlockName(int x, int y, int z) {
    Value value =  worldValues.get(x, y, z);
    return value == null ? null : value.getBlockOrFluid();
  }

  @Override
  public CoordTriplet getMaxCoord() {
    return maxDims;
  }

  @Override
  public CoordTriplet getMinCoord() {
    return new CoordTriplet(0, 0, 0);
  }

  @Override
  public List<TileEntity> getParts() {
    return parts;
  }

  private static class Value {
    private TileEntity entity = null;
    private String blockOrFluid = "";

    private Value(TileEntity entity) {
      this.entity = entity;
    }

    private Value(String blockOrFluid) {
      this.blockOrFluid = blockOrFluid;
    }

    public TileEntity getEntity() {
      return entity;
    }

    public String getBlockOrFluid() {
      return blockOrFluid;
    }
  }
}
