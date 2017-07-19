package ced.infnct.graylog.decorator;

import com.github.joschi.jadconfig.Parameter;
import org.graylog2.plugin.PluginConfigBean;


/**
 * Created by salvullo on 12/07/2017.
 */
public class MAC2NameMapperDecoratorConfiguration implements PluginConfigBean {

    @Parameter(value = "infnct_mac_name_mapper_file", required = true)
    private String mapFileName = "/etc/graylog/infnct-mac-name-map";

    public String getMapFileName() {
        return mapFileName;
    }
}
