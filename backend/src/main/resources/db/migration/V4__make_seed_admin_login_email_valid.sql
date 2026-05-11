-- LoginRequest validates email format, so the seeded admin username must also be
-- a valid email address for role-based smoke tests and admin login.
UPDATE users
SET email = 'admin@edussafy.local'
WHERE email = 'admin'
  AND role = 'ADMIN';
