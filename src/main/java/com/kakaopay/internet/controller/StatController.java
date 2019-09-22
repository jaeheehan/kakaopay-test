package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.*;
import com.kakaopay.internet.service.StatService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class StatController {

    private StatService statService;

    @Autowired
    public StatController(StatService statService){
        this.statService= statService;
    }

    @ApiOperation(value = "기기목록")
    @GetMapping(path = "/devices")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
                    required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<DeviceList> getDevices(){
        return ResponseEntity.ok(statService.getDeviceList());
    }

    @ApiOperation(value = "년도별 최대 접속기기")
    @GetMapping(path = "/topDeviceEachYear")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<InternetUseRowList> getTopDeviceEachYear() {
        return ResponseEntity.ok(statService.getTopDeviceEachYear());
    }

    @ApiOperation(value = "캐쉬 제거")
    @GetMapping(path = "/refreshCache")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity refreshCache() throws Exception{
        statService.cacheEvict();
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "특정 년도 최대이용 접속기기")
    @GetMapping(path = "/internetUseTopByYear")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<StatResult> internetUseTopByYear(@RequestParam(name = "year") int year){
        return ResponseEntity.ok(statService.internetUseTopByYear(year));
    }

    @ApiOperation(value = "특정 기기 최대이용 년도")
    @GetMapping(path = "/internetUseYearTopByDevice")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<StatResult> internetUseYearTopByDevice(@RequestParam(name = "device_id") String device_id){
        return ResponseEntity.ok(statService.internetUseYearTopByDevice(device_id));
    }


    @ApiOperation(value = "특정 기기 다음 년도 예측")
    @GetMapping(path = "/forecastUseByYear")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<InternetUseRow> forecastUseByYear(@RequestParam(name = "device_id") String device_id){
        return ResponseEntity.ok(statService.forecastUseByYear(device_id));
    }

}
