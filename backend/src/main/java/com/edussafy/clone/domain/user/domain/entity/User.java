package com.edussafy.clone.domain.user.domain.entity;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String studentNo;
    private Integer generation;
    private String region;
    private Integer classNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_file_id")
    private FileResource profileFile;

    private String phoneNumber;
    private String emergencyPhoneNumber;
    private String zipCode;
    private String address;
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Builder
    public User(Long id, String email, String password, String name, String studentNo, Integer generation,
                String region, Integer classNo, FileResource profileFile, String phoneNumber,
                String emergencyPhoneNumber, String zipCode, String address, String addressDetail,
                UserRole role, UserStatus status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.studentNo = studentNo;
        this.generation = generation;
        this.region = region;
        this.classNo = classNo;
        this.profileFile = profileFile;
        this.phoneNumber = phoneNumber;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.role = role;
        this.status = status;
    }

    public void updateProfile(String phoneNumber, String emergencyPhoneNumber, String zipCode,
                              String address, String addressDetail) {
        this.phoneNumber = phoneNumber;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
