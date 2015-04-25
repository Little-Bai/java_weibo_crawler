## Sina Weibo Label Crawler

I wrote this program for getting some data for tranining a `Multi Label Classification Algorithm Based on Random Walk Model`.

There is a list named `screen_name.txt` which used as a `to crawl list`. This program will get the user's tags and weibo contents then store them in mysql.

Due to the sina API limitation, this program should be run for several times by hands to continue crawing. And of course you can let this program sleep for a while to waitting the re-authorization of Sina Weibo API or write and run a shell script to run this program once an hour. That will liberate your hands.

Maybe I will rewrite this project when I need to use it some day. But I do not suggest you guys to use it for learning.
