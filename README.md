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

