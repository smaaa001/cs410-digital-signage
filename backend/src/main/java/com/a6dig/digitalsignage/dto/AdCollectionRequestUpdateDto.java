package com.a6dig.digitalsignage.dto;

public class AdCollectionRequestUpdateDto<T extends AdContentRequestUpdateDto> extends AdCollectionRequestDto<T>{
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
