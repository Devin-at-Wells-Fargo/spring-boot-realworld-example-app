package io.spring.core.report;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Report {
  private String id;
  private String reporterId;
  private ContentType reportedContentType;
  private String reportedContentId;
  private ReportReason reason;
  private ReportStatus status;
  private DateTime createdAt;
  private DateTime updatedAt;

  public Report(String reporterId, ContentType contentType, String contentId, ReportReason reason) {
    this.id = UUID.randomUUID().toString();
    this.reporterId = reporterId;
    this.reportedContentType = contentType;
    this.reportedContentId = contentId;
    this.reason = reason;
    this.status = ReportStatus.PENDING;
    this.createdAt = new DateTime();
    this.updatedAt = new DateTime();
  }

  public enum ContentType {
    ARTICLE, COMMENT
  }

  public enum ReportReason {
    SPAM, HARASSMENT, INAPPROPRIATE_CONTENT
  }

  public enum ReportStatus {
    PENDING, REVIEWED, DISMISSED
  }
}
