package io.swagger.oas.integration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.oas.models.OpenAPI;
import io.swagger.util.Json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OpenApiConfiguration { // implements OpenAPIConfig {

    private OpenAPI openApi = new OpenAPI();

    Map<String, Object> userDefinedOptions = new ConcurrentHashMap<>();
    private boolean basePathAsKey;

    private String id;
    private String resourcePackageNames;
    private String resourceClassNames;
    private String filterClassName;
    private String readerClassName;
    private String scannerClassName;
    private String processorClassName;

    private boolean prettyPrint;
    private boolean scanAllResources;
    private Collection<String> ignoredRoutes = Collections.emptySet();

    public boolean isScanAllResources() {
        return scanAllResources;
    }

    public void setScanAllResources(boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
    }

    public OpenApiConfiguration withScanAllResources(boolean scanAllResources) {
        this.scanAllResources = scanAllResources;
        return this;
    }

    public Collection<String> getIgnoredRoutes() {
        return ignoredRoutes;
    }

    public void setIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
    }

    public OpenApiConfiguration withIgnoredRoutes(Collection<String> ignoredRoutes) {
        this.ignoredRoutes = ignoredRoutes == null || ignoredRoutes.isEmpty() ? Collections.<String>emptySet()
                : Collections.unmodifiableCollection(new HashSet<String>(ignoredRoutes));
        return this;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public OpenApiConfiguration withPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }


    public Set<Class<?>> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<Class<?>> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    @JsonIgnore
    private Set<Class<?>> resourceClasses;


    private boolean pathAsProcessorKey;

    public static Map<String, OpenApiConfiguration> fromUrl(URL location, String defaultId) {

        // get file as string (for the moment, TODO use commons config)
        // load from classpath etc, look from file..
        try {
            Map<String, OpenApiConfiguration> configurationMap = new HashMap<>();

            String configAsString = readUrl(location);
            List<OpenApiConfiguration> configurations = Json.mapper().readValue(configAsString, new TypeReference<List<OpenApiConfiguration>>() {
            });
            for (OpenApiConfiguration config : configurations) {
                // TODO we have multiple base paths in 3.0? how to handle?
                // TODO use urls as kyes, but need to resolve the url with placeholders and use that?
                // for the moment use title instead,
/*
                if (config.openApi.getInfo() == null || StringUtils.isEmpty(config.openApi.getInfo().getTitle())){
                    if (StringUtils.isEmpty(id)) {
                        config.openApi.setInfo(
                                (config.openApi.getInfo() == null ?
                                        new Info() :
                                        config.openApi.getInfo()).title("/")
                        );
                    } else {
                        config.openApi.setInfo(
                                (config.openApi.getInfo() == null ?
                                        new Info() :
                                        config.openApi.getInfo()).title(id)
                        );
                    }
                }

                configurationMap.put(config.openApi.getInfo().getTitle(), config);
*/

                configurationMap.put((config.getId() == null) ? defaultId : config.getId(), config);

/*
                if (StringUtils.isEmpty(config.openApi.basePath("/"))){
                    if (StringUtils.isEmpty(basePath)) {
                        config.openApi.basePath("/");
                    } else {
                        config.openApi.basePath(basePath);
                    }
                }
                configurationMap.put(config.openApi.getBasePath(), config);
*/
            }
            return configurationMap;

        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            throw new RuntimeException("exception reading config", e);
        }

    }

    private static Properties loadProperties(URI uri) throws IOException {
        FileReader reader = new FileReader(new File(uri));
        Properties props = new Properties();
        props.load(reader);
        reader.close();
        return props;
    }

    private static String readUrl(URL url) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine).append("\n");
        }
        in.close();
        return sb.toString();
    }

    public OpenAPI getOpenApi() {
        return openApi;
    }
    public void setOpenApi (OpenAPI openApi) {
        this.openApi = openApi;
    }
    public OpenApiConfiguration openApi (OpenAPI openApi) {
        this.openApi = openApi;
        return this;
    }

    public boolean isPathAsProcessorKey() {
        return pathAsProcessorKey;
    }

    public void setPathAsProcessorKey(boolean pathAsProcessorKey) {
        this.pathAsProcessorKey = pathAsProcessorKey;
    }

    public OpenApiConfiguration withPathAsProcessorKey(boolean pathAsProcessorKey) {
        this.pathAsProcessorKey = pathAsProcessorKey;
        return this;
    }

    public String getReaderClassName() {
        return readerClassName;
    }

    public void setReaderClassName(String readerClassName) {
        this.readerClassName = readerClassName;
    }

    public String getScannerClassName() {
        return scannerClassName;
    }

    public void setScannerClassName(String scannerClassName) {
        this.scannerClassName = scannerClassName;
    }

    public String getProcessorClassName() {
        return processorClassName;
    }

    public void setProcessorClassName(String processorClassName) {
        this.processorClassName = processorClassName;
    }

    public Map<String, Object> getUserDefinedOptions() {
        return userDefinedOptions;
    }

    public void setUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
    }

    public OpenApiConfiguration withScannerClass(String scannerClass) {
        this.scannerClassName = scannerClass;
        return this;
    }

    public OpenApiConfiguration withReaderClassName(String readerClass) {
        this.readerClassName = readerClass;
        return this;
    }

    public OpenApiConfiguration withProcessorClass(String processorClass) {
        this.processorClassName = processorClass;
        return this;
    }

    public OpenApiConfiguration withUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
        return this;
    }

    public boolean isBasePathAsKey() {
        return basePathAsKey;
    }

    public void setBasePathAsKey(boolean basePathAsKey) {
        this.basePathAsKey = basePathAsKey;
    }

    public OpenApiConfiguration withBasePathAsKey(boolean basePathAsKey) {
        this.basePathAsKey = basePathAsKey;
        return this;
    }

    public String getResourcePackageNames() {
        return resourcePackageNames;
    }

    public void setResourcePackageNames(String resourcePackageNames) {
        this.resourcePackageNames = resourcePackageNames;
    }

    public OpenApiConfiguration withResourcePackageNames(String resourcePackage) {
        this.resourcePackageNames = resourcePackage;
        return this;
    }

    public String getResourceClassNames() {
        return resourceClassNames;
    }

    public void setResourceClassNames(String resourcePackageNames) {
        this.resourceClassNames = resourceClassNames;
    }

    public OpenApiConfiguration withResourceClassNames(String resourceClassNames) {
        this.resourceClassNames = resourceClassNames;
        return this;
    }


    public String getFilterClassName() {
        return filterClassName;
    }

    public void setFilterClassName(String filterClassName) {
        this.filterClassName = filterClassName;
    }

    public OpenApiConfiguration withFilterClass(String filterClass) {
        this.filterClassName = filterClass;
        return this;
    }


    public OpenApiConfiguration withId(String id) {
        this.id = id;
        return this;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
