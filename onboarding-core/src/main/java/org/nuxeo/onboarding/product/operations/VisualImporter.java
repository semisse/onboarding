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

package org.nuxeo.onboarding.product.operations;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.filemanager.service.extension.AbstractFileImporter;
import org.nuxeo.ecm.platform.types.TypeManager;

import java.io.Serializable;

public class VisualImporter extends AbstractFileImporter {

    private static final long serialVersionUID = 1L;

    @Override
    public DocumentModel create(CoreSession session, Blob content, String path, boolean overwrite, String fullname,
                                TypeManager typeService) {
        DocumentModel doc = session.createDocumentModel(path, content.getFilename(), "visual");
        doc.setPropertyValue("dc:title", content.getFilename());
        doc.setPropertyValue("file:content", (Serializable) content);
        doc = session.createDocument(doc);
        return doc;
    }
}