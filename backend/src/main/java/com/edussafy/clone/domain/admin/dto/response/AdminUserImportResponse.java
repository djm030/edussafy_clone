package com.edussafy.clone.domain.admin.dto.response;

import java.util.List;

public record AdminUserImportResponse(int totalCount, int successCount, int failCount, List<FailedRow> failedRows) {
    public record FailedRow(int rowNo, String email, String reason) {}
}
