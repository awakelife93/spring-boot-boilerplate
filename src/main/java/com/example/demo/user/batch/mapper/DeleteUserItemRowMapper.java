package com.example.demo.user.batch.mapper;

import com.example.demo.utils.ConvertUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class DeleteUserItemRowMapper implements RowMapper<DeleteUserItem> {

  @Override
  public DeleteUserItem mapRow(ResultSet resultSet, int rowNumber)
    throws SQLException {
    DeleteUserItem user = DeleteUserItem.of(
      resultSet.getLong("user_id"),
      resultSet.getString("email"),
      resultSet.getString("name"),
      resultSet.getString("role"),
      ConvertUtils.convertStringToLocalDateTimeFormat(
        resultSet.getString("deleted_dt"),
        "yyyy-MM-dd HH:mm:ss"
      )
    );

    return user;
  }
}
