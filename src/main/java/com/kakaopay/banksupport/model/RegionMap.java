package com.kakaopay.banksupport.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("RegionMap")
public class RegionMap {
    private String mapId;
    private String name;
}
