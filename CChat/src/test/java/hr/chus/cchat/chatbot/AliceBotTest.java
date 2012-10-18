package hr.chus.cchat.chatbot;

import junit.framework.Assert;

import org.aitools.programd.Core;
import org.aitools.programd.bot.Bot;
import org.aitools.programd.util.XMLKit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class AliceBotTest {

    @Autowired
    private transient Core core;

    private transient Bot  bot;

    @Before
    public void setup() {
        bot = core.getBots().getABot();
    }

    @Test
    public void simpleTest() {
        final String user = "user_id";

        // Assert.assertEquals(getResponse("Did I cause THIS hazzard", user), "Don't blame yourself.");
        Assert.assertEquals("Eliza is so cool. Eliza for President!", getResponse("I LIKE ELIZA", user));
        Assert.assertEquals("My name is TestBot.", getResponse("what is your name?", user));
        Assert.assertEquals("Hi there!", getResponse("hello!", user));
        Assert.assertEquals("Hi there!", getResponse("hello !", user));
        Assert.assertEquals("Hi there!", getResponse("  hello    !", user));
        Assert.assertEquals("Ishmael, have you slain any whales lately?", getResponse("call me Ishmael", user));
        Assert.assertEquals("Once more? Hi there!", getResponse("hello again!", user));
        Assert.assertEquals("Salutations, Ishmael", getResponse("hello there", user));
    }

    private String getResponse(String p_msg, String p_user) {
        final String response = core.getResponse(p_msg, p_user, bot.getID());
        return XMLKit.filterWhitespace(XMLKit.removeMarkup(response));
    }

}
