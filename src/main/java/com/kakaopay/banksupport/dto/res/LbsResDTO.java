package com.kakaopay.banksupport.dto.res;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LbsResDTO extends ComDTO {
    List<Map> list;
}
