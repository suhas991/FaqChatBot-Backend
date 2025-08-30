package com.genrative.faqchatbot.repository;

import com.genrative.faqchatbot.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.beans.JavaBean;

@Repository
public interface IncidentRepository extends JpaRepository<Incident,String> {

}

