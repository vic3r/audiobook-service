-- Database Setup Script for Audiobook Service
-- This script creates the database if it doesn't exist
-- Note: Tables are automatically created by JPA/Hibernate (ddl-auto: update)
-- This script is for manual database setup if needed

-- Connect to PostgreSQL
\c postgres;

-- Create database if it doesn't exist
SELECT 'CREATE DATABASE audiobooks'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'audiobooks')\gexec

-- Connect to audiobooks database
\c audiobooks;

-- Create schema (if needed)
-- CREATE SCHEMA IF NOT EXISTS audiobook_schema;

-- Note: Tables will be created automatically by Spring Boot JPA:
--   - users
--   - audiobooks  
--   - library_items
--   - purchases
--
-- To verify tables after Spring Boot starts:
--   SELECT table_name FROM information_schema.tables 
--   WHERE table_schema = 'public' ORDER BY table_name;

-- Create indexes for performance (optional, JPA creates these automatically)
-- These are created automatically by JPA based on entity annotations

-- Verify database was created
SELECT 'Database audiobooks is ready' AS status;
