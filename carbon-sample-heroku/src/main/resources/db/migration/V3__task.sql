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

SET search_path = carbon, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: task; Type: TABLE; Schema: carbon; Owner: -
--

CREATE TABLE task (
    id BIGINT NOT NULL,
    title VARCHAR(64) NOT NULL,
    description text NOT NULL,
    user_id bigint NOT NULL,
    available boolean DEFAULT true NOT NULL
);


--
-- Name: task_id_seq; Type: SEQUENCE; Schema: carbon; Owner: -
--

CREATE SEQUENCE task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: task_id_seq; Type: SEQUENCE OWNED BY; Schema: carbon; Owner: -
--

ALTER SEQUENCE task_id_seq OWNED BY task.id;


--
-- Name: task id; Type: DEFAULT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY task ALTER COLUMN id SET DEFAULT nextval('task_id_seq'::regclass);


--
-- Name: task task_pkey; Type: CONSTRAINT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- Name: task task_user_id_fkey; Type: FK CONSTRAINT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY task
    ADD CONSTRAINT task_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE task ADD COLUMN "finished" boolean NOT NULL DEFAULT false;

--
-- PostgreSQL database dump complete
--

