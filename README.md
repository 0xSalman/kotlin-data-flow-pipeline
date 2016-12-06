# Waldo Assignment
A simple app that reads photos from amazon S3 storage, extracts EXIF data and stores in mongodb (embedded). 
Technology stack is Kotlin + SpringBoot + MongoDB

## Requirements
- make (http://www.gnu.org/software/make/)
- gradle
- java 8
- (Windows Only) set $HOME environment variable. This is used to specify logging dir

## Getting Started

### Running with make
1. `make -B build`
2. `make run`

### Running without make
1. `gradle clean build`
2. `java -jar ./build/libs/assignment-1.0.jar`

## Design/Architect

Used event driven, single responsibility and separation of concern architecture to build a resilient 
and performant data flow pipeline. Each step in the pipeline can be a microservice on its own and even have 
its own retry and exception queues etc. 

When a step throws an error, the event is retried 5 times by the current step. Otherwise, it goes back to previous step
or exception queue depending on whether previous step was already retried or not. 

Take a look at `design-diagram` for an overview of design

## Notes/Caveats 

1. Solution is not perfect - most likely has bugs
2. Not very familiar with S3 storage so did not make use of `eTag` and `modifiedDate` attributes in the bucket list
3. Due to using internal producer/listener, the code is not decoupled 
4. Data is not persisted forever since using embedded db
5. No unit/integration tests (was trying not to spend more than few hours)
6. Did not plan to use SpringBoot but ran into some issues with number of connections to db when using multi threading
7. In real world, the in-memory producer & listener can be replaced with different implementation. Maybe Kafka or RabbitMQ