package com.urlshorter.site.repositories;

import com.urlshorter.site.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {

    void deleteAllById(Iterable<? extends Long> longs);

    List<Link> findLinksByLongLinkLike(String longLinkFragment);

    List<Link> findAllById(Iterable<Long> longs);

}
