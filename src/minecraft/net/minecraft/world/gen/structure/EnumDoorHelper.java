package net.minecraft.world.gen.structure;

class EnumDoorHelper
{
    static final int[] doorEnum = new int[EnumDoor.values().length];

    static
    {
        try
        {
            doorEnum[EnumDoor.OPENING.ordinal()] = 1;
        }
        catch (NoSuchFieldError nosuchfielderror)
        {
            ;
        }

        try
        {
            doorEnum[EnumDoor.WOOD_DOOR.ordinal()] = 2;
        }
        catch (NoSuchFieldError nosuchfielderror1)
        {
            ;
        }

        try
        {
            doorEnum[EnumDoor.GRATES.ordinal()] = 3;
        }
        catch (NoSuchFieldError nosuchfielderror2)
        {
            ;
        }

        try
        {
            doorEnum[EnumDoor.IRON_DOOR.ordinal()] = 4;
        }
        catch (NoSuchFieldError nosuchfielderror3)
        {
            ;
        }
    }
}
