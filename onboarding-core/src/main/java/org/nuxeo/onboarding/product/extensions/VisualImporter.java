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

package org.nuxeo.onboarding.product.extensions;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.filemanager.api.FileImporterContext;
import org.nuxeo.ecm.platform.filemanager.service.extension.AbstractFileImporter;
import org.nuxeo.ecm.platform.filemanager.utils.FileManagerUtils;
import org.nuxeo.onboarding.product.adapters.VisualAdapter;

import java.io.Serializable;

public class VisualImporter extends AbstractFileImporter {

    @Override
    public DocumentModel createOrUpdate(FileImporterContext context) {
        CoreSession session = context.getSession();
        DocumentModel doc = session.createDocumentModel(context.getParentPath(), context.getFileName(), "visual");
        VisualAdapter visualAdapter = doc.getAdapter(VisualAdapter.class);

        String filename = FileManagerUtils.fetchFileName(context.getBlob().getFilename());
        String title = FileManagerUtils.fetchTitle(filename);

        DocumentModel existingDocument = FileManagerUtils.getExistingDocByTitle(session, context.getParentPath(), title);

        if (existingDocument != null) {
            String fileName = context.getBlob().getFilename();
            String[] splitFileName;
            splitFileName = fileName.split("\\.");
            String name = splitFileName[0];
            String extension = splitFileName[1];
            visualAdapter.setTitle(name + "_COPY." + extension);
            visualAdapter.setFileContent((Serializable) context.getBlob());
            doc = session.createDocument(doc);
            session.save();
            return doc;
        }
        visualAdapter.setTitle(context.getBlob().getFilename());
        visualAdapter.setFileContent((Serializable) context.getBlob());
        doc = session.createDocument(doc);
        session.save();

        return doc;
    }
}