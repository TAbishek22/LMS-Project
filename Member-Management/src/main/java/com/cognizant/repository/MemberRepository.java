package com.cognizant.repository;

import com.cognizant.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
    Optional<MemberEntity> findByPhone(String phone);
    List<MemberEntity> findByMembershipStatus(String membershipStatus);
    List<MemberEntity> findByNameContainingIgnoreCase(String name);
}
