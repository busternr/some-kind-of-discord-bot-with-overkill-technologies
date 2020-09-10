package com.nikolayromanov.backend;

import com.nikolayromanov.backend.entities.User;
import com.nikolayromanov.backend.models.Message;
import com.nikolayromanov.backend.models.MessageType;
import com.nikolayromanov.backend.models.ResponseErrors;
import com.nikolayromanov.backend.models.StatusCode;
import com.nikolayromanov.backend.models.ValidationError;
import com.nikolayromanov.backend.models.messages.StringMessageBody;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class BackendApplicationTests {
	private final int PORT = 8080;
	private final String URL = "ws://localhost:" + PORT + "/websocket";

	private static final String MESSAGES_SUBSCRIBE_ENDPOINT = "/queue/user";
	private static final String MESSAGES_SEND_ENDPOINT = "/queue/user/messages";

	private final StompSession stompSession = this.createClient();
	private Message replyMessage;
	private Map<String,String> replyHeaders;

	public BackendApplicationTests() throws InterruptedException, ExecutionException, TimeoutException {
	}

	@BeforeEach
	private void beforeEach() {
		this.replyMessage = null;
		this.replyHeaders = null;
	}

	@Test
	public void testMessageNotFound() throws InterruptedException {
		Message message = new Message();
		message.setHeader("type", "not.existing.message");

		sendMessage(message);

		assertReplyHeader("not.existing.message.reply");
		assertStatusCodeHeader(StatusCode.ENDPOINT_NOT_FOUND);
	}

	@Test
	public void testEchoHiMessage() throws InterruptedException {
		sendMessage(createMessage(MessageType.EchoHi, new StringMessageBody("test")));

		StringMessageBody replyBody = this.processMessageBody(replyMessage.getBody(), StringMessageBody.class);

		assertEquals("Hi, test", replyBody.getMessage());
		assertReplyHeader("echo.hi.reply");
		assertStatusCodeHeader(StatusCode.OK);
	}

	@Test
	public void testAuthRegisterMessage() throws InterruptedException {
		final long MIN = 100000000;
		final long MAX = 999999999;
		String randomNumber = Integer.toString ((int) ((Math.random() * (MAX - MIN)) + MIN));
		String username = "testUser" + randomNumber;

		// Successful registration
		sendMessage(createAuthRegisterMessage(username, "password"));

		assertReplyHeader("auth.register.reply");
		assertStatusCodeHeader(StatusCode.OK);

		// Check null username
		sendMessage(createAuthRegisterMessage(null, "password"));

		assertReplyHeader("auth.register.reply");
		assertValidationError("username");

		// Check null password
		sendMessage(createAuthRegisterMessage("username", null));

		assertReplyHeader("auth.register.reply");
		assertValidationError("password");

		// Check username is taken
		sendMessage(createAuthRegisterMessage(username, "password"));

		assertReplyHeader("auth.register.reply");
		assertInternalError("User with the same username already exists.");
	}

	private StompSession createClient() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSession stompSession = stompClient.connect(URL, new StompSessionHandlerAdapter() {}).get(1, SECONDS);
		stompSession.subscribe(MESSAGES_SUBSCRIBE_ENDPOINT, new StompMessageHandler());

		return stompSession;
	}

	private Message createMessage(MessageType messageType, Object body) {
		Message message = new Message(body);
		message.setHeader("type", messageType.getValue());

		return message;
	}

	private Message createAuthRegisterMessage(String username, String password) {
		User user = new User(username, password);

		return createMessage(MessageType.AuthRegister, user);
	}

	private void sendMessage(Message message) throws InterruptedException {
		this.stompSession.send(MESSAGES_SEND_ENDPOINT, message);
		Thread.sleep(1000); // Questionable
	}

	private <T> T processMessageBody(Object messageBody, Class<T> cls) {
		Gson gson = new Gson();

		return gson.fromJson(gson.toJson(messageBody), cls);
	}

	private <T> T processMessageBodyErrors(Object messageBody, Class<T> cls) {
		ResponseErrors<?> replyBody = this.processMessageBody(messageBody, ResponseErrors.class);

		return this.processMessageBody(replyBody.getErrors().get(0), cls);
	}

	private void assertHeader(String expectedValue, String header) {
		assertEquals(expectedValue, this.replyHeaders.get(header));
	}

	private void assertReplyHeader(String value) {
		assertHeader(value, "type");
	}

	private void assertStatusCodeHeader(StatusCode statusCode) {
		assertHeader(statusCode.getValue(), "statusCode");
	}

	private void assertValidationError(String field) {
		ValidationError validationError =  this.processMessageBodyErrors(replyMessage.getBody(), ValidationError.class);

		assertEquals(validationError, new ValidationError(field, ResponseErrors.INVALID));
		assertStatusCodeHeader(StatusCode.VALIDATION_ERROR);
	}

	private void assertInternalError(String error) {
		String internalError =  this.processMessageBodyErrors(replyMessage.getBody(), String.class);

		assertEquals(internalError, error);
		assertStatusCodeHeader(StatusCode.INTERNAL_SERVER_ERROR);
	}

	private class StompMessageHandler implements StompFrameHandler {
		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return Message.class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object object) {
			Message message = (Message) object ;
			replyHeaders = message.getHeaders();
			replyMessage = message;
		}
	}
}