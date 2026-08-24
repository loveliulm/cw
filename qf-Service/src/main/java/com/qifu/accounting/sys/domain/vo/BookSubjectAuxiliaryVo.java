package com.qifu.accounting.sys.domain.vo;

import com.qifu.accounting.sys.domain.BookSubjectAuxiliary;
import lombok.Data;

import java.util.List;

/**
 *  yangyang
 * @date 2020/8/31 10:18
 */
@Data
public class BookSubjectAuxiliaryVo {
    private Long subjectId;

    private List<BookSubjectAuxiliary> supplierList;

    private List<BookSubjectAuxiliary> customerList;

    private List<BookSubjectAuxiliary> projectList;

    private List<BookSubjectAuxiliary> departmentList;

    private List<BookSubjectAuxiliary> personList;

    private List<BookSubjectAuxiliary> stockList;

}
