/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/) and others.
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
 * Contributors:
 *     Samuel Fialho
 */

package org.nuxeo.onboarding.product.adapters;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class AdapterFactory implements DocumentAdapterFactory {


    private static final String WRONG_DOCUMENT_TYPE = "Document Type not found";
    private static final String DUBLINCORE = "dublincore";

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if ("product".equals(doc.getType()) && doc.hasSchema(DUBLINCORE)) {
            return new ProductAdapter(doc);
        } else if ("visual".equals(doc.getType()) && doc.hasSchema(DUBLINCORE)) {
            return new VisualAdapter(doc);
        } else {
            throw new NuxeoException(WRONG_DOCUMENT_TYPE);
        }
    }
}
