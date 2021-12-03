package com.mqttprovider.service;


import com.mqttprovider.domain.Data;

import java.util.List;

public interface DataService {
    List<Data> query();
    Data queryById(int id);
    List<Integer> getAllId();
}
