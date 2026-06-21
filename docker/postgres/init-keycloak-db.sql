-- ============================================================
--  Create a dedicated database for Keycloak on the shared
--  PostgreSQL instance.  The main application database
--  (org_db) is already created via the POSTGRES_DB env var.
-- ============================================================
CREATE DATABASE keycloak;
