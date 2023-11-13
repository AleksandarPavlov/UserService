package com.example.UsersService.service;

import com.example.UsersService.model.BloomFilterEntity;
import com.example.UsersService.repository.BloomFilterRepository;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Service
@Transactional
public class BloomFilterService {
    private final BloomFilterRepository bloomFilterRepository;

    public BloomFilterService(BloomFilterRepository bloomFilterRepository) {
        this.bloomFilterRepository = bloomFilterRepository;
    }

    public void saveBloomFilter(BloomFilter<String> bloomFilter) {
        try {
            byte[] filterData;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream out = new ObjectOutputStream(bos)) {
                out.writeObject(bloomFilter);
                filterData = bos.toByteArray();
            }

            BloomFilterEntity existingEntity = bloomFilterRepository.findById(1L).orElse(null);
            if (existingEntity != null) {
                existingEntity.setFilterData(filterData);
                bloomFilterRepository.save(existingEntity);
            } else {
                BloomFilterEntity newEntity = new BloomFilterEntity(filterData);
                bloomFilterRepository.save(newEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BloomFilter<String> getBloomFilter() {
        BloomFilterEntity entity = bloomFilterRepository.findById(1L).orElse(null);

        if (entity != null) {;
            try {
                System.out.println(deserializeBloomFilter(entity.getFilterData()));
                return deserializeBloomFilter(entity.getFilterData());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 100000, 0.1);
    }

    private BloomFilter<String> deserializeBloomFilter(byte[] filterData) throws IOException, ClassNotFoundException {

        return (BloomFilter<String>) new ObjectInputStream(new ByteArrayInputStream(filterData)).readObject();
    }
}
