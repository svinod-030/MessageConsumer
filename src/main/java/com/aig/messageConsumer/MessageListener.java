package com.aig.messageConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.aig.messageConsumer.config.ApplicationConfigReader;
import com.aig.messageConsumer.util.ApplicationConstant;

@Service
public class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);
    
    @Autowired
    ApplicationConfigReader applicationConfigReader;
    
    /**
     * Message listener for app
     * @param User details a user defined object used for deserialization of message
     */
    @RabbitListener(queues = "${app.queue.name}")
    public void receiveMessageForApp1(final Object data) {
    	log.info("Received message: {} from app queue.", data);
    	//Need to send this retrieved data to GATEWAY API and need to send it back to rabbitMQ
    	
    	//Below logic is needed if we want to make REST API call.... otherwise, we can remove below snippet
    	
    	try {
    		//log.info("Making REST call to the API");
    		//TODO: Code to make REST call
        	//log.info("<< Exiting receiveMessageForApp1() after API call.");
    	} catch(HttpClientErrorException  ex) {
    		
    		if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
        		log.info("Delay...");
        		try {
    				Thread.sleep(ApplicationConstant.MESSAGE_RETRY_DELAY);
    			} catch (InterruptedException e) { }
    			
    			log.info("Throwing exception so that message will be requed in the queue.");
    			throw new RuntimeException();
    		} else {
    			throw new AmqpRejectAndDontRequeueException(ex); 
    		}
    		
    	} catch(Exception e) {
    		log.error("Internal server error occurred in API call. Bypassing message requeue {}", e);
    		throw new AmqpRejectAndDontRequeueException(e); 
    	}
    }
}
