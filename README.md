# FtcAutoScout

A program that lets you easily scout teams before competitions.

Credit to [team 16668](https://github.com/J-Barta/FTC-Data-Puller) for inspiring us to make this. (And for a bit of the code we weren't sure how to do)

# Usage

To use this program, download the JAR and run it. Then, enter in an event you would like to scout. To find an event, go to [TheOrangeAlliance](https://theorangealliance.org/events) and search for your event. Then look in the URL and copy the event code. It should look something like this:
```
2122-FIM-MRHRU
```
Next, enter in an API key. To do this, go to [TheOrangeAlliance](https://theorangealliance.org/events) and in the top left, create an account if you don't have one already. Then click in that area and generate an API key. After it is generated, copy and paste it into the scouting program.

That's it, just watch as the program scouts your event for you. If your event has 30 or more teams, it will pause after 29 of them and wait until 60 seconds are up. This is because TheOrangeAlliance has a rate limiter of 30 requests every 60 seconds. This pause means that you won't get any incomplete data.
