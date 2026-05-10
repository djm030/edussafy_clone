package com.edussafy.clone.domain.agreement.domain.entity;

import com.edussafy.clone.domain.agreement.domain.enums.AgreementCategory;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementTargetType;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementType;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "agreements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agreement extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AgreementCategory category;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AgreementTargetType targetType;
    private Long targetId;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String contentHtml;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "attachment_file_id")
    private FileResource attachmentFile;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AgreementType agreementType;
    @Column(nullable = false)
    private String version;
    @Column(nullable = false)
    private Boolean isRequired;
    @Column(nullable = false)
    private Boolean isActive;
    @Column(nullable = false)
    private Integer sortOrder;
    @Builder
    public Agreement(Long id, AgreementCategory category, AgreementTargetType targetType, Long targetId, String title, String contentHtml, FileResource attachmentFile, AgreementType agreementType, String version, Boolean isRequired, Boolean isActive, Integer sortOrder) {
        this.id=id; this.category=category; this.targetType=targetType == null ? AgreementTargetType.GLOBAL : targetType; this.targetId=targetId; this.title=title; this.contentHtml=contentHtml; this.attachmentFile=attachmentFile; this.agreementType=agreementType; this.version=version; this.isRequired=isRequired == null ? false : isRequired; this.isActive=isActive == null ? true : isActive; this.sortOrder=sortOrder == null ? 0 : sortOrder;
    }

    public void update(AgreementCategory category, AgreementTargetType targetType, Long targetId, String title, String contentHtml, FileResource attachmentFile, AgreementType agreementType, String version, Boolean isRequired, Boolean isActive, Integer sortOrder) {
        this.category=category; this.targetType=targetType == null ? AgreementTargetType.GLOBAL : targetType; this.targetId=targetId; this.title=title; this.contentHtml=contentHtml; this.attachmentFile=attachmentFile; this.agreementType=agreementType; this.version=version; this.isRequired=isRequired != null && isRequired; this.isActive=isActive != null && isActive; this.sortOrder=sortOrder == null ? this.sortOrder : sortOrder;
    }

    public void inactive() { this.isActive = false; }
}
