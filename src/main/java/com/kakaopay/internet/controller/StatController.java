package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.domain.DeviceList;
import com.kakaopay.internet.domain.InternetUseRowList;
import com.kakaopay.internet.domain.StatResult;
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

    @ApiOperation(value = "연도별기기")
    @GetMapping(path = "/topDeviceEachYear")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<InternetUseRowList> getTopDeviceEachYear() {
        return ResponseEntity.ok(statService.getTopDeviceEachYear());
    }


    @ApiOperation(value = "연도최대이용률기기")
    @GetMapping(path = "/internetUseTopByYear")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<StatResult> internetUseTopByYear(@RequestParam(name = "year") int year){
        return ResponseEntity.ok(statService.internetUseTopByYear(year));
    }

    @ApiOperation(value = "기기최대이용률년도")
    @GetMapping(path = "/internetUseYearTopByDevice")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity<StatResult> internetUseYearTopByDevice(@RequestParam(name = "device_id") String device_id){
        Device device = new Device();
        device.setDevice_id(device_id);
        return ResponseEntity.ok(statService.internetUseYearTopByDevice(device));
    }

}
