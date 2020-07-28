package com.szz.hello.common;

import java.util.List;

/**
 * @author szz
 */
public class ObjectStatusVo {

    private String className;

    private BusFlag status;

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

    public BusFlag getStatus() {
        return status;
    }

    public void setStatus(BusFlag status) {
        this.status = status;
    }

    static class SubField {

        private String className;

        private String label;

        private BusFlag status;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public BusFlag getStatus() {
            return status;
        }

        public void setStatus(BusFlag status) {
            this.status = status;
        }
    }


}
