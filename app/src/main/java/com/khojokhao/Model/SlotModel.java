package com.khojokhao.Model;

public class SlotModel {
    String slot_id;
    String day;
    String slot_timing;
    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSlot_timing() {
        return slot_timing;
    }

    public void setSlot_timing(String slot_timing) {
        this.slot_timing = slot_timing;
    }
}
