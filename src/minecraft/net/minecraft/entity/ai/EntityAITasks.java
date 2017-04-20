package net.minecraft.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.profiler.Profiler;

public class EntityAITasks
{
    /** A list of EntityAITaskEntrys in EntityAITasks. */
    public List taskEntries = new ArrayList();

    /** A list of EntityAITaskEntrys that are currently being executed. */
    private List executingTaskEntries = new ArrayList();

    /** Instance of Profiler. */
    private final Profiler theProfiler;
    private int tickCount;
    private int tickRate = 3;

    public EntityAITasks(Profiler par1Profiler)
    {
        this.theProfiler = par1Profiler;
    }

    public void addTask(int par1, EntityAIBase par2EntityAIBase)
    {
        this.taskEntries.add(new EntityAITaskEntry(this, par1, par2EntityAIBase));
    }

    /**
     * removes the indicated task from the entity's AI tasks.
     */
    public void removeTask(EntityAIBase par1EntityAIBase)
    {
        Iterator iterator = this.taskEntries.iterator();

        while (iterator.hasNext())
        {
            EntityAITaskEntry entityaitaskentry = (EntityAITaskEntry)iterator.next();
            EntityAIBase entityaibase1 = entityaitaskentry.action;

            if (entityaibase1 == par1EntityAIBase)
            {
                if (this.executingTaskEntries.contains(entityaitaskentry))
                {
                    entityaibase1.resetTask();
                    this.executingTaskEntries.remove(entityaitaskentry);
                }

                iterator.remove();
            }
        }
    }

    public void onUpdateTasks()
    {
        ArrayList arraylist = new ArrayList();
        Iterator iterator;
        EntityAITaskEntry entityaitaskentry;

        if (this.tickCount++ % this.tickRate == 0)
        {
            iterator = this.taskEntries.iterator();

            while (iterator.hasNext())
            {
                entityaitaskentry = (EntityAITaskEntry)iterator.next();
                boolean flag = this.executingTaskEntries.contains(entityaitaskentry);

                if (flag)
                {
                    if (this.canUse(entityaitaskentry) && this.canContinue(entityaitaskentry))
                    {
                        continue;
                    }

                    entityaitaskentry.action.resetTask();
                    this.executingTaskEntries.remove(entityaitaskentry);
                }

                if (this.canUse(entityaitaskentry) && entityaitaskentry.action.shouldExecute())
                {
                    arraylist.add(entityaitaskentry);
                    this.executingTaskEntries.add(entityaitaskentry);
                }
            }
        }
        else
        {
            iterator = this.executingTaskEntries.iterator();

            while (iterator.hasNext())
            {
                entityaitaskentry = (EntityAITaskEntry)iterator.next();

                if (!entityaitaskentry.action.continueExecuting())
                {
                    entityaitaskentry.action.resetTask();
                    iterator.remove();
                }
            }
        }

        this.theProfiler.startSection("goalStart");
        iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
            entityaitaskentry = (EntityAITaskEntry)iterator.next();
            this.theProfiler.startSection(entityaitaskentry.action.getClass().getSimpleName());
            entityaitaskentry.action.startExecuting();
            this.theProfiler.endSection();
        }

        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        iterator = this.executingTaskEntries.iterator();

        while (iterator.hasNext())
        {
            entityaitaskentry = (EntityAITaskEntry)iterator.next();
            entityaitaskentry.action.updateTask();
        }

        this.theProfiler.endSection();
    }

    /**
     * Determine if a specific AI Task should continue being executed.
     */
    private boolean canContinue(EntityAITaskEntry par1EntityAITaskEntry)
    {
        this.theProfiler.startSection("canContinue");
        boolean flag = par1EntityAITaskEntry.action.continueExecuting();
        this.theProfiler.endSection();
        return flag;
    }

    /**
     * Determine if a specific AI Task can be executed, which means that all running higher (= lower int value) priority
     * tasks are compatible with it or all lower priority tasks can be interrupted.
     */
    private boolean canUse(EntityAITaskEntry par1EntityAITaskEntry)
    {
        this.theProfiler.startSection("canUse");
        Iterator iterator = this.taskEntries.iterator();

        while (iterator.hasNext())
        {
            EntityAITaskEntry entityaitaskentry1 = (EntityAITaskEntry)iterator.next();

            if (entityaitaskentry1 != par1EntityAITaskEntry)
            {
                if (par1EntityAITaskEntry.priority >= entityaitaskentry1.priority)
                {
                    if (this.executingTaskEntries.contains(entityaitaskentry1) && !this.areTasksCompatible(par1EntityAITaskEntry, entityaitaskentry1))
                    {
                        this.theProfiler.endSection();
                        return false;
                    }
                }
                else if (this.executingTaskEntries.contains(entityaitaskentry1) && !entityaitaskentry1.action.isInterruptible())
                {
                    this.theProfiler.endSection();
                    return false;
                }
            }
        }

        this.theProfiler.endSection();
        return true;
    }

    /**
     * Returns whether two EntityAITaskEntries can be executed concurrently
     */
    private boolean areTasksCompatible(EntityAITaskEntry par1EntityAITaskEntry, EntityAITaskEntry par2EntityAITaskEntry)
    {
        return (par1EntityAITaskEntry.action.getMutexBits() & par2EntityAITaskEntry.action.getMutexBits()) == 0;
    }
}
