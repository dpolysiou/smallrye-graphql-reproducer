package org.acme;

import jakarta.validation.Valid;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;

@GraphQLApi
public class GraphQLResource {

  @Query
  public Foo getFoo() {
    return new Foo("Alice");
  }

  public List<Foo> page(@Source Foo foo, @Valid Pagination pagination) {
    return List.of(foo);
  }

  public List<Foo> pageWithWorkaround(@Valid Pagination pagination, @Source Foo foo) {
    return List.of(foo);
  }
}
