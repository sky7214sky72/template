package com.infrastructure.config.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

@Configuration
public class P6spySqlFormatter implements MessageFormattingStrategy {

  @PostConstruct
  public void setLogMessageFormat() {
    com.p6spy.engine.spy.P6SpyOptions.getActiveInstance()
        .setLogMessageFormat(this.getClass().getName());
  }

  @Override
  public String formatMessage(int connectionId, String now, long elapsed, String category,
      String prepared, String sql, String url) {
    sql = formatSql(category, sql);
    // 실행 시간(elapsed)을 함께 출력하여 슬로우 쿼리 탐지에 활용
    return String.format("[%s] | %d ms | %s", category, elapsed, sql);
  }

  private String formatSql(String category, String sql) {
    if (sql == null || sql.trim().isEmpty()) {
      return sql;
    }

    // Statement나 PreparedStatement일 경우 포매팅 (PostgreSQL 방언 적용)
    if (Category.STATEMENT.getName().equals(category)) {
      String tmpsql = sql.trim().toLowerCase();
      if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith(
          "comment")) {
        sql = FormatStyle.DDL.getFormatter().format(sql);
      } else {
        sql = FormatStyle.BASIC.getFormatter().format(sql);
      }
    }
    return sql;
  }
}
