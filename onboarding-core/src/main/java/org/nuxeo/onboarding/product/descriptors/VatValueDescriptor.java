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
