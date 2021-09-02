# rfid-music-player
A RFID based jukebox, running on a raspberry pi that connects to Sonos

![image](https://user-images.githubusercontent.com/831457/131868263-39efd503-bf63-4821-8b12-a6bcb1d3fc19.png)

## Problem
Throughout the years we've lost some physical aspect of playing music. Back in the 70s, 80s, and 90s, it would take a conscious effort to select a record/tape/CD to play. You had to flip through the albums you had, find one, and actually load the album. Today, we just fire up spotify and hit play. While theres no arguing things are easier, I personally have lost some of that magic of discovering new albums. I tend to stick to my favorites and play them almost exclusively.

What if we could recreate the "magic" of finding and playing albums through spotify?

Further, I spent a lot of money on Sonos speakers throughout my house. Can I somehow integrate Sonos into this system?

## Solution
I liked the idea of RFID cards as a physical way to represent an album. If youre not familiar, a RFID card is a small card that contains a chip. When you scan the card, it reports some unique identifier back.

So I started there. I looked around the internet and saw there were a few folks doing something similar, but nothing quite fit my needs. Plus, I thought how much work could it be to read a card and tell the sonos API to play?

I knew I wanted:
- a RFID based system, so I needed RFID cards and a RFID reader. I chose a cheap RFID reader, specifically this one: https://smile.amazon.com/gp/product/B07FCLY4S9, though you can find them much cheaper on ebay.
- Some screen to display the album that's playing. I selected a HyperPixel Square (non-touch): https://smile.amazon.com/HyperPixel-4-0-Square-Raspberry-Non-Touch/dp/B08259FS1F
- Integration with Sonos speakers within my house
- Some enclosure to wrap it all up (luckily I have a Prusa mk3s)

Luckily, I knew there were existing APIs for Sonos and spotify. After receiving the reader and screen, I decided to see what I could hack together in a weekend.

I first attempted to use some open source linux tools, like rfdump, a few others, but to be honest they all kind of sucked. I had problems getting my cheap RFID scanner actually reading anything at all, and it was very hit or miss. Sometimes it'd scan, sometimes it wouldnt. Admittedly, I spent like 10 hours trying to get these tools working.

Then I decided, you know, Java probably has some library for this. If I go with Java, I can leverage Spring Boot, and tap into Hibernate ORM for storage of the cards. Sure enough, the ability to scan RFID cards using my scanner just worked in Java. After some "hello world" level validation, I started hacking together a solution in spring boot.

After about 8 hours, I had a RFID reading app, that had some real basic Bootstrap UI, that let me scan cards and link the card UUID to a spotify album UUID. Toss in some rules around volume, grouping, etc. And my 8 hour app has been running flawlessly for 6 months.

I still had the problem of "how do I display album art on the screen?", luckily I read an article on hackaday that pointed me to: https://www.hackster.io/mark-hank/sonos-album-art-on-raspberry-pi-screen-5b0012

And just like that, I could scan a RFID card, and have the album play throughout my house.

## Bill of materials
TBA



## Install guide
There is an assumption you are familiar with linux, java, node, etc. If you've never written a bash script, this guide is probably not for you.

### Step 1. Install sonos-http-api & music-screen-api on your Raspberry Pi
I'd recommend following this guide here: https://www.hackster.io/mark-hank/sonos-album-art-on-raspberry-pi-screen-5b0012

After, you'll have the necessary sonos API running, along with the ability to display album art on your hyperpixel.

### Step 2. Clone this repo and run the java app
You can use maven to fetch the application dependencies, run something like `mvn clean install`
After, you'll have a `target/` directory that contains the fat jar. You can then run `java -jar jukebox-0.0.1-SNAPSHOT.jar`.

## Customization
You must provide a spotify clientID and clientSecret to the application. See https://developer.spotify.com/documentation/general/guides/authorization-guide/ on how to get this clientId/clientSecret

Here is the available settings you can adjust. You can find this under `src/main/resources/application.yml`

```
server:
  port : 8081

spring:
  datasource:
    url: jdbc:h2:file:./data/jukebox
    driverClassName: org.h2.Driver
    username: sa
    password: password
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update

# You must provide a clientId and clientSecret
spotify:
  clientId: na
  clientSecret: na

sonos:
# Define the speakers you want the album to play on
  mainSpeaker: Office
  speakerGroup: Alex's PC, Bedroom, Half Bathroom, Kitchen, Living Room, Master Bathroom
  volume: 25
  setVolume: true
```

## Print an enclosure
I've included the stl's and the 123d design files I used to create the enclosure. I printed these on a Prusa mk3s in PLA. See the files under `3dmodels/`

I also have included the card holders/trays that I designed.

![image](https://user-images.githubusercontent.com/831457/131878003-2aec6eb1-3b4a-49df-aabb-e415d7c2e731.png)



## Using the application
After you have everything up and running, you can connect to the application on the default port (http://your-raspi:8081). This will show an interface similar to:

![image](https://user-images.githubusercontent.com/831457/131877450-5f689881-d110-4cc4-bb7c-eda084b83336.png)

This is essentially the view of the data. As you add albums, this view will obviously change.

### Creating labels
I wanted to display album art on each card. I found some Avery labels template `Presta 94238`, 2" x 3-1/2" Rectangle Labels that seemed to mostly fit the RFID cards. Avery also has a free template editor that I used to actually design the labels (https://app.print.avery.com/)

### Adding a card to the application
In order to link a RFID card to an album, you must first scan the card. Doing so will create an audible beep from the RFID reader. At this point, a blank entry in the application should appear, like:

![image](https://user-images.githubusercontent.com/831457/131878785-5a5d986b-9f34-4f0d-a6e4-5fed9a59687d.png)

You can then hit "edit" on that record, and find the spotify album for this card. 

![image](https://user-images.githubusercontent.com/831457/131878971-bed7861d-74fa-4682-883c-4270dd7cce1d.png)

Hit save, and that card is ready to play!






Thanks for reading!
