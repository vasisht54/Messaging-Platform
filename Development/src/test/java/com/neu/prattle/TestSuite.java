package com.neu.prattle;

import com.neu.prattle.controller.*;
import com.neu.prattle.dao.DatabaseDAOChatTests;
import com.neu.prattle.db.DatabaseDAOTests;
import com.neu.prattle.db.MongoDBChatTests;
import com.neu.prattle.db.MongoDBTests;
import com.neu.prattle.main.PrattleApplicationTests;
import com.neu.prattle.model.GroupTests;
import com.neu.prattle.model.MessageTests;
import com.neu.prattle.model.TranslatorTests;
import com.neu.prattle.model.UserTests;
import com.neu.prattle.service.ChatAndMessageServiceTests;
import com.neu.prattle.service.UserServiceImplTests;
import com.neu.prattle.websocket.MessageDecoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Run all test suites.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({UserTests.class, GroupTests.class,
        MessageTests.class, MongoDBTests.class, DatabaseDAOTests.class, UserServiceImplTests.class,
        UserControllerTests.class, MessageDecoderTest.class, AppControllerTest.class,
        PrattleApplicationTests.class, GroupControllerTests.class, TranslatorTests.class, MongoDBChatTests.class,
        ChatAndMessageServiceTests.class, DatabaseDAOChatTests.class, LoginTests.class,
        MessageControllerTests.class})
public class TestSuite {
}
