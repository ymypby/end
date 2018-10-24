package com.service;

import com.dao.B_LEASEINFOHIS_BRIEF_DAO;
import com.dao.B_STATIONINFO_BRIEF_DAO;
import com.domain.B_LEASEINFOHIS_BRIEF;
import com.domain.B_STATIONINFO_BRIEF;
import com.other.MyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymy on 2018/10/6.
 */
@Service
public class MapService {
    B_LEASEINFOHIS_BRIEF_DAO b_leaseinfohis_brief_dao;
    B_STATIONINFO_BRIEF_DAO b_stationinfo_brief_dao;
    private List<B_STATIONINFO_BRIEF> stationList;
    private List<String> stationId;
    @Autowired
    public MapService( B_LEASEINFOHIS_BRIEF_DAO b_leaseinfohis_brief_dao,B_STATIONINFO_BRIEF_DAO b_stationinfo_brief_dao){
        this.b_leaseinfohis_brief_dao = b_leaseinfohis_brief_dao;
        this.b_stationinfo_brief_dao = b_stationinfo_brief_dao;
        setStationList();
    }
    //设置 stationId 和 stationList
    public synchronized  void setStationList(){
        stationList = b_stationinfo_brief_dao.get();
        List<String> list = new ArrayList<String>();
        for(int i =0;i<stationList.size();i++){
            list.add(stationList.get(i).getSTATIONID());
        }
        stationId = list;
    }

    public MyMap getMyMap(){
        try{
            return new MyMap(stationId);
        }catch (NullPointerException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public List<Integer> getDegree(String id){
        MyMap myMap = getMyMap();
        List<B_LEASEINFOHIS_BRIEF> b_leaseinfohis_briefs = b_leaseinfohis_brief_dao.findByID(id);
        myMap.generateEdge(b_leaseinfohis_briefs);
        return myMap.countDegree();
    }


}
