package com.school.management.Models.Generic;

import lombok.Data;
import lombok.NoArgsConstructor;

public class mGeneric {
    @Data
    @NoArgsConstructor
    public  static  class mAPIResponse<T> {
        public mAPIResponse(int respCode, String respMsg, T respData) {
            RespCode = respCode;
            RespMsg = respMsg;
            RespData = respData;
        }
        public mAPIResponse(int respCode, String respMsg) {
            RespCode = respCode;
            RespMsg = respMsg;
        }
        private int RespCode;
        private String RespMsg;
        private T RespData;
    }
}
