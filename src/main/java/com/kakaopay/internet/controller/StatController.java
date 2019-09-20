package com.kakaopay.internet.controller;

import com.kakaopay.internet.domain.Device;
import com.kakaopay.internet.service.StatService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity getDevices(){
        return ResponseEntity.ok(statService.getDeviceList());
    }

    @ApiOperation(value = "연도별기기")
    @GetMapping(path = "/topDeviceEachYear")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Bearer token",
            required = true, dataType = "string", paramType = "header") })
    public ResponseEntity getTopDeviceEachYear() {
        return ResponseEntity.ok(statService.getTopDeviceEachYear());
    }

}
