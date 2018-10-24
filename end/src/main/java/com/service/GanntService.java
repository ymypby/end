package com.service;

import com.dao.B_LEASEINFOHIS_BRIEF_DAO;
import com.dao.B_STATIONINFO_BRIEF_DAO;
import com.domain.B_LEASEINFOHIS_BRIEF;
import com.other.Curve;
import com.other.Gantt;
import com.other.Mark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ymy on 2018/10/12.
 */
@Service
public class GanntService {

    @Autowired
    B_LEASEINFOHIS_BRIEF_DAO b_leaseinfohis_brief_dao;


    public List<B_LEASEINFOHIS_BRIEF> getInfoByIdAndTime(String cardNo ,String minDay, String maxDay){

        return b_leaseinfohis_brief_dao.findByIDAndTime(cardNo,minDay,maxDay);
    }

    /**
     *对list中的轨迹进行计数，leaseStation与returnStation相等即为轨迹一样
     * @param list:按用户id，minDay和maxDay为条件从数据库中取得list
     * @return 固定轨迹的总数量
     */
    public Map<B_LEASEINFOHIS_BRIEF,Integer> getCountMap(List<B_LEASEINFOHIS_BRIEF> list){
        Map<B_LEASEINFOHIS_BRIEF,Integer>  map = new HashMap<B_LEASEINFOHIS_BRIEF,Integer>();
        B_LEASEINFOHIS_BRIEF sample;
        List<Map.Entry<B_LEASEINFOHIS_BRIEF,Integer>> list1;
        int i=0;
        Iterator<B_LEASEINFOHIS_BRIEF> iterator  = list.iterator();
        while(iterator.hasNext()){
            sample = iterator.next();
            if(!map.containsKey(sample)){
                map.put(sample,1);
            }else{
                map.put(sample,map.get(sample)+1);
            }
        }
        return map;
    }

    /**
     * 1.消除不合理数据（用户不可能在同一时间骑一辆车） 2. 合并轨迹（前一段轨迹的returnstaion与下一段轨迹的leasestation相等，且时间小于5min）
     * @param list
     * @return
     */
    public List<B_LEASEINFOHIS_BRIEF> mergeTrajectory(List<B_LEASEINFOHIS_BRIEF> list){
        B_LEASEINFOHIS_BRIEF sample ;
        B_LEASEINFOHIS_BRIEF sample1;
        Map<String,List<B_LEASEINFOHIS_BRIEF>> map  =  new HashMap<>();
        List<B_LEASEINFOHIS_BRIEF> result = new LinkedList<>();
        Iterator<B_LEASEINFOHIS_BRIEF> iterator = list.iterator();
        while(iterator.hasNext()){
            sample = iterator.next();
            String key = sample.getLEASEDAY();
            if(!map.containsKey(key)){
                map.put(key,new LinkedList<B_LEASEINFOHIS_BRIEF>());
            }
            map.get(key).add(sample);
        }
        Set<String> set = map.keySet();
        Iterator<String> setIterator = set.iterator();
        while(setIterator.hasNext()){
            Collections.sort(map.get(setIterator.next()), new Comparator<B_LEASEINFOHIS_BRIEF>() {
                @Override
                public int compare(B_LEASEINFOHIS_BRIEF o1, B_LEASEINFOHIS_BRIEF o2) {
                    return o1.getLEASETIME().compareTo(o2.getLEASETIME());
                }
            });
        }

        setIterator = set.iterator();
        while(setIterator.hasNext()){
            List<B_LEASEINFOHIS_BRIEF> list1 = map.get(setIterator.next());
            for(int i=0;i<list1.size()-1;){
                if(list1.get(i).getRETURNTIME().compareTo(list1.get(i+1).getLEASETIME()) > 0 ){
                    list1.remove(i+1);
                }else{
                    i++;
                }
            }
        }

        setIterator = set.iterator();
        while(setIterator.hasNext()){
            List<B_LEASEINFOHIS_BRIEF> list1 = map.get(setIterator.next());
            for(int i=0;i<list1.size()-1;){
                sample = list1.get(i);
                sample1 = list1.get(i+1);
                if(sample.getRETURNSTATION().equals(sample1.getLEASESTATION())){
                    String[] returnTime = (sample.getRETURNTIME().split(" "))[1].split(":");
                    String[] leaseTime = (sample1.getRETURNTIME().split(" "))[1].split(":");
                    int returnSeconds = Integer.parseInt(returnTime[0])*3600+Integer.parseInt(returnTime[1])*60+Integer.parseInt(returnTime[2]);
                    int leaseSeconds = Integer.parseInt(leaseTime[0])*3600+Integer.parseInt(leaseTime[1])*60+Integer.parseInt(leaseTime[2]);
                    if((leaseSeconds - returnSeconds) < 300){
                        sample.setRETURNHOUR(sample1.getRETURNHOUR());
                        sample.setRETURNDAY(sample1.getRETURNDAY());
                        sample.setRETURNSTATION(sample1.getRETURNSTATION());
                        sample.setRETURNTIME(sample1.getRETURNTIME());
                        list1.remove(i+1);
                    }else{
                        i = i+1;
                    }
                }else{
                    i=i+1;
                }
            }
        }

        setIterator = set.iterator();
        while(setIterator.hasNext()){
            result.addAll(map.get(setIterator.next()));
        }
        return result;
    }
    /**
     * 返回供带箭头弧线所用的一系列值
     * @param map getCountMap返回值
     * @return
     */
    public List<Curve> getCurves(Map<B_LEASEINFOHIS_BRIEF,Integer> map){
        List<Curve> list = new ArrayList<>();
        List<Map.Entry<B_LEASEINFOHIS_BRIEF,Integer>> entryList = new ArrayList<>(map.entrySet());
        for(Map.Entry<B_LEASEINFOHIS_BRIEF,Integer> entry:entryList){
            list.add(new Curve(entry.getKey().getLEASESTATION(),entry.getKey().getRETURNSTATION(),entry.getValue()));
        }
        return list;
    }

