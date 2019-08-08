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

import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.RandomDocumentPicks;
import org.elasticsearch.test.ESTestCase;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Collections;


public class LimitrepProcessorTests extends ESTestCase  {

    private static LimitrepProcessor processor;
    @BeforeClass
    public static void setupProcessor() throws IOException {
        processor = new LimitrepProcessor("", "message");
    }

    public void testContentBeFiltered() throws Exception {
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(),
                Collections.singletonMap("message", "content that should be filtered"));
        IngestDocument targetIngestDocument = processor.execute(ingestDocument);
        assertNull(targetIngestDocument);
    }

    public void testNullField() throws Exception {
        IngestDocument originIngestDocument = RandomDocumentPicks.randomIngestDocument(random(),
                Collections.singletonMap("message", null));
        IngestDocument targetIngestDocument = new IngestDocument(originIngestDocument);
        processor.execute(targetIngestDocument);
        assertEquals(originIngestDocument, targetIngestDocument);
    }

    public void testEmptyDoc() throws Exception {
        IngestDocument originIngestDocument = RandomDocumentPicks.randomIngestDocument(random(),
                Collections.emptyMap());
        IngestDocument targetIngestDocument = new IngestDocument(originIngestDocument);
        processor.execute(targetIngestDocument);
        assertEquals(originIngestDocument, targetIngestDocument);
    }

}
