package com.project.zipkok.repository;

import com.project.zipkok.model.RealEstateImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealEstateImageRepository extends JpaRepository<RealEstateImage, Long> {

}
