package com.myshop.repository;

import com.myshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
}
