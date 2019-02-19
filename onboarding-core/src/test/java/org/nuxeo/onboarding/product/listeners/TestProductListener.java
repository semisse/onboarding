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

import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.onboarding.product.OnboardingTestFeature;
import org.nuxeo.onboarding.product.adapters.ProductAdapter;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({OnboardingTestFeature.class})
public class TestProductListener {

    protected final List<String> events = Arrays.asList("documentModified");

    @Inject
    protected EventService s;

    @Inject
    protected CoreSession session;

    @Inject
    protected CollectionManager collectionManager;

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("productlistener");
        assertNotNull(listener);
    }

    @Test
    public void shouldChangeTheTitleToSoldOut() {
        //Create Product
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        doc = session.createDocument(doc);
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setDocumentTitle("Test Product");
        productAdapter.setDocumentPrice(10d);
        doc.setPropertyValue("product_schema:available", true);
        doc = session.saveDocument(doc);

        //Create Visual
        DocumentModel visual = session.createDocumentModel("/", "Visual", "visual");
        visual = session.createDocument(visual);
        visual.setPropertyValue("dc:title", "visual title");
        session.saveDocument(visual);
        DocumentModel returnedVisual = session.getDocument(new PathRef("/Visual"));
        assertNotNull(returnedVisual);

        //Add visual to collection
        collectionManager.addToCollection(doc, visual, session);

        //Set availability of the product to false
        doc.setPropertyValue("product_schema:available", false);
        session.saveDocument(doc);

        //Fire the event
        EventProducer eventProducer = Framework.getService(EventProducer.class);
        DocumentEventContext ctx = new DocumentEventContext(session, session.getPrincipal(), doc);
        Event event = ctx.newEvent("soldoutEvent");
        eventProducer.fireEvent(event);

        //Check permissions
        DocumentModel folder = session.getParentDocument(visual.getRef());
        ACE[] permissions = folder.getACP().getACL("soldout").getACEs();
        Assert.assertEquals("Group1", permissions[0].getUsername());
        Assert.assertEquals(SecurityConstants.READ, permissions[0].getPermission());
        Assert.assertTrue(permissions[0].isGranted());

        //Check if title was changed
        DocumentModel retrieveDoc = session.getDocument(doc.getRef());
        Assert.assertEquals(retrieveDoc.getPropertyValue("dc:title"), "Test Product - Sold Out!");
    }
}
