--
-- PostgreSQL database dump
--

-- Dumped from database version 12.4
-- Dumped by pg_dump version 12.4

-- Started on 2021-11-05 11:26:04

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 10 (class 2615 OID 17435)
-- Name: jwtauth; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA jwtauth;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 323 (class 1259 OID 17524)
-- Name: refresh_token; Type: TABLE; Schema: jwtauth; Owner: -
--

CREATE TABLE jwtauth.refresh_token (
    refresh_token_id bigint NOT NULL,
    refresh_token character varying NOT NULL,
    user_device_id bigint NOT NULL,
    expiry_date timestamp without time zone,
    revoked boolean DEFAULT false NOT NULL,
    replaced_by bigint,
    tstamp timestamp without time zone DEFAULT now() NOT NULL,
    jti character varying(36) NOT NULL,
    logged_in boolean DEFAULT false
);


--
-- TOC entry 322 (class 1259 OID 17522)
-- Name: refresh_token_refresh_token_id_seq; Type: SEQUENCE; Schema: jwtauth; Owner: -
--

CREATE SEQUENCE jwtauth.refresh_token_refresh_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3197 (class 0 OID 0)
-- Dependencies: 322
-- Name: refresh_token_refresh_token_id_seq; Type: SEQUENCE OWNED BY; Schema: jwtauth; Owner: -
--

ALTER SEQUENCE jwtauth.refresh_token_refresh_token_id_seq OWNED BY jwtauth.refresh_token.refresh_token_id;


--
-- TOC entry 318 (class 1259 OID 17469)
-- Name: role; Type: TABLE; Schema: jwtauth; Owner: -
--

CREATE TABLE jwtauth.role (
    role_id bigint NOT NULL,
    role_name character varying
);


--
-- TOC entry 317 (class 1259 OID 17467)
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: jwtauth; Owner: -
--

CREATE SEQUENCE jwtauth.role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3198 (class 0 OID 0)
-- Dependencies: 317
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: jwtauth; Owner: -
--

ALTER SEQUENCE jwtauth.role_role_id_seq OWNED BY jwtauth.role.role_id;


--
-- TOC entry 316 (class 1259 OID 17458)
-- Name: user_; Type: TABLE; Schema: jwtauth; Owner: -
--

CREATE TABLE jwtauth.user_ (
    user_id bigint NOT NULL,
    email character varying NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    first_name character varying,
    last_name character varying,
    active boolean NOT NULL,
    secret character varying(16),
    verification_code character varying(36) DEFAULT NULL::character varying
);


--
-- TOC entry 321 (class 1259 OID 17509)
-- Name: user_device; Type: TABLE; Schema: jwtauth; Owner: -
--

CREATE TABLE jwtauth.user_device (
    user_device_id bigint NOT NULL,
    user_id bigint NOT NULL,
    device_id character varying(255) NOT NULL
);


--
-- TOC entry 320 (class 1259 OID 17507)
-- Name: user_device_user_device_id_seq; Type: SEQUENCE; Schema: jwtauth; Owner: -
--

CREATE SEQUENCE jwtauth.user_device_user_device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3199 (class 0 OID 0)
-- Dependencies: 320
-- Name: user_device_user_device_id_seq; Type: SEQUENCE OWNED BY; Schema: jwtauth; Owner: -
--

ALTER SEQUENCE jwtauth.user_device_user_device_id_seq OWNED BY jwtauth.user_device.user_device_id;


--
-- TOC entry 319 (class 1259 OID 17482)
-- Name: user_role; Type: TABLE; Schema: jwtauth; Owner: -
--

CREATE TABLE jwtauth.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- TOC entry 315 (class 1259 OID 17456)
-- Name: user_user_id_seq; Type: SEQUENCE; Schema: jwtauth; Owner: -
--

CREATE SEQUENCE jwtauth.user_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3200 (class 0 OID 0)
-- Dependencies: 315
-- Name: user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: jwtauth; Owner: -
--

ALTER SEQUENCE jwtauth.user_user_id_seq OWNED BY jwtauth.user_.user_id;


--
-- TOC entry 3024 (class 2604 OID 17527)
-- Name: refresh_token refresh_token_id; Type: DEFAULT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.refresh_token ALTER COLUMN refresh_token_id SET DEFAULT nextval('jwtauth.refresh_token_refresh_token_id_seq'::regclass);


--
-- TOC entry 3022 (class 2604 OID 17472)
-- Name: role role_id; Type: DEFAULT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.role ALTER COLUMN role_id SET DEFAULT nextval('jwtauth.role_role_id_seq'::regclass);


--
-- TOC entry 3020 (class 2604 OID 17461)
-- Name: user_ user_id; Type: DEFAULT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_ ALTER COLUMN user_id SET DEFAULT nextval('jwtauth.user_user_id_seq'::regclass);


--
-- TOC entry 3023 (class 2604 OID 17512)
-- Name: user_device user_device_id; Type: DEFAULT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_device ALTER COLUMN user_device_id SET DEFAULT nextval('jwtauth.user_device_user_device_id_seq'::regclass);


--
-- TOC entry 3191 (class 0 OID 17524)
-- Dependencies: 323
-- Data for Name: refresh_token; Type: TABLE DATA; Schema: jwtauth; Owner: -
--



--
-- TOC entry 3186 (class 0 OID 17469)
-- Dependencies: 318
-- Data for Name: role; Type: TABLE DATA; Schema: jwtauth; Owner: -
--

