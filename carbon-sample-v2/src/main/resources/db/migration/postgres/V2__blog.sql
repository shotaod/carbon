--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;

SET search_path = carbon;

SET default_tablespace = '';

SET default_with_oids = FALSE;

--
-- Name: blog; Type: TABLE; Schema: carbon;
--

CREATE TABLE blog (
  id      BIGINT NOT NULL,
  title   TEXT   NOT NULL,
  summary TEXT   NOT NULL,
  content TEXT   NOT NULL
);

--
-- Name: blog_id_seq; Type: SEQUENCE; Schema: carbon;
--

CREATE SEQUENCE blog_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

--
-- Name: blog_id_seq; Type: SEQUENCE OWNED BY; Schema: carbon;
--

ALTER SEQUENCE blog_id_seq
OWNED BY blog.id;

--
-- Name: blog id; Type: DEFAULT; Schema: carbon;
--

ALTER TABLE ONLY blog
  ALTER COLUMN id SET DEFAULT nextval('blog_id_seq' :: REGCLASS);

--
-- Name: blog blog_pkey; Type: CONSTRAINT; Schema: carbon;
--

ALTER TABLE ONLY blog
  ADD CONSTRAINT blog_pkey PRIMARY KEY (id);

--
-- PostgreSQL database dump complete
--

