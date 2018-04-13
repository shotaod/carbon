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

--
-- Name: carbon; Type: SCHEMA; Schema: -;
--

CREATE SCHEMA IF NOT EXISTS carbon;
SET search_path = carbon;
SET default_tablespace = '';
SET default_with_oids = FALSE;
--
-- Name: user_id_seq; Type: SEQUENCE; Schema: carbon;
--

CREATE SEQUENCE IF NOT EXISTS user_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

--
-- Name: user; Type: TABLE; Schema: carbon;
--

CREATE TABLE "carbon"."user" (
  id        BIGINT DEFAULT nextval('user_id_seq' :: REGCLASS) NOT NULL,
  email     CHARACTER VARYING(255)                            NOT NULL,
  user_name CHARACTER VARYING(255)                            NOT NULL,
  secret    CHARACTER VARYING(65)                             NOT NULL
);

--
-- Name: user user_email_key; Type: CONSTRAINT; Schema: carbon;
--

ALTER TABLE ONLY "carbon"."user"
  ADD CONSTRAINT user_email_key UNIQUE (email);

--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: carbon;
--

ALTER TABLE ONLY "carbon"."user"
  ADD CONSTRAINT user_pkey PRIMARY KEY (id);

--
-- Name: user_id_pk; Type: INDEX; Schema: carbon;
--

CREATE UNIQUE INDEX user_id_pk
  ON "carbon"."user" USING BTREE (id);

--
-- PostgreSQL database dump complete
--

