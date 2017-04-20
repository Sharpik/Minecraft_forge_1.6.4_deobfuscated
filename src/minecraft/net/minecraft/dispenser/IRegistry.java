package net.minecraft.dispenser;

public interface IRegistry
{
    Object getObject(Object object);

    /**
     * Register an object on this registry.
     */
    void putObject(Object object, Object object1);
}
