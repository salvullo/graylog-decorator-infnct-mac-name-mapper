package ced.infnct.graylog.decorator;

import com.google.common.collect.ImmutableMap;
import com.google.inject.assistedinject.Assisted;
import org.graylog2.decorators.Decorator;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.decorators.SearchResponseDecorator;
import org.graylog2.rest.models.messages.responses.ResultMessageSummary;
import org.graylog2.rest.resources.search.responses.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


/**
 * This is the plugin. Your class should implement one of the existing plugin
 * interfaces. (i.e. AlarmCallback, MessageInput, MessageOutput)
 */
public class MAC2NameMapperDecorator implements SearchResponseDecorator {

    private static final Logger log = LoggerFactory.getLogger(MAC2NameMapperDecorator.class);

    private static final String CK_SOURCE_FIELD = "source_field";
    private static final String CK_TARGET_FIELD = "target_field";

    private static Map<String, String> MAC_MAPPING;


    private final String sourceField;
    private final String targetField;

    @Inject
    public MAC2NameMapperDecorator(@Assisted Decorator decorator, @Named("infnct_mac_name_mapper_file") String mapFilename) {

        this.sourceField = (String) requireNonNull(decorator.config().get(CK_SOURCE_FIELD), CK_SOURCE_FIELD + " cannot be null");
        this.targetField = (String) requireNonNull(decorator.config().get(CK_TARGET_FIELD), CK_TARGET_FIELD + " cannot be null");

        MAC_MAPPING = GSONUtils.deserializeImmutableMap(mapFilename);
    }

    @Override
    public SearchResponse apply(SearchResponse searchResponse) {
        final List<ResultMessageSummary> summaries = searchResponse.messages().stream()
                .map(summary -> {
                    // Do not touch the message if the field does not exist.
                    if (!summary.message().containsKey(sourceField)) {
                        return summary;
                    }

                    final String mac = String.valueOf(summary.message().get(sourceField)).
                            replaceAll("[-]", ":").toLowerCase();

                    final String apname = MAC_MAPPING.get(mac);

                    // If we cannot map the severity we do not touch the message.
                    if (apname == null) {
                        return summary;
                    }

                    final Message message = new Message(ImmutableMap.copyOf(summary.message()));

                    message.addField(targetField, apname);

                    return summary.toBuilder().message(message.getFields()).build();
                })
                .collect(Collectors.toList());

        return searchResponse.toBuilder().messages(summaries).build();
    }

    public interface Factory extends SearchResponseDecorator.Factory {
        @Override
        MAC2NameMapperDecorator create(Decorator decorator);

        @Override
        MAC2NameMapperDecorator.Config getConfig();

        @Override
        MAC2NameMapperDecorator.Descriptor getDescriptor();
    }

    public static class Config implements SearchResponseDecorator.Config {
        @Override
        public ConfigurationRequest getRequestedConfiguration() {
            return new ConfigurationRequest() {
                {
                    addField(new TextField(
                            CK_SOURCE_FIELD,
                            "Source field",
                            "mac",
                            "The message field which contains the MAC address of a device."
                    ));
                    addField(new TextField(
                            CK_TARGET_FIELD,
                            "Target field",
                            "name",
                            "The message field that will be created with the mapped device name."
                    ));
                }
            };
        }
    }

    public static class Descriptor extends SearchResponseDecorator.Descriptor {
        public Descriptor() {
            super("INFNCTMAC2NameMapperDecorator", "http://docs.graylog.org/", "INFNCT MAC->Name Mapper");
        }
    }


}
