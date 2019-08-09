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

class FieldContentCache {

    private final Cache<String, Object> cache;
    private final HashFunction hf;

    FieldContentCache(Cache<String, Object> cache, HashFunction hf) {
        this.cache = cache;
        this.hf = hf;
    }

    public Object get(String fieldName, String fieldValue) throws Exception {
        return cache.get(hf.hash(fieldName, fieldValue));
    }

    public void put(String fieldName, String fieldValue) throws Exception {
        cache.put(hf.hash(fieldName, fieldValue), new Object());
    }

    public long getWeight() {
        return cache.weight();
    }


}
