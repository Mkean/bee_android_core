package com.bee.launch.persistence;

import com.bee.android.common.persistence.MMKVManager;
import com.bee.android.common.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class MMKVInstance {

    private static Map<String, Persistence> map;
    private static Map<String, Persistence> mapMulti;


    static {
        map = new HashMap<>();
        mapMulti = new HashMap<>();
    }

    public static Persistence getPersistence(String id) {
        if (map.containsKey(id)) {
            return map.get(id);
        } else {
            Persistence persistence = new Persistence(MMKVManager.INSTANCE.mmkvWithId(id));
            map.put(id, persistence);
            return persistence;
        }
    }

    public static Persistence getPersistenceMulti(String id) {
        if (mapMulti.containsKey(id)) {
            return mapMulti.get(id);
        } else {
            Persistence persistence = new Persistence(MMKVManager.INSTANCE.mmkvWithID_MULIT(id));
            mapMulti.put(id, persistence);
            return persistence;
        }
    }
}
