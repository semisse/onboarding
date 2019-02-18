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

        // Add some logic starting from here.

        if (doc == null) {
            return;
        }
        String type = doc.getType();

        Boolean available = (Boolean) doc.getPropertyValue("product_schema:available");
        if ("product".equals(type) && !available) {
            doc.setPropertyValue("dc:title", doc.getPropertyValue("dc:title") + " - Sold Out!");
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
