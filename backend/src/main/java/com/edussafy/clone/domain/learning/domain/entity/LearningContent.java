package com.edussafy.clone.domain.learning.domain.entity;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseSession;
import com.edussafy.clone.domain.learning.domain.enums.LearningContentType;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.global.entity.BaseTimeEntity;
import com.edussafy.clone.global.file.FileResource;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "learning_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningContent extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "course_id")
    private Course course;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "session_id")
    private CourseSession session;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "category_id")
    private LearningCategory category;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private LearningContentType contentType;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "thumbnail_file_id")
    private FileResource thumbnailFile;
    private String contentUrl;
    private Integer durationSeconds;
    @Column(nullable = false)
    private Integer viewCount;
    @Column(nullable = false)
    private Integer likeCount;
    @Column(nullable = false)
    private Integer bookmarkCount;
    @Column(nullable = false)
    private Integer downloadCount;
    @Column(nullable = false)
    private Boolean isRequired;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Builder
    public LearningContent(Long id, Course course, CourseSession session, LearningCategory category, String title,
                           String description, LearningContentType contentType, FileResource thumbnailFile,
                           String contentUrl, Integer durationSeconds, Integer viewCount, Integer likeCount,
                           Integer bookmarkCount, Integer downloadCount, Boolean isRequired, LocalDateTime openAt,
                           LocalDateTime closeAt, User createdBy) {
        this.id = id;
        this.course = course;
        this.session = session;
        this.category = category;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.thumbnailFile = thumbnailFile;
        this.contentUrl = contentUrl;
        this.durationSeconds = durationSeconds;
        this.viewCount = viewCount == null ? 0 : viewCount;
        this.likeCount = likeCount == null ? 0 : likeCount;
        this.bookmarkCount = bookmarkCount == null ? 0 : bookmarkCount;
        this.downloadCount = downloadCount == null ? 0 : downloadCount;
        this.isRequired = isRequired != null && isRequired;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.createdBy = createdBy;
    }

    public void update(Course course, CourseSession session, LearningCategory category, String title, String description,
                       LearningContentType contentType, FileResource thumbnailFile, String contentUrl, Integer durationSeconds,
                       Boolean isRequired, LocalDateTime openAt, LocalDateTime closeAt) {
        this.course = course;
        this.session = session;
        this.category = category;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.thumbnailFile = thumbnailFile;
        this.contentUrl = contentUrl;
        this.durationSeconds = durationSeconds;
        this.isRequired = isRequired != null && isRequired;
        this.openAt = openAt;
        this.closeAt = closeAt;
    }

    public void increaseViewCount() { this.viewCount += 1; }
    public void increaseLikeCount() { this.likeCount += 1; }
    public void increaseDownloadCount() { this.downloadCount += 1; }
}
