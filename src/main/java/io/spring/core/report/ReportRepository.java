package io.spring.core.report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {
  void save(Report report);

  Optional<Report> findById(String id);

  List<Report> findAll();

  List<Report> findByStatus(Report.ReportStatus status);

  void updateStatus(String id, Report.ReportStatus status);
}
