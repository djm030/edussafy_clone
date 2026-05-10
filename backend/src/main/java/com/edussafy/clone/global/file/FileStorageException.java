package com.edussafy.clone.global.file;

import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;

public class FileStorageException extends BusinessException {
    public FileStorageException() {
        super(ErrorCode.FILE_STORAGE_FAILED);
    }
}
