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


import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.test.ESTestCase;

import java.util.HashMap;

import java.util.Map;
import java.util.regex.PatternSyntaxException;

import static org.hamcrest.Matchers.equalTo;


public class LimitrepProcessorFactoryTests extends ESTestCase {

    private static final LimitrepProcessor.Factory factory = new LimitrepProcessor.Factory();

    public void testNoRequiredField()  {
        Map<String, Object> config = new HashMap<>();
        String processorTag = randomAlphaOfLength(10);

        expectThrows(ElasticsearchParseException.class, () -> factory.create(null, processorTag, config));
    }
    public void testBuildDefaults()  {
        Map<String, Object> config = new HashMap<>();
        config.put("field", "_field");

        String processorTag = randomAlphaOfLength(10);

        LimitrepProcessor processor = factory.create(null, processorTag, config);
        assertThat(processor.getTag(), equalTo(processorTag));
        assertThat(processor.getField(), equalTo("_field"));
    }

    public void testInvalidTimeInterval()  {
        String processorTag = randomAlphaOfLength(10);
        Map<String, Object> config = new HashMap<>();
        config.put("field", "_field");
        config.put("timeInterval", -1);
        expectThrows(IllegalArgumentException.class, () -> factory.create(null, processorTag, config));

        config.put("field", "_field");
        config.put("timeInterval", 3601);
        expectThrows(IllegalArgumentException.class, () -> factory.create(null, processorTag, config));
    }

    public void testInvalidCacheSize()  {
        String processorTag = randomAlphaOfLength(10);
        Map<String, Object> config = new HashMap<>();
        config.put("field", "_field");

        config.put("cacheSize", -1);
        expectThrows(IllegalArgumentException.class, () -> factory.create(null, processorTag, config));

        config.put("field", "_field");
        config.put("cacheSize", 1024 * 1024 + 1);
        expectThrows(IllegalArgumentException.class, () -> factory.create(null, processorTag, config));
    }

    public void testInvalidIgnorePattern()  {
        String processorTag = randomAlphaOfLength(10);
        Map<String, Object> config = new HashMap<>();
        config.put("field", "_field");
        config.put("ignorePattern", "*");
        expectThrows(PatternSyntaxException.class, () -> factory.create(null, processorTag, config));
    }


}
