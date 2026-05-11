-- The document submission page lets students create their own document posts.
-- Existing databases seeded doc-req as NOTICE, which made BoardCommandService
-- treat it as admin/operator-only and return 403 after file upload.
UPDATE boards
SET board_type = 'NORMAL',
    description = 'Student document submission board with attachments'
WHERE code = 'doc-req'
  AND board_type = 'NOTICE';

-- Match the frontend document form categories so API submissions do not have to
-- fall back to the generic default category.
INSERT INTO board_categories (board_id, name, code)
SELECT b.id, '증빙', 'EVIDENCE'
FROM boards b
WHERE b.code = 'doc-req'
  AND NOT EXISTS (
      SELECT 1 FROM board_categories c WHERE c.board_id = b.id AND c.code = 'EVIDENCE'
  );

INSERT INTO board_categories (board_id, name, code)
SELECT b.id, '서류', 'DOCUMENT'
FROM boards b
WHERE b.code = 'doc-req'
  AND NOT EXISTS (
      SELECT 1 FROM board_categories c WHERE c.board_id = b.id AND c.code = 'DOCUMENT'
  );

INSERT INTO board_categories (board_id, name, code)
SELECT b.id, '기타', 'ETC'
FROM boards b
WHERE b.code = 'doc-req'
  AND NOT EXISTS (
      SELECT 1 FROM board_categories c WHERE c.board_id = b.id AND c.code = 'ETC'
  );
