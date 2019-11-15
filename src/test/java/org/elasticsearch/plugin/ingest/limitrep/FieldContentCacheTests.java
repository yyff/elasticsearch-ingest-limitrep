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
        FieldContentCache cache = new FieldContentCache(10, 100, new MDHash("SHA-1"));
//        FieldContentCache cache = new FieldContentCache(10, 100, new MDHash("SHA-1"));
        cache.put("test content");
        assertNotNull(cache.get("test content"));
    }

    public void testCacheWeight() throws Exception {
        FieldContentCache cache = new FieldContentCache(10, 100, new MDHash("SHA-1"));
        assertEquals(cache.getWeight(), 0);

        cache.put("test content");
        assertEquals(cache.getWeight(), 1);
        cache.put("test content");
        assertEquals(cache.getWeight(), 1);
        cache.put("another test content");
        assertEquals(cache.getWeight(), 2);
    }

    public void testCacheMiss() throws Exception {
        FieldContentCache cache = new FieldContentCache(10, 100, new MDHash("SHA-1"));
        cache.put("test content");
        assertNull(cache.get("another test content"));
    }


    public void testCacheMissByTimeout() throws Exception {
        FieldContentCache cache = new FieldContentCache(10, 1, new MDHash("SHA-1"));
        cache.put("test content");
        Thread.sleep(1000 );
        assertNull(cache.get("test content"));
    }

    public void testCacheMissByFull() throws Exception {
        FieldContentCache cache = new FieldContentCache(2, 1, new MDHash("SHA-1"));
        cache.put("test content");
        cache.put("test content2");
        cache.put("test content3");
        assertNull(cache.get("test content"));
    }

    public void testCacheMethod() throws Exception {
        FieldContentCache cache = new FieldContentCache(10, 100, new MDHash("SHA-1"));
        cache.put("test content");
        assertNotNull(cache.get("test content"));

        cache = new FieldContentCache(10, 100, new MDHash("SHA-256"));
        cache.put("test content");
        assertNotNull(cache.get("test content"));

        cache = new FieldContentCache(10, 100, new MDHash("SHA-384"));
        cache.put("test content");
        assertNotNull(cache.get("test content"));

        cache = new FieldContentCache(10, 100, new MDHash("SHA-512"));
        cache.put("test content");
        assertNotNull(cache.get("test content"));

        cache = new FieldContentCache(10, 100, new MDHash("MD5"));
        cache.put("test content");
        assertNotNull(cache.get("test content"));
    }


}
