package io.spring.infrastructure.report;

import static org.assertj.core.api.Assertions.assertThat;

import io.spring.core.report.Report;
import io.spring.core.report.ReportRepository;
import io.spring.infrastructure.DbTestBase;
import io.spring.infrastructure.repository.MyBatisReportRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(MyBatisReportRepository.class)
public class MyBatisReportRepositoryTest extends DbTestBase {

  @Autowired private ReportRepository reportRepository;

  @Test
  public void should_save_and_fetch_report() {
    Report report = new Report("user1", Report.ContentType.ARTICLE, "article1", Report.ReportReason.SPAM);
    reportRepository.save(report);

    Optional<Report> fetched = reportRepository.findById(report.getId());
    assertThat(fetched).isPresent();
    assertThat(fetched.get().getReporterId()).isEqualTo("user1");
    assertThat(fetched.get().getReason()).isEqualTo(Report.ReportReason.SPAM);
    assertThat(fetched.get().getStatus()).isEqualTo(Report.ReportStatus.PENDING);
  }

  @Test
  public void should_find_reports_by_status() {
    Report report1 = new Report("user1", Report.ContentType.ARTICLE, "article1", Report.ReportReason.SPAM);
    Report report2 = new Report("user2", Report.ContentType.COMMENT, "comment1", Report.ReportReason.HARASSMENT);
    reportRepository.save(report1);
    reportRepository.save(report2);

    List<Report> pendingReports = reportRepository.findByStatus(Report.ReportStatus.PENDING);
    assertThat(pendingReports).hasSize(2);
  }

  @Test
  public void should_update_report_status() {
    Report report = new Report("user1", Report.ContentType.ARTICLE, "article1", Report.ReportReason.SPAM);
    reportRepository.save(report);

    reportRepository.updateStatus(report.getId(), Report.ReportStatus.REVIEWED);

    Optional<Report> updated = reportRepository.findById(report.getId());
    assertThat(updated).isPresent();
    assertThat(updated.get().getStatus()).isEqualTo(Report.ReportStatus.REVIEWED);
  }

  @Test
  public void should_find_all_reports() {
    Report report1 = new Report("user1", Report.ContentType.ARTICLE, "article1", Report.ReportReason.SPAM);
    Report report2 = new Report("user2", Report.ContentType.COMMENT, "comment1", Report.ReportReason.HARASSMENT);
    reportRepository.save(report1);
    reportRepository.save(report2);

    List<Report> allReports = reportRepository.findAll();
    assertThat(allReports).hasSize(2);
  }

  @Test
  public void should_handle_different_content_types() {
    Report articleReport = new Report("user1", Report.ContentType.ARTICLE, "article1", Report.ReportReason.SPAM);
    Report commentReport = new Report("user1", Report.ContentType.COMMENT, "comment1", Report.ReportReason.HARASSMENT);
    
    reportRepository.save(articleReport);
    reportRepository.save(commentReport);

    Optional<Report> fetchedArticleReport = reportRepository.findById(articleReport.getId());
    Optional<Report> fetchedCommentReport = reportRepository.findById(commentReport.getId());
    
    assertThat(fetchedArticleReport.get().getReportedContentType()).isEqualTo(Report.ContentType.ARTICLE);
    assertThat(fetchedCommentReport.get().getReportedContentType()).isEqualTo(Report.ContentType.COMMENT);
  }
}
