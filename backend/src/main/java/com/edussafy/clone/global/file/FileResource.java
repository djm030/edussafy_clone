package com.edussafy.clone.global.file;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileResource extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false)
    private String fileUrl;

    private String fileType;
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id")
    private User uploadedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileTargetType targetType;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileRole fileRole;

    @Column(nullable = false)
    private Integer sortOrder;

    @Builder
    public FileResource(Long id, String originalName, String storedName, String fileUrl, String fileType,
                        Long fileSize, User uploadedBy, FileTargetType targetType, Long targetId,
                        FileRole fileRole, Integer sortOrder) {
        this.id = id;
        this.originalName = originalName;
        this.storedName = storedName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
        this.targetType = targetType;
        this.targetId = targetId;
        this.fileRole = fileRole;
        this.sortOrder = sortOrder == null ? 0 : sortOrder;
    }

    public void linkTo(FileTargetType targetType, Long targetId, FileRole fileRole, int sortOrder) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.fileRole = fileRole;
        this.sortOrder = sortOrder;
    }
}
