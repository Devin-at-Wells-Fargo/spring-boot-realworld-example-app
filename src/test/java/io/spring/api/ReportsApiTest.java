package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.JacksonCustomizations;
import io.spring.api.security.WebSecurityConfig;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.report.ReportRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReportsApi.class)
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class ReportsApiTest extends TestWithCurrentUser {

  @MockBean private ArticleRepository articleRepository;
  @MockBean private CommentRepository commentRepository;
  @MockBean private ReportRepository reportRepository;

  private Article article;
  private Comment comment;
  @Autowired private MockMvc mvc;

  @BeforeEach
  public void setUp() throws Exception {
    RestAssuredMockMvc.mockMvc(mvc);
    super.setUp();
    article = new Article("title", "desc", "body", Arrays.asList("test", "java"), user.getId());
    when(articleRepository.findBySlug(eq(article.getSlug()))).thenReturn(Optional.of(article));
    comment = new Comment("comment", user.getId(), article.getId());
    when(commentRepository.findById(eq(article.getId()), eq(comment.getId())))
        .thenReturn(Optional.of(comment));
  }

  @Test
  public void should_report_article_success() throws Exception {
    Map<String, Object> param =
        new HashMap<String, Object>() {
          {
            put(
                "report",
                new HashMap<String, Object>() {
                  {
                    put("reason", "SPAM");
                  }
                });
          }
        };

    given()
        .contentType("application/json")
        .header("Authorization", "Token " + token)
        .body(param)
        .when()
        .post("/reports/articles/{slug}", article.getSlug())
        .then()
        .statusCode(201)
        .body("report.reason", equalTo("SPAM"));
  }

  @Test
  public void should_report_comment_success() throws Exception {
    Map<String, Object> param =
        new HashMap<String, Object>() {
          {
            put(
                "report",
                new HashMap<String, Object>() {
                  {
                    put("reason", "HARASSMENT");
                  }
                });
          }
        };

    given()
        .contentType("application/json")
        .header("Authorization", "Token " + token)
        .body(param)
        .when()
        .post("/reports/articles/{slug}/comments/{commentId}", article.getSlug(), comment.getId())
        .then()
        .statusCode(201)
        .body("report.reason", equalTo("HARASSMENT"));
  }

  @Test
  public void should_get_422_with_null_reason() throws Exception {
    Map<String, Object> param =
        new HashMap<String, Object>() {
          {
            put(
                "report",
                new HashMap<String, Object>() {
                  {
                    put("reason", null);
                  }
                });
          }
        };

    given()
        .contentType("application/json")
        .header("Authorization", "Token " + token)
        .body(param)
        .when()
        .post("/reports/articles/{slug}", article.getSlug())
        .then()
        .statusCode(422)
        .body("errors.reason[0]", equalTo("can't be null"));
  }

  @Test
  public void should_get_404_when_article_not_found() throws Exception {
    when(articleRepository.findBySlug(eq("nonexistent"))).thenReturn(Optional.empty());

    Map<String, Object> param =
        new HashMap<String, Object>() {
          {
            put(
                "report",
                new HashMap<String, Object>() {
                  {
                    put("reason", "SPAM");
                  }
                });
          }
        };

    given()
        .contentType("application/json")
        .header("Authorization", "Token " + token)
        .body(param)
        .when()
        .post("/reports/articles/nonexistent")
        .then()
        .statusCode(404);
  }
}
