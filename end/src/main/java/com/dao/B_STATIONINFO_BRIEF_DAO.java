package com.dao;

import com.domain.B_LEASEINFOHIS_BRIEF;
import com.domain.B_STATIONINFO_BRIEF;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
/**
 * Created by ymy on 2018/10/5.
 */
@Repository
public class B_STATIONINFO_BRIEF_DAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<B_STATIONINFO_BRIEF> get(){
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(" FROM B_STATIONINFO_BRIEF");
        List<B_STATIONINFO_BRIEF> list = null;
        list = query.list();
        session.close();
        return list;
    }
}
