package com.edussafy.clone.global.file;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileResourceRepository extends JpaRepository<FileResource, Long> {
    List<FileResource> findByIdIn(Collection<Long> ids);
    List<FileResource> findByTargetTypeAndTargetIdAndFileRoleOrderBySortOrderAscIdAsc(
            FileTargetType targetType,
            Long targetId,
            FileRole fileRole
    );
}
