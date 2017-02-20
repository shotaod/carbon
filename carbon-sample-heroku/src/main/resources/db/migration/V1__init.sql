--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: carbon; Owner: root
--
CREATE SCHEMA IF NOT EXISTS  d6vh9houtm9ttn;

CREATE SEQUENCE IF NOT EXISTS user_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


SET default_tablespace = '';
SET default_with_oids = false;

--
-- Name: user; Type: TABLE; Schema: ; Owner: root
--

CREATE TABLE d6vh9houtm9ttn.user (
    id bigint DEFAULT nextval('user_id_seq'::regclass) NOT NULL,
    email character varying(255) NOT NULL,
    user_name character varying(255) NOT NULL,
    password character varying(65) NOT NULL
);

--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: carbon; Owner: root
--

ALTER TABLE ONLY d6vh9houtm9ttn.user
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_id_pk; Type: INDEX; Schema: carbon; Owner: root
--

CREATE UNIQUE INDEX user_id_pk ON d6vh9houtm9ttn.user USING btree (id);


--
-- PostgreSQL database dump complete
--
