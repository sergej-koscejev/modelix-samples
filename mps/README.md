# MPS Courses Domain Language & model

## Language

The language used in all examples is the same. The language itself is fairly small. It describes a very simplified lecture
schedule. The language intentionally doesn't use expressions, creating editors for expressions by hand is very cumbersome and
at the time where these samples were created modelix has no support for generating these editors for you.
The main concepts of the language are:

- [Room](http://127.0.0.1:63320/node?ref=r%3Adfa26643-4653-44bc-9dfe-5a6581bcd381%28University.Schedule.structure%29%2F4128798754188010580):
  where lecture are held. Each room has a maximum capacity of students, a name and some additional properties.
- [Lecture](http://127.0.0.1:63320/node?ref=r%3Adfa26643-4653-44bc-9dfe-5a6581bcd381%28University.Schedule.structure%29%2F4128798754188010560):
  Have a name, some description and reference a room where they are held. And also have a schedule which determines when
  they are held and if they repeat through the whole semester or are a one time lecture.
- [Student](http://127.0.0.1:63320/node?ref=r%3Adfa26643-4653-44bc-9dfe-5a6581bcd381%28University.Schedule.structure%29%2F1648392019017048449):
  A student with a name and birthday.
- [Assignment](http://127.0.0.1:63320/node?ref=r%3Adfa26643-4653-44bc-9dfe-5a6581bcd381%28University.Schedule.structure%29%2F1648392019017048460):
  Lecture assignments for a single student.

Some concepts are contained a root node like a `Rooms` container to make structuring the editors in MPS
easier:

```mermaid
  classDiagram
      class Rooms {
        <<root>>
      }
      class Courses {
        <<root>>
      }

      class Students {
        <<root>>
      }

      class LectureAssignments {
        <<root>>
      }

      Rooms *-- Room : 0..n
      Courses *-- Lecture: 0..n
      Lecture .. Room: 1
      Lecture *-- Schedule: 1
      OneOff <|-- Schedule
      Recurring <|-- Schedule
      Schedule *-- DateAndTime:1
      Students *-- Student: 0..n
      Student *-- DateAndTime:born [1]
      LectureAssignments .. Student: 1
      LectureAssignments *-- Assignment: 0..n
      Assignment .. Lecture:1
```


## Generated API

In oder to be able to work with the metamodel / structure of the language outside of MPS we need to generate an API that
is usable outside of MPS. This API is generated with the [api-gen](https://github.com/modelix/api-gen) plugin from modelix.
The plugin takes an MPS language definition and exports it into a Java API that wraps around the metamodel independent [model-api](https://github.com/modelix/model-api)
from modelix. The wrapper is than metamodel specific and give you easy access to the instance of your language.

The generator is configured in the `University.Schedule.api` with an [`ApiDefinition`](http://127.0.0.1:63320/node?ref=r%3A86be3a58-5d45-4d2b-aadb-835f83eeb67b%28University.Schedule.api.gen%29%2F8546592165266022808).
When the model that contains the `ApiDefinition` is rebuild it will generate Java classes for the languages that are
referenced withing it.

You can find the generated code within the repository at [`mps/solutions/University.Schedule.api/source_gen`](mps/solutions/University.Schedule.api/source_gen)
The generated Java code is then **not** compiled within MPS but using a separate gradle build [here](mps/solutions/University.Schedule.api/build.gradle.kts).
The generated code has no dependency into MPS at all but depends on `org.modelix.mps.api-gen:runtime` which contains a couple
of base classes to make the code generator implementation less convoluted and easier to maintain.

For instance accessing the `rooms` on the `Rooms` root node and then reading the name of the `Room` using the modelix
model api directly would look like this:

```kotlin
val iNode : INode = ...
iNode.getChildren("rooms").forEach { it.getPropertyValue("name") }
```

You can see that this is quite error-prone because every access to a child role or property is just a string. If you have a
typo or the structure of the language changes you will only notice it at runtime.

Using the generated API the same code looks like this:

```kotlin
val iNode : INode = ...
val rooms = MPSLanguageRegistry.getInstance<Rooms>(iNode)
rooms.children.rooms.forEach { it.properties.name }
```

The code generator has exported the language definition, and we can use to write type safe code that works
with the models. For properties and children we now have attributes in the generated classes and if somebody renames a
property or child-role the compiler will tell us. Of course the `MPSLanguageRegistry.getInstance<Rooms>` would throw an exception if our `iNode` instance
isn't a `Rooms` instance.

The generate class for a the [`Room`]() concept:

```
concept Room extends BaseConcept
             implements INamedConcept

instance can be root: false
alias: <no alias>
short description: <no short description>

properties:
maxPlaces : integer
hasRemoteEquipment : boolean

children:
<< ... >>

references:
<< ... >>
```


<details>
<summary>
Will look like this:
</summary>

```java
package University.Schedule.structure;

/*Generated by MPS */

import jetbrains.mps.lang.core.structure.BaseConcept;
import jetbrains.mps.lang.core.structure.INamedConcept;
import org.modelix.mps.apigen.runtime.INodeHolder;
import org.jetbrains.annotations.NotNull;
import org.modelix.model.api.INode;
import org.jetbrains.annotations.Nullable;

/**
* Generated for http://127.0.0.1:63320/node?ref=r%3Adfa26643-4653-44bc-9dfe-5a6581bcd381%28University.Schedule.structure%29%2F4128798754188010580
  */
  public class Room extends BaseConcept implements INamedConcept {

public class Properties extends BaseConcept.Properties implements INodeHolder, INamedConcept.Properties {

    @NotNull
    @Override
    public INode getINode() {
      return Room.this.getINode();
    }
    @Nullable
    public Integer getMaxPlaces() {
      String propertyValue = getINode().getPropertyValue("maxPlaces");
      if (propertyValue != null && !(propertyValue.isEmpty())) {
        return Integer.parseInt(propertyValue);
      }
      return null;
    }
    @Nullable
    public Integer setMaxPlaces(Integer value) {
      if (value != null) {
        getINode().setPropertyValue("maxPlaces", Integer.toString(value));
      } else {
        getINode().setPropertyValue("maxPlaces", null);
      }
      return value;
    }
    @Nullable
    public Boolean getHasRemoteEquipment() {
      String propertyValue = getINode().getPropertyValue("hasRemoteEquipment");
      if (propertyValue != null && !(propertyValue.isEmpty())) {
        return Boolean.parseBoolean(propertyValue);
      }
      return null;
    }
    @Nullable
    public Boolean setHasRemoteEquipment(@Nullable Boolean value) {
      if (value != null) {
        getINode().setPropertyValue("hasRemoteEquipment", Boolean.toString(value));
      } else {
        getINode().setPropertyValue("hasRemoteEquipment", null);
      }
      return value;
    }
}
public class Children extends BaseConcept.Children implements INodeHolder, INamedConcept.Children {

    @NotNull
    @Override
    public INode getINode() {
      return Room.this.getINode();
    }
}
public class References extends BaseConcept.References implements INodeHolder, INamedConcept.References {

    @NotNull
    @Override
    public INode getINode() {
      return Room.this.getINode();
    }


}

private final Properties properties;
private final Children children;
private final References references;
public Room(INode node) {
super(node);
this.properties = new Properties();
this.children = new Children();
this.references = new References();
}
public Properties getProperties() {
return this.properties;
}
public Children getChildren() {
return this.children;
}
public References getReferences() {
return this.references;
}
}

```

</details>

### Limitations

At the moment it's not possible to regenerate the API as part of the CI/gradle build, that's why the generated sources
are checked into the repository.  **This limitation is specific this example** and is somehow caused by the MPS build failing
to load the right languages during the build. Other projects are successfully using the `api-gen` code generator within
their CI/gradle build.  The limitation will get fixed in the future but for now the generated Java
code is checked into the repository.