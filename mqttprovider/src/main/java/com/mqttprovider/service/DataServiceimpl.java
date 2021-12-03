package com.mqttprovider.service;

import com.mqttprovider.dao.DataDao;
import com.mqttprovider.domain.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@EnableTransactionManagement//开启事务管理器
@Service
public class DataServiceimpl implements DataService{
    @Autowired
    DataDao dataDao;

    @Override
    public List<Data> query() {
        return dataDao.query();
    }

    @Override
    public Data queryById(int id) {
        return dataDao.queryById(id);
    }

    @Override
    public List<Integer> getAllId() {
        return dataDao.getAllId();
    }
}
