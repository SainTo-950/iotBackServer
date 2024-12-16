package com.iot.back.server.DAO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostgreDbDAO {
    List<?> selectDbTableList();

    List<?> selectDbColumnList(Map<String, Object> map);

    List<?> selectTableData(Map<String, Object> map);

    int insertData(@Param("tableName") String tableName, @Param("values") List<String> values);
}
