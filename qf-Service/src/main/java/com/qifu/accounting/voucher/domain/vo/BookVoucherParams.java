package com.qifu.accounting.voucher.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.AuxiliaryTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  yangyang
 * @date 2020/7/23 14:01
 */
@Data
public class BookVoucherParams {
    private Long bookId;

    private String voucherCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private Long [] voucherIds;

    private Long subjectId;

    private String summary;

    private String voucherWord;

    private Long auxiliaryTypeId;

    private String auxiliaryId;

    private String auxiliary;

    private BigDecimal smallMoney;

    private BigDecimal bigMoney;

    //"desc" "asc"
    private String order = "asc";

    public void resetAuxiliary(){
        this.auxiliary = "";
        if(this.auxiliaryTypeId != null){
            if(this.auxiliaryTypeId == 6){
                if("全部".equals(this.auxiliaryId)){
                    this.auxiliary = "stock_id != 0";
                }else{
                    this.auxiliary = "stock_id = "+this.auxiliaryId;
                }
            }else if(this.auxiliaryTypeId == 5){
                if("全部".equals(this.auxiliaryId)){
                    this.auxiliary = "person_id != 0";
                }else{
                    this.auxiliary = "person_id = "+this.auxiliaryId;
                }
            }else if(this.auxiliaryTypeId == 4){
                if("全部".equals(this.auxiliaryId)){
                    this.auxiliary = "department_id != 0";
                }else{
                    this.auxiliary = "department_id = "+this.auxiliaryId;
                }
            }else if(this.auxiliaryTypeId == 3){
                if("全部".equals(this.auxiliaryId)){
                    this.auxiliary = "project_id != 0";
                }else{
                    this.auxiliary = "project_id = "+this.auxiliaryId;
                }
            }else if(this.auxiliaryTypeId == 2){
                if("全部".equals(this.auxiliaryId)){
                    this.auxiliary = "customer_id != 0";
                }else{
                    this.auxiliary = "customer_id = "+this.auxiliaryId;
                }
            }else if(this.auxiliaryTypeId == 1){
                if("全部".equals(this.auxiliaryId)){
                    this.auxiliary = "supplier_id != 0";
                }else{
                    this.auxiliary = "supplier_id = "+this.auxiliaryId;
                }
            }else{
                this.auxiliary = "(stock_id != 0 or person_id != 0 or department_id != 0 or " +
                        "project_id != 0 or customer_id != 0 or supplier_id != 0)";
            }
        }
    }
}
