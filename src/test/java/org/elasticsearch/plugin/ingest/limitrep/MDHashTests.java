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

public class MDHashTests extends ESTestCase {
    public void testInvalidMethod() {
        expectThrows(IllegalArgumentException.class, () -> new MDHash("invalid method"));
    }


    public void testMDHashMethod() throws Exception {
        HashFunction hf = new MDHash("SHA-1");
        assertEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue"));
        assertNotEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue2"));


         hf = new MDHash("SHA-256");
        assertEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue"));
        assertNotEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue2"));

         hf = new MDHash("SHA-384");
        assertEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue"));
        assertNotEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue2"));

         hf = new MDHash("SHA-512");
        assertEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue"));
        assertNotEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue2"));

         hf = new MDHash("MD5");
        assertEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue"));
        assertNotEquals(hf.hash("fieldvalue"), hf.hash("fieldvalue2"));

    }

}
