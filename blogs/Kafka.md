Event Streams : 
    Event - Functionality done by a service/micro-service.
    An application that handles thousands of user requests at peak time and it needs to send 
    the events to multiple services - so those services which recieves this events can do their functionality 
    corresponding to it. 
    And again each of those services may need to send the events to other services - to completely
    fulfill an user need.

The problem with API :: Need of Event Streaming Platform
    1. IF a service needs to send events to 10 services - it uses 10 api requests, like that if 10 services are there
then it would be 10 * 10 api hits for a order. If there are 1k orders per minute then it would cause 
1k * 100 api hits - 100k api hits. - Very Costly & Affects the performance 
    2.  Retries - If a service tries to send data to other service via api - it will need to retry if data is not sent 
properly -either due to client side issue or server side issue. A minimum of 5 retries can occur - 100K * 5 :
            200k hits roughly
    3. Different clients may use different kind of api - like soap, grpc, rest - SO it is additional 
burnout
    4. Client service's ip may change - dns may change - requires additional security - tls

For all of this - an application which has many events and needs to send to many client services and get from other
services - need event stream system instead of apis

KAFKA - A distributed event streaming platform
1. A Kafka Cluster will be created with multiple nodes.
2. Can physically split a topic into multiple partitions
3. Make different consumers to read from different paritions and different producers to
    write to different paritions 
4. If there are different users - services can write data to kafka broker's topic on unique partition for 
    each user. 
5. Topics - Logical part , Parititon - Physical storage unit of a topic 


1. If a producer puts a message in a topic, then only one consumer from a consumer group can connect
 to particular partition of the topic and consumes data at a time - As the contrary will cause data inconsistency
  -> This differentiates mqtt from karfka-( distributed even streaming platform)

   One consumer can connect to two partitions of two different topics
   One partition allows different consumers from different consumer-groups to connect with it, but not 
    consumers of same consumer-group

   Publisher can be configured to publish message to a particular partition of a particular topic, if not 
    then message will go and published in a topic of any partitions - partitions will be allocated by round-robin
   Publisher doesnt know which consumer will read the data

2. How Kafka achieves fault-tolerance ? 
Kafka CLuster can contain multiple kafka brokers which are replicas of each other - in same location
or different geographical location
Each topic of a kafka broker node, will have different parititions.
Paritions can have leader & followers - ie: 
    Partition p1 of topic-1 in kafka broker-1, kafka broker-2 are replicas of paritition p1 in topic-1 in
     kafka broker-0
    Here p1 of broker-0 is leader, and remaining are followers - What we do in leader will be followed by followers
By this if kafka broker-2 is crashed , remaining broker-nodes will have same data as datas are replicated are
equally
ZooKeeper will decide who is leader & follower

3. ZooKeeper will maintain the all meta data of kafka
        Acts as centralized management system - Until kafka 2.7
    After 2.7 - we can manage everything without zookeeper - using Kraft (Kafka Controller)

Kafka helps to achieve Decoupling - All services communicate with each other, but not need to be in same container/tightly coupled/directly communicates
