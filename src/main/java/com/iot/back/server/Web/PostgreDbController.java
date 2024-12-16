package com.iot.back.server.Web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.back.server.Service.PostgreDbService;

@RestController
public class PostgreDbController {
    @Autowired
    private PostgreDbService postgreDbService;

    @GetMapping("/getDBTableList")
    public List<?> getDBTableList() {
        return this.postgreDbService.selectDbTableList();
    }

    @GetMapping("/getDBTableColumns")
    public List<?> getDBTableColumns(@RequestParam String table) {
        Map<String, Object> vo = new HashMap<>();
        if (table != null) {
            String[] temp = table.split("\\.");
            if (temp.length > 1) {
                table = temp[1];
            }
        }
        vo.put("table", table);

        return this.postgreDbService.selectDbColumnList(vo);
    }

    @GetMapping("/getDBTableData")
    public List<?> getDBTableData(@RequestParam String table) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("table_name", table);

        return this.postgreDbService.selectTableData(vo);
    }

    @GetMapping("/insertIoTData")
    public int insertIoTDaa(@RequestParam String tableName,
                                @RequestParam String values) {
        if (values == null) {
            return -1;
        }

        String[] paramVal = values.split("\\^");
        
        return this.postgreDbService.insertData(tableName, Arrays.asList(paramVal));
    }
}
