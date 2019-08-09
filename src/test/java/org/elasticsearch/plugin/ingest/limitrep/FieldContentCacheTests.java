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

import org.elasticsearch.test.ESTestCase;

public class FieldContentCacheTests extends ESTestCase {


    public void testCacheHit() throws Exception {
        FieldContentCache cache = FieldContentCacheBuilder.build(10, 100, "SHA-1");
        cache.put("message", "test content");
        assertNotNull(cache.get("message", "test content"));
    }

    public void testCacheWeight() throws Exception {
        FieldContentCache cache = FieldContentCacheBuilder.build(10, 100, "SHA-1");
        assertEquals(cache.getWeight(), 0);

        cache.put("message", "test content");
        assertEquals(cache.getWeight(), 1);
        cache.put("message", "test content");
        assertEquals(cache.getWeight(), 1);
        cache.put("message", "another test content");
        assertEquals(cache.getWeight(), 2);
    }

    public void testCacheMiss() throws Exception {
        FieldContentCache cache = FieldContentCacheBuilder.build(10, 100, "SHA-1");
        cache.put("message", "test content");
        assertNull(cache.get("message", "another test content"));
    }


    public void testCacheMissByTimeout() throws Exception {
        FieldContentCache cache = FieldContentCacheBuilder.build(10, 1, "SHA-1");
        cache.put("message", "test content");
        Thread.sleep(1000 );
        assertNull(cache.get("message", "test content"));
    }

    public void testCacheMissByFull() throws Exception {
        FieldContentCache cache = FieldContentCacheBuilder.build(2, 1, "SHA-1");
        cache.put("message", "test content");
        cache.put("message", "test content2");
        cache.put("message", "test content3");
        assertNull(cache.get("message", "test content"));
    }

    public void testCacheMethod() throws Exception {
        FieldContentCache cache = FieldContentCacheBuilder.build(10, 100, "SHA-1");
        cache.put("message", "test content");
        assertNotNull(cache.get("message", "test content"));

        cache = FieldContentCacheBuilder.build(10, 100, "SHA-256");
        cache.put("message", "test content");
        assertNotNull(cache.get("message", "test content"));

        cache = FieldContentCacheBuilder.build(10, 100, "SHA-384");
        cache.put("message", "test content");
        assertNotNull(cache.get("message", "test content"));

        cache = FieldContentCacheBuilder.build(10, 100, "SHA-512");
        cache.put("message", "test content");
        assertNotNull(cache.get("message", "test content"));

        cache = FieldContentCacheBuilder.build(10, 100, "MD5");
        cache.put("message", "test content");
        assertNotNull(cache.get("message", "test content"));
    }


}
