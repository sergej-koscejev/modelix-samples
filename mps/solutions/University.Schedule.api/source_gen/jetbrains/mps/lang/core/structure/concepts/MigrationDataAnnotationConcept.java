package jetbrains.mps.lang.core.structure.concepts;

/*Generated by MPS */

import org.modelix.mps.apigen.runtime.AbstractConcept;
import jetbrains.mps.lang.core.structure.MigrationDataAnnotation;
import org.jetbrains.annotations.NotNull;
import org.modelix.model.api.INode;
import java.util.HashMap;
import org.modelix.mps.apigen.runtime.MPSProperty;
import org.modelix.mps.apigen.runtime.MPSChildLink;
import org.modelix.mps.apigen.runtime.MPSReferenceLink;
import java.util.List;
import java.util.ArrayList;

public class MigrationDataAnnotationConcept extends AbstractConcept<MigrationDataAnnotation> {

  @NotNull
  @Override
  public MigrationDataAnnotation createInstance(@NotNull INode node) {
    return new MigrationDataAnnotation(node);
  }
  private final HashMap<String, MPSProperty> properties = new HashMap<String, MPSProperty>();
  private final HashMap<String, MPSChildLink> children = new HashMap<String, MPSChildLink>();
  private final HashMap<String, MPSReferenceLink> references = new HashMap<String, MPSReferenceLink>();
  private final List<AbstractConcept<?>> superConcepts = new ArrayList<AbstractConcept<?>>();
  public static final MigrationDataAnnotationConcept INSTANCE = new MigrationDataAnnotationConcept();
  private MigrationDataAnnotationConcept() {
    super(false, "jetbrains.mps.lang.core.structure.MigrationDataAnnotation", "MigrationDataAnnotation", "ceab5195-25ea-4f22-9b92-103b95ca8c0c/8703179436978668945");
  }

  @Override
  protected void doInit() {
    this.children.put("dataNode", new MPSChildLink(this, BaseConceptConcept.INSTANCE, false, false, "dataNode"));
    this.superConcepts.add(NodeAttributeConcept.INSTANCE);
    this.superConcepts.add(MigrationAnnotationConcept.INSTANCE);
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