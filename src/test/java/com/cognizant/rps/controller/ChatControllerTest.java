package com.cognizant.rps.controller;
import com.cognizant.rps.RpsApplication;
import com.cognizant.rps.models.Match;
import com.cognizant.rps.models.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.transaction.annotation.Transactional;

//import javax.servlet.http.HttpSession;

import static com.cognizant.rps.models.Status.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@DirtiesContext(classMode= DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = RpsApplication.class)
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ChatControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
    @Autowired
    private ChatController cc;

//    @BeforeEach
//    public void resetDB() {
//        pr.deleteAll();
//    }

    private final ObjectMapper om = new ObjectMapper();

//    public ChatControllerTest(ChatController cc) {
//        this.cc = cc;
//    }

    @Test
    public void receiveJOINMessageTest() throws Exception{
        Match d = new Match();
        Message m = new Message("a","b",d,JOIN);
        Message test = cc.receiveMessage(m);
        assertEquals("b",test.getReceiverName(),"pass");
        assertEquals("a",test.getSenderName(),"pass");
        assertEquals(d,test.getMessage(),"pass");
        assertEquals(JOIN,test.getStatus(),"pass");


    }

    @Test
    public void receiveMESSAGEMessageTest() throws Exception{
        Match d = new Match();
        Message m = new Message("a","b",d,MESSAGE);
        Message test = cc.receiveMessage(m);
        assertEquals("b",test.getReceiverName(),"pass");
        assertEquals("a",test.getSenderName(),"pass");
        assertEquals(d,test.getMessage(),"pass");
        assertEquals(MESSAGE,test.getStatus(),"pass");


    }

    @Test
    public void receiveLEAVEMessageTest() throws Exception{
        Match c = new Match();
        Message m = new Message("a","b",c,LEAVE);
        Message test = cc.receiveMessage(m);
        assertEquals("b",test.getReceiverName(),"pass");
        assertEquals("a",test.getSenderName(),"pass");
        assertEquals(c,test.getMessage(),"pass");
        assertEquals(LEAVE,test.getStatus(),"pass");


    }

    @Test
    public void recMessageTest() throws Exception{
        Match c = new Match();
        Message m = new Message("a","b",c,MESSAGE);
        Message test = cc.recMessage(m);
        assertEquals("b",test.getReceiverName(),"pass");
        assertEquals("a",test.getSenderName(),"pass");
        assertEquals(c,test.getMessage(),"pass");
        assertEquals(MESSAGE,test.getStatus(),"pass");


    }

}
