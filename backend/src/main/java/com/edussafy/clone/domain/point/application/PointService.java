package com.edussafy.clone.domain.point.application;

import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import com.edussafy.clone.domain.point.domain.repository.PointTransactionRepository;
import com.edussafy.clone.domain.point.dto.mapper.PointDtoMapper;
import com.edussafy.clone.domain.point.dto.response.PointSummaryResponse;
import com.edussafy.clone.domain.point.dto.response.PointTransactionResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.domain.repository.UserStatRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {
    private final UserRepository userRepository;
    private final UserStatRepository userStatRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final PointDtoMapper mapper;

    public PointSummaryResponse getMySummary(Long userId) {
        User user = getUser(userId);
        return mapper.toSummaryResponse(userStatRepository.findByUser(user).orElse(null));
    }

    public PageResponse<PointTransactionResponse> getMyTransactions(Long userId, PointTransactionType transactionType,
                                                                    LocalDate startDate, LocalDate endDate, int page, int size) {
        User user = getUser(userId);
        LocalDateTime from = (startDate == null ? LocalDate.now().minusYears(1) : startDate).atStartOfDay();
        LocalDateTime to = (endDate == null ? LocalDate.now() : endDate).atTime(LocalTime.MAX);
        return PageResponse.from((transactionType == null
                ? pointTransactionRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(user, from, to, PageRequest.of(page, size))
                : pointTransactionRepository.findByUserAndTransactionTypeAndCreatedAtBetweenOrderByCreatedAtDesc(user, transactionType, from, to, PageRequest.of(page, size)))
                .map(mapper::toTransactionResponse));
    }

    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
}
