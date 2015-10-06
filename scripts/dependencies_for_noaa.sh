sudo apt-get install software-properties-common python-software-properties python-pip &&
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 627220E7 &&
echo 'deb http://archive.scrapy.org/ubuntu scrapy main' | sudo tee /etc/apt/sources.list.d/scrapy.list &&
sudo apt-get update && sudo apt-get install scrapy &&
sudo apt-get install openssl;
sudo pip install service-identity &&
sudo pip install --upgrade pyasn1 &&
sudo apt-get install parallel &&
sudo add-apt-repository ppa:webupd8team/java &&
sudo apt-get update &&
sudo apt-get install oracle-java8-installer &&
java -version;