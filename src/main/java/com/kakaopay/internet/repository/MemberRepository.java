package com.kakaopay.internet.repository;

import com.kakaopay.internet.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, String> {
}
