package com.iot.back.server.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.back.server.DAO.PostgreDbDAO;

@Service
public class PostgreDbService {
    @Autowired
    private PostgreDbDAO postgreDbDAO;

    public List<?> selectDbTableList() {
        return this.postgreDbDAO.selectDbTableList();
    }

    public List<?> selectDbColumnList(Map<String, Object> map) {
        return this.postgreDbDAO.selectDbColumnList(map);
    }

    public List<?> selectTableData(Map<String, Object> map) {
        return this.postgreDbDAO.selectTableData(map);
    }

    public int insertData(String tableName, List<String> values) {
        return this.postgreDbDAO.insertData(tableName, values);
    }
}
