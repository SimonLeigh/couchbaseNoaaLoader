###### Way too slow, max 3 connections

````bash
yum install lftp;
lftp  ftp://ftp.ncdc.noaa.gov/pub/data/noaa/;
````
###### Single threaded, too slow to get directory list
````bash
wget -r --no-parent --reject "index.htm*" -X /pub/data/noaa/additional /pub/data/noaa/ISH-DVD2012 /pub/data/noaa/isd-lite /pub/data/noaa/software /pub/data/noaa/updates -c http://www1.ncdc.noaa.gov/pub/data/noaa/
````
````bash
wget --spider --force-html -r -l4 --reject "index.htm*" -I "/pub/data/noaa/19*/, /pub/data/noaa/20*/" http://www1.ncdc.noaa.gov/pub/data/noaa/ 2>&1 \
  | grep '^--' | awk '{ print $3 }' \
  | grep -v '\.\(gz\)$' \
  > urls.txt
````
````bash
wget --spider --force-html -r -l4 --reject "index.htm*" -I "/pub/data/noaa/19*/, /pub/data/noaa/20*/" http://www1.ncdc.noaa.gov/pub/data/noaa/ 2>&1   | grep '^--' | awk '{ print $3 }'   | grep '\.\(gz\)$' > urls.txt
````

##### Success with Python
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

###### Run command for .jar
````bash
java -cp cb-noaa.jar org.couchbase.noaaLoader.support.ishJava ./noaa 84.40.63.62 > cb_upload_log.txt &
````