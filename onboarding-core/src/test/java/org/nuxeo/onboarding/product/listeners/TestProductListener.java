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
import org.nuxeo.onboarding.product.adapters.VisualAdapter;
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
    public void shouldChangeTheTitleToSoldOutAndMoveToFolder() {
        //Create Product
        DocumentModel doc = session.createDocumentModel("/", "ProductTest", "product");
        ProductAdapter productAdapter = doc.getAdapter(ProductAdapter.class);
        productAdapter.setTitle("Test Product");
        productAdapter.setPrice(10d);
        productAdapter.setAvailability(false);
        productAdapter.save();

        //Create Visual
        DocumentModel visual = session.createDocumentModel("/", "Visual", "visual");
        VisualAdapter visualAdapter = visual.getAdapter(VisualAdapter.class);
        visual.setPropertyValue("dc:title", "visual title");
        visualAdapter.save();

        //Add visual to collection
        collectionManager.addToCollection(productAdapter.getDoc(), visualAdapter.getDoc(), session);

        //Set availability of the product to false
        productAdapter.setAvailability(false);

        //Fire the event
        EventProducer eventProducer = Framework.getService(EventProducer.class);
        DocumentEventContext ctx = new DocumentEventContext(session, session.getPrincipal(), productAdapter.getDoc());
        Event event = ctx.newEvent("soldoutEvent");
        eventProducer.fireEvent(event);

        //Check permissions
        DocumentModel folder = session.getParentDocument(visualAdapter.getRef());
        ACE[] permissions = folder.getACP().getACL("soldout").getACEs();
        Assert.assertEquals("Group1", permissions[0].getUsername());
        Assert.assertEquals(SecurityConstants.READ, permissions[0].getPermission());
        Assert.assertTrue(permissions[0].isGranted());

        //Check if title was changed
        Assert.assertEquals(productAdapter.getTitle(), "Test Product - Sold Out!");

        // Check if it was moved
        DocumentModel newLocation = session.getDocument(visualAdapter.getRef());
        Assert.assertEquals(newLocation.getPathAsString(), "/default-domain/workspaces/Sold Out folder/Visual");
    }
}
