package io.spring.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import graphql.execution.DataFetcherResult;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.report.Report;
import io.spring.core.report.ReportRepository;
import io.spring.core.user.User;
import io.spring.graphql.DgsConstants.MUTATION;
import io.spring.graphql.exception.AuthenticationException;
import io.spring.graphql.types.ReportPayload;
import lombok.AllArgsConstructor;

@DgsComponent
@AllArgsConstructor
public class ReportMutation {

  private ReportRepository reportRepository;
  private ArticleRepository articleRepository;
  private CommentRepository commentRepository;

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.ReportArticle)
  public DataFetcherResult<ReportPayload> reportArticle(
      @InputArgument("slug") String slug, 
      @InputArgument("reason") Report.ReportReason reason) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    Article article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Report report = new Report(user.getId(), Report.ContentType.ARTICLE, article.getId(), reason);
    reportRepository.save(report);
    
    return DataFetcherResult.<ReportPayload>newResult()
        .localContext(report)
        .data(ReportPayload.newBuilder().build())
        .build();
  }

  @DgsData(parentType = MUTATION.TYPE_NAME, field = MUTATION.ReportComment)
  public DataFetcherResult<ReportPayload> reportComment(
      @InputArgument("slug") String slug,
      @InputArgument("commentId") String commentId, 
      @InputArgument("reason") Report.ReportReason reason) {
    User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
    Article article = articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    Comment comment = commentRepository.findById(article.getId(), commentId).orElseThrow(ResourceNotFoundException::new);
    Report report = new Report(user.getId(), Report.ContentType.COMMENT, comment.getId(), reason);
    reportRepository.save(report);
    
    return DataFetcherResult.<ReportPayload>newResult()
        .localContext(report)
        .data(ReportPayload.newBuilder().build())
        .build();
  }
}
