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

class FieldContentCacheBuilder {
    public static FieldContentCache build(long cacheSize, long secondsExpired, String method) {
        Cache<String, Object> cache = CacheBuilder.<String, Object>builder().setMaximumWeight(cacheSize).
                setExpireAfterWrite(TimeValue.timeValueSeconds(secondsExpired)).build();
        HashFunction hf;
        switch (method) {
            case "SHA-1":
            case "SHA-256":
            case "SHA-384":
            case "SHA-512":
            case "MD5":
                hf = new MDHash(method);
                break;
            default:
                throw new IllegalArgumentException("no such hash method: " + method);
        }
        return new FieldContentCache(cache, hf);
    }
}
