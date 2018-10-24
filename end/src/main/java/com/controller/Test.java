package com.controller;

import com.dao.B_LEASEINFOHIS_BRIEF_DAO;
import com.dao.B_STATIONINFO_BRIEF_DAO;
import com.domain.B_LEASEINFOHIS_BRIEF;
import com.domain.B_STATIONINFO_BRIEF;
import com.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymy on 2018/10/5.
 */
@RestController
public class Test {

    @Autowired
    B_LEASEINFOHIS_BRIEF_DAO b_leaseinfohis_brief_dao;

    @RequestMapping(value = "/index",method= RequestMethod.GET)
    public List<B_LEASEINFOHIS_BRIEF> get(){
        return b_leaseinfohis_brief_dao.findByIDAndTime("131643936","2014-04-02","2014-04-02");
    }


}
