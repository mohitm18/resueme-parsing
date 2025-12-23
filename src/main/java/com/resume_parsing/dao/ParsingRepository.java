package com.resume_parsing.dao;

import com.resume_parsing.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsingRepository extends JpaRepository<Resume,Long > {
}
