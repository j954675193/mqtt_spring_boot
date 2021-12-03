package com.mqttprovider.dao;

import com.mqttprovider.domain.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataDao {
    @Select("select * from tj_data")
    List<Data> query();

    @Select("select * from tj_data where id=#{id}")
    Data queryById(int id);

    @Select("select id from tj_data")
    List<Integer> getAllId();
}
