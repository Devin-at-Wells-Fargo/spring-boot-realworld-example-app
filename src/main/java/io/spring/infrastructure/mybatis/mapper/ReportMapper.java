package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.report.Report;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReportMapper {
  void insert(@Param("report") Report report);
  
  Report findById(@Param("id") String id);
  
  List<Report> findAll();
  
  List<Report> findByStatus(@Param("status") String status);
  
  void updateStatus(@Param("id") String id, @Param("status") String status);
}
