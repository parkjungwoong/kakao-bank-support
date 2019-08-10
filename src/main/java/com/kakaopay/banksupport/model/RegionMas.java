package com.kakaopay.banksupport.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("RegionMas")
public class RegionMas {
    private String regionId;
    private String name;
    private String mapId;
}
