package net.minecraft.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;

public class DispenserBehaviors
{
    public static void registerDispenserBehaviours()
    {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.arrow, new DispenserBehaviorArrow());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.egg, new DispenserBehaviorEgg());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.snowball, new DispenserBehaviorSnowball());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.expBottle, new DispenserBehaviorExperience());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.potion, new DispenserBehaviorPotion());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.monsterPlacer, new DispenserBehaviorMobEgg());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.firework, new DispenserBehaviorFireworks());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.fireballCharge, new DispenserBehaviorFireball());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.boat, new DispenserBehaviorBoat());
        DispenserBehaviorFilledBucket dispenserbehaviorfilledbucket = new DispenserBehaviorFilledBucket();
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketLava, dispenserbehaviorfilledbucket);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketWater, dispenserbehaviorfilledbucket);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.bucketEmpty, new DispenserBehaviorEmptyBucket());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.flintAndSteel, new DispenserBehaviorFire());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.dyePowder, new DispenserBehaviorDye());
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.itemsList[Block.tnt.blockID], new DispenserBehaviorTNT());
    }
}
