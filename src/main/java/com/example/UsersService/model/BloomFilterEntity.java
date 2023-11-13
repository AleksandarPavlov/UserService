package com.example.UsersService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "bloom_filter")
public class BloomFilterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "filter_data", columnDefinition = "BLOB")
    private byte[] filterData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFilterData() {
        return filterData;
    }

    public void setFilterData(byte[] filterData) {
        this.filterData = filterData;
    }

    public BloomFilterEntity(){

    }
    public BloomFilterEntity(byte[] filterData) {
        this.filterData = filterData;
    }
}
