package org.modelix.model.repositoryconcepts.structure.concepts;

/*Generated by MPS */

import org.modelix.mps.apigen.runtime.AbstractConcept;
import org.modelix.model.repositoryconcepts.structure.ModelReference;
import org.jetbrains.annotations.NotNull;
import org.modelix.model.api.INode;
import java.util.HashMap;
import org.modelix.mps.apigen.runtime.MPSProperty;
import org.modelix.mps.apigen.runtime.MPSChildLink;
import org.modelix.mps.apigen.runtime.MPSReferenceLink;
import java.util.List;
import java.util.ArrayList;
import jetbrains.mps.lang.core.structure.concepts.BaseConceptConcept;

public class ModelReferenceConcept extends AbstractConcept<ModelReference> {

  @NotNull
  @Override
  public ModelReference createInstance(@NotNull INode node) {
    return new ModelReference(node);
  }
  private final HashMap<String, MPSProperty> properties = new HashMap<String, MPSProperty>();
  private final HashMap<String, MPSChildLink> children = new HashMap<String, MPSChildLink>();
  private final HashMap<String, MPSReferenceLink> references = new HashMap<String, MPSReferenceLink>();
  private final List<AbstractConcept<?>> superConcepts = new ArrayList<AbstractConcept<?>>();
  public static final ModelReferenceConcept INSTANCE = new ModelReferenceConcept();
  private ModelReferenceConcept() {
    super(false, "org.modelix.model.repositoryconcepts.structure.ModelReference", "ModelReference", "0a7577d1-d4e5-431d-98b1-fae38f9aee80/6402965165736932003");
  }

  @Override
  protected void doInit() {
    this.references.put("", new MPSReferenceLink(this, ModelConcept.INSTANCE, false, "model"));
    this.superConcepts.add(BaseConceptConcept.INSTANCE);
  }
  @NotNull
  @Override
  protected HashMap<String, MPSProperty> myProperties() {
    return properties;
  }
  @NotNull
  @Override
  protected HashMap<String, MPSChildLink> myChildLinks() {
    return children;
  }
  @NotNull
  @Override
  protected HashMap<String, MPSReferenceLink> myReferenceLinks() {
    return references;
  }
  @NotNull
  @Override
  protected List<AbstractConcept<?>> mySuperConcepts() {
    return superConcepts;
  }
}