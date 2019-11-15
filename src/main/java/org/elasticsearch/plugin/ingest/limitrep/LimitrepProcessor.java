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
import java.util.Map;
import java.util.regex.Pattern;

import static org.elasticsearch.ingest.ConfigurationUtils.readIntProperty;
import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;

public class LimitrepProcessor extends AbstractProcessor {

    public static final String TYPE = "limitrep";

    private final String field;
    private final FieldContentCache cache;
    private Pattern ignorePattern = null;

    private static String concatHashKey(String index, String field, String fieldValue) {
        return String.format("%s_%s_%s", index, field, fieldValue);
    }

    public LimitrepProcessor(String tag, String field, FieldContentCache cache) {
        super(tag);
        this.field = field;
        this.cache = cache;
    }

    public LimitrepProcessor(String tag, String field, FieldContentCache cache, Pattern ignorePattern) {
        this(tag, field, cache);
        this.ignorePattern = ignorePattern;
    }

    @Override
    public IngestDocument execute(IngestDocument ingestDocument) throws Exception {
        String content = ingestDocument.getFieldValue(field, String.class, true);
        if (content == null) {
            return ingestDocument;
        }
        String index = (String)ingestDocument.getSourceAndMetadata().get(IngestDocument.MetaData.INDEX.getFieldName());

        // TODO: use processing chain
        if (ignorePattern != null) {
            content = ignorePattern.matcher(content).replaceAll("");
        }

        String key = concatHashKey(index, field, content);
        if (cache.get(key) == null) {
            cache.put(key);
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
            throws IllegalArgumentException {
            String field = readStringProperty(TYPE, tag, config, "field");
            long timeInterval = readIntProperty(TYPE, tag, config, "timeInterval", 3600);
            long cacheSize = readIntProperty(TYPE, tag, config, "cacheSize", 1024 * 1024);
            String method = readStringProperty(TYPE, tag, config, "method", "MD5");

            // check parameters
            if (timeInterval <= 0 || timeInterval > 60 * 60) {
                throw new IllegalArgumentException("timeInterval must be in [0, 3600]");
            }
            if (cacheSize <= 0 || cacheSize > 1024 * 1024) {
                throw new IllegalArgumentException("cacheSize must be in (0, 1048576]");
            }


            String ignorePattern = readStringProperty(TYPE, tag, config, "ignorePattern", "");
            Pattern p = null;
            if (!ignorePattern.equals("")) {
                p = Pattern.compile(ignorePattern);
            }

            FieldContentCache cache = new FieldContentCache(cacheSize, timeInterval, new MDHash(method));
            return new LimitrepProcessor(tag, field, cache, p);
        }
    }
}
