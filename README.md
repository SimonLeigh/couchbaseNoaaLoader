##### Success with Python

First of all, you need scrapy, so install it with:

````bash
pip install scrapy
````

###### spider.py
````python
from scrapy.selector import HtmlXPathSelector
from scrapy.spider import BaseSpider
from scrapy.http import Request

DOMAIN = 'www1.ncdc.noaa.gov/pub/data/noaa/'
URL = 'http://%s' % DOMAIN

class MySpider(BaseSpider):
    name = DOMAIN
    allowed_domains = [DOMAIN]
    start_urls = [
        (URL + str(year)) for year in range(1901,2016)
    ]

    def parse(self, response):
        hxs = HtmlXPathSelector(response)
        for url in hxs.select('//a/@href').extract():
            if not url.startswith('http://'):
                url= response.url + url
            request = Request(url=url,callback=self.parse)
            print url
            yield request
````

###### 100 connections to download files.
````bash
  scrapy runspider spider.py 2>&1 | grep '^http' | grep '\.gz$' > urls.txt;
  aria2c -i urls.txt -j 100;
````

It's possible to use gnu-parallel or aria2c to parallelise the download. Aria is sometimes more difficult to install, here are the two options:

## wget with gnu-parallel
````bash
cat urls.txt | parallel -j 100 --gnu "wget {}"
````

## aria2
````bash
  aria2c -i urls.txt -j 100;
````

###### Run command for .jar to insert station metadata
````bash
java -cp cb-noaa.jar org.couchbase.noaaLoader.Application {path/to/isdhistory.csv} {CLUSTERIP} {BUCKET}
````

###### Run command for .jar to insert samples
````bash
java -cp cb-noaa.jar org.couchbase.noaaLoader.support.ishJava ./noaa 84.40.63.62 > cb_upload_log.txt &
````