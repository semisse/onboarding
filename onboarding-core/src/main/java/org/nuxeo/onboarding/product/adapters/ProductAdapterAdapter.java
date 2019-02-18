package org.nuxeo.onboarding.product.adapters;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ProductAdapterAdapter {
    protected DocumentModel doc;

    protected String titleXpath = "dc:title";
    protected String descriptionXpath = "dc:description";

    public ProductAdapterAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public void save() {
        CoreSession session = doc.getCoreSession();
        doc = session.saveDocument(doc);
    }

    public DocumentRef getParentRef() {
        return doc.getParentRef();
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

    public void setTitle(String value) {
        doc.setPropertyValue(titleXpath, value);
    }

    public String getDescription() {
        return (String) doc.getPropertyValue(descriptionXpath);
    }

    public void setDescription(String value) {
        doc.setPropertyValue(descriptionXpath, value);
    }

    public void setDummyData() {
        doc.setPropertyValue("dc:title", "some title");
        doc.setPropertyValue("product_schema:price", 10d);
    }

    public void setDistributor(String name, String location) {
        Map<String, Serializable> distributor = new HashMap<>();
        distributor.put("Name", name);
        distributor.put("Location", location);
        doc.setPropertyValue("product:Distributor", (Serializable) distributor);
    }

    public String getDistributorName() {
        return (String) doc.getPropertyValue("product:Distributor/Name");
    }

    public String getDistributorLocation() {
        return (String) doc.getPropertyValue("product:Distributor/Location");
    }


}
