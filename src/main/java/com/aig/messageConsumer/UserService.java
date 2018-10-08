package com.aig.messageConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aig.messageConsumer.bean.UserVO;
import com.aig.messageConsumer.config.ApplicationConfigReader;
import com.aig.messageConsumer.util.ApplicationConstant;

@RestController
@RequestMapping(path = "/userservice")
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final RabbitTemplate rabbitTemplate;
	private ApplicationConfigReader applicationConfig;
	private MessageSender messageSender;
	private MessageListener messageListener;

	public ApplicationConfigReader getApplicationConfig() {
		return applicationConfig;
	}

	@Autowired
	public void setApplicationConfig(ApplicationConfigReader applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	@Autowired
	public UserService(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	@Autowired
	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	@Autowired
	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	@RequestMapping(path = "/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendMessage(@RequestBody UserVO user) {

		String exchange = getApplicationConfig().getAppExchange();
		String routingKey = getApplicationConfig().getAppRoutingKey();

		/* Sending to Message Queue */
		try {
			for (int i = 0; i < 100; i++) {
				user.setUserName("Vinod - " + i);
				messageSender.sendMessage(rabbitTemplate, exchange, routingKey, user);
			}
			return new ResponseEntity<String>(ApplicationConstant.IN_QUEUE, HttpStatus.OK);

		} catch (Exception ex) {
			log.error("Exception occurred while sending message to the queue. Exception= {}", ex);
			return new ResponseEntity(ApplicationConstant.MESSAGE_QUEUE_SEND_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
