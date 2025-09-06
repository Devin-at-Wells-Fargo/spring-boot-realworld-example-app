package io.spring.api;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.report.Report;
import io.spring.core.report.ReportRepository;
import io.spring.core.user.User;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reports")
@AllArgsConstructor
public class ReportsApi {
  private ReportRepository reportRepository;
  private ArticleRepository articleRepository;
  private CommentRepository commentRepository;

  @PostMapping(path = "/articles/{slug}")
  public ResponseEntity<?> reportArticle(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody ReportArticleParam reportParam) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Report report = new Report(user.getId(), Report.ContentType.ARTICLE, article.getId(), reportParam.getReason());
    reportRepository.save(report);
    return ResponseEntity.status(201).body(reportResponse(report));
  }

  @PostMapping(path = "/articles/{slug}/comments/{commentId}")
  public ResponseEntity<?> reportComment(
      @PathVariable("slug") String slug,
      @PathVariable("commentId") String commentId,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody ReportCommentParam reportParam) {
    Article article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Comment comment = commentRepository.findById(article.getId(), commentId).orElseThrow(ResourceNotFoundException::new);
    Report report = new Report(user.getId(), Report.ContentType.COMMENT, comment.getId(), reportParam.getReason());
    reportRepository.save(report);
    return ResponseEntity.status(201).body(reportResponse(report));
  }

  private Map<String, Object> reportResponse(Report report) {
    return new HashMap<String, Object>() {
      {
        put("report", new HashMap<String, Object>() {
          {
            put("id", report.getId());
            put("reason", report.getReason());
            put("status", report.getStatus());
            put("createdAt", report.getCreatedAt());
          }
        });
      }
    };
  }
}

@Getter
@NoArgsConstructor
@JsonRootName("report")
class ReportArticleParam {
  @NotNull(message = "can't be null")
  private Report.ReportReason reason;
}

@Getter
@NoArgsConstructor
@JsonRootName("report")
class ReportCommentParam {
  @NotNull(message = "can't be null")
  private Report.ReportReason reason;
}
