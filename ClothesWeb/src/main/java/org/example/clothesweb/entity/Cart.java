package org.example.clothesweb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cart {
    private Long id;
    private Long userId;
    private Long clothesId;
    private String clothesSize;
    private Integer quantity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String clothesName;
    private BigDecimal clothesPrice;
    private String clothesImageUrl;

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClothesId() {
        return clothesId;
    }

    public void setClothesId(Long clothesId) {
        this.clothesId = clothesId;
    }

    public String getClothesSize() {
        return clothesSize;
    }

    public void setClothesSize(String clothesSize) {
        this.clothesSize = clothesSize;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getClothesName() {
        return clothesName;
    }

    public void setClothesName(String clothesName) {
        this.clothesName = clothesName;
    }

    public BigDecimal getClothesPrice() {
        return clothesPrice;
    }

    public void setClothesPrice(BigDecimal clothesPrice) {
        this.clothesPrice = clothesPrice;
    }

    public String getClothesImageUrl() {
        return clothesImageUrl;
    }

    public void setClothesImageUrl(String clothesImageUrl) {
        this.clothesImageUrl = clothesImageUrl;
    }
}
