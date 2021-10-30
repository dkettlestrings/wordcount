# Wordcount

A technical-interview-style project that counts word frequency in a document 
and displays the most common words. All code, features, documentation, and 
tests are minimal to reflect the time pressure of an interview situation. No 
excessive effort and time have been put into this project. 

## What It Does

The program is a processing pipeline that consists of the following steps:

1. grab a copy of [Moby Dick](http://www.gutenberg.org/files/2701/2701-0.txt) 
from Project Gutenburg
2. extract all of the words from the text
3. normalize all of the words (i.e. strip out non-characters and make all 
characters lowercase)
4. filter out words from a set of common words (e.g. "the", "of", "to", "and", 
etc.)
5. count the number of occurrences of each word
6. print the 50 most common words

## Build and Test

This project uses [sbt](https://www.scala-sbt.org/download.html) to build. Once 
installed you can run `sbt test` to run the tests. To create the executable JAR, 
run `sbt assembly` which will create an executable JAR in 
`wordcount/target/scala-2.12/wordcount.jar`.

## Running the Executable

The program's configuration is hard-coded so running the JAR takes no inputs: 
simply run as `java -jar wordcount.jar`.

To run on AWS you can simply run the JAR from an EC2 instance. This "low-tech" 
(by today's standards) approach makes sense if you are just trying to hack 
something together, trying out a new program, or simply don't need to run it 
very often. Below I will talk about a more "production-appropriate" mechanism 
for running this application at scale on AWS.

### Running on an EC2 Instance

At a high level this is simply starting up an AMI (Amazon Machine Image), 
installing a JRE and your code on that machine. You can do this with the old 
tools of `ssh`, `apt`, and `scp`.

1. [log into your AWS account](https://signin.aws.amazon.com/)
2. next, launch an EC2 instance. The precise AMI doesn't matter much; any 
free-tier Linux AMI should work but the following steps use `apt`. I suggest 
using `ami-00399ec92321828f5`: an Ubuntu Server image running on x86_64
3. next choose an instance type: you can use the `t2.micro` size since this is 
very lightweight (and I am using the free tier)
4. proceed to "Review and Launch". You will get a scary-looking message about 
your instance being open to the internet because of your default security 
group, but honestly, it's not a big deal (note: I think that might be because 
I am using a free-tier account for this - I'm used to seeing default security 
groups that have more protection)
5. you will be prompted to add a key pair for accessing your instance. If you 
have one already, great, but if not create a new pair, download the `.pem` 
file, store it in your `/.ssh` directory, add it to you key manager, etc.
6. press "Launch Instances"; your instance is now running!
7. click on the link for the instance and find the "Public IPv4 address"
8. go to your terminal and ssh into the container - something like 
`ssh ubuntu@3.21.158.142`
9. it's always good practise to update your package manager: 
`sudo apt update && sudo apt upgrade -y`
10. install the JRE: `sudo apt install default-jre`
11. back in the terminal for your local machine, navigate to the 
`wordcount/target/scala-2.12` directory and do 
`scp wordcount.jar ubuntu@3.21.158.142:~`
12. back over on the EC2 instance simply run the JAR as you would on your local 
machine: `java -jar wordcount.jar`

### An Alternative Setup

The process above has many manual steps. If you are comfortable with light 
system administration and you need to run this job very infrequently, then the 
Ec2 setup is probably optimal since it is lightweight and has fewer 
dependencies. However, if you need to scale the deployment of this application 
then there are better solutions on AWS.

One such setup would be to create a Docker image with the application bundled 
inside. SBT has a 
[standard plugin](https://www.scala-sbt.org/sbt-native-packager/formats/docker.html) 
for supporting Docker image creation. Next, you would publish that image on 
AWS's ECR (Elastic Container Registry): think of it as essentially your own 
DockerHub. From there you have **LOTS** of options in the container 
orchestration market, but if you are tied to AWS already you can just use ECS 
(Elastic Container Service). However, every team I have ever seen has done 
container orchestration very differently, so it really depends on what kind of 
infrastructure you have in place.

