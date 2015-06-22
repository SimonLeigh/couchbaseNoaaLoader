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