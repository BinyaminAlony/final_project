package project_biu.configs;

/**
 * The Config interface defines the contract for configuration objects.
 * Implementations of this interface should provide methods to create configurations,
 * retrieve their name and version, and close resources.
 */
public interface Config {

    //Creates the configuration.
    void create();

    /**
     * Retrieves the name of the configuration.
     *
     * @return The name of the configuration.
     */
    String getName();

    /**
     * Retrieves the version of the configuration.
     *
     * @return The version of the configuration.
     */
    int getVersion();

    //Closes the configuration and releases resources.
    void close();
}
