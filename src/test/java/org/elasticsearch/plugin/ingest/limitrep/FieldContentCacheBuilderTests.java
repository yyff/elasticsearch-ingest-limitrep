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

public class FieldContentCacheBuilderTests extends ESTestCase {
    public void testCacheBuilder() throws Exception {
        assertNotNull(FieldContentCacheBuilder.build(10, 1000, "SHA-1"));
        assertNotNull(FieldContentCacheBuilder.build(10, 1000, "SHA-256"));
        assertNotNull(FieldContentCacheBuilder.build(10, 1000, "SHA-384"));
        assertNotNull(FieldContentCacheBuilder.build(10, 1000, "SHA-512"));
        assertNotNull(FieldContentCacheBuilder.build(10, 1000, "MD5"));

    }

    public void testInvalidParam() throws Exception {
        expectThrows(IllegalArgumentException.class, ()-> FieldContentCacheBuilder.build(-1, 10, "SHA-1"));
        expectThrows(IllegalArgumentException.class, ()-> FieldContentCacheBuilder.build(1, -1, "SHA-1"));
        expectThrows(IllegalArgumentException.class, ()-> FieldContentCacheBuilder.build(1, 1000, "invalid_method"));
        expectThrows(IllegalArgumentException.class, ()-> FieldContentCacheBuilder.build(1, 1000, "MURMUR3"));
    }
}
