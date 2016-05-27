import scrapy
from scrapy.spiders.init import InitSpider
from scrapy.http import Request, FormRequest
import csv
import datetime

class GumtreeItem(scrapy.Item):
    # define the fields for your item here like:
    url = scrapy.Field()
    name = scrapy.Field()
    phone = scrapy.Field()
    saleOrRent = scrapy.Field()
    dateScraped = scrapy.Field()
    notes = scrapy.Field()

class GumtreeSpider(InitSpider):
    name = 'gumtree'
    start_urls = ['http://www.gumtree.com.au/s-land-for-sale/c20031']
    login_page = 'https://www.gumtree.com.au/t-login-form.html'
    base_url = 'https://www.gumtree.com.au'
    # allowed_domains = ['example.com']

    def init_request(self):
        """This function is called before crawling starts."""
        print "init_request=============="
        return Request(url=self.login_page, callback=self.login)

    def login(self, response):
        """Generate a login request."""
        print "login=============="
        return FormRequest.from_response(response,
                    formdata={'loginMail': 'xxx@gmail.com', 'password': 'xxx'},
                    callback=self.check_login_response)

    def check_login_response(self, response):
        """Check the response returned by a login request to see if we are
        successfully logged in.
        """
        print "check login=============="

        if "Sign out" in response.body:
            print "huhuhuhuhuh"
            self.log("Successfully logged in. Let's start crawling!")
            # self.driver = webdriver.Chrome()
            # Now the crawling can begin..
            # f = open("linkedinIds.csv", "r")
            # ids = [url.strip() for url in f.readlines()]
            # f.close()
            # for id in self.ids:
            #     url = 'https://www.linkedin.com/vsearch/p?f_CC=%s&trk=rr_connectedness' % id
            #     print "url=======%s" % url
            #     yield scrapy.Request(url, self.parsess)

            # land for sale
            request = Request('http://www.gumtree.com.au/s-land-for-sale/c20031', self.parseGumtree, meta={'saleOrRent' : 'sale'})
            request = self.updateRequestHeaders(request);
            yield request
            # yield Request('https://www.linkedin.com/vsearch/p?f_CC=3530383&trk=rr_connectedness', self.parsess)
            self.initialized()

        else:
            self.log("@@@@ Invalid login!!!")
            # Something went wrong, we couldn't log in, so nothing happens.

    # def start_requests(self):
    #     with open('abn_parsed_urls.csv', 'rbU') as csv_file:
    #         data = csv.reader(csv_file, delimiter=",")
    #         abnItems = []
    #         for row in data:
    #             abnItem = AbnItem(abn=row[0], url=row[1], email=[2])
    #             abnItems.append(abnItem)
    #             yield scrapy.Request(abnItem['url'], callback=self.parse, meta={'item' : abnItem})

        # yield scrapy.Request('http://www.example.com/1.html', self.parse)
        # yield scrapy.Request('http://www.example.com/2.html', self.parse)
        # yield scrapy.Request('http://www.example.com/3.html', self.parse)

    def updateRequestHeaders(self, request):
        request.headers['User-Agent'] = (
            'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, '
            'like Gecko) Chrome/45.0.2454.85 Safari/537.36')

        # set 100 results per page
        request.cookies['up'] = '%7B%22ln%22%3A%22532176319%22%2C%22sps%22%3A%22100%22%2C%22ls%22%3A%22l%3D0%26c%3D20031%26r%3D0%26sv%3DLIST%26sf%3Ddate%22%2C%22lbh%22%3A%22l%3D0%26c%3D20031%26r%3D0%26sv%3DLIST%26sf%3Ddate%22%7D'
        return request

    def getResponseCookies(self, response):
        print "response cookies====", response.headers.getlist('Set-Cookie') #[0].split(";")[0].split("=")[1]

    def parseGumtree(self, response):
        self.getResponseCookies(response)

        with open('gumtree.html', 'a') as f:
		    f.write(response.body)

        current_date = datetime.datetime.now()
        saleOrRent = response.meta['saleOrRent']
        print len(response.xpath('//ul[@id="srchrslt-adtable"]/li'))
        for ads in response.xpath('//ul[@id="srchrslt-adtable"]/li'):

            # ads.xpath('./div[@class="itemOffered"]//span[@class="rs-ad-attributes-forsaleby_s"]/text()').extract()[0]

            urlArr = ads.xpath('div[@itemprop="itemOffered"]//div[contains(@class, "rs-ad-thumbimage")]//a[@itemprop="url"]/@href').extract()

            if len(urlArr):
                print 'invalid url'
                continue
            fullUrl = (self.base_url + urlArr[0])
            # fullUrl = self.__to_absolute_url(self.base_url, ads.xpath('div[@itemprop="itemOffered"]//div[contains(@class, "rs-ad-thumbimage")]//a[@itemprop="url"]/@href').extract())
            # fullUrl = self.base_url + ads.xpath('div[@itemprop="itemOffered"]//div[contains(@class, "rs-ad-thumbimage")]//a[@itemprop="url"]/@href').extract()[0]

            gumtreeItem = GumtreeItem(url=fullUrl, name="", phone="", saleOrRent=saleOrRent, dateScraped=current_date, notes="")
            yield gumtreeItem
        # print response.xpath('//span[@id="ctl00_TemplateBody_WebPartManager1_gwpciCharityDetails_ciCharityDetails_Email"]/a/text()').extract()[0]
        # for h3 in response.xpath('//h3').extract():
        #     yield MyItem(title=h3)
        #
        # for url in response.xpath('//a/@href').extract():
        #     yield scrapy.Request(url, callback=self.parse)
        return

    def __to_absolute_url(self, base_url, link):
        '''
        Convert relative URL to absolute url
        '''

        import urlparse
        link = urlparse.urljoin(base_url, link)
        return link
