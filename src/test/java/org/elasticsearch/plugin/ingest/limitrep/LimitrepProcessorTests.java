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

import static org.hamcrest.Matchers.equalTo;


public class LimitrepProcessorTests extends ESTestCase  {

    private static LimitrepProcessor processor;
    @BeforeClass
    public static void setupProcessor() throws IOException {
        FieldContentCache cache = FieldContentCacheBuilder.build(10, 1000, "SHA-1");
        processor = new LimitrepProcessor("", "message", cache);
    }

    public void testRepeatedContentBeFiltered() throws Exception {
        setupProcessor();
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(),
                Collections.singletonMap("message", "repeated content should be filtered"));
        IngestDocument targetIngestDocument = processor.execute(ingestDocument);
        assertNotNull(targetIngestDocument);
        assertThat(targetIngestDocument.getFieldValue("message", String.class), equalTo("repeated content should be filtered"));

        ingestDocument = RandomDocumentPicks.randomIngestDocument(random(),
                Collections.singletonMap("message", "test content need be long enough. test content need be long enough"));
        targetIngestDocument = processor.execute(ingestDocument);
        assertNotNull(targetIngestDocument);
        assertThat(targetIngestDocument.getFieldValue("message", String.class),
                equalTo("test content need be long enough. test content need be long enough"));

        ingestDocument = RandomDocumentPicks.randomIngestDocument(random(),
                Collections.singletonMap("message", "repeated content should be filtered"));
        targetIngestDocument = processor.execute(ingestDocument);
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
