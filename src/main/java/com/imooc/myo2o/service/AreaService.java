package com.imooc.myo2o.service;

import com.imooc.myo2o.entity.Area;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AreaService {
    public static String AREALISTKEY="arealist";
    List<Area> getAreaList();
}
