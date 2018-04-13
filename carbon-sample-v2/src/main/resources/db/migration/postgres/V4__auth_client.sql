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

SET search_path = carbon, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = FALSE;

--
-- Name: auth_client; Type: TABLE; Schema: carbon; Owner: -
--

CREATE TABLE auth_client (
  id          BIGINT         NOT NULL,
  client_host CHARACTER(127) NOT NULL,
  client_id   CHARACTER(127) NOT NULL
);

--
-- Name: auth_client_id_seq; Type: SEQUENCE; Schema: carbon; Owner: -
--

CREATE SEQUENCE auth_client_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

--
-- Name: auth_client_id_seq; Type: SEQUENCE OWNED BY; Schema: carbon; Owner: -
--

ALTER SEQUENCE auth_client_id_seq
OWNED BY auth_client.id;

--
-- Name: auth_client id; Type: DEFAULT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY auth_client
  ALTER COLUMN id SET DEFAULT nextval('auth_client_id_seq' :: REGCLASS);

--
-- Name: auth_client auth_client_client_host_key; Type: CONSTRAINT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY auth_client
  ADD CONSTRAINT auth_client_client_host_key UNIQUE (client_host);

--
-- Name: auth_client auth_client_client_id_key; Type: CONSTRAINT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY auth_client
  ADD CONSTRAINT auth_client_client_id_key UNIQUE (client_id);

--
-- Name: auth_client auth_client_pkey; Type: CONSTRAINT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY auth_client
  ADD CONSTRAINT auth_client_pkey PRIMARY KEY (id);

--
-- Name: auth_client_client_host_uindex; Type: INDEX; Schema: carbon; Owner: -
--

CREATE UNIQUE INDEX auth_client_client_host_uindex
  ON auth_client USING BTREE (client_host);

--
-- Name: auth_client_client_id_uindex; Type: INDEX; Schema: carbon; Owner: -
--

CREATE UNIQUE INDEX auth_client_client_id_uindex
  ON auth_client USING BTREE (client_id);

--
-- PostgreSQL database dump complete
--

