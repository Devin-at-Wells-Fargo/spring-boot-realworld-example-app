package io.spring.infrastructure.repository;

import io.spring.core.report.Report;
import io.spring.core.report.ReportRepository;
import io.spring.infrastructure.mybatis.mapper.ReportMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyBatisReportRepository implements ReportRepository {
  private ReportMapper reportMapper;

  @Autowired
  public MyBatisReportRepository(ReportMapper reportMapper) {
    this.reportMapper = reportMapper;
  }

  @Override
  public void save(Report report) {
    reportMapper.insert(report);
  }

  @Override
  public Optional<Report> findById(String id) {
    return Optional.ofNullable(reportMapper.findById(id));
  }

  @Override
  public List<Report> findAll() {
    return reportMapper.findAll();
  }

  @Override
  public List<Report> findByStatus(Report.ReportStatus status) {
    return reportMapper.findByStatus(status.name());
  }

  @Override
  public void updateStatus(String id, Report.ReportStatus status) {
    reportMapper.updateStatus(id, status.name());
  }
}
