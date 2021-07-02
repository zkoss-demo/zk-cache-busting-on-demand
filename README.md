# ZK Cache Busting on Demand Example
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Example project for [ZK Small Talk - Cache Busting on Demand](https://www.zkoss.org/wiki/Small_Talks/2021/July/Cache_Busting_On_Demand) 

## Useful Build Commands

**run example**
```
mvn jetty:run
```
**run for development (MD5 cache disabled)**
```
mvn jetty:run -Dzk.example.cachebust.MD5HashInit.cacheEnabled=false
```
**open in browser**

http://localhost:8080/

**build war**
```
mvn clean package
```
