package com.ijrobotics.ijschoolnotificationservice.client;

import com.ijrobotics.ijschoolnotificationservice.domain.enumeration.NotificationType;
import com.ijrobotics.ijschoolnotificationservice.service.NotificationService;
import com.ijrobotics.ijschoolnotificationservice.service.dto.NotificationDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Component
public class NotificationConsumerThread implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    NotificationService notificationService;
    private String topic="Notifications";
    private String groupId="NotificationsGroup";
    private String bootstrapServer="127.0.0.1:9092";

    public NotificationConsumerThread() {
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
    {
        this.run();
    }

    public void run() {

        Logger logger = LoggerFactory.getLogger(NotificationConsumerThread.class.getName());
        logger.info("***************NOTIFICATION CONSUMER STARTED***************");
        //latch for dealing with multiple threads
        CountDownLatch latch = new CountDownLatch(1);

        //Create the consumer runnable
        logger.info("Creating the consumer Thread");
        ConsumerRunnable myConsumerRunnable = new ConsumerRunnable(this.topic, this.bootstrapServer, this.groupId, latch,notificationService);

        //start the thread
        Thread myThread=new Thread(myConsumerRunnable);
        myThread.start();

        //add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(
                ()->{
                    logger.info("Caught shutdown hook");
                    myConsumerRunnable.shutdown();
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("app has exited");
                }
        ));

//        try {
//            latch.await();
//        }catch (InterruptedException e){
//            logger.error("Application got interrupted", e);
//        }finally {
//            logger.info("Application is closing");
//        }

    }



    public static class ConsumerRunnable implements Runnable {


        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class.getName());
        private NotificationService notificationService;

        ConsumerRunnable(String topic, String bootstrapServer, String groupId, CountDownLatch latch,NotificationService notificationService) {
            this.latch = latch;
            this.notificationService=notificationService;
            Properties properties = new Properties();
            properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            this.consumer = new KafkaConsumer<String, String>(properties);
            this.consumer.subscribe(Arrays.asList(topic));
        }

        @Override
        public void run() {
            //poll for new Data
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        if(!record.value().isEmpty()){
                            String[] notificationInfo =record.value().split(",");
                            //Get the guardians
                            ArrayList<String> guardiansIds = new ArrayList<>(Arrays.asList(notificationInfo).subList(1, notificationInfo.length - 5));
                            //Get the StudentName
                            String studentName= notificationInfo[notificationInfo.length-4];
                            //Get the SubjectName
                            String subjectName= notificationInfo[notificationInfo.length-2].replace("]","");
                            //get the type of notification
                            String notificationType= notificationInfo[notificationInfo.length-1];
                            guardiansIds.forEach(guardiansIds1->{
                                NotificationDTO notificationDTO=new NotificationDTO();
                                notificationDTO.setCreationDate(ZonedDateTime.now());
                                notificationDTO.setTitle(notificationType);
                                notificationDTO.setDescription(studentName+" entered to "+subjectName+" class ");
                                notificationDTO.setNotificationType(NotificationType.valueOf(notificationType));
                                notificationDTO.setWatched(false);
                                notificationService.sendNotification(guardiansIds1,notificationDTO);
                            });
                        }
                    }
                }
            } catch (WakeupException e) {
                logger.info("Received Shutdown Signal");
            } finally {
                consumer.close();
                //tell our main code we are done with the consumer
                latch.countDown();
            }
        }

        public void shutdown() {
            //special method to interrupt consumer.poll
            //it will throw exception WakeUpException
            consumer.wakeup();
        }
    }
}
