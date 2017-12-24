package com.cstu.controller.api;

import com.cstu.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api", produces = {"application/json; charset=windows-1251"})
public class ParserController {

    private final ParserService parserService;

    @Autowired
    public ParserController(ParserService parserService) {
        this.parserService = parserService;
    }


    @RequestMapping(value = "/parser/{group}/{sdate}/{edate}")
    public String getSchedule(@PathVariable String group,
                              @PathVariable String sdate,
                              @PathVariable String edate) {
        return parserService
                        .getSchedule(group, sdate, edate);
    }

    @RequestMapping(method = GET)
    public String api() {
        return "It`s API";
    }
}
