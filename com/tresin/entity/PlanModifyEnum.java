package com.tresin.entity;

/**
 * Created by Admin on 2018/7/31.
 */
public enum PlanModifyEnum {

    NEW("04","PLAN_ADD_ITEM_ LOCK","PLAN_ADD_ITEM_COUNT"),
    UPDATE("02","PLAN_MODIFICATION_LOCK","PLAN_MODIFICATION_COUNT"),
    DELETE("03","PLAN_ITEMS_DELETION_ LOCK","PLAN_ITEMS_DELETION_COUNT"),
    DATE_CHANGE("01","PLAN_MODIFICATION_LOCK","PLAN_MODIFICATION_COUNT");

    private String type;
    private String key;
    private String count;

    public static PlanModifyEnum getItemByType(String type){
        PlanModifyEnum[] items = PlanModifyEnum.values();
        for(PlanModifyEnum item : items){
            if(type.equals(item.getType())){
                return item;
            } else {
                continue;
            }
        }
        return null;
    }

    PlanModifyEnum(String type, String key, String count){
        this.type = type;
        this.key = key;
        this.count = count;
    }
    public String getType(){
        return type;
    }

    public String getKey(){
        return this.key;
    }

    public String getCount(){
        return this.count;
    }
}

class  Test{
    public static void main(String[] args){
        String type ="01";
        PlanModifyEnum item = PlanModifyEnum.getItemByType(type);
        System.out.println(item == PlanModifyEnum.DATE_CHANGE);
    }
}
