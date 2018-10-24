package com.controller;

import com.domain.B_LEASEINFOHIS_BRIEF;
import com.domain.B_STATIONINFO_BRIEF;
import com.other.Curve;
import com.service.GanntService;
import com.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Created by ymy on 2018/10/12.
 */
@RestController
public class GanttController {
    @Autowired
    GanntService ganntService;

    @Autowired
    StationService stationService;

//    @RequestMapping(value = "/Gannt",method =  RequestMethod.POST)
//    public List<B_LEASEINFOHIS_BRIEF> getInfo(@RequestParam(value="cardNo") String cardNo,@RequestParam(value = "minDay") String minDay, @RequestParam(value = "maxDay") String maxDay){
//        return ganntService.getInfoByIdAndTime(cardNo,minDay,maxDay);
//    }

    @RequestMapping( "/origion_html")
    public List<Object> getInfo(@RequestBody  Map<String, String> args){
        List<B_LEASEINFOHIS_BRIEF> list = ganntService.getInfoByIdAndTime(args.get("cardNo"),args.get("minDay"),args.get("maxDay"));
        List<String> listStations = ganntService.getMustStations(list);
        Map<B_LEASEINFOHIS_BRIEF,Integer> map = ganntService.getCountMap(list);
        List<Curve> listCurve = ganntService.getCurves(map);
        List<Object> result =  ganntService.getGantts(list,map);
        result.add(listCurve);
        result.add(listStations);
        return  result;
    }

//    @RequestMapping( "/merge_gantt_html")
//    public List<Object> getMergeInfo(@RequestBody  Map<String, String> args){
//        List<B_LEASEINFOHIS_BRIEF> list = ganntService.getInfoByIdAndTime(args.get("cardNo"),args.get("minDay"),args.get("maxDay"));
//        List<String> listStations = ganntService.getMustStations(ganntService.mergeTrajectory(list));
//        Map<B_LEASEINFOHIS_BRIEF,Integer> map = ganntService.getCountMap(list);
//        List<Curve> listCurve = ganntService.getCurves(map);
//        List<Object> result =  ganntService.getGantts(list,map);
//        result.add(listCurve);
//        result.add(listStations);
//        return  result;
//    }

    @RequestMapping("/merge_html")
    public List<Object> mergetrajectory(@RequestBody  Map<String, String> args){
        List<B_LEASEINFOHIS_BRIEF> list = ganntService.mergeTrajectory(ganntService.getInfoByIdAndTime(args.get("cardNo"),args.get("minDay"),args.get("maxDay")));
        List<String> listStations = ganntService.getMustStations(list);
        Map<B_LEASEINFOHIS_BRIEF,Integer> map = ganntService.getCountMap(list);
        List<Curve> listCurve = ganntService.getCurves(map);
        List<Object> result =  ganntService.getGantts(list,map);
        result.add(listCurve);
        result.add(listStations);
        return  result;
    }
    @RequestMapping("/station_html")
    public List<B_STATIONINFO_BRIEF> get(){
        return stationService.get();
    }

}
