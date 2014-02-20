/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.champeau.teamcity;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class GroovyRunType extends RunType {
    private PluginDescriptor descriptor;

    public GroovyRunType(PluginDescriptor descriptor, RunTypeRegistry registry) {
        registry.registerRunType(this);
        this.descriptor = descriptor;
    }

    @NotNull
    @Override
    public String getType() {
        return "Groovy script";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Groovy script";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Allows execution of Groovy scripts as build steps";
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(final Map<String, String> stringStringMap) {
                Collection<InvalidProperty> invalid = new LinkedList<InvalidProperty>();
                for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                    String key = stringStringEntry.getKey();
                    String value = stringStringEntry.getValue();
                    if ("scriptBody".equals(key) && value==null) {
                        invalid.add(new InvalidProperty(key, "Script body can be null"));
                    }
                }
                return invalid;
            }
        };
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return descriptor.getPluginResourcesPath("groovyRunParams.jsp");
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        return descriptor.getPluginResourcesPath("viewGroovyParams.jsp");
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return null;
    }
}
