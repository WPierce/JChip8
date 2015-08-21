import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author William Pierce
 */
public class Log4JConfigurator
{
    public static void configureFrom(String filePath) throws IOException
    {
        ConfigurationSource source = new ConfigurationSource(new FileInputStream(filePath));
        Configurator.initialize(null, source);
    }
}
