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

package org.nuxeo.onboarding.product.listeners;

import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class ProductListener implements EventListener {

    @Override
    public void handleEvent(Event event) {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();


        if (doc == null) {
            return;
        }
        String type = doc.getType();

        Boolean available = (Boolean) doc.getPropertyValue("product_schema:available");
        if ("product".equals(type) && !available) {
            ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
            productAdapter.setDocumentTitle(productAdapter.getTitle() + " - Sold Out!");
            ctx.getCoreSession().saveDocument(doc);

            CoreSession session = doc.getCoreSession();
            DocumentModel folder = session.createDocumentModel("/", "Sold Out folder", "Folder");
            folder = session.createDocument(folder);
            ctx.getCoreSession().saveDocument(folder);

            List<DocumentRef> visualsInProduct = doc.getAdapter(Collection.class)
                    .getCollectedDocumentIds()
                    .stream()
                    .map(IdRef::new)
                    .collect(Collectors.toList());

            ACP acp = new ACPImpl();
            acp.addACE("soldout", new ACE("Group1", SecurityConstants.READ, true));
            folder.setACP(acp, true);

            DocumentRef folderRef = folder.getRef();
            ctx.getCoreSession().move(visualsInProduct, folderRef);
        }
    }
}