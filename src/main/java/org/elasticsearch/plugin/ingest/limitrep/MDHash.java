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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDHash implements HashFunction {
    private MessageDigest md;

    public MDHash(String method) {
        try {
            this.md = MessageDigest.getInstance(method);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("no such hash method: "+ method);
        }
    }

    public String hash(String fieldName, String fieldValue) throws Exception {
        String key = fieldName + "_" + fieldValue;
        md.update(key.getBytes("utf-8"));
        return new String(md.digest(), "utf-8");
//        return Base64.getEncoder().encodeToString(md.digest());
    }

}