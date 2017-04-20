package net.minecraft.network.rcon;

public interface IServer
{
    /**
     * Gets an integer property. If it does not exist, set it to the specified value.
     */
    int getIntProperty(String s, int i);

    /**
     * Gets a string property. If it does not exist, set it to the specified value.
     */
    String getStringProperty(String s, String s1);

    /**
     * Saves an Object with the given property name.
     */
    void setProperty(String s, Object object);

    /**
     * Saves all of the server properties to the properties file.
     */
    void saveProperties();

    /**
     * Returns the filename where server properties are stored
     */
    String getSettingsFilename();

    /**
     * Returns the server's hostname.
     */
    String getHostname();

    /**
     * Never used, but "getServerPort" is already taken.
     */
    int getPort();

    /**
     * Returns the server message of the day
     */
    String getServerMOTD();

    /**
     * Returns the server's Minecraft version as string.
     */
    String getMinecraftVersion();

    /**
     * Returns the number of players currently on the server.
     */
    int getCurrentPlayerCount();

    /**
     * Returns the maximum number of players allowed on the server.
     */
    int getMaxPlayers();

    /**
     * Returns an array of the usernames of all the connected players.
     */
    String[] getAllUsernames();

    String getFolderName();

    /**
     * Used by RCon's Query in the form of "MajorServerMod 1.2.3: MyPlugin 1.3; AnotherPlugin 2.1; AndSoForth 1.0".
     */
    String getPlugins();

    String executeCommand(String s);

    /**
     * Returns true if debugging is enabled, false otherwise.
     */
    boolean isDebuggingEnabled();

    /**
     * Logs the message with a level of INFO.
     */
    void logInfo(String s);

    /**
     * Logs the message with a level of WARN.
     */
    void logWarning(String s);

    /**
     * Logs the error message with a level of SEVERE.
     */
    void logSevere(String s);

    /**
     * If isDebuggingEnabled(), logs the message with a level of INFO.
     */
    void logDebug(String s);
}
