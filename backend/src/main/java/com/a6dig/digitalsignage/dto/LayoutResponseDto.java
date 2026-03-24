package com.a6dig.digitalsignage.dto;

import java.time.LocalDateTime;
import java.util.List;

public class LayoutResponseDto {
    private Long id;
    private String name;
    private int cols;
    private int rows;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<LayoutSlotResponseDto> slots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<LayoutSlotResponseDto> getSlots() {
        return slots;
    }

    public void setSlots(List<LayoutSlotResponseDto> slots) {
        this.slots = slots;
    }
}
