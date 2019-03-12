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
    protected String titleXpath = "dc:title";
    protected String descriptionXpath = "dc:description";
    protected String productPrice = "product_schema:price";
    protected String productDistributor = "product:Distributor";
    protected String distributorName = "product:Distributor/Name";
    protected String distributorLocation = "product:Distributor/Location";
    protected String productAvailability = "product_schema:available";

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
        doc.setPropertyValue(titleXpath, title);
    }

    public String getDistributorName() {
        return (String) doc.getPropertyValue(distributorName);
    }

    public String getDistributorLocation() {
        return (String) doc.getPropertyValue(distributorLocation);
    }

    public String getDescription() {
        return (String) doc.getPropertyValue(descriptionXpath);
    }

    public void setDescription(String value) {
        doc.setPropertyValue(descriptionXpath, value);
    }

    public Double getPrice() {
        return (Double) doc.getPropertyValue(productPrice);
    }

    public void setPrice(Double price) {
        doc.setPropertyValue(productPrice, price);
    }

    public Boolean getAvailability() {
        return (Boolean) doc.getPropertyValue(productAvailability);
    }

    public void setAvailability(Boolean availability) {
        doc.setPropertyValue(productAvailability, availability);
    }

    public void setDistributor(String name, String location) {
        Map<String, Serializable> distributor = new HashMap<>();
        distributor.put("Name", name);
        distributor.put("Location", location);
        doc.setPropertyValue(productDistributor, (Serializable) distributor);
    }
}