INSERT INTO jwtauth.role VALUES (1, 'ROLE_USER');
INSERT INTO jwtauth.role VALUES (2, 'ROLE_ADMIN');


--
-- TOC entry 3184 (class 0 OID 17458)
-- Dependencies: 316
-- Data for Name: user_; Type: TABLE DATA; Schema: jwtauth; Owner: -
--



--
-- TOC entry 3189 (class 0 OID 17509)
-- Dependencies: 321
-- Data for Name: user_device; Type: TABLE DATA; Schema: jwtauth; Owner: -
--



--
-- TOC entry 3187 (class 0 OID 17482)
-- Dependencies: 319
-- Data for Name: user_role; Type: TABLE DATA; Schema: jwtauth; Owner: -
--



--
-- TOC entry 3201 (class 0 OID 0)
-- Dependencies: 322
-- Name: refresh_token_refresh_token_id_seq; Type: SEQUENCE SET; Schema: jwtauth; Owner: -
--

SELECT pg_catalog.setval('jwtauth.refresh_token_refresh_token_id_seq', 1, false);


--
-- TOC entry 3202 (class 0 OID 0)
-- Dependencies: 317
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: jwtauth; Owner: -
--

SELECT pg_catalog.setval('jwtauth.role_role_id_seq', 3, false);


--
-- TOC entry 3203 (class 0 OID 0)
-- Dependencies: 320
-- Name: user_device_user_device_id_seq; Type: SEQUENCE SET; Schema: jwtauth; Owner: -
--

SELECT pg_catalog.setval('jwtauth.user_device_user_device_id_seq', 1, false);


--
-- TOC entry 3204 (class 0 OID 0)
-- Dependencies: 315
-- Name: user_user_id_seq; Type: SEQUENCE SET; Schema: jwtauth; Owner: -
--

SELECT pg_catalog.setval('jwtauth.user_user_id_seq', 1, false);


--
-- TOC entry 3029 (class 2606 OID 17466)
-- Name: user_ person_pk; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_
    ADD CONSTRAINT person_pk PRIMARY KEY (user_id);


--
-- TOC entry 3042 (class 2606 OID 17532)
-- Name: refresh_token refresh_token_pk; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.refresh_token
    ADD CONSTRAINT refresh_token_pk PRIMARY KEY (refresh_token_id);


--
-- TOC entry 3044 (class 2606 OID 17534)
-- Name: refresh_token refresh_token_un; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.refresh_token
    ADD CONSTRAINT refresh_token_un UNIQUE (refresh_token_id);


--
-- TOC entry 3033 (class 2606 OID 17477)
-- Name: role role_pk; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.role
    ADD CONSTRAINT role_pk PRIMARY KEY (role_id);


--
-- TOC entry 3035 (class 2606 OID 17479)
-- Name: role role_un; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.role
    ADD CONSTRAINT role_un UNIQUE (role_id);


--
-- TOC entry 3037 (class 2606 OID 17514)
-- Name: user_device user_device_pk; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_device
    ADD CONSTRAINT user_device_pk PRIMARY KEY (user_device_id);


--
-- TOC entry 3039 (class 2606 OID 17516)
-- Name: user_device user_device_un; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_device
    ADD CONSTRAINT user_device_un UNIQUE (user_device_id);


--
-- TOC entry 3031 (class 2606 OID 17481)
-- Name: user_ user_un; Type: CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_
    ADD CONSTRAINT user_un UNIQUE (user_id);


--
-- TOC entry 3040 (class 1259 OID 17557)
-- Name: refresh_token_jti_idx; Type: INDEX; Schema: jwtauth; Owner: -
--

CREATE UNIQUE INDEX refresh_token_jti_idx ON jwtauth.refresh_token USING btree (jti);


--
-- TOC entry 3048 (class 2606 OID 17535)
-- Name: refresh_token refresh_token_fk; Type: FK CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.refresh_token
    ADD CONSTRAINT refresh_token_fk FOREIGN KEY (user_device_id) REFERENCES jwtauth.user_device(user_device_id);


--
-- TOC entry 3049 (class 2606 OID 17541)
-- Name: refresh_token refresh_token_replaced_by_fk; Type: FK CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.refresh_token
    ADD CONSTRAINT refresh_token_replaced_by_fk FOREIGN KEY (replaced_by) REFERENCES jwtauth.refresh_token(refresh_token_id);


--
-- TOC entry 3047 (class 2606 OID 17517)
-- Name: user_device user_device_fk; Type: FK CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_device
    ADD CONSTRAINT user_device_fk FOREIGN KEY (user_id) REFERENCES jwtauth.user_(user_id);


--
-- TOC entry 3045 (class 2606 OID 17497)
-- Name: user_role user_role_fk; Type: FK CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_role
    ADD CONSTRAINT user_role_fk FOREIGN KEY (user_id) REFERENCES jwtauth.user_(user_id);


--
-- TOC entry 3046 (class 2606 OID 17502)
-- Name: user_role user_role_fk_1; Type: FK CONSTRAINT; Schema: jwtauth; Owner: -
--

ALTER TABLE ONLY jwtauth.user_role
    ADD CONSTRAINT user_role_fk_1 FOREIGN KEY (role_id) REFERENCES jwtauth.role(role_id);


-- Completed on 2021-11-05 11:26:04

--
-- PostgreSQL database dump complete
--

