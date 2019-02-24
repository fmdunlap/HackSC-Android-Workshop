# HackSC:Android Native Workshop - TrivHack!
  - [Prerequisites](#prerequisites)
    - [Getting Started](#getting-started)
  - [Built With](#built-with)
  - [The Actual Workshop](#the-actual-workshop)
    - [Video](#video)
    - [Slides](#slides)
  - [Contributing](#contributing)
  - [Me!](#me)
  - [License](#license)
  - [Acknowledgments](#acknowledgments)

TrivHack is a simple, single activity application that allows the user to answer random trivia questions. Each question that the user gets correct will increment their score by one! How exciting! :D

<div style="text-align:center">
    <img src="https://i.imgur.com/iGOu4DO.png?1" />
    <br/>
    <p> Wow. What an intensely difficult question. </p>
</div>

This repo contains all of the code, as well as a second branch "mvp" that contains a version of the same app that uses the Model-View-Presenter design pattern (which I consider current best practices for native Android in 2019.)

The app itself is built using Andriod Native (*Java\**) with Retrofit to handle the API communication with OpenTrivia DB.

\*I know what you're thinking... "But, Forrest! It's 2019! Why aren't you using Kotlin? Or Flutter? Or insertMobileFrameworkOrTechnologyHere()?" Well, frankly,it's because my audience (y'all) don't know Kotlin, or Dart, or whatever else. Because of good ol' CSCI 201, I know at least some of y'all have experience with Java. So Java wins! As a note here, I definitely *do* recommend learning some of these other mobile technologies. At the very least, go check out Kotlin. It's used widely in industry for Android Native these days - particularly in startups.

## Prerequisites

There are a few things you need installed before we can get started.

First and foremost, is [Android Studio](https://developer.android.com/studio/install). Android studio is an implementation of JetBrain's IntelliJ IDE with a *TON* of extra features added in to make your Android Dev life easier. It has all kinds of [advanced profiling tools](https://developer.android.com/studio/profile/android-profiler), [logging tools](https://developer.android.com/studio/debug/am-logcat), and a (sometimes) intuitive [layout editor](https://developer.android.com/studio/write/layout-editor).

We didn't get into the details of most of the above during the workshop, but I encourage you to go take a peak at all of those if you're interested.

Next up is getting a device set up in Android Studio. To do that, follow [this guide](https://developer.android.com/studio/run/managing-avds) **OR** if you want to launch/install the app on your Android phone, follow [this guide.](https://developer.android.com/training/basics/firstapp/running-app)

### Getting Started

All you need to do to get started is to clone this repository, and open it up in Android studio. Press Shift+F10, select your emulator (or your phone), and you're off to the races!

*I'll be adding more verbose tutorials of the what/how/why of the app (and the MVP version!) in the coming few days, so check back soon!*

## Built With

I think the most important thing for me to mention here is [Retrofit](https://square.github.io/retrofit/) - Retrofit is a really awesome way to turn an HTTP API into a Java interface. Effectively, that means that it abstracts away all of the annoying aspects of dealing with APIs - the networking, the URL parsing, the response decoding, etc - and replaces them with a Java object that makes our network calls ***easy*** and ***fun.***

Here's [another tutorial](https://www.vogella.com/tutorials/Retrofit/article.html) because, tbh, retrofit can be confusing for first-timers.

## The Actual Workshop

### Video
[TrivHack Video Tutorial](https://www.youtube.com/watch?v=SLA5h747OXI&list=PLB7tCTXBff5zv57WBpSDvzGUR-xMNFnkK)

### Slides
[Slides from Hack Night](https://docs.google.com/presentation/d/1XIzKVKPzBSlt9BRzoWFcucF5LsIp64RZjRu2LDvvGT4/edit?usp=sharing)

## Contributing

There are definitely a few bugs in my code. Tbh, I wrote this thing the night before my workshop, soooooo if you find something that you want to fix, feel free to submit a PR! (For example, the darn cardview not expanding to hold the question text properly 😤. That's a problem for later.)


## Me!

**Forrest Dunlap** - [My Github](https://github.com/fmdunlap), [My Linkedin](https://www.linkedin.com/in/forrest-dunlap/) 

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Many thanks to the HackSC Special Projects team for all the help putting this together!
