package com.a6dig.digitalsignage.dto;

import java.util.List;

public class LayoutRequestDto {
    private String name;
    private int layoutCol;
    private int layoutRow;

    private List<LayoutSlotRequestDto> layoutSlotRequestDtoList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLayoutCol() {
        return layoutCol;
    }

    public void setLayoutCol(int layoutCol) {
        this.layoutCol = layoutCol;
    }

    public int getLayoutRow() {
        return layoutRow;
    }

    public void setLayoutRow(int layoutRow) {
        this.layoutRow = layoutRow;
    }

    public List<LayoutSlotRequestDto> getLayoutSlotRequestDtoList() {
        return layoutSlotRequestDtoList;
    }

    public void setLayoutSlotRequestDtoList(List<LayoutSlotRequestDto> layoutSlotRequestDtoList) {
        this.layoutSlotRequestDtoList = layoutSlotRequestDtoList;
    }
}
