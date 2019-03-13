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

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import java.io.Serializable;

public class VisualAdapter {
    protected DocumentModel doc;
    protected final static String TITLE = "dc:title";
    protected final static String DESCRIPTION = "dc:description";
    protected final static String FILE_CONTENT = "file:content";

    public VisualAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public void save() {
        CoreSession session = doc.getCoreSession();
        if (doc.getId() == null) {
            doc = session.createDocument(doc);
        }
        doc = session.saveDocument(doc);
    }

    public DocumentModel getDoc() { return doc; }

    public DocumentRef getParentRef() {
        return doc.getParentRef();
    }

    public DocumentRef getRef() { return doc.getRef(); }

    public String getId() {
        return doc.getId();
    }

    public String getName() {
        return doc.getName();
    }

    public String getPath() {
        return doc.getPathAsString();
    }

    public String getState() {
        return doc.getCurrentLifeCycleState();
    }

    public String getTitle() {
        return doc.getTitle();
    }

    public Serializable getFileContent() { return doc.getPropertyValue(FILE_CONTENT); }

    public String getDescription() {
        return (String) doc.getPropertyValue(DESCRIPTION);
    }

    public void setDescription(String value) {
        doc.setPropertyValue(DESCRIPTION, value);
    }

    public void setTitle(String value) {
        doc.setPropertyValue(TITLE, value);
    }

    public void setFileContent(Serializable value) {
        doc.setPropertyValue(FILE_CONTENT, value);
    }
}
