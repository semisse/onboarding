package org.nuxeo.onboarding.product.descriptors;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.runtime.model.Descriptor;

@XObject("country")
public class VatValueDescriptor implements Descriptor {
    @XNode("@id")
    protected String countryNameId;

    @XNode("vatValue")
    protected Double vatValue;

    public VatValueDescriptor() {
    }

    public Double getVatValue() {
        return vatValue;
    }

    @Override
    public String getId() {
        return countryNameId;
    }

    @Override
    public Descriptor merge(Descriptor other) {
        VatValueDescriptor otherVat = (VatValueDescriptor) other;
        if (otherVat.getVatValue() > vatValue) {
            vatValue = otherVat.getVatValue();
        }
        return this;
    }

}
