package com.dao;

import com.domain.B_LEASEINFOHIS_BRIEF;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ymy on 2018/10/6.
 */
@Repository
public class B_LEASEINFOHIS_BRIEF_DAO {
    @Autowired
    private SessionFactory sessionFactory;

    public List<B_LEASEINFOHIS_BRIEF> findByID(String id){
        String hql = "FROM B_LEASEINFOHIS_BRIEF AS E WHERE E.ID = :id";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        List<B_LEASEINFOHIS_BRIEF> list;
        query.setParameter("id",id);
        list  = query.list();
        session.close();
        return list;
    }

    public List<B_LEASEINFOHIS_BRIEF> findByIDAndTime(String id,String minDay,String maxDay){
        String hql = "FROM B_LEASEINFOHIS_BRIEF AS E WHERE E.CARDNO = :id AND E.RETURNDAY >= :minDay AND E.LEASEDAY <= :maxDay ORDER BY  E.RETURNTIME";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(hql);
        List<B_LEASEINFOHIS_BRIEF> list;
        query.setParameter("id",id);
        query.setParameter("minDay",minDay);
        query.setParameter("maxDay",maxDay);
        list  = query.list();
        session.close();
        return list;
    }


}
