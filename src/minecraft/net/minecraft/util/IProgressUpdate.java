package net.minecraft.util;

public interface IProgressUpdate
{
    /**
     * "Saving level", or the loading,or downloading equivelent
     */
    void displayProgressMessage(String s);

    /**
     * This is called with "Working..." by resetProgressAndMessage
     */
    void resetProgresAndWorkingMessage(String s);

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    void setLoadingProgress(int i);
}
