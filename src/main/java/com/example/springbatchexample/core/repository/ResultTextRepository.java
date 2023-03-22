package com.example.springbatchexample.core.repository;

import com.example.springbatchexample.core.domain.ResultText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultTextRepository extends JpaRepository<ResultText, Integer> {
}
