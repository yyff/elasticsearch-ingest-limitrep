/*
 * Copyright [2019] [Fan Yang]
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
 *
 */

package org.elasticsearch.plugin.ingest.limitrep;

import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.Processor;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.ingest.ConfigurationUtils.readIntProperty;
import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;

public class LimitrepProcessor extends AbstractProcessor {

    public static final String TYPE = "limitrep";

    private final String field;
    private final FieldContentCache cache;

    public LimitrepProcessor(String tag, String field, FieldContentCache cache) throws IOException {
        super(tag);
        this.field = field;
        this.cache = cache;
    }

    @Override
    public IngestDocument execute(IngestDocument ingestDocument) throws Exception {
        String content = ingestDocument.getFieldValue(field, String.class, true);
        if (content == null) {
            return ingestDocument;
        }
        if (cache.get(field, content) == null) {
            cache.put(field, content);
            return ingestDocument;
        }

        return null;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    String getField() {
        return field;
    }

    public static final class Factory implements Processor.Factory {

        @Override
        public LimitrepProcessor create(Map<String, Processor.Factory> factories, String tag, Map<String, Object> config) 
            throws Exception {
            String field = readStringProperty(TYPE, tag, config, "field");
            long timeInterval = readIntProperty(TYPE, tag, config, "timeInterval", 3600);
            long cacheSize = readIntProperty(TYPE, tag, config, "cacheSize", 1024 * 1024);
            String method = readStringProperty(TYPE, tag, config, "method", "MD5");

            FieldContentCache cache = FieldContentCacheBuilder.build(cacheSize, timeInterval, method);
            return new LimitrepProcessor(tag, field, cache);
        }
    }
}
