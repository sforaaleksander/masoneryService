package com.codecool.masonrysystem.dao;

import com.codecool.masonrysystem.exception.ElementNotFoundException;
import com.codecool.masonrysystem.helper.IdGenerator;
import com.codecool.masonrysystem.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

class SessionDaoTest {
    static SessionDao sessionDao;
    static Session session;

    @BeforeAll
    public static void init() {
        sessionDao = new SessionDao();
    }

    @BeforeEach
    public void createMockSesion(){
        session = getMockSession();
    }

    @AfterEach
    public void nullyfyMockSession(){
        session = null;
    }

    public static Session getMockSession(){
        String sessionId = (new IdGenerator()).generateId(16);
        Session session = mock(Session.class);
        stub(session.getSessionId()).toReturn(sessionId);
        stub(session.getUserId()).toReturn(13L);
        return session;
    }

    @Test
    void insert() throws SQLException {
            assertTrue(sessionDao.insert(session));
            assertEquals(13, sessionDao.getById(session.getSessionId()).getUserId());
            sessionDao.delete(session.getSessionId());
    }

    @Test
    void getAll() throws SQLException {
            HashSet<Session> sessions = new HashSet<Session>();
            sessionDao.getAll().stream().forEach(s -> {
                assertNotNull(s.getUserId());
                assertNotNull(s.getSessionId());
                assertFalse(sessions.contains(s));
                sessions.add(s);
            });
    }

    @Test
    void getById() throws SQLException {
            sessionDao.insert(session);
            assertEquals(13, sessionDao.getById(session.getSessionId()).getUserId());
            sessionDao.delete(session.getSessionId());
    }

    @Test
    void create() throws SQLException {
        String sessionId = session.getSessionId();
        long userId = session.getUserId();
            ResultSet resultSet = mock(ResultSet.class);
            stub(resultSet.getString("session_id")).toReturn(sessionId);
            stub(resultSet.getLong("user_id")).toReturn(13L);
            Session createdSession = sessionDao.create(resultSet);
            assertEquals(userId, createdSession.getUserId());
            assertEquals(sessionId, createdSession.getSessionId());
    }

    @Test
    void update() {
        assertFalse(sessionDao.update(session));
    }

    @Test
    void delete() throws SQLException {
        sessionDao.insert(session);
        sessionDao.delete(session.getSessionId());
        assertThrows(ElementNotFoundException.class, () -> sessionDao.getById(session.getSessionId()));
    }
}