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
import java.util.HashMap;
import java.util.Map;

public class ProductAdapter {
    protected DocumentModel doc;
    protected final static String TITLE = "dc:title";
    protected final static String DESCRIPTION = "dc:description";
    protected final static String PRODUCT_PRICE = "product_schema:price";
    protected final static String PRODUCT_DISTRIBUTOR = "product:Distributor";
    protected final static String DISTRIBUTOR_NAME = "Name";
    protected final static String DISTRIBUTOR_LOCATION = "Location";
    protected final static String DISTRIBUTOR_NAME_FIELD = "product:Distributor/Name";
    protected final static String DISTRIBUTOR_LOCATION_FIELD = "product:Distributor/Location";
    protected final static String PRODUCT_AVAILABILITY = "product_schema:available";

    public ProductAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public void save() {
        CoreSession session = doc.getCoreSession();
        if (doc.getId() == null) {
            doc = session.createDocument(doc);
        }
        doc = session.saveDocument(doc);
    }

    public DocumentRef getParentRef() {
        return doc.getParentRef();
    }

    public DocumentRef getDocRef() {
        return doc.getRef();
    }

    public DocumentModel getDoc() {
        return doc;
    }

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

    public void setTitle(String title) {
        doc.setPropertyValue(TITLE, title);
    }

    public String getDistributorNameField() {
        return (String) doc.getPropertyValue(DISTRIBUTOR_NAME_FIELD);
    }

    public String getDistributorLocation() {
        return (String) doc.getPropertyValue(DISTRIBUTOR_LOCATION_FIELD);
    }

    public String getDescription() {
        return (String) doc.getPropertyValue(DESCRIPTION);
    }

    public void setDescription(String value) {
        doc.setPropertyValue(DESCRIPTION, value);
    }

    public Double getPrice() {
        return (Double) doc.getPropertyValue(PRODUCT_PRICE);
    }

    public void setPrice(Double price) {
        doc.setPropertyValue(PRODUCT_PRICE, price);
    }

    public Boolean getAvailability() {
        return (Boolean) doc.getPropertyValue(PRODUCT_AVAILABILITY);
    }

    public void setAvailability(Boolean availability) {
        doc.setPropertyValue(PRODUCT_AVAILABILITY, availability);
    }

    public void setDistributor(String name, String location) {
        Map<String, Serializable> distributor = new HashMap<>();
        distributor.put(DISTRIBUTOR_NAME, name);
        distributor.put(DISTRIBUTOR_LOCATION, location);
        doc.setPropertyValue(PRODUCT_DISTRIBUTOR, (Serializable) distributor);
    }
}