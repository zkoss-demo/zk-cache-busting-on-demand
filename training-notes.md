# ZK Cache Busting on Demand Example
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Original example project for [ZK Small Talk - Cache Busting on Demand](https://www.zkoss.org/wiki/Small_Talks/2021/July/Cache_Busting_On_Demand) - 
Edited for the public training session: "Cache Busting in ZK - Aug 4th 2021"

* Summarize Built-In Methods/Features
* Implement a custom URL encoding using a content hash (live coding)
* Verify the results
* Discuss Improvements & Limitations

## Built-In Methods/Features

**Change ZK Version Info**
Library Property [org.zkoss.zk.ui.versionInfo.enabled](https://www.zkoss.org/wiki/ZK_Configuration_Reference/zk.xml/The_Library_Properties/org.zkoss.zk.ui.versionInfo.enabled)
(zk.xml, command line, system property)
* +effective / +simple 
* -brute force / -inefficient

**Javascript Module** 
(lang-addon.zul `<javascript-module name="my_mod" version="x.y.z">`)
* +per module (good for independent release cycles)
* -limited to JS files

**Manual revving** via url parameters (?v=xyz)
* +simple / +non invasive
* -hard to maintain / -doesn't work everywhere

**Setting Cache Headers**
* -complicated (server and browser specific) 
* -additional requests (304 unchanged)
* ...good luck ;)

## Automatic Revving in the URL (path/filename)
**Custom UrlEncoder** 
e.g. [zk.example.cachebust.GlobalUrlEncoder](src/main/java/zk/example/cachebust/GlobalUrlEncoder.java)
* +full control
* -can only configure one / -also custom URL handling required

**Custom ExtendletContext**(s)
* +multiple extensions possible / +usage on demand / +leverage built-in Class Web Resource (CWR) mechanism
* -slighly less intuitive/convenient to implement 

**quick demo** [zk.example.cachebust.MD5HashInit](src/main/java/zk/example/cachebust/MD5HashInit.java)

http://localhost:8080/index.zul

**live code** [zk.example.cachebust.SHA1HashInit](src/main/java/zk/example/cachebust/SHA1HashInit.java)

http://localhost:8080/testnew.zul

* register an ExtendletContext in a WebAppInit listener
  * simplify interface
  * dummy implementation (doing nothing / hard coding URL path /zkau/web/)
  * add version token to URL (prefix `"/_zv"`)
* compute the content hash (SHA-1)
  * locate the actual file (`Servlets.getExtendletContext`, `cwrExtendletContext.getResourceAsStream`)
  * digest input stream into hash bytes
  * base64 encode bytes for URL
* reuse existing ZK features
  * locate I18N and browser specific version (`Servlets.locate(...)`)
  * encode with default CWR-Extendlet (`cwrExtendletContext.encodeURL`)
  * replace ZK's version token (`webManager.getClassWebResource`, `cwr.encodeURLPrefix`) with SHA-1 hash
* measure computation overhead
  * small / large image

* improvements
  * cache hash results
  * configuration
    * hash method (MD5, SHA-1, ...)
    * disable during development
    * invalidate cache at intervals (in case of dynamic resources)
  * pre-hash known/common resources during application startup
    * potentially pre-hash during build time ?
     
* limitations
  * missing option to also handle wpd/wcs bundles
  * doesn't handle container static resources (served by container's default servlet)

* Ideas? / Q & A

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
