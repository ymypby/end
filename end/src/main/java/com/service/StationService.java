package com.service;

import com.dao.B_STATIONINFO_BRIEF_DAO;
import com.domain.B_STATIONINFO_BRIEF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ymy on 2018/10/20.
 */
@Service
public class StationService {
    @Autowired
    B_STATIONINFO_BRIEF_DAO b_stationinfo_brief_dao;

    public List<B_STATIONINFO_BRIEF> get(){
        return  b_stationinfo_brief_dao.get();
    }
}
