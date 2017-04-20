package net.minecraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapInfo;

public class ItemMap extends ItemMapBase
{
    protected ItemMap(int par1)
    {
        super(par1);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public static MapData getMPMapData(short par0, World par1World)
    {
        String s = "map_" + par0;
        MapData mapdata = (MapData)par1World.loadItemData(MapData.class, s);

        if (mapdata == null)
        {
            mapdata = new MapData(s);
            par1World.setItemData(s, mapdata);
        }

        return mapdata;
    }

    public MapData getMapData(ItemStack par1ItemStack, World par2World)
    {
        String s = "map_" + par1ItemStack.getItemDamage();
        MapData mapdata = (MapData)par2World.loadItemData(MapData.class, s);

        if (mapdata == null && !par2World.isRemote)
        {
            par1ItemStack.setItemDamage(par2World.getUniqueDataId("map"));
            s = "map_" + par1ItemStack.getItemDamage();
            mapdata = new MapData(s);
            mapdata.scale = 3;
            int i = 128 * (1 << mapdata.scale);
            mapdata.xCenter = Math.round((float)par2World.getWorldInfo().getSpawnX() / (float)i) * i;
            mapdata.zCenter = Math.round((float)(par2World.getWorldInfo().getSpawnZ() / i)) * i;
            mapdata.dimension = par2World.provider.dimensionId;
            mapdata.markDirty();
            par2World.setItemData(s, mapdata);
        }

        return mapdata;
    }

    public void updateMapData(World par1World, Entity par2Entity, MapData par3MapData)
    {
        if (par1World.provider.dimensionId == par3MapData.dimension && par2Entity instanceof EntityPlayer)
        {
            short short1 = 128;
            short short2 = 128;
            int i = 1 << par3MapData.scale;
            int j = par3MapData.xCenter;
            int k = par3MapData.zCenter;
            int l = MathHelper.floor_double(par2Entity.posX - (double)j) / i + short1 / 2;
            int i1 = MathHelper.floor_double(par2Entity.posZ - (double)k) / i + short2 / 2;
            int j1 = 128 / i;

            if (par1World.provider.hasNoSky)
            {
                j1 /= 2;
            }

            MapInfo mapinfo = par3MapData.func_82568_a((EntityPlayer)par2Entity);
            ++mapinfo.field_82569_d;

            for (int k1 = l - j1 + 1; k1 < l + j1; ++k1)
            {
                if ((k1 & 15) == (mapinfo.field_82569_d & 15))
                {
                    int l1 = 255;
                    int i2 = 0;
                    double d0 = 0.0D;

                    for (int j2 = i1 - j1 - 1; j2 < i1 + j1; ++j2)
                    {
                        if (k1 >= 0 && j2 >= -1 && k1 < short1 && j2 < short2)
                        {
                            int k2 = k1 - l;
                            int l2 = j2 - i1;
                            boolean flag = k2 * k2 + l2 * l2 > (j1 - 2) * (j1 - 2);
                            int i3 = (j / i + k1 - short1 / 2) * i;
                            int j3 = (k / i + j2 - short2 / 2) * i;
                            int[] aint = new int[Block.blocksList.length];
                            Chunk chunk = par1World.getChunkFromBlockCoords(i3, j3);

                            if (!chunk.isEmpty())
                            {
                                int k3 = i3 & 15;
                                int l3 = j3 & 15;
                                int i4 = 0;
                                double d1 = 0.0D;
                                int j4;
                                int k4;
                                int l4;
                                int i5;

                                if (par1World.provider.hasNoSky)
                                {
                                    j4 = i3 + j3 * 231871;
                                    j4 = j4 * j4 * 31287121 + j4 * 11;

                                    if ((j4 >> 20 & 1) == 0)
                                    {
                                        aint[Block.dirt.blockID] += 10;
                                    }
                                    else
                                    {
                                        aint[Block.stone.blockID] += 10;
                                    }

                                    d1 = 100.0D;
                                }
                                else
                                {
                                    for (j4 = 0; j4 < i; ++j4)
                                    {
                                        for (k4 = 0; k4 < i; ++k4)
                                        {
                                            l4 = chunk.getHeightValue(j4 + k3, k4 + l3) + 1;
                                            int j5 = 0;

                                            if (l4 > 1)
                                            {
                                                boolean flag1;

                                                do
                                                {
                                                    flag1 = true;
                                                    j5 = chunk.getBlockID(j4 + k3, l4 - 1, k4 + l3);

                                                    if (j5 == 0)
                                                    {
                                                        flag1 = false;
                                                    }
                                                    else if (l4 > 0 && j5 > 0 && Block.blocksList[j5].blockMaterial.materialMapColor == MapColor.airColor)
                                                    {
                                                        flag1 = false;
                                                    }

                                                    if (!flag1)
                                                    {
                                                        --l4;

                                                        if (l4 <= 0)
                                                        {
                                                            break;
                                                        }

                                                        j5 = chunk.getBlockID(j4 + k3, l4 - 1, k4 + l3);
                                                    }
                                                }
                                                while (l4 > 0 && !flag1);

                                                if (l4 > 0 && j5 != 0 && Block.blocksList[j5].blockMaterial.isLiquid())
                                                {
                                                    i5 = l4 - 1;
                                                    boolean flag2 = false;
                                                    int k5;

                                                    do
                                                    {
                                                        k5 = chunk.getBlockID(j4 + k3, i5--, k4 + l3);
                                                        ++i4;
                                                    }
                                                    while (i5 > 0 && k5 != 0 && Block.blocksList[k5].blockMaterial.isLiquid());
                                                }
                                            }

                                            d1 += (double)l4 / (double)(i * i);
                                            ++aint[j5];
                                        }
                                    }
                                }

                                i4 /= i * i;
                                j4 = 0;
                                k4 = 0;

                                for (l4 = 0; l4 < Block.blocksList.length; ++l4)
                                {
                                    if (aint[l4] > j4)
                                    {
                                        k4 = l4;
                                        j4 = aint[l4];
                                    }
                                }

                                double d2 = (d1 - d0) * 4.0D / (double)(i + 4) + ((double)(k1 + j2 & 1) - 0.5D) * 0.4D;
                                byte b0 = 1;

                                if (d2 > 0.6D)
                                {
                                    b0 = 2;
                                }

                                if (d2 < -0.6D)
                                {
                                    b0 = 0;
                                }

                                i5 = 0;

                                if (k4 > 0)
                                {
                                    MapColor mapcolor = Block.blocksList[k4].blockMaterial.materialMapColor;

                                    if (mapcolor == MapColor.waterColor)
                                    {
                                        d2 = (double)i4 * 0.1D + (double)(k1 + j2 & 1) * 0.2D;
                                        b0 = 1;

                                        if (d2 < 0.5D)
                                        {
                                            b0 = 2;
                                        }

                                        if (d2 > 0.9D)
                                        {
                                            b0 = 0;
                                        }
                                    }

                                    i5 = mapcolor.colorIndex;
                                }

                                d0 = d1;

                                if (j2 >= 0 && k2 * k2 + l2 * l2 < j1 * j1 && (!flag || (k1 + j2 & 1) != 0))
                                {
                                    byte b1 = par3MapData.colors[k1 + j2 * short1];
                                    byte b2 = (byte)(i5 * 4 + b0);

                                    if (b1 != b2)
                                    {
                                        if (l1 > j2)
                                        {
                                            l1 = j2;
                                        }

                                        if (i2 < j2)
                                        {
                                            i2 = j2;
                                        }

                                        par3MapData.colors[k1 + j2 * short1] = b2;
                                    }
                                }
                            }
                        }
                    }

                    if (l1 <= i2)
                    {
                        par3MapData.setColumnDirty(k1, l1, i2);
                    }
                }
            }
        }
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!par2World.isRemote)
        {
            MapData mapdata = this.getMapData(par1ItemStack, par2World);

            if (par3Entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)par3Entity;
                mapdata.updateVisiblePlayers(entityplayer, par1ItemStack);
            }

            if (par5)
            {
                this.updateMapData(par2World, par3Entity, mapdata);
            }
        }
    }

    /**
     * returns null if no update is to be sent
     */
    public Packet createMapDataPacket(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        byte[] abyte = this.getMapData(par1ItemStack, par2World).getUpdatePacketData(par1ItemStack, par2World, par3EntityPlayer);
        return abyte == null ? null : new Packet131MapData((short)Item.map.itemID, (short)par1ItemStack.getItemDamage(), abyte);
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().getBoolean("map_is_scaling"))
        {
            MapData mapdata = Item.map.getMapData(par1ItemStack, par2World);
            par1ItemStack.setItemDamage(par2World.getUniqueDataId("map"));
            MapData mapdata1 = new MapData("map_" + par1ItemStack.getItemDamage());
            mapdata1.scale = (byte)(mapdata.scale + 1);

            if (mapdata1.scale > 4)
            {
                mapdata1.scale = 4;
            }

            mapdata1.xCenter = mapdata.xCenter;
            mapdata1.zCenter = mapdata.zCenter;
            mapdata1.dimension = mapdata.dimension;
            mapdata1.markDirty();
            par2World.setItemData("map_" + par1ItemStack.getItemDamage(), mapdata1);
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        MapData mapdata = this.getMapData(par1ItemStack, par2EntityPlayer.worldObj);

        if (par4)
        {
            if (mapdata == null)
            {
                par3List.add("Unknown map");
            }
            else
            {
                par3List.add("Scaling at 1:" + (1 << mapdata.scale));
                par3List.add("(Level " + mapdata.scale + "/" + 4 + ")");
            }
        }
    }
}
