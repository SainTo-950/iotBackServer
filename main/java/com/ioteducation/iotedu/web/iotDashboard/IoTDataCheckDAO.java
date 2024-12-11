package com.ioteducation.iotedu.web.iotDashboard;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IoTDataCheckDAO {
    int insertGatheringData(Map<String, Object> vo);

    Map<String, Object> getLastData(Map<String, Object> vo);
}
