package ced.infnct.graylog.decorator;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class MAC2NameMapperDecoratorMetaData implements PluginMetaData {
    private static final String PLUGIN_PROPERTIES = "ced.infnct.graylog-decorator-infnct-mac-name-mapper/graylog-plugin.properties";

    @Override
    public String getUniqueId() {
        return "ced.infnct.graylog.decorator.MAC2NameMapperDecoratorPlugin";
    }

    @Override
    public String getName() {
        return "MAC2NameMapperDecorator";
    }

    @Override
    public String getAuthor() {
        return "salvullo <salvatore.monforte@ct.infn.it>";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/salvullo/graylog-decorator-infnct-mac-name-mapper");
    }

    @Override
    public Version getVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public String getDescription() {
        // TODO Insert correct plugin description
        return "Description of MAC2NameMapperDecorator plugin";
    }

    @Override
    public Version getRequiredVersion() {
        return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(0, 0, 0, "unknown"));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
