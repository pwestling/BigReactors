package erogenousbeef.bigreactors.simulator;

import erogenousbeef.bigreactors.api.IHeatEntity;
import erogenousbeef.bigreactors.api.registry.ReactorConversions;
import erogenousbeef.bigreactors.api.registry.ReactorInterior;
import erogenousbeef.bigreactors.api.registry.TurbineCoil;
import erogenousbeef.bigreactors.common.data.StandardReactants;
import erogenousbeef.bigreactors.common.multiblock.helpers.RadiationHelper;

public class BigReactorSimulator {


  private boolean activelyCooled;

  private static final String OUR_10X10 =
      "E E E E E E E E" +
          "E E X E E X E E" +
          "E X X X X X X E" +
          "E E X D D X E E" +
          "E E X D D X E E" +
          "E X X X X X X E" +
          "E E X E E X E E" +
          "E E E E E E E E";
  private int ticks;

  public BigReactorSimulator(boolean activelyCooled, int ticks) {
    this.activelyCooled = activelyCooled;
    this.ticks = ticks;
  }

  public static void init() {

    ReactorInterior.registerBlock("blockIron", 0.50f, 0.75f, 1.40f, IHeatEntity.conductivityIron);
    ReactorInterior.registerBlock("blockGold", 0.52f, 0.80f, 1.45f, IHeatEntity.conductivityGold);
    ReactorInterior.registerBlock("blockDiamond", 0.55f, 0.85f, 1.50f, IHeatEntity.conductivityDiamond);
    ReactorInterior.registerBlock("blockEmerald", 0.55f, 0.85f, 1.50f, IHeatEntity.conductivityEmerald);
    ReactorInterior.registerBlock("blockGraphite", 0.10f, 0.50f, 2.00f, IHeatEntity.conductivityGold); // Graphite: a great moderator!
    ReactorInterior.registerBlock("blockGlassColorless", 0.20f, 0.25f, 1.10f, IHeatEntity.conductivityGlass);
    ReactorInterior.registerBlock("blockIce", 0.33f, 0.33f, 1.15f, IHeatEntity.conductivityWater);
    ReactorInterior.registerBlock("blockSnow", 0.15f, 0.33f, 1.05f, IHeatEntity.conductivityWater / 2f);

    // Mod blocks
    ReactorInterior.registerBlock("blockCopper", 0.50f, 0.75f, 1.40f, IHeatEntity.conductivityCopper);
    ReactorInterior.registerBlock("blockOsmium", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
    ReactorInterior.registerBlock("blockBrass", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
    ReactorInterior.registerBlock("blockBronze", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
    ReactorInterior.registerBlock("blockZinc", 0.51f, 0.77f, 1.41f, IHeatEntity.conductivityCopper);
    ReactorInterior.registerBlock("blockAluminum", 0.50f, 0.78f, 1.42f, IHeatEntity.conductivityIron);
    ReactorInterior.registerBlock("blockSteel", 0.50f, 0.78f, 1.42f, IHeatEntity.conductivityIron);
    ReactorInterior.registerBlock("blockInvar", 0.50f, 0.79f, 1.43f, IHeatEntity.conductivityIron);
    ReactorInterior.registerBlock("blockSilver", 0.51f, 0.79f, 1.43f, IHeatEntity.conductivitySilver);
    ReactorInterior.registerBlock("blockLead", 0.75f, 0.75f, 1.75f, IHeatEntity.conductivitySilver);
    ReactorInterior.registerBlock("blockElectrum", 0.53f, 0.82f, 1.47f, 2.2f); // Between gold and emerald
    ReactorInterior.registerBlock("blockElectrumFlux", 0.54f, 0.83f, 1.48f, 2.4f); // Between gold and emerald
    ReactorInterior.registerBlock("blockPlatinum", 0.57f, 0.86f, 1.58f, IHeatEntity.conductivityEmerald);
    ReactorInterior.registerBlock("blockShiny", 0.57f, 0.86f, 1.58f, IHeatEntity.conductivityEmerald);
    ReactorInterior.registerBlock("blockTitanium", 0.58f, 0.87f, 1.59f, 2.7f); // Mariculture
    ReactorInterior.registerBlock("blockEnderium", 0.60f, 0.88f, 1.60f, IHeatEntity.conductivityDiamond);

    ReactorInterior.registerFluid("water", RadiationHelper.waterData.absorption, RadiationHelper.waterData.heatEfficiency, RadiationHelper.waterData.moderation, IHeatEntity.conductivityWater);
    ReactorInterior.registerFluid("redstone", 0.75f, 0.55f, 1.60f, IHeatEntity.conductivityEmerald);
    ReactorInterior.registerFluid("glowstone", 0.20f, 0.60f, 1.75f, IHeatEntity.conductivityCopper);
    ReactorInterior.registerFluid("cryotheum", 0.66f, 0.95f, 6.00f, IHeatEntity.conductivityDiamond); // Cryotheum: an amazing moderator!
    ReactorInterior.registerFluid("ender", 0.90f, 0.75f, 2.00f, IHeatEntity.conductivityGold);
    ReactorInterior.registerFluid("pyrotheum", 0.66f, 0.90f, 1.00f, IHeatEntity.conductivityIron);

    ReactorInterior.registerFluid("life essence", 0.70f, 0.55f, 1.75f, IHeatEntity.conductivityGold); // From Blood Magic

    StandardReactants.register();

    // Register reactant => reactant conversions for making cyanite
    ReactorConversions.register(StandardReactants.yellorium, StandardReactants.cyanite);
    ReactorConversions.register(StandardReactants.blutonium, StandardReactants.cyanite);

    TurbineCoil.registerBlock("blockIron", 1f, 1f, 1f);
    TurbineCoil.registerBlock("blockGold", 2f, 1f, 1.75f);

    TurbineCoil.registerBlock("blockCopper", 1.2f, 1f, 1.2f);  // TE, lots of mods
    TurbineCoil.registerBlock("blockOsmium", 1.2f, 1f, 1.2f);  // Mekanism
    TurbineCoil.registerBlock("blockZinc", 1.35f, 1f, 1.3f);
    TurbineCoil.registerBlock("blockLead", 1.35f, 1.01f, 1.3f);// TE, Mekanism, some others
    TurbineCoil.registerBlock("blockBrass", 1.4f, 1f, 1.2f);  // Metallurgy
    TurbineCoil.registerBlock("blockBronze", 1.4f, 1f, 1.2f);  // Mekanism, many others
    TurbineCoil.registerBlock("blockAluminum", 1.5f, 1f, 1.3f);  // TiCo, couple others
    TurbineCoil.registerBlock("blockSteel", 1.5f, 1f, 1.3f);  // Metallurgy, Mek, etc.
    TurbineCoil.registerBlock("blockInvar", 1.5f, 1f, 1.4f);  // TE
    TurbineCoil.registerBlock("blockSilver", 1.7f, 1f, 1.5f);  // TE, lots of mods
    TurbineCoil.registerBlock("blockElectrum", 2.5f, 1f, 2.0f);  // TE, lots of mods
    TurbineCoil.registerBlock("blockElectrumFlux", 2.5f, 1.01f, 2.2f);  // Redstone Arsenal, note small energy bonus (7% at 1000RF/t output)
    TurbineCoil.registerBlock("blockPlatinum", 3.0f, 1f, 2.5f);  // TE, lots of mods
    TurbineCoil.registerBlock("blockShiny", 3.0f, 1f, 2.5f);  // TE
    TurbineCoil.registerBlock("blockTitanium", 3.1f, 1f, 2.7f);  // Mariculture
    TurbineCoil.registerBlock("blockEnderium", 3.0f, 1.02f, 3.0f);  // TE, note tiny energy bonus!	(14% at 1000RF/t output)

    TurbineCoil.registerBlock("blockLudicrite", 3.5f, 1.02f, 3.5f);

    // Metallurgy fantasy metals
    TurbineCoil.registerBlock("blockMithril", 2.2f, 1f, 1.5f);
    TurbineCoil.registerBlock("blockOrichalcum", 2.3f, 1f, 1.7f);
    TurbineCoil.registerBlock("blockQuicksilver", 2.6f, 1f, 1.8f);
    TurbineCoil.registerBlock("blockHaderoth", 3.0f, 1f, 2.0f);
    TurbineCoil.registerBlock("blockCelenegil", 3.3f, 1f, 2.25f);
    TurbineCoil.registerBlock("blockTartarite", 3.5f, 1f, 2.5f);
    TurbineCoil.registerBlock("blockManyullyn", 3.5f, 1f, 2.5f);

    //    StandardReactants.yelloriumMapping = Reactants.registerSolid("ingotYellorium", StandardReactants.yellorium);
    //    StandardReactants.cyaniteMapping = Reactants.registerSolid("ingotCyanite", StandardReactants.cyanite);
    //
    //    ItemStack blockYellorium = blockMetal.getItemStackForMaterial("Yellorium");
    //    Reactants.registerSolid(blockYellorium, StandardReactants.yellorium, Reactants.standardSolidReactantAmount * 9);
    //
    //    ItemStack blockBlutonium = blockMetal.getItemStackForMaterial("Blutonium");
    //    Reactants.registerSolid(blockBlutonium, StandardReactants.blutonium, Reactants.standardSolidReactantAmount * 9);
  }

  public ReactorResult simulate(IFakeReactorWorld world) {
    MultiblockReactorSimulator simulator = new MultiblockReactorSimulator(world, "yellorium", activelyCooled);
    this.ticks = ticks;
    for (int i = 0; i < this.ticks; i++) {
      simulator.updateServer();
    }
    //    System.out.println("Energy Created: " + String.format("%.2f flux per tick", simulator.getEnergyGeneratedLastTick()));
    //    System.out.println("Fuel Consumed: " + StaticUtils.Strings.formatMillibuckets(simulator.getFuelConsumedLastTick()));
    //    System.out.println("Reactivity: " + String.format("%2.0f%%", simulator.getFuelFertility() * 100f));
    //    System.out.println("Efficiency: " + String.format("%.2f flux per mB", simulator.getEnergyGeneratedLastTick()/simulator.getFuelConsumedLastTick()));
    //    System.out.println("Coolant temp: "+simulator.getCoolantTemperature());
    //    System.out.println("Fuel temp: "+simulator.getFuelHeat());
    //    System.out.println("Reactor temp: "+simulator.getReactorHeat());

    return new ReactorResult(simulator.getEnergyGeneratedLastTick() / simulator.getFuelConsumedLastTick(),
        simulator.getEnergyGeneratedLastTick(), world.getNumRods());


  }

  public static class ReactorResult {

    public final double efficiency;
    public final double output;
    public final int numRods;

    public ReactorResult(double efficiency, double output, int numRods) {
      this.efficiency = efficiency;
      this.output = output;
      this.numRods = numRods;
    }

    @Override
    public String toString() {
      return "ReactorResult{" +
          "efficiency=" + efficiency +
          ", output=" + output +
          ", numRods=" + numRods +
          '}';
    }
  }

  public static void main(String[] args) {
    BigReactorSimulator.init();
    String reactor =
        "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C X G X C C C C C" +
            "C C C C C G C G C C C C C" +
            "C C C C C X G X C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C" +
            "C C C C C C C C C C C C C";


            FakeReactorWorld fakeReactorWorld = new ReactorGenetics(15, 15, 10).makeReactor(reactor);

    ReactorResult simulate = new BigReactorSimulator(false, 10000).simulate(fakeReactorWorld);
    System.out.println(simulate);
    System.out.println("Small turbines: " + simulate.output / 2000.0);
    System.out.println("Big turbines: " + simulate.output / 32000.0);
    System.out.println("Energy final: " + (simulate.output / 2000.0) * 20791);


    // System.out.println(MultiblockTurbineSimulator.simulateServer(2000, 20, 4, "blockEnderium", 4, 1800,0));

  }

}
