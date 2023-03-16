package org.example.mufiye.repository;

import java.util.List;
import org.example.mufiye.mo.MessageMo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<MessageMo, String> {
    List<MessageMo> findAllByToUserIdOrderByCreateTimeDesc(String toUserId, Pageable pageable);
}
