# ⚡︎ BoxLang Module: BoxLang Compat Module

```
|:------------------------------------------------------:|
| ⚡︎ B o x L a n g ⚡︎
| Dynamic : Modular : Productive
|:------------------------------------------------------:|
```

<blockquote>
	Copyright Since 2023 by Ortus Solutions, Corp
	<br>
	<a href="https://www.boxlang.io">www.boxlang.io</a> |
	<a href="https://www.ortussolutions.com">www.ortussolutions.com</a>
</blockquote>

<p>&nbsp;</p>

## Welcome to the BoxLang Compat Module

This module will allow your ColdFusion (CFML) applications under Adobe or Lucee to run under BoxLang.  This module will provide the necessary compatibility layer to run your applications under BoxLang.

If there are any issues, please report them to the [BoxLang JIRA](https://ortussolutions.atlassian.net/browse/BL/issues) or the [Module Issues](https://github.com/ortus-boxlang/bx-compat/issues) repository.

## Settings

The module has the following available / default settings:

```json
settings = {
    isAdobe = true,
    isLucee = true
}
```

These are simple booleans that allow you to select which engine you want to mimic.  All module settings can be changed via the `boxlang.json` in your configuration.

```json
"modules" : {
    "compat" : {
        "disabled" : false,
        "settings" : {
            "isAdobe" : false,
            "isLucee" : true
        }
    }
}
```

## Server Scope Mimic

Depending on which engine you select an interceptor will be loaded that will seed the `server` scope with the appropriate engine details.

## Contributed Functions

The compat module will contribute the following functions globally:

* cacheClear
* cacheCount
* cacheGet
* cacheGetAll
* cacheGetAllIds
* cacheGetAsOptional
* cacheGetDefaultCacheName
* cacheGetEngineProperties
* cacheGetMetadata
* cacheGetOrFail
* cacheGetProperties
* cacheGetSession
* cacheIdExists
* cachePut
* cacheRegionExists
* cacheRegionNew
* cacheRegionRemove
* cacheRemove
* cacheRemoveAll
* cacheSetProperties


## Ortus Sponsors

BoxLang is a professional open-source project and it is completely funded by the [community](https://patreon.com/ortussolutions) and [Ortus Solutions, Corp](https://www.ortussolutions.com).  Ortus Patreons get many benefits like a cfcasts account, a FORGEBOX Pro account and so much more.  If you are interested in becoming a sponsor, please visits our patronage page: [https://patreon.com/ortussolutions](https://patreon.com/ortussolutions)

### THE DAILY BREAD

 > "I am the way, and the truth, and the life; no one comes to the Father, but by me (JESUS)" Jn 14:1-12
