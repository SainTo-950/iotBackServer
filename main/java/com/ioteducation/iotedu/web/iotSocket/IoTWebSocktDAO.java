package com.ioteducation.iotedu.web.iotSocket;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IoTWebSocktDAO {
    int insertIoData(Map<String, Object> vo);
}
