package com.tinyblack.framework.clean;


import com.tinyblack.framework.clean.base.DataRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * 管理类
 * Created by liutao on 2018/3/6.
 */

public class RepositoryMgr {
    private final Map<Class, DataRepository> dataRepositoryMap = new HashMap<>();
    private static RepositoryMgr INSTANCE = null;

    private RepositoryMgr() {

    }

    public static RepositoryMgr instance() {
        if (INSTANCE == null) {
            synchronized (RepositoryMgr.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryMgr();
                }
            }
        }
        return INSTANCE;
    }

    public void addRepository(Class<? extends DataRepository> clazz, @NonNull DataRepository dataRepository) {
        dataRepositoryMap.put(clazz, dataRepository);
    }

    @NonNull
    public <T extends DataRepository> T getRepository(Class<T> clazz) {
        DataRepository dataRepository = dataRepositoryMap.get(clazz);
        if (dataRepository == null) {
            throw new RuntimeException("must addRepository(" + clazz.getSimpleName() + ")");
        }
        return (T) dataRepository;
    }
}
