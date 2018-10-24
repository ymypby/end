package com.other;

import com.domain.B_LEASEINFOHIS_BRIEF;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ymy on 2018/10/6.
 */
public class MyMap {
    int Gtype; //图的类型(0:无向图 1:有向图)
    int V; //顶点的数量
    int E; //边的数量
    List<String> Vertex;
    int[][] EdgeWeight;
    public MyMap(){
        V = 0;
        E = 0;
        Vertex = null;
        EdgeWeight = null;
    }
    public MyMap(List<String>list) {
        this();
        setVertex(list);
    }

    public void generateEdge(List<B_LEASEINFOHIS_BRIEF> list){
        int x,y;
        B_LEASEINFOHIS_BRIEF sample;
        Iterator<B_LEASEINFOHIS_BRIEF> it = list.iterator();
        while(it.hasNext()){
            sample = it.next();
            x = Vertex.indexOf(sample.getLEASESTATION());
            y = Vertex.indexOf(sample.getRETURNSTATION());
            if(EdgeWeight[x][y] == 0 && EdgeWeight[y][x] == 0)
                E++;
            EdgeWeight[x][y]++;

        }
    }

    public void clearEdge(){
        EdgeWeight = new int[V][V];
        E = 0;
    }

    public void setVertex(List<String> list){
        try{
            Vertex = list;
            EdgeWeight = new int[Vertex.size()][Vertex.size()];
            V = Vertex.size();
            E = 0;
        }catch (NullPointerException e){
            throw new NullPointerException("空值");
        }
    }

    public List<Integer> countDegree(){
        List<Integer> list= new ArrayList<Integer>();
        int count = 0;
        for(int i = 0;i<V;i++){
            count = 0;
            for(int j = 0;j<V;j++){
                count = count + EdgeWeight[i][j];
            }
            list .add(count);
        }
        return list;
    }
}
