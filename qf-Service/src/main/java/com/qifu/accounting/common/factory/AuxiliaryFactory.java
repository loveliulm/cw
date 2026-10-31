package com.qifu.accounting.common.factory;

import com.qifu.accounting.sys.domain.BookSubject;
import org.springframework.stereotype.Component;

/**
 *  yangyang
 * @date 2020/7/15 17:23
 */
@Component
public class AuxiliaryFactory {

    /**
     * 重置是否是辅组核算字段
     * @param bookSubject
     * @return
     */
    public BookSubject isAuxiliarySet(BookSubject bookSubject){
        if((bookSubject.getIsCustomer() != null && bookSubject.getIsCustomer()) ||
                (bookSubject.getIsPerson() != null && bookSubject.getIsPerson()) ||
                (bookSubject.getIsDepartment() != null && bookSubject.getIsDepartment()) ||
                (bookSubject.getIsStock() != null && bookSubject.getIsStock()) ||
                (bookSubject.getIsSupplier() != null && bookSubject.getIsSupplier()) ||
                (bookSubject.getIsProject() != null && bookSubject.getIsProject())){
            bookSubject.setIsAuxiliary(Boolean.TRUE);
        }

        if((bookSubject.getIsCustomer() != null && !bookSubject.getIsCustomer()) &&
                (bookSubject.getIsPerson() != null && !bookSubject.getIsPerson()) &&
                (bookSubject.getIsDepartment() != null && !bookSubject.getIsDepartment()) &&
                (bookSubject.getIsStock() != null && !bookSubject.getIsStock()) &&
                (bookSubject.getIsSupplier() != null && !bookSubject.getIsSupplier()) &&
                (bookSubject.getIsProject() != null && !bookSubject.getIsProject())){
            bookSubject.setIsAuxiliary(Boolean.FALSE);
        }
        return  bookSubject;
    }



    public void updateBalanceByAuxiliary(BookSubject bookSubject){

    }

}
