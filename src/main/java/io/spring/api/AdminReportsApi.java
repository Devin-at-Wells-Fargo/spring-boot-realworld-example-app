package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.report.Report;
import io.spring.core.report.ReportRepository;
import io.spring.core.user.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin/reports")
@AllArgsConstructor
public class AdminReportsApi {
  private ReportRepository reportRepository;

  @GetMapping
  public ResponseEntity<?> getAllReports(@AuthenticationPrincipal User user) {
    List<Report> reports = reportRepository.findAll();
    return ResponseEntity.ok(reportsResponse(reports));
  }

  @GetMapping(params = "status")
  public ResponseEntity<?> getReportsByStatus(
      @RequestParam("status") Report.ReportStatus status, @AuthenticationPrincipal User user) {
    List<Report> reports = reportRepository.findByStatus(status);
    return ResponseEntity.ok(reportsResponse(reports));
  }

  @PutMapping(path = "/{id}/status")
  public ResponseEntity<?> updateReportStatus(
      @PathVariable("id") String reportId,
      @RequestParam("status") Report.ReportStatus status,
      @AuthenticationPrincipal User user) {
    Report report = reportRepository.findById(reportId).orElseThrow(ResourceNotFoundException::new);
    reportRepository.updateStatus(reportId, status);
    return ResponseEntity.ok(reportResponse(report));
  }

  private Map<String, Object> reportsResponse(List<Report> reports) {
    return new HashMap<String, Object>() {
      {
        put("reports", reports);
      }
    };
  }

  private Map<String, Object> reportResponse(Report report) {
    return new HashMap<String, Object>() {
      {
        put("report", report);
      }
    };
  }
}
