package ced.infnct.graylog.decorator;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

/**
 * Implement the Plugin interface here.
 */
public class MAC2NameMapperDecoratorPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new MAC2NameMapperDecoratorMetaData();
    }

    @Override
    public Collection<PluginModule> modules() {
        return Collections.singletonList(new MAC2NameMapperDecoratorModule());
    }
}
