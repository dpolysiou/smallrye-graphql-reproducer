package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.graphql.client.GraphQLClientException;
import io.smallrye.graphql.client.GraphQLError;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class GraphQLResourceTest {

  @Inject
  Client client;

  private final Pagination invalidParams = new Pagination(-1, -1);

  @Test
  void page() {
    GraphQLClientException exception =
        assertThrows(GraphQLClientException.class,
            () -> client.queryWithBuggyValidation(invalidParams));
    List<GraphQLError> errors = exception.getErrors();
    assertEquals(1, errors.size());
    assertEquals("System error", errors.get(0).getMessage());
  }

  @Test
  void pageWithWorkaround() {
    GraphQLClientException exception =
        assertThrows(GraphQLClientException.class, () -> client.queryWithWorkaround(invalidParams));
    List<GraphQLError> errors = exception.getErrors();
    assertEquals(2, errors.size());
    assertTrue(errors.get(0).getMessage().startsWith("validation failed"));
    assertTrue(errors.get(1).getMessage().startsWith("validation failed"));
  }

  @Test
  void pageWithValidParams() {
    Pagination validParams = new Pagination(1, 0);
    List<Foo> response = client.queryWithBuggyValidation(validParams).response();
    assertEquals(1, response.size());

    assertEquals(response, client.queryWithWorkaround(validParams).response());
  }

}