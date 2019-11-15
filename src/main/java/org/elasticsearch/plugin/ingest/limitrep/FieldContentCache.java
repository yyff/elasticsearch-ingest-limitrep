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
import org.elasticsearch.common.cache.Cache;
import org.elasticsearch.common.cache.CacheBuilder;
import org.elasticsearch.common.unit.TimeValue;

class FieldContentCache {

    private final Cache<String, Object> cache;
    private final HashFunction hf;

    FieldContentCache(long cacheSize, long secondsExpired, HashFunction hf) {
        this.cache = CacheBuilder.<String, Object>builder().setMaximumWeight(cacheSize).
                setExpireAfterWrite(TimeValue.timeValueSeconds(secondsExpired)).build();
        this.hf = hf;
    }

    Object get(String content) throws Exception {
        return cache.get(hf.hash(content));
    }

    void put(String content) throws Exception {
        cache.put(hf.hash(content), new Object());
    }

    long getWeight() {
        return cache.weight();
    }


}