    /**
     *为每一种轨迹赋予特定的状态值。
     * @return List<Object> 包含两个数组1.{"endDate": "08:11:21","startDate": "07:22:43","taskName": "2014-04-02","status": "H"} 2.{"A": 1,"B": 1,"C": 1,"D": 1,"E": 1,"F": 1,"G": 1,"H": 1}
     */
    public List<Object> getGantts(List<B_LEASEINFOHIS_BRIEF> list, Map<B_LEASEINFOHIS_BRIEF,Integer> map){
        Map<String,List<B_LEASEINFOHIS_BRIEF>> stringListMap = new HashMap<String,List<B_LEASEINFOHIS_BRIEF>>();
        B_LEASEINFOHIS_BRIEF sample;
        B_LEASEINFOHIS_BRIEF sample1;
        List<Mark> marks = new LinkedList<>();
        Mark mark;
        List<Gantt> gantts = new ArrayList<>();
        Map<B_LEASEINFOHIS_BRIEF,Character> mapping = new HashMap<>();
        Map<Character,Integer> countMap = new HashMap<>();
        List<Object> result = new ArrayList<>();
        List<Map.Entry<B_LEASEINFOHIS_BRIEF,Integer>> list1;
        int i=0;
        char[] status = {'A','B','C','D','E','F','G','H'};
        list1 = new ArrayList<>(map.entrySet());
        Collections.sort(list1, new Comparator<Map.Entry<B_LEASEINFOHIS_BRIEF, Integer>>() {
            @Override
            public int compare(Map.Entry<B_LEASEINFOHIS_BRIEF, Integer> o1, Map.Entry<B_LEASEINFOHIS_BRIEF, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        //给每一个矩形赋予颜色标签
        for(Map.Entry<B_LEASEINFOHIS_BRIEF,Integer> entry:list1){
            if(i > 7){
                mapping.put(entry.getKey(),status[7]);
            }else {
                mapping.put(entry.getKey(),status[i]);
                countMap.put(status[i],entry.getValue());
                i = i+1;
            }
        }


        Iterator<B_LEASEINFOHIS_BRIEF> iterator = list.iterator();
        while(iterator.hasNext()){
            sample = iterator.next();
            if(!stringListMap.containsKey(sample.getLEASEDAY())){
                stringListMap.put(sample.getLEASEDAY(),new LinkedList<B_LEASEINFOHIS_BRIEF>());
            }
            stringListMap.get(sample.getLEASEDAY()).add(sample);
        }

        Set<String> set = stringListMap.keySet();
        Iterator<String> setIterator = set.iterator();
        while(setIterator.hasNext()){
            Collections.sort(stringListMap.get(setIterator.next()), new Comparator<B_LEASEINFOHIS_BRIEF>() {
                @Override
                public int compare(B_LEASEINFOHIS_BRIEF o1, B_LEASEINFOHIS_BRIEF o2) {
                    return o1.getLEASETIME().compareTo(o2.getLEASETIME());
                }
            });
        }

        setIterator = set.iterator();
        while(setIterator.hasNext()){
            List<B_LEASEINFOHIS_BRIEF> list2 = stringListMap.get(setIterator.next());
            for(i=0;i<list2.size()-1;i++){
                sample = list2.get(i);
                sample1 = list2.get(i+1);
              if(sample.getRETURNSTATION().equals(sample1.getLEASESTATION())){
                  mark =  new Mark(1,sample1.getRETURNTIME().split(" ")[1],sample1.getLEASETIME().split(" ")[1],sample1.getLEASEDAY());
                  marks.add(mark);
              }
              if(sample.getBIKEID().equals(sample1.getBIKEID())){
                  mark =  new Mark(0,sample1.getRETURNTIME().split(" ")[1],sample1.getLEASETIME().split(" ")[1],sample1.getLEASEDAY());
                  marks.add(mark);
              }
            }
        }

        iterator = list.iterator();

        //判定是否需要蓝色矩形或蓝色矩形
        while(iterator.hasNext()){
            sample = iterator.next();
            Gantt s = new Gantt();
            s.endDate = sample.getRETURNTIME().split(" ")[1];
            s.startDate = sample.getLEASETIME().split(" ")[1];
            s.status = mapping.get(sample);
            s.taskName = sample.getLEASEDAY();
            s.leaseStation = sample.getLEASESTATION();
            s.returnStation = sample.getRETURNSTATION();
            gantts.add(s);
        }
        result.add(gantts);
        result.add(countMap);
        result.add(marks);
        return result;
    }

    /**
     * 返回绘制轨迹的唯一站点
     * @param list
     * @return
     */
    public List<String> getMustStations(List<B_LEASEINFOHIS_BRIEF> list){
        Set<String> set = new HashSet<>();
        B_LEASEINFOHIS_BRIEF sample;
        Iterator<B_LEASEINFOHIS_BRIEF> iterator = list.iterator();
        while(iterator.hasNext()){
            sample = iterator.next();
            set.add(sample.getLEASESTATION());
            set.add(sample.getRETURNSTATION());
        }
        return new ArrayList<>(set);
    }
//    public  List<Object> getGantts(List<B_LEASEINFOHIS_BRIEF> list){
//        Map<B_LEASEINFOHIS_BRIEF,Integer>  map = new HashMap<B_LEASEINFOHIS_BRIEF,Integer>();
//        B_LEASEINFOHIS_BRIEF sample;
//        List<Gantt> gantts = new ArrayList<>();
//        Map<B_LEASEINFOHIS_BRIEF,Character> mapping = new HashMap<>();
//        Map<Character,Integer> countMap = new HashMap<>();
//        List<Object> result = new ArrayList<>();
//        List<Map.Entry<B_LEASEINFOHIS_BRIEF,Integer>> list1;
//        int i=0;
//        Iterator<B_LEASEINFOHIS_BRIEF> iterator  = list.iterator();
//        while(iterator.hasNext()){
//             sample = iterator.next();
//            if(!map.containsKey(sample)){
//                map.put(sample,1);
//            }else{
//                map.put(sample,map.get(sample)+1);
//            }
//        }
//        char[] status = {'A','B','C','D','E','F','G','H'};
//        list1 = new ArrayList<>(map.entrySet());
//        Collections.sort(list1, new Comparator<Map.Entry<B_LEASEINFOHIS_BRIEF, Integer>>() {
//            @Override
//            public int compare(Map.Entry<B_LEASEINFOHIS_BRIEF, Integer> o1, Map.Entry<B_LEASEINFOHIS_BRIEF, Integer> o2) {
//                return (o2.getValue()).compareTo(o1.getValue());
//            }
//        });
//        for(Map.Entry<B_LEASEINFOHIS_BRIEF,Integer> entry:list1){
//            if(i > 7){
//                mapping.put(entry.getKey(),status[7]);
//            }else {
//                mapping.put(entry.getKey(),status[i]);
//                countMap.put(status[i],entry.getValue());
//                i = i+1;
//            }
//        }
//        iterator = list.iterator();
//        while(iterator.hasNext()){
//            sample = iterator.next();
//            Gantt s = new Gantt();
//            s.endDate = sample.getRETURNTIME().split(" ")[1];
//            s.startDate = sample.getLEASETIME().split(" ")[1];
//            s.status = mapping.get(sample);
//            s.taskName = sample.getLEASEDAY();
//            s.leaseStation = sample.getLEASESTATION();
//            s.returnStation = sample.getRETURNSTATION();
//            gantts.add(s);
//        }
//        result.add(gantts);
//        result.add(countMap);
//        return result;
//    }


}
