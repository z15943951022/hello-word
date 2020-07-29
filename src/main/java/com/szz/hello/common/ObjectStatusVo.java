package com.szz.hello.common;

import java.util.List;

/**
 * @author szz
 */
public class ObjectStatusVo {

    private String className;

    private BusStatus status;

    private List<SubField> subField;

    public List<SubField> getSubField() {
        return subField;
    }

    public void setSubField(List<SubField> subField) {
        this.subField = subField;
    }

    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }

    public BusStatus getStatus() {
        return status;
    }

    public void setStatus(BusStatus status) {
        this.status = status;
    }

    static class SubField {

        private String fieldName;

        private String label;

        private BusStatus status;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public BusStatus getStatus() {
            return status;
        }

        public void setStatus(BusStatus status) {
            this.status = status;
        }
    }


}
