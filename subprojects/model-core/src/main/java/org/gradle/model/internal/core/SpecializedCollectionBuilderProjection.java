/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.model.internal.core;

import org.gradle.model.collection.CollectionBuilder;
import org.gradle.model.internal.core.rule.describe.ModelRuleDescriptor;
import org.gradle.model.internal.type.ModelType;

public class SpecializedCollectionBuilderProjection<P extends CollectionBuilder<E>, E> extends TypeCompatibilityModelProjectionSupport<P> {

    private final ModelType<P> publicType;
    private final SpecializedCollectionBuilderFactory<P, E> factory;

    public SpecializedCollectionBuilderProjection(ModelType<P> publicType, final SpecializedCollectionBuilderFactory<P, E> factory) {
        super(publicType, true, true);
        this.publicType = publicType;
        this.factory = factory;
    }

    @Override
    protected ModelView<P> toView(MutableModelNode modelNode, ModelRuleDescriptor ruleDescriptor, boolean writable) {
        P instance = factory.create(modelNode, ruleDescriptor);
        return new SpecializedCollectionBuilderView<P, E>(modelNode.getPath(), publicType, instance);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        SpecializedCollectionBuilderProjection<?, ?> that = (SpecializedCollectionBuilderProjection<?, ?>) o;

        return !(publicType != null ? !publicType.equals(that.publicType) : that.publicType != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (publicType != null ? publicType.hashCode() : 0);
        result = 31 * result + (factory != null ? factory.hashCode() : 0);
        return result;
    }

    @Override
    public <T> boolean canBeViewedAsWritable(ModelType<T> targetType) {
        return targetType.equals(publicType) && super.canBeViewedAsWritable(targetType);
    }

    @Override
    public <T> boolean canBeViewedAsReadOnly(ModelType<T> targetType) {
        return targetType.equals(publicType) && super.canBeViewedAsReadOnly(targetType);
    }
}
