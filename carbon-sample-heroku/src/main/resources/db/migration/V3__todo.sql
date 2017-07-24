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
-- Name: todo; Type: TABLE; Schema: carbon; Owner: -
--

CREATE TABLE todo (
    id BIGINT NOT NULL,
    text text NOT NULL,
    user_id bigint NOT NULL,
    available boolean DEFAULT true NOT NULL
);


--
-- Name: todo_id_seq; Type: SEQUENCE; Schema: carbon; Owner: -
--

CREATE SEQUENCE todo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: todo_id_seq; Type: SEQUENCE OWNED BY; Schema: carbon; Owner: -
--

ALTER SEQUENCE todo_id_seq OWNED BY todo.id;


--
-- Name: todo id; Type: DEFAULT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY todo ALTER COLUMN id SET DEFAULT nextval('todo_id_seq'::regclass);


--
-- Name: todo todo_pkey; Type: CONSTRAINT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY todo
    ADD CONSTRAINT todo_pkey PRIMARY KEY (id);


--
-- Name: todo todo_user_id_fkey; Type: FK CONSTRAINT; Schema: carbon; Owner: -
--

ALTER TABLE ONLY todo
    ADD CONSTRAINT todo_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- PostgreSQL database dump complete
--

